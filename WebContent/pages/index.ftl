<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/stickymenu.blue.css" />
<script language="javascript" src="${base}/js/jquery.stickymenu.js"></script>

<script type="text/javascript"> 
$(document).ready(function() {
	$('#smenubar').stickyMenubar({
		floatingDiv: $('#optionbar'),
		floatingDivBackground: true,
		onLeaveTop: function() {
			$('.top_menu').fadeIn();
			$('#header').addClass('margin_bottom'); 
		},
		onScroll: function() {
			$('.top_menu').fadeOut(); 
			$('#header').removeClass('margin_bottom'); 
		}
	});
});

function setTab(name,cursel,n){
	for(i=1;i<=n;i++){ 
		var menu=document.getElementById(name+i); 
		menu.className=i==cursel?"navhover":"navnormal"; 
	} 
}
function changeTab(url){
	document.getElementById('port_frame').src=url;
}
function aboutTitan(){
	jBox.alert("<div class='cloudtitle_en' style='color: #888;'>Titan</div><br><div>Verson: 2.0</div><br><div><a href='#' onclick='showHistory()'>Update History</a></div><br><div>Help</div>");
}
function showHistory(){
	jBox.open("iframe:${base}/auth/goHistory.do", null, 800, 350, { buttons: { 'Close': true },iframeScrolling: 'yes', showClose: false
            });
	
}
//Let the height change with the browser
function getContentSize() {
            var wh = document.documentElement.clientHeight;
            var eh = 145;
            ch = (wh - eh) + "px";
            document.getElementById( "middle_frame" ).style.height = ch;
        }
        window.onload = getContentSize;
        window.onresize = getContentSize;
</script>
</head>

<body>
<div id="main">
 <div class="banner">
    <div class="logtime">
      	Welcome&nbsp;<a style="color:#3ba7fa;" href="#" onclick="changeTab('${base}/auth/goUserDetail.do?userId=${loginUser.userId}')">${loginUser.userCode}</a>
      	&nbsp;&nbsp;&nbsp;
      	Points : <a id="points" style="color:#3ba7fa;" href="#" onclick="changeTab('${base}/mytitan/myPointList.do')">${point}</a>
      	&nbsp;&nbsp;&nbsp;
      	EXP :  ${exp}
      	&nbsp;&nbsp;&nbsp;
      	<img src="${base}/images/icon/47.png" width="15px" height="15px" /><a id="inbox" style="color:#8f9191;" href="#" onclick="changeTab('${base}/mytitan/inboxList.do')">(${inbox})</a>
      	&nbsp;&nbsp;&nbsp;
      	Sign In Time：${loginUser.loginTime?string('yyyy-MM-dd HH:mm')}
      	&nbsp;&nbsp;&nbsp;
      	<a style="color:#8f9191;" href="${base}/doLogout.do"><img src="${base}/images/Log_Out.png" width="15px" height="15px" />&nbsp;Log Out</a> 
      </div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="logo_style">
	  <tr>
	    <td width="60px"><img src="${base}/images/HP-Logo.png" width="70" height="70" /></td>
	    <td width="350px" class="logo_style_title">Titan 2.0</td>
	    <td >
			<div id="optionbar">
			    <div>
			    &nbsp;&nbsp;
			     <ul id="smenubar">
			       <#list userMenuList as userMenu>
				       <li id="tab${userMenu_index+1}" onclick="setTab('tab',${userMenu_index+1},'${userMenuList?size}'); <#if userMenu.menu2List?size==0>changeTab('${base}/${userMenu.menu1.actionUrl!""}')<#else>changeTab('${base}/${userMenu.menu1.actionUrl!""}')</#if>">
				       		<a href="#"><@s.text name="${userMenu.menu1.menuCode!''}"/></a>
			            	<ul style="Opacity:0.7;">
			            		<li>
								  <#list userMenu.menu2List as userMenu2>
									  <#if userMenu2.menu2.actionUrl?has_content>
											<a href="${base}${userMenu2.menu2.actionUrl!''}" target="mainframe">
												<@s.text name="${userMenu2.menu2.menuCode!''}"/>
											</a>
									   <#else>
											<@s.text name="${userMenu2.menu2.menuCode!''}"/>
									  </#if>
								  </#list>
								</li>
			                </ul>
			            </li>   
			       </#list>  
			     </ul> 
			    </div>
			</div><!--end header-->
		</td>
	  </tr>
	</table>
  	
      
     <!-- <div class="sysbtn">
       	<ul>
          <li><a href="#" onclick="aboutTitan()"><img src="${base}/images/b_6.gif" width="9" height="9" />About</a></li>
          <li><a href="#" onclick="changeTab('${base}/auth/goUserDetail.do?userId=${loginUser.userId}')"><img src="${base}/images/b_6.gif" width="9" height="9" />My Account</a></li>
		  <li class="spanimg" style=" background-image:none; border:none"><img src="${base}/images/b_7.gif" width="11" height="13" /></li>
 		  <li><a href="${base}/doLogout.do"><img src="${base}/images/Log_Out.png" width="11" height="12" />Log Out</a></li>
 		  <li><a href="javascript:document.frames('mainframe').location.reload();"><img src="${base}/images/b_4.gif" width="12" height="12" />Refresh</a></li>
 		  <li><a href="javascript:window.history.go(1);"><img src="${base}/images/b_3.gif" width="13" height="10" />Forward</a></li>
 		  <li><a href="javascript:window.history.go(-1);"><img src="${base}/images/b_2.gif" width="13" height="10" />Back</a></li>
		  <#if menuAuth.isAuth("MENU_MAINPAGE") >
		  <li><a href="#" onclick="changeTab('${base}/home.do')"><img src="${base}/images/b_1.gif" width="11" height="10" />Home</a></li>
          </#if>
        </ul>
      </div>-->
    
  </div>
  <div id="middle_frame">
  <iframe class="port_frame" name="mainframe" id="port_frame" frameborder="0" onload="javascript:{dyniframesize('port_frame');}" scrolling="auto" src="">
  </iframe><!--end port_frame--></div>
<script type="text/javascript"> 
	<#if userMenuList?has_content>
		setTab('tab',1,'1');
		<#if userMenuList[0].menu2List?size==0>
			changeTab('<#if userMenuList?has_content>${base}/${userMenuList[0].menu1.actionUrl!""}<#else>${base}/home.do</#if>')
		<#else>
			changeTab('<#if userMenuList?has_content>${base}/${userMenuList[0].menu1.actionUrl!""}<#else>${base}/home.do</#if>');
		</#if>
	</#if>
</script>

<div class="footer">
	<table width="100%" height="100%">
	<tr><td align="center">
	<font color="#8b8d8d">&copy;copyright 2014 Hewlett-Packard Development Company, L.P.&nbsp;&nbsp;|&nbsp;&nbsp;
		<a style="color:#8b8d8d;" href="#" onclick="aboutTitan()">About Titan</a>
	</font></td></tr>
</div>
</div>
</body>
</html>