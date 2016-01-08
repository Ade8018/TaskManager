package com.example.taskmanager.buss;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;

public class TaskList {
	public static final String TAG = TaskList.class.getName();
	public static final int MAX_TASK_COUNT = 20;
	private List<RecentTaskInfo> mInfos;
	private PackageManager mPm;
	private ActivityManager mAm;
	private LruCache<Integer, Bitmap> mThumbs = new LruCache<Integer, Bitmap>(
			4 * 1000 * 1000) {
		@Override
		protected int sizeOf(Integer key, Bitmap value) {
			int bytes = value.getRowBytes() * value.getHeight();
			Log.e(TAG, "图片大小(字节): " + bytes);
			return bytes;
		}
	};

	@SuppressWarnings("deprecation")
	public TaskList(ActivityManager am, PackageManager pm) {
		if (am == null || pm == null)
			throw new NullPointerException();
		mAm = am;
		mPm = pm;
		mInfos = am.getRecentTasks(MAX_TASK_COUNT,
				ActivityManager.RECENT_IGNORE_UNAVAILABLE);
		dofilter();
	}

	private void dofilter() {
		int size = mInfos.size();
		for (int i = 0; i < size; i++) {
			String pkg = getComponentPkgName(i);
			if ("com.iwit.luncher".equals(pkg)) {
				mInfos.remove(i--);
				size--;
				break;
			}
		}
	}

	private String getComponentPkgName(int index) {
		return mInfos.get(index).baseIntent.getComponent().getPackageName();
	}

	public int size() {
		return mInfos.size();
	}

	public int getPersistentIdByIndex(int index) {
		return mInfos.get(index).persistentId;
	}

	public ComponentName getBaseActivityByIndex(int index) {
		return mInfos.get(index).baseIntent.getComponent();
	}

	public Drawable getIconDrawable(int index) {
		ComponentName cn = getBaseActivityByIndex(index);
		try {
			return mPm.getActivityInfo(cn, PackageManager.GET_INTENT_FILTERS)
					.loadIcon(mPm);
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public Bitmap getThumbnail(Context context, int index) {
		int pid = getPersistentIdByIndex(index);
		Bitmap result = mThumbs.get(pid);
		if (result == null) {
			if (Build.VERSION.SDK_INT < 20) {
				result = TaskUtil.getTaskTopThumbnail(context, pid);
			} else {
				result = TaskUtil.getThumbnail((ActivityManager) context
						.getSystemService(Context.ACTIVITY_SERVICE), pid);
			}
			if (result == null)// 已经尝试过获取缩略图的，即便为null，也不再获取了。
				// 此处可修改为使用默认图片
				result = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
			mThumbs.put(pid, result);
		}
		return result;
	}

	public void removeTask(int position) {
		if (Build.VERSION.SDK_INT < 20) {
			TaskUtil.removeTask(mAm, getPersistentIdByIndex(position), 1);
		} else {
			TaskUtil.removeTask(mAm, getPersistentIdByIndex(position));
		}
		mInfos.remove(position);
		mThumbs.remove(position);
	}

	public void resumeTask(Context context, int position) {
		if (getComponentPkgName(position).contains("com.android.launcher")) {
			return;
		}
		if (mInfos.get(position).id > 0) {
			mAm.moveTaskToFront(getPersistentIdByIndex(position), 0);
		} else {
			Intent intent = mInfos.get(position).baseIntent;
			intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
					| Intent.FLAG_ACTIVITY_TASK_ON_HOME
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				context.startActivity(intent);
			} catch (Exception e) {
			}
		}
	}
}
