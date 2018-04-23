package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.Counter;
import com.quanturium.bouquet.runtime.logging.MessageManager;
import com.quanturium.bouquet.runtime.logging.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Single;

class WeaverComponentSingle extends WeaverComponentAbstract<Single> {

	WeaverComponentSingle(RxLogger.Scope scope, ProceedingJoinPoint proceedingJoinPoint, MessageManager messageManager, Callback callback) {
		super(ComponentType.OBSERVABLE, scope, proceedingJoinPoint, messageManager, callback);
	}

	@Override
	protected Single buildComponent() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Single<T> buildComponentInternal() throws Throwable {

		Single<T> single = (Single<T>) getJoinPoint().proceed();

		if (getScope() == RxLogger.Scope.NONE)
			return single;

		if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SOURCE)
			getMessageManager().printSource(getComponentInfo());

		if (getScope() == RxLogger.Scope.SOURCE)
			return single;

		final StopWatch stopWatch = new StopWatch();
		final Counter emittedItems = new Counter();

		return single
				.doOnSubscribe(disposable -> {
					stopWatch.start();
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.SUBSCRIBE);
				})
				.doOnEvent((value, throwable) -> {
					if (value != null)
						emittedItems.increment();

					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE) {
						if (value != null) { // success
							getMessageManager().printEvent(getComponentInfo(), RxEvent.SUCCESS, value);
						} else { // error
							getMessageManager().printEvent(getComponentInfo(), RxEvent.ERROR, throwable);
						}
					}
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
