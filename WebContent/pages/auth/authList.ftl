<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">

<script src="${base}/js/dhtml/mktree.js" type="text/javascript"></script>
<link href="${base}/js/dhtml/mktree.css" rel="stylesheet" type="text/css">

<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>
<#assign authIndex = 1>
<link rel="Stylesheet" type="text/css" href="${base}/css/jquery.jmodal.css" />
<script type="text/javascript" src="${base}/js/jquery.jmodal.js"></script>
<script>
function doSearch(){
	if(checkBox('menu','menuIdAry')){
			doFormSubmit('authForm','authList.do');
	}
}


function doDelete(){
	if(checkBox('delete','authIdAry')){
		if(confirm('Confirm the delete?')){
			doFormSubmit('authForm','doAuthRemove.do');
		}
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
	 if(type=='menu' &&count == 0){
		 alert('Please Select a Menu\!');
		 return false;
	 }else if (type=='delete' && count == 0){
	 	 alert('Please Select Need To Delete Right!');
	 	 return false;
	 }else if (type=='update' && count > 1){
	 	 alert('Please choose only one!');
	 	 return false;
	 }else{
	 	 return true;
	 }
}


function doCreateAuth(){
	if(checkBox('menu','menuIdAry')){
			jsCreateAuth();
	}
}

function jsCreateAuth()
{
 
var authName;
var authDesc;
var authNote;
var menuCode;
var menuName;
$.fn.jmodal({
    title: 'Right Create',
    content: function(body) {
    body.html('Right Code:<input type="text" id="nameInput" /><br />Right Name:<input type="text" id="descInput" > <br />Right Notes:<textarea  id="noteInput" />');
    },
    buttonText: {ok: 'Confirm', cancel: 'Cancel' },
    okEvent: function(data, args) {
       	authName = $('#nameInput');
    	authDesc = $('#descInput');
    	authNote = $('#noteInput');
    	
   		if($.trim(authName.val())=='' || $.trim(authDesc.val())=='' || $.trim(authNote.val())=='' ){
    		args.isCancelling=true;
        	alert('Please fill in!');
    	}
    	else{
    		args.isCancelling=false;
    		document.getElementById("authName").value = authName.val();
    		document.getElementById("authDesc").value = authDesc.val();
    		document.getElementById("authNote").value = authNote.val();
    	 	doFormSubmit('authForm','doAuthSave.do');
    	}
    }
});
                
}



 


	// 修改权限
	function doUpdate()
	{	
 
		var obj = $(":radio[name='authIdAry'][checked:true]");
		if(obj.length != 1) {
			alert("Please Select Need To Update Right!");
			return;
		}
		//构造JSON对象
		var software = {"authId":obj[0].value}
		 
		$.ajax({
			url: "${base}/auth/selectAuthInfo.do",
			type: "POST",
			cache: false, //清除缓存
			data: software, //提交的数据
			dataType: "json", //返回的数据类型为json
			success: function(data) { //成功	
				update(data);	
			},
			error: function() {
				alert("Search Right Fail!");
			} 
		});             
	}
	
	// 修改权限页面
	function update(data) {
 
		var authName;
		var authDesc;
		var authNote;
		var menuCode;
		var authId;
		$.fn.jmodal({
		    title: 'Right Update',
		    width: 500,
		    content: function(body) {
		    	body.html('Right Code:<input type="text" id="nameInput" /><br />Right Name:<input type="text" id="descInput" > <br />Remark:<textarea  id="notesInput" />');
		    	{
		    		$('#nameInput').val(data.authName);
		    		$('#descInput').val(data.authDesc);
		    		$('#notesInput').val(data.remark);
		    		$('#strCreateDate').val(data.createDate.toString());
		    		$('#createUser').val(data.createUser);
		    		authId = data.authId;
		    	}
		    },
		    buttonText: { ok: 'Confirm', cancel: 'Cancel' },
		    okEvent: function(data, args) {
		       	authName = $.trim($('#nameInput').val());
		    	authDesc = $.trim($("#descInput").val());
		    	authNote = $.trim($('#notesInput').val());
		    	
		   		if(authName == '' || authDesc == ''  ){
		    		args.isCancelling=true;
		        	alert('Please fill in!');
		    	}
		    	else{
		    		args.isCancelling=false;
		    		$("#authName").val(authName);
		    		$("#authDesc").val(authDesc);
		    		$("#authNote").val(authNote);
		    		$("#authId").val(authId);
		    		
		    		doFormSubmit('authForm','doAuthSave.do','');
		    	}
		    }
		});	
	}
	
//查询后显示选择的菜单	
function expandSelectItem(){
	var obj = $(":radio[name='menuIdAry'][checked:true]");
	var menuId = obj[0].value
	expandToItem('tree1',menuId);
}	
</script>




</head>

<body >
 
<form  id="authForm" name="authForm" method="post" action="">
	<input id="authId" 	 name="auth.authId" 	type="hidden"/>
	<input id="authName" name="auth.authName" 	type="hidden"/>
	<input id="authDesc" name="auth.authDesc" 	type="hidden"/>
	<input id="authNote" name="auth.remark"		type="hidden"/>
	<input id="strCreateDate" name="authVo.strCreateDate" type="hidden"/>
	<input id="createUser" name="auth.createUser" type="hidden"/>
	
	
<div class="title"><img src="${base}/images/titleico.gif" width="18" height="15" class="ico" />Rights Manage</div>


<table width="100%"  border="0">
<tr>
<td width="20%" valign="top">
<div class="box">
<div class="table_link" align="right">
      <button class="c" type="button" onclick="expandTree('tree1')">Open</button>
       <button class="c" type="button" onclick="collapseTree('tree1')">Close</button>
     </div>
        <div class="ro">
        <table width="100%"   border="0" cellpadding="0" cellspacing="0">
        
                           <tr >
                          	   <td WIDTH="40%"  ALIGN="LEFT" VALIGN="TOP">      
     								<div style="min-height:360px">
		 							<ul class="mktree" id="tree1">
								
												 <#list meunTreeList as menuTree>
													<li><a herf="###" onclick="doSearch()"><input  type="radio" name="menuIdAry" <#if '${menuIdAry!""}'=='${menuTree.menu1.menuId!""}'>checked</#if> value="${menuTree.menu1.menuId!""}"/></a>${menuTree.menu1.menuName!""}
														<#if menuTree.menu2List?has_content>
															<ul>
																<#list menuTree.menu2List as menu2Tree>
															   		
						 											 		<li id='${menu2Tree.menu2.menuId!""}'><a herf="###" onclick="doSearch()"><input  type="radio" name="menuIdAry" <#if '${menuIdAry!""}'=='${menu2Tree.menu2.menuId!""}'>checked</#if> value="${menu2Tree.menu2.menuId!""}"/></a>
						 											 	
						 											 	${menu2Tree.menu2.menuName!""}
						 											 	<#if menu2Tree.menu3List?has_content>
							 											 	<ul>
																				<#list menu2Tree.menu3List as menu3Tree>
																			   		 <li id="${menu3Tree.menuId!""}"><a herf="###" onclick="doSearch()"><input  type="radio" name="menuIdAry" <#if '${menuIdAry!""}'=='${menu3Tree.menuId!""}'>checked</#if> value="${menu3Tree.menuId!""}"/></a>${menu3Tree.menuName!""}
																			   		 		
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
</div>
</td>
<td width="5%"></td>
<td width="75%" valign="top" align="right">
		<#--doPageRedirect('goAuthCreate.do')-->
		<div class="table_link">
				<a class="tooltips" href="#"  onclick="doPageRedirect('authList.do')">
				<img src="${base}/images/icon/advanceSearch.png" /><span>Search&nbsp;&nbsp;All</span> </a>&nbsp;&nbsp;&nbsp;
				<a class="tooltips" href="#"  onclick="doCreateAuth()">
				<img src="${base}/images/icon/11.png" /><span>Add</span> </a>&nbsp;&nbsp;&nbsp;
				<a class="tooltips" href="#"  onclick="doUpdate()">
				<img src="${base}/images/icon/24.png" /><span>upadte</span> </a>&nbsp;&nbsp;&nbsp;
				<a class="tooltips" href="#"  onclick="doDelete()">
				<img src="${base}/images/icon1/er_s.gif" /><span>Delete</span> </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <!--<ul>
        	<#if menuAuth.isAuth("BTN_RIGHT_CUD") >
        		<li><a href="#" onclick="doDelete()">Delete</a></li>
          		<li><a href="#" onclick="doUpdate()">Update</a></li>
          		<li><a href="#" onclick="doCreateAuth()">Add</a></li>
          	</#if>
          	<li><a href="#" onclick="doPageRedirect('authList.do')">Search All</a></li>
        </ul>--></div>
        <br>
        
					      <table id="pageTable" width="100%"  border="0" cellpadding="0" cellspacing="0" class="class="display"">
					      	
					         <thead>
							        <th width="5%" >
										<!--<input type="radio" id="allId" onclick="doAll(this ,'authIdAry')" />-->
									</th>
							        <th width="30%">Right Code</th>
							        <th width="25%">Right Name</th>
							        <th width="25%">Right Notes</th>
							   		<th width="15%">Right Menu</th>
							 </thead>
							 <tbody>
							<#if authList?has_content >
					     	 <#list authList as auth>
								<tr class="table_padding" class="mousechange">
									<td align="left"><input type="radio" id="authIdAry" name="authIdAry"  value="${(auth.authId)!''}"/></td>
									<td align="left">${(auth.authName)!''}</td>
									<td align="left">${(auth.authDesc)!''}</td>
									<td align="left">${(auth.remark)!''}</td>
									<td align="left">${(auth.menu.menuName)!''}</td>
								</tr>
								 <#assign authIndex = authIndex + 1>
								 </#list>
								 	 </#if>
										<#if authIndex<1>
											<#list authIndex..10 as i>
												<tr>
												 
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
												</tr>
											</#list>
										 </#if>
					 		 </tbody>
					        </table>
 </td></tr>
</table>
</form>
</body>
</html>
