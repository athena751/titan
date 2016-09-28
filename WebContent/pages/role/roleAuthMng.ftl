<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script src="${base}/js/dhtml/mktree.js" type="text/javascript"></script>
<link href="${base}/js/dhtml/mktree.css" rel="stylesheet" type="text/css">
<#import "/commons/pagerSorter.ftl" as pagerSorter>

<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage sortLeft="0" sortRight="1"/>
<#assign authIndex = 1>
<script>
 
function doSearch(){
	if(checkBox('','menuIdAry')){
			doFormSubmit('authForm','doSearchMenuAuth.do');
	}
} 	

function doSearchAll(){
			doFormSubmit('roleAuthForm','goRoleAuthMng.do');
}
	  
function doSave(){
	if(checkBox('selMenu','menuIdAry')){
		 doFormSubmit('authForm','doSaveMenuRoleAuth.do')
		 
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
	 if(type=='delete' && count == 0){
		 alert('Please select from permissions from the user already has at least one!');
		 return false;
	 }else if(type=='addAuth' && count == 0){
	 	 alert('Please select from the list of permissions at least one!');
		 return false;
	 }else if(type=='selMenu' && count == 0){
	 	 alert('Please Select Menu To Update Right!');
		 return false;
	 }else{
	 	 return true;
	 }
}


//查询后显示选择的菜单	
function expandSelectItem(){
	var menuIds = document.getElementsByName("menuIdAry");
	if(menuIds != null && menuIds.length > 0){
    	for(var i = 0 ; menuIds.length > i; i++){
      	  if(menuIds[i].checked){
	 	  	expandToItem('tree1',menuIds[i].value);
	 	  }
		}
	}
}	

function doBack(){
 	history.go(-1); 
}
</script> 
<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
</head>

<body>
<form id="roleAuthForm" name="roleAuthForm" method="post" action="">
	<input  name="roleIdAry" 	type="hidden" value="${role.roleId!""}"/>
</form>
<form  id="authForm" name="authForm" method="post" action="">
<input id="roleId" 	 name="roleId" 	type="hidden" value="${role.roleId!""}"/>
<div class="port_bar"><span class="marfin_lef10">Users</span><span class="marfin_lef5">></span><span class="marfin_lef5">Roles Manage</span>></span><span class="marfin_lef5">Assign Right</span></span></div>
<div class="title"><img src="${base}/images/titleico.gif" width="18" height="15" class="ico" />Role：${role.roleDesc!""}  Assign Right</div>

<div class="showlist_main showlist_mainnopadding"></div>

<table width="100%"  class="fixedtable" border="0" cellpadding="0" cellspacing="0">
<tr>
<td width="20%" valign="top">
<div class="box">
<div class="table_link" align="right">
      <button class="c" type="button" onclick="expandTree('tree1')">Open</button>
       <button class="c" type="button" onclick="collapseTree('tree1')">Close</button>
     </div>
		<div class="ro">	      
  
  <div class="clear"></div>
        <table width="100%"   border="0" cellpadding="0" cellspacing="0"  class="tlist">
                           <tr >
                          	   <td WIDTH="40%"  ALIGN="LEFT" VALIGN="TOP">      
     								<div style="min-height:360px">
		 							<ul class="mktree" id="tree1">
								
												 <#list meunTreeList as menuTree>
													<li  id="${menuTree.menu1.menuId!""}" ><a herf="###" onclick=""><input  type="checkbox" name="menuIdAry" <#if menuTree.menu1.ischeckedMenu==true>checked</#if> value="${menuTree.menu1.menuId!""}"/></a>${menuTree.menu1.menuName!""}
														<#if menuTree.menu2List?has_content>
															<ul>
																<#list menuTree.menu2List as menu2Tree>
						 												<li id='${menu2Tree.menu2.menuId!""}' ><a herf="###" onclick=""><input  type="checkbox" name="menuIdAry" <#if menu2Tree.menu2.ischeckedMenu==true>checked</#if> value="${menu2Tree.menu2.menuId!""}"/></a>
						 											 	${menu2Tree.menu2.menuName!""}
						 											 	<#if menu2Tree.menu3List?has_content>
							 											 	<ul>
																				<#list menu2Tree.menu3List as menu3Tree>
																			   		 <li id="${menu3Tree.menuId!""}" ><a herf="###" onclick=""><input  type="checkbox" name="menuIdAry" <#if menu3Tree.ischeckedMenu==true>checked</#if> value="${menu3Tree.menuId!""}"/></a>${menu3Tree.menuName!""}
																			   		 </li>
										 										</#list> 
																			</ul>
																		</#if>
																		</li>
						 										</#list> 
															</ul>
														</#if>		
													</li>
												  </#list> 
												 
											</ul>
										</div>
     							</td>
     						</tr>	
     					</table>
 </div>
</div>
</td>
<td width="5%" ></td>
<td width="75%" valign="top">
<div class="box" align="right">
<div class="table_link" align="right">
		<button class="c" type="button" onclick="doSave()">Save</button>
		<button class="c" type="button" onclick="doSearchAll()">right you have</button>
		<button class="c" type="button" onclick="doSearch()">right need to distribute</button>
        
          	<#--<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>
          	 <li><a href="#" onclick=""><img src="${base}/images/expand-all.gif" width="16" height="16" class="ico" />全选</a></li>-->
       </div>
		<div class="ro">	      
		<#--doPageRedirect('goAuthCreate.do')-->
		
        <div class="clear"></div>
        <br>
					      <table id="table" width="100%"  border="0" cellpadding="0" cellspacing="0" class="tlist">
					      	
					         <thead>
							        <th width="4%" >
										<!--<input type="checkbox" id="allId" onclick="doAll(this ,'authIdAry')" />-->
									</th>
							        <th width="27%" align="left">Right Code</th>
							        <th width="26%" align="left">Right Name</th>
							        <th width="27%" align="left">Right Notes</th>
							   		<th width="16%" align="left">Right Menu</th>
							 </thead>
							 <tbody>
							<#if authList?has_content >
					     	 <#list authList as auth>
								<tr class="table_padding mousechange2">
									<td align="center">
											<input type="checkbox"  name="authIdAry" <#if auth.ischecked==true>checked</#if> value="${(auth.authId)!''}"/>
									</td>
									<td align="left">${(auth.authName)!''}</td>
									<td align="left">${(auth.authDesc)!''}</td>
									<td align="left">${(auth.remark)!''}</td>
									<td align="left">${(auth.menu.menuName)!''}</td>
								</tr>
								<#-- <#assign authIndex = authIndex + 1>-->
								 </#list>
								 	 </#if>
										<#--<#if authIndex<11>
											<#list authIndex..10 as i>
												<tr>
												 
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
												</tr>
											</#list>
										 </#if>-->
					 		 </tbody>
					          
					        </table>

	</div>
</div>
 </td></tr>
</table>
</form>
</body>
</html>
