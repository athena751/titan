<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/manhuaDate.1.0.css" />
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
<script type="text/javascript" src="${base}/js/fusionCharts/FusionCharts.js"></script>
<script type="text/javascript" src="${base}/js/fusionCharts/FusionCharts.jqueryplugin.js"></script>
<script>
$(document).ready(function(){
	 	$("#projectId").change(function(){
	 		$("#contributionReportData").hide();
			$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
			var project = this.value;
	  		//get sprint list of the project selected
	  		$.ajax( {
				type : 'get',
				url : '${base}/sprint/getSprintsByPorject.do',
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
  	   		$("#contributionReportData").hide();
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
					goContributionReport();
				}
			});
  	  });
		if($("#selectedsprintId").val() != ''){
			$("#projectId").trigger("change");
        }
});

function showData(){
	$("#contributionReportData").show();
	$("#showHideData").html('<a onclick="hideData()">Hide detail data</a>');
}
function hideData(){
	$("#contributionReportData").hide();
	$("#showHideData").html('<a onclick="showData()">Show detail data</a>');
}

function goContributionReport(){
	doFormSubmit('contributionReportForm','goContribution.do');
}
</script>
</head>

<body>
<form id="contributionReportForm" name="projectForm" method="post">
 	<input type="hidden" id="selectedsprintId" value="${(selectedsprintId)!''}"/>
 	<#if navigation?has_content>
		${navigation}
	</#if>
	 
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			  	Contribution Report 
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
				             	 <option value="${project.projectId}"  <#if '${project.projectId!""}'=='${projectId!""}'>selected</#if>>${project.projectName}</option>
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
	        	<input style="border-style: solid; border-width: 0;width:70px;color:#9c9fa1" id="startDate" name="startDate" type="text" value="${startDate!""}" readonly="true"/>-&nbsp;&nbsp;
	            <input style="border-style: solid; border-width: 0;width:80px;color:#9c9fa1" id="endDate" name="endDate" type="text" value="${endDate!""}" readonly="true" />
	        </td>
	      </tr>
	      <#if contributionReportVo?has_content >
	      	<tr>
	      		<td colspan="6" align="center">
	      		&nbsp;
	      			<table border="0">
	      				<tr>
	      					<td align="center"><h3>The Max Codes</h3></td><td width="150px"></td>
	      					<td align="center"><h3>The Max Fix Bugs</h3></td><td width="150px"></td> 
	      					<td align="center"><h3>The Selfless Dedication</h3></td><td width="150px"></td>
	      					<td align="center"><h3>Function</h3></td>      				
	      				</tr>
	      				
	      				<tr>		      
							<td  align="center" height="150px" width="222px"  style="color:#515150;background-image:url(../images/medal/codes.png);background-repeat:no-repeat ;">
							<h2>${(contributionReportVo.maxtotalCount)!""}</h2></td><td width="150px"></td>
							<td  align="center" height="150px" width="222px"  style="color:#515150;background-image:url(../images/medal/fix.png);background-repeat:no-repeat ;">
							<h2>${(contributionReportVo.maxbugfixCount)!""}</h2></td><td width="150px"></td>
							<td  align="center" height="150px" width="222px"  style="color:#515150;background-image:url(../images/medal/heart.png);background-repeat:no-repeat ;">
							<h2>${(contributionReportVo.maxunknowCount)!""}</h2></td><td width="150px"></td>
							<td  align="center" height="150px" width="222px"  style="color:#515150;background-image:url(../images/medal/function.png);background-repeat:no-repeat ;">
							<h2>${(contributionReportVo.maxuscodeCount)!""}</h2></td>
						</tr>
						<tr>
							<td  align="center" valign="bottom" height="44px" width="222px" style="color:#515150;background-image:url(../images/medal/banner.png);background-repeat:no-repeat ;">
							<h3>${(contributionReportVo.maxtotalName)!""}</h3></td><td width="150px"></td>
							<td  align="center" valign="bottom" height="44px" width="222px" style="color:#515150;background-image:url(../images/medal/banner.png);background-repeat:no-repeat ;">
							<h3>${(contributionReportVo.maxbugfixName)!""}</h3></td><td width="150px"></td>
							<td  align="center" valign="bottom" height="44px" width="222px" style="color:#515150;background-image:url(../images/medal/banner.png);background-repeat:no-repeat ;">
							<h3>${(contributionReportVo.maxunknownName)!""}</h3></td><td width="150px"></td>
							<td  align="center" valign="bottom" height="44px" width="222px" style="color:#515150;background-image:url(../images/medal/banner.png);background-repeat:no-repeat ;">
							<h3>${(contributionReportVo.maxuscodeName)!""}</h3></td>
						</tr>
	      			</table>	      		
	      		</td>
	      	</tr>
	      </#if>
	      <tr height="40px"></tr>
		  <tr id="contributionReportData" style="display:none" >		   
		    <td align="center" colspan="6">
			    <table>
	               <tr class="tablecontent_title">
						<td align="center" width="100">Developer</td>
						<td align="center" width="200">Total</td>
						<td align="center" width="200">Defect</td>
						<td align="center" width="200">User Story</td>
						<td align="center" width="300">QuIX</td>
						<td width="10%">Unknown</td>
					</tr>
				    <#if contributionReportList?has_content>
			          <#list contributionReportList as contributionRep>
			           <tr class="mousechange">
					    <td align="center" width="10%">${contributionRep.devName!""}</td>
					    <td align="center" width="20%">${contributionRep.totalCommit!""}</td>
					    <td align="center" width="20%">${contributionRep.defectcodeCount!""}</td>
					    <td align="center" width="20%">${contributionRep.uscodeCount!""}</td>
					    <td align="center" width="20%">${contributionRep.quixcodeCount!""}</td>
					    <td align="left" width="10%">${contributionRep.unknowcodeCount!""}</td>
				      </tr>
			         </#list>
			        </#if>      
		         </table>
		   </td>
	      </tr>  
		</table>
		<div id="showHideData" align="right"><a onclick="showData()">Show detail data</a></div>
	</div>
</form>
</body>
</html>
