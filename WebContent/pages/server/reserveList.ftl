<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>


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

function doRelease(id){
	var type = $("input[name='showType']:checked").val();
	if(type=='SERVERRES'){
	   if(confirm('Are you sure to release this server?')){
	      doPageRedirect('releaseServer.do?reserveId='+id);
	   }
   	}else if(type=='CARDRES'){
   	   if(confirm('Are you sure to release this card?')){
	      doPageRedirect('releaseCard.do?reserveId='+id);
	   }
   	}
}

function doUpdate(id){
	var type = $("input[name='showType']:checked").val();
    doPageRedirect('goReserveUpdate.do?reserveId='+id+'&showType='+type);
}

function goView(id){
    doPageRedirect('goReserveView.do?reserveId='+id+'&showType='+"${showType}");

}

function doReturn(id){
	var type = $("input[name='showType']:checked").val();
	if(type=='SERVERRES'){
		if(confirm('Are you sure to return this server?')){
	    	doPageRedirect('returnServer.do?reserveId='+id);
	    }
	}else if(type=='CARDRES'){
		if(confirm('Are you sure to return this card?')){
	    	doPageRedirect('returnCard.do?reserveId='+id+'&showType='+type);
	    }
	}
   
}

function changeType(){
	var type = $("input[name='showType']:checked").val();
	doPageRedirect('reserveList.do?showType='+type);
}
</script>
</head>

<body>
<form id="reserveListForm" action="" method="POST">
<#if navigation?has_content>
	${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">My Reservations</div>
	</td><td align="right">
		<div class="table_link"></div>
	</td></tr>
</table>
<table width="100%" border="0">
	<tr>
		<td align="left">
			<div class="title">
			 <h4>
				<a herf="###" onclick="changeType()"><input	type="radio" name="showType" value="SERVERRES" checked <#if '${showType!""}'=='SERVERRES'>checked</#if>/>Server Reservation</a>
				<a herf="###" onclick="changeType()"><input	type="radio" name="showType" value="CARDRES" <#if '${showType!""}'=='CARDRES'>checked</#if>/>Card Reservation</a>
		     <h4>
			</div>
		</td>
	</tr>
</table>
<div class="box">
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <!--<th width="2%">
					<input type="checkbox" id="allId" onclick="doAll(this ,'serverIdAry')" />
				</th>-->
		        <th width="10%"><#if '${showType!""}'=='SERVERRES'>Server<#else>Card</#if></th>
		        <th width="10%">Reserve Type</th>
		        <th width="10%">Status</th>
		        <th width="10%">Start Date</th>
		        <th width="10%">End Date</th>
		        <th width="20%">Reserve Note</th>
		        <th width="10%">Action</th>
		</thead>		
		<tbody>	
         <#if reserveVoList?has_content >
     	 <#list reserveVoList as reserve>
			<tr class="mousechange">
				<!--<td align="center"><input type="checkbox" id="reserveIdAry" name="reserveIdAry"  value="${(reserve.serverName)!''}"/></td>-->
				<td align="center"><a href="#" onclick="goView('${reserve.reserveId}')">${(reserve.serverName)!''}</td>	
				
				<td align="center">
					<#if 'RESERVED'=='${(reserve.type)!""}'><div><table><tr><td align="center" bgcolor="#FF0000"><B><font color="white">${(reserve.type)!""}</font></B></td></tr></table></div></#if>
					<#if 'FREE'=='${(reserve.type)!""}'><div><table><tr><td align="center" bgcolor="green"><B><font color="white">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${(reserve.type)!""}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></B></td></tr></table></div></#if>
					<#if 'TAKEOVER'=='${(reserve.type)!""}'><div><table><tr><td align="center" bgcolor="gray"><B><font color="white">${(reserve.type)!""}</font></B></td></tr></table></div></#if>
				</td>
				<td align="center">${(reserve.status)!''}</td>
				<td align="center">${(reserve.startDate)!''}</td>
				<td align="center">${(reserve.endDate)!''}</td>
				<td align="center">${(reserve.remark)!''}</td>
				<td align="center">
				<div class="table_link">
				  <#if 'RESERVED'=='${reserve.type!""}'>
				    <a class="tooltips" href="#"  onclick="doUpdate('${reserve.reserveId}')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;Upadte&nbsp;&nbsp;</span> </a>&nbsp;&nbsp;&nbsp;
				    <a class="tooltips" href="#"  onclick="doRelease('${reserve.reserveId}')"><img src="${base}/images/icon/release.png" /><span>&nbsp;&nbsp;Release&nbsp;&nbsp;</span> </a>&nbsp;&nbsp;&nbsp;				    
				  </#if>
				  <#if 'TAKEOVER'=='${reserve.type!""}'>
				    <li><a href="#" onclick="doReturn('${reserve.reserveId}')">Return</a></li>
				  </#if>
				  				   
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
