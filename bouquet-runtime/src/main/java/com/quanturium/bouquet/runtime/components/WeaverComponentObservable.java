package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.Counter;
import com.quanturium.bouquet.runtime.logging.MessageManager;
import com.quanturium.bouquet.runtime.logging.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Observable;

class WeaverComponentObservable extends WeaverComponentAbstract<Observable> {

	WeaverComponentObservable(RxLogger.Scope scope, ProceedingJoinPoint proceedingJoinPoint, MessageManager messageManager, Callback callback) {
		super(ComponentType.OBSERVABLE, scope, proceedingJoinPoint, messageManager, callback);
	}

	@Override
	public Observable buildComponent() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Observable<T> buildComponentInternal() throws Throwable {

		Observable<T> observable = (Observable<T>) getJoinPoint().proceed();

		if (getScope() == RxLogger.Scope.NONE)
			return observable;

		if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SOURCE)
			getMessageManager().printSource(getComponentInfo());

		if (getScope() == RxLogger.Scope.SOURCE)
			return observable;

		final StopWatch stopWatch = new StopWatch();
		final Counter emittedItems = new Counter();

		return observable
				.doOnSubscribe(disposable -> {
					stopWatch.start();
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.SUBSCRIBE);
				})
				.doOnNext(value -> {
					emittedItems.increment();
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.NEXT, value);
				})
				.doOnError(throwable -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.ERROR, throwable);
				})
				.doOnComplete(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.COMPLETE);
				})
				.doOnTerminate(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.TERMINATE);
				})
				.doOnDispose(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.DISPOSE);
				})
				.doFinally(() -> {
					stopWatch.stop();
					getComponentInfo().setTotalExecutionTime(stopWatch.getElapsedTime());
					getComponentInfo().setTotalEmittedItems(emittedItems.get());
					getComponentInfo().setObserveOnThread(Thread.currentThread().getName());
					if(getComponentInfo().subscribeOnThread() == null)
						getComponentInfo().setSubscribeOnThread(Thread.currentThread().getName());

					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SUMMARY)
						getMessageManager().printSummary(getComponentInfo());
				});
	}
}
