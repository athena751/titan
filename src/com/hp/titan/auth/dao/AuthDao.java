package com.hp.titan.auth.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Role;
import com.iconclude.dharma.commons.util.StringUtils;


public class AuthDao extends DefaultBaseDao<Auth, String> {
	/**
	 * @param obj
	 * @throws BaseDaoException
	 */
	public void saveOrUpdate(Auth obj) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	/**
	 * 方法说明：查询所有的role
	 * @return
	 * @throws BaseDaoException
	 */
	public List<Auth> findAll(Auth authVo) throws BaseDaoException{
	 
		String hql = "from Auth a where a.isValid=0 ";
		if(authVo!=null){
			if(authVo.getMenuId()!=null){
				hql=hql+" and a.menuId="+authVo.getMenuId();
			}
		}
		return this.getHibernateTemplate().find(hql);
	}
	
	/**
	 * 通过菜单ID列表查询权限列表
	 * @return
	 * @throws BaseDaoException
	 */
	public List<Auth> findAllAuthByMenuId(List<Integer> menuIdList) throws BaseDaoException{
		 Criteria criteria = this.getSession().createCriteria(Auth.class, "auth");
		 criteria.add(Restrictions.eq("auth.isValid", 0));
		 criteria.add(Restrictions.in("auth.menuId", menuIdList));
		 return criteria.list();
	}
	
	
	public Auth findById(String id) throws BaseDaoException
	  {
	    return (Auth) getHibernateTemplate().get(Auth.class, id);
	  }
	
	public List<Auth> findByMenuId(Integer menuId) throws BaseDaoException
	  {
		String hql = "from Auth a where a.isValid=0 and a.menuId="+menuId;
		return this.getHibernateTemplate().find(hql);
	  }
	
	public List<Auth> findAuthByIdList(List<String> idList) throws BaseDaoException{
		 Criteria criteria = this.getSession().createCriteria(Auth.class, "auth");
		 criteria.add(Restrictions.in("auth.authId", idList));
		 return criteria.list();
	} 
}
