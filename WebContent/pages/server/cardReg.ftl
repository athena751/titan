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
	
	$("#cardName").blur(function(){
		var cardName = this.value;
		if('' == cardName){
			$("#cardNameMessage").html('<div class="messageNG">Please provice card name!');
		}
		else{
			$("#cardNameMessage").html('OK');
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
	
	
		
	// card type
	$("#cardType").blur(function(){
		var cardType = this.value;
		if('' == cardType){
			$("#cardTypeMessage").html('<div class="messageNG">Please select a card type!');
		}else{$("#cardTypeMessage").html('OK');}
				
	});
	
	
});

function checkInput() {
	var cardNameMessage = $("#cardNameMessage").html();
	if('OK' != cardNameMessage){
		alert('There is some error on Card Name, please check it!');
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
	
	var cardTypeMessage = $("#cardTypeMessage").html();
	if('OK' != cardTypeMessage){
		alert('There is some error on Card Type, please check it!').html();
		return;
	}		
	
	save();
}


function save(){  
   doFormSubmit('cardRegForm', 'doCardSave.do');   
}
function cancel() {
   doPageRedirect('cardList.do')
}
</script>

<body>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Card Register</div>
	</td><td align="right">
	</td></tr>
</table>
<form id="cardRegForm" name="cardRegForm" method="post" action="doCardSave.do">
<div class="box">
	<table class="tablecontent" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Card Name：</td>
			<td align="left" width="90%">
				<input id="cardName" name="card.cardName" class="input_text" type="text" value="${card.cardName!""}"/><span id="cardNameMessage"></span>
			
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Team：</td>
			<td align="left" width="90%">
				<select id="group" name="card.groupId" class="select"  style="width:180px">
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
				<select id="project" name="card.projectId" class="select"  style="width:180px">
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
				<select id="cardType" name="card.cardType" class="select"  style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list enumList as enum>
				             	 <option value="${enum.enumValue}" <#if '${card.cardType!""}'=='${enum.enumValue!""}'>selected</#if>>${enum.enumValue}</option>
				          	</#list> 
			    </select>
			    <span id="cardTypeMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Card Status：</td>
			<td align="left" width="90%">
				<select id="cardStatus" name="card.cardStatus" class="select"  style="width:180px">
	        		<option value="used" selected>used</option>
	        		<option value="not used" selected>not used</option>
			    </select>
			    <span id="cardStatusMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Serial Number：</td>
			<td align="left" width="90%">
				<input id="card.sn" name="card.sn" class="input_text" type="text" value="${card.sn!""}"/>
			</td>
		</tr>	
	    <tr>
			<td class="tablecontent_title" align="right" width="10%">Part Number：</td>
			<td align="left" width="90%">
				<input id="card.pn" name="card.pn" class="input_text" type="text" value="${card.pn!""}"/>
			</td>
		</tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">World Wide Name：</td>
			<td align="left" width="90%">
				<input id="card.wwn" name="card.wwn" class="input_text" type="text" value="${card.wwn!""}"/>
			</td>
		</tr>		
			
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Vendor：</td>
			<td align="left" width="90%">
				<input id="card.vendor" name="card.vendor" class="input_text" type="text" value="${card.vendor!""}"/>
			</td>
		</tr>	
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Attach Server：</td>
			<td align="left" width="90%">
				<select id="server" name="card.serverId" class="select"  style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list serverList as server>
				             	 <option value="${server.serverId}" <#if '${server.severId!""}'=='${card.serverId!""}'>selected</#if>>${server.serverName}</option>
				          	</#list> 
			    </select>
			    <span id="serverMessage"></span>
			</td>
		</tr>
		
		</tr>
	       <td align="right" class="tablecontent_title" width="10%">Purchase Date:</td>
	       <td align="left" width="90%">
	       		<input id="purchaseDate" name="card.purchaseDate" type="text" class="mh_date" value="${card.purchaseDate!""}"/>
	       </td>
        <tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">
				<textarea id="description" name="card.description" class="text_area" value="${card.description!""}"></textarea>
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
