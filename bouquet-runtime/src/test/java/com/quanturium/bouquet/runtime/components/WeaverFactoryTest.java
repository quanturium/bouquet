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
	public void buildWeaverSubscribeOnComponent() {
		assertTrue(weaverFactory.buildWeaverSubscribeOnComponent(ComponentType.OBSERVABLE, null) instanceof WeaverSubscribeOnComponentObservable);
		assertTrue(weaverFactory.buildWeaverSubscribeOnComponent(ComponentType.FLOWABLE, null) instanceof WeaverSubscribeOnComponentFlowable);
		assertTrue(weaverFactory.buildWeaverSubscribeOnComponent(ComponentType.SINGLE, null) instanceof WeaverSubscribeOnComponentSingle);
		assertTrue(weaverFactory.buildWeaverSubscribeOnComponent(ComponentType.MAYBE, null) instanceof WeaverSubscribeOnComponentMaybe);
		assertTrue(weaverFactory.buildWeaverSubscribeOnComponent(ComponentType.COMPLETABLE, null) instanceof WeaverSubscribeOnComponentCompletable);
	}

	@Test
	public void buildWeaverObserveOnComponent() {
		assertTrue(weaverFactory.buildWeaverObserveOnComponent(ComponentType.OBSERVABLE, null) instanceof WeaverObserveOnComponentObservable);
		assertTrue(weaverFactory.buildWeaverObserveOnComponent(ComponentType.FLOWABLE, null) instanceof WeaverObserveOnComponentFlowable);
		assertTrue(weaverFactory.buildWeaverObserveOnComponent(ComponentType.SINGLE, null) instanceof WeaverObserveOnComponentSingle);
		assertTrue(weaverFactory.buildWeaverObserveOnComponent(ComponentType.MAYBE, null) instanceof WeaverObserveOnComponentMaybe);
		assertTrue(weaverFactory.buildWeaverObserveOnComponent(ComponentType.COMPLETABLE, null) instanceof WeaverObserveOnComponentCompletable);
	}
}