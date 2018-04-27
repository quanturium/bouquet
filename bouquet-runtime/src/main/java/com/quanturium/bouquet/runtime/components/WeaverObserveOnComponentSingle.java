package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Single;

public class WeaverObserveOnComponentSingle extends WeaverObserveOnComponentAbstract<Single> {

	WeaverObserveOnComponentSingle(ProceedingJoinPoint joinPoint, WeaverComponent<Single> parentWeaverComponent) {
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

		getParentWeaverComponent().getComponentInfo().setObserveOnScheduler(getSchedulerName());

		return ((Single<T>) getJoinPoint().proceed());
	}
}
