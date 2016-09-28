<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<#include "/system/include/head.ftl">

<script src="${base}/js/jquery.metadata.js" type="text/javascript"></script>
<script src="${base}/js/jquery.ui.widget.js" type="text/javascript"></script>
<script src="${base}/js/jquery.ui.button.js" type="text/javascript"></script>


<script type="text/javascript">
$(document).ready(function(){

	$("#bntProjectAdd").click(function(){	
		var newTr = "<tr><td><select class='select' style='width:180px'>"
		            + "<option value=''selected >Please Select</option>"
		            + "<#list projectList as project>"
					+ "<option value='${project.projectId}'>${project.projectName}</option>"
					+ "</#list>"
		            + "</select></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		$("#tblproject tr:last").after(newTr);
  	});
  	
  	$("#regInfo").delegate(".bntDelTr", "click", function(){
		$(this).closest("tr").remove();
  	});
	
	//User name
	$("#usercode").blur(function(){
	  	var userCode = this.value;
	  	if('' == userCode){
	  		$("#usercodeMessage").html('<div class="messageNG">Please provide a user name!</div>');
	  	}
	  	else{
	  		$.ajax( {
				type : 'get',
				url : '${base}/auth/checkUserCode.do?t=' + new Date().getTime(),
				dataType: "text",
				data : { 'userCode' : userCode },
				success : function(msg) {
					if (msg == "exist") {	
						$("#usercodeMessage").html('<div class="messageNG">The user name has been registered\! please use another user name\!</div>');
					}
					else{
						$("#usercodeMessage").html('OK');
					}
					
				}
			});
	  	}
	});
	
	// Password and confirm password
	$("#password").blur(function(){
		var pwd = this.value;
		if('' == pwd){
			$("#passwordMessage").html('<div class="messageNG">Please provide a password!');
		}
		else if(pwd.length < 6){
			$("#passwordMessage").html('<div class="messageNG">Your password must be at least no less than 6 characters in length!');
		}
		else{
			$("#passwordMessage").html('OK');
		}
	});
	
	// Confirm password
	$("#confirm_password").blur(function(){
		var pwd = $("#password").val();
		var confirmpwd = this.value;
		if('' == confirmpwd){
			$("#passwordConfirmMessage").html('<div class="messageNG">Please provide a confirmation password!');
		}
		else if(pwd != confirmpwd){
			$("#passwordConfirmMessage").html('<div class="messageNG">Passwords must be consistent!');
		}
		else{
			$("#passwordConfirmMessage").html('OK');
		}
	});
	
	// Email
	$("#email").blur(function(){
		var email = this.value;
		if('' == email){
			$("#emailMessage").html('<div class="messageNG">Please provide an Email!');
		}
		else if(email.indexOf('@hp.com') < 0){
			$("#emailMessage").html('<div class="messageNG">Please enter a valid Email address!');
		}
		else{
			$.ajax( {
				type : 'get',
				url : '${base}/auth/checkUserMail.do?t=' + new Date().getTime(),
				dataType: "text",
				data : { 'email' : email },
				success : function(msg) {
					if (msg == "exist") {
						$("#emailMessage").html('<div class="messageNG">The mailbox is already registered\! please use another email address\!');
					}
					else{
						$("#emailMessage").html('OK');
					}
				}
			});
		}
	});
	
	// Group
	$("#userGroupId").blur(function(){
		var userGroupId = this.value;
		if('' == userGroupId){
			$("#userGroupIdMessage").html('<div class="messageNG">Please select a group!');
		}
		else{
			$("#userGroupIdMessage").html('OK');
		}
	});
	
	// Role
	$("#userRoleId").blur(function(){
		var userRoleId = this.value;
		if('' == userRoleId){
			$("#userRoleIdMessage").html('<div class="messageNG">Please select a role!');
		}
		else{
			$("#userRoleIdMessage").html('OK');
		}
	});
	
	// Confirm manage
	$("#confirmUserId").blur(function(){
		var confirmUserId = this.value;
		if('' == confirmUserId){
			$("#confirmUserIdMessage").html('<div class="messageNG">Please select a group!');
		}
		else{
			$("#confirmUserIdMessage").html('OK');
		}
	});
});

function checkInput() {
	var userCodeMessage = $("#usercodeMessage").html();
	if('OK' != userCodeMessage){
		alert('There is some error on User Name, please check it!');
		return;
	}
	
	var passwordMessage = $("#passwordMessage").html();
	if('OK' != passwordMessage){
		alert('There is some error on password, please check it!').html();
		return;
	}
	
	var passwordConfirmMessage = $("#passwordConfirmMessage").html();
	if('OK' != passwordConfirmMessage){
		alert('There is some error on confirm password, please check it!');
		return;
	}
	
	var emailMessage = $("#emailMessage").html();
	if('OK' != emailMessage){
		alert('There is some error on Email, please check it!');
		return;
	}
	
	var userGroupIdMessage = $("#userGroupIdMessage").html();
	if('OK' != userGroupIdMessage){
		alert('There is some error on Group, please check it!');
		return;
	}
	
	var userRoleIdMessage = $("#userRoleIdMessage").html();
	if('OK' != userRoleIdMessage){
		alert('There is some error on Role, please check it!');
		return;
	}
	
	var confirmUserIdMessage = $("#confirmUserIdMessage").html();
	if('OK' != confirmUserIdMessage){
		alert('There is some error on Confirm Manager, please check it!');
		return;
	}
		
	onSave();
}

function onSave() {
	if(confirm('Confirm submit?')){
		 saveProjectsIds();
  		 doFormSubmit('signupForm','doRegister.do')
    }
}

function saveProjectsIds(){
   var projectIds = [];
   var trArray = document.getElementById("tblproject").rows;
   for(var i=1; i<trArray.length; i++){
       if(trArray[i].cells[0].getElementsByTagName("select")[0].value != "objectHTMLTableRowElement"){
        projectIds.push(trArray[i].cells[0].getElementsByTagName("select")[0].value);
       }
   }
   document.getElementById("userProjectIds").value = projectIds;
}
</script>
</head>

<body>
<form id="signupForm" method="post" action="doRegister.do">
<div class="port_header" id="port_hd">
  <div class="banner">
    <div class="banner-left">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="logo_style">
	  <tr>
	    <td rowspan="2" width="69px"><img src="${base}/images/hplogo.jpg" width="62" height="53" /></td>
	    <td class="logo_style_cn">Titan Service Management</td>
	  </tr>
	</table>
  	</div>
    <div class="banner-right"></div>
  </div>
</div><!--end header-->
<div class="port_reg">
 <div id="regInfo"  class="box">
	<table width="60%" align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="20%" style="padding-bottom:15px"></td>
			<td width="80%" style="padding-bottom:15px">
	    		<div class="title">Register Here</div>
	    	</td>
	    </tr>
    </table>
	<table width="80%" border="0" cellpadding="0"  class="tablecontent">
		<tr>
			<td align="right" class="tablecontent_title" width="20%">User Name:</td>
			<td align="left" width="80%">
				<input id="usercode" name="usercode"  type="text" class="input_text" size="25"/><span id="usercodeMessage"></span>
			</td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="20%">Password:</td>
			<td align="left" width="80%">
				<input id="password" name="password" type="password" class="input_text" size="26"/><span id="passwordMessage"></span>
			</td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="20%">Confirm Password:</td>
			<td align="left" width="80%">
				<input id="confirm_password" type="password" class="input_text" size="26"/><span id="passwordConfirmMessage"></span>
			</td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="20%">E-mail:</td>
			<td align="left" width="80%">
				<input  id="email" name="email" type="text" class="input_text"  size="25"/><span id="emailMessage"></span>
			</td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="20%">User Group:</td>
			<td align="left" width="90%">
						<select id="userGroupId" name="userGroupId" class="select"  style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list groupList as group>
				             	 <option value="${group.groupId}" <#if '${group.groupId!""}'=='${user.groupId!""}'>selected</#if>>${group.groupName}</option>
				          	</#list> 
			          	</select>
			          	<span id="userGroupIdMessage"></span>
			</td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="20%">Role:</td>
			<td align="left" width="90%">
						<select id="userRoleId" name="userRoleId" class="select" style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list roleList as role>
				             	 <option value="${role.roleId}">${role.roleName}</option>
				          	</#list> 
			          	</select>
			          	<span id="userRoleIdMessage"></span>
			</td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="20%">Projects:</td>
			<td align="left" width="90%">
			    <input type="hidden" name="userProjectIds" id="userProjectIds" />	
			    <table id="tblproject" width="100%">
			    <tr></tr>
				</table>
				<button type="button" id="bntProjectAdd">Add</button>			
				<span id="projectIdMessage"></span>
			</td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="20%">Confirm Manager:</td>
			<td align="left" width="90%">
						<select id="confirmUserId" name="confirmUserId"  class="select" style="width:180px">
			        		<option value="" selected>Please Select</option>
				        	<#list manageList as manage>
				             	 <option value="${manage.userId}">${manage.userCode}</option>
				          	</#list> 
			          	</select>
			          	<span id="confirmUserIdMessage"></span>
			</td>
		</tr>
	</table>
	<table width="60%" align="center" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td width="20%" style="padding-top:15px"></td>
			<td width="80%" style="padding-top:15px">
			  	<!--start button-->
			    <div class="button">
			    	<div class="l"></div>
			      	<button type="button" class="c"  onclick="checkInput();">Submit</button>
			      	<div class="r"></div>
			      	<div class="clear"></div>
			    </div>
			    <!--end button-->	
			    <!--start button-->
			    <div class="button">
			    	<div class="l"></div>
			    	<button type="button" class="c" onclick="window.close();">Cancel</button>
			    	<div class="r"></div>
			    	<div class="clear"></div>
			    </div>
			    <!--end button-->
    		</td>
    	</tr>
    </table>
	<div class="clear"></div>
 </div>
</div>
</form>
</body>
</html>