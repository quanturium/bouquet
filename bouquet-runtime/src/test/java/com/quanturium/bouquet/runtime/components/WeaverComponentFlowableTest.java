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

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeaverComponentFlowableTest {

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	MessageManager messageManager;
	@Mock
	WeaverComponent.Callback callback;

	private TestSubscriber observer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		observer = new TestSubscriber();
	}

	@Test
	public void build() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1).delay(10, TimeUnit.MILLISECONDS));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback);
		Flowable flowable = weaverComponentFlowable.build();

		flowable.subscribe(observer);
		observer.awaitTerminalEvent();
		observer.dispose();

		observer.assertValue(1);
		observer.assertNoErrors();
		observer.assertComplete();

		assertNotNull(weaverComponentFlowable.getComponentInfo().observeOnThread());
		assertNotNull(weaverComponentFlowable.getComponentInfo().subscribeOnThread());
		assertTrue(weaverComponentFlowable.getComponentInfo().totalExecutionTime() > 0);
		assertTrue(weaverComponentFlowable.getComponentInfo().totalEmittedItems() > 0);
	}

	@Test
	public void buildAll() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback);
		Flowable flowable = weaverComponentFlowable.build();

		flowable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSource(weaverComponentFlowable.getComponentInfo());
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.REQUEST, Long.MAX_VALUE);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.NEXT, 1);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.COMPLETE);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.TERMINATE);
		order.verify(messageManager).printSummary(weaverComponentFlowable.getComponentInfo());
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.CANCEL);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildSource() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.SOURCE, proceedingJoinPoint, messageManager, callback);
		Flowable flowable = weaverComponentFlowable.build();

		flowable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSource(weaverComponentFlowable.getComponentInfo());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycle() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Flowable flowable = weaverComponentFlowable.build();

		flowable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.REQUEST, Long.MAX_VALUE);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.NEXT, 1);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.COMPLETE);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.TERMINATE);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.CANCEL);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycleWithError() throws Throwable {
		Exception e = new IllegalStateException();
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.error(e));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Flowable flowable = weaverComponentFlowable.build();

		flowable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.REQUEST, Long.MAX_VALUE);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.ERROR, e);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.TERMINATE);
		order.verify(messageManager).printEvent(weaverComponentFlowable.getComponentInfo(), RxEvent.CANCEL);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildSummary() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.SUMMARY, proceedingJoinPoint, messageManager, callback);
		Flowable flowable = weaverComponentFlowable.build();

		flowable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSummary(weaverComponentFlowable.getComponentInfo());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildNone() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.NONE, proceedingJoinPoint, messageManager, callback);
		Flowable flowable = weaverComponentFlowable.build();

		flowable.subscribe(observer);
		observer.dispose();

		verifyZeroInteractions(messageManager);
	}
}