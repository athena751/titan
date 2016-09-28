<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/manhuaDate.1.0.css" />
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
<script type="text/javascript" src="${base}/js/jqplot/jquery.jqplot.js"></script>
<script type="text/javascript" src="${base}/js/jqplot/jquery.min.jqplot.js"></script>
<script type="text/javascript" src="${base}/js/jqplot/excanvas.js"></script>
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
							if(option.value == $("#selectSprintid").val()){
								option.selected = true;
							}
							x.add(option,x.options[null]);
						}
					}
				}
			});
		 
  	});
  	if($("#projectId").val() != ''){
		$("#projectId").trigger("change");
		if($("#selectSprintid").val() != ''){
			showChart();
		}
	}
});
function onBack() {
   doPageRedirect('sprintList.do')
}

function showData(){
	$("#testjobData").show();
	$("#testcaseData").show();
	$("#showHideData").html('<a onclick="hideData()">Hide detail data</a>');
}

function hideData(){
	$("#testjobData").hide();
	$("#testcaseData").hide();
	$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
}

function showChart(){
	$("#chart_container").show();
	$('#chart_container').empty();
	var showType = $("#showType").val();
	var sprintId = '${sprintId!""}';
	var projectId = '${projectId!""}';
	$.ajax( {
		type : 'get',
		url : '${base}/sprint/getChartXml.do?t=' + new Date().getTime(),
		dataType: "json",
		data : { 'sprintId' : sprintId,
				 'projectId': projectId,
				 'showType' : showType				 
		},
		success : function(data) {
			var view=[];
			var title;
			var pass;
			var fail;
			var active;
			var pending;
			var running;
			var abort;
			var notrun;
			var skipped;
			if(showType=='0'){
				title='Jobs in Project';
				pass=data[0].testjobPass;
				fail=data[0].testjobFail;
				active=data[0].testjobActive;
				pending=data[0].testjobPending;
				running=data[0].testjobRunning;
				abort=data[0].testjobAbort;
				view.push(['pass',pass]);
				view.push(['fail',fail]);
				view.push(['active',active]);
				view.push(['pending',pending]);
				view.push(['running',running]);
				view.push(['abort',abort]);
			}else{
				title='Cases in Project';
				pass=data[0].testcasePass;
				fail=data[0].testcaseFail;
				running=data[0].testcaseRuning;
				notrun=data[0].testcaseNotrun;
				skipped=data[0].testcaseSkip;
				view.push(['pass',pass]);
				view.push(['fail',fail]);
				view.push(['not run',notrun]);
				view.push(['skipped',skipped]);
				view.push(['running',running]);
			}
			var plot1 = $.jqplot('chart_container',[view], {
			seriesColors: [ "#74e530", "#e53030", "#d531cf", "#f4f42f","#4fb3f2","#fb7102"],
			seriesDefaults: {
         		renderer: $.jqplot.PieRenderer,
        		rendererOptions: {    
           			showDataLabels: true,
           			diameter:300,
         		}
     		},
      		title :title,
       		legend: { 
       			show:true,
       			//location: 'e',
       		},
       		
       		});
			
			//$("#chart_container").disposeFusionCharts();
			//$("#chart_container").insertFusionCharts({swfUrl: "${base}/js/fusionCharts/Pie3D.swf", dataSource: msg, dataFormat: "xml", width: "860", height: "300", id: "myChartID"});       
						
		}
	});
}

function getReport(){
    var sprintId = document.getElementById("sprintId").value;
    doFormSubmit('sprintForm','goSprintReport.do') 
}

</script>
</head>

<body>
 <form id="sprintForm" name="sprintForm" method="post">
	<#if navigation?has_content>
		${navigation}
	</#if>
	 <input type="hidden" id="original" value="${(sprint.sprintName)!''}"/>
	 <input type="hidden" id="selectSprintid" value="${(selectSprintid)!''}"/>
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5">
			  	Sprint Report
			 </span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table class="tablecontent" width="100%">

<tr>
	<td align="right" class="tablecontent_title" width="10%">Project</td>
	<td align="left" width="90%">
		<select id="projectId" name="projectId"  class="select" style="width:180px" >
			<option value="" selected>Please Select</option>
			<#list projectList as project>
				<option value="${project.projectId}"<#if '${projectId!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
			</#list> 
		</select>
	</td>
</tr>
<tr>
			<td class="tablecontent_title" align="right" width="10%">Sprint</td>
			<td align="left" width="90%">
				<select class="select" id="sprintId" name="sprintId">
					<option value=""></option>
				</select>
				<input type="button" onclick="getReport()" value="Go"></input>
			</td>
		</tr>
<tr>
<#if sprintId?has_content>
<tr>
  <td align="right" class="tablecontent_title" width="10%">Name:</td>
  <td align="left" width="90%">${sprint.sprintName!""}</td>
</tr>
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
		<tr id="testjobData" style="display:none">
          <td align="right" class="tablecontent_title" width="10%">Test jobs:</td>
          <td align="left" width="10%">
          <table id="tblModule" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testjob total</td>
						<td width="10%">${reportVo.testjobTotal!""}</td>
						<td width="10%"></td>
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
          <td align="right" class="tablecontent_title" width="10%">Test cases:</td>
          <td align="left" width="10%">
          <table id="tblModule" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="10%">Testcase total</td>
						<td width="10%">${reportVo.testcaseTotal!""}</td>
						<td width="10%"></td>
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
