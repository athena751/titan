<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
 
<script>
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
});

function onSave() {
	saveProjectsIds();
   	doFormSubmit('userForm','doUserDetailUpdate.do?userGroupId='+document.getElementById("groupId").value)
}
function onBack() {
 	 window.history.go(-1);
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
<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
</head>

<body>
<form id="userForm" name="userForm" method="post" >
	 <#if user.userId?has_content >
			<input type="hidden" name="user.userId"  value="${(user.userId)!""}"/>
	 </#if>
<div class="port_bar"><span class="marfin_lef10">Users</span><span class="marfin_lef5">></span><span class="marfin_lef5">Users Manage</span><span class="marfin_lef5">></span>
		  <span class="marfin_lef5"><#if user.userId?has_content >
		  	User Info Update
		  <#else>
		  	User Create
		  </#if>	</span></div>
<div class="title"><img src="${base}/images/titleico.gif" width="18" height="15" class="ico" />
		   <span class="marfin_lef5">
			   <#if user.userId?has_content >
			  	User Info Update
			  <#else>
			  	User Create
			  </#if>	
		 </span></div>

<div class="showlist_main showlist_mainnopadding"></div>

<div  id="regInfo" class="box">
      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
<tr>
  <td align="right" width="10%">User Name：</td>
  <td align="left" width="90%"><input id="usercode" name="user.userCode" type="text" size="61" class="input_text" value="${user.userCode!""}" readonly="true"/></td>
</tr>
<tr>
<td align="right" width="10%">Password：</td>
<td align="left" width="90%"><input name="user.password" type="password" class="input_text" size="61" value="${user.password!""}"/></td>
</tr>
<tr>
<td align="right" width="10%">E-mail：</td>
<td align="left" width="90%"><input name="user.mail"  type="text" class="input_text" size="61" value="${user.mail!""}"/></td>
</tr>
<tr>
<td align="right" width="10%">User Group：</td>
<td align="left" width="90%">
			<select name="user.groupId" id="groupId" class="select"  style="width:400px" disabled="disabled">
        		<option value="" selected>Please Select</option>
	        	<#list groupList as group>
	             	 <option value="${group.groupId}" <#if '${group.groupId!""}'=='${user.groupId!""}'>selected</#if>>${group.groupName}</option>
	          	</#list> 
          	</select>
</td>
</tr>
<td align="right" width="10%">Role：</td>
<td align="left" width="90%">
			<select name="user.roleId" id="" class="select" style="width:400px">
        		<option value="" selected>Please Select</option>
	        	<#list roleList as role>
	             	 <option value="${role.roleId}" <#if '${role.roleId!""}'=='${user.roleId!""}'>selected</#if>>${role.roleName}</option>
	          	</#list> 
          	</select>
</td>
</tr>
<tr>
			<td align="right" width="10%">Projects:</td>
			<td align="left" width="90%">
			    <input type="hidden" name="userProjectIds" id="userProjectIds" />	
			    <table id="tblproject" width="100%">
			    <tr></tr>
			    <#if user.userProjects?has_content>
					 <#list user.userProjects as userProject >
					 <tr>
					  <td><select class='select'>
					  		<#list projectList as project>
								<option value='${project.projectId}' <#if '${project.projectId}'=='${userProject.projectId!""}'>selected</#if>>${project.projectName}</option>
							</#list>
					       </select>					  
					  </td>
					  <td><button type='button' class='bntDelTr'>Delete</button></td>
					  </tr>
					 </#list>
				</#if>
				</table>
				<button type="button" id="bntProjectAdd">Add</button>			
				<span id="projectIdMessage"></span>
			</td>
		</tr>
<tr>
<td align="right" width="10%">Remark：</td>
<td width="90%" align="left" >
<textarea name="user.remark" class="text_area">${user.remark!""}
</textarea>
</td>
</tr>

<tr>
  <td style="vertical-align:middle; text-align:center;"  colspan="2"> 
 
  	<!--start button-->
    <div  class="button" style="margin-left: 500px;">
      <div class="l"></div>
      <button class="c" type="button" onclick="onSave()">Save</button>
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
</div>
</body>
</html>
