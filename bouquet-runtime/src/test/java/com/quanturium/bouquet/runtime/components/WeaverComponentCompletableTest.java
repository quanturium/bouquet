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

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeaverComponentCompletableTest {

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
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete().delay(10, TimeUnit.MILLISECONDS));

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback);
		Completable completable = weaverComponentCompletable.build();

		completable.subscribe(observer);
		observer.awaitTerminalEvent();
		observer.dispose();

		observer.assertNoValues();
		observer.assertNoErrors();
		observer.assertComplete();

		assertNotNull(weaverComponentCompletable.getComponentInfo().observeOnThread());
		assertNotNull(weaverComponentCompletable.getComponentInfo().subscribeOnThread());
		assertTrue(weaverComponentCompletable.getComponentInfo().totalExecutionTime() > 0);
	}

	@Test
	public void buildAll() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager, callback);
		Completable completable = weaverComponentCompletable.build();

		completable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSource(weaverComponentCompletable.getComponentInfo());
		order.verify(messageManager).printEvent(weaverComponentCompletable.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentCompletable.getComponentInfo(), RxEvent.COMPLETE);
		order.verify(messageManager).printSummary(weaverComponentCompletable.getComponentInfo());
		order.verify(messageManager).printEvent(weaverComponentCompletable.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildSource() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.SOURCE, proceedingJoinPoint, messageManager, callback);
		Completable completable = weaverComponentCompletable.build();

		completable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSource(weaverComponentCompletable.getComponentInfo());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycle() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Completable completable = weaverComponentCompletable.build();

		completable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentCompletable.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentCompletable.getComponentInfo(), RxEvent.COMPLETE);
		order.verify(messageManager).printEvent(weaverComponentCompletable.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildLifecycleWithError() throws Throwable {
		Exception e = new IllegalStateException();
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.error(e));

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager, callback);
		Completable completable = weaverComponentCompletable.build();

		completable.subscribe(observer);
		observer.dispose();

		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printEvent(weaverComponentCompletable.getComponentInfo(), RxEvent.SUBSCRIBE);
		order.verify(messageManager).printEvent(weaverComponentCompletable.getComponentInfo(), RxEvent.ERROR, e);
		order.verify(messageManager).printEvent(weaverComponentCompletable.getComponentInfo(), RxEvent.DISPOSE);
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildSummary() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.SUMMARY, proceedingJoinPoint, messageManager, callback);
		Completable completable = weaverComponentCompletable.build();

		completable.subscribe(observer);
		observer.dispose();


		InOrder order = Mockito.inOrder(messageManager);
		order.verify(messageManager).printSummary(weaverComponentCompletable.getComponentInfo());
		order.verifyNoMoreInteractions();
	}

	@Test
	public void buildNone() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Completable.complete());

		WeaverComponentCompletable weaverComponentCompletable = new WeaverComponentCompletable(RxLogger.Scope.NONE, proceedingJoinPoint, messageManager, callback);
		Completable completable = weaverComponentCompletable.build();

		completable.subscribe(observer);
		observer.dispose();

		verifyZeroInteractions(messageManager);
	}
}