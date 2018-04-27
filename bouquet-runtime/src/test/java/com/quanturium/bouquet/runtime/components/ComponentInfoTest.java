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

public class ComponentInfoTest {

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	MethodSignature signature;

	ComponentType componentType;
	ComponentInfo componentInfo;

	@Before
	public void setUp() {
		componentType = ComponentType.OBSERVABLE;
		MockitoAnnotations.initMocks(this);
		when(proceedingJoinPoint.getSignature()).thenReturn(signature);
		componentInfo = new ComponentInfo(componentType, proceedingJoinPoint);
	}

	@Test
	public void type() {
		assertEquals(componentInfo.type(), ComponentType.OBSERVABLE);
	}

	@Test
	public void classSimpleName() {
		when(signature.getDeclaringType()).thenReturn(Observable.class);
		assertEquals(componentInfo.classSimpleName(), "Observable");
	}

	@Test
	public void classCanonicalName() {
		when(signature.getDeclaringType()).thenReturn(Observable.class);
		assertEquals(componentInfo.classCanonicalName(), "io.reactivex.Observable");
	}

	@Test
	public void methodName() {
		when(signature.getName()).thenReturn("methodName");
		assertEquals(componentInfo.methodName(), "methodName");
	}

	@Test
	public void methodParamTypesList() {
		Class[] classArray = new Class[]{Object.class};

		when(signature.getParameterTypes()).thenReturn(classArray).thenReturn(null);
		assertEquals(classArray[0], componentInfo.methodParamTypesList().get(0));
		assertEquals(0, componentInfo.methodParamTypesList().size());
	}

	@Test
	public void methodParamNamesList() {
		String[] nameArray = new String[]{"name"};

		when(signature.getParameterNames()).thenReturn(nameArray).thenReturn(null);
		assertEquals(nameArray[0], componentInfo.methodParamNamesList().get(0));
		assertEquals(0, componentInfo.methodParamTypesList().size());
	}

	@Test
	public void methodParamValuesList() {
		Object[] argsArray = new Object[]{"object"};

		when(proceedingJoinPoint.getArgs()).thenReturn(argsArray).thenReturn(null);
		assertEquals(argsArray[0], componentInfo.methodParamValuesList().get(0));
		assertEquals(0, componentInfo.methodParamValuesList().size());
	}

	@Test
	public void methodReturnType() throws NoSuchMethodException {
		Method testMethod = getClass().getMethod("testMethod");

		when(signature.getMethod()).thenReturn(testMethod);
		assertEquals("Observable", componentInfo.methodReturnType());
	}

	@Test
	public void subscribeOnThread() {
		String s = "threadName";
		componentInfo.setSubscribeOnThread(s);
		assertEquals(s, componentInfo.subscribeOnThread());
	}

	@Test
	public void observeOnThread() {
		String s = "threadName";
		componentInfo.setObserveOnThread(s);
		assertEquals(s, componentInfo.observeOnThread());
	}

	@Test
	public void totalExecutionTime() {
		int total = 10;
		componentInfo.setTotalExecutionTime(total);
		assertEquals(total, componentInfo.totalExecutionTime());
	}

	@Test
	public void totalEmittedItems() {
		int total = 10;
		componentInfo.setTotalEmittedItems(total);
		assertEquals(total, componentInfo.totalEmittedItems());
	}

	public Observable testMethod() {
		return null;
	}
}