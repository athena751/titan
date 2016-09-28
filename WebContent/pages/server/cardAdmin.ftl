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
</script>
</head>

<body>
<form id="cardListForm" action="" method="POST">
<#if navigation?has_content>
	${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Card Management&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doPageRedirect('goCardReg.do')">
		<img src="${base}/images/icon/11.png" /><span>&nbsp;&nbsp;Register&nbsp;&nbsp;</span> </a></div>
	</td><!--<td align="right">
		<div class="table_link"><ul>		      	
          	<li><a href="#" onclick="doPageRedirect('goCardReg.do')">Register</a></li>
        </ul></div>-->
	</td></tr>
</table>
<div class="box">
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <th width="30%">Name</th>
		        <th width="10%">type</th>
		        <th width="10%">Server</th>
		        <th width="10%">SN</th>
		        <th width="10%">PN</th>
		        <th width="10%">Vendor</th>
		        <th width="10%">Project</th>
		        <th width="15%">Description</th>
		        <th width="5%">Action</th>
		</thead>	
		<tbody>	
         <#if cardVoList?has_content >
     	 <#list cardVoList as card>
			<tr class="mousechange">			
				<td align="left"><a href="#" onclick="doPageRedirect('goCardView.do?cardId=${(card.cardId)}')">${(card.cardName)!''}</td>
				<td align="center">${(card.cardType)!''}</td>
				<td align="center">${(card.serverName)!''}</td>
				<td align="center">${(card.cardSn)!''}</td>
				<td align="center">${(card.cardPn)!''}</td>
				<td align="center">${(card.vendor)!''}</td>
				<td align="center">${(card.project)!''}</td>
				<td align="center">${(card.description)!''}</td>
				<td align="right">
				<div class="table_link">				    
				 <!--   <li><a href="#" onclick="doDisable('${(card.cardId)}')">Delete</a></li>  !-->
				    <a class="tooltips" href="#" onclick="doPageRedirect('goCardUpdate.do?cardId=${(card.cardId)}')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;</span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
