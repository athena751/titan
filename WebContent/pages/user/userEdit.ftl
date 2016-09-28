<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
 
<script>
function onCheckInput() {
	var userCode = document.getElementById("usercode").value;
	if(userCode==""){
		alert("Please provide the user name!");
		document.getElementById("usercode").focus();
		return false;
	}
	
	var password = document.getElementById("password").value;
	if(password==""){
		alert("Please provide the password!");
		document.getElementById("password").focus();
		return false;
	}

	var userGroup = document.getElementById("userGroup").value;
	if(userGroup==""){
		alert("Please select the group!");
		document.getElementById("userGroup").focus();
		return false;
	}
	
	var userRole = document.getElementById("userRole").value;
	if(userRole==""){
		alert("Please select the role!");
		document.getElementById("userRole").focus();
		return false;
	}
	return true;
}
function onBack() {
   doPageRedirect('userList.do')
}

function doCheck() {
	 <#if user.userId?has_content >
		if(onCheckInput()){
			var emaildefault=document.getElementById("usermail").defaultValue;
			var email=document.getElementById("usermail").value;
			if(emaildefault!=email){
				if(checkUserEmail()){
					doFormSubmit('userForm','doUserSave.do');
				}
			}else{
				doFormSubmit('userForm','doUserSave.do');
			}
			
		}
	 <#else>
		if(onCheckInput()){
			var blc=checkUserCode();
			var blm=checkUserEmail();
			if(blc && blm){
				doFormSubmit('userForm','doUserSave.do');
			}
		}
	</#if>
}
	 
function checkUserCode() {
	 		var userCode = document.getElementById("usercode").value;
			var message  ='';	
			var bl=false;	
			if(userCode!=''){
				$.ajax( {
					type : 'get',
					url : '${base}/auth/checkUserCode.do',
					dataType: "text",
					async : false,
					data : { 'userCode' : userCode },
					success : function(msg) {
						if (msg == "exist") {
							message="The user name has been registered\! please use another user name\! ";
							alert(message);
							document.getElementById("usercode").focus();
						}else if (msg == "noexist"){
							message="The user name is not up! you can register!";
							bl=true;
						}
						document.getElementById("usercodeMessage").innerHTML=message;
					}
				});
			}
			return bl;
}

function checkUserEmail() { 
	 		var mail = document.getElementById("usermail").value;	
			var message  ='';
			var bl=false;
			if(mail!=''){
				$.ajax( {
					type : 'get',
					url : '${base}/auth/checkUserMail.do',
					dataType: "text",
					data : { 'email' : mail },
					async : false,
					success : function(msg) {
						if (msg == "exist") {
							message="The e-mail has been registered\! please use another e-mail\! ";
							alert(message);
							document.getElementById("usermail").focus();
						}else if (msg == "noexist"){
							message="The e-mail is not up! you can register!";
						   bl=true;
						}
						document.getElementById("usermailMessage").innerHTML=message;
					}
				});
			}
	 	return bl;
}

</script>
<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
</head>

<body>
<form id="userForm" name="userForm" method="post" >
	 <#if user.userId?has_content >
			<input id="userId" type="hidden" name="user.userId"  value="${(user.userId)!""}"/>
			<input type="hidden" name="userVo.strCreateDate"  value="${(user.createDate).toString()!""}"/>
			<input type="hidden" name="user.createUser"  value="${(user.createUser)!""}"/>
	 </#if>
	<div class="port_bar"><span class="marfin_lef10">Users</span><span class="marfin_lef5">></span><span class="marfin_lef5">Users Manage</span><span class="marfin_lef5">></span>
		  <span class="marfin_lef5"><#if user.userId?has_content >
		  	User Info Update
		  <#else>
		  	User Create
		  </#if>	</span></div>
	<div class="title">
		  
		   <img src="${base}/images/titleico.gif" width="18" height="15" class="ico" />
		   <span class="marfin_lef5">
			   <#if user.userId?has_content >
			  	User Info Update
			  <#else>
			  	User Create
			  </#if>	
		 </span></div>

	<div class="showlist_main showlist_mainnopadding"></div>

	<div class="box">
		<div class="rc">
		<b class="rctop"><b class="rc1"></b><b class="rc2"></b><b class="rc3"></b><b class="rc4"></b></b>
	      <table width="98.5%" height="100%" align="center" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
				<tr>
				  <td bgcolor="#e2f1fd" align="right" width="10%" color="#fcfdfd">User Name：</td>
				  <td align="left" width="90%"><input id="usercode"   name="user.userCode"  type="text" size="61" <#if user.userId?has_content >readonly</#if> class="input_text" value="${user.userCode!""}"/><span id="usercodeMessage"></span></td>
				</tr>
				<tr>
				<td bgcolor="#e2f1fd" align="right" width="10%">Password：</td>
				<td align="left" width="90%"><input id="password"  name="user.password" type="password" class="input_text" size="61" value="${user.password!""}"/></td>
				</tr>
				<tr>
				<td bgcolor="#e2f1fd" align="right" width="10%">E-mail：</td>
				<td align="left" width="90%"><input id="usermail"    name="user.mail"  type="text"  class="input_text" size="61" value="${user.mail!""}"/><span id="usermailMessage"></span></td>
				</tr>
				<tr>
				<td bgcolor="#e2f1fd" align="right" width="10%">User Group：</td>
				<td align="left" width="90%">
							<select id="userGroup" name="user.groupId"  class="select"  style="width:400px">
				        		<option value="" selected>Please Select</option>
					        	<#list groupList as group>
					             	 <option value="${group.groupId}" <#if '${group.groupId!""}'=='${user.groupId!""}'>selected</#if>>${group.groupName}</option>
					          	</#list> 
				          	</select>
				</td>
				</tr>
				<tr>
				<td bgcolor="#e2f1fd" align="right" width="10%">Role：</td>
				<td align="left" width="90%">
							<select id="userRole" name="user.roleId"  class="select" style="width:400px">
				        		<option value="" selected>Please Select</option>
					        	<#list roleList as role>
					             	 <option value="${role.roleId}" <#if '${role.roleId!""}'=='${user.roleId!""}'>selected</#if>>${role.roleName}</option>
					          	</#list> 
				          	</select>
				</td>
				</tr>
				<tr>
				<td bgcolor="#e2f1fd" align="right" width="10%">Remark：</td>
				<td width="90%" align="left" >
				<textarea name="user.remark" class="text_area">${user.remark!""}
				</textarea>
				</td>
				</tr>
				
				<tr>
				  <td  style="vertical-align:middle; text-align:center;"  colspan="2"> 
				 
				  	<!--start button-->
				    <div  class="button" style="margin-left: 500px;">
				      <div class="l"></div>
				      <button class="c" type="button" onclick="doCheck()">Save</button>
				      <div class="r"></div>
				      <div class="clear"></div>
				      </div>
				    <!--end button-->
				        <!--start button-->
				    <div class="button" style="float:center;">
				      <div class="l"></div>
				     	 <button class="c" type="button" onclick="onBack()">Back</button>
				      <div class="r"></div>
				      <div class="clear"></div>
				      </div>
				    <!--end button-->
				    </td>
				</tr>
		</table>
		<b class="rcbottom"><b class="rc4"></b><b class="rc3"></b><b class="rc2"></b><b class="rc1"></b></b>
		
	</div>
	
</body>
</html>
