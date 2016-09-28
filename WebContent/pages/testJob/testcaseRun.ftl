<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script type="text/javascript">
function onSave(){	
	    var state = document.getElementById("state").value;
	    var note = document.getElementById("remark").value;
	    document.getElementById("runCaseState").value = state;
	    document.getElementById("runCaseNote").value = note ;
		doFormSubmit('form_testcase','doRuncaseSave.do');

}
function onBack() {
	doPageRedirect('testjobList.do');
}

function showDefect(){
	var res = $('#state').val();
	var runcaseId = "${(runCase.runCaseId)!""}";
	if(res == 'FAIL'){
		$.ajax({
			type: 'post',
			url: 'getDefectByRunCaseId.do?t=' + new Date().getTime(),
			dataType: "text",
			data: {
				'runCaseId': runcaseId
			},
			async: false,
			success: function(msg) {
				$("#defectField").html(msg);
				$('#trDefect').show();
			}
		});	
	}
	else{
		$('#trDefect').hide();
	}
}

function editDefect() {
	var runcaseId = "${(runCase.runCaseId)!""}";
    var html = '<div class="msg-div"> <p>Defect type:</p>'
            		  +'<select id="defecttype" name="defecttype"  class="select" style="width:160px">'
            		  +'<option value="rally">Rally</option>'
            		  +'<option value="quix">Quix</option>'
            		  +'</select>'
            		  +'<p>Defect number:</p>'
            		  +'<input type="text" id="defectNo" name="defectNo" style="width:75px;"/>'
            		  +'</div>';
			
    var data = {};
    var states = {};
    states.state1 = {
    content: html,
    buttons: { 'OK': 1, 'Cancel': 0 },
    submit: function (v, h, f) {
        if (v == 0) {
            return true; // close the window
        }
        else {
            h.find('.errorBlock').hide('fast', function () { $(this).remove(); });
            //data.amount = f.amount;  
            var defectType = document.getElementById("defecttype").value;//$("testcasepara");
            var defectNo = document.getElementById("defectNo").value;//$("testcasepara");
            $.ajax( {
				type : 'post',
				url : 'doSaveCaseDefect.do?t=' + new Date().getTime(),
				dataType: "text",
				data : {'defectType' : defectType,
						'defectNo' : defectNo,
						'runCaseId' : runcaseId
				},
				async: false,
				success : function(msg) {
					if(msg == 'success'){
						//jBox.nextState('<div class="msg-div">Submitting...</div>');
			            // 或 jBox.goToState('state2')
			            window.setTimeout(function () { jBox.goToState('state2'); }, 10);
					}
					else if(msg == 'noDefect'){
						window.setTimeout(function () {  jBox.goToState('state3'); }, 10);
					}
					else if(msg == 'duplicated'){
						window.setTimeout(function () {  jBox.goToState('state4'); }, 10);
					}
				}
			});
        }

        return false;
    }
    };
    states.state2 = {
        content: 'Success',
        buttons: {'OK': 0 }, // no buttons
        submit: function (v, h, f) {
            if (v == 0) {
                window.setTimeout(function () {  refreshHistory(runcaseId); }, 10);
                return true; // close the window
            }
            return false;
        }
    };
    states.state3 = {
        content: 'No defect found!',
        buttons: {'OK': 0 } // no buttons
    };
    states.state4 = {
        content: 'Duplicated defect',
        buttons: {'OK': 0 } // no buttons
    };
    jBox.open(states, 'Case defect', 450, 'auto');
}

function refreshHistory(runcaseId){
	$.ajax({
		type: 'post',
		url: 'getDefectByRunCaseId.do?t=' + new Date().getTime(),
		dataType: "text",
		data: {
			'runCaseId': runcaseId
		},
		async: false,
		success: function(msg) {
			$("#defectField").html(msg);
		}
	});	
}
</script>
</head>

<body>
<form id="form_testcase" method="post">
<#if navigation?has_content>
	 ${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Run Test Case</div>
	</td><td align="right">
	</td></tr>
</table>
<div class="box">
	<table class="tablecontent" width="100%">
		<tr>
			<td class="tablecontent_title" colspan="2"><div class="sub_title">General</div></td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Case Code：</td>
			<td>${testcase.caseCode!""}</td>
			<input id="runCaseId" type="hidden" name="runCase.runCaseId"  value="${(runCase.runCaseId)!""}"/>
			<input id="runJobId" type="hidden" name="runCase.runJobId"  value="${(runCase.runJobId)!""}"/>
			<input id="caseId" type="hidden" name="runCase.caseId"  value="${(runCase.caseId)!""}"/>
			<input id="runCaseState" type="hidden" name="runCaseState"  value="${(runCaseState)!""}"/>
			<input id="runCaseNote" type="hidden" name="runCaseNote"  value="${(runCaseNote)!""}"/>
			<input id="testcaseId" type="hidden" name="testcase.caseId"  value="${(testcase.caseId)!""}"/>
			<input id="testcaseCode" type="hidden" name="testcase.caseCode"  value="${(testcase.caseCode)!""}"/>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				Case Name：
			</td>
			<td align="left" width="90%">
				${testcase.caseName!""}
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				Case Type：
			</td>
			<td align="left" width="90%">
				<select class="select" id="caseType" name="testcase.type" disabled="disabled">
					<option value=""></option>
					<option value="Manual" <#if 'Manual'=='${testcase.type!""}'>selected</#if>>Manual</option>
					<option value="Automated" <#if 'Automated'=='${testcase.type!""}'>selected</#if>>Automated</option>
				</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				Owner：
			</td>
			<td align="left" width="90%">
				<select class="select" id="owner" name="testcase.ownerId" disabled="disabled">
					<option value=""></option>
		        	<#list userList as user>
		        	<option value="${user.userId}" <#if '${user.userId!""}'=='${testcase.ownerId!""}'>selected</#if>>${user.userCode}</option>		            
		          	</#list>
				</select>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">
				Project：
			</td>
			<td align="left" width="90%">
				<select class="select" id="project" name="testcase.projectId" disabled="disabled">
					<option value=""></option>
		        	<#list projectList as project>
		        	<option value="${project.projectId}" <#if '${project.projectId!""}'=='${testcase.projectId!""}'>selected</#if>>${project.projectName}</option>
		          	</#list>
				</select>
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
			<td class="tablecontent_title" align="right" width="10%">Description：</td>
			<td align="left" width="90%">
				<textarea id="description" name="testcase.description" class="text_area" value="${testcase.description!""}" readonly="true">${testcase.description!""}</textarea>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Test Step：</td>
			<td align="left" width="90%">
				<textarea id="testStep" name="testcase.testStep" class="text_area"  value="${testcase.testStep!""}" readonly="true">${testcase.testStep!""}</textarea>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Expectation：</td>
			<td align="left" width="90%">
				<textarea  id="expectation" name="testcase.expectation" class="text_area"  value="${testcase.expectation!""}" readonly="true">${testcase.expectation!""}</textarea>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Test Result：</td>
			<td align="left" width="90%">
				<select class="select" id="state" name="runCase.state" onChange="showDefect()">
					<option value=""></option>
					<option value="NOTRUN" <#if '${runCase.state!""}'=='NOTRUN'>selected</#if>>NOTRUN</option>
					<option value="SKIPPED" <#if '${runCase.state!""}'=='SKIPPED'>selected</#if>>SKIPPED</option>
					<option value="PASS" <#if '${runCase.state!""}'=='PASS'>selected</#if>>PASS</option>
					<option value="FAIL" <#if '${runCase.state!""}'=='FAIL'>selected</#if>>FAIL</option>
				</select>
			</td>
		</tr>
		<tr id="trDefect" style='display:none'>
			<td class="tablecontent_title" align="right" width="10%">Defects：</td>
			<td><div id='defectField'><div></td>
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
				<textarea class="text_area" id="testStep" name="testcase.testStep" value="${testcase.testStep!""}"></textarea>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">Expectation：</td>
			<td align="left" width="90%">
				<textarea class="text_area" id="expectation" name="testcase.expectation" value="${testcase.expectation!""}"></textarea>
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
				<select class="select" id="dserver" name="testcase.serverId">
					<option value=""></option>
					<#if dedicatedServerList?has_content >
					<#list dedicatedServerList as server>
		        	<option value="${server.serverId}" <#if '${server.serverId!""}'=='${testcase.serverId!""}'>selected</#if>>${server.serverName}(${server.serverHostname})</option>
					</#list>
					</#if>
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
					        <option value='$hostname' <#if '$hostname'=='${parameter.paraValue!""}'>selected</#if>>$hostname</option>
					        <option value='$ip' <#if '$ip'=='${parameter.paraValue!""}'>selected</#if>>$ip</option>
					        <option value='$iloname' <#if '$iloname'=='${parameter.paraValue!""}'>selected</#if>>$iloname</option>
					        <option value='$iloip' <#if '$iloip'=='${parameter.paraValue!""}'>selected</#if>>$iloip</option>
					        <option value='' <#if ''=='${parameter.paraValue!""}'>selected</#if>>Other</option>
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
			<td class="tablecontent_title" align="right" width="10%">Test Notes：</td>
			<td align="left" width="90%">
				<textarea class="text_area" id="remark" name="runCase.remark" value="${runCase.remark!""}">${runCase.remark!""}</textarea>
			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr><td align="center">
			<button type="button" id="btnNext" onclick="onNext()" style="display:none;">Next</button>
			<button type="button" id="btnSave" onclick="onSave()" >Save</button>			
			<button type="button" id="btnCancel" onclick="onBack()">Cancel</button>
		</td></tr>
	</table>
</div>
</form>
</body>
</html>
