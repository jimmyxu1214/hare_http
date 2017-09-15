package org.hare.http.factory;

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

public class IdleConnectionMonitorThread extends Thread {
	private static final Logger log = Logger.getLogger(IdleConnectionMonitorThread.class);
	//检查线程等待毫秒
	private static final Integer WAIT_TIME = 5000;
	//活动时间秒
	private static final Integer IDLE_TIME = 180;
	private final PoolingHttpClientConnectionManager cm;
	private volatile boolean shutdown;

	public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager cm) {
		super();
		this.cm = cm;
	}

	@Override
	public void run() {
		log.info("==========IdleConnectionMonitorThread start==========");
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(WAIT_TIME);
					//关闭失效的连接
					cm.closeExpiredConnections();
					// 可选的, 关闭30秒内不活动的连接
					cm.closeIdleConnections(IDLE_TIME, TimeUnit.SECONDS);
				}
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}

}
