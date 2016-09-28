<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/manhuaDate.1.0.css" />
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
<script type="text/javascript" src="${base}/js/fusionCharts/FusionCharts.js"></script>
<script type="text/javascript" src="${base}/js/fusionCharts/FusionCharts.jqueryplugin.js"></script>
<script>
function onBack() {
   doPageRedirect('sprintList.do')
}
</script>
</head>

<body>
 <form id="sprintForm" name="sprintForm" method="post">
	 <#if sprint.id.sprintId?has_content>
			<input type="hidden" name="sprintId"  value="${(sprintId)!''}"/>
			<input type="hidden" name="projectId"  value="${(sprint.id.projectId)!''}"/>
	 </#if>
	 <#if navigation?has_content>
		 ${navigation}
	</#if>
	 <input type="hidden" id="original" value="${(sprint.sprintName)!''}"/>
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5">
			  	Sprint View 
			 </span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table class="tablecontent" width="100%">
<tr>
  <td align="right" class="tablecontent_title" width="10%">Name:</td>
  <td align="left" width="90%">${sprint.sprintName!""}</td>
</tr>

<tr>
	<td align="right" class="tablecontent_title" width="10%">Project</td>
	<td align="left" width="90%">
		<select id="projectId" name="sprint.id.projectId"  class="select" style="width:180px" disabled="disabled">
			<option value="" selected>Please Select</option>
			<#list projectList as project>
				<option value="${project.projectId}"<#if '${sprint.id.projectId!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
			</#list> 
		</select>
		<span id="projectIdMessage"></span>
	</td>
</tr>
	<td align="right" class="tablecontent_title" width="10%">Start Date:</td>
	<td align="left" width="90%">${sprint.startDate!""}</td>
<tr>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">End Date:</td>
	<td align="left" width="90%">${sprint.endDate!""}</td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Description:</td>
  <td align="left" width="90%">${sprint.remark!""}</td>
</tr>

</table>

<div class="title">All defects in this Sprint</div>
<div class="box">
     <table class="tlist" id="pageTable" width="100%">
	 <thead>
		<th width="12%">Defect Number</th>
		<th width="25%">Case Name</th>
		<th width="25%">Job Name</th>
		<th width="22%">Found Date</th>
	 </thead>
	 <tbody>	
         <#if defectinfoVoList?has_content >
     	 <#list defectinfoVoList as defectinfoVo>
			<tr class="mousechange">
				<td align="center">${(defectinfoVo.defectNum)!''}</td>
				<td align="center">${(defectinfoVo.testcaseName)!''}</td>
				<td align="center">${(defectinfoVo.testjobName)!''}</td>
				<td align="center">${(defectinfoVo.inputDate)!''}</td>
			</tr>
			 </#list>
 		 </#if>
      </tbody>  
</table>
</div>
<table width="100%" border="0">
	<tr><td align="center">
		<input type="button" onclick="onBack()" value="Return to Sprint List"></input>
	</td></tr>
</table>
</div>
</form>
</body>
</html>
