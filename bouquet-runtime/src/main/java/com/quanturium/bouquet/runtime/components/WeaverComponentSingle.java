package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.Counter;
import com.quanturium.bouquet.runtime.logging.MessageManager;
import com.quanturium.bouquet.runtime.logging.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Single;

class WeaverComponentSingle extends WeaverComponentAbstract<Single> {

	WeaverComponentSingle(RxLogger.Scope scope, ProceedingJoinPoint proceedingJoinPoint, MessageManager messageManager) {
		super(RxComponent.OBSERVABLE, scope, proceedingJoinPoint, messageManager);
	}

	@Override
	public Single buildRx() throws Throwable {
		return build();
	}

	@SuppressWarnings({"unchecked"})
	private <T> Single<T> build() throws Throwable {

		Single<T> single = (Single<T>) getJoinPoint().proceed();

		if (getScope() == RxLogger.Scope.NONE)
			return single;

		if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SOURCE)
			getMessageManager().printSource(getRxComponentInfo());

		if (getScope() == RxLogger.Scope.SOURCE)
			return single;

		final StopWatch stopWatch = new StopWatch();
		final Counter emittedItems = new Counter();

		return single
				.doOnSubscribe(disposable -> {
					stopWatch.start();
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.SUBSCRIBE);
				})
				.doOnEvent((value, throwable) -> {
					if (value != null)
						getRxComponentInfo().setTotalEmittedItems(1);

					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE) {
						if (value != null) { // success
							getMessageManager().printEvent(getRxComponentInfo(), RxEvent.SUCCESS, value);
						} else { // error
							getMessageManager().printEvent(getRxComponentInfo(), RxEvent.ERROR, throwable);
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
					getRxComponentInfo().setObserveOnThread(Thread.currentThread().getName());

					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SUMMARY)
						getMessageManager().printSummary(getRxComponentInfo());
				});
	}
}
