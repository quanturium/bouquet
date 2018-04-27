package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeaverSubscribeOnComponentSingleTest {

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	WeaverComponent parentWeaverComponent;
	@Mock
	ComponentInfo componentInfo;

	private TestObserver observer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		observer = new TestObserver();
	}

	@Test
	public void build() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));
		when(parentWeaverComponent.getComponentInfo()).thenReturn(componentInfo);
		when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{Schedulers.io()});

		WeaverSubscribeOnComponentSingle weaverOnSubscribeComponentSingle = new WeaverSubscribeOnComponentSingle(proceedingJoinPoint, parentWeaverComponent);
		Single single = weaverOnSubscribeComponentSingle.build();

		single.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo).setSubscribeOnScheduler(anyString());
		verify(componentInfo).setSubscribeOnThread(anyString());
	}

	@Test
	public void buildWithoutParent() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverSubscribeOnComponentSingle weaverOnSubscribeComponentSingle = new WeaverSubscribeOnComponentSingle(proceedingJoinPoint, null);
		Single single = weaverOnSubscribeComponentSingle.build();

		single.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo, times(0)).setSubscribeOnScheduler(anyString());
		verify(componentInfo, times(0)).setSubscribeOnThread(anyString());
	}
}