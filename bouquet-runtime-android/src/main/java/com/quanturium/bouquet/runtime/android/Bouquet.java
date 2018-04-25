package com.quanturium.bouquet.runtime.android;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.android.logging.AndroidLogger;
import com.quanturium.bouquet.runtime.components.ComponentType;
import com.quanturium.bouquet.runtime.components.WeaverFactory;
import com.quanturium.bouquet.runtime.logging.Logger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

@Aspect
public class Bouquet {

	private static volatile MessageManager messageManager = new MessageManager(new AndroidLogger());
	private static volatile boolean enabled = true;
	private static final WeaverFactory WEAVER_FACTORY = new WeaverFactory();

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
	 * Set a custom logger. By default it uses {@link AndroidLogger}
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
			messageManager.printWrongMethodReturnType();
			return joinPoint.proceed();
		}

		Annotation annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(RxLogger.class);
		RxLogger.Scope scope = annotation != null ? ((RxLogger) annotation).value() : RxLogger.Scope.ALL;

		return WEAVER_FACTORY.buildWeaverComponent(componentType, scope, joinPoint, messageManager).build();
	}

	@Pointcut(value = "execution(* io.reactivex.*.subscribeOn(..)) && if()")
	public static boolean methodSubscribeOn(ProceedingJoinPoint joinPoint) {
		return true;
	}

	@Around(value = "methodSubscribeOn(joinPoint)")
	public Object processSubscribeOn(ProceedingJoinPoint joinPoint) throws Throwable {
		ComponentType componentType = ComponentType.fromClass(((MethodSignature) joinPoint.getSignature()).getReturnType());

		if (componentType == null) {
			messageManager.printWrongMethodReturnType();
			return joinPoint.proceed();
		}

		return WEAVER_FACTORY.buildWeaverOnSubscribeComponent(componentType, joinPoint).build();
	}
}
