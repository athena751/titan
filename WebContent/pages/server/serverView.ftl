<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script src="${base}/js/jquery.metadata.js" type="text/javascript"></script>
<script src="${base}/js/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${base}/js/jquery.ui.button.js" type="text/javascript"></script>
</head>
<script>

$(document).ready(function(){
	

		
	// Server IP
	$("#serverIp").blur(function(){
		var serverIp = this.value;
		if('' == serverIp){
			$("#serverIpMessage").html('<div class="messageNG">Please provide a Server Ip!');
		}else if(serverIp.indexOf('.') < 0){
		    $("#serverIpMessage").html('<div class="messageNG">Please provide a valid Server Ip!');
		}else{$("#serverIpMessage").html('OK');}
				  
		   
				
	});
	// Console IP
	$("#consoleIp").blur(function(){
		var consoleIp = this.value;
		if(consoleIp.indexOf('.') < 0){
		    $("#consoleIpMessage").html('<div class="messageNG">Please provide a valid Console Ip!');
		}else{$("#consoleIpMessage").html('OK');}	  
	   
	});
	
	
});

function checkInput() {

	var serverIpMessage = $("#serverIpMessage").html();
	if('OK' != serverIpMessage){
		alert('There is some error on Server IP, please check it!').html();
		return;
	}	
	save();
}


function save(){    
   doFormSubmit('serverRegForm', 'doServerSave.do');   
}
function cancel() {
   if('searchPage' == '${fromTag!""}'){
      doPageRedirect('goAdvanceSearch.do')
      }else{
      doPageRedirect('serverList.do')
      }
}
</script>

<body>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Server Details</div>
	</td><td align="right">
	</td></tr>
</table>
<form id="serverRegForm" name="serverRegForm" method="post" action="doServerSave.do">
<div class="box">
	<table class="tablecontent" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Server Name：</td>
			<td align="left" width="90%">${server.serverName!""}</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Group：</td>
			<td align="left" width="90%">
				<select id="serverGroupId" name="server.groupId" class="select"  style="width:180px" disabled="disabled">
	        		<option value="" selected>Please Select</option>
		        	<#list groupList as group>
		             	 <option value="${group.groupId}" <#if '${group.groupId!""}'=='${server.groupId!""}'>selected</#if>>${group.groupName}</option>
		          	</#list> 
	          	</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Project：</td>
			<td align="left" width="90%">
				<select id="serverProjectId" name="server.projectId" class="select"  style="width:180px" disabled="disabled">
	        		<option value="" selected>Please Select</option>
		        	<#list projectList as project>
		             	 <option value="${project.projectId}" <#if '${project.projectId!""}'=='${server.projectId!""}'>selected</#if>>${project.projectName}</option>
		          	</#list> 
	          	</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Server Type：</td>
			<td align="left" width="90%">${server.serverType!""}
			</td>
		</tr>
		<tr>
	       <td align="right" class="tablecontent_title" width="10%">Server Status：</td>
	       <td align="left" width="90%">${server.status!""}</td>
        </tr>
		<tr>
	       <td align="right" class="tablecontent_title" width="10%">Hastab：</td>
	       <td align="left" width="90%">
        		<#if '${server.hastab!""}'=='1'>have</#if>
        		<#if '${server.hastab!""}'=='0'>not have</#if>
	       </td>
        </tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Server IP：</td>
			<td align="left" width="90%">${server.serverIp!""}</td>
		</tr>
			
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Operation System：</td>
			<td align="left" width="90%">${server.distro!""}</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">OS UserName：</td>
			<td align="left" width="90%">${server.serverAccount!""}</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">OS PassWord：</td>
			<td align="left" width="90%">********</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">MP/ilo IP：</td>
			<td align="left" width="90%">${server.consoleIp!""}</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">MP/ilo UserName：</td>
			<td align="left" width="90%">${server.consoleAccount!""}</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">MP/ilo PassWord：</td>
			<td align="left" width="90%">********</td>
		</tr>			
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Location：</td>
			<td align="left" width="90%">${server.location!""}</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Serial Number：</td>
			<td align="left" width="90%">${server.sn!""}</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Part Number：</td>
			<td align="left" width="90%">${server.pn!""}</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Mac Address：</td>
			<td align="left" width="90%">${server.macAddr!""}</td>
		</tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Memory：</td>
			<td align="left" width="90%">${server.memory!""}</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">CPU：</td>
			<td align="left" width="90%">${server.cpu!""}</td>
		</tr>		
		</tr>
	       <td align="right" class="tablecontent_title" width="10%">Purchase Date：</td>
	       <td align="left" width="90%"><input id="purchaseDate" name="server.purchaseDate" type="text" class="mh_date" value="${server.purchaseDate!""}" readonly="true" /></td></td>
        <tr>
			
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">${server.description!""}</td>
		</tr>
		<tr>
            <td align="right" class="tablecontent_title" width="10%">Reserve info：</td>
            
            <td align="left" ><#if '${server.serverState!""}'=='RESERVED'>This server reserved by ${reserveVo.userName!""} from  ${reserveVo.startDate!""} to ${reserveVo.endDate!""}</#if></td
         </tr>		

	</table>	
	<table width="100%" border="0">
		<tr><td align="center">
		    <button class="c" type="button" onclick="cancel()" >Return To Server List</button>
	
		</td></tr>
	</table>
</div>
</form>
</body>
</html>
