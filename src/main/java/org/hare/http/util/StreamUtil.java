package org.hare.http.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamUtil {

	private static final Logger logger = LoggerFactory.getLogger(StreamUtil.class);
	public static final int DEFAULT_BUFFER_SIZE = 2048;

	/**
	 * 通过绝对路径读取文本文件内容
	 * @param path
	 * @return
	 */
	public static String readText(String path) {
		try {
			return readString(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过classpath路径读取文本文件内容
	 * @param classpath
	 * @return
	 */
	public static String readClasspathText(String classpath) {
		if (!classpath.startsWith("/")) {
			classpath = "/" + classpath;
		}
		return readString(StreamUtil.class.getResourceAsStream(classpath));
	}

	/**
	 * 将输入流中信息读取成字符串, 缓冲默认使用2kb
	 * 
	 * @param in
	 * @return
	 */
	public static byte[] readByteArray(InputStream in) {
		return readByteArray(in, DEFAULT_BUFFER_SIZE);

	}

	/**
	 * 将输入流中信息读取成字节数组
	 * 
	 * @param in
	 * @param bufferSize
	 *            读取流时的字节缓冲大小
	 * @return
	 */
	public static byte[] readByteArray(InputStream in, int bufferSize) {
		BufferedInputStream bis = new BufferedInputStream(in);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[bufferSize];
		int i = 0;
		try {
			while ((i = bis.read(buf)) != -1) {
				bos.write(buf, 0, i);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * 将输入流中信息读取成字符串
	 * 
	 * @param in
	 * @param bufferSize
	 *            读取流时的字节缓冲大小
	 * @return
	 */
	public static String readString(InputStream in, int bufferSize) {
		BufferedInputStream bis = new BufferedInputStream(in);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[bufferSize];
		int i = 0;
		try {
			while ((i = bis.read(buf)) != -1) {
				bos.write(buf, 0, i);
			}
			return bos.toString("UTF-8");
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return null;

	}

	/**
	 * 将输入流中信息读取成字符串, 缓冲默认使用2kb
	 * 
	 * @param in
	 * @return
	 */
	public static String readString(InputStream in) {
		return readString(in, DEFAULT_BUFFER_SIZE);

	}

	/**
	 * 将字符串写入流中
	 * 
	 * @param out
	 * @param data
	 */
	public static void writeString(OutputStream out, String data) {
		BufferedOutputStream bout = new BufferedOutputStream(out);
		try {
			bout.write(data.getBytes());
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (bout != null) {
					bout.flush();
					bout.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}

		}
	}

}
