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
	var testjobId = '${testjob.testjobId}';
	$.ajax( {
		type : 'get',
		url : '${base}/test/getChartXml.do?t=' + new Date().getTime(),
		dataType: "text",
		data : { 'testjobId' : testjobId
		},
		success : function(msg) {
			$("#chart_container").disposeFusionCharts();
			$("#chart_container").insertFusionCharts({swfUrl: "${base}/js/fusionCharts/Pie3D.swf", dataSource: msg, dataFormat: "xml", width: "860", height: "300", id: "myChartID"});       
						
		}
	});
}

</script>
</head>

<body>
 <form id="testjobForm" name="testjobForm" method="post">
	 <#if testjob.testjobId?has_content>
			<input type="hidden" name="testjob.testjobId"  value="${(testjob.testjobId)!''}"/>	
			<input type="hidden" name="testjob"  value="${(testjob)!''}"/>	
			<input id="testjobCode" type="hidden" name="testjob.testjobCode"  value="${(testjob.testjobCode)!""}"/>
	 </#if>
	 <#if navigation?has_content>
	 	${navigation}
	 </#if>
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5"><#if testjob.testjobId?has_content>
			  	Test Job Info  
			  <#else>
			  	Test Job Create 
			  </#if>	</span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
<tr>
  <td align="right" class="tablecontent_title" width="10%">Job Name:</td>
  <td align="left" width="90%">${testjob.testjobName!""}</td>

</tr>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Job Type:</td>
  <td align="left" width="90%">${testjob.jobType!""}</td>
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
			  <option value="${user.userId}" <#if '${user.userId!""}'=='${testjob.ownerId!""}'>selected</#if>>${user.userCode}</option>		            
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
				<option value="${project.projectId}"<#if '${testjob.projectId!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
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
  <td align="left" width="90%">${testjob.remark!""}</td>
</tr>
 
</table>

 <table width="100%" border="0">
        <tr></tr>
        <tr></tr>
		<tr><td align="certer">
		     <div class="table_link"><ul>
			<li>
				<#if '${testjob.jobType!""}'=='Manual'>
					<a href="#" onclick="doPageRedirect('goMTestJobRun.do?testjobId=${(testjob.testjobId)}')">Run Job</a>
				</#if>
				 <#if '${testjob.jobType!""}'=='Automated'>
				 	<a href="#" onclick="doPageRedirect('goTestJobRun.do?testjobId=${(testjob.testjobId)}')">Run Job</a>
				 </#if>
			</li>
			</ul></div>
			</td>
		</tr>
</table>
<div class="title">All defects in this Sprint</div>
<div class="box">
     <table class="tlist" id="pageTable" width="100%">
	 <thead>
		<th width="12%">Defect Number</th>
		<th width="25%">Case Name</th>
		<th width="22%">Found Date</th>
	 </thead>
	 <tbody>	
         <#if defectinfoVoList?has_content >
     	 <#list defectinfoVoList as defectinfoVo>
			<tr class="mousechange">
				<td align="center">${(defectinfoVo.defectNum)!''}</td>
				<td align="center">${(defectinfoVo.testcaseName)!''}</td>
				<td align="center">${(defectinfoVo.inputDate)!''}</td>
			</tr>
			 </#list>
 		 </#if>
      </tbody>  
</table>
</div>
 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5">
			  	Test Job Runs  
			  </span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
    <table class="tlist" id="pageTable" width="100%">
		<tr>
			<th width="10%" align="left">Time</th>		
			<th width="20%">Result</th>
			<th width="20%">Run Owner</th>
			<th width="20%"></th>
		</tr>     
	<#if runJobList?has_content >
	    <#list runJobList as runJob>
			<tr class="mousechange">
				<td align="left">${(runJob.startTime)!''}</td>	
				<td align="center">
				 <#if 'FAIL'=='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="#FF0000"><B>${(runJob.result)!''}</B></td></tr></table></div></#if>
				 <#if 'PASS'=='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="green"><B>${(runJob.result)!''}</B></td></tr></table></div></#if>
				 <#if 'ACTIVE'=='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="lightgray"><B>${(runJob.result)!''}</B></td></tr></table></div></#if>
				 <#if 'ABORT'=='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="gray"><B>${(runJob.result)!''}</B></td></tr></table></div></#if>
				 <#if 'RUNNING'=='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="lightgreen"><B>${(runJob.result)!''}</B></td></tr></table></div></#if>
				 <#if 'FAIL'!='${runJob.result!""}'&&'PASS'!='${runJob.result!""}'&&'ACTIVE'!='${runJob.result!""}'&&'ABORT'!='${runJob.result!""}'&&'RUNNING'!='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="lightgray"><B>NONE</B></td></tr></table></div></#if>		
				</td>
				<td align="center">${(runJob.owner.userCode)!''}</td>
				<td align="center"><div class="table_link">
				 <#if '${testjob.jobType!""}'=='Manual'>
						 	 <a class="tooltips" href="#" onclick="doPageRedirect('goViewMJobRun.do?runJobId=${(runJob.runJobId)}')"><img src="${base}/images/icon/27.png" /><span>View&nbsp;&nbsp;Detail</span></a>&nbsp;&nbsp;&nbsp;
				 	</#if>
				 <#if '${testjob.jobType!""}'=='Automated'>
					 <a class="tooltips" href="#" onclick="doPageRedirect('testjobResult.do?runJobId=${(runJob.runJobId)}')"><img src="${base}/images/icon/27.png" /><span>View&nbsp;&nbsp;Detail</span></a>&nbsp;&nbsp;&nbsp;
				 </#if>
				 </td>
			</tr>
	    </#list>
		
 	</#if>      
	</table>
</div>
<table width="100%" border="0">
	<tr><td align="center">
		<input type="button" onclick="onBack()" value="Return To Job List"></input>
	</td></tr>
</table>
</div>
</form>
</body>
</html>
