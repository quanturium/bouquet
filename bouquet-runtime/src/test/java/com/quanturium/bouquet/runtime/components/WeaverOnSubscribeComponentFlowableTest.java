package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WeaverOnSubscribeComponentFlowableTest {

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

		WeaverOnSubscribeComponentFlowable weaverOnSubscribeComponentFlowable = new WeaverOnSubscribeComponentFlowable(proceedingJoinPoint, parentWeaverComponent);
		Flowable flowable = weaverOnSubscribeComponentFlowable.build();

		flowable.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo).setSubscribeOnThread(anyString());
	}

	@Test
	public void buildWithoutParent() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverOnSubscribeComponentFlowable weaverOnSubscribeComponentFlowable = new WeaverOnSubscribeComponentFlowable(proceedingJoinPoint, null);
		Flowable flowable = weaverOnSubscribeComponentFlowable.build();

		flowable.subscribe(observer);
		observer.dispose();

		observer.assertComplete();
		verify(componentInfo, times(0)).setSubscribeOnThread(anyString());
	}
}