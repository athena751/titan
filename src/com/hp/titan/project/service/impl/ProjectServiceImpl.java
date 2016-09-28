package com.hp.titan.project.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.app.exception.BaseException;
import com.hp.quix.dao.QuixDao;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.common.vo.DefectVo;
import com.hp.titan.common.vo.SprintCommonVo;
import com.hp.titan.common.vo.UserstoryVo;
import com.hp.titan.mytitan.model.CommitPath;
import com.hp.titan.mytitan.model.CommitReport;
import com.hp.titan.mytitan.model.ProjectRallyQuix;
import com.hp.titan.mytitan.model.Reportary;
import com.hp.titan.mytitan.model.UserstoryInfo;
import com.hp.titan.mytitan.vo.CodeReportVo;
import com.hp.titan.mytitan.vo.MyCommitVo;
import com.hp.titan.project.dao.DedicatedServerDao;
import com.hp.titan.project.dao.ProjectDao;
import com.hp.titan.project.dao.ProjectModuleDao;
import com.hp.titan.project.model.CommitusDefect;
import com.hp.titan.project.model.DedicatedServer;
import com.hp.titan.project.model.Project;
import com.hp.titan.project.model.ProjectModule;
import com.hp.titan.project.service.ProjectService;
import com.hp.titan.rally.dao.RallyDao;
import com.hp.titan.test.dao.ParameterdataDao;
import com.hp.titan.test.model.ParameterData;

public class ProjectServiceImpl implements ProjectService{
	private ProjectDao projectDao;
	private ProjectModuleDao projectModuleDao;
	private DedicatedServerDao dedicatedServerDao;
	private ParameterdataDao parameterdataDao;
	private RallyDao rallyDao;
	
	// Get all project
	public List<Project> getAllProjectList()throws BaseDaoException{
		return projectDao.findAllProject();
	}
	
	// Get project by group
	public List<Project> getProjectByGroup(Integer groupId)throws BaseDaoException{
		return projectDao.findProjectByGroup(groupId);
	}
	
	
	// Save project
	public void saveProject(Project project) throws BaseDaoException{
		projectDao.saveOrUpdate(project);
	}
	
	//  Get project by id
	public Project getProjectById(String projectId) throws BaseDaoException{
		return projectDao.findById(projectId);
	}
	
	// Delete projects
	public void deleteProjectList(String id) throws BaseException {
//		this.deleteModulesByProjectId(id);

		Project project = projectDao.findById(id);
		project.setIsValid(1);
		project.setLastUpdateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
		project.setLastUpdate_Date(DateUtils.getCurrentDate());
		projectDao.update(project);// .delete(group);
	}

	// Check if the project exist
	public Boolean isExistProject(String projectName) throws BaseDaoException{
		Project project = projectDao.getProjectByProjectName(projectName);
		if(project == null){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void saveProjectModules(List<ProjectModule> moduleList)
			throws BaseException {
		for(ProjectModule module : moduleList){
			projectModuleDao.saveOrUpdate(module);
		}
	}

	@Override
	public List<ProjectModule> getProjectModuleList(String projectId)
			throws BaseException {
		return projectModuleDao.findByProjectId(projectId);
	}

	@Override
	public void deleteModulesByProjectId(String projectId) throws BaseException {
		List<ProjectModule> modules = projectModuleDao.findByProjectId(projectId);
		projectModuleDao.deleteAll(modules);
	}
	
	@Override
	public void saveDedicatedServers(
			List<DedicatedServer> serverList) throws BaseException {
		for(DedicatedServer server : serverList){
			dedicatedServerDao.saveOrUpdate(server);
		}
	}

	@Override
	public List<DedicatedServer> getDedicatedServerList(String projectId)
			throws BaseException {
		return dedicatedServerDao.findByProjectId(projectId);
	}

	@Override
	public void deleteDedicatedServersByProjectId(String projectId)
			throws BaseException {
		List<DedicatedServer> servers = dedicatedServerDao.findByProjectId(projectId);
		dedicatedServerDao.deleteAll(servers);
	}

	@Override
	public void deleteModule(ProjectModule module) throws BaseException {
		projectModuleDao.delete(module);
	}

	@Override
	public void deleteDedicatedServer(DedicatedServer server) throws BaseException {
		dedicatedServerDao.delete(server);
	}
	
	public List<ParameterData> findByProjectIdAndType(String projectId, String type)  throws BaseException{
		if(null != type && !"".equals(type)){
			return parameterdataDao.findByProjectIdAndType(projectId, type);
		}
		return parameterdataDao.findParameterDataByProjectId(projectId);
	}
	
	public List<ParameterData> findParameterDataByProjectId(String projectId) throws BaseDaoException{
		return parameterdataDao.findParameterDataByProjectId(projectId);
	}
	
	public void deleteParaDataByProjectId(String projectId)throws BaseDaoException{
		parameterdataDao.deleteParaDataByProjectId(projectId);
	}
	
	public void saveParaData(List<ParameterData> pdList) throws BaseDaoException{
		for(ParameterData pd : pdList){
			parameterdataDao.saveParaData(pd);
		}
	}
	
	public DedicatedServer getDedicatedServerById(String dedicatedServerId) throws BaseDaoException{
		return dedicatedServerDao.findById(dedicatedServerId);
	}
	
	public List<Project> getOtherProjectList(Set<Project> s) throws BaseException{
		return projectDao.getOtherProjectList(s);
	}
	
	public List<SprintCommonVo> getSprintByProjectName(String projectNameInRally)  throws BaseException, IOException{
		return rallyDao.getSprintByProjectName(projectNameInRally);
	}
	
	public ProjectRallyQuix getProjectRallyQuixById(String rallyQuixId) throws BaseException{
		return projectDao.getProjectRallyQuixById(rallyQuixId);
	}
	
	public List<ProjectRallyQuix>getProjectRallyQuixByType(String type) throws BaseException{
		return projectDao.getProjectRallyQuixByType(type);
	}
	
	public List<ProjectRallyQuix>getProjectRallyByProjectId(String projectId) throws BaseException{
		return projectDao.getProjectRallyByProjectId(projectId);
	}
	
	public List<ProjectRallyQuix>getProjectQuixByProjectId(String projectId) throws BaseException{
		return projectDao.getProjectQuixByProjectId(projectId);
	}
	
	public void doProjectAddRally(ProjectRallyQuix projectRallyQuix)throws BaseException{
		projectDao.doProjectAddRally(projectRallyQuix);
	}

	public List<Reportary> getReportaryByProjectId(String projectId) throws BaseException{
		return projectDao.getReportaryByProjectId(projectId);
	}
	
	public void saveReportary(Reportary r) throws BaseException{
		projectDao.saveReportary(r);
	}
	
	public List<Reportary> getAllReportary() throws BaseException{
		return projectDao.getAllReportary();
	}
	
	public Reportary getReportaryByReportaryId(String reportaryId) throws BaseException{
		return projectDao.getReportaryByReportaryId(reportaryId);
	}
	
	public void clearSyncedRepositoryData(String reportaryId)throws BaseException{
		List<MyCommitVo> myCommitVoList = projectDao.getCommitList("", reportaryId);
		if(null != myCommitVoList && myCommitVoList.size() != 0){
			Iterator<MyCommitVo> iter = myCommitVoList.iterator();
			while(iter.hasNext()){
				MyCommitVo vo = iter.next();
				projectDao.deleteChangePath(vo.getCommitreportId());
			}	
		}
		projectDao.deleteCommitReport(reportaryId);
	}
	
	public void saveCommitReport(CommitReport cr)throws BaseException{
		projectDao.saveCommit(cr);
	}
	
	public void saveCommitPath(List<CommitPath> cpList)throws BaseException{
		Iterator<CommitPath> iter = cpList.iterator();
		while(iter.hasNext()){
			CommitPath cp = iter.next();
			projectDao.saveCommitPath(cp);
		}
	}
	
	public void saveCommitUSDEList(List<CommitusDefect> commitUSDEList)throws BaseException{
		Iterator<CommitusDefect> iter = commitUSDEList.iterator();
		while(iter.hasNext()){
			CommitusDefect cd = iter.next();
			projectDao.saveCommitUSDE(cd);
		}
	}
	
	public List<DefectVo> getRallyDefectInfoByProjectModule(String projectName, String module) throws IOException{
		return rallyDao.getRallyDefectInfoByProjectModule(projectName, module);
	}
	
	public List<UserstoryVo> getRallyUSInofoByPorject(String projectName) throws IOException{
		return rallyDao.getRallyUSInofoByPorject(projectName);
	}
	
	public List<DefectVo> getQuixDefectInfoByProjectModule(String projectName, String module) throws IOException{
		QuixDao quixDao = new QuixDao();
		return quixDao.getQuixDefectInfoByProjectModule(projectName, module);
	}
	
	public List<DefectVo> getQuixForReport(String email, String startDate, String endDate, String projects) throws IOException{
		QuixDao quixDao = new QuixDao();
		return quixDao.getQuixForReport(email, startDate, endDate, projects);
	}
	public List<String> getCommitYearList(String reportaryId)throws BaseException{
		return projectDao.getCommitYearList(reportaryId);
	}
	
	public List<CodeReportVo> getAnnualReportList(List<String> yearList, String reportId)throws BaseException{
		List<CodeReportVo> crVoList = new ArrayList<CodeReportVo>();
		Iterator<String> iter = yearList.iterator();
		while(iter.hasNext()){
			String year = iter.next();
			CodeReportVo crVo = projectDao.getCommitAnnualData(year, reportId);
			crVo.setYear(year);
			crVoList.add(crVo);
		}
		return crVoList;
	}
	
	public String getMaxSvnVersion(String reportaryId)throws BaseException{
		return projectDao.getMaxSvnVersion(reportaryId);
	}
	
	public List<MyCommitVo> getCommitVo(String email, String reportaryId)throws BaseException{
		return projectDao.getCommitList(email, reportaryId);
	}
	
	public Boolean ifSvnRevisionExist(long revision, String reportaryId)throws BaseException{
		List<CommitReport> commitReport = projectDao.getCommitInfoByRevision(revision, reportaryId);
		if(null != commitReport && commitReport.size() != 0){
			return true;
		}
		return false;
	}
	
	public String getDefaultPeriod(String reportaryId)throws BaseException{
		String startDate = projectDao.getStartSvnDate(reportaryId);
		String endDate = projectDao.getEndSvnDate(reportaryId);
		if(!"null".equals(startDate) && !"null".equals(endDate)){
			return "Default from " + startDate + " to " + endDate;
		}
		return "No data";
	}
	
	public List<UserstoryInfo> getUSInfoByProjectName(String projectName) throws BaseException{
		return projectDao.getUserStoriesByName(projectName);
	}
	
	public List<String> getProjectRallyQuixByUserId(Integer userId) throws BaseDaoException{
		return projectDao.getprojectRallyQuixByUserId(userId);
	}
	
	public ProjectDao getProjectDao() {
		return projectDao;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public ProjectModuleDao getProjectModuleDao() {
		return projectModuleDao;
	}

	public void setProjectModuleDao(ProjectModuleDao projectModuleDao) {
		this.projectModuleDao = projectModuleDao;
	}

	public DedicatedServerDao getDedicatedServerDao() {
		return dedicatedServerDao;
	}

	public void setDedicatedServerDao(DedicatedServerDao dedicatedServerDao) {
		this.dedicatedServerDao = dedicatedServerDao;
	}

	public ParameterdataDao getParameterdataDao() {
		return parameterdataDao;
	}

	public void setParameterdataDao(ParameterdataDao parameterdataDao) {
		this.parameterdataDao = parameterdataDao;
	}

	public RallyDao getRallyDao() {
		return rallyDao;
	}

	public void setRallyDao(RallyDao rallyDao) {
		this.rallyDao = rallyDao;
	}

}
