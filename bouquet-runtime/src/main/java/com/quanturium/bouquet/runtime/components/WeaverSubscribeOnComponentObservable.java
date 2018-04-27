package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Observable;

public class WeaverSubscribeOnComponentObservable extends WeaverSubscribeOnComponentAbstract<Observable> {

	WeaverSubscribeOnComponentObservable(ProceedingJoinPoint joinPoint, WeaverComponent<Observable> parentWeaverComponent) {
		super(joinPoint, parentWeaverComponent);
	}

	@Override
	public Observable build() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Observable<T> buildComponentInternal() throws Throwable {
		if (getParentWeaverComponent() == null)
			return (Observable<T>) getJoinPoint().proceed();

		if (getParentWeaverComponent().getComponentInfo().subscribeOnScheduler() == null) {
			getParentWeaverComponent().getComponentInfo().setSubscribeOnScheduler(getSchedulerName());
		}

		return ((Observable<T>) getJoinPoint().proceed())
				.doFinally(() -> {
					if (getParentWeaverComponent().getComponentInfo().subscribeOnThread() == null)
						getParentWeaverComponent().getComponentInfo().setSubscribeOnThread(Thread.currentThread().getName());
				});
	}
}
