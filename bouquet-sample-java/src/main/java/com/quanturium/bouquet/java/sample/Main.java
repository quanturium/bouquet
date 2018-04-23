package com.quanturium.bouquet.java.sample;

import com.quanturium.bouquet.annotations.RxLogger;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Observable<String> observable = getObservableExample("String 4");

		Disposable disposable = observable
				.subscribe();

		Thread.sleep(1000);
	}

	@RxLogger
	private static Observable<String> getObservableExample(String extra) {
		return Observable.just("String 1", "String 2", "String 3", extra)
				.subscribeOn(Schedulers.io())
				.observeOn(Schedulers.newThread());
	}

}
