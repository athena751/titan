package com.hp.titan.sprint.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.app.common.util.DateUtils;
import com.hp.app.exception.BaseDaoException;
import com.hp.titan.common.util.UserSessionUtil;
import com.hp.titan.notify.EmailManageAction;
import com.hp.titan.project.model.Project;
import com.hp.titan.sprint.dao.SprintDao;
import com.hp.titan.sprint.model.Sprint;
import com.hp.titan.sprint.model.SprintId;
import com.hp.titan.sprint.service.SprintService;
import com.hp.titan.test.dao.DefectinfoDao;
import com.hp.titan.test.vo.DefectInfoVo;

public class SprintServiceImpl implements SprintService{
	private SprintDao sprintDao;
	private DefectinfoDao defectinfoDao;
	
	// Get all sprint by project set
	public List<Sprint> getAllSprintList(Set<Project> s) throws BaseDaoException{
		return sprintDao.findAllSprint(s);
	}
	
	// Get all sprint
	public List<Sprint> getAllSprintList() throws BaseDaoException{
		return sprintDao.findSprints();
	}
	
	// Get sprint by
	public List<Sprint> getSprintList(Sprint sprint) throws BaseDaoException{
		return sprintDao.findSprint(sprint);
	}
	
	// Save sprint
	public void saveSprint(Sprint sprint) throws BaseDaoException{
		sprintDao.saveOrUpdate(sprint);
	}
	
	// Save sprints
	public void saveSprints(List<Sprint> sprintList) throws BaseDaoException{
		Iterator<Sprint> iter = sprintList.iterator();
		while(iter.hasNext()){
			sprintDao.saveOrUpdate(iter.next());
		}
	}
	
	//  Get sprint by id
	public Sprint getSprintById(SprintId sprintId) throws BaseDaoException{
		return sprintDao.findById(sprintId);
	}
	
	// Delete sprints
	public void deleteSprintList(SprintId id) throws BaseDaoException {
		Sprint sprint = sprintDao.findById(id);
		sprint.setIsValid(1);
		sprint.setLastUpdateUser(Integer.valueOf(UserSessionUtil.getUser().getUserId()));
		sprint.setLastUpdate_Date(DateUtils.getCurrentDate());
		try {
			sprintDao.update(sprint);// .delete(group);
		} catch (BaseDaoException e) {
			e.printStackTrace();
		}
	}
	
	// Check if the sprint exist
	public Boolean isExistSprint(String sprintName) throws BaseDaoException{
		Sprint sprint = sprintDao.getSprintBySprintName(sprintName);
		if(sprint == null){
			return false;
		}else{
			return true;
		}
	}
	
	public void sprintNotify() throws BaseDaoException {
 		 Date currentDate = new Date();
 		    for (Sprint s : this.getAllSprintList()){            		    	
 		    	if(s.getEndDate().equals(currentDate)){
	              System.out.println( "Sending notify Email to  project owner" );
	              EmailManageAction email = new EmailManageAction();
	              email.sendEmail("just a test for sprint notify", s.getEndDate() + s.getSprintName(), "xiao-man.ruan@hp.com");
 		    	  } 
 		    }
	}
	
	public List<DefectInfoVo> getDefectInfoBySprintId(String sprintId) throws BaseDaoException{
		return defectinfoDao.getDefectInfoBySprintId(sprintId);
	}
	
	public List<Sprint> findSprintByProject(String projectId) throws BaseDaoException{
		return sprintDao.findSprintByProject(projectId);
	}
	
	public SprintDao getSprintDao() {
		return sprintDao;
	}

	public void setSprintDao(SprintDao sprintDao) {
		this.sprintDao = sprintDao;
	}

	public DefectinfoDao getDefectinfoDao() {
		return defectinfoDao;
	}

	public void setDefectinfoDao(DefectinfoDao defectinfoDao) {
		this.defectinfoDao = defectinfoDao;
	}
}
