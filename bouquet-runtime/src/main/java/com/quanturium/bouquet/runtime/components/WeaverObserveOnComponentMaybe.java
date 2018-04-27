package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Maybe;

public class WeaverObserveOnComponentMaybe extends WeaverObserveOnComponentAbstract<Maybe> {

	WeaverObserveOnComponentMaybe(ProceedingJoinPoint joinPoint, WeaverComponent<Maybe> parentWeaverComponent) {
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

		getParentWeaverComponent().getComponentInfo().setObserveOnScheduler(getSchedulerName());

		return ((Maybe<T>) getJoinPoint().proceed());
	}
}
