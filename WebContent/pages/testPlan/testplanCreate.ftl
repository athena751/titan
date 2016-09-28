<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script>
$(document).ready(function(){

		
	// Name
	$("#testplanName").blur(function(){
		var testplanName = this.value;
		if('' == testplanName){
			$("#testplanNameMessage").html('<div class="messageNG">Please provide test plan name!');
		}else{
	  		$.ajax( {
				type : 'get',
				url : 'checkTestplanName.do?t=' + new Date().getTime(),
				dataType: "text",
				data : { 'testplanName' : testplanName },
				success : function(msg) {
					if (msg == "exist") {	
						$("#testplanNameMessage").html('<div class="messageNG">The name has been used!');
						document.getElementById("testplanName").focus();
					}else{
						$("#testplanNameMessage").html('');
					}
				}
			});
	  	}
	});
	
	// Type
	$("#testplanType").blur(function(){
		var testplanType = this.value;
		if('' == testplanType){
			$("#testplanTypeMessage").html('<div class="messageNG">Please select test plan type!');
		}
		else{
			$("#testplanTypeMessage").html('');
		}
	});
	
	// Project
	$("#projectId").blur(function(){
		var projectId = this.value;
		if('' == projectId){
			$("#projectIdMessage").html('<div class="messageNG">Please select a project!');
		}
		else{
			$("#projectIdMessage").html('');
		}
	});
	
	// Owner
	$("#ownerId").blur(function(){
		var ownerId = this.value;
		if('' == ownerId){
			$("#ownerIdMessage").html('<div class="messageNG">Please select a owner!');
		}
		else{
			$("#ownerIdMessage").html('');
		}
	});
	
});

function checkInput() {
    $("#testplanName").trigger("blur");
	var testplanNameMessage = $("#testplanNameMessage").html();
	if('' != testplanNameMessage){
		alert('There is some error on test plan Name, please check it!');
		return;
	}
	
	 $("#testplanType").trigger("blur");
	var testplanTypeMessage = $("#testplanTypeMessage").html();
	if('' != testplanTypeMessage){
		alert('There is some error on test plan Type, please check it!');
		return;
	}

	$("#ownerId").trigger("blur");
	var ownerIdMessage = $("#ownerIdMessage").html();
	if('' != ownerIdMessage){
		alert('There is some error on Owner, please check it!');
		return;
	}
	
	$("#projectId").trigger("blur");
	var projectIdMessage = $("#projectIdMessage").html();
	if('' != projectIdMessage){
		alert('There is some error on project, please check it!');
		return;
	}
	
	onSave();
}

function onSave() {
   doFormSubmit('testplanForm','doTestplanSave.do')
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
			<input type="hidden" name="testplan.createUser"  value="${(testplan.createUser)!""}"/>
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
  <td align="right" class="tablecontent_title" width="10%">Name:</td>
  <td align="left" width="90%"><input id="testplanName" name="testplan.testplanName" type="text" class="input_text" value="${testplan.testplanName!""}"/><span id="testplanNameMessage"></span></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Type:</td>
  <td align="left" width="90%">
          <select class="select" id="testplanType" name="testplan.testplanType">
					<option value=""></option>
					<option value="Manual">Manual</option>
					<option value="Automated">Automated</option>
		  </select>
		  <span id="testplanTypeMessage"></span>
</td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Testplan Owner</td>
	<td align="left" width="90%">
		<select id="ownerId" name="testplan.ownerId"  class="select" style="width:180px">
			<option value="" selected>Please Select</option>
			<#list userList as user>
		            <option value="${user.userId}">${user.userCode}</option>
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
  <td align="right" class="tablecontent_title" width="10%">Description:</td>
  <td align="left" width="90%"><input id="remark" name="testplan.remark"  type="text" class="input_text" value="${testplan.remark!""}"  /><span id="remarkMessage"></span></td>
</tr>
</table>
<table width="100%" border="0">
	<tr><td align="center">
		<input type="button" onclick="checkInput()" value="next"></input>
		<input type="button" onclick="onBack()" value="Cancel"></input>
	</td></tr>
</table>
</div>
</form>
</body>
</html>
