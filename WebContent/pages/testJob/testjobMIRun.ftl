<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script>

function onSave() {
   doFormSubmit('testjobForm','doTestjobUpdate.do')
}
function onBack() {
   doPageRedirect('goJobRunList.do?testjobId=${(testjob.testjobId)}')
}

function doBatchUpdate(){
    var state = document.getElementById("state").value;
	document.getElementById("runCaseState").value = state;
    if(checkBox('pass','runcaseIdAry')){
        if(confirm("Are you sure to update result to " + state + "?")){
			doFormSubmit('testjobForm','goBatchUpdateResult.do');
		}
	}
}

function checkBox(type,name){
	var testcaseIds = document.getElementsByName(name);
	var count = 0;
	if(testcaseIds != null && testcaseIds.length > 0){
     for(var i = 0 ; testcaseIds.length > i; i++){
        if(testcaseIds[i].checked){
	    	count = count + 1;
	   	}
     }
	}
	 if(count == 0){
		 alert('Please select at least one!');
		 return false;
	 }else if (type=='update' && count > 1){
	 	 alert('Please choose only one!');
	 	 return false;
	 }else{
	 	 return true;
	 }
}

</script>
</head>

<body>
 <form id="testjobForm" name="testjobForm" method="post">
     <input id="runCaseState" type="hidden" name="runCaseState"  value="${(runCaseState)!""}"/>
	 <#if testjob.testjobId?has_content>
			<input type="hidden" name="testjob.testjobId"  value="${(testjob.testjobId)!''}"/>	
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
      <table width="100%" border="0" align="left" cellpadding="0" cellspacing="0" class="tablecontent" >
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
	<td align="right" class="tablecontent_title" width="10%">Job Owner</td>
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
	<td align="right" class="tablecontent_title" width="10%">Project</td>
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
	<td class="tablecontent_title" align="right" width="10%">Sprint：</td>
	 <td align="left" width="90%">
		<select class="select" id="sprint" name="testjob.sprintId" disabled="disabled">
					<option value=""></option>
					<#if sprintList?has_content >
					 <#list sprintList as sprint>
		        	   <option value="${sprint.id.sprintId}" <#if '${sprint.id.sprintId!""}'=='${testjob.sprintId!""}'>selected</#if>>${sprint.sprintName}</option>
					 </#list>
					</#if>
		</select>
	</td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Description:</td>
  <td align="left" width="90%">${testjob.remark!""}</td>
</tr>
</table>
 <table width="100%" border="0">
        <tr></tr>
        <tr></tr>
		<tr>
		<td align="right">
		     <div class="table_link"><ul>		     
			<li>
				<#if '${testjob.jobType!""}'=='Manual'>
				    <select class="select" id="state" name="runCase.state">
					<option value="SKIPPED">SKIPPED</option>
					<option value="PASS">PASS</option>
				    </select>
					<a href="#" onclick="doBatchUpdate()">Batch Update</a>
				</#if>
			</li>
			</ul></div>
			</td>
			
		</tr>
</table>
 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5">
			  	Test cases  
			  </span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
    <table class="tlist" id="pageTable" width="100%">
		<tr>
			<th width="2%">
		<!--	<input type="checkbox" id="allId" onclick="doAll(this ,'runcaseIdAry')" />  -->
			</th>
			<th width="10%">Name</th>	
			<th width="20%">Result</th>
			<th width="20%">Test Note</th>
			<th width="20%">Action</th>
		</tr>     
	<#if runCaseList?has_content >
	   <#list testcaseList as case>
	    <#list runCaseList as runcase>
	      <#if '${case.caseId!""}'=='${runcase.caseId!""}'>
			<tr class="mousechange">
				<td align="center"><input type="checkbox" id="runcaseIdAry" name="runcaseIdAry"  value="${(runcase.runCaseId)!''}"/></td>
				<td align="center">${(case.caseName)!''}</td>	
				<td align="center">
				 <#if 'FAIL'=='${runcase.state!""}'><div><table><tr><td align="center" bgcolor="#FF0000"><B>${(runcase.state)!''}</B></td></tr></table></div></#if>
				 <#if 'PASS'=='${runcase.state!""}'><div><table><tr><td align="center" bgcolor="green"><B>${(runcase.state)!''}</B></td></tr></table></div></#if>
				 <#if 'NOTRUN'=='${runcase.state!""}'><div><table><tr><td align="center" bgcolor="lightgray"><B>${(runcase.state)!''}</B></td></tr></table></div></#if>
				 <#if 'SKIPPED'=='${runcase.state!""}'><div><table><tr><td align="center" bgcolor="gray"><B>${(runcase.state)!''}</B></td></tr></table></div></#if>				 		
				</td>
				<td align="center">${(runcase.remark)!''}</td>
				<td align="center"><div class="table_link"><ul>
				 <li><a href="#" onclick="doPageRedirect('goTestcaseRun.do?runCaseId=${(runcase.runCaseId)}')">Run</a></li> 
				 </td>
			</tr>
		   </#if>
		 </#list>
	    </#list>
		
 	</#if>      
	</table>
</div>
<table width="100%" border="0">
	<tr><td align="center">
		<input type="button" onclick="onBack()" value="Return To Run List"></input>
	</td></tr>
</table>
</div>
</form>
</body>
</html>
