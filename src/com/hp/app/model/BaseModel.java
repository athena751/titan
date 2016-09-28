/**   
 * @Title: BaseModel.java
 * @Package com.hp.app.model
 * @Description: TODO
 * @author  xu.yang@hp.com
 * @date 2010-12-28 下午04:51:57
 * @version V1.0   
 */
package com.hp.app.model;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @ClassName: BaseModel
 * @Description: TODO
 * @author xu.yang@hp.com
 * @date 2010-12-28 下午04:51:57
 */
public abstract class BaseModel implements Serializable {
	
	protected Timestamp createDate;
	
	protected Integer createUser;
	
	protected Timestamp lastUpdate_Date;
	
	protected Integer lastUpdateUser;
	


	@Override
	public String toString() {
		return "BaseModel [createBy=" + createUser + ", createTime=" + createDate
				+ ", lastUpdateBy=" + lastUpdateUser + ", lastUpdateTime="
				+ /*lastUpdateDate + */"]";
	}




	public Integer getCreateUser() {
		return createUser;
	}


	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}


	public Integer getLastUpdateUser() {
		return lastUpdateUser;
	}


	public void setLastUpdateUser(Integer lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}




	public Timestamp getCreateDate() {
		return createDate;
	}




	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}




	public Timestamp getLastUpdate_Date() {
		return lastUpdate_Date;
	}




	public void setLastUpdate_Date(Timestamp lastUpdateDate) {
		lastUpdate_Date = lastUpdateDate;
	}


}
