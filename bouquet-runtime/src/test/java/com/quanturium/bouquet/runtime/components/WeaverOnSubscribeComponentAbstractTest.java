package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;

import static org.junit.Assert.assertEquals;

public class WeaverOnSubscribeComponentAbstractTest {

	WeaverOnSubscribeComponentAbstract<Observable> weaverComponent;

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	MessageManager messageManager;
	@Mock
	WeaverComponent.Callback callback;
	@Mock
	Observable observable;
	@Mock
	WeaverComponent parentWeaverComponent;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		weaverComponent = new WeaverOnSubscribeComponentAbstract<Observable>(proceedingJoinPoint, parentWeaverComponent) {

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
}