<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#include "/system/include/uploadify.ftl">
<script type="text/javascript">
$(document).ready(function(){

	//hide or show manual and auto div
	$("#caseType").change(function(){
		var caseType = $("#caseType").val();
		if(caseType=="Manual"){
  			$("#divManual").show();
  			$("#divAuto").hide();  			
  			$("#btnNext").hide();
  			$("#btnSave0").show(); 
  			$("#btnSave1").show();
  			$("#btnCancel").show(); 
  		} else if(caseType=="Automated"){
  			$("#divManual").hide();
  			$("#divAuto").show();  			
  			$("#btnNext").show();
  			$("#btnSave0").hide(); 
  			$("#btnSave1").hide();
  			$("#btnCancel").show(); 
  		} else {
  			$("#divManual").hide();
  			$("#divAuto").hide();  			
  			$("#btnNext").hide();
  			$("#btnSave0").hide(); 
  			$("#btnSave1").hide();
  			$("#btnCancel").show(); 
  		}
  	});

  	//add tr for table
  	var paramIndex = 0;
  	var paramHtml = '';
	$("#bntPrmInAdd").click(function(){
		paramIndex++;
		var newTr = "<tr><td>"+paramIndex+"</td>"
					+ paramHtml
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		$("#tblPrmIn tr:last").after(newTr);
  	});
	$("#bntPrmOutAdd").click(function(){
		var newTr = "<tr><td><select class='select'>"
					+ "<option value='PASS'>PASS</option>"
					+ "<option value='FAIL'>Other</option></select></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><input class='input_text' type='text'></input></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		$("#tblPrmOut tr:last").after(newTr);
  	});
	$("#bntLogAdd").click(function(){
		var newTr = "<tr><td><input class='input_text' type='text'></input></td>"
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
	  	var caseName = this.value;
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
	$("#caseType").change(function(){
		var caseType = this.value;
		if('' == caseType){
			$("#caseTypeMessage").html('Please select a Type!');
			document.getElementById("caseType").focus();
		}else{
			$("#caseTypeMessage").html('');
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
	$("#project").trigger("change");
	$("#owner").trigger("change");
	$("#caseType").trigger("change");
	$("#caseName").trigger("blur");
	
	if(''!= $("#projectMessage").html() 
		|| ''!=$("#ownerMessage").html() 
		|| ''!=$("#caseTypeMessage").html() 
		|| ''!=$("#caseNameMessage").html())
		return false;
	
	return true;
}
function createJson(){
	var jsonObj = {paraArray:[],logArray:[]};
	var trArray = document.getElementById("tblPrmIn").rows;
	for(var i=1; i<trArray.length; i++){
		jsonObj.paraArray.push({
			"paraType": "INPUT",
			"paraOrder": trArray[i].cells[0].innerHTML,
			"paraName": trArray[i].cells[1].getElementsByTagName("input")[0].value,
			"paraValue": trArray[i].cells[2].getElementsByTagName("select")[0].value,
			"description": trArray[i].cells[3].getElementsByTagName("input")[0].value
		});
	}
	trArray = document.getElementById("tblPrmOut").rows;
	for(var i=1; i<trArray.length; i++){
		jsonObj.paraArray.push({
			"paraType": trArray[i].cells[0].getElementsByTagName("select")[0].value,
			"paraName": trArray[i].cells[1].getElementsByTagName("input")[0].value,
			"description": trArray[i].cells[2].getElementsByTagName("input")[0].value
		});
	}
	trArray = document.getElementById("tblLog").rows;
	for(var i=1; i<trArray.length; i++){
		jsonObj.logArray.push({
			"logName": trArray[i].cells[0].getElementsByTagName("input")[0].value,
			"logLocation": trArray[i].cells[1].getElementsByTagName("input")[0].value,
			"remark": trArray[i].cells[2].getElementsByTagName("input")[0].value
		});
	}
	document.getElementById("jsonObj").value=JSON.stringify(jsonObj);
}
function onSave(next){
	createJson();	
	if(verifyDedicatedServerOrRunningOn()&&check()&&verifyTimeOutPositiveInteger()){	
		if(next==0){
			doFormSubmit('form_testcase','doTestcaseSave.do');
		}else if(next==1){
			doFormSubmit('form_testcase','doTestcaseSaveNext.do');
		}
	}
}
function onNext(){
	createJson();	
	if(verifyDedicatedServerOrRunningOn()&&check()&&verifyTimeOutPositiveInteger()){	
		doFormSubmit('form_testcase','doTestcaseAutoNext.do');
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
		<div class="title">Create Test Case</div>
	</td><td align="right">
	</td></tr>
</table>
<div class="box">
	<table class="tablecontent" width="100%">
		<tr>
			<td class="tablecontent_title" colspan="2"><div class="sub_title">General</div></td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Testcase Code</td>
			<td align="left" width="90%"></td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				<font color="red">*</font>Testcase Name
			</td>
			<td align="left" width="90%">
				<input class="input_text" type="text" id="caseName" name="testcase.caseName"></input>
				<span id="caseNameMessage" color="red"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				<font color="red">*</font>Type
			</td>
			<td align="left" width="90%">
				<select class="select" id="caseType" name="testcase.type">
					<option value=""></option>
					<option value="Manual">Manual</option>
					<option value="Automated">Automated</option>
				</select>
				<span id="caseTypeMessage" color="red"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Description</td>
			<td align="left" width="90%">
				<textarea class="text_area" id="description" name="testcase.description"></textarea>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				<font color="red">*</font>Owner
			</td>
			<td align="left" width="90%">
				<select class="select" id="owner" name="testcase.ownerId">
					<option value=""></option>
		        	<#list userList as user>
		            <option value="${user.userId}">${user.userCode}</option>
		          	</#list>
				</select>
				<span id="ownerMessage" color="red"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				<font color="red">*</font>Project
			</td>
			<td align="left" width="90%">
				<select class="select" id="project" name="testcase.projectId">
					<option value=""></option>
		        	<#list projectList as project>
		            <option value="${project.projectId}">${project.projectName}</option>
		          	</#list>
				</select>
				<span id="projectMessage" color="red"></span>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Module</td>
			<td align="left" width="90%">
				<select class="select" id="module" name="testcase.moduleId">
					<option value=""></option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Time Out<br/>(Default as 20 mins)</td>
			<td align="left" width="90%">
				<input class="input_text" type="text" id="timeout" name="testcase.timeout" style="width:50px;text-align:right;"></input> mins &nbsp;
			</td>
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
				<textarea class="text_area" id="testStep" name="testcase.testStep"></textarea>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Expectation：</td>
			<td align="left" width="90%">
				<textarea class="text_area" id="expectation" name="testcase.expectation"></textarea>
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
			<td class="tablecontent_title" align="right" width="10%">Dedicated Server</td>
			<td align="left" width="90%">
				<select class="select" id="dserver" name="testcase.dedicatedserverId" onChange="dServerChange()">
					<option value=""></option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Running on</td>
			<td align="left" width="90%">
				<select class="select" id="server" name="testcase.runonserverData">
					<option value=""></option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Command：</td>
			<td align="left" width="90%">
				<input class="input_text" type="text" id="command" name="testcase.command"></input>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Parameter：</td>
			<td align="left" width="90%">
				<table id="tblPrmIn" width="100%">
					<tr>
						<td width="5%">Rank</td>
						<td width="20%">Parameter</td>
						<td width="10%">Value</td>
						<td>Description</td>
						<td width="5%"></td>
					</tr>
				</table>
				<button type="button" id="bntPrmInAdd">Add</button>				
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Return：<br>(Default 0 as pass)</td>
			<td align="left" width="90%">
				<table id="tblPrmOut" width="100%">
					<tr>
						<td width="10%">Type</td>
						<td width="20%">Parameter</td>
						<td>Description</td>
						<td width="5%"></td>
					</tr>
				</table>
				<button type="button" id="bntPrmOutAdd">Add</button>				
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Log：</td>
			<td align="left" width="90%">
				<table id="tblLog" width="100%">
					<tr>
						<td width="10%">Log Name</td>
						<td width="20%">Log Location</td>
						<td>Description</td>
						<td width="5%"></td>
					</tr>
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
				<textarea class="text_area" id="remark" name="testcase.remark"></textarea>
			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr><td align="center">
			<button type="button" id="btnNext" onclick="onNext()" style="display:none;">Next</button>
			<button type="button" id="btnSave0" onclick="onSave(0)" style="display:none;">Save</button>
			<button type="button" id="btnSave1" onclick="onSave(1)" style="display:none;">Save And New</button>
			<button type="button" id="btnCancel" onclick="onBack()">Cancel</button>
		</td></tr>
	</table>
</div>
</form>
</body>
</html>
