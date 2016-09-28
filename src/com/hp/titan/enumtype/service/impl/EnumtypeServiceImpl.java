package com.hp.titan.enumtype.service.impl;

//import java.util.Iterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.titan.enumtype.dao.EnumTypeDao;
import com.hp.titan.enumtype.dao.EnumValueDao;
import com.hp.titan.enumtype.model.EnumType;
import com.hp.titan.enumtype.model.EnumValue;
import com.hp.titan.enumtype.service.EnumtypeService;


public class EnumtypeServiceImpl implements EnumtypeService{
	
   private EnumTypeDao enumTypeDao;
   private EnumValueDao enumValueDao;
   
   
   
   public List<EnumValue> getValueByType(Integer typeId) throws BaseDaoException{
	   return  enumValueDao.getEnumValueByTypeId(typeId);
	   
   }
   
   public List<EnumType> getAllType() throws BaseDaoException{
	   return  enumTypeDao.getAllType();	   
   }
   
   public void saveType(EnumType enumType) throws BaseDaoException{
	   
	   this.enumTypeDao.saveOrUpdate(enumType);
   }
   
   public EnumType getTypeById(Integer typeId) throws BaseDaoException{
	   return enumTypeDao.findById(typeId);
   }
   public void saveValue(EnumValue enumValue) throws BaseDaoException{
	   
	   this.enumValueDao.saveOrUpdate(enumValue);
   }
   
   public EnumValue getValueById(Integer valueId) throws BaseDaoException{
	   return enumValueDao.findById(valueId);
   }
   
   public EnumTypeDao getEnumTypeDao() {
	    return enumTypeDao;
   }
   public void setEnumTypeDao(EnumTypeDao enumTypeDao) {
	    this.enumTypeDao = enumTypeDao;
   }
   
   public EnumValueDao getEnumValueDao() {
	    return enumValueDao;
    }
   public void setEnumValueDao(EnumValueDao enumValueDao) {
	    this.enumValueDao = enumValueDao;
   }

	

	
}
	