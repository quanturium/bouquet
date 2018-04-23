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

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeaverComponentObservableTest {

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
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1).delay(10, TimeUnit.MILLISECONDS));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback);
		Observable observable = weaverComponentObservable.build();

		observable.subscribe(observer);
		observer.awaitTerminalEvent();
		observer.dispose();

		observer.assertValue(1);
		observer.assertNoErrors();
		observer.assertComplete();

		assertNotNull(weaverComponentObservable.getComponentInfo().observeOnThread());
		assertNotNull(weaverComponentObservable.getComponentInfo().subscribeOnThread());
		assertTrue(weaverComponentObservable.getComponentInfo().totalExecutionTime() > 0);
		assertTrue(weaverComponentObservable.getComponentInfo().totalEmittedItems() > 0);
	}

	@Test
	public void buildAll() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback);
		Observable observable = weaverComponentObservable.build();

		observable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSource(weaverComponentObservable.getComponentInfo());
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.NEXT, 1);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.COMPLETE);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.TERMINATE);
		order.verify(messageManager).printSummary(weaverComponentObservable.getComponentInfo());
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildSource() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.SOURCE, proceedingJoinPoint, messageManager, callback);
		Observable observable = weaverComponentObservable.build();

		observable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSource(weaverComponentObservable.getComponentInfo());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycle() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Observable observable = weaverComponentObservable.build();

		observable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.NEXT, 1);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.COMPLETE);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.TERMINATE);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycleWithError() throws Throwable {
		Exception e = new IllegalStateException();
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.error(e));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Observable observable = weaverComponentObservable.build();

		observable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.ERROR, e);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.TERMINATE);
		order.verify(messageManager).printEvent(weaverComponentObservable.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildSummary() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.SUMMARY, proceedingJoinPoint, messageManager, callback);
		Observable observable = weaverComponentObservable.build();

		observable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSummary(weaverComponentObservable.getComponentInfo());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildNone() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Observable.just(1));

		WeaverComponentObservable weaverComponentObservable = new WeaverComponentObservable(RxLogger.Scope.NONE, proceedingJoinPoint, messageManager, callback);
		Observable observable = weaverComponentObservable.build();

		observable.subscribe(observer);
		observer.dispose();

		verifyZeroInteractions(messageManager);
	}
}