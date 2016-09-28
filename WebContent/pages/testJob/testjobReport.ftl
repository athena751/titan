<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script type="text/javascript" src="${base}/js/jqplot/jquery.jqplot.js"></script>
<script type="text/javascript"  src="${base}/js/plugins/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript"  src="${base}/js/plugins/jqplot.donutRenderer.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery.json-2.4.min.js"></script>
<link rel="stylesheet" href="${base}/css/jquery.jqplot.css" type="text/css"></link>
<script>
$(document).ready(function(){
    $("#projectId").change(function(){
		var project = this.value;
	
		
			//get sprint list of the project selected
	  		$.ajax( {
				type : 'get',
				url : 'getSprintsByPorject.do',
				data : { 'projectId' : project },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success : function(data) {				      
					var y = document.getElementById("sprintId");
					for(var j=y.length-1; j>0; j--){
						y.remove(j);
					}
					if(data.length != 0){
						var x = document.getElementById("sprintId");
						for(var i=0; i<data.length; i++){
							var option=document.createElement("option");
						    option.value = data[i].sprintId;
							option.text = data[i].sprintName;
							if(option.value == $("#selectedSprintid").val()){
								option.selected = true;
							}
							x.add(option,x.options[null]);
						}
					}
				}
			});
		 
  	});
  	
  	
  	 $("#sprintId").change(function(){
		var sprint = this.value;
		if(sprint == ''){
			sprint = $("#selectedSprintid").val();
		}
			//get testjob list of the sprint selected
	  		$.ajax( {
				type : 'get',
				url : 'getTestjobBySprint.do',
				data : { 'sprintId' : sprint },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success : function(data) {				      
					var y = document.getElementById("testjobId");
					for(var j=y.length-1; j>0; j--){
						y.remove(j);
					}
					if(data.length != 0){
						var x = document.getElementById("testjobId");
						for(var i=0; i<data.length; i++){
							var option=document.createElement("option");
						    option.value = data[i].testjobID;
							option.text = "[" + data[i].testjobCode + "] " + data[i].testjobName;
							if(option.value == $("#selectedTestjobid").val()){
								option.selected = true;
							}
							x.add(option,x.options[null]);
						}
					}
				}
			});
		 
  	});
  	
  	if($("#selectedProjectid").val() != ''){
		$("#projectId").trigger("change");
		if($("#selectedSprintid").val() != ''){
			$("#sprintId").trigger("change");
			if($("#selectedTestjobid").val() != ''){
				showChart();
			}
		}
	}
});
function onSave() {
   doFormSubmit('testjobForm','doTestjobUpdate.do')
}
function onBack() {
   doPageRedirect('testjobList.do')
}
function showData(){
	$("#testcaseData").show();
	$("#showHideData").html('<a onclick="hideData()">Hide detail data</a>');
}

function hideData(){
	$("#testcaseData").hide();
	$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
}

function showChart(){
	$("#chart_container").show();
	$('#chart_container').empty(); 
	var testjobId = '${(testjob.testjobId)!""}';
	$.ajax( {
		type : 'get',
		url : '${base}/test/getChartXml.do?t=' + new Date().getTime(),
		dataType: "json",
		data : { 'testjobId' : testjobId
		
		},
		success : function(data) {
			var pass=data[0].testcasePass;
			var fail=data[0].testcaseFail;
			var norun=data[0].testcaseNotrun;
			var skip=data[0].testcaseSkip;
			var run=data[0].testcaseRuning;
			var view=[['pass',pass],['fail',fail],['not run',norun],['skipped',skip],['running',run]];  
			var plot1 = $.jqplot('chart_container',[view], {
			seriesColors: [ "#74e530", "#e53030", "#d531cf", "#f4f42f","#74e888"], 
			seriesDefaults: {				
         		renderer: $.jqplot.PieRenderer,
        		rendererOptions: {    
           			showDataLabels: true,
           			diameter:300,
         		}
     		},
      		title : 'Cases in Project',
       		legend: { 
       			show:true,
       			//location: 'e',
       		},
       		
       		});		
		}
	});
}

function getReport(){
	var sprintId = document.getElementById("sprintId").value;
	var projectId = document.getElementById("projectId").value;
   var testjobId = document.getElementById("testjobId").value;
   doPageRedirect('goTestjobReport.do?testjobId='+testjobId + '&selectedProjectid='+projectId + '&selectedSprintid=' + sprintId);
}

</script>
</head>

<body>
 <form id="testjobForm" name="testjobForm" method="post">
 	<#if navigation?has_content>
		${navigation}
	</#if>
	 <#if testjob?has_content>
			<input type="hidden" name="testjob.testjobId"  value="${(testjob.testjobId)!''}"/>	
			<input type="hidden" name="testjob"  value="${(testjob)!''}"/>	
			<input type="hidden" id="selectedProjectid" value="${(selectedProjectid)!''}"/>
			<input type="hidden" id="selectedSprintid" value="${(selectedSprintid)!''}"/>
			<input type="hidden" id="selectedTestjobid" value="${(selectedTestjobid)!''}"/>
			<input id="testjobCode" type="hidden" name="testjob.testjobCode"  value="${(testjob.testjobCode)!""}"/>
	 </#if>
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5">
			  	Test Job Report
            </span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
<tr>
	<td align="right" class="tablecontent_title" width="10%"> Please Select</td>
	<td align="left" width="90%">
	    Project
		<select id="projectId" name="projectId"  class="select" style="width:180px" >
			<option value="" selected></option>
			<#list projectList as project>
				<option value="${project.projectId}"<#if '${selectedProjectid!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
			</#list> 
		</select>
		&nbsp;Sprint&nbsp;
		<select class="select" id="sprintId" name="sprintId">
					<option value=""></option>
		</select>
		&nbsp;Test Job&nbsp;
		<select class="select" id="testjobId" name="testjobId">
					<option value=""></option>
				</select>
		<input type="button" onclick="getReport()" value="Go"></input>
	</td>
</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Job Name:</td>
  <td align="left" width="90%">${(testjob.testjobName)!""}</td>

</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Job Type:</td>
  <td align="left" width="90%">${(testjob.jobType)!""}</td>
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
	<td align="right" class="tablecontent_title" width="10%">Job Owner:</td>
	<td align="left" width="90%">
		<select id="ownerId" name="testjob.ownerId"  class="select" style="width:180px" disabled="disabled">
			<option value="" selected readonly="true"> Please Select</option>
			<#list userList as user>
			  <option value="${user.userId}" <#if '${user.userId!""}'=='${(testjob.ownerId)!""}'>selected</#if>>${user.userCode}</option>		            
		    </#list>
			
		</select>
		<span id="ownerIdMessage"></span>
	</td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Project:</td>
	<td align="left" width="90%">
		<select id="projectId" name="testjob.projectId"  class="select" style="width:180px" disabled="disabled">
			<option value="" selected>Please Select</option>
			<#list projectList as project>
				<option value="${project.projectId}"<#if '${(testjob.projectId)!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
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
  <td align="left" width="90%">${(testjob.remark)!""}</td>
</tr>
 <#if reportVo?has_content>
 		<tr>
			<td class="tablecontent_title" align="right" width="10%">view</td>
			<td align="left">
				<div style="width:98%;height:500px;display:none" id="chart_container" align="center"></div>
			</td>      
		</tr>
 		<tr id="testcaseData" style="display:none">
          <td align="right" class="tablecontent_title" width="10%">Test cases:</td>
          <td align="left" width="10%">
          <table id="tblModule" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase total</td>
						<td width="10%">${reportVo.testcaseTotal!""}</td>
						<td width="10%">100%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase Pass</td>
						<td>${reportVo.testcasePass!""}</td>
						<td>${reportVo.casepassCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase Fail</td>
						<td>${reportVo.testcaseFail!""}</td>
						<td>${reportVo.casefailCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase Notrun</td>
						<td>${reportVo.testcaseNotrun!""}</td>
						<td>${reportVo.casenotrunCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase Skipped</td>
						<td>${reportVo.testcaseSkip!""}</td>
						<td>${reportVo.caseskipCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase Running</td>
						<td>${reportVo.testcaseRuning!""}</td>
						<td>${reportVo.caserunCent!""}%</td>
					</tr>
					
				</table>
        </tr>
 </#if>
</table>
<div id="showHideData" align="right"><a onclick="showData()">Show detail data</a></div> 

</form>
</body>
</html>
