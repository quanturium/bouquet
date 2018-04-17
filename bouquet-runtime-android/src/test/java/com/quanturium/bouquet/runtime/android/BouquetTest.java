package com.quanturium.bouquet.runtime.android;

import com.quanturium.bouquet.annotations.RxLogger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;

import java.lang.reflect.Method;

import io.reactivex.Observable;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BouquetTest {

	@Test
	public void setEnabled() {
		Bouquet.setEnabled(true);
		assertTrue(Bouquet.isEnabled());

		Bouquet.setEnabled(false);
		assertFalse(Bouquet.isEnabled());
	}

	@Test
	public void process() throws Throwable {
		ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
		MethodSignature methodSignature = mock(MethodSignature.class);
		Method testMethod = getClass().getMethod("testMethod");
		when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
		when(methodSignature.getReturnType()).thenReturn(Observable.class);
		when(methodSignature.getMethod()).thenReturn(testMethod);
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.empty());
		Bouquet bouquet = new Bouquet();
		Bouquet.setLogger((tag, message) -> {
		}); // Prevent android from failing due to missing mock on Log.d()

		assertTrue(bouquet.process(proceedingJoinPoint) instanceof Observable);
	}

	@RxLogger(RxLogger.Scope.NONE)
	public Observable testMethod() {
		return null;
	}
}