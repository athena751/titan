package com.hp.titan.enumtype.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import com.hp.titan.auth.model.User;
import com.hp.titan.project.model.Project;
import com.hp.titan.enumtype.model.EnumType;
import com.hp.titan.enumtype.model.EnumValue;
import com.hp.titan.server.vo.CardVo;
import com.hp.titan.sprint.model.Sprint;
import com.hp.titan.test.vo.TestjobVo;
import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;

public class EnumTypeDao extends DefaultBaseDao<EnumType, String> {
	
	public List<EnumType> getAllType()throws BaseDaoException{
		String hql = "from EnumType as e";
		List<EnumType> enumType = this.getHibernateTemplate().find(hql);
		return enumType;
		
	}

	public EnumType findById(Integer typeId) throws BaseDaoException{
		return (EnumType) getHibernateTemplate().get(EnumType.class, typeId);
	}	
	
	public void saveEnumType(EnumType enumType)throws BaseDaoException  {
		this.getHibernateTemplate().saveOrUpdate(enumType);
	}
	
		public void saveOrUpdate(EnumType enumType)throws BaseDaoException {
		try {
			this.getHibernateTemplate().saveOrUpdate(enumType);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	public EnumType getEnumTypeByName(String typeName) {
		String hql = "select e from EnumType e where e.typeName = ?";
		List<EnumType> enumType = this.getHibernateTemplate().find(hql,typeName.toUpperCase());
		return enumType.size()>0?enumType.get(0):null;
	}	
	
}
