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
   doFormSubmit('authForm','doAuthSave.do')
}
function onBack() {
   doPageRedirect('authList.do')
}

</script>
</head>

<body>
 <form id="authForm" name="authForm" method="post" action="doAuthSave.do">
  <#if auth.authId?has_content >
			<input type="hidden" name="auth.authId"  value="${(auth.authId)!""}"/>
			<input type="hidden" name="authVo.strCreateDate"  value="${(auth.createDate).toString()!""}"/>
			<input type="hidden" name="auth.createUser"  value="${(auth.createUser)!""}"/>
	 </#if>
<div class="port_bar"><span class="marfin_lef10">Users</span><span class="marfin_lef5">></span><span class="marfin_lef5">Rights Manage</span><span class="marfin_lef5">></span>
			<span class="marfin_lef5"><#if auth.authId?has_content >
			  	Right Update
			  <#else>
			  	Right Create 
			  </#if>	</span></div>
<div class="title"><img src="${base}/images/titleico.gif" width="18" height="15" class="ico" />
			<span class="marfin_lef5"><#if auth.authId?has_content >
			  	Right Update 
			  <#else>
			  	Right Create 
			  </#if>	</span>	
</div>

<div class="showlist_main showlist_mainnopadding"></div>

<div class="box">
	<div class="bi">
	  <div class="bt">
			<div>
			</div>
	  </div>
      <div class="table_link">
        <ul>
          <li><a href="#" onclick="onBack()">Back</a></li>
        </ul>
      </div>
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
<tr>
  <td align="right" width="10%">Name：</td>
  <td align="left" width="90%"><input name="auth.authName" type="text" class="input_text" value="${auth.authName!''}"/></td>
</tr>
<tr>
  <td align="right" width="10%">Description：</td>
  <td align="left" width="90%"><input name="auth.authDesc" type="text" class="input_text" value="${auth.authDesc!''}" /></td>
</tr>
<tr>
<td align="right" width="10%">Remark：</td>
<td width="90%" align="left" >
<textarea name="auth.remark" class="text_area">${auth.remark!''}

</textarea>
</td>
</tr>
 
<tr>
  <td align="right" width="10%">&nbsp;</td>
  <td align="left" width="90%">
    <!--start button-->
    <div class="button">
      <div class="l"></div>
      <button class="c" type="button" onclick="onSave()" type="button">Confirm</button>
      <div class="r"></div>
      <div class="clear"></div>
      </div>
    <!--end button-->
  </td>
</tr>
</table>
<div class="clear"></div>
        
     
<div class="bb">
			<div>
			</div>
		</div>
	</div>
</div>
</form>
</body>
</html>
