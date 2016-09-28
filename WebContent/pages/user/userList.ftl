<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
function doDelete(){
	if(checkBox('delete','userIdAry')){
		if(confirm('Confirm the delete?')){
			doFormSubmit('userListForm','doUserRemove.do');
		}
	}
}

function doUpdate(){
	if(checkBox('update','userIdAry')){
		doFormSubmit('userListForm','goUserUpdate.do')
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
function onSearch(){
	$("#userListForm").submit();
}
function query() {
	$("#userListForm").submit();
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
<form id="userListForm" action="${base}/auth/userList.do" method="POST">
<div class="port_bar"><span class="marfin_lef10">Users</span><span class="marfin_lef5">></span><span class="marfin_lef5">Users Manage</span></div>
<div class="title"><img src="${base}/images/titleico.gif" width="18" height="15" class="ico" /><span class="marfin_lef5">Users Manage
	&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doPageRedirect('goUserCreate.do')">
		<img src="${base}/images/icon/11.png" /><span>Add&nbsp;&nbsp;Users</span> </a>
</span></div>

	   <div class="table_link">
        </div>
        <br>
<div class="clear"></div>
        <table id="pageTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="display">
         	<thead>
		        <th width="4%" align="center">
					<!--<input type="checkbox" id="allId" onclick="doAll(this ,'userIdAry')" />-->
				</th>
				<th width="10%" >User Name</th>
		        <th width="20%" >E-mail</th>
		     	<th width="15%" >User Group</th>
		     	<th width="15%" >Role</th>
		        <th width="15%" >Remark</th>
		        <th width="15%" >Action</th>
			</thead>
          
          <#if userList?has_content >
     	 <#list userList as user>
			<tr class="table_padding" class="mousechange">
				<td align="center"><input type="radio" id="userIdAry" name="userIdAry"  value="${(user.userId)!''}"/></td>
				<td align="center">${(user.userCode)!''}</td>
				<td align="center">${(user.mail)!''}</td>
				<td align="center"><#list user.userGroups as group>${(group.groupName)!''}</#list></td>
				<td align="center"><#list user.userRoles as role>${(role.roleName)!''}</#list></td>
				<td align="center">${(user.remark)!''}</td>
				<td align="center">
				<a class="tooltips" href="#"  onclick="doPageRedirect('${base}/auth/goUserUpdate.do?userId=${(user.userId)}')">
				<img src="${base}/images/icon/24.png" /><span>upadte</span> </a>&nbsp;&nbsp;&nbsp;
				<a class="tooltips" href="#"  onclick="doPageRedirect('${base}/auth/doUserRemove.do?userId=${(user.userId)}')">
				<img src="${base}/images/icon1/er_s.gif" /><span>Delete</span> </a>&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
			 </#list>
 		 </#if>
          
        </table>
</form>
</body>
</html>
