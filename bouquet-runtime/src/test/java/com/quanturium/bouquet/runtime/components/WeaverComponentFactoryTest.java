package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class WeaverComponentFactoryTest {

	WeaverComponentFactory weaverComponentFactory;

	@Before
	public void setUp() {
		weaverComponentFactory = new WeaverComponentFactory();
	}

	@Test
	public void build() {

		assertTrue(weaverComponentFactory.buildWeaverComponent(RxComponent.OBSERVABLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentObservable);
		assertTrue(weaverComponentFactory.buildWeaverComponent(RxComponent.FLOWABLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentFlowable);
		assertTrue(weaverComponentFactory.buildWeaverComponent(RxComponent.SINGLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentSingle);
		assertTrue(weaverComponentFactory.buildWeaverComponent(RxComponent.MAYBE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentMaybe);
		assertTrue(weaverComponentFactory.buildWeaverComponent(RxComponent.COMPLETABLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentCompletable);

	}
}