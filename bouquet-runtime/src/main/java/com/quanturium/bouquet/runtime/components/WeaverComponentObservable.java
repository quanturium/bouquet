package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.Counter;
import com.quanturium.bouquet.runtime.logging.MessageManager;
import com.quanturium.bouquet.runtime.logging.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Observable;

class WeaverComponentObservable extends WeaverComponentAbstract<Observable> {

	WeaverComponentObservable(RxLogger.Scope scope, ProceedingJoinPoint proceedingJoinPoint, MessageManager messageManager) {
		super(RxComponent.OBSERVABLE, scope, proceedingJoinPoint, messageManager);
	}

	@Override
	public Observable buildRx() throws Throwable {
		return build();
	}

	@SuppressWarnings({"unchecked"})
	private <T> Observable<T> build() throws Throwable {

		Observable<T> observable = (Observable<T>) getJoinPoint().proceed();

		if (getScope() == RxLogger.Scope.NONE)
			return observable;

		if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SOURCE)
			getMessageManager().printSource(getRxComponentInfo());

		if (getScope() == RxLogger.Scope.SOURCE)
			return observable;

		final StopWatch stopWatch = new StopWatch();
		final Counter emittedItems = new Counter();

		return observable
				.doOnSubscribe(disposable -> {
					stopWatch.start();
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.SUBSCRIBE);

					if (getRxComponentInfo().observeOnThread() == null) {
						getRxComponentInfo().setObserveOnThread(Thread.currentThread().getName());
					}
				})
				.doOnNext(value -> {
					emittedItems.increment();
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.NEXT, value);
				})
				.doOnError(throwable -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.ERROR, throwable);
				})
				.doOnComplete(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.COMPLETE);
				})
				.doOnTerminate(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.TERMINATE);
				})
				.doOnDispose(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.DISPOSE);
				})
				.doFinally(() -> {
					stopWatch.stop();
					getRxComponentInfo().setTotalExecutionTime(stopWatch.getElapsedTime());
					getRxComponentInfo().setTotalEmittedItems(emittedItems.get());

					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SUMMARY)
						getMessageManager().printSummary(getRxComponentInfo());
				});
	}
}
