<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script src="${base}/js/jquery.metadata.js" type="text/javascript"></script>
<script src="${base}/js/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${base}/js/jquery.ui.button.js" type="text/javascript"></script>
</head>
<script>

function save(){  
   doFormSubmit('createValueForm', 'doValueSave.do');   
}
function cancel() {
   doPageRedirect('typeList.do')
}
</script>

<body>
<table width="40%" border="0">
	<tr><td align="left">
		<div class="title"> <#if 'edit'=='${type!""}'> Update </#if> <#if 'create'=='${type!""}'> Create </#if> Value for ${(enumType.typeName)!''}</div>
	</td><td align="right">
	</td></tr>
</table>
<form id="createValueForm" name="createValueForm" method="post" action="doValueSave.do">
<input  type="hidden" id="typeId" name="typeId"  value="${enumValue.typeId!""}"/>
<input  type="hidden" id="enumValue.enumId" name="enumValue.enumId"  value="${enumValue.enumId!""}"/>
<input  type="hidden" id="enumValue.typeId" name="enumValue.typeId"  value="${enumValue.typeId!""}"/>
<div class="box">
	<table class="tablecontent" width="40%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td class="tablecontent_title" align="right" width="20%"><font color="red"> * </font>Value Name：</td>
			<td align="left" width="90%">
				<input id="enumValue.enumValue" name="enumValue.enumValue" class="input_text" type="text" value="${enumValue.enumValue!""}"/><span id="enumValueMessage"></span>
		</tr>
		
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">
				<textarea id="description" name="enumValue.description" class="text_area" value="${enumValue.description!""}"></textarea>
			</td>
		</tr>		

	</table>	
<table width="40%" border="0">
	<tr><td align="center">
	    <button class="c" type="button" onclick="save()" >Save</button>
	    <button class="c" type="button" onclick="cancel()" >Cancel</button>

	</td></tr>
</table>
</div>
</form>
</body>
</html>
