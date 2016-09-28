package com.hp.titan.server.vo;

import com.hp.app.vo.BaseVo;

public class ReDeviceVo extends BaseVo {

	private Integer freeNum;
	private Integer reserveNum;
	private String serverType;
	private String cardType;
	private String storageType;
	private Integer cardNum;
	private Integer storageNum;
	private Integer serverTotal;
	private Integer cardTotal;
	private Integer storageTotal;
	public Integer getFreeNum() {
		return freeNum;
	}
	public void setFreeNum(Integer freeNum) {
		this.freeNum = freeNum;
	}
	public Integer getReserveNum() {
		return reserveNum;
	}
	public void setReserveNum(Integer reserveNum) {
		this.reserveNum = reserveNum;
	}
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public Integer getCardNum() {
		return cardNum;
	}
	public void setCardNum(Integer cardNum) {
		this.cardNum = cardNum;
	}
	public Integer getStorageNum() {
		return storageNum;
	}
	public void setStorageNum(Integer storageNum) {
		this.storageNum = storageNum;
	}
	public Integer getServerTotal() {
		return serverTotal;
	}
	public void setServerTotal(Integer serverTotal) {
		this.serverTotal = serverTotal;
	}
	public Integer getCardTotal() {
		return cardTotal;
	}
	public void setCardTotal(Integer cardTotal) {
		this.cardTotal = cardTotal;
	}
	public Integer getStorageTotal() {
		return storageTotal;
	}
	public void setStorageTotal(Integer storageTotal) {
		this.storageTotal = storageTotal;
	}	
	
}
