package com.example.chiachen.opengl_demo;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
	MyView myView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		myView = new MyView(this);
		myView.requestFocus();//取得焦點
		myView.setFocusableInTouchMode(true);
		setContentView(myView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		myView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		myView.onPause();
	}
}
