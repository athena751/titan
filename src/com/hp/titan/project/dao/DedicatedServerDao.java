package com.hp.titan.project.dao;

import java.util.List;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.project.model.DedicatedServer;

public class DedicatedServerDao extends DefaultBaseDao<DedicatedServer, String> {

	public void saveOrUpdate(DedicatedServer server) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(server);
	}

	public List<DedicatedServer> findByProjectId(String projectId) throws BaseDaoException{
		String hql = "from DedicatedServer as ds where ds.projectId = ?";
		List<DedicatedServer> serverList = this.getHibernateTemplate().find(hql, projectId);
	    return serverList;
	}
	
	public DedicatedServer findById(String dedicatedServerId) throws BaseDaoException{
		String hql = "from DedicatedServer as ds where ds.serverId = ?";
		List<DedicatedServer> serverList = this.getHibernateTemplate().find(hql, dedicatedServerId);
	    return serverList.get(0);
	}
	
	public void deleteAll(List<DedicatedServer> servers) throws BaseDaoException{
		this.getHibernateTemplate().deleteAll(servers);
	}
}
