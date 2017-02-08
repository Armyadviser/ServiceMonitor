package com.gesoft.adsl.monitor;

import java.util.HashMap;

/**
 * @author Storm_Falcon
 *	全局单例Map，保存每个被监控服务的线程句柄
 */
class GlobalServiceThreadInstance {

	private static HashMap<String, Thread> globalThread;
	
	private GlobalServiceThreadInstance() {}
	
	synchronized static HashMap<String, Thread> getServiceThreadMap() {
		if (globalThread == null) {
			globalThread = new HashMap<String, Thread>();
		}
		return globalThread;
	}
}
