package com.example.taskmanager.buss;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class TaskUtil {
	public static Bitmap getTaskTopThumbnail(Context context,
			int persistentTaskId) {
		try {
			Method method_getTaskTopThumbnail = ActivityManager.class
					.getDeclaredMethod("getTaskTopThumbnail", int.class);
			method_getTaskTopThumbnail.setAccessible(true);
			return (Bitmap) method_getTaskTopThumbnail.invoke(
					context.getSystemService(Context.ACTIVITY_SERVICE),
					persistentTaskId);
		} catch (NoSuchMethodException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean removeTask(ActivityManager am, int taskId) {
		try {
			Method method_removeTask = ActivityManager.class.getDeclaredMethod(
					"removeTask", int.class);
			method_removeTask.setAccessible(true);
			return (boolean) method_removeTask.invoke(am, taskId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean removeTask(ActivityManager am, int taskId, int flags) {
		try {
			Method method_removeTask = ActivityManager.class.getDeclaredMethod(
					"removeTask", int.class, int.class);
			method_removeTask.setAccessible(true);
			return (boolean) method_removeTask.invoke(am, taskId, flags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static BitmapFactory.Options sBitmapOptions;
	static {
		sBitmapOptions = new BitmapFactory.Options();
		sBitmapOptions.inMutable = true;
	}

	// /**
	// * Returns a task thumbnail from the activity manager
	// */
	public static Bitmap getThumbnail(ActivityManager activityManager,
			int persistentTaskId) {
		Bitmap thumbnail = null;
		try {
			Class cls_TaskThumbnail = Class
					.forName("android.app.ActivityManager$TaskThumbnail");
			Method m_getTaskThumbnail = ActivityManager.class
					.getDeclaredMethod("getTaskThumbnail", int.class);
			m_getTaskThumbnail.setAccessible(true);
			Object obj_TaskThumbnail = m_getTaskThumbnail.invoke(
					activityManager, persistentTaskId);
			if (obj_TaskThumbnail == null)
				return null;
			Field f_mainThumbnail = cls_TaskThumbnail.getField("mainThumbnail");
			Field f_thumbnailFileDescriptor = cls_TaskThumbnail
					.getField("thumbnailFileDescriptor");
			thumbnail = (Bitmap) f_mainThumbnail.get(obj_TaskThumbnail);
			ParcelFileDescriptor descriptor = (ParcelFileDescriptor) f_thumbnailFileDescriptor
					.get(obj_TaskThumbnail);
			if (thumbnail == null && descriptor != null) {
				thumbnail = BitmapFactory.decodeFileDescriptor(
						descriptor.getFileDescriptor(), null, sBitmapOptions);
			}
			if (descriptor != null) {
				try {
					descriptor.close();
				} catch (IOException e) {
				}
			}
		} catch (Exception e) {
		}
		return thumbnail;
	}
}
