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
   doFormSubmit('storageUpdateForm', 'doStorageUpdate.do');   
}
function cancel() {
   doPageRedirect('storageList.do');
}
</script>

<body>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title"> <#if 'edit'=='${type!""}'>storage Update</#if></div>
		<div class="title"> <#if 'view'=='${type!""}'>storage Details</#if></div>
	</td><td align="right">
	</td></tr>
</table>

<form id="storageUpdateForm" name="storageUpdateForm" method="post">
<input type="hidden" id="storage.storageId" name="storage.storageId"  value="${(storage.storageId)!''}"/>
<input type="hidden" id="storage.ownerId" name="storage.ownerId"  value="${(storage.ownerId)!''}"/>
<input type="hidden" id="storage.createDate" name="storage.createDate"  value="${(storage.createDate)!''}"/>		
<input type="hidden" id="storage.createUserId" name="storage.createUserId"  value="${(storage.createUserId)!''}"/>	
<div class="box">
	<table class="tablecontent" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>storage Name：</td>
			<td align="left" width="90%" >
				<input id="storageName" name="storage.storageName" class="input_text" type="text" <#if 'view'=='${type!""}'> readonly='true' </#if> value="${storage.storageName!""}"/><span id="storageNameMessage"></span>
			
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Team：</td>
			<td align="left" width="90%">
				<select id="group" name="storage.groupId" class="select"  <#if 'view'=='${type!""}'> disabled="disabled" </#if> style="width:180px">
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
				<select id="project" name="storage.projectId" class="select"  <#if 'view'=='${type!""}'> disabled="disabled" </#if> style="width:180px">
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
				<select id="storage.storageType" name="storage.storageType" class="select" <#if 'view'=='${type!""}'>disabled="disabled" </#if> style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list enumList as enum>
				             	 <option value="${enum.enumValue}" <#if '${storage.storageType!""}'=='${enum.enumValue!""}'>selected</#if>>${enum.enumValue}</option>
				          	</#list> 
			    </select>
			    <span id="serverMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Storage Status：</td>
			<td align="left" width="90%">
				<select id="storageStatus" name="storage.storageStatus" class="select"  style="width:180px" <#if 'view'=='${type!""}'>disabled="disabled" </#if>>
	        		<option value="used"  <#if '${storage.storageStatus!""}'=='null'>selected</#if>>null</option>
	        		<option value="used"  <#if '${storage.storageStatus!""}'=='used'>selected</#if>>used</option>
	        		<option value="not used"  <#if '${storage.storageStatus!""}'=='not used'>selected</#if>>not used</option>
			    </select>
			    <span id="storageStatusMessage"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Serial Number：</td>
			<td align="left" width="90%">
				<input id="storage.sn" name="storage.sn" class="input_text" type="text"  <#if 'view'=='${type!""}'> readonly='true' </#if> value="${storage.sn!""}"/>
			</td>
		</tr>	
	    <tr>
			<td class="tablecontent_title" align="right" width="10%">Part Number：</td>
			<td align="left" width="90%">
				<input id="storage.pn" name="storage.pn" class="input_text" type="text" <#if 'view'=='${type!""}'> readonly='true' </#if> value="${storage.pn!""}"/>
			</td>
		</tr>

		<tr>
			<td class="tablecontent_title" align="right" width="10%">Capacity：</td>
			<td align="left" width="90%">
				<input id="storage.capacity" name="storage.capacity" class="input_text" type="text" <#if 'view'=='${type!""}'> readonly='true' </#if>  value="${storage.capacity!""}"/>
			</td>
		</tr>		
			
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Slot Number：</td>
			<td align="left" width="90%">
				<input id="storage.slotNum" name="storage.slotNum" class="input_text" type="text" <#if 'view'=='${type!""}'> readonly='true' </#if>  value="${storage.slotNum!""}"/>
			</td>
		</tr>			
		
	    <tr>
			<td class="tablecontent_title" align="right" width="10%">Used Slot Number：</td>
			<td align="left" width="90%">
				<input id="storage.slotUsed" name="storage.slotUsed" class="input_text" type="text" <#if 'view'=='${type!""}'> readonly='true' </#if>  value="${storage.slotUsed!""}"/>
			</td>
		</tr>
		
		<tr>
	       <td align="right" class="tablecontent_title" width="10%">Purchase Date:</td>
	       <td align="left" width="90%"><input id="purchaseDate" name="storage.purchaseDate" type="text" <#if 'view'=='${type!""}'> readonly='true' <#else> class="mh_date" </#if> value="${storage.purchaseDate!""}"/></td>
        </tr>
        
          <tr>
			<td class="tablecontent_title" align="right" width="10%">Location：</td>
			<td align="left" width="90%">
				<input id="storage.location" name="storage.location" class="input_text" type="text" <#if 'view'=='${type!""}'> readonly='true' </#if>  value="${storage.location!""}"/>
			</td>
		</tr>	
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">
				<textarea id="description" name="storage.description" class="text_area" <#if 'view'=='${type!""}'> readonly='true' </#if> value="${storage.description!""}">${storage.description!""}</textarea>
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
