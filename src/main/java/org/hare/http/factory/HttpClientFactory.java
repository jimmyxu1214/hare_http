package org.hare.http.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

public class HttpClientFactory {

	private static Map<Integer, IdleConnectionMonitorThread> monitorMap = new ConcurrentHashMap<Integer, IdleConnectionMonitorThread>();

	/**
	 * 多线程的HttpClient,内部使用PoolingHttpClientConnectionManager
	 * 
	 * @param maxConnection
	 * @param maxPerRoute
	 * @param openIdleConnectionMonitor
	 * @return
	 */
	public static CloseableHttpClient createPoolingHttpClient(int maxConnection, int maxPerRoute, boolean openIdleConnectionMonitor) {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		// 将最大连接数增加到200
		cm.setMaxTotal(maxConnection);
		// 将每个路由基础的连接增加到20
		cm.setDefaultMaxPerRoute(maxPerRoute);
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		if (openIdleConnectionMonitor) {
			IdleConnectionMonitorThread cmThread = new IdleConnectionMonitorThread(cm);
			cmThread.start();
			monitorMap.put(httpClient.hashCode(), cmThread);
		}
		return httpClient;
	}

	/**
	 * 多线程的HttpClient,内部使用PoolingHttpClientConnectionManager, 支持HTTPS
	 * 
	 * @param maxConnection
	 * @param maxPerRoute
	 * @param openIdleConnectionMonitor
	 * @return
	 */
	public static CloseableHttpClient createSSLPoolingHttpClient(int maxConnection, int maxPerRoute, boolean openIdleConnectionMonitor,
			SSLConnectionSocketFactory sslSocketFactory) {
		Registry<ConnectionSocketFactory> register = RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslSocketFactory).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(register);
		// 将最大连接数增加到200
		cm.setMaxTotal(maxConnection);
		// 将每个路由基础的连接增加到20
		cm.setDefaultMaxPerRoute(maxPerRoute);
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		if (openIdleConnectionMonitor) {
			IdleConnectionMonitorThread cmThread = new IdleConnectionMonitorThread(cm);
			cmThread.start();
			monitorMap.put(httpClient.hashCode(), cmThread);
		}
		return httpClient;
	}

	public static void shutdownIdleConnectionMonitorThread(CloseableHttpClient httpClient) {
		IdleConnectionMonitorThread cmThread = monitorMap.get(httpClient.hashCode());
		if (cmThread != null) {
			cmThread.shutdown();
		}
	}

	/**
	 * 内部使用BasicHttpClientConnectionManager，每次只能管理一个连接，但是会尽量重用以前的连接
	 * 
	 * @return
	 */
	public static CloseableHttpClient createBasicHttpClient() {
		HttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		return httpClient;
	}

	/**
	 * 内部使用BasicHttpClientConnectionManager，每次只能管理一个连接，但是会尽量重用以前的连接,支持HTTPS
	 * 
	 * @return
	 */
	public static CloseableHttpClient createSSLBasicHttpClient(SSLConnectionSocketFactory sslSocketFactory) {
		Registry<ConnectionSocketFactory> register = RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslSocketFactory).build();
		HttpClientConnectionManager cm = new BasicHttpClientConnectionManager(register);
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
		return httpClient;
	}

	/**
	 * 创建一个默认的配置HttpClient
	 * 
	 * @return
	 */
	public static CloseableHttpClient createDefaultHttpClient() {
		return HttpClients.createDefault();
	}

	/**
	 * 创建一个默认的配置HttpClient，支持HTTPS
	 * 
	 * @return
	 */
	public static CloseableHttpClient createSSLDefaultHttpClient(SSLConnectionSocketFactory sslSocketFactory) {
		return HttpClients.custom().setSSLSocketFactory(sslSocketFactory).build();
	}

	/**
	 * trustStore 和keyStore密码相同
	 * 
	 * @param trustStoreFile
	 *            存储用来校验服务器端身份的证书
	 * @param keyStoreFile
	 *            客户端证书
	 * @param keyStorePassword
	 * 
	 * @param protocol
	 *            <pre>
	 *            protocol jdk1.6 jdk1.7 默认TLSv1, jdk1.8 默认 TLSv1.2
	 *            protocol支持: TLSv1.2 TLSv1.1 TLSv1 SSLv3
	 * </pre>
	 * @return
	 */
	// TODO 本方法还需要根据实际情况修改
	public static SSLConnectionSocketFactory createSSLConnectionSocketFactory(File trustStoreFile, File keyStoreFile, String keyStorePassword, String protocol) {
		InputStream trustIn = null;
		InputStream keyIn = null;
		KeyStore trustStore = null;
		KeyStore keyStore = null;
		try {
			if (trustStoreFile != null) {
				trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustIn = new FileInputStream(trustStoreFile);
				trustStore.load(trustIn, keyStorePassword.toCharArray());
			}
			if (keyStoreFile != null) {
				keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
				keyIn = new FileInputStream(keyStoreFile);
				keyStore.load(keyIn, keyStorePassword.toCharArray());
			}
			SSLContextBuilder sslContextBuilder = SSLContexts.custom();
			if (protocol != null) {
				sslContextBuilder.useProtocol(protocol);
			}
			if (trustStore != null) {
				sslContextBuilder.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy());
			} else {
				// 信任所有服务器
				sslContextBuilder.loadTrustMaterial(new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;
					}
				});
			}
			if (keyStore != null) {
				sslContextBuilder.loadKeyMaterial(keyStore, keyStorePassword.toCharArray());
			}
			SSLContext sslContext = sslContextBuilder.build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			return sslsf;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (trustIn != null) {
				try {
					trustIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (keyIn != null) {
				try {
					keyIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 创建一个信任所有证书的SSLConnectionFactory protocol jdk1.6 jdk1.7 默认TLSv1 jdk1.8 默认
	 * 
	 * @param protocol
	 *            <pre>
	 *            protocol jdk1.6 jdk1.7 默认TLSv1, jdk1.8 默认 TLSv1.2
	 *            protocol支持: TLSv1.2 TLSv1.1 TLSv1 SSLv3
	 * 
	 * @return
	 */
	public static SSLConnectionSocketFactory createTrustAllSSLConnectionSocketFactory(String protocol) {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[] {};
				}

				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}
			} };
			SSLContextBuilder sslContextBuilder = SSLContexts.custom();
			if (protocol != null) {
				sslContextBuilder.useProtocol(protocol);
			}
			SSLContext sslContext = sslContextBuilder.build();
			sslContext.init(null, trustAllCerts, new SecureRandom());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			return sslsf;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
