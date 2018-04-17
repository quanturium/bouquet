package com.quanturium.bouquet.java.sample;

import com.quanturium.bouquet.annotations.RxLogger;

import io.reactivex.Observable;

import static com.quanturium.bouquet.annotations.RxLogger.Scope.ALL;

public class Main {

	public static void main(String[] args) {
		getObservableExample("String 4")
				.subscribe();
	}

	@RxLogger(ALL)
	private static Observable<String> getObservableExample(String extra) {
		return Observable.just("String 1", "String 2", "String 3", extra);
	}

}
