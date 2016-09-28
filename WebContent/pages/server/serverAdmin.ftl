<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
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
$(document).ready(function(){
  $(document).keydown(function(){ 
		if(event.keyCode == 13) 
			$("#filter").click();}
	);
});
function doRefresh(){
	$.ajax( {
		type : 'get',
		beforeSend:function(){jBox.tip("Refreshing...", 'loading');},
		url : '${base}/server/refreshFromiloIP.do?t=' + new Date().getTime(),
	    contentType: "application/json; charset=utf-8",
		dataType:"json",
		success : function(msg) {
			window.setTimeout(function () { jBox.tip('Refresh success!', 'success'); }, 0);
			window.location.href = "${base}/server/serverAdmin.do";
		}
	});
}
</script>
</head>

<body>
<form id="serverListForm" action="" method="POST">
<#if navigation?has_content>
	${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Server Management&nbsp;&nbsp;&nbsp;&nbsp;</div>
	</td><td align="right">
		<div class="table_link">		      	
       	  	<#--<li><a href="#" onclick="doPageRedirect('serverAdmin.do')">Refresh</a></li>-->
       	  	<a class="tooltips" href="#" onclick="doRefresh()"><img src="${base}/images/icon/Refresh.png" /><span>&nbsp;&nbsp;Refresh&nbsp;&nbsp;</span> </a>&nbsp;&nbsp;&nbsp;&nbsp;     	
       	  	<#--<li><input type="" onclick="doRefresh()" value="Refresh"/></li>-->        	
			<a class="tooltips" href="#" onclick="doPageRedirect('goServerReg.do')"><img src="${base}/images/icon/server-regist.png" /><span>&nbsp;&nbsp;Register&nbsp;&nbsp;</span> </a>&nbsp;&nbsp;&nbsp;&nbsp;     	
        	<a class="tooltips" href="#" onclick="doPageRedirect('serverDiscover.do')"><img src="${base}/images/icon/discover.png" /><span>&nbsp;&nbsp;Discover&nbsp;&nbsp;</span> </a>     	
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
	</td></tr>
</table>
<div class="box">
      <table class="display" id="pageTable" width="100%">
          <thead>
		       <!-- <th width="2%">
					<input type="checkbox" id="allId" onclick="doAll(this ,'serverIdAry')" />
				</th>-->
		        <th width="8%">Name</th>
		        <th width="7%">IP</th>
		        <th width="10%">ConsoleIP</th>
		        <th width="10%">Operation System</th>
		        <th width="7%">Status</th>
		        <th width="10%">Owner</th>
		        <th width="10%">Project</th>
		        <th width="10%">Cpu</th>
		        <th width="10%">Memory</th>
		        <th width="10%">Hastab</th>
		        <th width="10%">Purchase Date</th>
		        <th width="20%">Description</th>
		        <th width="15%">Action</th>
		</thead>	
		<tbody>	
         <#if serverVoList?has_content >
     	 <#list serverVoList as server>
			<tr class="mousechange">
				<!--<td align="center"><input type="checkbox" id="serverIdAry" name="serverIdAry"  value="${(server.serverId)!''}"/></td>-->
				<td align="center"><a href="#" onclick="doPageRedirect('goServerView.do?serverId=${(server.serverId)}')">${(server.serverName)!''}</td>
				<td align="center">${(server.serverIp)!''}</td>
				<td align="center">${(server.consoleIp)!''}</td>
				<td align="center">${(server.distro)!''}</td>
				<td align="center">
				<div align="center"
				<#if 'RESERVED'=='${server.serverStatus!""}'>style="width:90%;height:100%;background-color:#FF0000;"</#if>
				<#if 'FREE'=='${server.serverStatus!""}'>style="width:90%;height:100%;background-color:green;"</#if>
				<#if 'TAKEOVER'=='${server.serverStatus!""}'>style="width:90%;height:100%;background-color:gray;"</#if>
				<#if 'KEYSERVER'=='${server.serverStatus!""}'>style="width:90%;height:100%;background-color:lightgray;"</#if>
				>
				<B><font color="white">${(server.serverStatus)!''}</font></B>
				</div>
				</td>
				<td align="center">		
				${(server.owner)!''}</td>
				<td align="center">${(server.project)!''}</td>
				<td align="center">${(server.cpu)!''}</td>
				<td align="center">${(server.memory)!''}</td>
				<td align="center">${(server.hastab)!''}</td>
				<td align="center">${(server.purchaseDate)!''}</td>
				<td align="center">${(server.remark)!''}</td>
				<td align="center">
				<div class="table_link">			    
				    <a class="tooltips" href="#" onclick="doPageRedirect('goServerUpdateAll.do?serverId=${(server.serverId)}')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;&nbsp;</span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
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
