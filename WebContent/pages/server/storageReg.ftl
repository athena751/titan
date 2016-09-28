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
	
	$("#storageName").blur(function(){
		var storageName = this.value;
		if('' == storageName){
			$("#storageNameMessage").html('<div class="messageNG">Please provice storage name!');
		}
		else{
			$("#storageNameMessage").html('OK');
		}
	});
	
	// type
	$("#storageType").blur(function(){
		var type = this.value;
		if('' == type){
			$("#storageTypeMessage").html('<div class="messageNG">Please select a type!');
		}
		else{
			$("#storageTypeMessage").html('OK');
		}
	});
	
	
});

function checkInput() {
	var storageNameMessage = $("#storageNameMessage").html();
	if('OK' != storageNameMessage){
		alert('There is some error on Storage Name, please check it!');
		return;
	}
	
	var storageTypeMessage = $("#storageTypeMessage").html();
	if('OK' != storageTypeMessage){
		alert('Please provide storage type!');
		return;
	}
	save();
}

function save(){  
   doFormSubmit('storageRegForm', 'doStorageSave.do');   
}
function cancel() {
   doPageRedirect('storageList.do')
}
</script>

<body>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Storage Register</div>
	</td><td align="right">
	</td></tr>
</table>
<form id="storageRegForm" name="storageRegForm" method="post" action="doStorageSave.do">
<div class="box">
	<table class="tablecontent" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Storage Name：</td>
			<td align="left" width="90%">
				<input id="storageName" name="storage.storageName" class="input_text" type="text" value="${storage.storageName!""}"/><span id="storageNameMessage"></span>
			
		</tr>
				<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Team：</td>
			<td align="left" width="90%">
				<select id="group" name="storage.groupId" class="select"  style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list groupList as group>
				             	 <option value="${group.groupId}" <#if '${group.groupId!""}'=='${storage.groupId!""}'>selected</#if>>${group.groupName}</option>
				          	</#list> 
			     </select>
			     <span id="groupMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Project：</td>
			<td align="left" width="90%">
				<select id="project" name="storage.projectId" class="select"  style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list projectList as project>
				             	 <option value="${project.projectId}" <#if '${project.projectId!""}'=='${storage.projectId!""}'>selected</#if>>${project.projectName}</option>
				          	</#list> 
			    </select>
			    <span id="projectMessage"></span>
			</td>
		</tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Storage Type：</td>
			<td align="left" width="90%">
				<select id="storageType" name="storage.storageType" class="select"  style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list enumList as enum>
				             	 <option value="${enum.enumValue}" <#if '${storage.storageType!""}'=='${enum.enumValue!""}'>selected</#if>>${enum.enumValue}</option>
				          	</#list> 
			    </select>
			    <span id="storageTypeMessage"></span>
			</td>
		</tr>
		<tr>
	       <td align="right" class="tablecontent_title" width="10%">Storage Status:</td>
	       <td align="left" width="90%">
	        <select id="storageStatus" name="storage.storageStatus" class="select"  style="width:180px">
        		<option value="used" selected>used</option>
        		<option value="not used" selected>not used</option>
		    </select>
		    <span id="storageStatusMessage"></span>
	       </td>
        </tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Serial Number：</td>
			<td align="left" width="90%">
				<input id="storage.sn" name="storage.sn" class="input_text" type="text" value="${storage.sn!""}"/>
			</td>
		</tr>	
	    <tr>
			<td class="tablecontent_title" align="right" width="10%">Part Number：</td>
			<td align="left" width="90%">
				<input id="storage.pn" name="storage.pn" class="input_text" type="text" value="${storage.pn!""}"/>
			</td>
		</tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Capacity：</td>
			<td align="left" width="90%">
				<input id="storage.capacity" name="storage.capacity" class="input_text" type="text" value="${storage.capacity!""}"/>
			</td>
		</tr>		
			
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Slot Number：</td>
			<td align="left" width="90%">
				<input id="storage.slotNum" name="storage.slotNum" class="input_text" type="text" value="${storage.slotNum!""}"/>
			</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Used Slot Number：</td>
			<td align="left" width="90%">
				<input id="storage.slotUsed" name="storage.slotUsed" class="input_text" type="text" value="${storage.slotUsed!""}"/>
			</td>
		</tr>		
		</tr>
	       <td align="right" class="tablecontent_title" width="10%">Purchase Date:</td>
	       <td align="left" width="90%"><input id="purchaseDate" name="storage.purchaseDate" type="text" class="mh_date" value="${storage.purchaseDate!""}" readonly="true" /></td></td>
        <tr>
        <tr>
			<td class="tablecontent_title" align="right" width="10%">Location：</td>
			<td align="left" width="90%">
				<input id="storage.location" name="storage.location" class="input_text" type="text" value="${storage.location!""}"/>
			</td>
		</tr>	
	
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">
				<textarea id="description" name="storage.description" class="text_area" value="${storage.description!""}"></textarea>
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
