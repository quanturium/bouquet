package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Flowable;

public class WeaverObserveOnComponentFlowable extends WeaverObserveOnComponentAbstract<Flowable> {

	WeaverObserveOnComponentFlowable(ProceedingJoinPoint joinPoint, WeaverComponent<Flowable> parentWeaverComponent) {
		super(joinPoint, parentWeaverComponent);
	}

	@Override
	public Flowable build() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Flowable<T> buildComponentInternal() throws Throwable {
		if (getParentWeaverComponent() == null)
			return (Flowable<T>) getJoinPoint().proceed();

		getParentWeaverComponent().getComponentInfo().setObserveOnScheduler(getSchedulerName());

		return ((Flowable<T>) getJoinPoint().proceed());
	}
}
