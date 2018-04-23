package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Maybe;

public class WeaverOnSubscribeComponentMaybe extends WeaverOnSubscribeComponentAbstract<Maybe> {

	WeaverOnSubscribeComponentMaybe(ProceedingJoinPoint joinPoint, WeaverComponent<Maybe> parentWeaverComponent) {
		super(joinPoint, parentWeaverComponent);
	}

	@Override
	public Maybe build() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Maybe<T> buildComponentInternal() throws Throwable {
		if (getParentWeaverComponent() == null)
			return (Maybe<T>) getJoinPoint().proceed();

		return ((Maybe<T>) getJoinPoint().proceed())
				.doFinally(() -> {
					if (getParentWeaverComponent().getComponentInfo().subscribeOnThread() == null)
						getParentWeaverComponent().getComponentInfo().setSubscribeOnThread(Thread.currentThread().getName());
				});
	}
}
