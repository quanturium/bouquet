package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import io.reactivex.Observable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class RxComponentInfoTest {

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	MethodSignature signature;

	RxComponent rxComponent;
	RxComponentInfo rxComponentInfo;

	@Before
	public void setUp() {
		rxComponent = RxComponent.OBSERVABLE;
		MockitoAnnotations.initMocks(this);
		when(proceedingJoinPoint.getSignature()).thenReturn(signature);
		rxComponentInfo = new RxComponentInfo(rxComponent, proceedingJoinPoint);
	}

	@Test
	public void rxComponentName() {
		assertEquals(rxComponentInfo.rxComponent(), RxComponent.OBSERVABLE);
	}

	@Test
	public void classSimpleName() {
		when(signature.getDeclaringType()).thenReturn(Observable.class);
		assertEquals(rxComponentInfo.classSimpleName(), "Observable");
	}

	@Test
	public void classCanonicalName() {
		when(signature.getDeclaringType()).thenReturn(Observable.class);
		assertEquals(rxComponentInfo.classCanonicalName(), "io.reactivex.Observable");
	}

	@Test
	public void methodName() {
		when(signature.getName()).thenReturn("methodName");
		assertEquals(rxComponentInfo.methodName(), "methodName");
	}

	@Test
	public void methodParamTypesList() {
		Class[] classArray = new Class[]{Object.class};

		when(signature.getParameterTypes()).thenReturn(classArray).thenReturn(null);
		assertEquals(classArray[0], rxComponentInfo.methodParamTypesList().get(0));
		assertEquals(0, rxComponentInfo.methodParamTypesList().size());
	}

	@Test
	public void methodParamNamesList() {
		String[] nameArray = new String[]{"name"};

		when(signature.getParameterNames()).thenReturn(nameArray).thenReturn(null);
		assertEquals(nameArray[0], rxComponentInfo.methodParamNamesList().get(0));
		assertEquals(0, rxComponentInfo.methodParamTypesList().size());
	}

	@Test
	public void methodParamValuesList() {
		Object[] argsArray = new Object[]{"object"};

		when(proceedingJoinPoint.getArgs()).thenReturn(argsArray).thenReturn(null);
		assertEquals(argsArray[0], rxComponentInfo.methodParamValuesList().get(0));
		assertEquals(0, rxComponentInfo.methodParamValuesList().size());
	}

	@Test
	public void methodReturnType() throws NoSuchMethodException {
		Method testMethod = getClass().getMethod("testMethod");

		when(signature.getMethod()).thenReturn(testMethod);
		assertEquals("io.reactivex.Observable", rxComponentInfo.methodReturnType().getTypeName());
	}

	@Test
	public void subscribeOnThread() {
		String s = "threadName";
		rxComponentInfo.setSubscribeOnThread(s);
		assertEquals(s, rxComponentInfo.subscribeOnThread());
	}

	@Test
	public void observeOnThread() {
		String s = "threadName";
		rxComponentInfo.setObserveOnThread(s);
		assertEquals(s, rxComponentInfo.observeOnThread());
	}

	@Test
	public void totalExecutionTime() {
		int total = 10;
		rxComponentInfo.setTotalExecutionTime(total);
		assertEquals(total, rxComponentInfo.totalExecutionTime());
	}

	@Test
	public void totalEmittedItems() {
		int total = 10;
		rxComponentInfo.setTotalEmittedItems(total);
		assertEquals(total, rxComponentInfo.totalEmittedItems());
	}

	public Observable testMethod() {
		return null;
	}
}