<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#include "/system/include/fileUpdateInclued.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
function sendEmail(){
	if(checkBox('add','userIdAry')){
		doFormSubmit('sendEmailForm','sendEmail.do')
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
	 }else{
	 	 return true;
	 }
}
$("#file5").pekeUpload({theme:'bootstrap', allowedExtensions:"jpeg|jpg|png|gif", onFileError:function(file,error){alert("error on file: "+file.name+" error: "+error+"")}});
</script>
<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
</head>

<body>
<form id="sendEmailForm" action="sendEmail.do" method="POST">
<div class="port_bar"><span class="marfin_lef10">Users</span><span class="marfin_lef5">></span><span class="marfin_lef5">Roles Manage</span>></span><span class="marfin_lef5">Manage Users></span><span class="marfin_lef5">Send Email</span></span></div>
<div class="title"><img src="${base}/images/titleico.gif" width="18" height="15" class="ico" />Select Add User List</div>

<div class="showlist_main showlist_mainnopadding"></div><div class="box">
	<div class="bi">
	  <div class="bt">
			<div>
			</div>
	  </div>
		
		<div class="table_link">
        <ul>
          <li><a href="#" onclick="sendEmail()">Send</a></li>
        </ul></div>
        <div class="clear"></div>
      <table  id="pageTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="tlist">
          <thead>
		        <th width="5%" class="table_header_complex_c">
					<!--<input type="radio" id="allId" onclick="doAll(this ,'userIdAry')" />-->
				</th>
				<th width="10%" class="table_header_complex_c">User Name</th>
		        <th width="20%" class="table_header_complex_c">E-mail</th>
		     	<th width="15%" class="table_header_complex_c">User Group</th>
		     	<th width="15%" class="table_header_complex_c">Role</th>
		        <th width="30%" class="table_header_complex_c">Remark</th>
			</thead>
		<tbody>	
         <#if userList?has_content >
     	 <#list userList as user>
			<tr class="table_padding" class="mousechange">
				<td align="center"><input type="checkbox" id="userIdAry" name="userIdAry"  value="${(user.mail)!''}"/></td>
				<td align="center">${(user.userCode)!''}</td>
				<td align="center">${(user.mail)!''}</td>
				<td align="center"><#list user.userGroups as group>${(group.groupName)!''}</#list></td>
				<td align="center"><#list user.userRoles as role>${(role.roleDesc)!''}</#list></td>
				<td align="center">${(user.remark)!''}</td>
			</tr>
			 </#list>
 		 </#if>
         </tbody>
        </table>
       
        <div class="bb">
			<div>
			<input id="file" type="file" name="file" />
			</div>
		</div>
	</div>
</div>
</form>
</body>
</html>
