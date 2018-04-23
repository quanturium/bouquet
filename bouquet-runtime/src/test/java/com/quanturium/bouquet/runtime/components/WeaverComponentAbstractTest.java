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
import static org.mockito.Mockito.verify;

public class WeaverComponentAbstractTest {

	WeaverComponentAbstract<Observable> weaverComponent;

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	MessageManager messageManager;
	@Mock
	WeaverComponent.Callback callback;
	@Mock
	Observable observable;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		weaverComponent = new WeaverComponentAbstract<Observable>(ComponentType.OBSERVABLE, RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback) {

			@Override
			protected Observable buildComponent() throws Throwable {
				return observable;
			}
		};
	}

	@Test
	public void getRxComponent() {
		assertEquals(ComponentType.OBSERVABLE, weaverComponent.getComponentType());
	}

	@Test
	public void getMessageManager() {
		assertEquals(messageManager, weaverComponent.getMessageManager());
	}

	@Test
	public void getRxComponentInfo() {
		assertNotNull(weaverComponent.getComponentInfo());
	}

	@Test
	public void getJoinPoint() {
		assertEquals(proceedingJoinPoint, weaverComponent.getJoinPoint());
	}

	@Test
	public void getScope() {
		assertEquals(RxLogger.Scope.ALL, weaverComponent.getScope());
	}

	@Test
	public void build() throws Throwable {
		Observable result = weaverComponent.build();

		verify(callback).before(weaverComponent);
		verify(callback).after(weaverComponent);
		assertEquals(observable, result);
	}

	@Test
	public void buildComponent() throws Throwable {
		assertEquals(observable, weaverComponent.buildComponent());
	}
}