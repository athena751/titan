<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script>
$(document).ready(function(){
	//add module
	$("#bntModuleAdd").click(function(){
		var newTr = "<tr><td style='display:none'><input type='hidden'></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		$("#tblModule tr:last").after(newTr);
  	});
	//add dedicated server
	$("#bntServerAdd").click(function(){
		var newTr = "<tr><td style='display:none'><input type='hidden'></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		$("#tblServer tr:last").after(newTr);
  	});
  	//add project parameter
	$("#bntProjPara").click(function(){
		var projectName = $("#projectName").val();
		var newTr = "<tr>"
					+ "<td><select class='select'>"
					+ "<option value='host'>host</option>"
					+ "<option value='ip'>ip</option>"
					+ "<option value='username'>username</option>"
					+ "<option value='password'>password</option>"
					+ "<option value='other'>other</option>"
					+ "<option value='none'>none</option>"
					+ "</select></td>"
					+ "<td><input class='input_text' type='text' value='$_" + projectName
					+"'></input></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		$("#tblProjPara tr:last").after(newTr);
  	});
  	//delete tr for table
 	$("#reginfo").delegate(".bntDelTr", "click", function(){
		$(this).closest("tr").remove();
  	});

	//Project Name
	$("#projectName").blur(function(){
	  	var projectName = this.value;
	  	if('' == projectName){
	  		$("#projectNameMessage").html('<div class="messageNG">Please provide a project name!</div>');
	  	}
	  	else{
	  		var originalName = document.getElementById('original').value;
	  		if(originalName != projectName){
		  		$.ajax( {
					type : 'get',
					url : '${base}/project/checkProjectName.do?t=' + new Date().getTime(),
					dataType: "text",
					data : { 'projectName' : projectName },
					success : function(msg) {
						if (msg == "exist") {	
							$("#projectNameMessage").html('<div class="messageNG">The Project name has been existed\! please use another project name\!</div>');
						}
						else{
							$("#projectNameMessage").html('OK');
						}
						
					}
				});
			}
			else{
				$("#projectNameMessage").html('OK');
			}
	  	}
	});
	
	// Group
	$("#groupId").blur(function(){
		var groupId = this.value;
		if('' == groupId){
			$("#groupIdMessage").html('<div class="messageNG">Please select a team!');
		}
		else{
			$("#groupIdMessage").html('OK');
		}
	});
	
	// PM
	$("#pmId").blur(function(){
		var pmId = this.value;
		if('' == pmId){
			$("#manageIdMessage").html('<div class="messageNG">Please select a PM!');
		}
		else{
			$("#manageIdMessage").html('OK');
		}
	});
	
});

function createJson(){
	var jsonObj = {moduleArray:[],serverArray:[],projectParaArray:[]};
	var trArray = document.getElementById("tblModule").rows;
	for(var i=1; i<trArray.length; i++){
		jsonObj.moduleArray.push({
			"moduleId": trArray[i].cells[0].getElementsByTagName("input")[0].value,
			"moduleName": trArray[i].cells[1].getElementsByTagName("input")[0].value,
			"remark": trArray[i].cells[2].getElementsByTagName("input")[0].value
		});
	}
	trArray = document.getElementById("tblServer").rows;
	for(var i=1; i<trArray.length; i++){
		jsonObj.serverArray.push({
			"serverId": trArray[i].cells[0].getElementsByTagName("input")[0].value,
			"serverName": trArray[i].cells[1].getElementsByTagName("input")[0].value,
			"serverHostname": trArray[i].cells[2].getElementsByTagName("input")[0].value,
			"serverIp": trArray[i].cells[3].getElementsByTagName("input")[0].value,
			"serverAccount": trArray[i].cells[4].getElementsByTagName("input")[0].value,
			"serverPasswd": trArray[i].cells[5].getElementsByTagName("input")[0].value,
			"remark": trArray[i].cells[6].getElementsByTagName("input")[0].value
		});
	}
	trArray = document.getElementById("tblProjPara").rows;
	for(var i=1; i<trArray.length; i++){
		jsonObj.projectParaArray.push({
			"projectParaType": trArray[i].cells[0].getElementsByTagName("select")[0].value,
			"projectParaName": trArray[i].cells[1].getElementsByTagName("input")[0].value
		});
	}
	document.getElementById("jsonObj").value=JSON.stringify(jsonObj);
}

function checkInput() {
	$("#projectName").trigger("blur");
	var projectNameMessage = $("#projectNameMessage").html();
	if('OK' != projectNameMessage){
		alert('There is some error on Project Name, please check it!');
		return;
	}
	
	$("#groupId").trigger("blur");
	var groupIdMessage = $("#groupIdMessage").html();
	if('OK' != groupIdMessage){
		alert('There is some error on team, please check it!');
		return;
	}
	
	$("#pmId").trigger("blur");
	var manageIdMessage = $("#manageIdMessage").html();
	if('OK' != manageIdMessage){
		alert('There is some error on PM, please check it!');
		return;
	}
	
	var projectName = $("#projectName").val();
	trArray = document.getElementById("tblProjPara").rows;
	for(var i=1; i<trArray.length; i++){
		projectParaName = trArray[i].cells[1].getElementsByTagName("input")[0].value;
		if(projectParaName.indexOf('$_' + projectName) < 0){
			alert('Parameter must begin with $_+project name!');
			return;
		}
	}
	onSave();
}

function onSave() {
	createJson();
    doProjectFormSubmit('projectForm','doProjectSave.do')
}
function onBack() {
   doPageRedirect('projectList.do')
}

function verifyInput(){
	var trArray = document.getElementById("tblModule").rows;
	for(var i=1;i<trArray.length;i++){
		if(trArray[i].cells[1].getElementsByTagName("input")[0].value==''){
			return "moduleName";
		}
	}
	trArray = document.getElementById("tblServer").rows;
	var exp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	for(var i=1; i<trArray.length; i++){
		if(trArray[i].cells[1].getElementsByTagName("input")[0].value=='' 
			|| trArray[i].cells[2].getElementsByTagName("input")[0].value==''
		    || trArray[i].cells[3].getElementsByTagName("input")[0].value==''
		    || trArray[i].cells[4].getElementsByTagName("input")[0].value==''
		    || trArray[i].cells[5].getElementsByTagName("input")[0].value==''){
			return "dedicatedServer";
		}
		if(!exp.exec(trArray[i].cells[3].getElementsByTagName("input")[0].value)){
			return "verifyIP";
		}
	}
}

function doProjectFormSubmit(formId,action){
	document.getElementById(formId).action = action;
	if("moduleName"==verifyInput()){
		alert("Module's Module name must be input !");
	}else if("dedicatedServer"==verifyInput()){
		alert("Dedicated Server's Title,Host Name,IP,User Name,Password must be Input !");
	}else if("verifyIP"==verifyInput()){
		alert("Please provide a valid Dedicated Server's IP,such as:xxx.xxx.xxx.xxx.and require x is a number,and xxx<=255!");
	}else{
		document.getElementById(formId).submit();
	}
	return;
}
</script>
</head>

<body>
 <form id="projectForm" name="projectForm" method="post">
 	<input type="hidden" id="jsonObj" name="jsonObj"/>
 	<#if navigation?has_content>
		${navigation}
	</#if>
	 <#if project.projectId?has_content>
			<input type="hidden" name="project.projectId"  value="${(project.projectId)!''}"/>
			<input type="hidden" name="projectVo.strCreateDate"  value="${(project.createDate).toString()!""}"/>
			<input type="hidden" name="project.createUser"  value="${(project.createUser)!""}"/>
	 </#if>
	 <input type="hidden" id="original"  value="${(project.projectName)!''}"/>
	 
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<#if project.projectId?has_content>
			  	Project Update  
			  <#else>
			  	Project Create 
			  </#if>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div id="reginfo" class="box">
      <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tablecontent">
<tr>
  <td align="right" class="tablecontent_title" width="10%">Name:</td>
  <td align="left" width="90%"><input id="projectName" name="project.projectName" type="text" class="input_text" value="${project.projectName!""}"/><span id="projectNameMessage"></span></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Description:</td>
  <td align="left" width="90%"><input id="remark" name="project.remark"  type="text" class="input_text" value="${project.remark!""}"  /><span id="remarkMessage"></span></td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Project Manager</td>
	<td align="left" width="90%">
		<select id="pmId" name="project.pmId"  class="select" style="width:180px">
			<option value="" selected>Please Select</option>
			<#list manageList as manage>
				<option value="${manage.userId}"<#if '${project.pmId!""}'=='${manage.userId!""}'>selected</#if>>${manage.userCode}</option>
			</#list> 
		</select>
		<span id="manageIdMessage"></span>
	</td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Team</td>
	<td align="left" width="90%">
		<select id="groupId" name="project.groupId"  class="select" style="width:180px">
			<option value="" selected>Please Select</option>
			<#list groupList as group>
				<option value="${group.groupId}"<#if '${project.groupId!""}'=='${group.groupId!""}'>selected</#if>>${group.groupName}</option>
			</#list> 
		</select>
		<span id="groupIdMessage"></span>
	</td>
</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Module</td>
			<td align="left" width="90%">
				<table id="tblModule" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="20%">Module Name</td>
						<td>Description</td>
						<td width="5%"></td>
					</tr>
					<#if moduleList?has_content>
					<#list moduleList as module>
					<tr>
						<td style='display:none'><input type='hidden' value="${(module.moduleId)}"></input></td>
						<td><input class='input_text' type='text' value="${(module.moduleName)!''}"></input></td>
						<td><input class='input_text' type='text' value="${(module.remark)!''}"></input></td>
						<td><button type='button' class='bntDelTr'>Delete</button></td>
					</tr>
					</#list>
					</#if>
				</table>
				<button type="button" id="bntModuleAdd">Add</button>				
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Dedicated Server</td>
			<td align="left" width="90%">
				<table id="tblServer" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="10%">Title</td>
						<td width="15%">Host Name</td>
						<td width="10%">IP</td>
						<td width="10%">User Name</td>
						<td width="10%">Password</td>
						<td>Description</td>
						<td width="5%"></td>
					</tr>
					<#if dedicatedServerList?has_content>
					<#list dedicatedServerList as server>
					<tr>
						<td style='display:none'><input type='hidden' value="${(server.serverId)}"></input></td>
						<td><input class='input_text' type='text' value="${(server.serverName)!''}"></input></td>
						<td><input class='input_text' type='text' value="${(server.serverHostname)!''}"></input></td>
						<td><input class='input_text' type='text' value="${(server.serverIp)!''}"></input></td>
						<td><input class='input_text' type='text' value="${(server.serverAccount)!''}"></input></td>
						<td><input class='input_text' type='text' value="${(server.serverPasswd)!''}"></input></td>
						<td><input class='input_text' type='text' value="${(server.remark)!''}"></input></td>
						<td><button type='button' class='bntDelTr'>Delete</button></td>
					</tr>
					</#list>
					</#if>
				</table>
				<button type="button" id="bntServerAdd">Add</button>				
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Project parameter</td>
			<td align="left" width="90%">
				<table id="tblProjPara" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="20%">Parameter type</td>
						<td>Parameter name</td>
						<td width="5%"></td>
					</tr>
					<#if projectParaList?has_content>
					<#list projectParaList as projectPara>
					<tr>
						<td><select class='select'>
							<option value='host' <#if '${projectPara.type!""}'=='host'>selected</#if>>host</option>
							<option value='ip' <#if '${projectPara.type!""}'=='ip'>selected</#if>>ip</option>
							<option value='username' <#if '${projectPara.type!""}'=='username'>selected</#if>>username</option>
							<option value='password' <#if '${projectPara.type!""}'=='password'>selected</#if>>password</option>
							<option value='none' <#if '${projectPara.type!""}'=='none'>selected</#if>>none</option>
							</select>
						</td>
						<td><input class='input_text' type='text' value="${(projectPara.paradataName)!''}"></input></td>
						<td><button type='button' class='bntDelTr'>Delete</button></td>
					</tr>
					</#list>
					</#if>
				</table>
				<button type="button" id="bntProjPara">Add</button>				
			</td>
		</tr>
</table>
<table width="100%" border="0">
	<tr><td align="center">
		<input type="button" onclick="checkInput()" value="Save"></input>
		<input type="button" onclick="onBack()" value="Cancel"></input>
	</td></tr>
</table>
</div>
</form>
</body>
</html>
