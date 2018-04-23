package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeaverOnSubscribeComponentCompletableTest {

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
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());
		when(parentWeaverComponent.getComponentInfo()).thenReturn(componentInfo);

		WeaverOnSubscribeComponentCompletable weaverOnSubscribeComponentCompletable = new WeaverOnSubscribeComponentCompletable(proceedingJoinPoint, parentWeaverComponent);
		Completable completable = weaverOnSubscribeComponentCompletable.build();

		completable.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo).setSubscribeOnThread(anyString());
	}

	@Test
	public void buildWithoutParent() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverOnSubscribeComponentCompletable weaverOnSubscribeComponentCompletable = new WeaverOnSubscribeComponentCompletable(proceedingJoinPoint, null);
		Completable completable = weaverOnSubscribeComponentCompletable.build();

		completable.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo, times(0)).setSubscribeOnThread(anyString());
	}
}