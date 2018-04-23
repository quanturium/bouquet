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

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeaverComponentSingleTest {

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
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1).delay(10, TimeUnit.MILLISECONDS));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback);
		Single single = weaverComponentSingle.build();

		single.subscribe(observer);
		observer.awaitTerminalEvent();
		observer.dispose();

		observer.assertValue(1);
		observer.assertNoErrors();
		observer.assertComplete();

		assertNotNull(weaverComponentSingle.getComponentInfo().observeOnThread());
		assertNotNull(weaverComponentSingle.getComponentInfo().subscribeOnThread());
		assertTrue(weaverComponentSingle.getComponentInfo().totalExecutionTime() > 0);
		assertTrue(weaverComponentSingle.getComponentInfo().totalEmittedItems() > 0);
	}

	@Test
	public void buildAll() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback);
		Single single = weaverComponentSingle.build();

		single.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSource(weaverComponentSingle.getComponentInfo());
		order.verify(messageManager).printEvent(weaverComponentSingle.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentSingle.getComponentInfo(), RxEvent.SUCCESS, 1);
		order.verify(messageManager).printSummary(weaverComponentSingle.getComponentInfo());
		order.verify(messageManager).printEvent(weaverComponentSingle.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildSource() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.SOURCE, proceedingJoinPoint, messageManager, callback);
		Single single = weaverComponentSingle.build();

		single.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSource(weaverComponentSingle.getComponentInfo());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycle() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Single single = weaverComponentSingle.build();

		single.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentSingle.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentSingle.getComponentInfo(), RxEvent.SUCCESS, 1);
		order.verify(messageManager).printEvent(weaverComponentSingle.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycleWithError() throws Throwable {
		Exception e = new IllegalStateException();
		when(proceedingJoinPoint.proceed()).thenReturn(Single.error(e));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Single single = weaverComponentSingle.build();

		single.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentSingle.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentSingle.getComponentInfo(), RxEvent.ERROR, e);
		order.verify(messageManager).printEvent(weaverComponentSingle.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildSummary() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.SUMMARY, proceedingJoinPoint, messageManager, callback);
		Single single = weaverComponentSingle.build();

		single.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSummary(weaverComponentSingle.getComponentInfo());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildNone() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Single.just(1));

		WeaverComponentSingle weaverComponentSingle = new WeaverComponentSingle(RxLogger.Scope.NONE, proceedingJoinPoint, messageManager, callback);
		Single single = weaverComponentSingle.build();

		single.subscribe(observer);
		observer.dispose();

		verifyZeroInteractions(messageManager);
	}
}