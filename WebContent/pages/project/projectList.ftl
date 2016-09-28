<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script type="text/javascript" language="javascript" class="init">
		$(document).ready(function() {
			$('#pageTable1').DataTable( {
				dom: 'lfrtip<"clear">T',
				tableTools: {
		            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
		        }
			});
		});	
	</script>
<script>
function doDelete(){	
		var projectId = getProjectId('projectIdAry');
		$.ajax( {
				type : 'get',
				url : '${base}/project/checkProjectHasSpring.do?t=' + new Date().getTime(),
				dataType: "text",
				data : { 'projectId' : projectId },
				async: false,
				success : function(msg) {
					if (msg == "exist") {
						alert("The project cannot be deleted because there are sprints in the project!")
					}
					else{
						if(confirm('Confirm the delete?')){
							doFormSubmit('projectDoId','doProjectRemove.do');
						}
					}
				}
			});
	
}

function doUpdate(){
	
		doFormSubmit('projectDoId','goProjectUpdate.do?')
	
}


function checkBox(type,name){
	var projectIds = document.getElementsByName(name);
	var count = 0;
	if(projectIds != null && projectIds.length > 0){
     for(var i = 0 ; projectIds.length > i; i++){
        if(projectIds[i].checked){
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

function getProjectId(name){
	var projectIds = document.getElementsByName(name);
	if(projectIds != null && projectIds.length > 0){
     for(var i = 0 ; projectIds.length > i; i++){
        if(projectIds[i].checked){
        	return projectIds[i].value;
	   	}
     }
	}
}

function doSetting(){
	if(checkBox('update','projectIdAry')){
		var projectId = getProjectId('projectIdAry');
		doFormSubmit('projectDoId','${base}/project/reportaryList.do?projectId=' + projectId)
	}
}
</script>
</head>


<body>
<form id="projectDoId" action="" method="POST">
<#if navigation?has_content>
	 ${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">My Projects&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doPageRedirect('goProjectCreate.do')">
		<img src="${base}/images/icon/11.png" /><span>Add&nbsp;&nbsp;Progect</span> </a></div>
	</td><td align="right">
		<div class="table_link"><ul>
			
        </ul></div>
	</td></tr>
</table>
<div class="box">
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <!--<th width="4%">
					<input type="checkbox" id="allId" onclick="doAll(this ,'projectIdAry')" />
				</th>-->
		        <th width="10%">Name</th>
		        <th width="10%">Manager Name</th>
		        <th width="10%">Group</th>
		        <!--<th width="20%">Description</th>-->
		        <th width="10%">Action</th>
			</thead>	
		<tbody>	
         <#if projectList?has_content >
     	 <#list projectList as project>
			<tr class="mousechange">
				<!--<td align="center"><input type="hidden" id="projectIdAry" name="projectIdAry"  value="${(project.projectId)!''}" <#if '${project.projectId!""}'=='${defaultProjectId!""}'>checked="checked" </#if>/></td>-->
				<td >
				<#if '${project.projectId!""}'=='${defaultProjectId!""}'><img src="${base}/images/zTree/3.png"/>&nbsp;&nbsp;</#if> 
				<a text-align="justify" text-justify="inter-ideograph" href="#" onclick="doPageRedirect('goProjectView.do?projectId=${(project.projectId)}')">${(project.projectName)!''}</a></td>
				<td align="center">${(project.projectManger.userCode)!''}</td>
				<td align="center">${(project.group.groupName)!''}</td>
				<!--<td align="center">${(project.remark)!''}</td>-->
				<td align="center">
       	  			<!--<li><a href="#" onclick="doDelete()">Delete</a></li>-->
				    <a class="tooltips" href="#" onclick="doPageRedirect('${base}/sprint/sprintList.do?projectId=${(project.projectId)}')"><img src="${base}/images/icon/27.png" /><span>View&nbsp;&nbsp;Sprint</span></a>&nbsp;&nbsp;&nbsp;
          			<a class="tooltips" href="#"  onclick="doPageRedirect('${base}/project/goProjectUpdate.do?projectId=${(project.projectId)}')"><img src="${base}/images/icon/24.png" /><span>upadte</span> </a>&nbsp;&nbsp;&nbsp;
					<a class="tooltips" href="#" onclick="doPageRedirect('${base}/project/reportaryList.do?projectId=${(project.projectId)}')"><img src="${base}/images/icon/cog.gif" /><span>Setting</span></a>
				</td>
			</tr>
			 </#list>
 		 </#if>
 		 
 		 
      </tbody>    
        </table>
        </div>
        <!--
        <#if otherProjectList?has_content >
        <br><br>
        <table width="100%" border="0">
			<tr><td align="left">
				<div class="title">Others' Projects</div>
				</td>
			</tr>
		</table>
		<div class="box">
        <table class="display" id="pageTable1" class="display" width="100%">
          <thead>
		        <th width="10%">Name</th>
		        <th width="10%">Manager Name</th>
		        <th width="10%">Group</th>
		        <th width="30%">Description</th>
		        <th width="10%">Action</th>
			</thead>	
		<tbody>	
     	 <#list otherProjectList as otherProject>
			<tr class="mousechange">
				<td align="center"><a href="#" onclick="doPageRedirect('${base}/sprint/sprintList.do?projectId=${(otherProject.projectId)}')">${(otherProject.projectName)!''}</a></td>
				<td align="center">${(otherProject.projectManger.userCode)!''}</td>
				<td align="center">${(otherProject.group.groupName)!''}</td>
				<td align="center">${(otherProject.remark)!''}</td>
				<td align="center">
				    <a class="tooltips" href="#" onclick="doPageRedirect('${base}/sprint/sprintList.do?projectId=${(otherProject.projectId)}')"><img src="${base}/images/icon/27.png" /><span>View&nbsp;&nbsp;Sprint</span></a>
					<a class="tooltips" href="#"  onclick="doPageRedirect('${base}/project/goProjectUpdate.do?projectId=${(otherProject.projectId)}')"><img src="${base}/images/icon/24.png" /><span>upadte</span> </a>&nbsp;&nbsp;&nbsp;
					<a class="tooltips" href="#" onclick="doPageRedirect('${base}/project/reportaryList.do?projectId=${(otherProject.projectId)}')"><img src="${base}/images/icon/cog.gif" /><span>Setting</span></a>
				</td>
				</td>
			</tr>
			 </#list>
 		 
      </tbody>    
        </table>
        </#if>
        -->
	</div>
</div>
</form>
</body>
</html>
