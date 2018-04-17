package com.quanturium.bouquet.android.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.quanturium.bouquet.annotations.RxLogger;

import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button).setOnClickListener(v -> {
			getObservableExample("String 4")
					.subscribe(s -> Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show());
		});
	}

	@RxLogger(RxLogger.Scope.ALL)
	private Observable<String> getObservableExample(String extra) {
		return Observable.just("String 1", "String 2", "String 3", extra);
	}
}
