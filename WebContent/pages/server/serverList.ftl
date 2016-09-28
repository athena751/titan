<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>
<style>
body{ background-color:#FFF}
</style>	
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
	var type = $("input[name='showType']:checked").val();
	if(type=='SERVERRES'){
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
	}else if(type=='CARDRES'){
	    $.ajax( {
			type : 'get',
			url : '${base}/server/checkCardStatus.do?t=' + new Date().getTime(),
			dataType: "text",
			data : { 'cardId' : serverId },
			success : function(msg) {				    
				if (msg == "reserved") {					
				    alert("This card had been reserved, please refresh your card List. ");
				     doPageRedirect('serverList.do?showType='+type);										
				}else{
				    doPageRedirect('goCardReserve.do?cardId='+serverId);
				}
			}
		});
	}
}

function takeOver(serverId){
	var type = $("input[name='showType']:checked").val();
	if(type=='SERVERRES'){
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
	}else if(type=='CARDRES'){
		$.ajax( {
			type : 'get',
			url : '${base}/server/checkCardStatus.do?t=' + new Date().getTime(),
			dataType: "text",
			data : { 'cardId' : serverId },
			success : function(msg) {				    
				if (msg == "takeover") {					
				    alert("This card had been taken over, please refresh your card List. ");
				     doPageRedirect('serverList.do?showType='+type);										
				}else{
				    if(confirm('Are you sure to take over this card?')){
	                    doPageRedirect('goCardTakeOver.do?showType='+type+'&cardId='+serverId);
	                }
				}
		   }
		});
	}
  
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

function advanceSearch(){
   doPageRedirect('goAdvanceSearch.do');
}

function changeType(){
	var type = $("input[name='showType']:checked").val();
	if(type == 'SERVERRES' ){
		$('#serverReservation').show();
		$('#cardReservation').hide();
	}
	else if(type == 'CARDRES' ){
		$('#serverReservation').hide();
		$('#cardReservation').show();
	}
}
</script>
<script type="text/javascript" language="javascript" class="init">
	$(document).ready(function(){
	    $(document).keydown(function(){ 
			if(event.keyCode == 13) 
				$("#filter").click();
		});
		if($('#showType').val() == 'SERVERRES' ){
			$('#serverReservation').show();
			$('#cardReservation').hide();
		}
		else if($('#showType').val() == 'CARDRES' ){
			$('#serverReservation').hide();
			$('#cardReservation').show();
		}
		$('#pageTableServer').DataTable( {
			dom: 'lfrtip',
			tableTools: {
	            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
	        }
		});
		$('#pageTableCard').DataTable( {
			dom: 'lfrtip',
			tableTools: {
	            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
	        }
		});
	});
</script>
</head>

<body>
<form id="serverListForm" action="" method="POST">
<#if navigation?has_content>
	${navigation}
</#if>
<input type="hidden" id="showType" value="${(showType)!''}" />
<div class="title">
	Reservations
</div>
<table width="100%" border="0">
	<tr>
		<td align="left">
			<div class="title">
			 <h4>
				<a herf="###" onclick="changeType()"><input	type="radio" name="showType" value="SERVERRES" checked <#if '${showType!""}'=='SERVERRES'>checked</#if>/>Server Reservation</a>
				<a class="tooltips" href="#" onclick="advanceSearch()"><img src="${base}/images/icon/magnifier.gif" /><span>Advance&nbsp;&nbsp;Search</span></a>&nbsp;&nbsp;
				<a herf="###" onclick="changeType()"><input	type="radio" name="showType" value="CARDRES" <#if '${showType!""}'=='CARDRES'>checked</#if>/>Card Reservation</a>
				<#--<a class="tooltips" href="#" onclick="advanceSearch()"><img src="${base}/images/icon/magnifier.gif" /><span>Advance&nbsp;&nbsp;Search</span></a>-->
		     <h4>
			</div>
		</td>
	</tr>
</table>
<div class="box">
<table width="100%" border="0">
 <tr>
	<td width="70%" valign="top">
     <div id="serverReservation" style="display: none;">
      <table class="display" id="pageTableServer" width="100%">
	          <thead>
			        <th width="2%">
						<!--<input type="checkbox" id="allId" onclick="doAll(this ,'serverIdAry')" />-->
					</th>
			        <th width="10%">Name</th>
			        <th width="10%">IP</th>
			        <th width="10%">Operation System</th>
			        <th width="10%">Status</th>
			        <th width="10%">Owner</th>
			        <th width="10%">Project</th>
			        <th width="20%">Reserve Note</th>
			        <th width="10%">Action</th>
			</thead>	
			<tbody>	
	         <#if serverVoList?has_content >
	     	 <#list serverVoList as server>
				<tr class="mousechange">
					<td align="center"><input type="checkbox" id="serverIdAry" name="serverIdAry"  value="${(server.serverId)!''}"/></td>
					<td align="center"><a href="#" onclick="doPageRedirect('goServerView.do?serverId=${(server.serverId)}')">${(server.serverName)!''}</td>
					<td align="center">${(server.serverIp)!''}</td>
					<td align="center">${(server.distro)!''}</td>
					<td align="center">
					<#if 'RESERVED'=='${server.serverStatus!""}'><div><table><tr><td align="center" bgcolor="#FF0000"><B><font color="white">${(server.serverStatus)!''}</font></B></td></tr></table></div></#if>
					<#if 'FREE'=='${server.serverStatus!""}'><div><table><tr><td align="center" bgcolor="green"><B><font color="white">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${(server.serverStatus)!''}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></B></td></tr></table></div></#if>
					<#if 'TAKEOVER'=='${server.serverStatus!""}'><div><table><tr><td align="center" bgcolor="gray"><B><font color="white">${(server.serverStatus)!''}</font></B></td></tr></table></div></#if>
					</td>
					<td align="center">		
					${(server.owner)!''}</td>
					<td align="center">${(server.project)!''}</td>
					<td align="center">${(server.resNote)!''}</td>
					<td align="center">
						<div class="table_link">
						    <#if 'FREE'=='${server.serverStatus!""}'>			  
						    <a class="tooltips" href="#" onclick="doReserve('${server.serverId}')"><img src="${base}/images/icon/time.gif" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Reserve&nbsp;&nbsp;&nbsp;&nbsp;</span></a> &nbsp;&nbsp;&nbsp;&nbsp; 
						    </#if>
						    <#if 'RESERVED'=='${server.serverStatus!""}'&&'${server.owner!""}'!='${currentUserName!""}'>
						    <a class="tooltips" href="#" onclick="takeOver('${server.serverId}')"><img src="${base}/images/icon1/server_manage.gif" /><span>Take&nbsp;&nbsp;&nbsp;&nbsp;Over</span></a> &nbsp;&nbsp;&nbsp;&nbsp; 
						    </#if>
						    <#if '${server.owner!""}'=='${currentUserName!""}'||'${isAdmin!""}'=='1'>				    
						    <a class="tooltips" href="#" onclick="doPageRedirect('goServerUpdate.do?serverId=${(server.serverId)}')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;&nbsp;</span></a>&nbsp;&nbsp;&nbsp;&nbsp;
						    </#if>
						</div>
					</td>
				</tr>
				 </#list>
	 		 </#if>
	 		 </tbody>   
       </table>     	
     </div>   
     <div id="cardReservation" style="display: none;">
      <div class="clear"></div>
      <table class="display" id="pageTableCard" width="100%">
	        <thead>
			        <th width="2%">
						<!--<input type="checkbox" id="allId" onclick="doAll(this ,'serverIdAry')" />-->
					</th>
			        <th width="10%">Name</th>
			        <th width="10%">Type</th>
			        <th width="10%">SN</th>
			        <th width="10%">PN</th>
			        <th width="10%">Status</th>
			        <th width="10%">Vendor</th>
			        <th width="10%">Project</th>
			        <th width="10%">Description</th>
			        <th width="10%">Purchase Date</th>
			        <th width="10%">Action</th>
			</thead>	
			<tbody>	
	         <#if cardVoList?has_content >
	     	 <#list cardVoList as card>
				<tr class="mousechange">
					<td align="center"><input type="checkbox" id="cardIdAry" name="cardIdAry"  value="${(card.cardId)!''}"/></td>
					<td align="center"><a href="#" onclick="doPageRedirect('goCardViewDetail.do?cardId=${(card.cardId)}')">${(card.cardName)!''}</td>
					<#--<td align="center">${(card.cardName)!''}</td>-->
					<td align="center">${(card.cardType)!''}</td>
					<td align="center">${(card.cardSn)!''}</td>
					<td align="center">${(card.cardPn)!''}</td>
					<td align="center">
						<#if 'RESERVED'=='${(card.cardState)!""}'><div><table><tr><td align="center" bgcolor="#FF0000"><B><font color="white">${(card.cardState)!""}</font></B></td></tr></table></div></#if>
						<#if 'FREE'=='${(card.cardState)!""}'><div><table><tr><td align="center" bgcolor="green"><B><font color="white">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${(card.cardState)!""}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></B></td></tr></table></div></#if>
						<#if 'TAKEOVER'=='${(card.cardState)!""}'><div><table><tr><td align="center" bgcolor="gray"><B><font color="white">${(card.cardState)!""}</font></B></td></tr></table></div></#if>
					</td>
					<td align="center">${(card.vendor)!''}</td>
					<td align="center">${(card.project)!''}</td>
					<td align="center">${(card.description)!''}</td>
					<td align="center">${(card.purchaseDate)!''}</td>
					<td align="center">
						<div class="table_link">
						    <#if 'FREE'=='${(card.cardState)!""}'>			  
						    	<a class="tooltips" href="#" onclick="doReserve('${card.cardId}')"><img src="${base}/images/icon/time.gif" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Reserve&nbsp;&nbsp;&nbsp;&nbsp;</span></a> &nbsp;&nbsp;&nbsp;&nbsp; 
						    </#if>
						    <#--<#if 'RESERVED'=='${(card.cardState)!""}'&&'${server.owner!""}'!='${currentUserName!""}'>-->
						    <#if 'RESERVED'=='${(card.cardState)!""}'&&'${(card.ownerId)!""}'!='${currentUserId!""}'>
						    	<a class="tooltips" href="#" onclick="takeOver('${card.cardId}')"><img src="${base}/images/icon1/server_manage.gif" /><span>Take&nbsp;&nbsp;&nbsp;&nbsp;Over</span></a> &nbsp;&nbsp;&nbsp;&nbsp; 
						    </#if>
						    <#if '${card.ownerId!""}'=='${currentUserId!""}'||'${isAdmin!""}'=='1'>				    
						    	<a class="tooltips" href="#" onclick="doPageRedirect('goCardUpdate.do?cardId=${(card.cardId)!""}&showType=CARDRES')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;&nbsp;</span></a>&nbsp;&nbsp;&nbsp;&nbsp;
						    </#if>
						</div>
					</td>
				</tr>
			 </#list>
	 		 </#if>
	 		</tbody>   
       </table>     	
     </div>
	</td>
  </tr>
 </table>
</div>
</form>
</body>
</html>
