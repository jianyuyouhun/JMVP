package com.jianyuyouhun.jmvplib.utils;


import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * IO流版主类，拷贝流，关闭流方法
 *
 * @author zdxing
 *
 */
public class IOUtils {
	public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
		copy(inputStream, outputStream, null);
	}

	public static void copy(InputStream inputStream, OutputStream outputStream, OnProgressCallback onProgressCallback) throws IOException {
		byte[] buffer = new byte[1024];
		int length = -1;
		int total = 0;
		while ((length = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, length);
			total = total + length;
			if (onProgressCallback != null)
				onProgressCallback.onProgressChange(total);
		}
		outputStream.flush();
	}

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = inputStream.read(buffer)) != -1)
			byteArrayOutputStream.write(buffer, 0, length);

		byteArrayOutputStream.flush();
		byteArrayOutputStream.close();
		return byteArrayOutputStream.toByteArray();
	}
}
