<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>
<style>
#dvCompute{position:absolute;visibility:hidden;}
</style>
<div id="dvCompute"></div>	
<script>
function doUpdate(){
	if(checkBox('update','testplanIdAry')){
		doFormSubmit('testplanDoId','goTestplanUpdate.do');
	}
}
function doCopy(testplanName){
	//if(!checkBox('update','testplanIdAry')){
	//	return;
	//}
	var name = testplanName;
	var html = '<div class="msg-div">'
            		  +'<p>New name for '
            		  +name
            		  +':</p>'
            		  +'<input type="text" id="newplanName" name="newplanName" value ="Copy of '
            		  + name
            		  +'" style="width:385px;"/>'
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
                var newplanName = document.getElementById("newplanName").value;//$("testcasepara");
                $.ajax( {
					type : 'get',
					url : 'checkTestplanName.do?t=' + new Date().getTime(),
					dataType: "text",
					data : { 'testplanName' : newplanName },
					success : function(msg) {
						if (msg == "exist") {	
							alert('The name has been used!');
						}else{
							doFormSubmit('testplanDoId','goTestplanCopy.do?newplanName=' + newplanName);
						}
					}
				});
            }

            return false;
        }
    };
    jBox.open(states, 'Case defect', 450, 'auto');
}

function checkBox(type,name){
	var testplanIds = document.getElementsByName(name);
	var count = 0;
	if(testplanIds != null && testplanIds.length > 0){
     for(var i = 0 ; testplanIds.length > i; i++){
        if(testplanIds[i].checked){
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

function getSelectedTestplanName(testplanId){
	var selectedTr = testplanId.parent().parent();
	return selectedTr[0].cells[2].innerHTML;
	
}
function doRowDelete(id){
	$.ajax( {
		type : 'get',
		url : 'checkPlanHasBeenUsed.do?t=' + new Date().getTime(),
		dataType: "text",
		data : { 'testplanId' : id },
		async: false,
		success : function(msg) {
			if (msg == "used") {
				alert("The test plan cannot be deleted because it has been used!")
			}
			else{
				if(confirm('Confirm the delete?')){
      				doPageRedirect('doTestplanRemove.do?testplanId='+id);
      			}
			}
		}
	});
}
function doPlanShow(id){
		$.ajax( {
			type : 'get',
			url : 'getPlanShow.do?t=' + new Date().getTime(),
			dataType: "text",
			data : { 'testplanId' : id },
			async: false,
			success : function(msg) {
				var json = eval('(' + msg + ')');
				var name='';
				var i=0;
				var wid=350;
				$(json).each(function(){
			        var eachname = this.caseName;
			        var eachcasecode = this.caseCode;
			        if(i!=0){
			        	name = name+eachcasecode+'： '+eachname+'<br/>';
			        	var p = compute(eachcasecode+'： '+eachname);
		        		if(p.w>330 && wid!='auto'){
				        	wid='auto';
				        }
			        }
			        i++;
			    });
			    if(name==''){
			    	name = 'No case';
			    }
				$.jBox.info(name,'Test Plan',{width: wid});
			}
	});
}

function compute(v) {
    var d = document.getElementById('dvCompute');
    d.innerHTML = v;
    return { w: d.offsetWidth, h: d.offsetHeight };
}
$(document).ready(function(){
  $(document).keydown(function(){ 
		if(event.keyCode == 13) 
			$("#filter").click();}
	);
});
</script>
</head>

<body>
<form id="testplanDoId" action="" method="POST">
<#if navigation?has_content>
	 ${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Testplans
		&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doPageRedirect('goTestplanCreate.do')">
		<img src="${base}/images/icon/11.png" /><span>Add&nbsp;&nbsp;Testplans</span> </a>
		</div>
	</td><!--<td align="right">
		<div class="table_link">
          	<li><a href="#" onclick="doPageRedirect('goTestplanCreate.do')">Add</a></li>
          	<li><a href="#" onclick="doCopy()">Copy</a></li>
        </div>
	</td>--></tr>
</table>
<div class="box">
      <table id="pageTable" width="100%" border="0" cellpadding="0" cellspacing="0" class="display">
          <thead>
		        <!--<th width="4%">
					<input type="checkbox" id="allId" onclick="doAll(this ,'testplanIdAry')" />
				</th>-->
		        <th width="10%">Code</th>
		        <th width="10%">Name</th>
		        <th width="10%">Type</th>
		        <th width="10%">Project</th>
		        <th width="10%">Owner</th>
		        <th width="20%">Description</th>
		        <th width="20%">Action</th>
			</thead>
		<tbody>	
         <#if testplanVoList?has_content >
     	 <#list testplanVoList as testplan>
			<tr class="mousechange">
				<!--<td align="center"><input type="radio" id="testplanIdAry" name="testplanIdAry"  value="${(testplan.testplanID)!''}"/></td>-->
				<td align="center">${(testplan.testplanCode)!''}</td>
				<td align="center">${(testplan.testplanName)!''}</td>
				<td align="center">${(testplan.testplanType)!''}</td>
				<td align="center">${(testplan.project)!''}</td>
				<td align="center">${(testplan.owner)!''}</td>
				<td align="center">${(testplan.description)!''}</td>
				<td align="center"><div class="table_link">
					<a class="tooltips" href="#" onclick="doPlanShow('${(testplan.testplanID)!''}')"><img src="${base}/images/icon/magnifier.gif" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Detail&nbsp;&nbsp;&nbsp;&nbsp;</span></a> &nbsp;&nbsp;&nbsp;&nbsp;
					<a class="tooltips" href="#" onclick="doCopy('${(testplan.testplanName)}')"><img src="${base}/images/icon/page_copy.gif" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Copy&nbsp;&nbsp;&nbsp;&nbsp;</span></a> &nbsp;&nbsp;&nbsp;&nbsp;
				    <#if '${testplan.owner!""}'=='${currentUserName!""}'||'${isAdmin!""}'=='1'>
				    <a class="tooltips" href="#" onclick="doPageRedirect('goTestplanEdit.do?testplanId=${(testplan.testplanID)}')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;&nbsp;</span></a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="tooltips" href="#" onclick="doRowDelete('${(testplan.testplanID)}')"><img src="${base}/images/icon1/er_s.gif" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Delete&nbsp;&nbsp;&nbsp;&nbsp;</span></a> 
					    
				    </#if>
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
