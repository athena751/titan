package com.hp.titan.sprint.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.project.model.Project;
import com.hp.titan.sprint.model.Sprint;
import com.hp.titan.sprint.model.SprintId;

public class SprintDao extends DefaultBaseDao<Sprint, Integer>{

	public List<Sprint> findAllSprint(Set<Project> s) throws BaseDaoException{
		String hql = "from com.hp.titan.sprint.model.Sprint as sprint where isValid=0 ";
		if(s!=null && s.size() != 0){
			Iterator<Project> it = s.iterator();
			hql += " and sprint.id.projectId in(";
			while(it.hasNext()){
				Project p = it.next();
				hql += "'" + p.getProjectId()+"',";
			}
			hql += ")";
		}
		return this.getHibernateTemplate().find(hql.replace(",)", ")"));
	}
	
//	public List<Sprint> findSprintByProject(String projectId) throws BaseDaoException{
//		String hql = "from com.hp.titan.sprint.model.Sprint as sprint where sprint.projectId =? and sprint.isValid=0";		
//		return this.getHibernateTemplate().find(hql, projectId);
//	}
//	
	public List<Sprint> findSprintByProject(String projectId) throws BaseDaoException{
		String hql = "from Sprint as sp where isValid=0 and sp.id.projectId = ? order by sp.startDate";
		List<Sprint> sprintList = this.getHibernateTemplate().find(hql, projectId);
	    return sprintList;
	}
	
	public List<Sprint> findSprints() throws BaseDaoException{
		String hql = "from Sprint as sp where isValid=0 ";
		List<Sprint> sprintList = this.getHibernateTemplate().find(hql);
	    return sprintList;
	}
	
	// find sprints by some condition
	public List<Sprint> findSprint(Sprint spring) throws BaseDaoException{
		StringBuffer hql = new StringBuffer();
		hql.append("from com.hp.titan.sprint.model.Sprint as sprint where isValid=0 ");
		if(spring != null){
			if (!StringUtils.isEmpty(spring.getId().getProjectId())) {
				hql.append(" and sprint.id.projectId  =:projectId ");
			}
		}
		Query query=this.getSession().createQuery(hql.toString());
		if(spring != null){
			if (!StringUtils.isEmpty(spring.getId().getProjectId())) {
				query.setParameter("projectId", spring.getId().getProjectId());
			}
		}
		List<Sprint> res =query.list();
		return res;
	}
	
	public void saveOrUpdate(Sprint sprint) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(this.getSession().merge(sprint));
	}
	
	public Sprint findById(SprintId id) throws BaseDaoException
	{
	    return (Sprint) getHibernateTemplate().get(Sprint.class, id);
	}
	
	public Sprint getSprintBySprintName(String sprintName) throws BaseDaoException {
		String hql = "select p from Sprint p where p.sprintName = ? and p.isValid = ?";
		List<Sprint> sprint = this.getHibernateTemplate().find(hql,sprintName, 0);
		return sprint.size()>0?sprint.get(0):null;
	}
}