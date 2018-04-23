package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

public interface WeaverOnSubscribeComponent<T> {

	T build() throws Throwable;

	ProceedingJoinPoint getJoinPoint();

	WeaverComponent<T> getParentWeaverComponent();
}
