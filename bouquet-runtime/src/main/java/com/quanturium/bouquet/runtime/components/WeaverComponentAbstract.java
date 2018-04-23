package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;

abstract class WeaverComponentAbstract<T> implements WeaverComponent<T> {

	private final ProceedingJoinPoint joinPoint;
	private final ComponentInfo componentInfo;
	private final MessageManager messageManager;
	private final ComponentType componentType;
	private final RxLogger.Scope scope;
	private final Callback callback;

	WeaverComponentAbstract(ComponentType componentType, RxLogger.Scope scope, ProceedingJoinPoint joinPoint, MessageManager messageManager, Callback callback) {
		this.componentType = componentType;
		this.scope = scope;
		this.callback = callback;
		this.componentInfo = new ComponentInfo(componentType, joinPoint);
		this.joinPoint = joinPoint;
		this.messageManager = messageManager;
	}

	@Override
	public T build() throws Throwable {
		callback.before(this);
		T result = buildComponent();
		callback.after(this);
		return result;
	}

	protected abstract T buildComponent() throws Throwable;

	@Override
	public ComponentType getComponentType() {
		return componentType;
	}

	@Override
	public MessageManager getMessageManager() {
		return messageManager;
	}

	@Override
	public ComponentInfo getComponentInfo() {
		return componentInfo;
	}

	@Override
	public ProceedingJoinPoint getJoinPoint() {
		return joinPoint;
	}

	@Override
	public RxLogger.Scope getScope() {
		return scope;
	}
}
