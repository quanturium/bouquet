package com.quanturium.bouquet.runtime.java;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.components.ComponentType;
import com.quanturium.bouquet.runtime.components.WeaverFactory;
import com.quanturium.bouquet.runtime.java.logging.JavaLogger;
import com.quanturium.bouquet.runtime.logging.Logger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

@Aspect
public final class Bouquet {

	private static volatile MessageManager messageManager = new MessageManager(new JavaLogger());
	private static volatile boolean enabled = true;
	private static final WeaverFactory weaverFactory = new WeaverFactory();

	/**
	 * Enable or disable Bouquet
	 *
	 * @param enabled true of false
	 */
	public static void setEnabled(boolean enabled) {
		Bouquet.enabled = enabled;
	}

	/**
	 * @return Whether Bouquet is enabled
	 */
	public static boolean isEnabled() {
		return enabled;
	}

	/**
	 * Set a custom logger. By default it uses {@link JavaLogger}
	 *
	 * @param logger the new logger implementation
	 */
	public static void setLogger(Logger logger) {
		Bouquet.messageManager = new MessageManager(logger);
	}

	@Pointcut(value = "execution(@com.quanturium.bouquet.annotations.RxLogger * *(..)) && if()")
	public static boolean methodAnnotatedWithRxLogger(ProceedingJoinPoint joinPoint) {
		ComponentType componentType = ComponentType.fromClass(((MethodSignature) joinPoint.getSignature()).getReturnType());

		if (componentType == null)
			return false;

		Annotation annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(RxLogger.class);
		RxLogger.Scope scope = annotation != null ? ((RxLogger) annotation).value() : RxLogger.Scope.ALL;
		return scope != RxLogger.Scope.NONE;
	}

	@Around(value = "methodAnnotatedWithRxLogger(joinPoint)")
	public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
		ComponentType componentType = ComponentType.fromClass(((MethodSignature) joinPoint.getSignature()).getReturnType());

		if (componentType == null) {
			Bouquet.messageManager.printWrongMethodReturnType();
			return joinPoint.proceed();
		}

		Annotation annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(RxLogger.class);
		RxLogger.Scope scope = annotation != null ? ((RxLogger) annotation).value() : RxLogger.Scope.ALL;

		return Bouquet.weaverFactory.buildWeaverComponent(componentType, scope, joinPoint, Bouquet.messageManager).build();
	}

	@Pointcut(value = "execution(* io.reactivex.*.subscribeOn(..)) && if()")
	public static boolean methodSubscribeOn(ProceedingJoinPoint joinPoint) {
		return true;
	}

	@Around(value = "methodSubscribeOn(joinPoint)")
	public Object processSubscribeOn(ProceedingJoinPoint joinPoint) throws Throwable {
		ComponentType componentType = ComponentType.fromClass(((MethodSignature) joinPoint.getSignature()).getReturnType());

		if (componentType == null) {
			return joinPoint.proceed();
		}

		return Bouquet.weaverFactory.buildWeaverSubscribeOnComponent(componentType, joinPoint).build();
	}

	@Pointcut(value = "execution(* io.reactivex.*.observeOn(..)) && if()")
	public static boolean methodObserveOn(ProceedingJoinPoint joinPoint) {
		return true;
	}

	@Around(value = "methodObserveOn(joinPoint)")
	public Object processObserveOn(ProceedingJoinPoint joinPoint) throws Throwable {
		ComponentType componentType = ComponentType.fromClass(((MethodSignature) joinPoint.getSignature()).getReturnType());

		if (componentType == null) {
			return joinPoint.proceed();
		}

		return Bouquet.weaverFactory.buildWeaverObserveOnComponent(componentType, joinPoint).build();
	}
}
