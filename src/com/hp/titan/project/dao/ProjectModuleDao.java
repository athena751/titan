package com.hp.titan.project.dao;

import java.util.List;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.project.model.ProjectModule;

public class ProjectModuleDao extends DefaultBaseDao<ProjectModule, Integer> {

	public void saveOrUpdate(ProjectModule module) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(module);
	}

	public List<ProjectModule> findByProjectId(String projectId) throws BaseDaoException{
		String hql = "from ProjectModule as pm where pm.projectId = ?";
		List<ProjectModule> moduleList = this.getHibernateTemplate().find(hql, projectId);
	    return moduleList;
	}
	
	public void deleteAll(List<ProjectModule> modules) throws BaseDaoException{
		this.getHibernateTemplate().deleteAll(modules);
	}
}
