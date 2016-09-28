package com.hp.titan.auth.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hp.app.common.page.Page;
import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.vo.UserVo;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.mytitan.model.ProjectRallyQuix;
 


public class UserDao extends DefaultBaseDao<User, Integer> {
	
	/**
 	 *保存
	 */
	public void saveOrUpdate(User user) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(user);
	}
	
	/**
	 * 通过Id查询用户
	 * @param id
	 * @return
	 */
	public User findById(Integer id) throws BaseDaoException
	{
	    return (User) getHibernateTemplate().get(User.class, id);
	}

	/**
	 * 用户名和用户码进行映射
	 * @return
	 */
	/*
	public Map<String, String> findUserName2UserCode() {
		Map<String, String> map = new HashMap<String, String>();
		String hql = "from User u";
		List<User> list = this.getHibernateTemplate().find(hql);
		for (User u : list) {
			map.put(u.getUserCode(), u.getUserName());
		}
		return map;
	}*/
	
	/**
	 * 获得所有用户带分页
	 * @return
	 */
	public Page getAllUserByPage(UserVo userVo,Page page) throws BaseDaoException {
		String hql="from User u where 1=1 and isValid=0 ";
		if(!UserSessionUtil.getUser().getUserCode().equals("ADMIN")){
			hql=hql+" and u.userCode!='ADMIN'";
		}
		if(userVo!=null){
			if (!StringUtils.isEmpty(userVo.getUserCode())) {
				hql=hql+" and u.userCode  =:userCode ";
			}
		}
		Query countQuery=this.getSession().createQuery(hql);
		if(userVo!=null){
			if (!StringUtils.isEmpty(userVo.getUserCode())) {
				countQuery.setParameter("userCode", userVo.getUserCode().toUpperCase());
			}
		}
		Integer count =countQuery.list().size();
		page.setDataCount(count);
		if(count.intValue()!=0){
			Query query = this.getSession().createQuery(hql);
			if(userVo!=null){
				if (!StringUtils.isEmpty(userVo.getUserCode())) {
					query.setParameter("userCode", userVo.getUserCode().toUpperCase());
				}
			}
			System.out.println("first"+ (page.getFirstRow()-1) +"size"+page.getPageSize()+"pageNo"+page.getPageNo());
			query.setFirstResult(page.getFirstRow()-1);
			query.setMaxResults(page.getPageSize());
			List<User> userList = query.list();
			page.setPageData(userList);
		}
		
		return page;
	}
	
	public List<User> findUserByIdList(List<Integer> idList) throws BaseDaoException{
		 Criteria criteria = this.getSession().createCriteria(User.class, "user");
		 criteria.add(Restrictions.in("user.userId", idList));
		 return criteria.list();
	} 
	
	
	
	
	
	
	/**
	 * 获得所有用户
	 * @return
	 */
	public List<User> getAllUserList() throws BaseDaoException{
		String hql = "select u from User u where 1=1 ";
		if(!UserSessionUtil.getUser().getUserCode().equals("ADMIN")){
			hql=hql+" and u.userCode!='ADMIN'";
		}
		List<User> user = this.getHibernateTemplate().find(hql);
		return user;
	}
	
	/**
	 * 获得所有用户
	 * @return
	 */
	public List<User> getAllUserListForSync() throws BaseDaoException{
		String hql = "select u from User u where u.userCode!='ADMIN' and u.userCode!='N/A'";
		List<User> user = this.getHibernateTemplate().find(hql);
		return user;
	}
	

	/**
	 * 根据查询条件获得所有用户
	 * @return
	 */
	public List<User> getAllUserByUserVo(UserVo userVo) throws BaseDaoException {
		StringBuffer sb =new StringBuffer();
		sb.append("from User u where 1=1 and isValid=0  ");
		sb.append(" and u.userCode!='N/A'");
		if(null != UserSessionUtil.getUser() && !UserSessionUtil.getUser().getUserCode().equals("ADMIN")){
			sb.append(" and u.userCode!='ADMIN'");
		}
		if(userVo!=null){
			if (!StringUtils.isEmpty(userVo.getUserCode())) {
				sb.append(" and u.userCode  =:userCode ");
			}
			if (!StringUtils.isEmpty(userVo.getConfirmUserId())) {
				sb.append(" and u.confirmUserId  =:confirmUserId ");
			}
			if(!StringUtils.isEmpty(userVo.getStatus())){
				sb.append(" and u.status = :status");
			}
		}
		String hql = sb.toString();
		Query query=this.getSession().createQuery(hql);
		if(userVo!=null){
			if (!StringUtils.isEmpty(userVo.getUserCode())) {
				query.setParameter("userCode", userVo.getUserCode().toUpperCase());
			}
			if (!StringUtils.isEmpty(userVo.getConfirmUserId())) {
				query.setParameter("confirmUserId", Integer.valueOf(userVo.getConfirmUserId()));
			}
			if(!StringUtils.isEmpty(userVo.getStatus())){
				query.setParameter("status", userVo.getStatus());
			}
		}
		List<User> users =query.list();
		return users;
	}
	
	/**
	 * Get all users bygroupId
	 * @return
	 */
	public List<User> getAllUserByGroupId(String groupId) throws BaseDaoException {
		String hql = "select u from User u,Group g where u in elements(g.users) and g.groupId='"+groupId+"' and u.isValid=0";
		if(null != UserSessionUtil.getUser() && !UserSessionUtil.getUser().getUserCode().equals("ADMIN")){
			hql += " and u.userCode!='ADMIN'";
		}
		return this.getHibernateTemplate().find(hql);
	}
	
	/**
	 * Get all users bygroupId except the login user
	 * @return
	 */
	
	public List<User> getUserListByGroup(String groupId) throws BaseDaoException {
		String hql = "select u from User u,Group g where u in elements(g.users) and g.groupId='"+groupId+"' and u.isValid=0";
		if(null != UserSessionUtil.getUser() && !UserSessionUtil.getUser().getUserCode().equals("ADMIN")){
			hql += " and u.userCode!='ADMIN' and  u.userId !=" + UserSessionUtil.getUser().getUserId() ;
		}
		return this.getHibernateTemplate().find(hql);
	}
	
	public User getUserByUserCode(String userCode) throws BaseDaoException {
		String hql = "select u from User u where u.userCode = ?";
		List<User> user = this.getHibernateTemplate().find(hql,userCode.toUpperCase());
		return user.size()>0?user.get(0):null;
	}
	
	public User getUserByEmail(String email) throws BaseDaoException {
		String hql = "select u from User u where u.mail = ? and isValid=0";
		List<User> user = this.getHibernateTemplate().find(hql,email);
		return user.size()>0?user.get(0):null;
	}
	
	public Boolean ifUserCodeExiest(String userCode) throws  BaseDaoException{
		String sql = "SELECT u.USER_ID FROM titan_user AS u WHERE u.USER_CODE = '" + userCode + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return false;
		}
		return true;
	}
	 
	public Boolean ifUserEmailExiest(String email) throws  BaseDaoException{
		String sql = "SELECT u.USER_ID FROM titan_user AS u WHERE u.MAIL = '" + email + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return false;
		}
		return true;
	}
	
	public List<User> getManagerByGroup(String groupId) throws BaseDaoException{
		String sql = " SELECT u.USER_ID, u.MAIL, u.USER_CODE FROM titan_role As r, titan_user AS u, titan_user_role AS ur, titan_group AS g, titan_user_group AS ug WHERE r.ROLE_ID = ur.ROLE_ID  AND ur.USER_ID = u.USER_ID AND g.GROUP_ID = ug.GROUP_ID AND ug.USER_ID = u.USER_ID AND u.IS_VALID = 0 AND r.ROLE_NAME = 'MANAGER' AND g.GROUP_ID = " + groupId;
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<User> managerList = new ArrayList<User>();
		for (Object[] object : objects){
			User u = new User();
			u.setUserId(Integer.valueOf(String.valueOf(object[0])));
			u.setMail(String.valueOf(object[1]));
			u.setUserCode(String.valueOf(object[2]));
			managerList.add(u);
		}
		return managerList;
	}
   
   /**
	 * 方法说明：根据id序列返回用户对象集合
	 * @param idList
	 * @return
	 * @throws BaseDaoException
	 */
	@SuppressWarnings("unchecked")
	public List<User> findbyIdList(List<Integer> idList){
		DetachedCriteria dCriteria = DetachedCriteria.forClass(User.class);
		 dCriteria.add(Restrictions.in("id", idList));
		 return findAllByCriteria(dCriteria);
	   } 
	

 



}
