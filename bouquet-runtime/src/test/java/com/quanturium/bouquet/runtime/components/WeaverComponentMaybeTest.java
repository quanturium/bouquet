package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeaverComponentMaybeTest {

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	MessageManager messageManager;
	@Mock
	WeaverComponent.Callback callback;

	private TestObserver observer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		observer = new TestObserver();
	}

	@Test
	public void build() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1).delay(10, TimeUnit.MILLISECONDS));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback);
		Maybe maybe = weaverComponentMaybe.build();

		maybe.subscribe(observer);
		observer.awaitTerminalEvent();
		observer.dispose();

		observer.assertValue(1);
		observer.assertNoErrors();
		observer.assertComplete();

		assertNotNull(weaverComponentMaybe.getComponentInfo().observeOnThread());
		assertNotNull(weaverComponentMaybe.getComponentInfo().subscribeOnThread());
		assertTrue(weaverComponentMaybe.getComponentInfo().totalExecutionTime() > 0);
		assertTrue(weaverComponentMaybe.getComponentInfo().totalEmittedItems() > 0);
	}

	@Test
	public void buildAll() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback);
		Maybe maybe = weaverComponentMaybe.build();

		maybe.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSource(weaverComponentMaybe.getComponentInfo());
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.SUCCESS, 1);
		order.verify(messageManager).printSummary(weaverComponentMaybe.getComponentInfo());
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildSource() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.SOURCE, proceedingJoinPoint, messageManager, callback);
		Maybe maybe = weaverComponentMaybe.build();

		maybe.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSource(weaverComponentMaybe.getComponentInfo());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycleWithSuccess() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Maybe maybe = weaverComponentMaybe.build();

		maybe.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.SUCCESS, 1);
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycleWithError() throws Throwable {
		Exception e = new IllegalStateException();
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.error(e));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Maybe maybe = weaverComponentMaybe.build();

		maybe.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.ERROR, e);
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycleWithComplete() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.empty());

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Maybe maybe = weaverComponentMaybe.build();

		maybe.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.COMPLETE);
		order.verify(messageManager).printEvent(weaverComponentMaybe.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildSummary() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.SUMMARY, proceedingJoinPoint, messageManager, callback);
		Maybe maybe = weaverComponentMaybe.build();

		maybe.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSummary(weaverComponentMaybe.getComponentInfo());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildNone() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Maybe.just(1));

		WeaverComponentMaybe weaverComponentMaybe = new WeaverComponentMaybe(RxLogger.Scope.NONE, proceedingJoinPoint, messageManager, callback);
		Maybe maybe = weaverComponentMaybe.build();

		maybe.subscribe(observer);
		observer.dispose();

		verifyZeroInteractions(messageManager);
	}
}