<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
<script type="text/javascript" src="${base}/js/jqplot/jquery.jqplot.js"></script>
<script type="text/javascript" src="${base}/js/jqplot/excanvas.js"></script>
<script type="text/javascript"  src="${base}/js/plugins/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript"  src="${base}/js/plugins/jqplot.donutRenderer.min.js"></script>
<script type="text/javascript" src="${base}/js/plugins/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="${base}/js/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="${base}/js/plugins/jqplot.pointLabels.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery.json-2.4.min.js"></script>
<link rel="stylesheet" href="${base}/css/jquery.jqplot.css" type="text/css"></link>
<script>

function getReport(){

   //var projectId = document.getElementById("projectId").value;
   //doPageRedirect('goDeviceReport.do?projectId='+projectId);
   
}

function showChart(){
	$("#chart_container").show();
    var showType = $("#showType").val();
	var projectId = '${projectId!""}';
	$('#chart_container').empty(); 
	if(showType=='0'){
	$.ajax( {
		type : 'get',
		url : '${base}/server/getChartXml.do?t=' + new Date().getTime(),
		dataType: "json",
		data : { 'projectId' : projectId,
		         'showType' : showType
		},
		success : function(data) {			
			var view1=[];
			var view2=[];
			var tick=[];
			for(var i=0;i<data.length; i++){					
				view1.push(data[i].freeNum);
				view2.push(data[i].reserveNum);
				tick.push(data[i].serverType);
			}
			var plot1 = $.jqplot('chart_container',[view1,view2], {
				title:'Servers in Project',
				stackSeries: true,
    			captureRightClick: true,				
	            seriesDefaults : {
	                renderer : $.jqplot.BarRenderer, 
	                rendererOptions : {
	                    barMargin : 300,
	                    barWidth:50,
	                },
	            	pointLabels: {
	            		show: true,
	            	},
	            },
	            axes : {
	                xaxis : {
	                    ticks :tick,
	                    renderer : $.jqplot.CategoryAxisRenderer
	                 }
	            },
	            legend: { 
		       			show:true,
		       			//location: 'e',
		       	},
		       	series:[{label:'free'},{label:'reserved'}],	
        	});
			}
		});	
		}else if(showType=='1'){
		$.ajax( {
			type : 'get',
			url : '${base}/server/getChartXml.do?t=' + new Date().getTime(),
			dataType: "json",
			data : { 'projectId' : projectId,
			         'showType' : showType
			},
			success : function(data) {
				var view=[];
				for(var i=0;i<data.length; i++){				
					view.push([data[i].cardType,data[i].cardNum]);
				}
				var plot1 = $.jqplot('chart_container',[view], {
					seriesDefaults: {
						
		         		renderer: $.jqplot.PieRenderer,
		        		rendererOptions: {    
		           			showDataLabels: true,
		           			diameter:300,
		         		}
		     		},
		      		title :'Cards in Project',
		       		legend: { 
		       			show:true,
		       			//location: 'e',
		       		},
	       		
	       		});				
			}
		});
		}else if(showType=='2'){
		$.ajax( {
			type : 'get',
			url : '${base}/server/getChartXml.do?t=' + new Date().getTime(),
			dataType: "json",
			data : { 'projectId' : projectId,
			         'showType' : showType
			},
			success : function(data) {
				var view=[];
				for(var i=0;i<data.length; i++){				
					view.push([data[i].storageType,data[i].storageNum]);
				}
				var plot1 = $.jqplot('chart_container',[view], {
					seriesDefaults: {
						
		         		renderer: $.jqplot.PieRenderer,
		        		rendererOptions: {    
		           			showDataLabels: true,
		           			diameter:300,
		         		}
		     		},
		      		title : 'Stroages in Project',
		       		legend: { 
		       			show:true,
		       			//location: 'e',
		       		},
	       		
	       		});				
			}
		});
	}
}

function showData(){
	$("#serverReportData").show();
	$("#cardReportData").show();
	$("#storageReportData").show();
	$("#showHideData").html('<a onclick="hideData()">Hide detail data</a>');
}
function hideData(){
	$("#serverReportData").hide();
	$("#cardReportData").hide();
	$("#storageReportData").hide();
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
			  	Device Report 
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table width="100%" class="tablecontent">
      
      <tr>
			<td class="tablecontent_title" align="left" width="10%"> Select Project：</td>
			<td align="left" width="90%">
				<select id="projectId" name="projectId" class="select" style="width:180px" onChange="getReport()">
			        		<option value="" selected>Please Select</option>
				        	<#list projectList as project>
				             	 <option value="${project.projectId}"  <#if '${project.projectId!""}'=='${projectId!""}'>selected</#if>>${project.projectName}</option>
				          	</#list> 
			    </select>
			</td>
			
	  </tr>
	  <tr>
			<td class="tablecontent_title" align="left" width="10%">view</td>
			<td align="left">
				<div>
					<select class="select" id="showType" name="showType" onChange="showChart()">
					    <option value="">please select</option>
						<option value="0">Server</option>
						<option value="1">Card</option>
						<option value="2">Storage</option>
					</select>
				</div>
				<div style="width:98%;height:500px;display:none " id="chart_container" align="center"></div>
			</td>      
		</tr>

	  <tr id="serverReportData" style="display:none">
	   <td align="left" class="tablecontent_title" width="10%">Server report</td>
	    <td align="left" width="90%">
	    <table width="100%" cellspacing="0px" style="border-collapse:collapse">
	               <tr>
						<td style='display:none'></td>
						<td width="16.5%">Server Total</td>
						<td width="10%">${serverTotal!""}</td>
						
					</tr>
	     <#if reDeviceVoList?has_content>
          <#list reDeviceVoList as reDevice>
           <tr >
		    <td align="right" width="10%">${reDevice.serverType!""}</td>
            <td align="left" width="50%">
              <table id="tblModule" width="100%" cellspacing="0px" style="border-collapse:collapse">
					<tr>
						<td style='display:none'></td>
						<td width="30%">server Free</td>
						<td>${reDevice.freeNum!""}</td>
						
					</tr>
					<tr>
						<td style='display:none'></td>
						<td width="30%">server reserved</td>
						<td>${reDevice.reserveNum!""}</td>
						
					</tr>
			 </table>	
			 </td>
	      </tr>
         </#list>
         </#if>      
         </table>	
	    </td>
     </tr>     
     <tr id="cardReportData" style="display:none">
	   <td align="left" class="tablecontent_title" width="10%">Card report</td>
	    <td align="left" width="90%">
	    <table width="100%" cellspacing="0px" style="border-collapse:collapse">
	                <tr>
						<td width="10%" style='display:none'></td>
						<td width="10%"> Card Total</td>
						<td width="10%">${cardTotal!""}</td>
					</tr>
	     <#if reCardVoList?has_content>
          <#list reCardVoList as reCard>
           <tr>
		    <td align="left" width="10%">${reCard.cardType!""}</td>
            <td align="left" width="30%">
              <table id="tblModule" width="100%" cellspacing="0px" style="border-collapse:collapse">
					<tr>
						<td width="10%" style='display:none'></td>
						
						<td width="10%">${reCard.cardNum!""}</td>
					</tr>
					
			 </table>	
			 </td>
	      </tr>
         </#list>
         </#if>      
         </table>	
	    </td>
     </tr>
      <tr id="storageReportData" style="display:none">
	   <td align="left" class="tablecontent_title" width="10%">Storage report</td>
	    <td align="left" width="90%">
	    <table width="100%" cellspacing="0px" style="border-collapse:collapse">
	                <tr>
						<td width="10%" style='display:none'></td>
						<td width="10%">Storage Total</td>
						<td width="10%">${storageTotal!""}</td>						
					</tr>
	     <#if reStorageVoList?has_content>
          <#list reStorageVoList as reStorage>
           <tr >
		    <td align="left" width="10%">${reStorage.storageType!""}</td>
            <td align="left" width="30%">
              <table id="tblModule" width="100%">
					<tr>
						<td width="10%" style='display:none'></td>
						
						<td width="10%">${reStorage.storageNum!""}</td>						
					</tr>					
			 </table>	
			 </td>
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
