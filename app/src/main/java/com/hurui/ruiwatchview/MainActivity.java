package com.hurui.ruiwatchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RuiWatchView ruiWatchView = findViewById(R.id.watch_view);

		ruiWatchView.start();
	}
}
