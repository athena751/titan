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
import com.hp.titan.test.vo.TestjobVo;
import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;

public class EnumValueDao extends DefaultBaseDao<EnumValue, Integer> {

	public EnumValue findById(Integer enumId) throws BaseDaoException{
		return (EnumValue) getHibernateTemplate().get(EnumValue.class, enumId);
	}	
	
	public void saveEnumValue(EnumValue enumValue)throws BaseDaoException  {
		this.getHibernateTemplate().saveOrUpdate(enumValue);
	}

	public void saveOrUpdate(EnumValue enumValue)throws BaseDaoException {
		try {
			this.getHibernateTemplate().saveOrUpdate(enumValue);
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	public List<EnumValue> getEnumValueByTypeId(Integer typeId) {
		String hql = "select v from EnumValue v where v.typeId = ?";
		return this.getHibernateTemplate().find(hql,typeId);
	}	
	
}
