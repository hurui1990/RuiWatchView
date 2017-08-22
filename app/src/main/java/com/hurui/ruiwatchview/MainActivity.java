package com.hurui.ruiwatchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

	private RuiWatchView mRuiWatchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mRuiWatchView = findViewById(R.id.watch_view);
		mRuiWatchView.start();
	}
}
