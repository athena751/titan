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
	 		var sprintId = this.value;
	 		var projectId = $("#projectId").val();
	 		$.ajax( {
				type : 'get',
				url : '${base}/report/getSprintPeriod.do?t=' + new Date().getTime(),
				data : { 'sprintId' : sprintId,
						 'projectId' : projectId},
				success : function(msg) {	
					var d = msg.split(';');
					$("#startDate").val(d[0]);
					$("#endDate").val(d[1]);
					goStrongFieldReport();
				}
			});
  	  });
		if($("#selectedsprintId").val() != ''){
			$("#projectId").trigger("change");
        }
});

var temp="";
function showData(){
	hideData(temp);
	$("#myStrongField").show();
	temp = name;
}
function hideData(){
	$("#myStrongField").hide();
}

function goStrongFieldReport(){
	doFormSubmit('strongFieldReportForm','myStrongField.do');
}
</script>
</head>

<body>
<form id="strongFieldReportForm" name="projectForm" method="post">
 	<input type="hidden" id="selectedsprintId" value="${(selectedsprintId)!""}"/>
 	<#if navigation?has_content>
		${navigation}
	</#if>
	 
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			  	My Strong Field  
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
				             	 <option value="${(project.projectId)!""}"  <#if '${project.projectId!""}'=='${projectId!""}'>selected</#if>>${(project.projectName)!""}</option>
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
	      <tr height="40px"></tr>
	      <#if strongFieldReportVo?has_content>
	      	<tr class="tablecontent_title">
	      		<td  align="center" width="10%">Name</td>
	      		<td  align="center" width="10%">Strong Field</td>
	      	</tr>
      		<tr class="tablecontent_title">
			    <td align="center" width="10%"><a onclick="showData();" ondblclick="hideData();">${(strongFieldReportVo.name)!""}</a></td>
			    <td align="center" width="10%">${(strongFieldReportVo.strongFieldName)!""}</td>
	  		</tr>
    	  </#if>
	      <tr height="40px"></tr>
		  <#if strongFieldReportVo?has_content>
		    <tr id="myStrongField" style="display: none">		   
		    	<td align="center" colspan="6">
			    	<table>
			          <tr class="tablecontent_title">
						<td align="center" width="100">NAME</td>
						<td align="center" width="100">STRONGFIELDNAME</td>
						<#if '${strongFieldReportVo.javaCommitLineCount!""}'!='0'>
							<td align="center" width="100">JAVA</td>
						</#if>
						<#if '${strongFieldReportVo.ftlCommitLineCount!""}'!='0'>
							<td align="center" width="100">FTL</td>
						</#if>
						<#if '${strongFieldReportVo.xmlCommitLineCount!""}'!='0'>
							<td align="center" width="100">XML</td>
						</#if>
						<#if '${strongFieldReportVo.jspCommitLineCount!""}'!='0'>
							<td align="center" width="100">JSP</td>
						</#if>
						<#if '${strongFieldReportVo.jsCommitLineCount!""}'!='0'>
							<td align="center" width="100">JAVASCRIPT</td>
						</#if>
						<#if '${strongFieldReportVo.cssCommitLineCount!""}'!='0'>
							<td align="center" width="100">CSS</td>
						</#if>
						<#if '${strongFieldReportVo.sqlCommitLineCount!""}'!='0'>
							<td align="center" width="100">SQL</td>
						</#if>
						<#if '${strongFieldReportVo.pngCommitLineCount!""}'!='0'>
							<td align="center" width="100">PNG</td>
						</#if>
	     			   </tr>
			           <tr class="tablecontent_title">
					    <td align="center" width="10%">${strongFieldReportVo.name!""}</td>
					    <td align="center" width="10%">${strongFieldReportVo.strongFieldName!""}</td>
					    <#if '${strongFieldReportVo.javaCommitLineCount!""}'!='0'>
					    	<td align="center" width="10%">${strongFieldReportVo.javaCommitLineCount!""}</td>
					    </#if>
					    <#if '${strongFieldReportVo.ftlCommitLineCount!""}'!='0'>
					    	<td align="center" width="10%">${strongFieldReportVo.ftlCommitLineCount!""}</td>
					    </#if>
					    <#if '${strongFieldReportVo.xmlCommitLineCount!""}'!='0'>
					    	<td align="center" width="10%">${strongFieldReportVo.xmlCommitLineCount!""}</td>
					    </#if>
					    <#if '${strongFieldReportVo.jspCommitLineCount!""}'!='0'>
					    	<td align="center" width="10%">${strongFieldReportVo.jspCommitLineCount!""}</td>
					    </#if>
					    <#if '${strongFieldReportVo.jsCommitLineCount!""}'!='0'>
					    	<td align="center" width="10%">${strongFieldReportVo.jsCommitLineCount!""}</td>
					    </#if>
					    <#if '${strongFieldReportVo.cssCommitLineCount!""}'!='0'>
					    	<td align="center" width="10%">${strongFieldReportVo.cssCommitLineCount!""}</td>
					    </#if>
					    <#if '${strongFieldReportVo.sqlCommitLineCount!""}'!='0'>
					    	<td align="center" width="10%">${strongFieldReportVo.sqlCommitLineCount!""}</td>
					    </#if>
					    <#if '${strongFieldReportVo.pngCommitLineCount!""}'!='0'>
					    	<td align="center" width="10%">${strongFieldReportVo.pngCommitLineCount!""}</td>
					    </#if>
				      </tr>
		         </table>
			   </td>
		    </tr>
          </#if>      
		</table>
	</div>
</form>
</body>
</html>
