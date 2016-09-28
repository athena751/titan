<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script type="text/javascript" src="${base}/js/jqplot/jquery.jqplot.js"></script>
<script type="text/javascript" src="${base}/js/jqplot/excanvas.js"></script>
<script type="text/javascript"  src="${base}/js/plugins/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript"  src="${base}/js/plugins/jqplot.donutRenderer.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery.json-2.4.min.js"></script>
<link rel="stylesheet" href="${base}/css/jquery.jqplot.css" type="text/css"></link>
<script>

$(document).ready(function(){
	showChart();
	showChartForProj();
});

function getReport(){

   var projectId = document.getElementById("projectId").value;
   doPageRedirect('goResReport.do?projectId='+projectId);   
}

function showChart(){
	$("#chart_container").show();
	var showType = "3";
	var projectId = '${projectId!""}';
	$.ajax( {
		type : 'get',
		url : '${base}/server/getChartXml.do?t=' + new Date().getTime(),
		dataType: "json",
		data : { 'projectId': projectId,
				 'showType' : showType				 
		},
		success : function(data) {
			var view=[];
			for(var i=0;i<data.length; i++){				
					view.push([data[i].userCode,data[i].reserveNum]);
			}
			var plot1 = $.jqplot('chart_container',[view], {
				seriesDefaults: {
					
	         		renderer: $.jqplot.PieRenderer,
	        		rendererOptions: {    
	           			showDataLabels: true,
	           			diameter:300,
	         		}
	     		},
	      		title : 'Reservations By User',
	       		legend: { 
	       			show:true,
	       			//location: 'e',
	       		},
       		
       		});		
		}
	});
}
function showChartForProj(){
	$("#chart_container_proj").show();
	var showType = "4";
	$.ajax( {
		type : 'get',
		url : '${base}/server/getChartXml.do?t=' + new Date().getTime(),
		dataType: "json",
		data : { 'showType' : showType				 
		},
		success : function(data) {
			var view=[];
			for(var i=0;i<data.length; i++){				
					view.push([data[i].projectName,data[i].rePorjNum]);
			}
			var plot1 = $.jqplot('chart_container_proj',[view], {
			seriesDefaults: {
				
         		renderer: $.jqplot.PieRenderer,
        		rendererOptions: {    
           			showDataLabels: true,
           			diameter:300,
         		}
     		},
      		title : 'Reservations in Project',
       		legend: { 
       			show:true,
       			//location: 'e',
       		},
       		
       		});		
		}
	});
}


function showData(){
	$("#resReportData").show();
	$("#resProjReportData").show();
	$("#showHideData").html('<a onclick="hideData()">Hide detail data</a>');
}
function hideData(){
	$("#resReportData").hide();
	$("#resProjReportData").hide();
	$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
}


</script>
</head>

<body>
 <form id="projectForm" name="projectForm" method="post">
 	<#if navigation?has_content>
		${navigation}
	</#if>
	 
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			  	Reservation Report 
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table width="100%" class="tablecontent">
      
      
      <tr>
			<td class="tablecontent_title" align="left" width="10%">View By User：</td>
  
			<td align="left">
			   <div>
					<select id="projectId" name="projectId" class="select" style="width:180px" onChange="getReport()">
			        		<option value="" selected>Select Project</option>
				        	<#list projectList as project>
				             	 <option value="${project.projectId}"  <#if '${project.projectId!""}'=='${projectId!""}'>selected</#if>>${project.projectName}</option>
				          	</#list> 
			        </select>
				</div>
				<div style="width:98%;height:500px;display:none " id="chart_container" align="center"></div>
			</td>      
	  </tr>
	   <tr>
			<td class="tablecontent_title" align="left" width="10%">View By Porject：</td>
			<td align="left">
				<div style="width:98%;height:500px;display:none " id="chart_container_proj" align="center"></div>
			</td>      
	  </tr>

	  <tr id="resReportData" style="display:none">
	   <td align="left" class="tablecontent_title" width="10%">Reservation Report：</td>
	    <td align="left" width="90%">
	    <table>
	               <tr>
						<td width="10%">User</td>
						<td width="10%">Reservation Number</td>
						<td width="10%">Sserver Details</td>
						
					</tr>
	     <#if resReportList?has_content>
          <#list resReportList as resReport>
           <tr >
		    <td align="left" width="10%">${resReport.userCode!""}</td>
		    <td align="left" width="10%">${resReport.reserveNum!""}</td>
		    <td align="left" width="10%">${resReport.servers!""}</td>
	      </tr>
         </#list>
         </#if>      
         </table>
	    </td>
     </tr>     
     <tr id="resProjReportData" style="display:none">
	   <td align="left" class="tablecontent_title" width="10%">Reservation Report：</td>
	    <td align="left" width="90%">
	    <table>
	               <tr>
						<td width="10%">Project</td>
						<td width="10%">Reservation Number</td>
						<td width="10%">Sserver Details</td>
						
					</tr>
	     <#if resProjReportList?has_content>
          <#list resProjReportList as resReport>
           <tr >
		    <td align="left" width="10%">${resReport.projectName!""}</td>
		    <td align="left" width="10%">${resReport.rePorjNum!""}</td>
		    <td align="left" width="10%">${resReport.servers!""}</td>
	      </tr>
         </#list>
         </#if>      
         </table>
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
