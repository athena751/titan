<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/manhuaDate.1.0.css" />
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
<script>
$(document).ready(function(){
	//Sprint Name
	$("#sprintName").blur(function(){
	  	var sprintName = this.value;
	  	if('' == sprintName){
	  		$("#sprintNameMessage").html('<div class="messageNG">Please provide a sprint name!</div>');
	  	}
	  	else{
	  		var originalName = document.getElementById('original').value;
	  		if(originalName != sprintName){
		  		$.ajax( {
					type : 'get',
					url : '${base}/sprint/checkSprintName.do?t=' + new Date().getTime(),
					dataType: "text",
					data : { 'sprintName' : sprintName },
					success : function(msg) {
						if (msg == "exist") {	
							$("#sprintNameMessage").html('<div class="messageNG">The Sprint name has been existed\! please use another sprint name\!</div>');
						}
						else{
							$("#sprintNameMessage").html('OK');
						}
						
					}
				});
			}
			else{
				$("#sprintNameMessage").html('OK');
			}
	  	}
	});
	
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
	
	// Start Date
	$("#startDate").blur(function(){
		var startDate = this.value;
		if('' == startDate){
			$("#startDateMessage").html('<div class="messageNG">Please provide start date!');
		}
		else{
			$("#startDateMessage").html('OK');
		}
	});
	
	// End Date
	$("#endDate").blur(function(){
		var endDate = this.value;
		if('' == endDate){
			$("#endDateMessage").html('<div class="messageNG">Please provide end date!');
		}
		else{
			$("#endDateMessage").html('OK');
		}
	});
});

$(function (){
	$("input.mh_date").manhuaDate({					       
		Event : "click",		       
		Left : 0,
		Top : -16,
		fuhao : "-",
		isTime : false,
		beginY : 2010,
		endY :2025
	});
	
});

function checkInput() {
	$("#sprintName").trigger("blur");
	var sprintNameMessage = $("#sprintNameMessage").html();
	if('OK' != sprintNameMessage){
		alert('There is some error on Sprint Name, please check it!');
		return;
	}
	
	$("#projectId").trigger("blur");
	var projectIdMessage = $("#projectIdMessage").html();
	if('OK' != projectIdMessage){
		alert('There is some error on project, please check it!');
		return;
	}
	
	$("#startDate").trigger("blur");
	var startDateMessage = $("#startDateMessage").html();
	if('OK' != startDateMessage){
		alert('There is some error on start date, please check it!');
		return;
	}
	
	$("#endDate").trigger("blur");
	var endDateMessage = $("#endDateMessage").html();
	if('OK' != endDateMessage){
		alert('There is some error on end date, please check it!');
		return;
	}
	
	var startDate = $("#startDate")[0].value;
	var endDate = $("#endDate")[0].value;	
	var d = new Date(startDate.replace(/-/g,"/")); 
	var d1 = new Date(endDate.replace(/-/g,"/"));
	var m = (d1.getTime()-d.getTime())/(1000*60*60*24); 
	if(m<0){
		alert('Start Date should before End Date!');
		return;
	}

	onSave();
}



function onSave() {
   doFormSubmit('sprintForm','doSprintSave.do')
}
function onBack() {
   doPageRedirect('sprintList.do')
}

</script>
</head>

<body>
 <form id="sprintForm" name="sprintForm" method="post">
	 <#if sprint.id.sprintId?has_content>
			<input type="hidden" name="sprint.id.sprintId"  value="${(sprint.id.sprintId)!''}"/>
			<input type="hidden" name="sprintVo.strCreateDate"  value="${(sprint.createDate).toString()!""}"/>
			<input type="hidden" name="sprint.createUser"  value="${(sprint.createUser)!""}"/>
	 </#if>
	 <#if navigation?has_content>
		 ${navigation}
	</#if>
	 <input type="hidden" id="original" value="${(sprint.sprintName)!''}"/>
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5"><#if sprint.id.sprintId?has_content>
			  	Sprint Update  
			  <#else>
			  	Sprint Create 
			  </#if>	</span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table class="tablecontent" width="100%">
<tr>
  <td align="right" class="tablecontent_title" width="10%">Name:</td>
  <td align="left" width="90%"><input id="sprintName" name="sprint.sprintName" type="text" class="input_text" value="${sprint.sprintName!""}"/><span id="sprintNameMessage"></span></td>
</tr>

<tr>
	<td align="right" class="tablecontent_title" width="10%">Project</td>
	<td align="left" width="90%">
		<select id="projectId" name="sprint.id.projectId"  class="select" style="width:180px">
			<option value="" selected>Please Select</option>
			<#list projectList as project>
				<option value="${project.projectId}"<#if '${sprint.id.projectId!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
			</#list> 
		</select>
		<span id="projectIdMessage"></span>
	</td>
</tr>
	<td align="right" class="tablecontent_title" width="10%">Start Date:</td>
	<td align="left" width="90%"><input id="startDate" name="sprintVo.startDate" type="text" class="mh_date" value="${sprint.startDate!""}" readonly="true" /><span id="startDateMessage"></span></td></td>
<tr>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">End Date:</td>
	<td align="left" width="90%"><input id="endDate" name="sprintVo.endDate" type="text" class="mh_date" value="${sprint.endDate!""}" readonly="true" /><span id="endDateMessage"></span></td></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Description:</td>
  <td align="left" width="90%"><input id="remark" name="sprint.remark"  type="text" class="input_text" value="${sprint.remark!""}"  /><span id="remarkMessage"></span></td>
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
