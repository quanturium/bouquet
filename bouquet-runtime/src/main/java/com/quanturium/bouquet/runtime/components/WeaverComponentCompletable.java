package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;
import com.quanturium.bouquet.runtime.logging.StopWatch;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Completable;

class WeaverComponentCompletable extends WeaverComponentAbstract<Completable> {

	WeaverComponentCompletable(RxLogger.Scope scope, ProceedingJoinPoint proceedingJoinPoint, MessageManager messageManager, Callback callback) {
		super(ComponentType.OBSERVABLE, scope, proceedingJoinPoint, messageManager, callback);
	}

	@Override
	protected Completable buildComponent() throws Throwable {
		return buildComponentInternal();
	}

	private Completable buildComponentInternal() throws Throwable {

		Completable completable = (Completable) getJoinPoint().proceed();

		if (getScope() == RxLogger.Scope.NONE)
			return completable;

		if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SOURCE)
			getMessageManager().printSource(getComponentInfo());

		if (getScope() == RxLogger.Scope.SOURCE)
			return completable;

		final StopWatch stopWatch = new StopWatch();

		return completable
				.doOnSubscribe(disposable -> {
					stopWatch.start();
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.SUBSCRIBE);
				})
				.doOnEvent(throwable -> {
					if(throwable == null) { // complete
						if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
							getMessageManager().printEvent(getComponentInfo(), RxEvent.COMPLETE);
					} else { // error
						if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
							getMessageManager().printEvent(getComponentInfo(), RxEvent.ERROR, throwable);
					}
				})
				.doOnDispose(() -> {
					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.LIFECYCLE)
						getMessageManager().printEvent(getComponentInfo(), RxEvent.DISPOSE);
				})
				.doFinally(() -> {
					stopWatch.stop();
					getComponentInfo().setTotalExecutionTime(stopWatch.getElapsedTime());
					getComponentInfo().setObserveOnThread(Thread.currentThread().getName());
					if (getComponentInfo().subscribeOnThread() == null)
						getComponentInfo().setSubscribeOnThread(Thread.currentThread().getName());

					if (getScope() == RxLogger.Scope.ALL || getScope() == RxLogger.Scope.SUMMARY)
						getMessageManager().printSummary(getComponentInfo());
				});
	}
}
