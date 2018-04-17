package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeaverComponentAbstractTest {

	WeaverComponentAbstract<Observable> weaverComponent;

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	MessageManager messageManager;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		weaverComponent = new WeaverComponentAbstract<Observable>(RxComponent.OBSERVABLE, RxLogger.Scope.ALL, proceedingJoinPoint, messageManager) {

			@Override
			public Observable buildRx() throws Throwable {
				return null; // NO-OP
			}
		};
	}

	@Test
	public void getRxComponent() {
		assertEquals(RxComponent.OBSERVABLE, weaverComponent.getRxComponent());
	}

	@Test
	public void getMessageManager() {
		assertEquals(messageManager, weaverComponent.getMessageManager());
	}

	@Test
	public void getRxComponentInfo() {
		assertNotNull(weaverComponent.getRxComponentInfo());
	}

	@Test
	public void getJoinPoint() {
		assertEquals(proceedingJoinPoint, weaverComponent.getJoinPoint());
	}

	@Test
	public void getScope() {
		assertEquals(RxLogger.Scope.ALL, weaverComponent.getScope());
	}
}