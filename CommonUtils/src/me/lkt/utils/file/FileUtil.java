package me.lkt.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

public class FileUtil {
	public static void copy(String source, String dest) {
		if (source == null || dest == null) {
			throw new NullPointerException();
		}
		File sourceFile = new File(source);
		if (!sourceFile.exists()) {
			throw new RuntimeException("源文件不存在");
		}
		makeDestFileDir(dest);
		channelCopy(sourceFile, new File(dest));
	}

	public static void copy(InputStream is, String dest) {
		if (is == null || dest == null) {
			throw new NullPointerException();
		}
		makeDestFileDir(dest);
		streamCopy(is, new File(dest));
	}

	private static void makeDestFileDir(String dest) {
		File destDir = new File(dest.substring(0, dest.lastIndexOf("/")));
		if (destDir.isDirectory()) {
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
		} else {
			throw new RuntimeException("请输入正确的目的文件路径");
		}
	}

	private static void channelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fo.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void streamCopy(InputStream is, File dest) {
		FileOutputStream fos = null;
		byte[] buf = new byte[512];
		int len = -1;
		try {
			fos = new FileOutputStream(dest);
			while ((len = is.read(buf)) != -1) {
				fos.write(buf, 0, len);
				fos.flush();
			}
		} catch (Exception e) {
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}