<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script>

function onSave() {
   doFormSubmit('testjobForm','doTestjobUpdate.do')
}
function onBack() {
   doPageRedirect('testjobList.do')
}

</script>
</head>

<body>
 <form id="testjobForm" name="testjobForm" method="post">
	 <#if testjob.testjobId?has_content>
			<input type="hidden" name="testjob.testjobId"  value="${(testjob.testjobId)!''}"/>	
			<input type="hidden" name="testjob"  value="${(testjob)!''}"/>	
			<input id="testjobCode" type="hidden" name="testjob.testjobCode"  value="${(testjob.testjobCode)!""}"/>
	 </#if>
	 <#if navigation?has_content>
	 	${navigation}
	 </#if>
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5"><#if testjob.testjobId?has_content>
			  	Test Job Info  
			  <#else>
			  	Test Job Create 
			  </#if>	</span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
<tr>
  <td align="right" class="tablecontent_title" width="10%">Job Name:</td>
  <td align="left" width="90%">${testjob.testjobName!""}</td>

</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Job Type:</td>
  <td align="left" width="90%">${testjob.jobType!""}</td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Server:</td>
  <td align="left" width="90%">
   <#list jobserverList as server>
    ${server.serverName!""} ${server.distro!""}
   </#list> 
  </td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Job Owner:</td>
	<td align="left" width="90%">
		<select id="ownerId" name="testjob.ownerId"  class="select" style="width:180px" disabled="disabled">
			<option value="" selected readonly="true"> Please Select</option>
			<#list userList as user>
			  <option value="${user.userId}" <#if '${user.userId!""}'=='${testjob.ownerId!""}'>selected</#if>>${user.userCode}</option>		            
		    </#list>
			
		</select>
		<span id="ownerIdMessage"></span>
	</td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Project:</td>
	<td align="left" width="90%">
		<select id="projectId" name="testjob.projectId"  class="select" style="width:180px" disabled="disabled">
			<option value="" selected>Please Select</option>
			<#list projectList as project>
				<option value="${project.projectId}"<#if '${testjob.projectId!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
			</#list> 
		</select>
		<span id="projectIdMessage"></span>
	</td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Description:</td>
  <td align="left" width="90%">${testjob.remark!""}</td>
</tr>

</table>

 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5">
			  	Test Job Runs  
			  </span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
    <table class="tlist" id="pageTable" width="100%">
		<tr>
			<th width="10%" align="left">Time</th>		
			<th width="20%">Description</th>
			<th width="20%">Result</th>
			<th width="20%"></th>
		</tr>     
	<#if runJobList?has_content >
	    <#list runJobList as runJob>
			<tr class="mousechange">
				<td align="left">${(runJob.startTime)!''}</td>	
				<td align="center">${(runJob.remark)!''}</td>
				<td align="center">${(runJob.result)!''}</td>
				<td align="center"><div class="table_link">
					 <a class="tooltips" href="#" onclick="doPageRedirect('testjobResult.do?runJobId=${(runJob.runJobId)}')"><img src="${base}/images/icon/27.png" /><span>View&nbsp;&nbsp;Detail</span></a>&nbsp;&nbsp;&nbsp;
				 </td>
			</tr>
	    </#list>
		
 	</#if>      
	</table>
</div>
<table width="100%" border="0">
	<tr><td align="center">
		<input type="button" onclick="onBack()" value="Cancel"></input>
	</td></tr>
</table>
</div>
</form>
</body>
</html>
