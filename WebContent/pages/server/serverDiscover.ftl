<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
$(document).ready(function(){
    $(document).keydown(function(){ 
		if(event.keyCode == 13) 
			$("#filter").click();}
	);
});

function check(){
	var ip1 = $("#ip11").val()+'.'+$("#ip12").val()+'.'+$("#ip13").val()+'.'+$("#ip14").val();
	var ip2 = $("#ip21").val()+'.'+$("#ip22").val()+'.'+$("#ip23").val()+'.'+$("#ip24").val();
	var exp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	if(ip1.indexOf('.') < 0 && ip2.indexOf('.') < 0 && !exp.exec(ip1) && !exp.exec(ip2) && compareIP(ip1,ip2)==false){
	    alert("Please provide a valid Console IP,such as:xxx.xxx.xxx.xxx.and require x is a number,and xxx<=255, From IP <= To IP !");
	    return false;
	}else{
		window.setTimeout(function () { jBox.tip('Searching...', 'loading'); }, 0);
		return true;
	}
}

function compareIP(ipBegin, ipEnd)  
{  
    var temp1;  
    var temp2;    
    temp1 = ipBegin.split("\.");
    temp2 = ipEnd.split("\.");
    for (var i = 0; i < 4; i++)  
    {  
    	var temp11 = parseInt(temp1[i]);
    	var temp22 = parseInt(temp2[i]);
        if (temp11>temp22)  
        {  
            return false;  
        }  
        else
        {  
            return true;  
        }  
    }  
    return false;     
}  

function doInsertDatabase(){
	var strJson = createJson();
	$.ajax( {
		type : 'get',
		beforeSend:function(){jBox.tip("InsertDatabase...", 'loading');},
		url : '${base}/server/insertOrUpdateDatabase.do?t='+new Date().getTime(),
	    contentType: "application/json; charset=utf-8",
		dataType:"json",
		data : {
			'strJson' : strJson
		},
		success : function(msg) {
			window.setTimeout(function () { jBox.tip('InsertDatabase success!', 'success'); }, 0);
			window.location.href = "${base}/server/serverAdmin.do?t="+new Date().getTime();
		}
	});
}

function verifyCheckbox(){
	var array = new Array();
	var serverName = document.getElementsByName('serverIdAry');
	var groupName = document.getElementsByName('server.groupId');
	var projectId = document.getElementsByName('server.projectId');
	var enumValue = document.getElementsByName('server.serverType');
	for(var i=0;i<serverName.length;i++){
		if(serverName[i].checked){
			array.push(serverName[i].value);	
		}
		if(serverName[i].checked && '' == groupName[i].value){
			alert("Team must be input !");
			return false;
		}	
		if(serverName[i].checked && '' == projectId[i].value){
			alert("Project must be input !");
			return false;
		}	
		if(serverName[i].checked && '' == enumValue[i].value){
			alert("ServerType must be input !");
			return false;
		}	
	}
	if(array.length>0){
		doInsertDatabase();
	}else{
		alert("Please choose checkboxes first !");
		return false;
	}
	
	
}


function createJson(){
	var strJson = {strJSON:[]};
	var serverName = document.getElementsByName('serverIdAry');
	var serverIp = document.getElementsByName('aserverIp');
	var consoleIp = document.getElementsByName('aconsoleIp');
	var distro = document.getElementsByName('adistro');
	var groupName = document.getElementsByName('server.groupId');
	var projectId = document.getElementsByName('server.projectId');
	var enumValue = document.getElementsByName('server.serverType');
	var cpu = document.getElementsByName('acpu');
	var memory = document.getElementsByName('amemory');
	var remark = document.getElementsByName('aremark');
	for(var i=0;i<serverName.length;i++){
		if(serverName[i].checked==true){
			strJson.strJSON.push({
				"serverName":serverName[i].value,
				"serverIp":serverIp[i].value,
				"consoleIp":consoleIp[i].value,
				"distro":distro[i].value,
				"projectId":projectId[i].value,
				"groupName":groupName[i].value,
				"enumValue":enumValue[i].value,
				"cpu":cpu[i].value,
				"memory":memory[i].value,
				"remark":remark[i].value							
			}); 
		}
	}
	return JSON.stringify(strJson);
}

//IP skip when input IP length==3
function maxleng(objec,index){
   if(objec.value.length==3){
       document.forms[0].elements[index+1].focus();
   }
}

</script>
</head>

<body>
<form id="serverListForm" action="" method="POST">
<#if navigation?has_content>
	${navigation}
</#if>
	<table width="100%" border="0">
		<tr>
			<td align="left">
				<div class="title">Server Discover</div>
			</td>
			<td align="right">
				<div class="table_link">
					<ul>		      	
			       	  	<li><a href="#" onclick="doPageRedirect('serverAdmin.do')">Return</a></li>	  	
			       	  	<li><a href="#" onclick="verifyCheckbox()">InsertData</a></li>      
		        	</ul>
		        </div>
			</td>
		</tr>
	</table>
<div class="box">
	<div style="font-weight:bold;">
	<form action="serverDiscover.do" method="post" >
	  <table align="center">
		<tr>
  			<td width="80%">
				Input Console IP From : &nbsp;
  				<input type="text"  onkeyup="maxleng(this,0)"  id="ip11" name="ips.ip11" style="width:30px; text-align:right" value="${(ips.ip11)!''}"/> .
  				<input type="text"  onkeyup="maxleng(this,1)" id="ip12" name="ips.ip12" style="width:30px; text-align:right" value="${(ips.ip12)!''}"/> .
  				<input type="text"  onkeyup="maxleng(this,2)" id="ip13" name="ips.ip13" style="width:30px; text-align:right" value="${(ips.ip13)!''}"/> .
  				<input type="text"  onkeyup="maxleng(this,3)" id="ip14" name="ips.ip14" style="width:30px; text-align:right" value="${(ips.ip14)!''}"/> - To -
  				<input type="text"  onkeyup="maxleng(this,4)" id="ip21" name="ips.ip21" style="width:30px; text-align:right" value="${(ips.ip21)!''}"/> .
  				<input type="text"  onkeyup="maxleng(this,5)" id="ip22" name="ips.ip22" style="width:30px; text-align:right" value="${(ips.ip22)!''}"/> .
  				<input type="text"  onkeyup="maxleng(this,6)" id="ip23" name="ips.ip23" style="width:30px; text-align:right" value="${(ips.ip23)!''}"/> .
  				<input type="text"  onkeyup="maxleng(this,7)" id="ip24" name="ips.ip24" style="width:30px; text-align:right" value="${(ips.ip24)!''}"/>&nbsp;
  				<input type="submit" value="Search" onClick="return check();"/>
  			</td>
  		</tr>
	  </table>
	</form>
    </div>
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <th width="2%">
					<#--<input type="checkbox" id="allId" onclick="doAll(this ,'serverIdAry')" />-->
				</th>
		        <th width="10%">Name</th>
		        <th width="10%">IP</th>
		        <th width="10%">Console IP</th>
		        <th width="12%">Operation System</th>
		        <th width="10%">Team</th>
		        <th width="10%">Project</th>
		        <th width="10%">ServerType</th>
		        <th width="6%">CPU</th>
		        <th width="10%">Memory</th>
		        <th width="10%">Description</th>
		</thead>	
		<tbody>	
         <#if serverList?has_content >
     	 <#list serverList as server>
			<tr class="mousechange">
				<td align="center"><input type="checkbox" id="serverIdAry" name="serverIdAry"  value="${(server.serverName)!''}"/></td>
				<td align="center" title="${(server.serverName)!''}"><input type="text" value="${server.serverName}" style="width:100px;background:transparent;border:0" disabled="disabled"/></td>
				<td align="center"><input type="text" name="aserverIp" value="${(server.serverIp)!''}" style="width:90px; background:transparent;border:0" disabled="disabled"/></td>
				<td align="center"><input type="text" name="aconsoleIp" value="${(server.consoleIp)!''}" style="width:90px; background:transparent;border:0" disabled="disabled"/></td>
				<td align="center" title="${(server.distro)!''}" style="width:20px"><input type="text" name="adistro" value="${(server.distro)}" style="background:transparent;border:0" disabled="disabled"/></td>
				<td align="center">
					<select id="group" name="server.groupId" class="select"  style="width:140px">
				        		<option value="" selected>Please Select</option>
				        		<#if groupList?has_content >
						        	<#list groupList as group>
						             	 <option value="${group.groupId}" <#if '${group.groupId!""}'=='${server.groupId!""}'>selected</#if>>${(group.groupName)!''}</option>
						          	</#list> 
						         </#if>
				     </select>
				</td>
				<td align="center">
					<select id="project" name="server.projectId" class="select"  style="width:140px">
			        		<option value="" selected>Please Select</option>
			        		<#if projectList?has_content >
					        	<#list projectList as project>
					             	 <option value="${project.projectId}" <#if '${project.projectId!""}'=='${server.projectId!""}'>selected</#if>>${project.projectName}</option>
					          	</#list> 
					        </#if> 
			    	</select>
				</td>
				<td align="center">
					<select id="serverType" name="server.serverType" class="select" style="width:140px">
				        		<option value="" selected>Please Select</option>
					        	<#if enumList?has_content >
						        	<#list enumList as enum>
						             	 <option value="${enum.enumValue}" <#if '${server.serverType!""}'=='${enum.enumValue!""}'>selected</#if>>${(enum.enumValue)!''}</option>
						          	</#list> 
						         </#if>  
				    </select>
				</td>
				<td align="center" title="${(server.cpu)!''}"><input type="text" name="acpu" value="${(server.cpu)!''}" style="background:transparent;border:0" disabled="disabled"/></td>
				<td align="center"><input type="text" name="amemory" value="${(server.memory)!''}" style="width:40px; background:transparent;border:0" disabled="disabled"/></td>
				<td align="center"><input type="text" name="aremark" value="${(server.remark)!''}"/></td>
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
