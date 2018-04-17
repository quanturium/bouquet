package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.Counter;
import com.quanturium.bouquet.runtime.logging.MessageManager;
import com.quanturium.bouquet.runtime.logging.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Maybe;

class WeaverComponentMaybe extends WeaverComponentAbstract<Maybe> {

	WeaverComponentMaybe(RxLogger.Scope scope, ProceedingJoinPoint proceedingJoinPoint, MessageManager messageManager) {
		super(RxComponent.OBSERVABLE, scope, proceedingJoinPoint, messageManager);
	}

	@Override
	public Maybe buildRx() throws Throwable {
		return build();
	}

	@SuppressWarnings({"unchecked"})
	private <T> Maybe<T> build() throws Throwable {

		Maybe<T> maybe = (Maybe<T>) getJoinPoint().proceed();

		if (getScope() == RxLogger.Scope.NONE)
			return maybe;

		if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SOURCE)
			getMessageManager().printSource(getRxComponentInfo());

		if (getScope() == RxLogger.Scope.SOURCE)
			return maybe;

		final StopWatch stopWatch = new StopWatch();
		final Counter emittedItems = new Counter();

		return maybe
				.doOnSubscribe(disposable -> {
					stopWatch.start();
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.SUBSCRIBE);

					if (getRxComponentInfo().observeOnThread() == null) {
						getRxComponentInfo().setObserveOnThread(Thread.currentThread().getName());
					}
				})
				.doOnEvent((value, throwable) -> {
					if (value != null)
						getRxComponentInfo().setTotalEmittedItems(1);
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE) {
						if (value != null) { // success
							getMessageManager().printEvent(getRxComponentInfo(), RxEvent.SUCCESS, value);
						} else if (throwable != null) { // error
							getMessageManager().printEvent(getRxComponentInfo(), RxEvent.ERROR, throwable);
						} else { // complete without emitting any items
							getMessageManager().printEvent(getRxComponentInfo(), RxEvent.COMPLETE);
						}
					}
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
