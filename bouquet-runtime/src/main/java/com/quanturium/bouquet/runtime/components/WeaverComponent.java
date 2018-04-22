package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;

public interface WeaverComponent<T> {

	T buildRx() throws Throwable;

	RxComponent getRxComponent();

	MessageManager getMessageManager();

	RxComponentInfo getRxComponentInfo();

	ProceedingJoinPoint getJoinPoint();

	RxLogger.Scope getScope();
}
