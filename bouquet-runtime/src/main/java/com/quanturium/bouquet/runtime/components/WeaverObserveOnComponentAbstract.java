package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

abstract class WeaverObserveOnComponentAbstract<T> implements WeaverObserveOnComponent<T> {

	private final ProceedingJoinPoint joinPoint;
	private final WeaverComponent<T> parentWeaverComponent;

	WeaverObserveOnComponentAbstract(ProceedingJoinPoint joinPoint, WeaverComponent<T> parentWeaverComponent) {
		this.joinPoint = joinPoint;
		this.parentWeaverComponent = parentWeaverComponent;
	}

	@Override
	public ProceedingJoinPoint getJoinPoint() {
		return joinPoint;
	}

	@Override
	public WeaverComponent<T> getParentWeaverComponent() {
		return parentWeaverComponent;
	}

	protected String getSchedulerName() {
		Object[] args = getJoinPoint().getArgs();
		if (args == null || args.length == 0)
			return null;

		RxScheduler rxScheduler = RxScheduler.fromClass(args[0].getClass());
		return rxScheduler != null ? rxScheduler.name() : args[0].getClass().getSimpleName();
	}
}
