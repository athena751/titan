<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script type="text/javascript" src="${base}/js/fusionCharts/FusionCharts.js"></script>
<script type="text/javascript" src="${base}/js/fusionCharts/FusionCharts.jqueryplugin.js"></script>
<script>
$(document).ready(function(){
	showChart();
});

function onBack() {
   doPageRedirect('projectList.do')
}
</script>
</head>

<body>
 <form id="projectForm" name="projectForm" method="post">
 	<#if navigation?has_content>
		${navigation}
	</#if>
	 <#if projectId?has_content>
			<input type="hidden" name="projectId"  value="${(projectId)!''}"/>
	 </#if>
	 
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			  	Project View  
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table width="100%" class="tablecontent">
<tr>
  <td align="right" class="tablecontent_title" width="10%">Name:</td>
  <td align="left" width="90%">${project.projectName!""}</td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Description:</td>
  <td align="left" width="90%">${project.remark!""}</td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Project Manager</td>
	<td align="left" width="90%">
		<select id="pmId" name="project.pmId"  class="select" style="width:180px" disabled="disabled">
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
		<select id="groupId" name="project.groupId"  class="select" style="width:180px" disabled="disabled">
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
						
					</tr>
					</#list>
					</#if>
				</table>
				
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
						
					</tr>
					</#list>
					</#if>
				</table>
						
			</td>
		</tr>
		
 
</table>
<table width="100%" border="0">
	<tr><td align="center">
		<input type="button" onclick="onBack()" value="Return to Porject List"></input>
	</td></tr>
</table>
</div>
</form>
</body>
</html>
