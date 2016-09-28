package com.hp.titan.auth.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.User;
import com.hp.titan.auth.model.ViewUserInfo;
import com.hp.titan.auth.vo.UserVo;


public class ViewUserInfoDao extends DefaultBaseDao<ViewUserInfo, Integer> {

	/**
	 * 获得所有用户视图
	 * 
	 * @return
	 */
	public List<ViewUserInfo> getAllUserViewList(UserVo userVo) throws BaseDaoException {
		String hql="from ViewUserInfo user where 1=1 ";
		if(userVo!=null){
			if (!StringUtils.isEmpty(userVo.getUserCode())) {
				hql=hql+" and user.userCode  =:userCode ";
			}
		}
		hql=hql+" order by user.userCode";
		
		List<ViewUserInfo> users =this.getHibernateTemplate().find(hql);
		 for(int i =0 ; i<users.size();i++){
			 ViewUserInfo  viewUsser =  users.get(i);
		 }
		return users;
	}

 

	public ViewUserInfo getUserByUserCode(String userCode) throws BaseDaoException {
		String hql = "select u from ViewUserInfo u where u.userCode = ?";
		List<ViewUserInfo> user = this.getHibernateTemplate().find(hql, userCode.toUpperCase());
		return user.size() != 0 ? user.get(0) : null;
	}

	 
 
}
