package com.hp.titan.enumtype.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.app.action.DefaultBaseAction;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.auth.model.Role;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.enumtype.model.EnumType;
import com.hp.titan.enumtype.model.EnumValue;
import com.hp.titan.enumtype.service.EnumtypeService;
import com.hp.titan.server.model.Server;


public class EnumtypeAction extends DefaultBaseAction {
	
	private List<EnumValue> enumValueList;
	private EnumtypeService enumtypeService;
	private List<EnumType> enumTypeList;
	private Integer typeId;
	private EnumType enumType;
	private EnumValue enumValue;
	private String type;
	private Integer enumId;
	
	
	public Integer getEnumId() {
		return enumId;
	}

	public void setEnumId(Integer enumId) {
		this.enumId = enumId;
	}

	public String doSearchEnumtype(){
		
		 try{
			   enumTypeList = enumtypeService.getAllType();
			   
			  }catch (BaseDaoException e) {
					e.printStackTrace();
			  }
		
		
		return SUCCESS;
	}
	
	public String goValueList(){
		
		 try{
			 
			 if(this.typeId != null&&!this.typeId.equals("")){
				 enumValueList = enumtypeService.getValueByType(typeId);
				 enumType = enumtypeService.getTypeById(typeId);
			  }
			   
			  }catch (BaseDaoException e) {
					e.printStackTrace();
			  }
		
		
		return SUCCESS;
		
	}
	
	public String goTypeAdd(){
		
		type = "create";
		
		enumType = new EnumType();	
		
		
		return SUCCESS;
		
	}
	
	public String doTypeSave(){
		
		try{			
			enumtypeService.saveType(enumType);
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}		
		return SUCCESS;	
		
	}
	
	public String goTypeUpdate(){
		
		type = "edit";
		
		try{
			if(this.typeId!=null && !this.typeId.equals("")){
				typeId = this.typeId;
				enumType = enumtypeService.getTypeById(typeId);	
				
			}
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}	
		
		return SUCCESS;	
		
		
		
	}
	
	public String goValueAdd(){
		type = "create";
		
		enumValue = new EnumValue();
		try{
			if(this.typeId!=null && !this.typeId.equals("")){
				typeId = this.typeId;
				enumType = enumtypeService.getTypeById(typeId);	
				
			}
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}	
		
		return SUCCESS;	
	}
	
	public String doValueSave(){
		
		try{	
			typeId = this.typeId;
			enumtypeService.saveValue(enumValue);
			
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}		
		return SUCCESS;	
	}
	
	public String goValueUpdate(){
		type = "edit";
		try{
			if(this.enumId!=null && !this.enumId.equals("")) {
				enumValue = enumtypeService.getValueById(this.enumId);
				enumType = enumtypeService.getTypeById(enumValue.getTypeId());
			}
		}catch (BaseDaoException e) {
			e.printStackTrace();
		}		
		return SUCCESS;	
		
		
	}
	



	public EnumtypeService getEnumtypeService() {
		return enumtypeService;
	}

	public void setEnumtypeService(EnumtypeService enumtypeService) {
		this.enumtypeService = enumtypeService;
	}



	public List<EnumType> getEnumTypeList() {
		return enumTypeList;
	}



	public void setEnumTypeList(List<EnumType> enumTypeList) {
		this.enumTypeList = enumTypeList;
	}

	public List<EnumValue> getEnumValueList() {
		return enumValueList;
	}

	public void setEnumValueList(List<EnumValue> enumValueList) {
		this.enumValueList = enumValueList;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public EnumType getEnumType() {
		return enumType;
	}

	public void setEnumType(EnumType enumType) {
		this.enumType = enumType;
	}

	public EnumValue getEnumValue() {
		return enumValue;
	}

	public void setEnumValue(EnumValue enumValue) {
		this.enumValue = enumValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}
