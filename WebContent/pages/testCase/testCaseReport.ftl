<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script type="text/javascript">
function getReport(){
   var projectId = document.getElementById("projectId").value;
   doPageRedirect('goTestcaseReport.do?projectId='+projectId);
}

</script>
</head>

<body>
<#if navigation?has_content>
	 ${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Test Case Report
		</div>
	</td></tr>
	<tr>
	<td align="right" width="80%">
	    Select Project :
		<select id="projectId" name="projectId"  class="select" style="width:180px" onChange="getReport()">
			<option value="" selected>Please Select</option>
			<#list projectList as project>
				<option value="${project.projectId}"<#if '${projectId!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
			</#list> 
		</select>
	</td>
	</tr>
</table>
<form id="testcaseDoId" action="${base}/test/testcaseList.do" method="POST">
    <table class="display" id="pageTable" width="100%">
		<thead>
			<th width="8%">CaseID</th>
			<th width="10%">Name</th>
			<th width="10%">Total</th>
			<th width="10%">Pass</th>
			<th width="10%">Fail</th>
			<th width="10%">Pass%</th>
			<th width="20%">Fail%</th>

		</thead>        
	<#if caseRepVoList?has_content >
	    <#list caseRepVoList as caseRep>
			<tr class="mousechange">
				<td align="center">${(caseRep.caseCode)!''}</td>
				<td align="center"><a href="#" onclick="doPageRedirect('goTestcaseView.do?caseId=${(caseRep.caseID)}')">${(caseRep.caseName)!''}</a></td>
				<td align="center">${(caseRep.caseRunTotal)!''}</td>
				<td align="center">${(caseRep.caseRunPass)!''}</td>
				<td align="center">${(caseRep.caseRunFail)!''}</td>
				<td align="center">${(caseRep.casepassCent)!''}</td>
				<td align="center">${(caseRep.casefailCent)!''}</td>
				
			</tr>
	    </#list>
		
 	</#if>      
	</table>
</form>
</body>
</html>
