package com.quanturium.bouquet.runtime.components;

import com.quanturium.bouquet.annotations.RxLogger;
import com.quanturium.bouquet.runtime.logging.MessageManager;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.annotations.NonNull;

public class WeaverFactory {

	// We use this in order for the onSubscribe component to access the parent Weaver and set
	// the onSubscribe thread name
	protected ThreadLocal<WeaverComponent> currentWeaverComponent = new ThreadLocal<>();
	protected WeaverComponent.Callback callback = new WeaverComponent.Callback() {
		@Override
		public void before(WeaverComponent weaverComponent) {
			currentWeaverComponent.set(weaverComponent);
		}

		@Override
		public void after(WeaverComponent weaverComponent) {
			currentWeaverComponent.set(null);
		}
	};

	public WeaverFactory() {
	}

	public WeaverComponent buildWeaverComponent(@NonNull ComponentType componentType, @NonNull RxLogger.Scope scope, @NonNull ProceedingJoinPoint joinPoint, @NonNull MessageManager messageManager) {

		switch (componentType) {
			case FLOWABLE:
				return new WeaverComponentFlowable(scope, joinPoint, messageManager, callback);
			case OBSERVABLE:
				return new WeaverComponentObservable(scope, joinPoint, messageManager, callback);
			case SINGLE:
				return new WeaverComponentSingle(scope, joinPoint, messageManager, callback);
			case MAYBE:
				return new WeaverComponentMaybe(scope, joinPoint, messageManager, callback);
			case COMPLETABLE:
				return new WeaverComponentCompletable(scope, joinPoint, messageManager, callback);
			default:
				return null;
		}
	}

	@SuppressWarnings("unchecked")
	public WeaverOnSubscribeComponent buildWeaverOnSubscribeComponent(@NonNull ComponentType componentType, @NonNull ProceedingJoinPoint joinPoint) {

		WeaverComponent parentWeaverComponent = currentWeaverComponent.get();

		switch (componentType) {
			case FLOWABLE:
				return new WeaverOnSubscribeComponentFlowable(joinPoint, parentWeaverComponent);
			case OBSERVABLE:
				return new WeaverOnSubscribeComponentObservable(joinPoint, parentWeaverComponent);
			case SINGLE:
				return new WeaverOnSubscribeComponentSingle(joinPoint, parentWeaverComponent);
			case MAYBE:
				return new WeaverOnSubscribeComponentMaybe(joinPoint, parentWeaverComponent);
			case COMPLETABLE:
				return new WeaverOnSubscribeComponentCompletable(joinPoint, parentWeaverComponent);
			default:
				return null;
		}
	}
}
