package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.annotations.NonNull;

public class WeaverComponentFactory {

	public WeaverComponentFactory() {
	}

	public WeaverComponent buildWeaverComponent(@NonNull RxComponent rxComponent, @NonNull RxLogger.Scope scope, @NonNull ProceedingJoinPoint joinPoint, @NonNull MessageManager messageManager) {

		switch (rxComponent) {
			case FLOWABLE:
				return saveAndReturnWeaverComponent(new WeaverComponentFlowable(scope, joinPoint, messageManager));
			case OBSERVABLE:
				return saveAndReturnWeaverComponent(new WeaverComponentObservable(scope, joinPoint, messageManager));
			case SINGLE:
				return saveAndReturnWeaverComponent(new WeaverComponentSingle(scope, joinPoint, messageManager));
			case MAYBE:
				return saveAndReturnWeaverComponent(new WeaverComponentMaybe(scope, joinPoint, messageManager));
			case COMPLETABLE:
				return saveAndReturnWeaverComponent(new WeaverComponentCompletable(scope, joinPoint, messageManager));
		}

		return null;
	}

	private WeaverComponent saveAndReturnWeaverComponent(WeaverComponent weaverComponent) {
		return weaverComponent;
	}

}
