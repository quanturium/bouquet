package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;
import com.quanturium.bouquet.runtime.logging.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Completable;

class WeaverComponentCompletable extends WeaverComponentAbstract<Completable> {

	WeaverComponentCompletable(RxLogger.Scope scope, ProceedingJoinPoint proceedingJoinPoint, MessageManager messageManager) {
		super(RxComponent.OBSERVABLE, scope, proceedingJoinPoint, messageManager);
	}

	@Override
	public Completable buildRx() throws Throwable {
		return build();
	}

	@SuppressWarnings({"unchecked"})
	private Completable build() throws Throwable {

		Completable completable = (Completable) getJoinPoint().proceed();

		if (getScope() == RxLogger.Scope.NONE)
			return completable;

		if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SOURCE)
			getMessageManager().printSource(getRxComponentInfo());

		if (getScope() == RxLogger.Scope.SOURCE)
			return completable;

		final StopWatch stopWatch = new StopWatch();

		return completable
				.doOnSubscribe(disposable -> {
					stopWatch.start();
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.SUBSCRIBE);
				})
				.doOnComplete(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.COMPLETE);
				})
				.doOnError(throwable -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.ERROR, throwable);
				})
				.doOnDispose(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getRxComponentInfo(), RxEvent.DISPOSE);
				})
				.doFinally(() -> {
					stopWatch.stop();
					getRxComponentInfo().setTotalExecutionTime(stopWatch.getElapsedTime());
					getRxComponentInfo().setObserveOnThread(Thread.currentThread().getName());

					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SUMMARY)
						getMessageManager().printSummary(getRxComponentInfo());
				});
	}
}
