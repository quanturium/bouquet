package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeaverObserveOnComponentCompletableTest {

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
		when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{Schedulers.io()});

		WeaverObserveOnComponentCompletable weaverObserveOnComponentCompletable = new WeaverObserveOnComponentCompletable(proceedingJoinPoint, parentWeaverComponent);
		Completable completable = weaverObserveOnComponentCompletable.build();

		completable.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo).setObserveOnScheduler(anyString());
	}

	@Test
	public void buildWithoutParent() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverObserveOnComponentCompletable weaverObserveOnComponentCompletable = new WeaverObserveOnComponentCompletable(proceedingJoinPoint, null);
		Completable completable = weaverObserveOnComponentCompletable.build();

		completable.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo, times(0)).setObserveOnScheduler(anyString());
	}
}