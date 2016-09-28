package com.hp.titan.auth.model;

import java.util.ArrayList;
import java.util.List;

public class ViewMenuTree {
	private Menu menu1;
	private List<ViewMenu2Tree> menu2List = new ArrayList<ViewMenu2Tree>();
	private int menu3Count=0;
	public int getMenu3Count() {
		return menu3Count;
	}
	public void setMenu3Count(int menu3Count) {
		this.menu3Count = menu3Count;
	}
	public Menu getMenu1() {
		return menu1;
	}
	public void setMenu1(Menu menu1) {
		this.menu1 = menu1;
	}
	public List<ViewMenu2Tree> getMenu2List() {
		return menu2List;
	}
	public void setMenu2List(List<ViewMenu2Tree> menu2List) {
		this.menu2List = menu2List;
	}
	 
	
}
