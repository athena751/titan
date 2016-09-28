package com.hp.app.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ScanConfig {
	/** 扫描状态开闭伐**/
	private boolean scanState=true;		//默认开启扫描功能
	private boolean storeScanState=true;		//存储默认开启扫描功能
	@SuppressWarnings("unchecked")
	private Map<String,Callable> map =new HashMap<String,Callable>();//记录运行线程
	private static ScanConfig instance = null;
	private ScanConfig() {
	}

	public static synchronized ScanConfig getInstance() {
		if (instance == null) {
			instance = new ScanConfig();
		}
		return instance;

	}

	public boolean getScanState() {
		return scanState;
	}

	public void setScanState(boolean scanState) {
		this.scanState = scanState;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Callable> getMap() {
		return map;
	}

	@SuppressWarnings("unchecked")
	public void setMap(Map<String, Callable> map) {
		this.map = map;
	}

	public boolean isStoreScanState() {
		return storeScanState;
	}

	public void setStoreScanState(boolean storeScanState) {
		this.storeScanState = storeScanState;
	}
}
