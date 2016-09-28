package com.hp.titan.auth.model;

// default package
// Generated 2010-5-26 17:40:21 by Hibernate Tools 3.3.0.GA

import java.util.HashSet;
import java.util.Set;

/**
 * 权限表
 */
public class Menu implements java.io.Serializable {

	
	private Integer menuId;
	private Integer menuLevel;
	private String menuName;
	private String menuCode;
	private Integer supperMenuId;
	private Integer isValid=0;
	private boolean ischeckedMenu=false;
	private String actionUrl;
	private String imgUrl;
	public Menu() {}
	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}


	public String getImgUrl() {
		return imgUrl;
	}


	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public boolean isIscheckedMenu() {
		return ischeckedMenu;
	}

	public void setIscheckedMenu(boolean ischeckedMenu) {
		this.ischeckedMenu = ischeckedMenu;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getSupperMenuId() {
		return supperMenuId;
	}

	public void setSupperMenuId(Integer supperMenuId) {
		this.supperMenuId = supperMenuId;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}


}
