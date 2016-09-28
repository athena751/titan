package com.hp.titan.test.dao;

import java.util.List;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.test.model.DefaultParameter;
import com.hp.titan.test.model.ParameterData;

public class ParameterdataDao extends DefaultBaseDao<ParameterData, String> {


	public List<ParameterData> findByType(String type) throws BaseDaoException{
		String hql = "from ParameterData as pd where pd.projectId is null and pd.type = ?";
		List<ParameterData> parameterDataList = this.getHibernateTemplate().find(hql, type);
	    return parameterDataList;
	}
	
	public List<ParameterData> findAllParameterData() throws BaseDaoException{
		String hql = "from ParameterData as pd where pd.projectId is null";
		List<ParameterData> parameterDataList = this.getHibernateTemplate().find(hql);
	    return parameterDataList;
	}
	
	public List<ParameterData> findByProjectIdAndType(String projectId, String type) throws BaseDaoException{
		String hql = "from ParameterData as pd where pd.type = ? and pd.projectId = ?";
		List<ParameterData> parameterDataList = this.getHibernateTemplate().find(hql, type, projectId);
	    return parameterDataList;
	}
	
	public List<ParameterData> findParameterDataByProjectId(String projectId) throws BaseDaoException{
		String hql = "from ParameterData as pd where pd.projectId = ?";
		List<ParameterData> parameterDataList = this.getHibernateTemplate().find(hql, projectId);
	    return parameterDataList;
	}
	
	public ParameterData findParameterDataByParaName(String paraName) throws BaseDaoException{
		String hql = "from ParameterData as pd where pd.paradataName = ?";
		List<ParameterData> parameterDataList = this.getHibernateTemplate().find(hql, paraName);
	    return parameterDataList.get(0);
	}
	
	public void deleteParaDataByProjectId(String projectId)throws BaseDaoException{
		String hql = "Delete FROM ParameterData pd Where pd.projectId=?" ;
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, projectId) ;
		query.executeUpdate() ;
	}
	
	public void saveParaData(ParameterData pd) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(pd);
	}
	
	public void saveDefaultPara(DefaultParameter dp) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(dp);
	}
	
	public void deleteDefaultParaByUserId(int userId, String testjobId) throws BaseDaoException{
		String hql = "Delete FROM DefaultParameter dp Where dp.id.userId=? and dp.id.testjobId = ?" ;
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, String.valueOf(userId)) ;
		query.setString(1, testjobId) ;
		query.executeUpdate() ;
	}
	
	public List<DefaultParameter> getDefaultPara(int userId, String testjobId) throws BaseDaoException{
		String hql = "from DefaultParameter as dp where dp.id.userId = ? and dp.id.testjobId = ?";
		List<DefaultParameter> pefaultParameterList = this.getHibernateTemplate().find(hql, userId, testjobId);
	    return pefaultParameterList;
	}
}
