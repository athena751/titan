<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script src="${base}/js/jquery.metadata.js" type="text/javascript"></script>
<script src="${base}/js/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${base}/js/jquery.ui.button.js" type="text/javascript"></script>
</head>
<script>

	$(document).ready(function(){
		
	});

	function cancel() {
		doPageRedirect('serverList.do?showType='+"CARDRES")
	}
</script>

<body>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Card Details</div>
	</td><td align="right">
	</td></tr>
</table>
<form id="serverRegForm" name="serverRegForm" method="post" action="doServerSave.do">
<div class="box">
	<table class="tablecontent" width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Card Name：</td>
			<td align="left" width="90%" >
				${card.cardName!""}
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Team：</td>
			<td align="left" width="90%">
				<select id="group" name="card.groupId" class="select"  <#if 'view'=='${type!""}'> disabled="disabled" </#if> style="width:180px" disabled="disabled">
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
				<select id="project" name="card.projectId" class="select"  <#if 'view'=='${type!""}'> disabled="disabled" </#if> style="width:180px" disabled="disabled">
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
				<select id="card.cardType" name="card.cardType" class="select" <#if 'view'=='${type!""}'>disabled="disabled"</#if> style="width:180px" disabled="disabled">
			        		<option value="" selected>Please Select</option>
				        	<#list enumList as enum>
				             	 <option value="${enum.enumValue}" <#if '${card.cardType!""}'=='${enum.enumValue!""}'>selected</#if>>${enum.enumValue}</option>
				          	</#list> 
			    </select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%"><font color="red"> * </font>Card Status：</td>
			<td align="left" width="90%">${card.cardStatus!""}
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Serial Number：</td>
			<td align="left" width="90%">
				${card.sn!""}
			</td>
		</tr>	
	    <tr>
			<td class="tablecontent_title" align="right" width="10%">Part Number：</td>
			<td align="left" width="90%">
				${card.pn!""}
			</td>
		</tr>

		<tr>
			<td class="tablecontent_title" align="right" width="10%">World Wide Name：</td>
			<td align="left" width="90%">
				${card.wwn!""}
			</td>
		</tr>		
			
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Vendor：</td>
			<td align="left" width="90%">
				${card.vendor!""}
			</td>
		</tr>			

		<tr>
			<td class="tablecontent_title" align="right" width="10%">Attach Server：</td>
			<td align="left" width="90%">
				<select id="server" name="card.serverId" class="select"  <#if 'view'=='${type!""}'> disabled="disabled" </#if> style="width:180px" disabled="disabled">
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
	       		${card.purchaseDate!""}
	       </td>
        </tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">${card.description!""}</td>
		</tr>		
	</table>	
	<table width="100%" border="0">
		<tr><td align="center">
		    <button class="c" type="button" onclick="cancel()" >Return To Card List</button>
		</td></tr>
	</table>
</div>
</form>
</body>
</html>
