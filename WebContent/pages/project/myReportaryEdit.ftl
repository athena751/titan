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
   var reportaryId = $("#reportaryId").val();
   if(reportaryId != ""){
   		if(confirm('If url changed, all the sync data will be removed, continue?')){
   	 		 doFormSubmit('reportaryForm','doReportaryUpdate.do');
   	 	}
   }
   else{
      doFormSubmit('reportaryForm','doReportarySave.do');
   }
}
function onBack() {
   doPageRedirect('reportaryList.do?projectId='+$("#projectId").val());
}
function checkInput() {
	$("#svnUrl").trigger("blur");
	var svnUrlMessage = $("#svnUrlMessage").html();
	if('OK' != svnUrlMessage){
		alert('There is some error on URL, please check it!');
		return;
	}
	
	$("#remark").trigger("blur");
	var remarkMessage = $("#remarkMessage").html();
	if('OK' != remarkMessage){
		alert('There is some error on remark, please check it!');
		return;
	}
	
	$("#userName").trigger("blur");
	var userNameMessage = $("#userNameMessage").html();
	if('OK' != userNameMessage){
		alert('There is some error on user name, please check it!');
		return;
	}
	
	$("#pwd").trigger("blur");
	var pwdMessage = $("#pwdMessage").html();
	if('OK' != pwdMessage){
		alert('There is some error on password, please check it!');
		return;
	}
	onSave();
}

$(document).ready(function(){
	// URL
	$("#svnUrl").blur(function(){
		var url = this.value;
		if('' == url){
			$("#svnUrlMessage").html('<div class="messageNG">Please enter the url!');
		}
		else{
			$("#svnUrlMessage").html('OK');
		}
	});
	
	// Remark
	$("#remark").blur(function(){
		var remark = this.value;
		if('' == remark){
			$("#remarkMessage").html('<div class="messageNG">Please enter the remark!');
		}
		else{
			$("#remarkMessage").html('OK');
		}
	});
	
	// userName
	$("#userName").blur(function(){
		var userName = this.value;
		if('' == userName){
			$("#userNameMessage").html('<div class="messageNG">Please enter the svn user name!');
		}
		else{
			$("#userNameMessage").html('OK');
		}
	});
	
	// pwd
	$("#pwd").blur(function(){
		var pwd = this.value;
		if('' == pwd){
			$("#pwdMessage").html('<div class="messageNG">Please enter the password!');
		}
		else{
			$("#pwdMessage").html('OK');
		}
	});
	
	// module
	$("#module").blur(function(){
		var module = this.value;
		if('' == module){
			$("#moduleMessage").html('<div class="messageNG">Please select the module!');
		}
		else{
			$("#moduleMessage").html('OK');
		}
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
});
</script>
</head>
<body>
 <form id="reportaryForm" name="reportaryForm" method="post">
 	 <input type="hidden" id="projectId" name="projectId"  value="${(project.projectId)!''}"/>
	 <#if reportary.reportaryId?has_content>
			<input type="hidden" id="reportaryId" name="reportary.reportaryId"  value="${(reportary.reportaryId)!''}"/>
			<#else>
			<input type="hidden" id="reportaryId" name="reportary.reportaryId"  value=""/>
	 </#if>
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5"><#if reportary.reportaryId?has_content>
	  			Repository Update  
	  		<#else>
	  			Repository Create 
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
	<td class="tablecontent_title" align="right" width="10%">Module：</td>
	<td align="left" width="90%">
	<select class="select" id="module" name="reportary.moduleId">
		<option value=""></option>
		<#if moduleList?has_content >
			<#list moduleList as module>
		        <option value="${module.moduleId}" <#if '${module.moduleId!""}'=='${reportary.moduleId!""}'>selected</#if>>${module.moduleName}</option>
			</#list>
		</#if>
	</select>
	</td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Begin data:</td>
  <td align="left" width="90%"><input id="beginDate" name="reportary.beginDate" type="text" class="mh_date" value="${reportary.beginDate!""}" readonly="true" />
  </td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Url:</td>
  <td align="left" width="90%"><input id="svnUrl" name="reportary.svnUrl" type="text" class="input_text" value="${reportary.svnUrl!""}"/><span id="svnUrlMessage"></span></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Svn user name:</td>
  <td align="left" width="90%"><input id="userName" name="reportary.userName" type="text" class="input_text" value="${reportary.userName!""}"/><span id="userNameMessage"></span></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Password</td>
  <td align="left" width="90%"><input id="pwd" name="reportary.pwd" type="text" class="input_text" value="${reportary.pwd!""}"/><span id="pwdMessage"></span></td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Auto refresh:</td>
  <td align="left" width="90%"><input name="reportary.autoRefresh" type="radio" value="1" <#if '${reportary.autoRefresh!""}'!='0'>checked</#if>>Yes&nbsp;&nbsp;<input name="reportary.autoRefresh" type="radio" value="0" <#if '${reportary.autoRefresh!""}'=='0'>checked</#if>>No</td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Remark:</td>
  <td align="left" width="90%"><input id="remark" name="reportary.remark" type="text" class="input_text" value="${reportary.remark!""}"/><span id="remarkMessage"></span></td>
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
