package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Maybe;

public class WeaverSubscribeOnComponentMaybe extends WeaverSubscribeOnComponentAbstract<Maybe> {

	WeaverSubscribeOnComponentMaybe(ProceedingJoinPoint joinPoint, WeaverComponent<Maybe> parentWeaverComponent) {
		super(joinPoint, parentWeaverComponent);
	}

	@Override
	public Maybe build() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Maybe<T> buildComponentInternal() throws Throwable {
		if (getParentWeaverComponent() == null)
			return (Maybe<T>) getJoinPoint().proceed();

		if (getParentWeaverComponent().getComponentInfo().subscribeOnScheduler() == null) {
			getParentWeaverComponent().getComponentInfo().setSubscribeOnScheduler(getSchedulerName());
		}

		return ((Maybe<T>) getJoinPoint().proceed())
				.doFinally(() -> {
					if (getParentWeaverComponent().getComponentInfo().subscribeOnThread() == null)
						getParentWeaverComponent().getComponentInfo().setSubscribeOnThread(Thread.currentThread().getName());
				});
	}
}
