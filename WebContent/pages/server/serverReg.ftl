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

$(document).ready(function(){
	
	//Server name
	$("#serverName").blur(function(){
	  	var serverName = this.value;
	  	if('' == serverName){
	  		$("#serverNameMessage").html('<div class="messageNG">Please provide a Server name!</div>');
	  	}
	  	else{
	  		$.ajax( {
				type : 'get',
				url : '${base}/server/checkServerName.do?t=' + new Date().getTime(),
				dataType: "text",
				data : { 'serverName' : serverName },
				success : function(msg) {
					if (msg == "exist") {	
						$("#serverNameMessage").html('<div class="messageNG">The server name has been registered\! please use another server name\!</div>');
					}
					else{
						$("#serverNameMessage").html('OK');
					}
					
				}
			});
	  	}
	});	
	

	
	// Project
	$("#project").blur(function(){
		var project = this.value;
		if('' == project){
			$("#projectMessage").html('<div class="messageNG">Please select a project!');
		}
		else{
			$("#projectMessage").html('OK');
		}
	});
	
	// Project
	$("#group").blur(function(){
		var project = this.value;
		if('' == project){
			$("#groupMessage").html('<div class="messageNG">Please select a group!');
		}
		else{
			$("#groupMessage").html('OK');
		}
	});
	
	// Type
	$("#serverType").blur(function(){
		var serverType = this.value;
		if('' == serverType){
			$("#serverTypeMessage").html('<div class="messageNG">Please select a type!');
		}
		else{
			$("#serverTypeMessage").html('OK');
		}
	});
	
	
	
	
		
	// Server IP
	$("#serverIp").blur(function(){
		var serverIp = this.value;
		var exp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		
		if('' == serverIp){
			$("#serverIpMessage").html('<div class="messageNG">Please provide a Server IP,such as:xxx.xxx.xxx.xxx.and require x is a number,and xxx<=255,or Unknown!');
		}else if('Unknown'==serverIp){
			$("#serverIpMessage").html('OK');
		}else if(serverIp.indexOf('.') < 0){
		    $("#serverIpMessage").html('<div class="messageNG">Please provide a valid Server IP,such as:xxx.xxx.xxx.xxx.and require x is a number,and xxx<=255,or Unknown!');
		}else if(!exp.exec(this.value)){
			$("#serverIpMessage").html('<div class="messageNG">Please provide a valid Server IP,such as:xxx.xxx.xxx.xxx.and require x is a number,and xxx<=255,or Unknown!');
		}
		else{$("#serverIpMessage").html('OK');}
				  
		   
				
	});
	// Console IP
	$("#consoleIp").blur(function(){
		var consoleIp = this.value;
		var exp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		
		if(consoleIp.indexOf('.') < 0){
		    $("#consoleIpMessage").html('<div class="messageNG">Please provide a valid Console IP,such as:xxx.xxx.xxx.xxx.and require x is a number,and xxx<=255!');
		}    
		else if(!exp.exec(this.value)){
			$("#consoleIpMessage").html('<div class="messageNG">Please provide a valid Console IP,such as:xxx.xxx.xxx.xxx.and require x is a number,and xxx<=255!');
		}
		<#--else{$("#consoleIpMessage").html('OK');}-->
		else{return;}
	});
	
	// Console QueryAllFromIP
	$("#queryAllFromIP").click(function(){
		var consoleIp = $("#consoleIp").val();
		var exp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		if(consoleIp.indexOf('.') < 0){
		    $("#consoleIpMessage").html('<div class="messageNG">Please provide an exist IP');
		    return;
		}    
		else if(!exp.exec(consoleIp)){
			$("#consoleIpMessage").html('<div class="messageNG">Please provide an exist IP');
			return;
		}
		else{
			var array = consoleIp.split('.');
			$.ajax({
				type : 'get',
				url:'${base}/server/returnDataFromIp.do?t=' + new Date().getTime(),
				data:{'ip1':array[0],'ip2':array[1],'ip3':array[2],'ip4':array[3]},
				contentType: "application/json; charset=utf-8",
				dataType:"json",
				success: function(result) {
				   var json = eval(result); //set result to jsonArray
				   var In;  
					if(json==null){
						   alert("Cannot get any data from this iLo, please check your iLo IP!");
						   $("#serverName").val("") ;
		                   $("#serverIp").val("") ;
		                   $("#distro").val("") ;
		                   $("#memory").val("") ;
		                   $("#sn").val("") ;
		                   $("#cpu").val("") ;
		                   $("#macAddr").val("") ;
		                   return;
					}else{
	        	       $.each(json, function (index, item) {  
	   	                    //get data by each
		                    In = json[index];  
		                   $("#serverName").val(In.serverName) ;
			                  $("#serverName").trigger("blur");
		                   $("#serverIp").val(In.serverIp) ;
			                  $("#serverIp").trigger("blur");
		                   $("#consoleIp").val(In.consoleIp) ;
		                   	  $("#consoleIp").trigger("blur");
		                   $("#distro").val(In.distro) ;
		                   $("#memory").val(In.memory) ;
		                   $("#sn").val(In.sn) ;
		                   $("#cpu").val(In.cpu) ;
		                   $("#macAddr").val(In.macAddr) ;
	              	  });  
					}
				}
			});
		}
	});
});

function checkInput() {
	var serverNameMessage = $("#serverNameMessage").html();
	if('OK' != serverNameMessage){
		alert('There is some error on Server Name, please check it!');
		return;
	}
	
	$("#project").trigger("blur");
	var projectMessage = $("#projectMessage").html();
	if('OK' != projectMessage){
		alert('There is some error on project, please check it!');
		return;
	}
	
	$("#group").trigger("blur");
	var groupMessage = $("#groupMessage").html();
	if('OK' != groupMessage){
		alert('There is some error on group, please check it!');
		return;
	}
	
	$("#serverType").trigger("blur");
	var serverTypeMessage = $("#serverTypeMessage").html();
	if('OK' != serverTypeMessage){
		alert('There is some error on sever type, please check it!');
		return;
	}
	var serverIpMessage = $("#serverIpMessage").html();
	if('OK' != serverIpMessage){
		alert('There is some error on Server IP, please check it!').html();
		return;
	}		
	
	save();
}


function save(){  
    document.getElementById("server.keyServer").value = "0";	
	if(document.getElementById("server.keyServer").checked){	   
	    document.getElementById("server.keyServer").value = "1";	    
	}  
   doFormSubmit('serverRegForm', 'doServerSave.do');   
}
function cancel() {
   doPageRedirect('serverList.do')
}
</script>

<body>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Server Register</div>
	</td><td align="right">
	</td></tr>
</table>
<form id="serverRegForm" name="serverRegForm" method="post" action="doServerSave.do">
<div class="box">
	<table class="tablecontent" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Server Name：</td>
			<td align="left" width="90%">
				<input id="serverName" name="server.serverName" class="input_text" type="text" value="${server.serverName!""}"/><span id="serverNameMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Team：</td>
			<td align="left" width="90%">
				<select id="group" name="server.groupId" class="select"  style="width:180px">
			        		<option value="" selected>Please Select</option>
			        		<#if groupList?has_content >
					        	<#list groupList as group>
					             	 <option value="${group.groupId}" <#if '${group.groupId!""}'=='${server.groupId!""}'>selected</#if>>${group.groupName}</option>
					          	</#list> 
					         </#if>
			     </select>
			     <span id="groupMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Project：</td>
			<td align="left" width="90%">
				<select id="project" name="server.projectId" class="select"  style="width:180px">
			        		<option value="" selected>Please Select</option>
			        		<#if projectList?has_content >
					        	<#list projectList as project>
					             	 <option value="${project.projectId}" <#if '${project.projectId!""}'=='${server.projectId!""}'>selected</#if>>${project.projectName}</option>
					          	</#list> 
					        </#if> 
			    </select>
			    <span id="projectMessage"></span>
			</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Server Type：</td>
			<td align="left" width="90%">
				<select id="serverType" name="server.serverType" class="select" style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#if enumList?has_content >
					        	<#list enumList as enum>
					             	 <option value="${enum.enumValue}" <#if '${server.serverType!""}'=='${enum.enumValue!""}'>selected</#if>>${enum.enumValue}</option>
					          	</#list> 
					         </#if>  
			    </select>
			    <span id="serverTypeMessage"></span>
			</td>
		</tr>
		<tr>
	       <td align="right" class="tablecontent_title" width="10%">Server Status:</td>
	       <td align="left" width="90%">
	        <select id="status" name="server.status" class="select"  style="width:180px">
        		<option value="used" selected>used</option>
        		<option value="not used" selected>not used</option>
		    </select>
		    <span id="statusMessage"></span>
	       </td>
        </tr>
		<tr>
	       <td align="right" class="tablecontent_title" width="10%">Hastab:</td>
	       <td align="left" width="90%">
	        <select id="hastab" name="server.hastab" class="select"  style="width:180px">
        		<option value="1" >have</option>
        		<option value="0" >not have</option>
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
			<td class="tablecontent_title" align="right" width="10%">UserName：</td>
			<td align="left" width="90%">
				<input id="serverIp" name="server.serverAccount" class="input_text" type="text" value="${server.serverAccount!""}"/>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Password：</td>
			<td align="left" width="90%">
				<input id="serverIp" name="server.serverPasswd" class="input_text" type="password" value="${server.serverPasswd!""}"/>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>MP/ilo IP：</td>
			<td align="left" width="90%">
				<input id="consoleIp" name="server.consoleIp" class="input_text" type="text" value="${server.consoleIp!""}"/><span id="consoleIpMessage"></span>
				<input name = "queryAllFromIP" id = "queryAllFromIP" type="button" value="Search"/>
			</td>
		</tr>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Operation System:</td>
			<td align="left" width="90%">
				<input id="distro" name="server.distro" class="input_text" type="text" value="${server.distro!""}"/>
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
				<input id="sn" name="server.sn" class="input_text" type="text" value="${server.sn!""}"/>
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
				<input id="macAddr" name="server.macAddr" class="input_text" type="text" value="${server.macAddr!""}"/>
			</td>
		</tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Memory：</td>
			<td align="left" width="90%">
				<input id="memory" name="server.memory" class="input_text" type="text" value="${server.memory!""}"/>
			</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">CPU：</td>
			<td align="left" width="90%">
				<input id="cpu" name="server.cpu" class="input_text" type="text" value="${server.cpu!""}"/>
			</td>
		</tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Key Server：</td>
			<td align="left" width="90%">
				<input type="checkbox" id="server.keyServer"  <#if 'RESERVED'=='${server.serverState!""}'||'TAKEOVER'=='${server.serverState!""}'> disabled="disabled" </#if> name="server.keyServer"  <#if '${server.keyServer!""}'=='1'>checked='true'</#if> value="${(server.keyServer)!''}"/>
			</td>
		</tr>	
		
		</tr>
	       <td align="right" class="tablecontent_title" width="10%">Purchase Date:</td>
	       <td align="left" width="90%"><input id="purchaseDate" name="server.purchaseDate" type="text" class="mh_date" value="${server.purchaseDate!""}" readonly="true"/></td>
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
	    <button class="c" type="button" onclick="checkInput()" >Register</button>
	    <button class="c" type="button" onclick="cancel()" >Cancel</button>

	</td></tr>
</table>
</div>
</form>
</body>
</html>
