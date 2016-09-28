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
<script type="text/javascript" src="${base}/js/plugins/jqplot.highlighter.min.js"></script>
<script type="text/javascript" src="${base}/js/plugins/jqplot.cursor.min.js"></script>
<script type="text/javascript" src="${base}/js/plugins/jqplot.dateAxisRenderer.min.js"></script>
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
	 		$("#codeReportData").hide();
			$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
			var project = this.value;
			if(project != '' && $("#reportId").val() != ''){
				//$("#showType").val('');
	 			$("#showType").removeAttr("disabled"); 
	 		}
	 		else{
	 			//$("#showType").val('');
	 			$("#showType").attr('disabled','disabled');
	 		}
			//get repository list of the project selected
	  		$.ajax({
				type : 'get',
				url : 'getReposByPorject.do?t=' + new Date().getTime(),
				data : { 'projectId' : project },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success : function(data) {				      
					var y = document.getElementById("reportId");
					for(var j=y.length-1; j>0; j--){
						y.remove(j);
					}
					if(data.length != 0){
						var x = document.getElementById("reportId");
						for(var i=0; i<data.length; i++){
							var option=document.createElement("option");
						    option.value = data[i].reportaryId;
							option.text = data[i].moduleName;
							if(option.value == $("#selectedreportId").val()){
								option.selected = true;
							}
							x.add(option,x.options[null]);
						}
					}
				}
			});
  	   });
  	   
  	   $("#reportId").change(function(){
  	   		//$("#chart_container").disposeFusionCharts();
  	   		$("#codeReportData").hide();
			$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
  	   		if(this.value != ''){
  	   			$("#showType").val('');
  	   			$("#showType").removeAttr("disabled"); 
	 		}
	 		else{
	 			$("#showType").val('');
	 			$("#showType").attr('disabled','disabled');
	 		}
	 		var reportId = this.value;
	 		$.ajax( {
				type : 'get',
				url : 'getDefaultPeriod.do?t=' + new Date().getTime(),
				data : { 'reportId' : reportId },
				success : function(msg) {				      
					$("#defaultDate").html(msg);
				}
			});
  	  });
  	  
  	  if($("#projectId").val() != ''){
			$("#projectId").trigger("change");
			if($("#selectedreportId").val() != ''){
				$("#showType").removeAttr("disabled"); 
			}
		}
		if($("#selectedreportId").val() != '' && $("#selectedShowtype").val() != ''){
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
	var reportId = $("#reportId").val();
	if(null == reportId || '' == reportId){
		reportId = $("#selectedreportId").val();
	}
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var showType = $("#showType").val();
	$('#chart_container').empty(); 
	$.ajax( {
		type : 'get',
		url : '${base}/report/getCodeCommitChartXml.do?t=' + new Date().getTime(),
		dataType: "json",
		data : { 'reportId': reportId,
		         'showType': showType,
		         'startDate': startDate,
		         'endDate': endDate
		},
		success : function(data) {
			var title;
			var view=[];
			var lab=[];
			if(showType == '3'){				
				var views=[];
				for(var i=0;i<data.length;i++){
						lab.push({label:data[i].year});
						view=[];
						view.push(['01-Jan-08',data[i].janNum]);
						view.push(['01-Feb-08',data[i].febNum]);
						view.push(['01-Mar-08',data[i].matNum]);
						view.push(['01-Apr-08',data[i].aprNum]);
						view.push(['01-May-08',data[i].mayNum]);
						view.push(['01-Jun-08',data[i].junNum]);
						view.push(['01-Jul-08',data[i].julNum]);
						view.push(['01-Aug-08',data[i].augNum]);
						view.push(['01-Sep-08',data[i].sepNum]);
						view.push(['01-Oct-08',data[i].octNum]);
						view.push(['01-Nov-08',data[i].novNum]);
						view.push(['01-Dec-08',data[i].decNum]);
						views.push(view);
				}
				var plot2 = $.jqplot('chart_container',views, {
					title : 'Commit annual report',	
					axes:{
				        xaxis:{
				          label: "Month",
				          renderer:$.jqplot.DateAxisRenderer,
				          tickOptions:{
				            formatString:'%b',
				          },
				          
				        },
				        yaxis:{
				          label: "Count",
				          min:0,
				        }
				      },
				      highlighter: {
				        show: true,
				        sizeAdjust: 7.5
				      },
				      cursor: {
				        show: false
				      },
				      legend: { 
		       			show:true,
		       			//location: 'e',
		       		  },
		       		  series:lab,		       		  	       		
		       		});	
			}else{
				if(showType == '0'){
					title='Code Committed By Use';
					for(var i=0;i<data.length;i++){
						view.push([data[i].userCode,data[i].commitNum]);
					}
				}
				if(showType == '1'){
					title='Average';				
					for(var i=0;i<data.length;i++){
						view.push([data[i].userCode,data[i].average]);
					}
				}
				if(showType == '2'){
					title='Code changed By User';
					for(var i=0;i<data.length;i++){
						view.push([data[i].userCode,data[i].codeNum]);
					}
										
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
			
			
						
		}
	});
}

function goCodeReport(){
	doFormSubmit('codeReportForm','goCodeReport.do');
}

function showData(){
	$("#codeReportData").show();
	$("#showHideData").html('<a onclick="hideData()">Hide detail data</a>');
}
function hideData(){
	$("#codeReportData").hide();
	$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
}


</script>
</head>

<body>
 <form id="codeReportForm" name="projectForm" method="post">
 	<input type="hidden" id="selectedreportId" value="${(selectedreportId)!''}"/>
 	<input type="hidden" id="selectedShowtype" value="${(selectedShowtype)!''}"/>
 	<#if navigation?has_content>
		${navigation}
	</#if>
	 
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			  	Code Commit Report 
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
				<select class="select" id="reportId" name="reportId">
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
			<td align="left">
				<div>
					<select class="select" id="showType" name="showType" onChange="goCodeReport()" disabled="disabled">
					    <option value="">please select</option>
						<option value="0" <#if '0'=='${selectedShowtype!""}'>selected</#if>>Commit Number</option>
						<option value="1" <#if '1'=='${selectedShowtype!""}'>selected</#if>>Average</option>
						<option value="2" <#if '2'=='${selectedShowtype!""}'>selected</#if>>Code Change</option>
						<option value="3" <#if '3'=='${selectedShowtype!""}'>selected</#if>>Lines</option>
					</select>
				</div>
				<div style="width:98%;height:550px;display:none " id="chart_container" align="center"></div>
			</td>      
		</tr>
		
	  <tr id="codeReportData" style="display:none" >
	   <td align="right" class="tablecontent_title" width="10%">Code Commit Report：</td>
	    <td align="left" width="90%">
	    <table>
	               <tr>
						<td width="10%">Developer</td>
						<td width="10%">Code Change</td>
						<td width="10%">Commit Number</td>
						<td width="10%">Average</td>
					</tr>
	     <#if codeReportList?has_content>
          <#list codeReportList as codeRep>
           <tr >
		    <td align="left" width="10%">${codeRep.userCode!""}</td>
		    <td align="left" width="10%">${codeRep.codeNum!""}</td>
		    <td align="left" width="10%">${codeRep.commitNum!""}</td>
		    <td align="left" width="10%">${codeRep.average!""}</td>
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
