package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ComponentInfo {

	private final ComponentType componentType;
	private final ProceedingJoinPoint joinPoint;

	private long totalExecutionTime;
	private int totalEmittedItems;
	private String subscribeOnScheduler;
	private String subscribeOnThread;
	private String observeOnScheduler;
	private String observeOnThread;

	public ComponentInfo(ComponentType componentType, ProceedingJoinPoint joinPoint) {
		this.componentType = componentType;
		this.joinPoint = joinPoint;
	}

	public ComponentType type() {
		return componentType;
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

	public String methodReturnType() {
		StringBuilder sb = new StringBuilder();
		methodReturnTypeInternal(((MethodSignature) this.joinPoint.getSignature()).getMethod().getGenericReturnType(), sb);
		return sb.toString();
	}

	private void methodReturnTypeInternal(Type type, StringBuilder sb) {
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type rawType = parameterizedType.getRawType();
			Type[] typeArguments = parameterizedType.getActualTypeArguments();
			sb.append(typeSimpleName(rawType));
			if (typeArguments != null && typeArguments.length > 0) {
				sb.append("<");
				for (int i = 0; i < typeArguments.length; i++) {
					methodReturnTypeInternal(typeArguments[i], sb);
				}
				sb.append(">");
			}
		} else {
			sb.append(typeSimpleName(type));
		}
	}

	private String typeSimpleName(Type type) {
		if (type instanceof Class) {
			return ((Class) type).getSimpleName();
		} else {
			return type.getTypeName();
		}
	}

	public long totalExecutionTime() {
		return totalExecutionTime;
	}

	public void setTotalExecutionTime(long totalExecutionTime) {
		this.totalExecutionTime = totalExecutionTime;
	}

	public int totalEmittedItems() {
		return totalEmittedItems;
	}

	public void setTotalEmittedItems(int totalEmittedItems) {
		this.totalEmittedItems = totalEmittedItems;
	}

	public boolean isSynchronous() {
		return subscribeOnScheduler == null && observeOnScheduler == null;
	}

	public String subscribeOnScheduler() {
		return this.subscribeOnScheduler;
	}

	public void setSubscribeOnScheduler(String schedulerName) {
		this.subscribeOnScheduler = schedulerName;
	}

	public String subscribeOnThread() {
		return subscribeOnThread;
	}

	public void setSubscribeOnThread(String subscribeOnThread) {
		this.subscribeOnThread = subscribeOnThread;
	}

	public String observeOnScheduler() {
		return this.observeOnScheduler;
	}

	public void setObserveOnScheduler(String schedulerName) {
		this.observeOnScheduler = schedulerName;
	}

	public String observeOnThread() {
		return observeOnThread;
	}

	public void setObserveOnThread(String observeOnThread) {
		this.observeOnThread = observeOnThread;
	}
}
