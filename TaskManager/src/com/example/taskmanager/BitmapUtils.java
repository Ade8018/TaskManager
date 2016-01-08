package com.example.taskmanager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapUtils {
	public static final String TAG = BitmapUtils.class.getName();

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 保存bitmap到本地
	 * 
	 * @param bitmap
	 */
	public static void savePicture(Bitmap bitmap, String rootPath, String name) {
		FileOutputStream b = null;
		String fileName = rootPath + "/" + name;
		File file = new File(rootPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 删除上一个 保存下一个
		try {
			b = new FileOutputStream(fileName);
			// 把数据写入文件
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (b != null) {
					b.flush();
					b.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将覆盖物和图片进行融合，返回一个Bitmap
	 * 
	 * @param floatBitmap
	 *            覆盖物
	 * @param src
	 *            源图片
	 * @param stretch
	 *            是否拉伸
	 * @param x
	 *            覆盖物相对源图片的x轴偏移
	 * @param y
	 *            覆盖物相对源图片的y轴偏移
	 * @return
	 */
	public static Bitmap montageBitmap(Bitmap floatBitmap, Bitmap src,
			boolean stretch, int x, int y) {
		Bitmap sizeFloatBitmap = floatBitmap;
		int w = src.getWidth();
		int h = src.getHeight();
		if (stretch) {
			sizeFloatBitmap = Bitmap
					.createScaledBitmap(floatBitmap, w, h, true);
		}
		Bitmap newBM = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(newBM);
		canvas.drawBitmap(src, 0, 0, null);
		canvas.drawBitmap(sizeFloatBitmap, x, y, null);
		return newBM;
	}

	/**
	 * 将图片按宽度比例缩放（放大）
	 * 
	 * @param src
	 *            源图片
	 * @param width
	 *            最后宽度
	 * @return
	 */
	public static Bitmap scaleBitmapByWidth(Bitmap src, int width) {
		return Bitmap.createScaledBitmap(src, width, width * src.getHeight()
				/ src.getWidth(), false);
	}

	/**
	 * 将图片按高度比例缩放（放大）
	 * 
	 * @param src
	 *            源图片
	 * @param width
	 *            最后高度
	 * @return
	 */
	public static Bitmap scaleBitmapByHeight(Bitmap src, int height) {
		return Bitmap.createScaledBitmap(src,
				height * src.getWidth() / src.getHeight(), height, false);
	}

	/**
	 * 镜像水平翻转图片
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap convertBmp(Bitmap bmp) {
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(-1, 1); // 镜像水平翻转
		bmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
		return bmp;
	}

	/**
	 * 剪切图片
	 * 
	 * @param bitmap
	 * @param radiusRate
	 *            边角的半径比例
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap, float radiusRate) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		radiusRate = 1;// 改为圆形

		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		// final int color = 0x00000000;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, (float) (roundPx / radiusRate),
				(float) (roundPx / radiusRate), paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 不缩小图片尺寸的情况下，压缩图片(即降低图片质量)
	 * 
	 * @param bm
	 *            要压缩的位图
	 * @param needSize
	 *            最终大小不大于needSize 字节，当不能满足此条件时，返回图片可压缩的最小的数组
	 * @return
	 */
	public static byte[] compressQuality(Bitmap bm, int needSize) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		if (bm == null || needSize <= 0) {
			return bos.toByteArray();
		}
		int q = 100;
		bm.compress(CompressFormat.JPEG, q, bos);
		while ((bos.size()) > needSize && q >= 10) {
			bos.reset();
			q -= 10;
			bm.compress(CompressFormat.JPEG, q, bos);
		}
		return bos.toByteArray();
	}

	public static Bitmap compressDimension(Bitmap bm, int longerEdgeLength) {
		if (bm == null || longerEdgeLength < 1) {
			return bm;
		}
		int width = bm.getWidth();
		int height = bm.getHeight();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, baos);
		int be = width / longerEdgeLength;
		if (width < height) {
			be = height / longerEdgeLength;
		}
		if (be < 1) {
			be = 1;
		}
		byte[] buf = baos.toByteArray();
		Options ops = new Options();
		ops.inSampleSize = be;
		return bm = BitmapFactory.decodeByteArray(buf, 0, buf.length, ops);
	}
}
