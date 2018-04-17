package com.quanturium.bouquet.runtime.android;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.android.logging.AndroidLogger;
import com.quanturium.bouquet.runtime.components.RxComponent;
import com.quanturium.bouquet.runtime.components.WeaverComponentFactory;
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
	private static final WeaverComponentFactory weaverComponentFactory = new WeaverComponentFactory();

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

	@Pointcut(value = "execution(@com.quanturium.bouquet.annotations.RxLogger * *(..))")
	public void methodAnnotatedWithRxLogger() {
	}

	@Around(value = "methodAnnotatedWithRxLogger()")
	public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
		RxComponent rxComponent = RxComponent.fromClass(((MethodSignature) joinPoint.getSignature()).getReturnType());

		if (rxComponent == null) {
			messageManager.printWrongMethodReturnType();
			return joinPoint.proceed();
		}

		Annotation annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(RxLogger.class);
		RxLogger.Scope scope = annotation != null ? ((RxLogger) annotation).value() : RxLogger.Scope.ALL;

		return weaverComponentFactory.build(rxComponent, scope, joinPoint, messageManager).buildRx();

	}
}
