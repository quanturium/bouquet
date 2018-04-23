package com.quanturium.bouquet.runtime.components;

import org.aspectj.lang.ProceedingJoinPoint;

import io.reactivex.Single;

public class WeaverOnSubscribeComponentSingle extends WeaverOnSubscribeComponentAbstract<Single> {

	WeaverOnSubscribeComponentSingle(ProceedingJoinPoint joinPoint, WeaverComponent<Single> parentWeaverComponent) {
		super(joinPoint, parentWeaverComponent);
	}

	@Override
	public Single build() throws Throwable {
		return buildComponentInternal();
	}

	@SuppressWarnings("unchecked")
	private <T> Single<T> buildComponentInternal() throws Throwable {
		if (getParentWeaverComponent() == null)
			return (Single<T>) getJoinPoint().proceed();

		return ((Single<T>) getJoinPoint().proceed())
				.doFinally(() -> {
					if (getParentWeaverComponent().getComponentInfo().subscribeOnThread() == null)
						getParentWeaverComponent().getComponentInfo().setSubscribeOnThread(Thread.currentThread().getName());
				});
	}
}
