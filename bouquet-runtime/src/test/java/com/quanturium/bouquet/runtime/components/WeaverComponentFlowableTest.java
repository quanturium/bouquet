package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WeaverComponentFlowableTest {

	@Mock
	ProceedingJoinPoint proceedingJoinPoint;
	@Mock
	MessageManager messageManager;

	private TestSubscriber observer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		observer = new TestSubscriber();
	}

	@Test
	public void buildRx() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager);
		Flowable flowable = weaverComponentFlowable.buildRx();

		flowable.subscribe(observer);
		observer.dispose();

		observer.assertValue(1);
		observer.assertNoErrors();
		observer.assertComplete();
	}

	@Test
	public void buildRxAll() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.ALL, proceedingJoinPoint, messageManager);
		Flowable flowable = weaverComponentFlowable.buildRx();

		flowable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSource(weaverComponentFlowable.getRxComponentInfo());
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.SUBSCRIBE);
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.REQUEST, Long.MAX_VALUE);
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.NEXT, 1);
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.CANCEL);
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.COMPLETE);
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.TERMINATE);
		verify(messageManager).printSummary(weaverComponentFlowable.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxSource() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.SOURCE, proceedingJoinPoint, messageManager);
		Flowable flowable = weaverComponentFlowable.buildRx();

		flowable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSource(weaverComponentFlowable.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxLifecycle() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.LIFECYCLE, proceedingJoinPoint, messageManager);
		Flowable flowable = weaverComponentFlowable.buildRx();

		flowable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.SUBSCRIBE);
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.REQUEST, Long.MAX_VALUE);
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.NEXT, 1);
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.CANCEL);
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.COMPLETE);
		verify(messageManager).printEvent(weaverComponentFlowable.getRxComponentInfo(), RxEvent.TERMINATE);
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxSummary() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.SUMMARY, proceedingJoinPoint, messageManager);
		Flowable flowable = weaverComponentFlowable.buildRx();

		flowable.subscribe(observer);
		observer.dispose();

		verify(messageManager).printSummary(weaverComponentFlowable.getRxComponentInfo());
		verifyNoMoreInteractions(messageManager);
	}

	@Test
	public void buildRxNone() throws Throwable {
		when(proceedingJoinPoint.proceed()).thenReturn(Flowable.just(1));

		WeaverComponentFlowable weaverComponentFlowable = new WeaverComponentFlowable(RxLogger.Scope.NONE, proceedingJoinPoint, messageManager);
		Flowable flowable = weaverComponentFlowable.buildRx();

		flowable.subscribe(observer);
		observer.dispose();

		verifyZeroInteractions(messageManager);
	}
}