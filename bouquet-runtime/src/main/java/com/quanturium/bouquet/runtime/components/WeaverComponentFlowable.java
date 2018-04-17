package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.Counter;
import com.quanturium.bouquet.runtime.logging.MessageManager;
import com.quanturium.bouquet.runtime.logging.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Flowable;

class WeaverComponentFlowable extends WeaverComponentAbstract<Flowable> {

	WeaverComponentFlowable(RxLogger.Scope scope, ProceedingJoinPoint proceedingJoinPoint, MessageManager messageManager) {
		super(RxComponent.OBSERVABLE, scope, proceedingJoinPoint, messageManager);
	}

	@Override
	public Flowable buildRx() throws Throwable {
		return build();
	}

	@SuppressWarnings({"unchecked"})
	private <T> Flowable<T> build() throws Throwable {

		Flowable<T> flowable = (Flowable<T>) getJoinPoint().proceed();

		if (getScope() == RxLogger.Scope.NONE)
			return flowable;

		if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SOURCE)
			getMessageManager().printSource(getRxComponentInfo());

		if (getScope() == RxLogger.Scope.SOURCE)
			return flowable;

		final StopWatch stopWatch = new StopWatch();
		final Counter emittedItems = new Counter();

		return flowable
				.doOnSubscribe(subscription -> {
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
				.doOnRequest(count -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.REQUEST, count);
				})
				.doOnCancel(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.CANCEL);
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
