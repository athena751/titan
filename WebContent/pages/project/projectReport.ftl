<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script type="text/javascript" src="${base}/js/jqplot/jquery.jqplot.js"></script>
<script type="text/javascript" src="${base}/js/plugins/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="${base}/js/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="${base}/js/plugins/jqplot.pointLabels.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery.json-2.4.min.js"></script>
<link rel="stylesheet" href="${base}/css/jquery.jqplot.css" type="text/css"></link><script>
$(document).ready(function(){
	var projectId = '${projectId}';
	if(projectId != ''){
		showChart();
	}
});

function showChart(){
	$("#chart_container").show();
	$('#chart_container').empty();
	var showType = $("#showType").val();	
	var projectId = '${projectId}';
	$.ajax( {
		type : 'get',
		url : '${base}/project/getChartXml.do?t=' + new Date().getTime(),
		dataType: "json",
		data : { 'projectId' : projectId,
				 'showType' : showType				 
		},
		success : function(data) {
			var view1=[];
			var view2=[];
			var view3=[];
			var view4=[];
			var view5=[];
			var view6=[];
			var serie=[];
			var views=[];
			if(showType=='0'){
				for(var i=0;i<data.length;i++){
					view1.push([data[i].sprintName,data[i].testjobPass]);
					view2.push([data[i].sprintName,data[i].testjobFail]);
					view3.push([data[i].sprintName,data[i].testjobActive]);
					view4.push([data[i].sprintName,data[i].testjobPending]);
					view5.push([data[i].sprintName,data[i].testjobRunning]);
					view6.push([data[i].sprintName,data[i].testjobAbort]);
					views.push(view1);
					views.push(view2);
					views.push(view3);
					views.push(view4);
					views.push(view5);
					views.push(view6);
				}
				serie.push({label:'pass'});
				serie.push({label:'fail'});
				serie.push({label:'active'});
				serie.push({label:'pending'});
				serie.push({label:'running'});
				serie.push({label:'abort'});
			}else{
				title='Sprints in Project';
				for(var i=0;i<data.length;i++){
					view1.push([data[i].sprintName,data[i].testcasePass]);
					view2.push([data[i].sprintName,data[i].testcaseFail]);
					view3.push([data[i].sprintName,data[i].testcaseNotrun]);
					view4.push([data[i].sprintName,data[i].testcaseSkip]);
					view5.push([data[i].sprintName,data[i].testcaseRuning]);
					views.push(view1);
					views.push(view2);
					views.push(view3);
					views.push(view4);
					views.push(view5);
				}
				serie.push({label:'pass'});
				serie.push({label:'fail'});
				serie.push({label:'notrun'});
				serie.push({label:'skipped'});
				serie.push({label:'dataset'});
			}
			var plot1 = $.jqplot('chart_container',views, {
				seriesColors: [ "#74e530", "#e53030", "#d531cf", "#f4f42f","#4fb3f2","#fb7102"], 
				title:'Sprints in Project',
				stackSeries: true,
    			captureRightClick: true,				
	            seriesDefaults : {
	                renderer : $.jqplot.BarRenderer, 
	                rendererOptions : {
	                    barMargin : 100,
	                    barWidth:50,
	                },
	            	pointLabels: {
	            		show: true,
	            	},
	            },
	            axes : {
	                xaxis : {
	                   // ticks :tick,
	                    renderer : $.jqplot.CategoryAxisRenderer
	                 }
	            },
	            legend: { 
		       			show:true,
		       			//location: 'e',
		       	},
		       	series:serie,	
        	});
		}
	});

}

function showData(){
	$("#testjobData").show();
	$("#testcaseData").show();
	$("#sprintData").show();
	$("#showHideData").html('<a onclick="hideData()">Hide detail data</a>');
}

function hideData(){
	$("#testjobData").hide();
	$("#testcaseData").hide();
	$("#sprintData").hide();
	$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
}

function onBack() {
   doPageRedirect('projectList.do')
}

function getReport(){

   var projectId = document.getElementById("project").value;
   doPageRedirect('goProjectReport.do?projectId='+projectId);
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
			  	Project Report 
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table width="100%" class="tablecontent">




	    <tr>
			<td class="tablecontent_title" align="right" width="10%"> Select Project</td>
			<td align="left" width="80%">
				<select id="project" name="projectId" class="select"  style="width:180px" onChange="getReport()">
					<#if projectList?has_content>
				        	<#list projectList as project>
				             	 <option value="${project.projectId}"  <#if '${project.projectId!""}'=='${projectId!""}'>selected</#if>>${project.projectName}</option>
				          	</#list> 
				    </#if>
			    </select>
			    
			</td>
			
		</tr>
		<tr>
  <td align="right" class="tablecontent_title" width="10%">Description:</td>
  <td align="left" width="90%"><#if project?has_content>${project.remark!""}</#if></td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Project Manager</td>
	<td align="left" width="90%">
		<select id="pmId" name="project.pmId"  class="select" style="width:180px" disabled="disabled">
			<option value="" selected>Please Select</option>
				<#if manageList?has_content>
					<#list manageList as manage>
						<option value="${manage.userId}"<#if '${project.pmId!""}'=='${manage.userId!""}'>selected</#if>>${manage.userCode}</option>
					</#list> 
				</#if>
		</select>
		<span id="manageIdMessage"></span>
	</td>
</tr>
<tr>
	<td align="right" class="tablecontent_title" width="10%">Team</td>
	<td align="left" width="90%">
		<select id="groupId" name="project.groupId"  class="select" style="width:180px" disabled="disabled">
			<option value="" selected>Please Select</option>
			<#if groupList?has_content>
				<#list groupList as group>
					<option value="${group.groupId}"<#if '${project.groupId!""}'=='${group.groupId!""}'>selected</#if>>${group.groupName}</option>
				</#list> 
			</#if>
		</select>
		<span id="groupIdMessage"></span>
	</td>
</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">view</td>
			<td align="left">
				<div>
					<select class="select" id="showType" name="showType" onChange="showChart()">
						<option value="0">Test job</option>
						<option value="1">Test case</option>
					</select>
				</div>
				<div style="width:98%;height:500px;display:none " id="chart_container" align="center"></div>
			</td>      
		</tr>
     <#if project?has_content>
		<tr id="testjobData" style="display:none">
          <td align="right" class="tablecontent_title" width="10%">Test jobs</td>
          <td align="left" width="10%">
          <table id="tblModule" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob total</td>
						<td width="10%">${reportVo.testjobTotal!""}</td>
						<td width="10%">100%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Pass</td>
						<td>${reportVo.testjobPass!""}</td>
						<td>${reportVo.passCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Fail</td>
						<td>${reportVo.testjobFail!""}</td>
						<td>${reportVo.failCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Active</td>
						<td>${reportVo.testjobActive!""}</td>
						<td>${reportVo.activeCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Pending</td>
						<td>${reportVo.testjobPending!""}</td>
						<td>${reportVo.pendCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Running</td>
						<td>${reportVo.testjobRunning!""}</td>
						<td>${reportVo.runCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Abort</td>
						<td>${reportVo.testjobAbort!""}</td>
						<td>${reportVo.abortCent!""}%</td>
					</tr>
					
				</table>
        </tr>
        <tr id="testcaseData" style="display:none">
          <td align="right" class="tablecontent_title" width="10%">Test cases</td>
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
      <#if reportVoList?has_content>
      <#list reportVoList as report>
      <tr id="sprintData" style="display:none">
		 <td align="right" class="tablecontent_title" width="10%">sprint</td>
          <td align="left" width="10%">
          <table id="tblModule" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob total</td>
						<td width="10%">${report.testjobTotal!""}</td>
						<td width="10%">100%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Pass</td>
						<td>${report.testjobPass!""}</td>
						<td>${report.passCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Fail</td>
						<td>${report.testjobFail!""}</td>
						<td>${report.failCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Active</td>
						<td>${report.testjobActive!""}</td>
						<td>${report.activeCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Pending</td>
						<td>${report.testjobPending!""}</td>
						<td>${report.pendCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Running</td>
						<td>${report.testjobRunning!""}</td>
						<td>${report.runCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob Abort</td>
						<td>${report.testjobAbort!""}</td>
						<td>${report.abortCent!""}%</td>
					</tr>
			
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase total</td>
						<td width="10%">${report.testcaseTotal!""}</td>
						<td width="10%">100%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase Pass</td>
						<td>${report.testcasePass!""}</td>
						<td>${report.casepassCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase Fail</td>
						<td>${report.testcaseFail!""}</td>
						<td>${report.casefailCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase Notrun</td>
						<td>${report.testcaseNotrun!""}</td>
						<td>${report.casenotrunCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase Skipped</td>
						<td>${report.testcaseSkip!""}</td>
						<td>${report.caseskipCent!""}%</td>
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase Running</td>
						<td>${report.testcaseRuning!""}</td>
						<td>${report.caserunCent!""}%</td>
					</tr>
				</table>	
	  </tr>
      </#list>
      </#if>
</table>
<div id="showHideData" align="right"><a onclick="showData()">Show detail data</a></div>

</div>
</form>
</body>
</html>
