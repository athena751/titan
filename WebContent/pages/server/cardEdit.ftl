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
   doFormSubmit('cardUpdateForm', 'doCardUpdate.do?showType='+"${showType!""}");   
}
function cancel() {
	if('${showType!""}'=='SERVERRES' || '${showType!""}'=='CARDRES'){
		doPageRedirect('serverList.do?showType='+"${showType!""}");
	}else{
		doPageRedirect('cardList.do');
	}
}
</script>

<body>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title"> <#if 'edit'=='${type!""}'>Card Update</#if></div>
		<div class="title"> <#if 'view'=='${type!""}'>Card Details</#if></div>
	</td><td align="right">
	</td></tr>
</table>

<form id="cardUpdateForm" name="cardUpdateForm" method="post">
<input type="hidden" id="card.cardId" name="card.cardId"  value="${(card.cardId)!''}"/>
<input type="hidden" id="card.ownerId" name="card.ownerId"  value="${(card.ownerId)!''}"/>
<input type="hidden" id="card.createDate" name="card.createDate"  value="${(card.createDate)!''}"/>		
<input type="hidden" id="card.createUserId" name="card.createUserId"  value="${(card.createUserId)!''}"/>	
<input type="hidden" id="card.cardState" name="card.cardState"  value="${(card.cardState)!''}"/>	
<div class="box">
	<table class="tablecontent" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Card Name：</td>
			<td align="left" width="90%" >
				<input id="cardName" name="card.cardName" class="input_text" type="text" <#if 'view'=='${type!""}'> readonly='true' </#if> value="${card.cardName!""}"/><span id="cardNameMessage"></span>
			
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Team：</td>
			<td align="left" width="90%">
				<select id="group" name="card.groupId" class="select"  <#if 'view'=='${type!""}'> disabled="disabled" </#if> style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list groupList as group>
				             	 <option value="${group.groupId}" <#if '${group.groupId!""}'=='${card.groupId!""}'>selected</#if>>${group.groupName}</option>
				          	</#list> 
			     </select>
			     <span id="groupMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Project：</td>
			<td align="left" width="90%">
				<select id="project" name="card.projectId" class="select"  <#if 'view'=='${type!""}'> disabled="disabled" </#if> style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list projectList as project>
				             	 <option value="${project.projectId}" <#if '${project.projectId!""}'=='${card.projectId!""}'>selected</#if>>${project.projectName}</option>
				          	</#list> 
			    </select>
			    <span id="projectMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Card Type：</td>
			<td align="left" width="90%">
				<select id="card.cardType" name="card.cardType" class="select" <#if 'view'=='${type!""}'>disabled="disabled"</#if> style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list enumList as enum>
				             	 <option value="${enum.enumValue}" <#if '${card.cardType!""}'=='${enum.enumValue!""}'>selected</#if>>${enum.enumValue}</option>
				          	</#list> 
			    </select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Card Status：</td>
			<td align="left" width="90%">
				<select id="cardStatus" name="card.cardStatus" class="select"  style="width:180px"  <#if 'view'=='${type!""}'> disabled="disabled" </#if>>
	        		<option value="used"  <#if '${card.cardStatus!""}'=='used'>selected</#if>>used</option>
	        		<option value="not used"  <#if '${card.cardStatus!""}'=='not used'>selected</#if>>not used</option>
			    </select>
			    <span id="cardStatusMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Serial Number：</td>
			<td align="left" width="90%">
				<input id="card.sn" name="card.sn" class="input_text" type="text"  <#if 'view'=='${type!""}'> readonly='true' </#if> value="${card.sn!""}"/>
			</td>
		</tr>	
	    <tr>
			<td class="tablecontent_title" align="right" width="10%">Part Number：</td>
			<td align="left" width="90%">
				<input id="card.pn" name="card.pn" class="input_text" type="text" <#if 'view'=='${type!""}'> readonly='true' </#if> value="${card.pn!""}"/>
			</td>
		</tr>

		<tr>
			<td class="tablecontent_title" align="right" width="10%">World Wide Name：</td>
			<td align="left" width="90%">
				<input id="card.wwn" name="card.wwn" class="input_text" type="text" <#if 'view'=='${type!""}'> readonly='true' </#if>  value="${card.wwn!""}"/>
			</td>
		</tr>		
			
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Vendor：</td>
			<td align="left" width="90%">
				<input id="card.vendor" name="card.vendor" class="input_text" type="text" <#if 'view'=='${type!""}'> readonly='true' </#if>  value="${card.vendor!""}"/>
			</td>
		</tr>			

		<tr>
			<td class="tablecontent_title" align="right" width="10%">Attach Server：</td>
			<td align="left" width="90%">
				<select id="server" name="card.serverId" class="select"  <#if 'view'=='${type!""}'> disabled="disabled" </#if> style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list serverList as server>
				             	 <option value="${server.serverId}" <#if '${server.serverId!""}'=='${card.serverId!""}'>selected</#if>>${server.serverName}</option>
				          	</#list> 
			    </select>
			    <span id="serverMessage"></span> 
			</td>
		</tr>
		
		<tr>
	       <td align="right" class="tablecontent_title" width="10%">Purchase Date:</td>
	       <td align="left" width="90%">
	       		<input id="purchaseDate" name="card.purchaseDate" type="text" <#if 'view'=='${type!""}'> readonly='true' <#else> class="mh_date" </#if> value="${card.purchaseDate!""}"/>
	       </td>
        </tr>
		
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">
				<textarea id="description" name="card.description" class="text_area" <#if 'view'=='${type!""}'> readonly='true' </#if> value="${card.description!""}">${card.description!""}</textarea>
			</td>
		</tr>		

	</table>	
<table width="100%" border="0">
	<tr><td align="center">
	 <#if 'edit'=='${type!""}'> <button class="c" type="button" onclick="save()" >Save</button>  </#if>   
	    <button class="c" type="button" onclick="cancel()" >Cancel</button>

	</td></tr>
</table>
</div>
</form>
</body>
</html>
