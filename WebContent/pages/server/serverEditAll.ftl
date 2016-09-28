<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script src="${base}/js/jquery.metadata.js" type="text/javascript"></script>
<script src="${base}/js/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${base}/js/jquery.ui.button.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${base}/css/manhuaDate.1.0.css" />
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
</head>
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

function checkInput(){
    if($("#serverIp")[0].value == ""){
       alert('please provide server IP!');
	   return;
    }
    
     if($("#group")[0].value == ""){
       alert('please select a group for server!');
	   return;
    }
    
     if($("#project")[0].value == ""){
       alert('please provide a project for server!');
	   return;
    }
   
    if($("#consoleIp")[0].value == ""){
       alert('please provide console IP!');
	   return;
    }
    
	save();
}
function save(){  
    
	if(document.getElementById("server.keyServer").checked){	   
	    document.getElementById("server.keyServer").value = "1";	    
	}else{
	    document.getElementById("server.keyServer").value = "0";	
	} 
   doFormSubmit('serverRegForm', 'doServerUpdateAll.do');   
}
function cancel() {
   doPageRedirect('serverAdmin.do')
}
</script>

<body>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Server Update</div>
	</td><td align="right">
	</td></tr>
</table>

<form id="serverRegForm" name="serverRegForm" method="post">
<input type="hidden" id="server.serverId" name="server.serverId"  value="${(server.serverId)!''}"/>
<input type="hidden" id="server.serverState" name="server.serverState"  value="${(server.serverState)!''}"/>
<input type="hidden" id="server.ownerId" name="server.ownerId"  value="${(server.ownerId)!''}"/>
<input type="hidden" id="server.createDate" name="server.createDate"  value="${(server.createDate)!''}"/>		
<input type="hidden" id="server.createUserId" name="server.createUserId"  value="${(server.createUserId)!''}"/>	
<div class="box">
	<table class="tablecontent" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Server Name：</td>
			<td align="left" width="90%">
				<input id="server.serverName" name="server.serverName" class="input_text" type="text" value="${server.serverName!""}"/><span id="serverNameMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Group：</td>
			<td align="left" width="90%">
				<select id="group" name="server.groupId" class="select"  style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list groupList as group>
				             	 <option value="${group.groupId}" <#if '${group.groupId!""}'=='${server.groupId!""}'>selected</#if>>${group.groupName}</option>
				          	</#list> 
			          	</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Project：</td>
			<td align="left" width="90%">
				<select id="project" name="server.projectId" class="select"  style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list projectList as project>
				             	 <option value="${project.projectId}" <#if '${project.projectId!""}'=='${server.projectId!""}'>selected</#if>>${project.projectName}</option>
				          	</#list> 
			          	</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Server Type：</td>
			<td align="left" width="90%">
				<select id="server.serverType" name="server.serverType" class="select" style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list enumList as enum>
				             	 <option value="${enum.enumValue}" <#if '${server.serverType!""}'=='${enum.enumValue!""}'>selected</#if>>${enum.enumValue}</option>
				          	</#list> 
			    </select>
			</td>
		</tr>	
		<tr>
	       <td align="right" class="tablecontent_title" width="10%">Server Status：</td>
	       <td align="left" width="90%">
	        <select id="status" name="server.status" class="select"  style="width:180px">
        		<option value="used"  <#if '${server.status!""}'=='used'>selected</#if>>used</option>
        		<option value="not used"  <#if '${server.status!""}'=='not used'>selected</#if>>not used</option>
		    </select>
		    <span id="statusMessage"></span>
	       </td>
        </tr>
		<tr>
	       <td align="right" class="tablecontent_title" width="10%">Hastab：</td>
	       <td align="left" width="90%">
	        <select id="hastab" name="server.hastab" class="select"  style="width:180px">
        		<option value="1"  <#if '${server.hastab!""}'=='1'>selected</#if>>have</option>
        		<option value="0"  <#if '${server.hastab!""}'=='0'>selected</#if>>not have</option>
		    </select>
		    <span id="hastabMessage"></span>
	       </td>
        </tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Server IP：</td>
			<td align="left" width="90%">
				<input id="serverIp" name="server.serverIp" class="input_text" type="text" value="${server.serverIp!""}"/><span id="serverIpMessage"></span>
			</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Operation System：</td>
			<td align="left" width="90%">
				<input id="server.distro" name="server.distro" class="input_text" type="text" value="${server.distro!""}"/>
			</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">OS UserName：</td>
			<td align="left" width="90%">
				<input id="server.serverAccount" name="server.serverAccount" class="input_text" type="text" value="${server.serverAccount!""}"/>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">OS PassWord：</td>
			<td align="left" width="90%">
				<input id="server.serverPasswd" name="server.serverPasswd" class="input_text" type="password" value="${server.serverPasswd!""}"/>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>MP/ilo IP：</td>
			<td align="left" width="90%">
				<input id="consoleIp" name="server.consoleIp" class="input_text" type="text" value="${server.consoleIp!""}"/><span id="consoleIpMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">MP/ilo UserName：</td>
			<td align="left" width="90%">
				<input id="server.consoleAccount" name="server.consoleAccount" class="input_text" type="text" value="${server.consoleAccount!""}"/>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">MP/ilo PassWord：</td>
			<td align="left" width="90%">
				<input id="server.consolePasswd" name="server.consolePasswd" class="input_text" type="password" value="${server.consolePasswd!""}"/>
			</td>
		</tr>			
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Location：</td>
			<td align="left" width="90%">
				<input id="server.Location" name="server.Location" class="input_text" type="text" value="${server.location!""}"/>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Serial Number：</td>
			<td align="left" width="90%">
				<input id="server.sn" name="server.sn" class="input_text" type="text" value="${server.sn!""}"/>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Part Number：</td>
			<td align="left" width="90%">
				<input id="pn" name="server.pn" class="input_text" type="text" value="${server.pn!""}"/>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Mac Address：</td>
			<td align="left" width="90%">
				<input id="server.macAddr" name="server.macAddr" class="input_text" type="text" value="${server.macAddr!""}"/>
			</td>
		</tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Memory：</td>
			<td align="left" width="90%">
				<input id="server.memory" name="server.memory" class="input_text" type="text" value="${server.memory!""}"/>
			</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">CPU：</td>
			<td align="left" width="90%">
				<input id="server.cpu" name="server.cpu" class="input_text" type="text" value="${server.cpu!""}"/>
			</td>
		</tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Key Server：</td>
			<td align="left" width="90%">
				<input type="checkbox" id="server.keyServer"  <#if 'RESERVED'=='${server.serverState!""}'||'TAKEOVER'=='${server.serverState!""}'> disabled="disabled" </#if> name="server.keyServer"  <#if '${server.keyServer!""}'=='1'>checked='true'</#if> value="${(server.keyServer)!''}"/>
			</td>
		</tr>	
		
		</tr>
	       <td align="right" class="tablecontent_title" width="10%">Purchase Date：</td>
	       <td align="left" width="90%"><input id="purchaseDate" name="server.purchaseDate" type="text" class="mh_date" value="${server.purchaseDate!""}" readonly="true" /></td></td>
        <tr>
				
			
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">
				<textarea id="server.description" name="server.description" class="text_area" value="${server.description!""}">${server.description!""}</textarea>
			</td>
		</tr>		

	</table>	
<table width="100%" border="0">
	<tr><td align="center">
	    <button class="c" type="button" onclick="checkInput()" >Save</button>
	    <button class="c" type="button" onclick="cancel()" >Cancel</button>

	</td></tr>
</table>
</div>
</form>
</body>
</html>
