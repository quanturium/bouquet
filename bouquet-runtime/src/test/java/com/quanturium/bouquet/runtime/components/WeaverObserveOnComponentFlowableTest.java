package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeaverObserveOnComponentFlowableTest {

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	WeaverComponent parentWeaverComponent;
	@Mock
	ComponentInfo componentInfo;

	private TestSubscriber observer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		observer = new TestSubscriber();
	}

	@Test
	public void build() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));
		when(parentWeaverComponent.getComponentInfo()).thenReturn(componentInfo);
		when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{Schedulers.io()});

		WeaverObserveOnComponentFlowable weaverObserveOnComponentFlowable = new WeaverObserveOnComponentFlowable(proceedingJoinPoint, parentWeaverComponent);
		Flowable flowable = weaverObserveOnComponentFlowable.build();

		flowable.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo).setObserveOnScheduler(anyString());
	}

	@Test
	public void buildWithoutParent() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverObserveOnComponentFlowable weaverObserveOnComponentFlowable = new WeaverObserveOnComponentFlowable(proceedingJoinPoint, null);
		Flowable flowable = weaverObserveOnComponentFlowable.build();

		flowable.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo, times(0)).setObserveOnScheduler(anyString());
	}
}