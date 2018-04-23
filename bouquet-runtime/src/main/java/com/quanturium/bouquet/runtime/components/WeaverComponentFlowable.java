package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.Counter;
import com.quanturium.bouquet.runtime.logging.MessageManager;
import com.quanturium.bouquet.runtime.logging.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Flowable;

class WeaverComponentFlowable extends WeaverComponentAbstract<Flowable> {

	WeaverComponentFlowable(RxLogger.Scope scope, ProceedingJoinPoint proceedingJoinPoint, MessageManager messageManager, Callback callback) {
		super(ComponentType.OBSERVABLE, scope, proceedingJoinPoint, messageManager, callback);
	}

	@Override
	public Flowable buildComponent() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Flowable<T> buildComponentInternal() throws Throwable {

		Flowable<T> flowable = (Flowable<T>) getJoinPoint().proceed();

		if (getScope() == RxLogger.Scope.NONE)
			return flowable;

		if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SOURCE)
			getMessageManager().printSource(getComponentInfo());

		if (getScope() == RxLogger.Scope.SOURCE)
			return flowable;

		final StopWatch stopWatch = new StopWatch();
		final Counter emittedItems = new Counter();

		return flowable
				.doOnSubscribe(subscription -> {
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
				.doOnRequest(count -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.REQUEST, count);
				})
				.doOnCancel(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.CANCEL);
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
