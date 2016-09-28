<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script type="text/javascript">
function doDisable(id){
	$.ajax( {
		type : 'get',
		url : 'checkCaseHasBeenUsed.do?t=' + new Date().getTime(),
		dataType: "text",
		data : { 'caseId' : id },
		async: false,
		success : function(msg) {
			if (msg == "used") {
				alert("The test case cannot be deleted because it has been used!")
			}
			else{
				if(confirm('Are you sure to delete the test case?')){       
        			doPageRedirect('goTestcaseDisable.do?caseId='+id);
    			}
			}
		}
	});
}
function doDisableAct(id){
	$.ajax( {
		type : 'get',
		url : 'remCaseHasBeenUsed.do?t=' + new Date().getTime(),
		dataType: "text",
		data : { 'caseId' : id },
		async: false,
		success : function(msg) {
			if (msg != "notused") {
			    if(msg.indexOf("test plan") >=0){
			        if(confirm("The test case cannot be deleted because it has been used by " + msg + "  Jump to test plan List?"))  doPageRedirect('testplanList.do');
			    }else{
			         if(msg.indexOf("test job") >= 0){
			         if(confirm("The test case cannot be deleted because it has been used by " + msg + " Jump to test job page?"))    doPageRedirect('testjobList.do');
			         }
			    }
			}
			else{
				if(confirm('Are you sure to delete the test case?')){       
        			doPageRedirect('goTestcaseDisable.do?caseId='+id);
    			}
			}
		}
	});
}
function checkBox(type,name){
	var testcaseIds = document.getElementsByName(name);
	var count = 0;
	if(testcaseIds != null && testcaseIds.length > 0){
     for(var i = 0 ; testcaseIds.length > i; i++){
        if(testcaseIds[i].checked){
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
</script>
</head>

<body>
<#if navigation?has_content>
	 ${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Test Cases
			&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doPageRedirect('goTestcaseCreate.do')">
		<img src="${base}/images/icon/11.png" /><span>Add&nbsp;&nbsp;TestCase</span> </a>
		</div>
	</td><!--<td align="right">
		<div class="table_link">
   			<ul>
     			<li><a href="#" onclick="doPageRedirect('goTestcaseCreate.do')">New</a></li>    	     	     			
    		</ul>
		</div>
	</td>--></tr>
</table>
<form id="testcaseDoId" action="${base}/test/testcaseList.do" method="POST">
    <table class="display" id="pageTable" width="100%">
		<thead>
			<th width="8%">CaseID</th>
			<th width="10%">Name</th>
			<th width="10%">Type</th>
			<th width="10%">Owner</th>
			<th width="10%">Project</th>
			<th width="10%">Module</th>
			<th width="20%">Description</th>
			<th width="20%">Action</th>
		</thead>        
	<#if testcaseVoList?has_content >
	    <#list testcaseVoList as testcase>
			<tr class="mousechange">
				<td align="center">${(testcase.caseCode)!''}</td>
				<td align="center"><a href="#" onclick="doPageRedirect('goTestcaseView.do?caseId=${(testcase.caseID)}')">${(testcase.caseName)!''}</a></td>
				<td align="center">${(testcase.caseType)!''}</td>
				<td align="center">${(testcase.owner)!''}</td>
				<td align="center">${(testcase.project)!''}</td>
				<td align="center">${(testcase.module)!''}</td>
				<td align="center" style="word-break:break-all">${(testcase.description)!''}</td>
				<td align="center"><div class="table_link">
				    <#if '${testcase.owner!""}'=='${currentUserName!""}'||'${isAdmin!""}'=='1'>
				    <a class="tooltips" href="#" onclick="doPageRedirect('goTestcaseUpdate.do?caseId=${(testcase.caseID)}')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;&nbsp;</span></a>&nbsp;&nbsp;&nbsp;&nbsp;
					    <a class="tooltips" href="#" onclick="doDisableAct('${(testcase.caseID)}')"><img src="${base}/images/icon1/er_s.gif" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Delete&nbsp;&nbsp;&nbsp;&nbsp;</span></a> 
					    
				    </#if>
				</div></td>
			</tr>
	    </#list>
		
 	</#if>      
	</table>
</form>
</body>
</html>
