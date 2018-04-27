package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Completable;

public class WeaverObserveOnComponentCompletable extends WeaverObserveOnComponentAbstract<Completable> {

	WeaverObserveOnComponentCompletable(ProceedingJoinPoint joinPoint, WeaverComponent<Completable> parentWeaverComponent) {
		super(joinPoint, parentWeaverComponent);
	}

	@Override
	public Completable build() throws Throwable {
		return buildComponentInternal();
	}

	private Completable buildComponentInternal() throws Throwable {
		if (getParentWeaverComponent() == null)
			return (Completable) getJoinPoint().proceed();

		getParentWeaverComponent().getComponentInfo().setObserveOnScheduler(getSchedulerName());

		return ((Completable) getJoinPoint().proceed());
	}
}
