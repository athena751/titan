<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/manhuaDate.1.0.css" />
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
<script type="text/javascript" src="${base}/js/jqplot/jquery.jqplot.js"></script>
<script type="text/javascript" src="${base}/js/jqplot/excanvas.js"></script>
<script type="text/javascript"  src="${base}/js/plugins/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript"  src="${base}/js/plugins/jqplot.donutRenderer.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery.json-2.4.min.js"></script>
<link rel="stylesheet" href="${base}/css/jquery.jqplot.css" type="text/css"></link>
<script>
$(document).ready(function(){
	 	$("#projectId").change(function(){
	 		$("#codeReportData").hide();
			$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
			var project = this.value;
	  		//get sprint list of the project selected
	  		$.ajax( {
				type : 'get',
				url : '${base}/sprint/getSprintsByPorject.do?t=' + new Date().getTime(),
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
							if(option.value == $("#selectedsprintId").val()){
								option.selected = true;
							}
							x.add(option,x.options[null]);
						}
					}
				}
			});
  	   });
  	   
  	   $("#sprint").change(function(){
  	   		$("#codeReportData").hide();
			$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
	 		var sprintId = this.value;
	 		var projectId = $("#projectId").val();
	 		$.ajax( {
				type : 'get',
				url : 'getSprintPeriod.do?t=' + new Date().getTime(),
				data : { 'sprintId' : sprintId,
						 'projectId' : projectId},
				success : function(msg) {	
					var d = msg.split(';');
					$("#startDate").val(d[0]);
					$("#endDate").val(d[1]);
					goScopeReport();		      
				}
			});
  	  });
		if($("#selectedsprintId").val() != ''){
			$("#projectId").trigger("change");
       		showChart();
        }
});

function showChart(){
	var projectId = $("#projectId").val();
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	$('#chart_container').empty();   
	$.ajax( {
		type : 'post',
		url : '${base}/report/getScopeChartXml.do?t=' + new Date().getTime(),
		dataType: "json",
		async:false, 
		data : { 'projectId': projectId,
		         'startDate': startDate,
		         'endDate': endDate
		},
		success : function(data) {
			var passed=data[0].testcasePass;
			var failed=data[0].testcaseFail;
			var defect=data[0].hasDefect;
			var vacuous=data[0].noDefect;
			var fixed=data[0].defectFixed;
			var notfixed=data[0].defectNotfixed;
			var view1=[['Passed',passed],['Failed',failed]];
			var view2=[['Defect',defect],['Vacuous',vacuous]];
			var view3=[['Fixed',fixed],['Not fixed',notfixed]];
			var plot1 = $.jqplot('chart_container',[view1], {
			seriesColors: [ "#74e530", "#e53030", ], 
			seriesDefaults: {
         		renderer: $.jqplot.PieRenderer,
        		rendererOptions: {    
           			showDataLabels: true,
           			diameter: 200,
         		}
     		},
      		title : 'Test&nbsp;&nbsp;result',
       		legend: { 
       			show:true,
       			//location: 'e',
       		},
       		
       		});
			var plot2 = $.jqplot('chart_container1',[view2], {
			seriesColors: [ "#d531cf", "#f4f42f", ], 
			seriesDefaults: {
         		renderer: $.jqplot.PieRenderer,
        		rendererOptions: {    
           			showDataLabels: true,
           			diameter: 200,
         		}
     		},
      		title : 'Test&nbsp;&nbsp;effective',
       		legend: { 
       			show:true,
       			//location: 'e'  
       		},
       		
       		});
			var plot3 = $.jqplot('chart_container2',[view3], {
			seriesDefaults: {
         		renderer: $.jqplot.PieRenderer,
        		rendererOptions: {   
           			showDataLabels: true,
           			diameter: 200,
         		}
         		
     		},
      		title : 'Defect&nbsp;&nbsp;state',
       		legend: { 
       			show:true,
       			//location: 'e'  
       		},
       		
       		});
		}
	});
	
	
}


function goScopeReport(){
	doFormSubmit('scopeReportForm','goScope.do');
}

function showData(){
	$("#codeReportData").show();
	$("#showHideData").html('<a onclick="hideData()">Hide detail data</a>');
}
function hideData(){
	$("#codeReportData").hide();
	$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
}

function goDetail(){
	doFormSubmit('scopeReportForm','goDefectDetail.do');
}


</script>
</head>

<body>
 <form id="scopeReportForm" name="projectForm" method="post">
 	<input type="hidden" id="selectedsprintId" value="${(selectedsprintId)!''}"/>
 	<#if navigation?has_content>
		${navigation}
	</#if>
	 
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			  	Insight Report 
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">

      <table width="100%" border="0">
      
      <tr>
			<td align="right" width="10%"> Project：</td>
			<td align="left" width="20%" >
				<select id="projectId" name="projectId" class="select" style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list projectList as project>
				             	 <option value="${project.projectId}"  <#if 

'${project.projectId!""}'=='${projectId!""}'>selected</#if>>${project.projectName}</option>
				          	</#list> 
			    </select>
			</td>
			<td align="right" width="15%">Select Sprint：</td>
			<td align="left" width="20%" >
				<select class="select" style="width:180px"  id="sprint" name="sprintId">
					<option value="">Please Select</option>
				</select>
			</td>

	        <td align="right" width="10%">Submit Period：</td>
	        <td align="left" width="30%" >  
	        	<input style="border-style: solid; border-width: 0;width:70px;color:#9c9fa1" id="startDate" 

name="startDate" type="text" value="${startDate!""}" readonly="true"/>-&nbsp;&nbsp;
	            <input style="border-style: solid; border-width: 0;width:80px;color:#9c9fa1" id="endDate" name="endDate" 

type="text" value="${endDate!""}" readonly="true" />
	        </td>
	        
      </tr>
      <tr><td colspan="6" height="60px" ></td></tr>
      <!--<tr><td  align="right">View&nbsp;&nbsp;&nbsp;&nbsp;：</td><td colspan="5" ></td></tr>--!>
      
	  <tr align="left"><td colspan="6" align="left" >
			<table  align="left" border="0" bordercolor="#fff" cellspacing="0" cellpadding="0" style="border-color:#fff;"  >
			<tr>
				<td >
					<div style="float:right;" id="chart_container" align="center"></div>				
				</td>
			
      			<td >
					<div style="float:right;" id="chart_container1" align="center"></div>
				</td>  
			
     			<td >
					<div style="float:right;" id="chart_container2" align="center"></div>
				</td> 
			</tr> 
			</table>     
		</td></tr>
		<tr id="codeReportData" style="display:none" >
	   
	    <td align="center" width="30%" colspan="2" >
	    	<#if reportVo?has_content>
		    	<table border="0" width="60%" align="center">
		    		<tr>
		    			<td class="tablecontent_title"  align="center" width="50%" style="font-

weight:bold;">Pass</td>
		    			<td class="tablecontent_title"  align="center" width="50%" style="font-

weight:bold;">Fail</td>
		    		</tr>
		    		<tr>
		    			<td align="center" width="50%">${reportVo.testcasePass!""}(${reportVo.passCent!""}%)</td>
		    			<td align="center" width="50%">${reportVo.testcaseFail!""}(${reportVo.failCent!""}%)</td>
		    		</tr>
		    	</table>
	    	</#if>
	    </td>
	    <td align="center" width="30%"  colspan="2">
	    	<#if reportVo?has_content>
		    	<table border="0" width="60%" align="center">
		    		<tr>
		    			<td class="tablecontent_title"  width="50%" align="center" style="font-

weight:bold;">Defects</td>
		    			<td class="tablecontent_title"  width="50%" align="center" style="font-

weight:bold;">Vacuous</td>
		    		</tr>
		    		<tr>
		    			<td width="50%" align="center">${reportVo.hasDefect!""}(${reportVo.hasdefectCent!""}%)</td>
		    			<td width="50%" align="center">${reportVo.noDefect!""}(${reportVo.nodefectCent!""}%)</td>
		    		</tr>
		    	</table>
		    </#if>
	    </td>
	    <td align="center" width="30%"  colspan="2">
	    	<#if reportVo?has_content>
		    	<table border="0" width="60%" align="center">
		    		<tr>
		    			<td class="tablecontent_title"   width="50%" align="center" style="font-

weight:bold;">Fixed</td>
		    			<td class="tablecontent_title"  width="50%" align="center" style="font-weight:bold;">Not 

fixed</td>
		    			<td class="tablecontent_title" width="50%" align="center" style="font-weight:bold;"></td>
		    		</tr>
		    		<tr >
		    			<td width="50%" align="center">${reportVo.defectFixed!""}(${reportVo.defectfixedCent!""}%)

</td>
		    			<td width="50%"" align="center">${reportVo.defectNotfixed!""}

(${reportVo.defectnotfixedCent!""}%)</td>
		    			<td width="50%"" align="center"><a class="tooltips" href="#" onclick="goDetail()">
		    			<img src="${base}/images/icon/magnifier.gif" /><span>Detail</span></a>
		    			</td>
		    		</tr>
		    	</table>
		    </#if>
	    </td>
     </tr>  
</table>
<div id="showHideData" align="right"><a onclick="showData()">Show detail data</a></div>

<table width="100%" border="0">
	
</table>
</div>
</form>
</body>
</html>
