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


$(document).ready(function(){
	 	$("#projectId").change(function(){
	 		//$("#chart_container").disposeFusionCharts();
	 		$("#userDefectReportData").hide();
	 		$("#defectStateReportData").hide();
			$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
			var project = this.value;
			if(project != '' && $("#rallyquixId").val() != ''){
				//$("#showType").val('');
	 			$("#showType").removeAttr("disabled"); 
	 		}
	 		else{
	 			//$("#showType").val('');
	 			$("#showType").attr('disabled','disabled');
	 		}
			//get repository list of the project selected
	  		$.ajax( {
				type : 'get',
				url : 'getRallyComponentByPorject.do?t=' + new Date().getTime(),
				data : { 'projectId' : project },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success : function(data) {				      
					var y = document.getElementById("rallyquixId");
					for(var j=y.length-1; j>0; j--){
						y.remove(j);
					}
					if(data.length != 0){
						var x = document.getElementById("rallyquixId");
						for(var i=0; i<data.length; i++){
							var option=document.createElement("option");
						    option.value = data[i].rallyquixId;
						    if(data[i].component == ''){
						    	option.text = 'QuIX';
						    }
						    else{
								option.text = data[i].component;
							}
							if(option.value == $("#selectedrallyquixId").val()){
								option.selected = true;
							}
							x.add(option,x.options[null]);
						}
					}
				}
			});
  	   });
  	   
  	   $("#rallyquixId").change(function(){
  	   		//$("#chart_container").disposeFusionCharts();
  	   		$("#userDefectReportData").hide();
  	   		$("#defectStateReportData").hide();
			$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
  	   		if(this.value != ''){
  	   			$("#showType").val('');
  	   			$("#showType").removeAttr("disabled"); 
	 		}
	 		else{
	 			$("#showType").val('');
	 			$("#showType").attr('disabled','disabled');
	 		}
  	  });
  	  
  	  if($("#projectId").val() != ''){
			$("#projectId").trigger("change");
			if($("#selectedrallyquixId").val() != ''){
				$("#showType").removeAttr("disabled"); 
			}
		}
		if($("#selectedrallyquixId").val() != '' && $("#selectedShowtype").val() != ''){
       		showChart();
        }
});

function dateChange(){
	//$("#chart_container").disposeFusionCharts();
  	if(this.value != ''){
  	   	$("#showType").val('');
  	   	$("#showType").removeAttr("disabled"); 
	}
	else{
	 	$("#showType").val('');
	 	$("#showType").attr('disabled','disabled');
	}
}

function showChart(){
	$("#chart_container").show();
	var rallyquixId = $("#rallyquixId").val();
	if(null == rallyquixId || '' == rallyquixId){
		rallyquixId = $("#selectedrallyquixId").val();
	}
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var showType = $("#showType").val();	
	$('#chart_container').empty();  
	$.ajax( {
		type : 'get',
		url : '${base}/report/getDefectChartXml.do?t=' + new Date().getTime(),
		dataType: "json",
		data : { 'rallyquixId': rallyquixId,
		         'showType': showType,
		         'startDate': startDate,
		         'endDate': endDate
		},
		success : function(data) {
			var view=[];
			var title;
			if(showType=='1'){
				title='User submitted';
				for(var i=0;i<data.length; i++){				
					view.push([data[i].userCode,data[i].usersubmitNum]);
				}
			}
			if(showType=='2'){
				title='User developer';
				for(var i=0;i<data.length; i++){				
					view.push([data[i].userCode,data[i].userdeveloperNum]);
				}
			}
			if(showType=='3'){
				title='State';
				view=[['Submitted',data[0].submittedCount],['Open',data[0].openCount],['Fixed',data[0].fixedCount],['Closed',data[0].closedCount]];
			}
			if(showType=='4'){
				title='Priority';
				view=[['Resolve Immediately',data[0].resolveImmCount],['High Attention',data[0].highCount],['Normal',data[0].normalCount],['Low',data[0].lowCount]];				
			}
			var plot1 = $.jqplot('chart_container',[view], {
			seriesDefaults: {
				
         		renderer: $.jqplot.PieRenderer,
        		rendererOptions: {    
           			showDataLabels: true,
           			diameter:300,
         		}
     		},
      		title : title,
       		legend: { 
       			show:true,
       			//location: 'e',
       		},
       		
       		});		
		}
	});
}

function goDefectReport(){
	doFormSubmit('defectReportForm','goDefectReport.do');
}

function showData(){
	$("#userDefectReportData").show();
	$("#defectStateReportData").show();
	$("#showHideData").html('<a onclick="hideData()">Hide detail data</a>');
}
function hideData(){
	$("#userDefectReportData").hide();
	$("#defectStateReportData").hide();
	$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
}


</script>
</head>

<body>
 <form id="defectReportForm" name="projectForm" method="post">
 	<input type="hidden" id="selectedrallyquixId" value="${(selectedrallyquixId)!''}"/>
 	<input type="hidden" id="selectedShowtype" value="${(selectedShowtype)!''}"/>
 	<#if navigation?has_content>
		${navigation}
	</#if>
	 
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			  	Defect Report 
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table width="100%" class="tablecontent">
      
      <tr>
			<td class="tablecontent_title" align="right" width="10%"> Project：</td>
			<td align="left" width="90%">
				<select id="projectId" name="projectId" class="select" style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list projectList as project>
				             	 <option value="${project.projectId}"  <#if '${project.projectId!""}'=='${projectId!""}'>selected</#if>>${project.projectName}</option>
				          	</#list> 
			    </select>
			</td>
			
	  </tr>
	  <tr>
			<td class="tablecontent_title" align="right" width="10%">Select Component：</td>
			<td align="left" width="90%">
				<select class="select" id="rallyquixId" name="rallyquixId">
					<option value=""></option>
				</select>
			</td>
		</tr>
      <tr>

	        <td align="right" class="tablecontent_title" width="10%">Submit Period：</td>
	        <td align="left" width="90%"> From  <input id="startDate" name="startDate" type="text" class="mh_date" value="${startDate!""}" readonly="true" onmouseup="dateChange()"/>
	                                      To    <input id="endDate" name="endDate" type="text" class="mh_date" value="${endDate!""}" readonly="true" /><span id="defaultDate"></span>
	        </td>
	        
      </tr>
	  <tr>
			<td class="tablecontent_title" align="right" width="10%">View：</td>
			<td align="left" width="90%">
				<div>
					<select class="select" id="showType" name="showType" onChange="goDefectReport()" disabled="disabled">
					    <option value="">please select</option>
						<option value="1" <#if '1'=='${selectedShowtype!""}'>selected</#if>>Submitter</option>
						<option value="2" <#if '2'=='${selectedShowtype!""}'>selected</#if>>Developer</option>
						<option value="3" <#if '3'=='${selectedShowtype!""}'>selected</#if>>State</option>
						<option value="4" <#if '4'=='${selectedShowtype!""}'>selected</#if>>Priority</option>
					</select>
				</div>
				<div style="width:98%;height:500px;display:none " id="chart_container" align="center";></div>
			</td>      
		</tr>
		
	  <tr id="userDefectReportData" style="display:none" >
	   <td align="right" class="tablecontent_title" width="10%">User defect Report：</td>
	    <td align="left" width="90%">
	    <table>
	               <tr>
						<td width="10%">Developer</td>
						<td width="10%">Submitted</td>
						<td width="10%">Fixed</td>
					</tr>
	     <#if defectReportList?has_content>
          <#list defectReportList as defectRep>
           <tr >
		    <td align="left" width="10%">${defectRep.userCode!""}</td>
		    <td align="left" width="10%">${defectRep.usersubmitNum!""}</td>
		    <td align="left" width="10%">${defectRep.userdeveloperNum!""}</td>
	      </tr>
         </#list>
         </#if>      
         </table>
	    </td>
     </tr> 
      <tr id="defectStateReportData" style="display:none" >
	   <td align="right" class="tablecontent_title" width="10%">Defect Report：</td>
	    <td align="left" width="90%">
	    <table>
	               <tr>
						<td width="10%">Low</td>
						<td width="10%">Normal</td>
						<td width="10%">High Attention</td>
						<td width="10%">Resolve Immediately</td>
					</tr>
	     <#if defectreportVo?has_content>
           <tr >
		    <td align="left" width="10%">${defectreportVo.lowCount!""}</td>
		    <td align="left" width="10%">${defectreportVo.normalCount!""}</td>
		    <td align="left" width="10%">${defectreportVo.highCount!""}</td>
		    <td align="left" width="10%">${defectreportVo.resolveImmCount!""}</td>
	      </tr>
         </#if> 
         <tr>
						<td width="10%">Submitted</td>
						<td width="10%">Open</td>
						<td width="10%">Fixed</td>
						<td width="10%">Closed</td>
					</tr>
	     <#if defectreportVo?has_content>
           <tr >
		    <td align="left" width="10%">${defectreportVo.submittedCount!""}</td>
		    <td align="left" width="10%">${defectreportVo.openCount!""}</td>
		    <td align="left" width="10%">${defectreportVo.fixedCount!""}</td>
		    <td align="left" width="10%">${defectreportVo.closedCount!""}</td>
	      </tr>
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
