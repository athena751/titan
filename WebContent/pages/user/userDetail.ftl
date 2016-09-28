<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
 
<script>
function goUpdate() {
   doFormSubmit('userForm','goUserDetailUpdate.do')
}
</script>
<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
</head>

<body>
 <form id="userForm" name="userForm" method="post" action="goUserUpdate.do">
	 <#if user.userId?has_content >
			<input type="hidden" name="user.userId"  value="${(user.userId)!""}"/>
	 </#if>
<div class="port_bar"><span class="marfin_lef10">Users</span><span class="marfin_lef5">></span><span class="marfin_lef5">User Info Detail</span><span class="marfin_lef5"></div>
<div class="title"><img src="${base}/images/titleico.gif" width="18" height="15" class="ico" /><span class="marfin_lef5">User Info Detail</span></div>

<div class="showlist_main showlist_mainnopadding"></div>

<div class="box">
	<div class="bi">
	  <div class="bt">
			<div>
			</div>
	  </div>
       <div class="table_link">
        <ul>
          <li><a href="#" onclick="goUpdate()">Update</a></li>
        </ul></div>
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
<tr>
  <td align="right" width="10%"><@s.text name="user.userCode"/>：</td>
  <td align="left" width="90%">${user.userCode!""}</td>
</tr>
<tr>
  <td align="right" width="10%">Password：</td>
  <td align="left" width="90%" value="${user.password!""}">******</td>
</tr>
<tr>
<td align="right" width="10%">E-mail：</td>
<td align="left" width="90%">${user.mail!""}</td>
</tr>
<tr>
<td align="right" width="10%">User Group：</td>
<td width="90%" align="left" >
 		<#list user.userGroups as group>
       	 		${group.groupName}	
       	 </#list> 
</td>
</tr>
<tr>
<td align="right" width="10%">Role：</td>
<td width="90%" align="left" >
 			<#list user.userRoles as role>
	             	${role.roleName}
	    	</#list> 
</td>
</tr>
<tr>
<td align="right" width="10%">Project：</td>
<td width="90%" align="left" >
 			<#list user.userProjects as project>
	             	${project.projectName} &nbsp;&nbsp;&nbsp;
	    	</#list> 
</td>
</tr>
<tr>
<td align="right" width="10%">Remark：</td>
<td width="90%" align="left" >
	${user.remark!""}
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
</body>
</html>
