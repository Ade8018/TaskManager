package com.example.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	Button btn;
	ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.btn);
		iv = (ImageView) findViewById(R.id.iv);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,TaskActivity.class);
				startActivity(intent);
//				ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//				List<RecentTaskInfo> infos = am.getRecentTasks(42, 2);
//				try {
//					ActivityInfo ai = getPackageManager().getActivityInfo(
//							infos.get(2).baseIntent.getComponent(),
//							PackageManager.GET_INTENT_FILTERS);
//					iv.setImageDrawable(new IconUtilities(
//							getApplicationContext()).createIconDrawable(ai
//							.loadIcon(getPackageManager())));
//					iv.setImageBitmap(TaskUtil.getTaskTopThumbnail(
//							getApplicationContext(), infos.get(2).persistentId));
//				} catch (NameNotFoundException e) {
//					e.printStackTrace();
//				}
//				return;
			}
		});
	}

	public static Bitmap captureScreen(Activity activity) {
		activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
		Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
		return bmp;
	}

}
