package com.hp.titan.project.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.hp.app.dao.DefaultBaseDao;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.auth.model.UserProject;
import com.hp.titan.mytitan.model.CommitPath;
import com.hp.titan.mytitan.model.CommitReport;
import com.hp.titan.mytitan.model.ProjectRallyQuix;
import com.hp.titan.mytitan.model.Reportary;
import com.hp.titan.mytitan.model.UserstoryInfo;
import com.hp.titan.mytitan.vo.CodeReportVo;
import com.hp.titan.mytitan.vo.MyCommitVo;
import com.hp.titan.project.model.CommitusDefect;
import com.hp.titan.project.model.Project;

public class ProjectDao extends DefaultBaseDao<Project, Integer>{

	public List<Project> findAllProject() throws BaseDaoException{
		String hql = "from com.hp.titan.project.model.Project as project where isValid=0 ";
		return this.getHibernateTemplate().find(hql);
	}
	
	public List<Project> findProjectByGroup(Integer groupId) throws BaseDaoException{
		String hql = "from com.hp.titan.project.model.Project as project where groupId = ? and isValid=0";
		return this.getHibernateTemplate().find(hql, groupId);
	}
	public void saveOrUpdate(Project project) throws BaseDaoException{
		//this.getHibernateTemplate().saveOrUpdate(this.getSession().merge(project));
		this.getHibernateTemplate().saveOrUpdate(project);
	}
	
	public Project findById(String id) throws BaseDaoException
	{
	    return (Project) getHibernateTemplate().get(Project.class, id);
	}
	
	public Project getProjectByProjectName(String projectName) throws BaseDaoException {
		String hql = "select p from Project p where p.projectName = ? and p.isValid = ?";
		List<Project> project = this.getHibernateTemplate().find(hql,projectName, 0);
		return project.size()>0?project.get(0):null;
	}
	
	public void saveUserProject(UserProject userProject) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(userProject);
	}
	
	public void deleteUserProject(Integer userId) throws BaseDaoException{
		String hql = "Delete FROM UserProject up Where up.id.userId=?" ;
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, userId.toString()) ;
		query.executeUpdate() ;
	}
	
	public List<Project> getOtherProjectList(Set<Project> s) throws BaseDaoException{
		String hql = "from com.hp.titan.project.model.Project as F where isValid=0 ";
		if(s!=null && s.size() != 0){
			Iterator<Project> it = s.iterator();
			hql += " and F.projectId not in(";
			while(it.hasNext()){
				Project p = it.next();
				hql += "'" + p.getProjectId()+"',";
			}
			hql += ")";
		}
		return this.getHibernateTemplate().find(hql.replace(",)", ")"));
	}
	
	public ProjectRallyQuix getProjectRallyQuixById(String rallyQuixId) throws BaseDaoException{
		String hql = "from ProjectRallyQuix prq where prq.rallyquixId = ?";
		List<ProjectRallyQuix> projectRallyQuixList = this.getHibernateTemplate().find(hql, rallyQuixId);
		return projectRallyQuixList.size()>0?projectRallyQuixList.get(0):null;
	}
	
	public List<ProjectRallyQuix>getProjectRallyQuixByType(String type) throws BaseDaoException{
		String hql = "";
		if(null != type && "rally".equals(type)){
			hql = "from ProjectRallyQuix prq WHERE prq.nameinQuix IS NULL";
		}
		else{
			hql = "from ProjectRallyQuix prq WHERE prq.nameinRally IS NULL";
		}
		List<ProjectRallyQuix> projectRallyQuixList = this.getHibernateTemplate().find(hql);
		return projectRallyQuixList;
	}
	
	public List<ProjectRallyQuix>getProjectRallyByProjectId(String projectId) throws BaseDaoException{
		String sql = "SELECT prq.NAME_IN_RALLY, prq.PROJECT_ID, prq.AUTO_REFRESH, prq.COMPONENT, prq.RALLY_QUIX_ID FROM project_rally_quix AS prq WHERE prq.NAME_IN_QUIX IS NULL AND prq.IS_VALID = 0 AND prq.PROJECT_ID = '" + projectId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<ProjectRallyQuix> projectRallyQuixList = new ArrayList<ProjectRallyQuix>();
		for (Object[] object : objects){
			ProjectRallyQuix prq = new ProjectRallyQuix();
			prq.setNameinRally(String.valueOf(object[0]));
			prq.setProjectId(String.valueOf(object[1]));
			prq.setAutoRefresh(String.valueOf(object[2]));
			prq.setComponent(String.valueOf(object[3]));
			prq.setRallyquixId(String.valueOf(object[4]));
			projectRallyQuixList.add(prq);
		}
		return projectRallyQuixList;
	}
	
	public List<ProjectRallyQuix>getProjectQuixByProjectId(String projectId) throws BaseDaoException{
		String sql = "SELECT prq.NAME_IN_QUIX, prq.PROJECT_ID, prq.AUTO_REFRESH, prq.COMPONENT, prq.RALLY_QUIX_ID FROM project_rally_quix AS prq WHERE prq.NAME_IN_RALLY IS NULL AND prq.IS_VALID = 0 AND prq.PROJECT_ID = '" + projectId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<ProjectRallyQuix> projectRallyQuixList = new ArrayList<ProjectRallyQuix>();
		for (Object[] object : objects){
			ProjectRallyQuix prq = new ProjectRallyQuix();
			prq.setNameinQuix(String.valueOf(object[0]));
			prq.setProjectId(String.valueOf(object[1]));
			prq.setAutoRefresh(String.valueOf(object[2]));
			prq.setComponent(String.valueOf(object[3]));
			prq.setRallyquixId(String.valueOf(object[4]));
			projectRallyQuixList.add(prq);
		}
		return projectRallyQuixList;
	}
	
	public void doProjectAddRally(ProjectRallyQuix projectRallyQuix)throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(projectRallyQuix);
	}
	
	public List<Reportary> getReportaryByProjectId(String projectId) throws BaseDaoException{
		String sql = "SELECT r.REPORTARY_ID, r.REMARK, r.SVN_URL, pm.MODULE_NAME, r.AUTO_REFRESH FROM reportary AS r, project_module AS pm WHERE r.MODULE_ID = pm.MODULE_ID AND r.IS_VALID = 0 AND r.PROJECT_ID = '" + projectId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<Reportary> reportaryList = new ArrayList<Reportary>();
		for (Object[] object : objects){
			Reportary r = new Reportary();
			r.setReportaryId(String.valueOf(object[0]));
			r.setRemark(String.valueOf(object[1]));
			r.setSvnUrl(String.valueOf(object[2]));
			r.setModuleName(String.valueOf(object[3]));
			r.setAutoRefresh(String.valueOf(object[4]));
			r.setProjectId(projectId);
			reportaryList.add(r);
		}
		return reportaryList;
	}
	
	public void saveReportary(Reportary r) throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(r);
	}
	
	public List<Reportary> getAllReportary() throws BaseDaoException{
		String hql = "from Reportary r where r.isValid=0";
		return this.getHibernateTemplate().find(hql);
	}
	
	public Reportary getReportaryByReportaryId(String reportaryId) throws BaseDaoException{
		String hql = "from Reportary r where r.isValid=0 and r.reportaryId=?";
		List<Reportary> reportaryList = this.getHibernateTemplate().find(hql, reportaryId);
		return reportaryList.size()>0?reportaryList.get(0):null;
	}
	
	public List<MyCommitVo> getCommitList(String email, String reportaryId)throws BaseDaoException{
		String sql = "SELECT cr.CODE_CHANGED, cr.`COMMENT`, cr.COMMITTEDBY, cr.COMMIT_REPORT_ID, cr.COMMIT_TIME, cr.REVISION, p.PROJECT_NAME, cr.COMMIT_REPORT_ID "
		       + "from commit_report as cr, reportary as r, project as p "
		       + "where r.PROJECT_ID = p.PROJECT_ID and cr.REPORTARY_ID = r.REPORTARY_ID";
		if(null != email && !"".equals(email)){
			sql +=" and cr.COMMITTEDBY = '" + email + "'";
		}
		if(null != reportaryId && !"".equals(reportaryId)){
			sql +=" and r.REPORTARY_ID = '" + reportaryId + "'";
		}
		
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<MyCommitVo> voList = new ArrayList<MyCommitVo>();
		for (Object[] object : objects){
			MyCommitVo vo = new MyCommitVo();
			vo.setCodeChange(object[0].toString());
			vo.setComment(object[1].toString());
			vo.setCommittedBy(object[2].toString());
			vo.setCommitreportId(object[3].toString());
			vo.setCommitTime(object[4].toString());
			vo.setRevision(object[5].toString());
			vo.setProjectName(object[6].toString());
			vo.setCommitreportId(object[7].toString());
			voList.add(vo);			
		}
		return voList;
	}
	
	public void deleteChangePath(String commitReportId)throws BaseDaoException{
		String hql = "delete from CommitPath cp where cp.commitreportId = ?";
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, commitReportId) ;
		query.executeUpdate() ;
	}
	
	public void deleteCommitReport(String reportaryId)throws BaseDaoException{
		String hql = "delete from CommitReport cr where cr.reportaryId = ?";
		Query query = this.getSession().createQuery(hql.toString());
		query.setString(0, reportaryId) ;
		query.executeUpdate() ;
	}
	
	public String getMaxSvnVersion(String reportaryId)throws BaseDaoException{
		String sql = "SELECT MAX(cr.REVISION) FROM commit_report cr WHERE cr.REPORTARY_ID ='" + reportaryId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null != objects && objects.size() != 0){
			if(null != objects.get(0)){
				return String.valueOf(objects.get(0));
			}
		}
		return "0";
	}
	
	public List<CommitReport>getCommitInfoByRevision(long revision, String reportaryId)throws BaseDaoException{
		String hql = "from CommitReport cr where cr.revision=? and cr.reportaryId=?";
		List<CommitReport> commitReport = this.getHibernateTemplate().find(hql, Integer.valueOf(String.valueOf(revision)), reportaryId);
		return commitReport.size()>0?commitReport:null;
	}
	
	public void saveCommit(CommitReport cr)throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(cr);
	}
	
	public void saveCommitPath(CommitPath cp)throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(cp);
	}
	
	public void saveCommitUSDE(CommitusDefect cd)throws BaseDaoException{
		this.getHibernateTemplate().saveOrUpdate(cd);
	}
	
	public String getStartSvnDate(String reportId)throws BaseDaoException{
		String sql = "SELECT MIN(cr.COMMIT_TIME) FROM commit_report as cr WHERE cr.REPORTARY_ID ='" + reportId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		Object obj = query.uniqueResult();
		return String.valueOf(obj);
	}
	
	public String getEndSvnDate(String reportId)throws BaseDaoException{
		String sql = "SELECT MAX(cr.COMMIT_TIME) FROM commit_report as cr WHERE cr.REPORTARY_ID ='" + reportId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		Object obj = query.uniqueResult();
		return String.valueOf(obj);
	}
	
	public List<String> getCommitYearList(String reportaryId)throws BaseDaoException{
		String sql = "SELECT DISTINCT YEAR(cr.COMMIT_TIME)  FROM commit_report AS cr WHERE cr.REPORTARY_ID ='" + reportaryId + "'";
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		List<String> resList = new ArrayList<String>();
		for (Object object : objects){
			String res = object.toString();
			resList.add(res);			
		}
		return resList;
	}
	
	public CodeReportVo getCommitAnnualData(String year, String reportId){
		String sql = "select SUM(cr.CODE_CHANGED) as cnt,month(cr.COMMIT_TIME) as monthflg from commit_report as cr where year(cr.COMMIT_TIME)=" + year + " AND cr.REPORTARY_ID='" + reportId + "' group by monthflg"; 
		Query query = this.getSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		if(null == objects || objects.size() == 0){
			return null;
		}
		Iterator<Object[]> iter = objects.iterator();
		CodeReportVo crVo = new CodeReportVo();
		while(iter.hasNext()){
			Object[] obj = iter.next();
			switch (Integer.valueOf(obj[1].toString())){
				case 1:
					crVo.setJanNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 2:
					crVo.setFebNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 3:
					crVo.setMatNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 4:
					crVo.setAprNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 5:
					crVo.setMayNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 6:
					crVo.setJunNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 7:
					crVo.setJulNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 8:
					crVo.setAugNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 9:
					crVo.setSepNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 10:
					crVo.setOctNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 11:
					crVo.setNovNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
				case 12:
					crVo.setDecNum(Integer.valueOf(obj[0].toString().replace(".0", "")));
					break;
			}
		}
		return crVo;
	}
	
	public List<UserstoryInfo> getUserStoriesByName(String projectName)throws BaseDaoException{
		String hql = "from UserstoryInfo us where us.projectinRally = ?";
		return this.getHibernateTemplate().find(hql,projectName);
	}
	
	public List<String> getprojectRallyQuixByUserId(Integer userId)throws BaseDaoException{
		
		String sql = "SELECT prq.NAME_IN_QUIX from project_rally_quix as prq where prq.PROJECT_ID IN (";
		       sql += "SELECT tup.PROJECT_ID from TITAN_USER_PROJECT tup where tup.USER_ID = '" + userId + "')";
		Query query = this .getSession().createSQLQuery(sql);
		List<Object> objects = query.list();
		List<String> projectNames = new ArrayList<String>();
		if(objects!=null&&objects.size()!=0){
			for (Object object : objects){
				if (object !=null){
					projectNames.add(object.toString());
				}
			}
		}
		return projectNames;
	}
}