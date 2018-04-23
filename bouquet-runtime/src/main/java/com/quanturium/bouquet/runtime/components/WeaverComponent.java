package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;

public interface WeaverComponent<T> {

	T build() throws Throwable;

	ComponentType getComponentType();

	MessageManager getMessageManager();

	ComponentInfo getComponentInfo();

	ProceedingJoinPoint getJoinPoint();

	RxLogger.Scope getScope();

	interface Callback {

		void before(WeaverComponent weaverComponent);

		void after(WeaverComponent weaverComponent);

	}
}
