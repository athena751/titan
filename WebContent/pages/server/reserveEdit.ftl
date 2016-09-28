<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/manhuaDate.1.0.css" />
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
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

function checkInput() {
    if($("#startDate")[0].value == ""){
       alert('please provide start date!');
	   return;
    }
    if($("#endDate")[0].value == ""){
       alert('please provide end date!');
	   return;
    }
	var startDate = $("#startDate")[0].value;
	var endDate = $("#endDate")[0].value;	
	var d = new Date(startDate.replace(/-/g,"/")); 
	var d1 = new Date(endDate.replace(/-/g,"/"));
	var m = (d1.getTime()-d.getTime())/(1000*60*60*24); 
	if(m<0){
		alert('Start Date should before End Date!');
		return;
	}
	onSave();
}

function onSave() {   
   doFormSubmit('serverReserve','updateReserve.do?showType='+"${showType!""}")
}
function onBack() {
   doPageRedirect('reserveList.do?showType='+"${showType!""}")
}

function showData(){
	$("#reserveHist").show();	
	$("#reserveSubHist").hide();
}

function hideData(){
    $("#reserveHist").hide();	
	$("#reserveSubHist").show();
}
</script>
<script type="text/javascript" language="javascript" class="init">
	$(document).ready(function(){
		if('${showType!""}'=='SERVERRES' ){
			$('#serverRes').show();
			$('#cardRes').hide();
		}else if('${showType!""}'=='CARDRES' ){
			$('#serverRes').hide();
			$('#cardRes').show();
		}
	});
</script>
</head>

<body>
 <form id="serverReserve" name="serverReserveform" method="post">
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5">
			<#if '${isView!""}'=='1'>
			  	Reservation  
			  <#else>
			  	<#if '${showType!""}'=='SERVERRES'>
					Update server reservation
				<#else>
					Update card reservation
				</#if>  
			  </#if>			  	
			 </span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>
<div id="jobinfo" class="box">
	<div id="serverRes" style="display: none;">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
		<input type="hidden" id="serverId" name="serverId"  value="${(server.serverId)!''}"/>	
		<input type="hidden" id="reservation.serverId" name="reservation.serverId"  value="${(server.serverId)!''}"/>
		<input type="hidden" id="reservation.reserveId" name="reservation.reserveId"  value="${(reservation.reserveId)!''}"/>
		<input type="hidden" id="reservation.userId" name="reservation.userId"  value="${(reservation.userId)!''}"/>
		<tr>
		  <td align="right" class="tablecontent_title" width="10%">Server Name:</td>
		  <td align="left" width="90%"><input id="serverName" name="server.serverName" type="text" class="input_text" value="${(server.serverName)!""}"/></td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Operation System:</td>
			<td align="left" width="90%">${(server.distro)!""}
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Server Type:</td>
			<td align="left" width="90%">${(server.serverType)!""}
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Memory:</td>
			<td align="left" width="90%">${(server.memory)!""}</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">CPU:</td>
			<td align="left" width="90%">${(server.cpu)!""}</td>
		</tr>		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Group:</td>
			<td align="left" width="90%">
				<select id="serverGroupId" name="server.groupId" class="select"  style="width:180px" disabled="disabled">
	        		<option value="" selected>Please Select</option>
		        	<#list groupList as group>
		             	 <option value="${(group.groupId)!""}" <#if '${(group.groupId)!""}'=='${(server.groupId)!""}'>selected</#if>>${(group.groupName)!""}</option>
		          	</#list> 
	          	</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Project:</td>
			<td align="left" width="90%">
				<select id="serverProjectId" name="server.projectId" class="select"  style="width:180px" disabled="disabled">
	        		<option value="" selected>Please Select</option>
		        	<#list projectList as project>
		             	 <option value="${(project.projectId)!""}" <#if '${(project.projectId)!""}'=='${(server.projectId)!""}'>selected</#if>>${(project.projectName)!""}</option>
		          	</#list> 
	          	</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Location:</td>
			<td align="left" width="90%">${(server.location)!""}</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description:</td>
			<td align="left" width="90%">${(server.description)!""}</td>
		</tr>		
	  </table>
	</div>
	<div id="cardRes" style="display: none;">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
		<input type="hidden" id="cardId" name="cardId"  value="${(card.cardId)!''}"/>
		<tr>
		  	<td align="right" class="tablecontent_title" width="10%">Card Name:</td>
		  	<td align="left" width="90%"><input id="cardName" name="card.cardName" type="text" class="input_text" value="${(card.cardName)!""}"/></td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Card Type:</td>
			<td align="left" width="90%">${(card.cardType)!""}</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">SN:</td>
			<td align="left" width="90%">${(card.sn)!""}</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">PN:</td>
			<td align="left" width="90%">${(card.pn)!""}</td>
		</tr>		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Group:</td>
			<td align="left" width="90%">
				<select id="cardGroupId" name="card.groupId" class="select"  style="width:180px" disabled="disabled">
	        		<option value="" selected>Please Select</option>
		        	<#list groupList as group>
		             	 <option value="${(group.groupId)!""}" <#if '${(group.groupId)!""}'=='${(card.groupId)!""}'>selected</#if>>${(group.groupName)!""}</option>
		          	</#list> 
	          	</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Project:</td>
			<td align="left" width="90%">
				<select id="cardProjectId" name="card.projectId" class="select"  style="width:180px" disabled="disabled">
	        		<option value="" selected>Please Select</option>
		        	<#list projectList as project>
		             	 <option value="${(project.projectId)!""}" <#if '${(project.projectId)!""}'=='${(card.projectId)!""}'>selected</#if>>${(project.projectName)!""}</option>
		          	</#list> 
	          	</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Location:</td>
			<td align="left" width="90%">${(card.location)!""}
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description:</td>
			<td align="left" width="90%">${(card.description)!""}
			</td>
		</tr>		
	  </table>
	</div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
		<tr>
		  <td align="right" class="tablecontent_title" width="10%">Reserved By:</td>
		  <td align="left" width="90%">
				<select id="reservation.userId" name="reservation.userId"  class="select" style="width:180px" disabled="disabled">
					<option value="" selected>Please Select</option>
					<#list userList as user>
				            <option value="${user.userId}" <#if '${currentUserId!""}'=='${user.userId!""}'>selected</#if>>${user.userCode}</option>
				    </#list>
				</select>
			</td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="10%">Start Date:</td>
			<td align="left" width="90%"><#if '${isView!""}'=="1">${reserveVo.startDate!""}<#else><input id="startDate" name="reserveVo.startDate" type="text" class="mh_date" value="${reserveVo.startDate!""}" readonly="true" /></#if></td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="10%">End Date:</td>
			<td align="left" width="90%"><#if '${isView!""}'=="1">${reserveVo.endDate!""}<#else><input id="endDate" name="reserveVo.endDate" type="text" class="mh_date" value="${reserveVo.endDate!""}" readonly="true" /></#if></td>
		</tr>
		
		<tr>
		  <td align="right" class="tablecontent_title" width="10%">Reserve Note:</td>
		  <td align="left" width="90%"><#if '${isView!""}'=="1">${reservation.remark!""}<#else>
		  <textarea class="text_area" id="remark" name="reservation.remark" value="${reservation.remark!""}">${reservation.remark!""}</textarea>
		  </#if></td>
		</tr>
		<tr id="reserveSubHist" >
		  <td align="right" class="tablecontent_title" width="10%">Reserve History:</td>
		  <td align="left" width="90%">
		  	<table>  
			  <#if histVoSubList?has_content >
				  <#list histVoSubList as hist>
				     <tr >
						 <td align="left" ><#if '${showType!""}'=='SERVERRES'>${(server.serverName)!""}<#else>${(card.cardName)!""}</#if> has been ${hist.actionType!""} by <#if '${(hist.userName)!""}'=="N/A"> system <#else> ${(hist.userName)!""} </#if><#if '${(hist.actionType)!""}'=="reserved"> from  ${(hist.startDate)!""} to ${(hist.endDate)!""} </#if> at ${(hist.createDate)!""}</td>
					  </tr>
				  </#list>
				  <#else>
			      <#list histVoList as histVo>
				      <tr>
						 <td align="left" ><#if '${showType!""}'=='SERVERRES'>${(server.serverName)!""}<#else>${(card.cardName)!""}</#if> has been ${histVo.actionType!""} by <#if '${histVo.userName!""}'=="N/A"> system <#else> ${histVo.userName!""} </#if><#if '${histVo.actionType!""}'=="reserved"> from  ${histVo.startDate!""} to ${histVo.endDate!""} </#if> at ${histVo.createDate!""}</td>
					  </tr>
			      </#list> 
			   </#if>   
		   </table>    
		   <div id="showHideData" align="left"><#if histVoSubList?has_content ><a onclick="showData()">Show More Records</a></#if></div>
		  </td>
		</tr>
		<tr id="reserveHist" style="display:none" >
		  <td align="right" class="tablecontent_title" width="10%">Reserve History:</td>
		  <td align="left" width="90%">  
			   <table>   
			    <#if histVoList?has_content >
			     <#list histVoList as histVo>
			       <tr  >
					 <td align="left" ><#if '${showType!""}'=='SERVERRES'>${(server.serverName)!""}<#else>${(card.cardName)!""}</#if> has been ${histVo.actionType!""} by <#if '${histVo.userName!""}'=="N/A"> system <#else> ${histVo.userName!""} </#if><#if '${histVo.actionType!""}'=="reserved"> from  ${histVo.startDate!""} to ${histVo.endDate!""} </#if> at ${histVo.createDate!""}</td>
				  </tr>
			     </#list>   
			    </#if>  
			   </table>
			   <div id="showHideData" align="left"><a onclick="hideData()">Hide Records </a></div>
		  </td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr><td align="center">
		<#if '${isView!""}'!="1">
			<input type="button" onclick="checkInput()" value="Save"></input>
		</#if>
			<input type="button" onclick="onBack()" value="Return"></input>
		</td></tr>
	</table>
</div>
</form>
</body>
</html>
