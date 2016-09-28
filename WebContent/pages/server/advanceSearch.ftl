<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
function doDisable(id){
   if(confirm('Are you sure to delete this server?')){
       doPageRedirect('goServerDisable.do?serverId='+id);
   }
	
}

function doUpdate(){
	if(checkBox('update','serverIdAry')){
		doFormSubmit('serverListForm','goServerUpdate.do')
	}
}

function doReserve(serverId){   
    $.ajax( {
				type : 'get',
				url : '${base}/server/checkServerStatus.do?t=' + new Date().getTime(),
				dataType: "text",
				data : { 'serverId' : serverId },
				success : function(msg) {				    
					if (msg == "reserved") {					
					    alert("This server had been reserved, please refresh your server List. ");
					     doPageRedirect('serverList.do');										
					}else{
					    doPageRedirect('goServerReserve.do?serverId='+serverId);
					}
				}
			});
  
}

function checkBox(type,name){
	var serverIds = document.getElementsByName(name);
	var count = 0;
	if(serverIds != null && serverIds.length > 0){
     for(var i = 0 ; serverIds.length > i; i++){
        if(serverIds[i].checked){
	    	count = count + 1;
	   	}
     }
	}
	 if(count == 0){
		 alert('Please select at least one!');
		 return false;
	 }else if (type=='update' && count > 1){
	 	 alert('Please choose only one!');
	 	 return false;
	 }else{
	 	 return true;
	 }
}

function onSearch(){
   doFormSubmit('serverListForm','serverList.do');
}

function doAdvanceSearch(){

   doFormSubmit('serverListForm','doAdvanceSearch.do');
}

function takeOver(serverId){

    $.ajax( {
				type : 'get',
				url : '${base}/server/checkServerStatus.do?t=' + new Date().getTime(),
				dataType: "text",
				data : { 'serverId' : serverId },
				success : function(msg) {				    
					if (msg == "takeover") {					
					    alert("This server had been taken over, please refresh your server List. ");
					     doPageRedirect('serverList.do');										
					}else{
					    if(confirm('Are you sure to take over this server?')){
                            doPageRedirect('goServerTakeOver.do?serverId='+serverId);
                        }
					}
				}
			});
  
}
</script>
</head>

<body>
<form id="serverListForm" action="" method="POST">
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Advance Search</div>
	</td><td align="right">
		
	</td></tr>
</table>

<div class="box">
	<table class="tablecontent" width="60%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Server Name：</td>
			<td width="10%"><input type="text" id="serverVo.serverName" name="serverVo.serverName"  value="${(serverVo.serverName)!''}"/></td>
			<td class="tablecontent_title" align="right" width="10%">Server IP：</td>
			<td width="10%"><input type="text" id="serverVo.serverIp" name="serverVo.serverIp"  value="${(serverVo.serverIp)!''}"/></td>
		</tr>		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Project：</td>
			<td width="10%">
				<select class="select" id="serverVo.projectId" name="serverVo.projectId">
					<option value=""></option>
					<#if projectList?has_content>
			        	<#list projectList as project>
			            <option value="${project.projectId}" <#if '${project.projectId!""}'=='${serverVo.projectId!""}'>selected</#if>>${project.projectName}</option>
			          	</#list>
		          	</#if>
				</select>
			</td>
			<td class="tablecontent_title" align="right" width="10%">Owner：</td>
			<td width="10%"><select class="select" id="serverVo.ownerId" name="serverVo.ownerId">
					<option value=""></option>
		        	<#list userList as user>
		            <option value="${user.userId}" <#if '${user.userId!""}'=='${serverVo.ownerId!""}'>selected</#if>>${user.userCode}</option>
		          	</#list>
				</select></td>			
		</tr>		
		<tr>
			<td class="tablecontent_title" align="right">Operation System：</td>
			<td ><input type="text" id="serverVo.distro" name="serverVo.distro"  value="${(serverVo.distro)!''}"/></td>
			<td class="tablecontent_title"  align="right">Status：</td>
			<td ><select class="select" id="serverVo.serverStatus" name="serverVo.serverStatus">
					<option value=""></option>
					<option value="FREE" <#if 'FREE'=='${serverVo.serverStatus!""}'>selected</#if>>FREE</option>					
					<option value="RESERVED" <#if 'RESERVED'=='${serverVo.serverStatus!""}'>selected</#if>>RESERVED</option>
					<option value="TAKEOVER" <#if 'TAKEOVER'=='${serverVo.serverStatus!""}'>selected</#if>>TAKEOVER</option>
					<option value="INVALID" <#if 'INVALID'=='${serverVo.serverStatus!""}'>selected</#if>>INVALID</option>
				</select>
			</td>
		</tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Location：</td>
			<td><input type="text" id="serverVo.location" name="serverVo.location"  value="${(serverVo.location)!''}"/></td>
			<td class="tablecontent_title" align="right">Serial Number：</td>
			<td><input type="text" id="serverVo.sn" name="serverVo.sn"  value="${(serverVo.sn)!''}"/></td>
			
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Mac Address：</td>
			<td><input type="text" id="serverVo.macAddr" name="serverVo.macAddr"  value="${(serverVo.macAddr)!''}"/></td>
			<td class="tablecontent_title" align="right" >Server Type：</td>
			<td><input type="text" id="serverVo.serverType" name="serverVo.serverType"  value="${(serverVo.serverType)!''}"/></td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Memory：</td>
			<td><input type="text" id="serverVo.memory" name="serverVo.memory"  value="${(serverVo.memory)!''}"/></td>
			<td class="tablecontent_title" align="right" >CPU：</td>
			<td><input type="text" id="serverVo.cpu" name="serverVo.cpu"  value="${(serverVo.cpu)!''}"/></td>
		</tr>	
		</table>
</div>

<table width="60%" border="0">
	<tr><td align="left">
		
	</td><td align="right">
		<div class="table_link"><ul>		    
          	<li><a href="#" onclick="doAdvanceSearch()">Search</a></li>         	
        </ul></div>
	</td></tr>
</table>
<div class="box">
      <table class="tlist" id="pageTable" width="100%">
          <thead>
		        <th width="2%">
					<!--<input type="checkbox" id="allId" onclick="doAll(this ,'serverIdAry')" />-->
				</th>
		        <th width="10%">Name</th>
		        <th width="10%">IP</th>
		        <th width="10%">Operation System</th>
		        <th width="10%">Status</th>
		        <th width="10%">Server Type</th>
		        <th width="10%">CPU</th>
		        <th width="10%">Memory</th>
		        <th width="10%">Location</th>      
		 <!--       <th width="10%">Action</th>                --!>
		</thead>	
		
		<tbody>	
         <#if serverVoList?has_content >
     	 <#list serverVoList as server>
			<tr class="mousechange">
				<td align="center"></td>
				<td align="center"><a href="#" onclick="doPageRedirect('goServerViewFromSearch.do?serverId=${(server.serverId)}')">${(server.serverName)!''}</td>
				<td align="center">${(server.serverIp)!''}</td>
				<td align="center">${(server.distro)!''}</td>
				<td align="center">
				<#if 'RESERVED'=='${server.serverStatus!""}'><div><table><tr><td align="center" bgcolor="#FF0000"><B><font color="white">${(server.serverStatus)!''}</font></B></td></tr></table></div></#if>
				<#if 'FREE'=='${server.serverStatus!""}'><div><table><tr><td align="center" bgcolor="green"><B><font color="white">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${(server.serverStatus)!''}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></B></td></tr></table></div></#if>
				<#if 'TAKEOVER'=='${server.serverStatus!""}'><div><table><tr><td align="center" bgcolor="gray"><B><font color="white">${(server.serverStatus)!''}</font></B></td></tr></table></div></#if>
				</td>
				<td align="center">${(server.serverType)!''}</td>
				<td align="center">${(server.cpu)!''}</td>
				<td align="center">${(server.memory)!''}</td>
				<td align="center">${(server.location)!''}</td>
<!--		 	<td align="right">        --!> 
<!--				    <div class="table_link"><ul>    --!>
<!--				    <#if 'FREE'=='${server.serverStatus!""}'>    --!>
<!--				    <li><a href="#" onclick="doReserve('${server.serverId}')">Reserve</a></li>    --!>
<!--				    </#if>    --!>
<!--				    <#if 'RESERVED'=='${server.serverStatus!""}'&&'${server.owner!""}'!='${currentUserName!""}'>    --!>
<!--				    <li><a href="#" onclick="takeOver('${server.serverId}')">Take Over</a></li>     --!>
<!--				    </#if>    --!>
<!--				    <#if '${server.owner!""}'=='${currentUserName!""}'||'${isAdmin!""}'=='1'>	 --!>			    
<!--				    <li><a href="#" onclick="doPageRedirect('goServerUpdate.do?serverId=${(server.serverId)}')">Edit</a></li>    --!>
<!--				    </#if>   --!>
<!--				 </ul></div>   --!>
<!--				</td>    --!>
			</tr>
			 </#list>
 		 </#if>
 		 </tbody>      
        </table>     	
	</div>
</div>
</form>
</body>
</html>
