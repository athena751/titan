package com.hp.titan.rally.dao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hp.app.exception.BaseException;
import com.hp.titan.common.vo.DefectVo;
import com.hp.titan.common.vo.LoginUserVo;
import com.hp.titan.common.vo.SprintCommonVo;
import com.hp.titan.common.vo.TaskVo;
import com.hp.titan.common.vo.UserstoryVo;
import com.hp.titan.test.action.RestApiFactory;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;

public class RallyDao {
	private RestApiFactory rallyapiFactory;
	
	/**
	 * Get defect info from rally by defect number
	 * @param defectNum
	 * @return
	 * @throws IOException 
	 * @throws IOException
	 */
	public DefectVo getDefectInfoByDefectNo(String defectNum) throws IOException{
		DefectVo defectVo = new DefectVo();
		RallyRestApi restApi = null;
		try {
			restApi = rallyapiFactory.getRestApi();
			
			QueryRequest defects = new QueryRequest("defect");
			defects.setLimit(10000);
            defects.setFetch(new Fetch("FormattedID", "Name", "State", "Priority", "SubmittedBy", "c_Developer",  "ObjectID", "LastUpdateDate"));
            defects.setQueryFilter(new QueryFilter("FormattedID", "=", defectNum));

            QueryResponse queryResponse = restApi.query(defects);
            if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
            	JsonObject defect = queryResponse.getResults().get(0).getAsJsonObject();
            	defectVo.setObjectId(defect.get("ObjectID").getAsString());
            	defectVo.setDefectNum(defect.get("FormattedID").getAsString());
            	defectVo.setDefectName(defect.get("Name").getAsString());
            	defectVo.setPriority(defect.get("Priority").getAsString());
            	defectVo.setState(defect.get("State").getAsString());
            	defectVo.setSubmittedBy(defect.getAsJsonObject("SubmittedBy").get("_refObjectName").getAsString());
            	defectVo.setLastupdateDate(defect.get("LastUpdateDate").getAsString());
            	defectVo.setSubmittedbyObjectid(defect.getAsJsonObject("SubmittedBy").get("ObjectID").getAsString());
            	// Find if this defect belong to any user story
            	String userStoryNum = this.getDefectUs(defectVo.getDefectNum(), restApi);
            	if(null != userStoryNum && !"".equals(userStoryNum)){
            		defectVo.setUserStory(userStoryNum);
            	}
            	return defectVo;
            } else {
                return null;
            }
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
            restApi.close();
        }
		return null;
	}
	
	
	/**
	 * Get user info from rally by user name
	 * @return
	 * @throws IOException 
	 */
	public LoginUserVo getUserInfoFromRallyByObjectId(String ObjectId) throws BaseException, IOException{
		RallyRestApi restApi = null;
		LoginUserVo loginuserVo = new LoginUserVo();
		try {
			restApi = rallyapiFactory.getRestApi();
			
			QueryRequest user = new QueryRequest("user");

            user.setFetch(new Fetch("EmailAddress"));
            user.setQueryFilter(new QueryFilter("ObjectID", "=", ObjectId));

            QueryResponse queryResponse = restApi.query(user);
            if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
            	JsonObject loginUserObj = queryResponse.getResults().get(0).getAsJsonObject();
            	loginuserVo.setEmail(loginUserObj.get("EmailAddress").getAsString());
            	loginuserVo.setObjectId(ObjectId);
            	return loginuserVo;
            } else {
                return null;
            }
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
            restApi.close();
        }
		return null;
	}
	
	public LoginUserVo getUserInfoByEmail(String email) throws BaseException, IOException{
		RallyRestApi restApi = null;
		LoginUserVo loginuserVo = new LoginUserVo();
		try {
			restApi = rallyapiFactory.getRestApi();
			
			QueryRequest user = new QueryRequest("user");

            user.setFetch(new Fetch("ObjectID"));
            user.setQueryFilter(new QueryFilter("EmailAddress", "=", email.replace("hpe.com", "hp.com")));

            QueryResponse queryResponse = restApi.query(user);
            if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
            	JsonObject loginUserObj = queryResponse.getResults().get(0).getAsJsonObject();
            	loginuserVo.setObjectId(loginUserObj.get("ObjectID").getAsString());
            	loginuserVo.setEmail(email);
            	return loginuserVo;
            } else {
                return null;
            }
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
            restApi.close();
        }
		return null;
	}
	
	public List<TaskVo> getTaskInfoByUserEmail(String email, String startDate, String endDate) throws IOException {
		RallyRestApi restApi = null;
		List<TaskVo> taskVoList = new ArrayList<TaskVo>();
		try {
			restApi = rallyapiFactory.getRestApi();
			
			QueryRequest task = new QueryRequest("task");
			task.setLimit(1000);
            task.setFetch(new Fetch("Name", "Actuals", "State", "ToDo", "Description", "LastUpdateDate", "WorkProduct"));
            QueryFilter qf = new QueryFilter("Owner.EmailAddress", "=", email.replace("@hpe.com", "@hp.com"));
//            qf = qf.or(new QueryFilter("Owner.EmailAddress", "=", email.replace("@hpe.com", "@hp.com")));
            qf = qf.and(new QueryFilter("LastUpdateDate", ">=", startDate));
            qf = qf.and(new QueryFilter("LastUpdateDate", "<=", endDate));
            task.setQueryFilter(qf);

            QueryResponse queryResponse = restApi.query(task);
            if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
            	Iterator<JsonElement> iter = queryResponse.getResults().iterator();
            	while(iter.hasNext()){
            		TaskVo taskVo = new TaskVo();
            		JsonObject taskObj = iter.next().getAsJsonObject();
            		taskVo.setTaskName(taskObj.get("Name").getAsString());
            		if(!taskObj.get("Actuals").isJsonNull()){
            			taskVo.setActual(taskObj.get("Actuals").getAsString());
            		}
            		if(!taskObj.get("ToDo").isJsonNull()){
            			taskVo.setTodo(taskObj.get("ToDo").getAsString());
            		}
            		if(!taskObj.get("State").isJsonNull()){
            			taskVo.setState(taskObj.get("State").getAsString());
            		}
            		if(!taskObj.get("LastUpdateDate").isJsonNull()){
            			taskVo.setLastupdateDate(taskObj.get("LastUpdateDate").getAsString());
            		}
            		if(!taskObj.getAsJsonObject("WorkProduct").isJsonNull()){
            			taskVo.setParent(taskObj.getAsJsonObject("WorkProduct").get("FormattedID").getAsString());
            		}
            		taskVo.setOwnerEmail(email);
            		taskVoList.add(taskVo);
            	}
            	return taskVoList;
            } else {
                return null;
            }
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
            restApi.close();
        }
		return null;
	}
	
	/**
	 * Get defect info by email
	 * @param email
	 * @return
	 * @throws IOException
	 */
	public List<DefectVo> getDefectInfoByEmail(String email) throws IOException{
		RallyRestApi restApi = null;
		List<DefectVo> defectVoList = new ArrayList<DefectVo>();
		try {
			restApi = rallyapiFactory.getRestApi();
			
			QueryRequest defects = new QueryRequest("defect");
			defects.setLimit(10000);
            defects.setFetch(new Fetch("FormattedID", "Name", "State", "Priority", "SubmittedBy", "c_Developer",  "ObjectID", "LastUpdateDate", "Project", "c_Component", "CreationDate"));
            defects.setQueryFilter(new QueryFilter("SubmittedBy.EmailAddress", "=", email.replace("hpe.com", "hp.com")));

            QueryResponse queryResponse = restApi.query(defects);
            if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
            	Iterator<JsonElement> iter = queryResponse.getResults().iterator();
            	while(iter.hasNext()){
            		DefectVo defectVo = new DefectVo();
            		JsonObject defect = iter.next().getAsJsonObject();
                	defectVo.setObjectId(defect.get("ObjectID").getAsString());
                	defectVo.setDefectNum(defect.get("FormattedID").getAsString());
                	defectVo.setDefectName(defect.get("Name").getAsString());
                	defectVo.setPriority(defect.get("Priority").getAsString());
                	defectVo.setState(defect.get("State").getAsString());
                	defectVo.setSubmittedBy(defect.getAsJsonObject("SubmittedBy").get("_refObjectName").getAsString());
                	defectVo.setLastupdateDate(defect.get("LastUpdateDate").getAsString());
                	defectVo.setSubmittedbyObjectid(defect.getAsJsonObject("SubmittedBy").get("ObjectID").getAsString());
                	defectVo.setSubmitDate(defect.get("CreationDate").getAsString());
                	if(!defect.get("c_Developer").isJsonNull()){
    					String devName = defect.get("c_Developer").getAsString();
    					defectVo.setDeveloper(devName);
    				}
                	if(!defect.get("c_Component").isJsonNull()){
                		defectVo.setComponent(defect.get("c_Component").getAsString());
                	}
                	defectVo.setProjectName(defect.get("Project").getAsJsonObject().get("_refObjectName").getAsString());
                	// Find if this defect belong to any user story
                	String userStoryNum = this.getDefectUs(defectVo.getDefectNum(), restApi);
                	if(null != userStoryNum && !"".equals(userStoryNum)){
                		defectVo.setUserStory(userStoryNum);
                	}
                	defectVoList.add(defectVo);
            	}
            	return defectVoList;
            } else {
                return null;
            }
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
            restApi.close();
        }
		return null;
	}
	
	public List<DefectVo> getRallyDefectInfoByDeveloper(String email) throws IOException{
		RallyRestApi restApi = null;
		List<DefectVo> defectVoList = new ArrayList<DefectVo>();
		try {
			restApi = rallyapiFactory.getRestApi();
			
			QueryRequest defects = new QueryRequest("defect");
			defects.setLimit(10000);
            defects.setFetch(new Fetch("FormattedID", "Name", "State", "Priority", "SubmittedBy", "c_Developer",  "ObjectID", "LastUpdateDate", "Project", "c_Component", "CreationDate"));
            defects.setQueryFilter(new QueryFilter("c_Developer", "=", email.replace("hpe.com", "hp.com")));

            QueryResponse queryResponse = restApi.query(defects);
            if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
            	Iterator<JsonElement> iter = queryResponse.getResults().iterator();
            	while(iter.hasNext()){
            		DefectVo defectVo = new DefectVo();
            		JsonObject defect = iter.next().getAsJsonObject();
                	defectVo.setObjectId(defect.get("ObjectID").getAsString());
                	defectVo.setDefectNum(defect.get("FormattedID").getAsString());
                	defectVo.setDefectName(defect.get("Name").getAsString());
                	defectVo.setPriority(defect.get("Priority").getAsString());
                	defectVo.setState(defect.get("State").getAsString());
                	defectVo.setSubmittedBy(defect.getAsJsonObject("SubmittedBy").get("_refObjectName").getAsString());
                	defectVo.setLastupdateDate(defect.get("LastUpdateDate").getAsString());
                	defectVo.setSubmittedbyObjectid(defect.getAsJsonObject("SubmittedBy").get("ObjectID").getAsString());
                	defectVo.setSubmitDate(defect.get("CreationDate").getAsString());
                	defectVo.setDeveloper(email);
                	if(!defect.get("c_Component").isJsonNull()){
                		defectVo.setComponent(defect.get("c_Component").getAsString());
                	}
                	defectVo.setProjectName(defect.get("Project").getAsJsonObject().get("_refObjectName").getAsString());
                	// Find if this defect belong to any user story
                	String userStoryNum = this.getDefectUs(defectVo.getDefectNum(), restApi);
                	if(null != userStoryNum && !"".equals(userStoryNum)){
                		defectVo.setUserStory(userStoryNum);
                	}
                	defectVoList.add(defectVo);
            	}
            	return defectVoList;
            } else {
                return null;
            }
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
            restApi.close();
        }
		return null;
	}
	
	public List<DefectVo> getRallyDefectInfoByProjectModule(String projectName, String module) throws IOException{
		RallyRestApi restApi = null;
		List<DefectVo> defectVoList = new ArrayList<DefectVo>();
		try {
			restApi = rallyapiFactory.getRestApi();
			
			QueryRequest defects = new QueryRequest("defect");
			defects.setLimit(10000);
			QueryFilter qf = new QueryFilter("Project.Name", "=", projectName);
			if(null != module && !"".equals(module)){
				qf = qf.and(new QueryFilter("c_Component", "=", module));
			}
            defects.setFetch(new Fetch("FormattedID", "Name", "State", "Priority", "SubmittedBy", "c_Developer",  "ObjectID", "LastUpdateDate", "c_Developer", "CreationDate"));
            defects.setQueryFilter(qf);

            QueryResponse queryResponse = restApi.query(defects);
            if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
            	Iterator<JsonElement> iter = queryResponse.getResults().iterator();
            	while(iter.hasNext()){
            		DefectVo defectVo = new DefectVo();
            		JsonObject defect = iter.next().getAsJsonObject();
                	defectVo.setObjectId(defect.get("ObjectID").getAsString());
                	defectVo.setDefectNum(defect.get("FormattedID").getAsString());
                	defectVo.setDefectName(defect.get("Name").getAsString());
                	defectVo.setPriority(defect.get("Priority").getAsString());
                	defectVo.setState(defect.get("State").getAsString());
                	defectVo.setSubmittedBy(defect.getAsJsonObject("SubmittedBy").get("_refObjectName").getAsString());
                	defectVo.setLastupdateDate(defect.get("LastUpdateDate").getAsString());
                	defectVo.setSubmittedbyObjectid(defect.getAsJsonObject("SubmittedBy").get("ObjectID").getAsString());
                	defectVo.setSubmitDate(defect.get("CreationDate").getAsString());
                	if(!defect.get("c_Developer").isJsonNull()){
                		defectVo.setDeveloper(defect.get("c_Developer").getAsString());
                	}
                	defectVo.setComponent(module);
                	defectVo.setProjectName(projectName);
                	
                	// Find if this defect belong to any user story
                	String userStoryNum = this.getDefectUs(defectVo.getDefectNum(), restApi);
                	if(null != userStoryNum && !"".equals(userStoryNum)){
                		defectVo.setUserStory(userStoryNum);
                	}
                	defectVoList.add(defectVo);
            	}
            	return defectVoList;
            } else {
                return null;
            }
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
            restApi.close();
        }
		return null;
	}
	
	public List<SprintCommonVo> getSprintByProjectName(String projectName) throws IOException{
		RallyRestApi restApi = null;
		List<SprintCommonVo> sprintCommonVoList = new ArrayList<SprintCommonVo>();
		try {
			restApi = rallyapiFactory.getRestApi();
			
			QueryRequest sprint = new QueryRequest("Iteration");
			sprint.setLimit(1000);
			sprint.setFetch(new Fetch("EndDate", "Name", "StartDate", "ObjectID"));
			sprint.setQueryFilter(new QueryFilter("Project.Name", "=", projectName));

            QueryResponse queryResponse = restApi.query(sprint);
            if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
            	Iterator<JsonElement> iter = queryResponse.getResults().iterator();
            	while(iter.hasNext()){
            		SprintCommonVo sprintCommonVo = new SprintCommonVo();
            		JsonObject defect = iter.next().getAsJsonObject();
            		sprintCommonVo.setObjectId(defect.get("ObjectID").getAsString());
            		sprintCommonVo.setEndDate(defect.get("EndDate").getAsString());
            		sprintCommonVo.setSprintName(defect.get("Name").getAsString());
            		sprintCommonVo.setStartDate(defect.get("StartDate").getAsString());
            		sprintCommonVoList.add(sprintCommonVo);
            	}
            	return sprintCommonVoList;
            } else {
                return null;
            }
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
            restApi.close();
        }
		return null;
	}
	
	public List<UserstoryVo> getUserStoriesByUser(String userObjectId) throws IOException{
		RallyRestApi restApi = null;
		List<UserstoryVo> usVoList = new ArrayList<UserstoryVo>();
		try {
			restApi = rallyapiFactory.getRestApi();
			
			QueryRequest sprint = new QueryRequest("HierarchicalRequirement");
			sprint.setLimit(1000);
			sprint.setFetch(new Fetch("PlanEstimate", "TaskActualTotal", "Name", "ObjectID", "AcceptedDate", "FormattedID", "TaskEstimateTotal", "TaskRemainingTotal", "Project", "ScheduleState"));
			sprint.setQueryFilter(new QueryFilter("Owner.ObjectID", "=", userObjectId));

            QueryResponse queryResponse = restApi.query(sprint);
            if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
            	Iterator<JsonElement> iter = queryResponse.getResults().iterator();
            	while(iter.hasNext()){
            		UserstoryVo usVo = new UserstoryVo();
            		JsonObject usObj = iter.next().getAsJsonObject();
            		usVo.setObjectId(usObj.get("ObjectID").getAsString());
            		usVo.setUsName(usObj.get("Name").getAsString());
            		usVo.setUsNum(usObj.get("FormattedID").getAsString());
            		usVo.setProjectinRally(usObj.getAsJsonObject("Project").get("Name").getAsString());
            		if(!usObj.get("PlanEstimate").isJsonNull()){
            			usVo.setPlanEstimate(usObj.get("PlanEstimate").getAsString());
            		}
            		if(!usObj.get("TaskActualTotal").isJsonNull()){
            			usVo.setTaskActuals(usObj.get("TaskActualTotal").getAsString());
            		}
            		if(!usObj.get("TaskEstimateTotal").isJsonNull()){
            			usVo.setTaskEstimates(usObj.get("TaskEstimateTotal").getAsString());
            		}
            		if(!usObj.get("TaskRemainingTotal").isJsonNull()){
            			usVo.setTaskTodos(usObj.get("TaskRemainingTotal").getAsString());
            		}
            		if(!usObj.get("ScheduleState").isJsonNull()){
            			usVo.setState(usObj.get("ScheduleState").getAsString());
            		}
            		if(!usObj.get("AcceptedDate").isJsonNull()){
            			usVo.setAcceptedDate(usObj.get("AcceptedDate").getAsString());
            		}
            		usVoList.add(usVo);
            	}
            	return usVoList;
            } else {
                return null;
            }
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
            restApi.close();
        }
		return null;
	}
	
	public List<UserstoryVo> getRallyUSInofoByPorject(String projeceName) throws IOException{
		RallyRestApi restApi = null;
		List<UserstoryVo> usVoList = new ArrayList<UserstoryVo>();
		try {
			restApi = rallyapiFactory.getRestApi();
			
			QueryRequest userstory = new QueryRequest("HierarchicalRequirement");
			userstory.setLimit(10000);
			userstory.setFetch(new Fetch("PlanEstimate", "TaskActualTotal", "Name", "ObjectID", "AcceptedDate", "FormattedID", "TaskEstimateTotal", "TaskRemainingTotal", "Project", "ScheduleState", "Owner", "Parent"));
			userstory.setQueryFilter(new QueryFilter("Project.Name", "=", projeceName));

            QueryResponse queryResponse = restApi.query(userstory);
            if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
            	Iterator<JsonElement> iter = queryResponse.getResults().iterator();
            	while(iter.hasNext()){
            		UserstoryVo usVo = new UserstoryVo();
            		JsonObject usObj = iter.next().getAsJsonObject();
            		usVo.setObjectId(usObj.get("ObjectID").getAsString());
            		usVo.setUsName(usObj.get("Name").getAsString());
            		usVo.setUsNum(usObj.get("FormattedID").getAsString());
            		usVo.setProjectinRally(usObj.getAsJsonObject("Project").get("Name").getAsString());
            		if(!usObj.get("PlanEstimate").isJsonNull()){
            			usVo.setPlanEstimate(usObj.get("PlanEstimate").getAsString());
            		}
            		if(!usObj.get("TaskActualTotal").isJsonNull()){
            			usVo.setTaskActuals(usObj.get("TaskActualTotal").getAsString());
            		}
            		if(!usObj.get("TaskEstimateTotal").isJsonNull()){
            			usVo.setTaskEstimates(usObj.get("TaskEstimateTotal").getAsString());
            		}
            		if(!usObj.get("TaskRemainingTotal").isJsonNull()){
            			usVo.setTaskTodos(usObj.get("TaskRemainingTotal").getAsString());
            		}
            		if(!usObj.get("ScheduleState").isJsonNull()){
            			usVo.setState(usObj.get("ScheduleState").getAsString());
            		}
            		if(!usObj.get("Owner").isJsonNull()){
            			usVo.setOwnerObj(usObj.getAsJsonObject("Owner").get("ObjectID").getAsString());
            		}
            		if(!usObj.get("Parent").isJsonNull()){
            			usVo.setParentNum(usObj.getAsJsonObject("Parent").get("FormattedID").getAsString());
            		}
            		if(!usObj.get("AcceptedDate").isJsonNull()){
            			usVo.setAcceptedDate(usObj.get("AcceptedDate").getAsString());
            		}
            		usVoList.add(usVo);
            	}
            	return usVoList;
            } else {
                return null;
            }
		}catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
            restApi.close();
        }
		return null;
	}
	
	private String getDefectUs(String defectNum, RallyRestApi restApi) throws IOException{
		QueryRequest userstory = new QueryRequest("HierarchicalRequirement");
		userstory.setLimit(10000);
		userstory.setFetch(new Fetch("FormattedID"));
		userstory.setQueryFilter(new QueryFilter("Defects.FormattedID", "contains", defectNum));
		QueryResponse queryResponse = restApi.query(userstory);
		 if (queryResponse.wasSuccessful() && queryResponse.getTotalResultCount() != 0) {
         	JsonObject loginUserObj = queryResponse.getResults().get(0).getAsJsonObject();
         	return loginUserObj.get("FormattedID").getAsString();
         } else {
             return null;
         }
	}

	public RestApiFactory getRallyapiFactory() {
		return rallyapiFactory;
	}

	public void setRallyapiFactory(RestApiFactory rallyapiFactory) {
		this.rallyapiFactory = rallyapiFactory;
	}
}
