<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script type="text/javascript">
$(document).ready(function(){

	//hide or show manual and auto div
	$("#caseType").change(function(){
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
  	
  	
  	//add tr for table
  	var rows = document.getElementById("tblPrmIn").rows.length;
  	var paramIndex = rows-1;
  	var paramHtml = '';
	$("#bntPrmInAdd").click(function(){
		if(paramHtml == ''){
			var projectId = $("#project").val();
			$.ajax( {
				type : 'get',
				url : 'getParameterData.do',
				dataType: "text",
				data : { 'projectId' : projectId },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				async: false,
				success : function(data) {
					if(data.length != 0){
						paramHtml = "<td><input class='input_text' type='text'></input></td>"
						+ "<td>"
						+ "<select class='select'>"
						+ "<option value=''selected >Please Select</option>";
						for(var i=0; i<data.length; i++){
							paramHtml = paramHtml + "<option value='"
							+ data[i].paradataName 
							+ "'>"
							+ data[i].paradataName 
							+ "</option>";
						}
						paramHtml = paramHtml + "</select>"
						+ "</td>";
					}
				}
			});
		}
		paramIndex++;
		var newTr = "<tr><td style='display:none'><input type='hidden' value=''></input></td><td>"+paramIndex+"</td>"
					+ paramHtml
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		$("#tblPrmIn tr:last").after(newTr);
  	});
	$("#bntPrmOutAdd").click(function(){
		var newTr = "<tr><td style='display:none'><input type='hidden'></td>"
					+ "<td><select class='select'>"
					+ "<option value='PASS'>PASS</option>"
					+ "<option value='FAIL'>Other</option></select></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		$("#tblPrmOut tr:last").after(newTr);
  	});
	$("#bntLogAdd").click(function(){
		var newTr = "<tr><td style='display:none'><input type='hidden'></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		$("#tblLog tr:last").after(newTr);
  	});
  	
  	//delete tr for table
 	$("#divAuto").delegate(".bntDelTr", "click", function(){
		$(this).closest("tr").remove();
  	});
  	
  	//check input data
	$("#caseName").blur(function(){
	  	var caseName = document.getElementById("caseName").value;
	  	if('' == caseName){
	  		$("#caseNameMessage").html('Please provide a Testcase name!');
	  	}else{
	  		$.ajax( {
				type : 'get',
				url : 'checkTestcaseName.do?t=' + new Date().getTime(),
				dataType: "text",
				data : { 'caseName' : caseName },
				success : function(msg) {
					if (msg == "exist") {	
						$("#caseNameMessage").html('The name has been used!');
						document.getElementById("caseName").focus();
					}else{
						$("#caseNameMessage").html('');
					}
				}
			});
	  	}
	});
	$("#owner").change(function(){
		var owner = this.value;
		if('' == owner){
			$("#ownerMessage").html('Please select a Owner!');
			document.getElementById("owner").focus();
		}else{
			$("#ownerMessage").html('');
		}
	});
	$("#project").change(function(){
		var project = this.value;
		if('' == project){
			$("#projectMessage").html('Please select a Project!');
			document.getElementById("project").focus();
		}else{
			$("#projectMessage").html('');

			//get module list of the project selected
	  		$.ajax( {
				type : 'get',
				url : 'getProjectModules.do',
				dataType: "text",
				data : { 'projectId' : project },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success : function(data) {
					var y = document.getElementById("module");
					for(var j=y.length-1; j>0; j--){
						y.remove(j);
					}					
					if(data.length != 0){
						var x = document.getElementById("module");
						for(var i=0; i<data.length; i++){
							var option=document.createElement("option");
							option.value = data[i].moduleId;
							option.text = data[i].moduleName;
							x.add(option,x.options[null]);
						}
					}
				}
			});

			//get dedicated server list of the project selected
	  		$.ajax( {
				type : 'get',
				url : 'getDedicatedServers.do',
				dataType: "text",
				data : { 'projectId' : project },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success : function(data) {
					var y = document.getElementById("dserver");
					for(var j=y.length-1; j>0; j--){
						y.remove(j);
					}
					if(data.length != 0){
						var x = document.getElementById("dserver");
						for(var i=0; i<data.length; i++){
							var option=document.createElement("option");
							option.value = data[i].serverId;
							option.text = data[i].serverName 
								+"(" +data[i].serverHostname+")";
							x.add(option,x.options[null]);
						}
					}
				}
			});
			
			//get running on list of the project selected
	  		$.ajax( {
				type : 'get',
				url : 'getServerParameterData.do',
				dataType: "text",
				data : { 'projectId' : project },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success : function(data) {
					var y = document.getElementById("server");
					for(var j=y.length-1; j>0; j--){
						y.remove(j);
					}
					if(data.length != 0){
						var x = document.getElementById("server");
						for(var i=0; i<data.length; i++){
							var option=document.createElement("option");
							option.value = data[i].paradataName;
							option.text = data[i].paradataName; 
							x.add(option,x.options[null]);
						}
					}
				}
			});
			//get parameter list of the project selected
	  		$("#tblPrmIn tr:gt(0)").remove();
	  		paramIndex = 0;
			//get running on list of the project selected
	  		$.ajax( {
				type : 'get',
				url : 'getParameterData.do',
				dataType: "text",
				data : { 'projectId' : project },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success : function(data) {
					if(data.length != 0){
						paramHtml = "<td><input class='input_text' type='text'></input></td>"
						+ "<td>"
						+ "<select class='select'>"
						+ "<option value=''selected >Please Select</option>";
						for(var i=0; i<data.length; i++){
							paramHtml = paramHtml + "<option value='"
							+ data[i].paradataName 
							+ "'>"
							+ data[i].paradataName 
							+ "</option>";
						}
						paramHtml = paramHtml + "</select>"
						+ "</td>";
					}
				}
			});
		}
	});
});
function check(){
	var caseNamedefault=document.getElementById("caseName").defaultValue;
	var caseName=document.getElementById("caseName").value;
	$("#owner").trigger("change");
	if(caseNamedefault!=caseName){
		$("#caseName").trigger("blur");
	}
	if(''!=$("#ownerMessage").html()|| ''!=$("#caseNameMessage").html())
		return false;
	return true;
}
function createJson(){
	var jsonObj = {paraArray:[],logArray:[]};
	var trArray = document.getElementById("tblPrmIn").rows;
	for(var i=1; i<trArray.length; i++){
		jsonObj.paraArray.push({
			"paraId": trArray[i].cells[0].getElementsByTagName("input")[0].value,
			"paraType": "INPUT",
			"paraOrder": trArray[i].cells[1].innerHTML,
			"paraName": trArray[i].cells[2].getElementsByTagName("input")[0].value,
			"paraValue": trArray[i].cells[3].getElementsByTagName("select")[0].value,
			"description": trArray[i].cells[4].getElementsByTagName("input")[0].value
		});
	}
	trArray = document.getElementById("tblPrmOut").rows;
	for(var i=1; i<trArray.length; i++){
		jsonObj.paraArray.push({
			"paraId": trArray[i].cells[0].getElementsByTagName("input")[0].value,
			"paraType": trArray[i].cells[1].getElementsByTagName("select")[0].value,
			"paraName": trArray[i].cells[2].getElementsByTagName("input")[0].value,
			"description": trArray[i].cells[3].getElementsByTagName("input")[0].value
		});
	}
	trArray = document.getElementById("tblLog").rows;
	for(var i=1; i<trArray.length; i++){
		jsonObj.logArray.push({
			"logId": trArray[i].cells[0].getElementsByTagName("input")[0].value,
			"logName": trArray[i].cells[1].getElementsByTagName("input")[0].value,
			"logLocation": trArray[i].cells[2].getElementsByTagName("input")[0].value,
			"remark": trArray[i].cells[3].getElementsByTagName("input")[0].value
		});
	}
	document.getElementById("jsonObj").value=JSON.stringify(jsonObj);
}
function onSave(){
	createJson();
	if(verifyDedicatedServerOrRunningOn()&&check()&&verifyTimeOutPositiveInteger()){
		doFormSubmit('form_testcase','doTestcaseUpdate.do');
	}		
}
function onNext(){
	createJson();	
	if(verifyDedicatedServerOrRunningOn()&&check()&&verifyTimeOutPositiveInteger()){	
		doFormSubmit('form_testcase','doTestcaseAutoUpdateNext.do');
	}
}
function onBack() {
	doPageRedirect('testcaseList.do');
}
function dServerChange(){
	if($("#dserver").val() != ''){
		$("#server").val('');
		$("#server").attr('disabled','disabled');
	}
	else{
		$("#server").removeAttr("disabled"); 
	}
}
function serverChange(){
	if($("#server").val() != ''){
		$("#dserver").val('');
		$("#dserver").attr('disabled','disabled');
	}
	else{
		$("#dserver").removeAttr("disabled"); 
	}
}

function verifyDedicatedServerOrRunningOn(){
	if("Automated"==$("#caseType").val()){
		if($("#dserver").val() =='' && $("#server").val() =='' || $("#dserver").val() !='' && $("#server").val() !=''){
			alert("Must input Dedicated Server or Running on and can't input both!");
			return false;
		}
	}
	return true;
}	

function verifyTimeOutPositiveInteger(){
	var idval = $("#timeout").val();
	var reg = /^([1-9]*[1-9][0-9]*)$/;
	if(idval!='' && !reg.exec(idval)){
		alert("Time out default as 20mins, you can input nothing, also you can input other positive integer !");
		document.getElementById("timeout").focus();
		return false;
	}
	return true;
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
		<div class="title">Update Test Case</div>
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
			<td align="left" width="90%">
				<input class="input_text" type="text" id="caseName" name="testcase.caseName" value="${testcase.caseName!""}"></input>
				<span id="caseNameMessage" color="red"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				<font color="red">*</font>Type：
			</td>
			<td align="left" width="90%">${testcase.type!""}</td>
			<input type="hidden" id="caseType" name="testcase.type" value="${testcase.type!""}">
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				<font color="red">*</font>Owner：
			</td>
			<td align="left" width="90%">
				<select class="select" id="owner" name="testcase.ownerId">
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
			<td><#list projectList as project><#if '${project.projectId!""}'=='${testcase.projectId!""}'>${project.projectName}<input type="hidden" id="project" name="testcase.projectId" value="${project.projectId}"/></#if></#list></td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Module：</td>
			<td align="left" width="90%">
				<select class="select" id="module" name="testcase.moduleId">
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
				<input class="input_text" type="text" id="timeout" name="testcase.timeout" style="width:50px;text-align:right;" value="${testcase.timeout!""}"></input> mins &nbsp;
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">
				<textarea id="description" name="testcase.description" class="text_area" value="${testcase.description!""}">${testcase.description!""}</textarea></td>
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
				<textarea id="testStep" name="testcase.testStep" class="text_area"  value="${testcase.testStep!""}">${testcase.testStep!""}</textarea>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Expectation：</td>
			<td align="left" width="90%">
				<textarea  id="expectation" name="testcase.expectation" class="text_area"  value="${testcase.expectation!""}">${testcase.expectation!""}</textarea>
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
				<select class="select" id="dserver" name="testcase.dedicatedserverId"  onChange="dServerChange()">
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
				<select class="select" id="server" name="testcase.runonserverData" onChange="serverChange()">
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
				<input class="input_text" type="text" id="command" name="testcase.command" value="${testcase.command!""}"></input>
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
						<td width="5%"></td>
					</tr>
					<#if paramList?has_content>
					 <#list paramList as parameter >
						<#if parameter.type=="INPUT">
					 <tr>
					  <td style='display:none'><input type='hidden' value="${(parameter.paraId)}"></input></td>
					  <td>${parameter.paraOrder!""}</td>
					  <td><input class='input_text' type='text' value="${parameter.paraName!""}"></input></td>
					  <td><select class='select'>
					  		<#list parameterdataList as parameterdata>
								<option value='${parameterdata.paradataName}' <#if '${parameterdata.paradataName}'=='${parameter.paraValue!""}'>selected</#if>>${parameterdata.paradataName}</option>
							</#list>
					       </select>					  
					  </td>
					  <td><input class='input_text' type='text' value="${parameter.description!""}"></input></td>				
					  <td><button type='button' class='bntDelTr'>Delete</button></td>
					  </tr>
					  	</#if>
					 </#list>
					</#if>
				</table>
				<button type="button" id="bntPrmInAdd">Add</button>				
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
						<td width="5%"></td>
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
					  <td><button type='button' class='bntDelTr'>Delete</button></td>
					  </tr>
					  	</#if>
					 </#list>
					</#if>
				</table>
				<button type="button" id="bntPrmOutAdd">Add</button>				
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
						<td width="5%"></td>
					</tr>					
					<#if logList?has_content>
					 <#list logList as log >
					 <tr>
					  <td style='display:none'><input type='hidden' value="${(log.logId)}"></input></td>
					  <td><input class='input_text' type='text' value="${log.logName!""}"></input></td>
					  <td><input class='input_text' type='text' value="${log.logLocation!""}"></input></td>
					  <td><input class='input_text' type='text' value="${log.remark!""}"></input></td>
					  <td><button type='button' class='bntDelTr'>Delete</button></td>
					  </tr>
					 </#list>
					</#if>
				
				</table>
				<button type="button" id="bntLogAdd">Add</button>				
			</td>
		</tr>
	</table>
	</div>
	<table class="tablecontent" width="100%">
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Notes：</td>
			<td align="left" width="90%">
				<textarea class="text_area" id="remark" name="testcase.remark" value="${testcase.remark!""}"></textarea>
			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr><td align="center">
			<button type="button" id="btnNext" onclick="onNext()" style="display:none;">Next</button>
			<button type="button" id="btnSave" onclick="onSave()" style="display:none;">Save</button>			
			<button type="button" id="btnCancel" onclick="onBack()">Cancel</button>
		</td></tr>
	</table>
</div>
</form>
</body>
</html>
