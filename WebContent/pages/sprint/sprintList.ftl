<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
function doDelete(){
	if(checkBox('delete','sprintIdAry')){
		var sprintId = getSprintId('sprintIdAry');
		$.ajax( {
			type : 'get',
			url : '${base}/sprint/checkSprintHasJob.do?t=' + new Date().getTime(),
			dataType: "text",
			data : { 'sprintId' : sprintId },
			async: false,
			success : function(msg) {
				if (msg == "exist") {
					alert("The sprint cannot be deleted because there are test jobs in the sprint!")
				}
				else{
					if(confirm('Confirm the delete?')){
						doFormSubmit('sprintDoId','doSprintRemove.do');
					}
				}
			}
		});
	}
}

function doSynchron(){
	var projectId = '${(projectId)!''}';
	var html = '<div class="msg-div">'
            		  +'<p>Please enter the project name in rally:</p>'
            		  +'<input type="text" id="projectNameInRally" name="projectNameInRally" style="width:375px;"/>'
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
                var projectNameInRally = document.getElementById("projectNameInRally").value;//$("testcasepara");
                $.ajax( {
					type : 'post',
					url : 'doSynchronProject.do?t=' + new Date().getTime(),
					dataType: "text",
					data : { 'projectNameInRally' : projectNameInRally,
							 'projectId' : projectId
					},
					async: false,
					success : function(msg) {
						if(msg == 'success'){
							//jBox.nextState('<div class="msg-div">Submitting...</div>');
			                // 或 jBox.goToState('state2')
			                window.setTimeout(function () { jBox.goToState('state2'); }, 10);
						}
						else if(msg == 'notFound'){
							window.setTimeout(function () {  jBox.goToState('state3'); }, 10);
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
         		doPageRedirect('${base}/sprint/sprintList.do?projectId=' + projectId);
                return true; // close the window
            }
        	return false;
        }
    };
    states.state3 = {
        content: 'No project found!',
        buttons: {'OK': 0 } // no buttons
    };
    jBox.open(states, 'Case defect', 450, 'auto');
}

function doUpdate(){
	if(checkBox('update','sprintIdAry')){
		doFormSubmit('sprintDoId','goSprintUpdate.do');
	}
}

function checkBox(type,name){
	var sprintIds = document.getElementsByName(name);
	var count = 0;
	if(sprintIds != null && sprintIds.length > 0){
     for(var i = 0 ; sprintIds.length > i; i++){
        if(sprintIds[i].checked){
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

function getSprintId(name){
	var sprintIds = document.getElementsByName(name);
	if(sprintIds != null && sprintIds.length > 0){
     for(var i = 0 ; sprintIds.length > i; i++){
        if(sprintIds[i].checked){
        	return sprintIds[i].value;
	   	}
     }
	}
}
</script>
</head>

<body>
<form id="sprintDoId" action="" method="POST">
<#if navigation?has_content>
	 ${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Sprints
		<#if projectId?has_content >          	
          		&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doSynchron()">
			<img src="${base}/images/icon/really.png" /><span>Sync&nbsp;&nbsp;up</span> </a>
          	</#if></div>
	</td><td align="right">
		<div class="table_link">
          	
        </div>
	</td></tr>
</table>

<div class="box">
      <table class="display" id="pageTable" width="100%">
          <thead>
		       <!-- <th width="4%">
					<input type="checkbox" id="allId" onclick="doAll(this ,'sprintIdAry')" />
				</th>--!>
		        <th width="10%">Name</th>
		        <th width="10%">Project</th>
		        <th width="12%">Start Date</th>
		        <th width="12%">End Date</th>
		        <th width="10%">State</th>
		        <th width="16%">Remark</th>
		        <th width="10%">Action</th>
			</thead>	
		<tbody>	
         <#if sprintList?has_content >
     	 <#list sprintList as sprint>
			<tr class="mousechange">
				<!--<td align="center"><input type="radio" id="sprintIdAry" name="sprintIdAry"  value="${(sprint.id.sprintId)!''}&${(sprint.id.projectId)!''}"/></td>--!>
				<td align="center"><a href="#" onclick="doPageRedirect('goSprintView.do?sprintId=${(sprint.id.sprintId)}&projectId=${(sprint.id.projectId)}')" >${(sprint.sprintName)!''}</a></td>
				<td align="center">${(sprint.project.projectName)!''}</td>
				<td align="center">${(sprint.startDate)!''}</td>
				<td align="center">${(sprint.endDate)!''}</td>
				<td align="center">${(sprint.state)!''}</td>
				<td align="center">${(sprint.remark)!''}</td>
				<td align="center"><div class="table_link">
				    <a class="tooltips" href="#" onclick="doPageRedirect('${base}/test/testjobList.do?sprintId=${(sprint.id.sprintId)}')"><img src="${base}/images/icon/27.png" /><span>View&nbsp;&nbsp;jobs</span></a>
				</div></td>
			</tr>
			 </#list>
 		 </#if>
      </tbody>    
        </table>
	</div>
</div>
</form>
</body>
</html>
