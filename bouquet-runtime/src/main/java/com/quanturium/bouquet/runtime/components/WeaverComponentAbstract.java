package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;

abstract class WeaverComponentAbstract<T> implements WeaverComponent<T> {

	private final ProceedingJoinPoint joinPoint;
	private final RxComponentInfo rxComponentInfo;
	private final MessageManager messageManager;
	private final RxComponent rxComponent;
	private final RxLogger.Scope scope;

	WeaverComponentAbstract(RxComponent rxComponent, RxLogger.Scope scope, ProceedingJoinPoint joinPoint, MessageManager messageManager) {
		this.rxComponent = rxComponent;
		this.scope = scope;
		this.rxComponentInfo = new RxComponentInfo(rxComponent, joinPoint);
		this.joinPoint = joinPoint;
		this.messageManager = messageManager;
	}

	@Override
	public RxComponent getRxComponent() {
		return rxComponent;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public RxComponentInfo getRxComponentInfo() {
		return rxComponentInfo;
	}

	public ProceedingJoinPoint getJoinPoint() {
		return joinPoint;
	}

	public RxLogger.Scope getScope() {
		return scope;
	}
}
