<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Titan</title>
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/css/login.css" />
		<style type="text/css">
HTML,BODY {
	height: 100%;
	weight: 960px;
}
</style>

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/jquery-1.9.0.min.js">
		</script>
		<script language="javascript">
if (window.name != "bencalie") {
	parent.location.reload();
	window.name = "bencalie";
} else {
	window.name = "";
}

function pFcheck() {
	if (document.aform.usercode.value == "") {
		document.getElementById("error").innerHTML = 'Please input User Name';
		document.aform.usercode.focus();
		return false;
	} else {
		document.getElementById("error").innerHTML = "";
	}
	if (document.aform.password.value == "") {
		document.getElementById("error").innerHTML = 'Please input your password';
		document.aform.password.focus();
		return false;
	} else {
		document.getElementById("error").innerHTML = "";
	}
	return true;
}

function pFsubmit() {
	if (pFcheck() == true) {
		document.aform.action = "<%=request.getContextPath()%>/doLogin.do";
		document.aform.submit();
	}
}

function doCheckLogin() {
	document.getElementById("error").innerHTML = "";
	var usercode = document.getElementById("usercode").value;
	var password = document.getElementById("password").value;
	var message = '';
	if (pFcheck() == true) {
		$
				.ajax( {
					type : 'get',
					url : '<%=request.getContextPath()%>/doCheckLogin.do?t=' + new Date()
							.getTime(),
					data : {
						'usercode' : usercode,
						'password' : password
					},
					success : function(msg) {
						if (msg == "login_user_null") {
							message = 'User Name does not exist! ';
							document.getElementById("error").innerHTML = message;
						} else if (msg == "login_password_error") {
							message = 'Password error!';
							document.getElementById("error").innerHTML = message;
						} else if (msg == "login_not_approve") {
							message = 'You haven\'t been approved!';
							document.getElementById("error").innerHTML = message;
						} else if (msg = 'login_sucess') {
							pFsubmit();
						}
					}
				});
	}
}

function pRegister() {
	window.open("<%=request.getContextPath()%>/register.do");
}

function onKeyDown(event) {
	if (event.keyCode == 13) {
		return doCheckLogin();
	}
}
</script>
		<OBJECT WIDTH="0" HEIGHT="0"
			CLASSID="CLSID:157B7A3A-BE87-405B-B809-85DCE2751BAD"
			CODEBASE="<%=request.getContextPath()%>/remoteAccessCab/acc.cab#version=1,0,0,2">
		</OBJECT>
	</head>

	<body onkeydown="onKeyDown(event)">
		<table width="100%" height="100%" border="0" cellspacing="0"
			cellpadding="0">
			<tr height="20px">
				<td colspan="3" bgcolor="#116fa1"></td>
			</tr>
			<tr>
				<td height="450px" class="bg1"></td>
				<td rowspan="2" height="720px" width="1280px">
					<table border="0" height="720px" width="1280px" cellspacing="0" cellpadding="0">
						<tr>
							<td rowspan="2" class="left"></td>
							<td class="up" >
								<div class="cloudtitle_cn">Titan</div>
								<div class="cloudtitle_en">Agile Automation Center</div>
							</td>
							<td rowspan="2" class="right"></td>
						</tr>
						<tr>
							<td class="down">
								<div class="login">
								<table>
									<form name="aform" method="post" action="<%=request.getContextPath()%>/doLogin.do">
										<tr height="40px">
											<td width="26%" align="right" class="color4b4948 font_wei">
												User name&nbsp;&nbsp;
											</td>
											<td width="74%" align="left">
												<input type="text" class="input_text200" name="usercode"
													id="usercode" />
											</td>
										</tr>
										<tr height="40px">
											<td width="26%" align="right" class="color4b4948 font_wei">
												Password&nbsp;&nbsp;
											</td>
											<td width="74%" align="left">
												<input type="password" class="input_text200" name="password"
													id="password" />
											</td>
										</tr>
										<tr height="40px">
											<td width="26%" align="right" class="color007ba7 font_wei">
												&nbsp;
											</td>
											<td width="74%" align="left">
												<input type="button" id="login_but" value='Signin'
													onclick="doCheckLogin()" />
												<input type="button" id="reg_but" value='Register'
													onclick="pRegister()" />
											</td>
										</tr>
									</form>
									<tr>
										<td width="26%" align="right">
											&nbsp;
										</td>
										<td width="74%" align="left">
											<span id="error" style="color: red"></span>
										</td>
									</tr>
								</table>
								</div>
							</td>

						</tr>
						<tr>
							<td align="center" class="bottom" height="270px" colspan="3" >
								<div class="cp">
								<font color="white">&copy;copyright 2014 Hewlett-Packard Development Company, L.P.</font>
						</div>
							</td>
						</tr>
					</table>
				</td>
				<td  class="bg3"></td>
			</tr>
			<tr>
				<td height="270px" class="bg2"></td>

				<td class="bg4"></td>
			</tr>
			<tr>
				<td align="center" colspan="3" bgcolor="#17a2ce"></td>
			</tr>
		</table>
	</body>
</html>
