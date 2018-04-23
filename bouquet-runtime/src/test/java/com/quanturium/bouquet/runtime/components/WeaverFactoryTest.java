package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WeaverFactoryTest {

	WeaverFactory weaverFactory;

	@Mock
	WeaverComponent weaverComponent;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		weaverFactory = new WeaverFactory();
	}

	@Test
	public void callback() {
		assertNull(weaverFactory.currentWeaverComponent.get());
		weaverFactory.callback.before(weaverComponent);
		assertEquals(weaverComponent, weaverFactory.currentWeaverComponent.get());
		weaverFactory.callback.after(weaverComponent);
		assertNull(weaverFactory.currentWeaverComponent.get());
	}

	@Test
	public void buildWeaverComponent() {
		assertTrue(weaverFactory.buildWeaverComponent(ComponentType.OBSERVABLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentObservable);
		assertTrue(weaverFactory.buildWeaverComponent(ComponentType.FLOWABLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentFlowable);
		assertTrue(weaverFactory.buildWeaverComponent(ComponentType.SINGLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentSingle);
		assertTrue(weaverFactory.buildWeaverComponent(ComponentType.MAYBE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentMaybe);
		assertTrue(weaverFactory.buildWeaverComponent(ComponentType.COMPLETABLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentCompletable);
	}

	@Test
	public void buildWeaverOnSubscribeComponent() {
		assertTrue(weaverFactory.buildWeaverOnSubscribeComponent(ComponentType.OBSERVABLE, null) instanceof WeaverOnSubscribeComponentObservable);
		assertTrue(weaverFactory.buildWeaverOnSubscribeComponent(ComponentType.FLOWABLE, null) instanceof WeaverOnSubscribeComponentFlowable);
		assertTrue(weaverFactory.buildWeaverOnSubscribeComponent(ComponentType.SINGLE, null) instanceof WeaverOnSubscribeComponentSingle);
		assertTrue(weaverFactory.buildWeaverOnSubscribeComponent(ComponentType.MAYBE, null) instanceof WeaverOnSubscribeComponentMaybe);
		assertTrue(weaverFactory.buildWeaverOnSubscribeComponent(ComponentType.COMPLETABLE, null) instanceof WeaverOnSubscribeComponentCompletable);
	}
}