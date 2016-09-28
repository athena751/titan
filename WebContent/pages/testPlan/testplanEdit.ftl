<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script>
$(document).ready(function(){

	
	// Project
	$("#projectId").blur(function(){
		var projectId = this.value;
		if('' == projectId){
			$("#projectIdMessage").html('<div class="messageNG">Please select a project!');
		}
		else{
			$("#projectIdMessage").html('OK');
		}
	});
	
	// Owner
	$("#ownerId").blur(function(){
		var ownerId = this.value;
		if('' == ownerId){
			$("#ownerIdMessage").html('<div class="messageNG">Please select a owner!');
		}
		else{
			$("#ownerIdMessage").html('OK');
		}
	});
	
});

function checkInput() {

	$("#ownerId").trigger("blur");
	var ownerIdMessage = $("#ownerIdMessage").html();
	if('OK' != ownerIdMessage){
		alert('There is some error on Owner, please check it!');
		return;
	}
	onSave();
}

function onSave() {
   doFormSubmit('testplanForm','doTestplanUpdate.do')
}
function onBack() {
   doPageRedirect('testplanList.do')
}

</script>
</head>

<body>
 <form id="testplanForm" name="testplanForm" method="post">
	 <#if testplan.testplanId?has_content>
			<input type="hidden" name="testplan.testplanId"  value="${(testplan.testplanId)!''}"/>	
	 </#if>
	 <#if navigation?has_content>
		 ${navigation}
	</#if>
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5"><#if testplan.testplanId?has_content>
			  	Testplan Update  
			  <#else>
			  	Testplan Create 
			  </#if>	</span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
<tr>
			<td class="tablecontent_title" align="right" width="10%">Testplan Code：</td>
			<td><input class="input_text" type="text" id="testplan.testplanCode" name="testplan.testplanCode" value="${testplan.testplanCode!""}"></input></td>
		</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Name:</td>
  <td align="left" width="90%"><input id="testplanName" name="testplan.testplanName" type="text" class="input_text" value="${testplan.testplanName!""}"/><span id="testplanNameMessage"></span></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Type:</td>
  <td align="left" width="90%">
          <select class="select" id="testplan.testplanType" name="testplan.testplanType">
                    <option value=""></option>
					<option value="Manual" <#if 'Manual'=='${testplan.testplanType!""}'>selected</#if>>Manual</option>
					<option value="Automated" <#if 'Automated'=='${testplan.testplanType!""}'>selected</#if>>Automated</option>
		  </select>
</td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Testplan Owner</td>
	<td align="left" width="90%">
		<select id="ownerId" name="testplan.ownerId"  class="select" style="width:180px">
			<option value="" selected>Please Select</option>
			<#list userList as user>		           
		            <option value="${user.userId}" <#if '${user.userId!""}'=='${testplan.ownerId!""}'>selected</#if>>${user.userCode}</option>		     
		    </#list>
			
		</select>
		<span id="ownerIdMessage"></span>
	</td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Project</td>
	<td align="left" width="90%">
		<select id="projectId" name="testplan.projectId"  class="select" style="width:180px">
			<option value="" selected>Please Select</option>
			<#list projectList as project>
				<option value="${project.projectId}"<#if '${testplan.projectId!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
			</#list> 
		</select>
		<span id="projectIdMessage"></span>
	</td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Select Testcase:</td>
  <td align="left" width="90%"></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Description:</td>
  <td align="left" width="90%"><input id="remark" name="testplan.remark"  type="text" class="input_text" value="${testplan.remark!""}"  /><span id="remarkMessage"></span></td>
</tr>
</table>
<table width="100%" border="0">
	<tr><td align="center">
		<input type="button" onclick="onSave()" value="Next"></input>
		<input type="button" onclick="onBack()" value="Cancel"></input>
	</td></tr>
</table>
</div>
</form>
</body>
</html>
