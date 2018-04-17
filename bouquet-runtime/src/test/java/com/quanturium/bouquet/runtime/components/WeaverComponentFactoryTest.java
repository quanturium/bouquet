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

		assertTrue(weaverComponentFactory.build(RxComponent.OBSERVABLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentObservable);
		assertTrue(weaverComponentFactory.build(RxComponent.FLOWABLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentFlowable);
		assertTrue(weaverComponentFactory.build(RxComponent.SINGLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentSingle);
		assertTrue(weaverComponentFactory.build(RxComponent.MAYBE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentMaybe);
		assertTrue(weaverComponentFactory.build(RxComponent.COMPLETABLE, RxLogger.Scope.ALL, null, null) instanceof WeaverComponentCompletable);

	}
}