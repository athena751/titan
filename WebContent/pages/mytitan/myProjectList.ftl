<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
function changeDefaultProject(projectId){
	doFormSubmit('projectDoId','goDefaultProjectChange.do?projectId=' + projectId);
}
</script>
</head>

<body>
<form id="projectDoId" action="" method="POST">
<#if navigation?has_content>
	 ${navigation}
</#if>
<table width="100%" border="0">
	<tr>
		<td align="left">
			<div class="title">My Projects</div>
		</td>
	</tr>
</table>
<div class="box">
      <table class="tlist" id="pageTable" width="100%">
          <thead>
          		<th width="3%"></th>
		        <th width="10%">Name</th>
		        <th width="10%">Manager Name</th>
		        <th width="10%">Group</th>
		        <th width="30%">Description</th>
		        <th width="10%">Action</th>
			</thead>	
		<tbody>	
         <#if projectList?has_content >
     	 <#list projectList as project>
			<tr class="mousechange">
				<td align="center"><#if '${project.projectId!""}'=='${defaultProjectId!""}'><img src="${base}/images/zTree/3.png"/></#if></td>
				<td align="center">${(project.projectName)!''}</td>
				<td align="center">${(project.projectManger.userCode)!''}</td>
				<td align="center">${(project.group.groupName)!''}</td>
				<td align="center">${(project.remark)!''}</td>
				<td align="center"><div class="table_link">
				    <input type="button" onclick="changeDefaultProject('${(project.projectId)}')" <#if '${project.projectId!""}'=='${defaultProjectId!""}'>disabled="disabled"</#if> value="Save As Default"></input>
				</div></td>
			</tr>
			 </#list>
 		 </#if>
      </tbody>    
        </table>
        </div>
	</div>
</div>
</form>
</body>
</html>
