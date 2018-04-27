package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Single;

public class WeaverSubscribeOnComponentSingle extends WeaverSubscribeOnComponentAbstract<Single> {

	WeaverSubscribeOnComponentSingle(ProceedingJoinPoint joinPoint, WeaverComponent<Single> parentWeaverComponent) {
		super(joinPoint, parentWeaverComponent);
	}

	@Override
	public Single build() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Single<T> buildComponentInternal() throws Throwable {
		if (getParentWeaverComponent() == null)
			return (Single<T>) getJoinPoint().proceed();

		if (getParentWeaverComponent().getComponentInfo().subscribeOnScheduler() == null) {
			getParentWeaverComponent().getComponentInfo().setSubscribeOnScheduler(getSchedulerName());
		}

		return ((Single<T>) getJoinPoint().proceed())
				.doFinally(() -> {
					if (getParentWeaverComponent().getComponentInfo().subscribeOnThread() == null)
						getParentWeaverComponent().getComponentInfo().setSubscribeOnThread(Thread.currentThread().getName());
				});
	}
}
