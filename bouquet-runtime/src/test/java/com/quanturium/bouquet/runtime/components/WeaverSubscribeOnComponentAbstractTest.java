package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class WeaverSubscribeOnComponentAbstractTest {

	WeaverSubscribeOnComponentAbstract<Observable> weaverComponent;

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	Observable observable;
	@Mock
	WeaverComponent parentWeaverComponent;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		weaverComponent = new WeaverSubscribeOnComponentAbstract<Observable>(proceedingJoinPoint, parentWeaverComponent) {

			@Override
			public Observable build() throws Throwable {
				return observable;
			}
		};
	}

	@Test
	public void getJoinPoint() {
		assertEquals(proceedingJoinPoint, weaverComponent.getJoinPoint());
	}

	@Test
	public void getParentWeaverComponent() {
		assertEquals(parentWeaverComponent, weaverComponent.getParentWeaverComponent());
	}

	@Test
	public void getSchedulerName() {
		when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{Schedulers.io()});

		assertEquals(RxScheduler.IO.name(), weaverComponent.getSchedulerName());
	}

	@Test
	public void getSchedulerNameWithNoArgs() {
		when(proceedingJoinPoint.getArgs()).thenReturn(null);

		assertNull(weaverComponent.getSchedulerName());
	}

	@Test
	public void getSchedulerNameWithUnknownScheduler() {
		Object scheduler = new Object();
		when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{scheduler});

		assertEquals(scheduler.getClass().getSimpleName(), weaverComponent.getSchedulerName());
	}
}