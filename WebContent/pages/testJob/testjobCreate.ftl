<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script>

$(document).ready(function(){  	

	$("#bntServerAdd").click(function(){	
		var newTr = "<tr><td><select class='select' style='width:180px'>"
		            + "<option value=''selected >Please Select</option>"
		            + "<#list serverList as server>"
					+ "<option value='${server.serverId}'>${server.serverName}</option>"
					+ "</#list>"
		            + "</select></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		$("#tblserver tr:last").after(newTr);
  	});
  	
  	$("#jobinfo").delegate(".bntDelTr", "click", function(){
		$(this).closest("tr").remove();
  	});
  	
  	$("#project").change(function(){
		var project = this.value;
	
		if('' == project){
			$("#projectMessage").html('Please select a Project!');
			document.getElementById("project").focus();
		}else{
			$("#projectMessage").html('');
			
			//get sprint list of the project selected
	  		$.ajax( {
				type : 'get',
				url : 'getSprintsByPorject.do',
				data : { 'projectId' : project },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success : function(data) {				      
					var y = document.getElementById("sprint");
					for(var j=y.length-1; j>0; j--){
						y.remove(j);
					}
					if(data.length != 0){
						var x = document.getElementById("sprint");
						for(var i=0; i<data.length; i++){
							var option=document.createElement("option");
						    option.value = data[i].sprintId;
							option.text = data[i].sprintName;
							x.add(option,x.options[null]);
						}
					}
				}
			});
		  }
  	});
  	
  	
  	
  	// Name
	$("#testjobName").blur(function(){
		var testjobName = this.value;
		if('' == testjobName){
			$("#testjobNameMessage").html('<div class="messageNG">Please provide test job name!');
		}
		
		else{
			$("#testjobNameMessage").html('OK');
		}
	});
	
	// Type
	$("#testjobType").blur(function(){
		var testjobType = this.value;
		if('' == testjobType){
			$("#testjobTypeMessage").html('<div class="messageNG">Please select test job type!');
		}
		else{
			$("#testjobTypeMessage").html('OK');
		}
	});
	
	// Project
	$("#project").blur(function(){
		var project = this.value;
		if('' == project){
			$("#projectMessage").html('<div class="messageNG">Please select a project!');
		}
		else{
			$("#projectMessage").html('OK');
		}
	});
	
	// Sprint
	$("#sprint").blur(function(){
		var sprint = this.value;
		if('' == sprint){
			$("#sprintMessage").html('<div class="messageNG">Please select a sprint!');
		}
		else{
			$("#sprintMessage").html('OK');
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
	
	$("#tagSel").change(function(){
		var tag = this.value;
		$("#tag").val(tag);
	});
	
  	
});

function saveServerIds(){
   var serverIds = [];
   var trArray = document.getElementById("tblserver").rows;
   for(var i=1; i<trArray.length; i++){
       if(trArray[i].cells[0].getElementsByTagName("select")[0].value != "objectHTMLTableRowElement"){
        serverIds.push(trArray[i].cells[0].getElementsByTagName("select")[0].value);
       }
   }
   document.getElementById("jobServerIds").value = serverIds;
}

function checkInput() {
    $("#testjobName").trigger("blur");
	var testjobNameMessage = $("#testjobNameMessage").html();
	if('OK' != testjobNameMessage){
		alert('There is some error on test job Name, please check it!');
		return;
	}
	
	 $("#testjobType").trigger("blur");
	var testjobTypeMessage = $("#testjobTypeMessage").html();
	if('OK' != testjobTypeMessage){
		alert('There is some error on test job Type, please check it!');
		return;
	}

	$("#ownerId").trigger("blur");
	var ownerIdMessage = $("#ownerIdMessage").html();
	if('OK' != ownerIdMessage){
		alert('There is some error on Owner, please check it!');
		return;
	}
	
	$("#project").trigger("blur");
	var projectMessage = $("#projectMessage").html();
	if('OK' != projectMessage){
		alert('There is some error on project, please check it!');
		return;
	}
	
	$("#sprint").trigger("blur");
	var sprintMessage = $("#sprintMessage").html();
	if('OK' != sprintMessage){
		alert('There is some error on sprint, please check it!');
		return;
	}
	
	onSave();
}

function onSave() {
   saveServerIds();
   doFormSubmit('testjobForm','doTestjobSave.do')
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
	 </#if>
	 <#if navigation?has_content>
	 	${navigation}
	 </#if>
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5">
			  	Test Job Create 
			</span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>
<div id="jobinfo" class="box">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
<tr>
  <td align="right" class="tablecontent_title" width="10%">Job Name:</td>
  <td align="left" width="90%"><input id="testjobName" name="testjob.testjobName" type="text" class="input_text" value="${testjob.testjobName!""}"/><span id="testjobNameMessage"></span></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Job Type:</td>
  <td align="left" width="90%">
          <select class="select" id="testjobType" name="testjob.jobType">
					<option value=""></option>
					<option value="Manual">Manual</option>
					<option value="Automated">Automated</option>
		  </select>
		  <span id="testjobTypeMessage"></span>
</td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Job Owner</td>
	<td align="left" width="90%">
		<select id="ownerId" name="testjob.ownerId"  class="select" style="width:180px">
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
		<select id="project" name="testjob.projectId"  class="select" style="width:180px">
			<option value="" selected>Please Select</option>
			<#list projectList as project>
				<option value="${project.projectId}"<#if '${testjob.projectId!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
			</#list> 
		</select>
		<span id="projectMessage"></span>
	</td>
</tr>
<tr>
			<td class="tablecontent_title" align="right" width="10%">Sprint</td>
			<td align="left" width="90%">
				<select class="select" id="sprint" name="testjob.sprintId">
					<option value=""></option>
				</select>
				<span id="sprintMessage"></span>
			</td>
		</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Servers</td>
	<td align="left" width="90%">
	    <input type="hidden" name="jobServerIds" id="jobServerIds" value="${jobServerIds!""}" />	
	    <table id="tblserver" width="100%">
	    <tr></tr>
		</table>
		<button type="button" id="bntServerAdd">Add</button>			
		<span id="serverIdMessage"></span>
	</td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Description:</td>
  <td align="left" width="90%"><input id="remark" name="testjob.remark"  type="text" class="input_text" value="${testjob.remark!""}"  /><span id="remarkMessage"></span></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%" rowspan="2">Tag:</td>
  <td align="left" width="90%"><input id="tag" name="testjob.tag"  type="text" class="input_text" value="${testjob.tag!""}"  /></td>
</tr>
<tr>
  <td align="left" width="90%">
  	<select id="tagSel"  class="select" style="width:180px">
		<option value="" selected>Please Select</option>
		<#list usertagList as usertag>
			<option value="${usertag.id.tag}">${usertag.id.tag}</option>
		</#list> 
  	</select>
  </td>
</tr>
</table>
<table width="100%" border="0">
	<tr><td align="center">
		<input type="button" onclick="checkInput()" value="Next"></input>
		<input type="button" onclick="onBack()" value="Cancel"></input>
	</td></tr>
</table>
</div>
</form>
</body>
</html>
