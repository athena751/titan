<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/manhuaDate.1.0.css" />
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
<script>
function onSave() {
   /*var reportaryId = $("#reportaryId").val();
   if(reportaryId != ""){
   		if(confirm('If url changed, all the sync data will be removed, continue?')){
   	 		 doFormSubmit('reportaryForm','doReportaryUpdate.do');
   	 	}
   }
   else{*/
      doFormSubmit('projectRallyForm','doProjectAddRally.do');
   //}
}
function onBack() {
   doPageRedirect('reportaryList.do?projectId='+$("#projectId").val());
}
function checkInput() {
	$("#projectRallyQuix").trigger("blur");
	var projectRallyQuixMessage = $("#projectRallyQuixMessage").html();
	if('OK' != projectRallyQuixMessage){
		alert('There is some error on name in rally, please check it!');
		return;
	}
	onSave();
}

$(document).ready(function(){
	// projectRallyQuix
	$("#projectRallyQuix").blur(function(){
		var projectRallyQuix = this.value;
		if('' == projectRallyQuix){
			$("#projectRallyQuixMessage").html('<div class="messageNG">Please enter the name in rally!');
		}
		else{
			$("#projectRallyQuixMessage").html('OK');
		}
	});
});
</script>
</head>
<body>
 <form id="projectRallyForm" name="projectRallyForm" method="post">
 	 <input type="hidden" id="projectId" name="projectRallyQuix.projectId"  value="${(project.projectId)!''}"/>
 	 <#if projectRallyQuix.rallyquixId?has_content>
			<input type="hidden" id="rallyquixId" name="projectRallyQuix.rallyquixId"  value="${(projectRallyQuix.rallyquixId)!''}"/>
	 </#if>
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5"><#if projectRallyQuix?has_content>
	  			Project Rally Update  
	  		<#else>
	  			Project Rally Create 
	  		</#if>	</span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>
<div class="box">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
<tr>
  <td align="right" class="tablecontent_title" width="10%">Project Name:</td>
  <td align="left" width="90%"><input id="projectName" type="text" class="input_text" value="${project.projectName!""}" readonly="true"/></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Name in rally:</td>
  <td align="left" width="90%"><input id="projectRallyQuix" name="projectRallyQuix.nameinRally" type="text" class="input_text" value="${projectRallyQuix.nameinRally!""}" /><span id="projectRallyQuixMessage"></span>
  </td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Component:</td>
  <td align="left" width="90%"><input id="component" name="projectRallyQuix.component" type="text" class="input_text" value="${projectRallyQuix.component!""}" />
  </td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Auto refresh:</td>
  <td align="left" width="90%"><input name="projectRallyQuix.autoRefresh" type="radio" value="1" <#if '${projectRallyQuix.autoRefresh!""}'!='0'>checked</#if>>Yes&nbsp;&nbsp;<input name="projectRallyQuix.autoRefresh" type="radio" value="0" <#if '${projectRallyQuix.autoRefresh!""}'=='0'>checked</#if>>No</td>
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
