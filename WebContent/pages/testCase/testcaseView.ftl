<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script type="text/javascript">
$(document).ready(function(){

  	 var caseType = $("#caseType").val();
  	 if(caseType=="Manual"){
  			$("#divManual").show();
  			$("#divAuto").hide();  			
  			$("#btnNext").hide();
  			$("#btnSave").show(); 
  			$("#btnCancel").show(); 
  		} else if(caseType=="Automated"){
  			$("#divManual").hide();
  			$("#divAuto").show();  			
  			$("#btnNext").show();
  			$("#btnSave").hide(); 
  			$("#btnCancel").show(); 
  		} else {
  			$("#divManual").hide();
  			$("#divAuto").hide();  			
  			$("#btnNext").hide();
  			$("#btnSave").hide(); 
  			$("#btnCancel").show(); 
  		}   	
  
});

function onBack() {
	doPageRedirect('testcaseList.do');
}
</script>
</head>

<body>
<form id="form_testcase" method="post">
<input type="hidden" id="isNew" name="isNew" value="${isNew}">
<input type="hidden" id="jsonObj" name="jsonObj"/>
<#if navigation?has_content>
	 ${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Test Case View</div>
	</td><td align="right">
	</td></tr>
</table>
<div class="box">
	<table class="tablecontent" width="100%">
		<tr>
			<td class="tablecontent_title" colspan="2"><div class="sub_title">General</div></td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Testcase Code：</td>
			<td>${testcase.caseCode!""}</td>
			<input id="testcaseId" type="hidden" name="testcase.caseId"  value="${(testcase.caseId)!""}"/>
			<input id="testcaseCode" type="hidden" name="testcase.caseCode"  value="${(testcase.caseCode)!""}"/>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				<font color="red">*</font>Testcase Name：
			</td>
			<td align="left" width="90%">${testcase.caseName!""}</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				<font color="red">*</font>Type：
			</td>
			<td align="left" width="90%">
				<select class="select" id="caseType" name="testcase.type" disabled="disabled">
					<option value=""></option>
					<option value="Manual" <#if 'Manual'=='${testcase.type!""}'>selected</#if>>Manual</option>
					<option value="Automated" <#if 'Automated'=='${testcase.type!""}'>selected</#if>>Automated</option>
				</select>
				<span id="caseTypeMessage" color="red"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				<font color="red">*</font>Owner：
			</td>
			<td align="left" width="90%">
				<select class="select" id="owner" name="testcase.ownerId" disabled="disabled">
					<option value=""></option>
		        	<#list userList as user>
		        	<option value="${user.userId}" <#if '${user.userId!""}'=='${testcase.ownerId!""}'>selected</#if>>${user.userCode}</option>		            
		          	</#list>
				</select>
				<span id="ownerMessage" color="red"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				<font color="red">*</font>Project：
			</td>
			<td align="left" width="90%">
				<select class="select" id="project" name="testcase.projectId" disabled="disabled">
					<option value=""></option>
		        	<#list projectList as project>
		        	<option value="${project.projectId}" <#if '${project.projectId!""}'=='${testcase.projectId!""}'>selected</#if>>${project.projectName}</option>
		          	</#list>
				</select>
				<span id="projectMessage" color="red"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Module：</td>
			<td align="left" width="90%">
				<select class="select" id="module" name="testcase.moduleId" disabled="disabled">
					<option value=""></option>
					<#if moduleList?has_content >
					<#list moduleList as module>
		        	<option value="${module.moduleId}" <#if '${module.moduleId!""}'=='${testcase.moduleId!""}'>selected</#if>>${module.moduleName}</option>
					</#list>
					</#if>
				</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Time Out<br/>(Default is 20 mins)</td>
			<td align="left" width="90%">
				${testcase.timeout!""}&nbsp mins
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">${testcase.description!""}</td>
		</tr>
	</table>
	<div id="divManual" style="display:none">
	<table class="tablecontent" width="100%">
		<tr>
			<td class="tablecontent_title" colspan="2"><div class="sub_title">Manual Case</div></td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Test Step：</td>
			<td align="left" width="90%">
				${testcase.testStep!""}
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Expectation：</td>
			<td align="left" width="90%">
				${testcase.expectation!""}
			</td>
		</tr>
	</table>
	</div>
	<div id="divAuto" style="display:none">
	<table class="tablecontent" width="100%">
		<tr>
			<td class="tablecontent_title" colspan="2"><div class="sub_title">Automated Case</div></td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Dedicated Server:</td>
			<td align="left" width="90%">
				<select class="select" id="dserver" name="testcase.dedicatedserverId"  onChange="dServerChange()" disabled="disabled">
					<option value=""></option>
					<#if dedicatedServerList?has_content >
					<#list dedicatedServerList as server>
		        	<option value="${server.serverId}" <#if '${server.serverId!""}'=='${testcase.dedicatedserverId!""}'>selected</#if>>${server.serverName}(${server.serverHostname})</option>
					</#list>
					</#if>
				</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Running on:</td>
			<td align="left" width="90%">
				<select class="select" id="server" name="testcase.runonserverData" onChange="serverChange()" disabled="disabled">
					<option value=""></option>
					<option value=''selected >Please Select</option>
					<#list serverdataList as serverdata>
						<option value='${serverdata.paradataName}' <#if '${serverdata.paradataName!""}'=='${testcase.runonserverData!""}'>selected</#if>>${serverdata.paradataName}</option>
					</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Command：</td>
			<td align="left" width="90%">
				${testcase.command!""}
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Parameter：</td>
			<td align="left" width="90%">
				<table id="tblPrmIn" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="5%">Rank</td>
						<td width="20%">Parameter</td>
						<td width="10%">Value</td>
						<td>Description</td>
						
					</tr>
					<#if paramList?has_content>
					 <#list paramList as parameter >
						<#if parameter.type=="INPUT">
					 <tr>
					  <td style='display:none'><input type='hidden' value="${(parameter.paraId)}"></input></td>
					  <td>${parameter.paraOrder!""}</td>
					  <td>${parameter.paraName!""}</td>
					  <td><select class='select' disabled="disabled">
					  		<#list parameterdataList as parameterdata>
								<option value='${parameterdata.paradataName}' <#if '${parameterdata.paradataName}'=='${parameter.paraValue!""}'>selected</#if>>${parameterdata.paradataName}</option>
							</#list>
					       </select>					  
					  </td>
					  <td>${parameter.description!""}</td>				
					 
					  </tr>
					  	</#if>
					 </#list>
					</#if>
				</table>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Return：</td>
			<td align="left" width="90%">
				<table id="tblPrmOut" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="10%">Type</td>
						<td width="20%">Parameter</td>
						<td>Description</td>
					</tr>
					<#if paramList?has_content>
					 <#list paramList as parameter >
						<#if parameter.type!="INPUT">
					 <tr>
					  <td style='display:none'><input type='hidden' value="${(parameter.paraId)}"></input></td>
					  <td><select class='select'>
					        <option value='PASS' <#if 'PASS'=='${parameter.type!""}'>selected</#if>>PASS</option>
					        <option value='FAIL' <#if 'FAIL'=='${parameter.type!""}'>selected</#if>>Other</option>
					  </select></td>
					  <td><input class='input_text' type='text' value="${parameter.paraName!""}"></input></td>
					  <td><input class='input_text' type='text' value="${parameter.description!""}"></input></td>				
					 
					  </tr>
					  	</#if>
					 </#list>
					</#if>
				</table>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Log：</td>
			<td align="left" width="90%">
				<table id="tblLog" width="100%">
					<tr>
						<td style='display:none'></td>
						<td width="10%">Log Name</td>
						<td width="20%">Log Location</td>
						<td>Description</td>
						
					</tr>					
					<#if logList?has_content>
					 <#list logList as log >
					 <tr>
					  <td style='display:none'><input type='hidden' value="${(log.logId)}"></input></td>
					  <td><input class='input_text' type='text' value="${log.logName!""}"></input></td>
					  <td><input class='input_text' type='text' value="${log.logLocation!""}"></input></td>
					  <td><input class='input_text' type='text' value="${log.remark!""}"></input></td>
					  
					  </tr>
					 </#list>
					</#if>
				
				</table>
			</td>
		</tr>
	</table>
	</div>
	<table class="tablecontent" width="100%">
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Notes：</td>
			<td align="left" width="90%">
				${testcase.remark!""}
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">In Test plan：</td>
			<td align="left" width="90%">
			  	<#list testplanList as tp>
			  	 ${tp.testplanName}
			  	 <#if '${tp.owner!""}'=='${currentUserName!""}'||'${isAdmin!""}'=='1'>
			  	 <a class="tooltips" href="#" onclick="doPageRedirect('goTestplanEdit.do?testplanId=${(tp.testplanId)}')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;&nbsp;</span></a>;&nbsp;&nbsp;&nbsp;&nbsp;
			  	 </#if>
		        </#list>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">In Test job：</td>
			<td align="left" width="90%">
			    <#list testjobList as tj>
			       ${tj.testjobName}
			        <#if '${tj.owner!""}'=='${currentUserName!""}'||'${isAdmin!""}'=='1'>
					    <a class="tooltips" href="#" onclick="doPageRedirect('goTestjobEdit.do?testjobId=${(tj.testjobId)}')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;&nbsp;</span></a>;&nbsp;&nbsp;&nbsp;&nbsp;
		            </#if>
		        </#list>
			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr><td align="center">
			<button type="button" id="btnCancel" onclick="onBack()">Return To Case List</button>
		</td></tr>
	</table>
</div>
</form>
</body>
</html>
