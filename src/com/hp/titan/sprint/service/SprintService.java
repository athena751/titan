package com.hp.titan.sprint.service;

import java.util.List;
import java.util.Set;

import com.hp.app.exception.BaseDaoException;
import com.hp.titan.project.model.Project;
import com.hp.titan.sprint.model.Sprint;
import com.hp.titan.sprint.model.SprintId;
import com.hp.titan.test.vo.DefectInfoVo;

public interface SprintService {
	public List<Sprint> getAllSprintList(Set<Project> s) throws BaseDaoException;
	public List<Sprint> getAllSprintList() throws BaseDaoException;
	public void saveSprint(Sprint sprint) throws BaseDaoException;
	public void saveSprints(List<Sprint> sprintList) throws BaseDaoException;
	public Sprint getSprintById(SprintId sprintId) throws BaseDaoException;
	public void deleteSprintList(SprintId sprintIdAry) throws BaseDaoException;
	public Boolean isExistSprint(String sprintName) throws BaseDaoException;
	public void sprintNotify() throws BaseDaoException;
	public List<Sprint> findSprintByProject(String projectId) throws BaseDaoException;
	public List<Sprint> getSprintList(Sprint sprint) throws BaseDaoException;
	public List<DefectInfoVo> getDefectInfoBySprintId(String sprintId) throws BaseDaoException;
}
