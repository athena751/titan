<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	

<script>
function doDelete(){
	if(checkBox('delete','roleIdAry')){
		if(confirm('Confirm the delete?')){
			doFormSubmit('userDoId','doRoleRemove.do');
		}
	}
}

function doUpdate(){
	if(checkBox('update','roleIdAry')){
		doFormSubmit('userDoId','goRoleUpdate.do')
	}
}
function goRoleAuthMng(){
	if(checkBox('update','roleIdAry')){
		doFormSubmit('userDoId','goRoleAuthMng.do')
	}
}

function goRoleUserMng(){
	if(checkBox('update','roleIdAry')){
		doFormSubmit('userDoId','goRoleUserMng.do')
	}
}

function doAll(obj ,name){
  var roleIds = document.getElementsByName(name);
  if(roleIds != null && roleIds.length > 0){
     for(var i = 0 ; roleIds.length > i; i++){
        roleIds[i].checked = obj.checked;
     }
	}
}



function checkBox(type,name){
	var roleIds = document.getElementsByName(name);
	var count = 0;
	if(roleIds != null && roleIds.length > 0){
     for(var i = 0 ; roleIds.length > i; i++){
        if(roleIds[i].checked){
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
</script>
<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
</head>

<body>
<form id="userDoId" action="" method="POST">
<div class="port_bar"><span class="marfin_lef10">Users</span><span class="marfin_lef5">></span><span class="marfin_lef5">Roles Manage</span></span></div>
<div class="title"><img src="${base}/images/titleico.gif" width="18" height="15" class="ico" />Roles Manage
&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doPageRedirect('goRoleCreate.do')">
		<img src="${base}/images/icon/11.png" /><span>Add&nbsp;&nbsp;Users</span> </a>
</div>

		
		<div class="table_link">
        </div>
        <div class="clear"></div>
      <table id="pageTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="display">
          <thead>
			        <th width="4%" class="table_header_complex_c">
						<!--<input type="checkbox" id="allId" onclick="doAll(this ,'roleIdAry')" />-->
					</th>
			        <th width="30%" class="table_header_complex_c">Name</th>
			        <th width="25%" class="table_header_complex_c">Description</th>
			        <th width="20%" class="table_header_complex_c">Remark</th>
			         <th width="30%" class="table_header_complex_c">Action</th>
			</thead>
          <tbody>
		<#if roleList?has_content >
     	 <#list roleList as role>
			<tr class="table_padding " class="mousechange">
				<td align="center"><!--<input type="radio" id="roleIdAry" name="roleIdAry"  value="${(role.roleId)!''}"/>--></td>
				<td align="center">${(role.roleName)!''}</td>
				<td align="center">${(role.roleDesc)!''}</td>
				<td align="center">${(role.remark)!''}</td>
				<td align="center">
				<a class="tooltips" href="#"  onclick="doPageRedirect('${base}/auth/goRoleUpdate.do?roleId=${(role.roleId)}')">
				<img src="${base}/images/icon/24.png" /><span>upadte</span> </a>&nbsp;&nbsp;&nbsp;
				<a class="tooltips" href="#"  onclick="doPageRedirect('${base}/auth/doRoleRemove.do?roleId=${(role.roleId)}')">
				<img src="${base}/images/icon1/er_s.gif" /><span>Delete</span> </a>&nbsp;&nbsp;&nbsp;
				<a class="tooltips" href="#"  onclick="doPageRedirect('${base}/auth/goRoleAuthMng.do?roleId=${(role.roleId)}')">
				<img src="${base}/images/icon/28.png" /><span>Manage&nbsp;&nbsp;Rights</span> </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
			 </#list>
 		 </#if>
 		</tbody>
          
        </table>
</form>
</body>
</html>
