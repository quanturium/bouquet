package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RxComponentInfo {

	private final RxComponent rxComponent;
	private final ProceedingJoinPoint joinPoint;

	private String subscribeOnThread;
	private String observeOnThread;
	private long totalExecutionTime;
	private int totalEmittedItems;

	public RxComponentInfo(RxComponent rxComponent, ProceedingJoinPoint joinPoint) {
		this.rxComponent = rxComponent;
		this.joinPoint = joinPoint;
	}

	public RxComponent rxComponent() {
		return rxComponent;
	}

	public String classSimpleName() {
		return this.joinPoint.getSignature().getDeclaringType().getSimpleName();
	}

	public String classCanonicalName() {
		return this.joinPoint.getSignature().getDeclaringType().getCanonicalName();
	}

	public String methodName() {
		return this.joinPoint.getSignature().getName();
	}

	public List<Class> methodParamTypesList() {
		Class[] parameterTypes = ((MethodSignature) this.joinPoint.getSignature()).getParameterTypes();
		return parameterTypes != null ? Arrays.asList(parameterTypes) : Collections.emptyList();
	}

	public List<String> methodParamNamesList() {
		String[] parameterNames = ((MethodSignature) this.joinPoint.getSignature()).getParameterNames();
		return parameterNames != null ? Arrays.asList(parameterNames) : Collections.emptyList();
	}

	public List<Object> methodParamValuesList() {
		Object[] args = this.joinPoint.getArgs();
		return args != null ? Arrays.asList(args) : Collections.emptyList();
	}

	public Type methodReturnType() {
		return ((MethodSignature) this.joinPoint.getSignature()).getMethod().getGenericReturnType();
	}

	public String subscribeOnThread() {
		return subscribeOnThread;
	}

	public String observeOnThread() {
		return observeOnThread;
	}

	public long totalExecutionTime() {
		return totalExecutionTime;
	}

	public int totalEmittedItems() {
		return totalEmittedItems;
	}

	public void setSubscribeOnThread(String subscribeOnThread) {
		this.subscribeOnThread = subscribeOnThread;
	}

	public void setObserveOnThread(String observeOnThread) {
		this.observeOnThread = observeOnThread;
	}

	public void setTotalExecutionTime(long totalExecutionTime) {
		this.totalExecutionTime = totalExecutionTime;
	}

	public void setTotalEmittedItems(int totalEmittedItems) {
		this.totalEmittedItems = totalEmittedItems;
	}
}
