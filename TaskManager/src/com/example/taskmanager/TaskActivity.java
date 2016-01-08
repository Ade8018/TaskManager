package com.example.taskmanager;

import com.example.taskmanager.buss.TaskFragment;

import android.app.Activity;
import android.os.Bundle;

public class TaskActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		getFragmentManager().beginTransaction()
				.replace(R.id.layout_activity_task, new TaskFragment())
				.commit();
	}
}
