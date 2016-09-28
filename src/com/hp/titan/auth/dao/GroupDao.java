package com.hp.titan.auth.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.Auth;
import com.hp.titan.auth.model.Group;
import com.hp.titan.auth.model.Role;


public class GroupDao extends DefaultBaseDao<Group, Integer> {
	
	/**
	 * 方法说明：查询所有的组织GROUP
	 * @return
	 * @throws BaseDaoException
	 */
	public List<Group> findAllGroup() throws BaseDaoException{
	 
		String hql = "from com.hp.titan.auth.model.Group as group where isValid=0 ";
		return this.getHibernateTemplate().find(hql);
	}
	
	
	/**
	 * 通过Id查询组
	 * @param id
	 * @return
	 */
	public Group findById(Integer id) throws BaseDaoException
	{
	    return (Group) getHibernateTemplate().get(Group.class, id);
	}
	
	
	
	/**
	 * 通过idList查询组列表
	 * @param idList
	 * @return
	 */
	public List<Group> findAuthByIdList(List<Integer> idList) throws BaseDaoException{
		 Criteria criteria = this.getSession().createCriteria(Group.class, "group");
		 criteria.add(Restrictions.in("group.groupId", idList));
		 return criteria.list();
	} 
	
	/**
 	 *保存
	 */
	public void saveOrUpdate(Group group) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(group);
	}
	
	
	/**
	 * 方法说明：根据id得到group以及user集合
	 * @param roleId
	 * @return
	 * @throws BaseDaoException
	 */
	public Group findGroupAndUserList(Integer groupId) throws BaseDaoException{
		String hql = "from Group as group where group.groupId in (:groupId)";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("groupId", groupId);
		List<Group> groupList =  query.list();
		if(groupList.size() > 0){
			Group  group = (Group)groupList.get(0);
			Set tmp = group.getUsers();
			/*****  获取延迟加载的用户集合 *******/
			int size = tmp.size();
			return group;
		}
		return null;
	}
}
