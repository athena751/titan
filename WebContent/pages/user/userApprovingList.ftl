<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
function doApprove(){
	if(checkBox('userIdAry')){
		if(confirm('Confirm the approve?')){
			doFormSubmit('userListForm','doUserApprove.do');
		}
	}
}

function doReject(){
	
		var html = "<div style='padding:10px;'>Please enter the reason：<input type='text' id='reason' name='reason' value='' /></div>";
		var submit = function (v, h, f) {
	        if (f.reason == '') {
	            jBox.tip("Please enter the reason!", 'error', { focusId: "reason" });
	            return false;
	        }
	        $('#rejectReason').val(f.reason);
	        if(null != $('#reason').val() && $('#reason').val() != ''){
				doFormSubmit('userListForm','doUserReject.do');
			}
	        return true;
	    };
	    jBox(html, { title: "Confirm Reject?", submit: submit, loaded: function (h) {}});

}

function checkBox(name){
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

</script>
<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
</head>

<body>
<form id="userListForm" action="${base}/auth/userList.do" method="POST">
<input type='hidden' id='rejectReason' name='rejectReason' />
<div class="port_bar"><span class="marfin_lef10">Application</span></div>
<div class="title"><img src="${base}/images/titleico.gif" width="18" height="15" class="ico" /><span class="marfin_lef5">Users Manage</span></div>

<div class="showlist_main showlist_mainnopadding"></div>
 
<div class="box">
	   <div class="table_link">
        <!--<ul>
     	  <#if menuAuth.isAuth("BTN_USER_CUD") >
         	 <li><a href="#" onclick="doApprove()">Approve </a></li>
          	<li><a href="#" onclick="doReject('goUserCreate.do')">Reject</a></li>
         </#if>
        </ul>--></div>
<div class="clear"></div>
        <table id="pageTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="tlist">
         	<thead>
		        <th width="4%" align="center">
				</th>
				<th width="10%" >User Name</th>
		        <th width="15%" >E-mail</th>
		     	<th width="15%" >User Group</th>
		     	<th width="15%" >Role</th>
		        <th width="20%" >Remark</th>
		        <th width="15%" >Action</th>
			</thead>
          
          <#if userList?has_content >
     	 <#list userList as user>
			<tr class="table_padding" class="mousechange">
				<td align="center"><input type="radio" id="userIdAry" name="userIdAry"  value="${(user.userId)!''}"/></td>
				<td align="center">${(user.userCode)!''}</td>
				<td align="center">${(user.mail)!''}</td>
				<td align="center"><#list user.userGroups as group>${(group.groupName)!''}</#list></td>
				<td align="center"><#list user.userRoles as role>${(role.roleDesc)!''}</#list></td>
				<td align="center">${(user.remark)!''}</td>
				<td align="center">
				<a class="tooltips" href="#"  onclick="doPageRedirect('${base}/auth/doUserApprove.do?userId=${(user.userId)}')">
				<img src="${base}/images/icon/10.png" /><span>Approve</span> </a>&nbsp;&nbsp;&nbsp;
				<a class="tooltips" href="#"  onclick="doReject('goUserCreate.do?userId=${(user.userId)}')">
				<img src="${base}/images/icon/12.png" /><span>Reject</span> </a>&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
			 </#list>
 		 </#if>
          
        </table>
        
	</div>
</div>
</form>
</body>
</html>
