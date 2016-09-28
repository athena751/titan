<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
<script>
function onSave() {
   doFormSubmit('groupForm','doGroupSave.do')
}
function onBack() {
   doPageRedirect('groupList.do')
}

</script>
</head>

<body>
 <form id="groupForm" name="groupForm" method="post" action="doGroupSave.do">
	 <#if group.groupId?has_content && group.groupId!=0>
			<input type="hidden" name="group.groupId"  value="${(group.groupId)!''}"/>
			<input type="hidden" name="groupVo.strCreateDate"  value="${(group.createDate).toString()!""}"/>
			<input type="hidden" name="group.createUser"  value="${(group.createUser)!""}"/>
	 </#if>
<div class="port_bar"><span class="marfin_lef10">Users</span><span class="marfin_lef5">></span><span class="marfin_lef5">User Groups Manage</span><span class="marfin_lef5">></span>
	<span class="marfin_lef5">
	<#if group.groupId?has_content && group.groupId!=0>
	  	User Group Update  
	  <#else>
	  	User Group Create 
	  </#if>	</span>
	</div>
<div class="title"><img src="${base}/images/titleico.gif" width="18" height="15" class="ico" />
	<span class="marfin_lef5"><#if group.groupId?has_content && group.groupId!=0>
	  	User Group Update  
	  <#else>
	  	User Group Create 
	  </#if>	</span>
</div>

<div class="showlist_main showlist_mainnopadding"></div>

<div class="box">
		<div class="rc">
		<b class="rctop"><b class="rc1"></b><b class="rc2"></b><b class="rc3"></b><b class="rc4"></b></b>
      <table width="98.5%" align="center" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
			<tr>
			  <td bgcolor="#e2f1fd" align="right" width="10%">Name:</td>
			  <td align="left" width="90%"><input name="group.groupName" type="text" class="input_text" value="${group.groupName!""}"/></td>
			</tr>
			<tr>
			  <td bgcolor="#e2f1fd" align="right" width="10%">Description:</td>
			  <td align="left" width="90%"><input name="group.descritption"  type="text" class="input_text" value="${group.descritption!""}"  /></td>
			</tr>
			<tr>
			<td bgcolor="#e2f1fd" align="right" width="10%">Remark:</td>
			<td width="90%" align="left" >
			<textarea name="group.remark" class="text_area" >${group.remark!""}
			
			</textarea>
			</td>
			</tr>
			<tr>
			  <td align="right" width="10%">&nbsp;</td>
			  <td align="left" width="90%">
			    <!--start button-->
			    <div class="button">
			      <div class="l"></div>
			      <button class="c" type="button" onclick="onSave()" >Confirm</button>
			      <div class="r"></div>
			      <div class="clear"></div>
			      </div>
			    <!--end button-->
			  </td>
			</tr>
			</table>
<b class="rcbottom"><b class="rc4"></b><b class="rc3"></b><b class="rc2"></b><b class="rc1"></b></b>
<div class="clear"></div>
        
 
</form>
</body>
</html>
