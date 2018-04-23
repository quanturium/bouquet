package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

abstract class WeaverOnSubscribeComponentAbstract<T> implements WeaverOnSubscribeComponent<T> {

	private final ProceedingJoinPoint joinPoint;
	private final WeaverComponent<T> parentWeaverComponent;

	WeaverOnSubscribeComponentAbstract(ProceedingJoinPoint joinPoint, WeaverComponent<T> parentWeaverComponent) {
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
}
