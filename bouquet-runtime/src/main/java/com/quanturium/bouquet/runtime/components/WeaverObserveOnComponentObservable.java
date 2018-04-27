package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Observable;

public class WeaverObserveOnComponentObservable extends WeaverObserveOnComponentAbstract<Observable> {

	WeaverObserveOnComponentObservable(ProceedingJoinPoint joinPoint, WeaverComponent<Observable> parentWeaverComponent) {
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

		getParentWeaverComponent().getComponentInfo().setObserveOnScheduler(getSchedulerName());

		return ((Observable<T>) getJoinPoint().proceed());
	}
}
