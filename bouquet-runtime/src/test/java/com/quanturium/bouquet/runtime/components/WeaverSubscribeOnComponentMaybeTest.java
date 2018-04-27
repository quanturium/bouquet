package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeaverSubscribeOnComponentMaybeTest {

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
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));
		when(parentWeaverComponent.getComponentInfo()).thenReturn(componentInfo);
		when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{Schedulers.io()});

		WeaverSubscribeOnComponentMaybe weaverOnSubscribeComponentMaybe = new WeaverSubscribeOnComponentMaybe(proceedingJoinPoint, parentWeaverComponent);
		Maybe maybe = weaverOnSubscribeComponentMaybe.build();

		maybe.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo).setSubscribeOnScheduler(anyString());
		verify(componentInfo).setSubscribeOnThread(anyString());
	}

	@Test
	public void buildWithoutParent() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverSubscribeOnComponentMaybe weaverOnSubscribeComponentMaybe = new WeaverSubscribeOnComponentMaybe(proceedingJoinPoint, null);
		Maybe maybe = weaverOnSubscribeComponentMaybe.build();

		maybe.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo, times(0)).setSubscribeOnScheduler(anyString());
		verify(componentInfo, times(0)).setSubscribeOnThread(anyString());
	}
}