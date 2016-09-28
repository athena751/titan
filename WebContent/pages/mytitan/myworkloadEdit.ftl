<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/manhuaDate.1.0.css" />
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
<script>
	$(function (){
		$("input.mh_date").manhuaDate({					       
			Event : "click",		       
			Left : 0,
			Top : -16,
			fuhao : "-",
			isTime : false,
			beginY : 2010,
			endY :2025
		});
		
	});
	
	function getTaskInfo(){
		if($("#startDate").val() == '' || $("#endDate").val() == '' ||  $("#sendTo").val() == '') {
			window.setTimeout(function () { jBox.tip('Please select the date!', 'error'); }, 2000);
			return;
		}
		$.ajax( {
			type : 'get',
			url : '${base}/mytitan/getTaskInfo.do?t=' + new Date().getTime(),
			dataType: "text",
			data : { 'startDate' : $("#startDate").val(),
					 'endDate' : $("#endDate").val()
			 },
			beforeSend:function(){jBox.tip("Fetching...", 'loading');},
			success : function(msg) {				      
				window.setTimeout(function () { jBox.tip('Fetching success!', 'success'); }, 50);
				$("#taskTable").html(msg);
			}
		});
	}
	
	function editCustomTask(){
		$("#tblCustomTask").show();
		$("#btnCustomTaskAdd").show();
		$("#btnGenerateReport").show();
		$("#issuesmiscplan").show();
	}
	
	function generateReport(){
		var jsonObj = createJson();
		$.ajax( {
			type : 'get',
			dataType: "text",
			url : '${base}/mytitan/generateReport.do?t=' + new Date().getTime(),
			data : { 
					 'jsonObj' : jsonObj
			 },
			success : function(msg) {	
				document.getElementById("reportContent").value = msg;
				jBox(msg, { title: "Weekly report", width: 'auto', height:'auto', submit: submit, buttons: { 'Save as draft': 'draft', 'Save and send': 'send', 'Cancel':'cancel' }});
			}
		});
	}
	
	function submit(v, h, f) {
		var jsonObj = createJson();
		document.getElementById("jsonObj").value = jsonObj;
        if(v == 'draft'){
        	doFormSubmit('workloadForm','doSaveReport.do')
        }
        else if(v == 'send'){
        	doFormSubmit('workloadForm','doSaveAndSendReport.do')
        }
    }

	
	function createJson(){
		var jsonObj = {rallyTaskArray:[],quixListArray:[],customTaskArray:[],issueMiscPlanArray:[]};
		if(document.getElementById("tblRallyTask")!=null){
		    var trArray = document.getElementById("tblRallyTask").rows;
		    for(var i=1; i<trArray.length; i++){
			    jsonObj.rallyTaskArray.push({
				"taskName": trArray[i].cells[0].innerHTML ,
				"state": trArray[i].cells[1].innerHTML ,
				"actual": trArray[i].cells[2].innerHTML ,
				"todo": trArray[i].cells[3].innerHTML ,
				"desc": trArray[i].cells[4].innerHTML ,
				"parent": trArray[i].cells[5].innerHTML 
			    });
		    }
		}
		if(document.getElementById("tblQuixList")!=null){		
		    var trArray = document.getElementById("tblQuixList").rows;
		    for(var i=1; i<trArray.length; i++){
			    jsonObj.quixListArray.push({
				    "QuixID": trArray[i].cells[0].innerHTML ,
				    "QuixName": trArray[i].cells[1].innerHTML ,
				    "State": trArray[i].cells[2].innerHTML ,
				    "Developer": trArray[i].cells[3].innerHTML ,
				    "Submit": trArray[i].cells[4].innerHTML ,
			        "Date": trArray[i].cells[5].innerHTML,
				    "Project": trArray[i].cells[6].innerHTML 
			    });
		    }
		}
		trArray = document.getElementById("tblCustomTask").rows;
		for(var i=1; i<trArray.length; i++){
			jsonObj.customTaskArray.push({
				"taskName": trArray[i].cells[1].getElementsByTagName("input")[0].value,
				"state": trArray[i].cells[2].getElementsByTagName("input")[0].value,
				"percentage": trArray[i].cells[3].getElementsByTagName("input")[0].value,
				"time": trArray[i].cells[4].getElementsByTagName("input")[0].value,
				"desc": trArray[i].cells[5].getElementsByTagName("input")[0].value
			});
		}
		jsonObj.issueMiscPlanArray.push({
			"issue": $("#issues").val(),
			"misc": $("#misc").val(),
			"plan": $("#plan").val()
		});
		return JSON.stringify(jsonObj);
	}
	
	function getNaturalWeek(dtstring){
       var dtstr=dtstring?dtstring.replace(/-/ig,'/'):new Date().getTime();
       var dt=new Date(dtstr);
       var days = [-6, 0, -1, -2, -3, -4, -5];
       var Dayms=(1000 * 60 * 60 * 24);
       var DateStart= dt.getTime() + (days[dt.getDay()] * Dayms);
       var WeekStart=new Date(DateStart);
       var Month=WeekStart.getMonth()+1;
       var Day=WeekStart.getDate();
       var WeekStartStr=WeekStart.getFullYear() + "-" + ((Month>9)?Month:("0"+Month)) + "-" +((Day>9)?Day:("0"+Day));
       var WeekEndString=WeekStart.getTime()+(6*Dayms);
       var WeekEnd=new Date(WeekEndString);
       Month=WeekEnd.getMonth()+1;
       Day=WeekEnd.getDate();
       var WeekEndStr=WeekEnd.getFullYear()+"-"+((Month>9)?Month:("0"+Month))+"-"+((Day>9)?Day:("0"+Day));
       document.getElementById("startDate").value = WeekStartStr;
       document.getElementById("endDate").value = WeekEndStr;
	}
	
	
	$(document).ready(function(){
		getNaturalWeek();
		//add custom tasks
		$("#btnCustomTaskAdd").click(function(){
			var newTr = "<tr>"
						+ "<td><input type='hidden'></td>"
						+ "<td><input class='input_text' type='text'></input></td>"
						+ "<td><input class='input_text' type='text'></input></td>"
						+ "<td><input class='input_text' type='text'></input></td>"
						+ "<td><input class='input_text' type='text'></input></td>"
						+ "<td><input class='input_text' type='text'></input></td>"
						+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
			$("#tblCustomTask tr:last").after(newTr);
	  	});
	  	//delete tr for table
	 	$(".box").delegate(".bntDelTr", "click", function(){
			$(this).closest("tr").remove();
	  	});
	 });
</script>
</head>

<body>
<form id="workloadForm" name="sprintForm" method="post">
	<#if navigation?has_content>
		${navigation}
	</#if>
	<input type="hidden" id="jsonObj" name="jsonObj"/>
	<input type="hidden" id="reportContent" name="reportContent"/>
	<table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			<span class="marfin_lef5">My workload</span>
			</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
      <table class="tablecontent" width="100%">
		<tr>
			<td align="right" class="tablecontent_title" width="10%">Start Date:</td>
			<td align="left" width="90%"><input id="startDate" name="startDate" type="text" class="mh_date" value="" readonly="true" /></td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="10%">End Date:</td>
			<td align="left" width="90%"><input id="endDate" name="endDate" type="text" class="mh_date" value="" readonly="true" /></td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="10%">Send to:</td>
			<td align="left" width="90%"><input class='input_text' type='text' id="sendTo" name="sendTo" value="m11y.all@hpe.com"}"></input></td>
		</tr>
	</table>
<table width="100%" border="0">
	<tr><td align="left">
		<input type="button" onclick="getTaskInfo()" value="Fetch Tasks from Rally and Quix"></input>
	</td></tr>

</table>
<div id="taskTable"></div>
<table id="tblCustomTask" width="100%" style="display:none">
	<tr>
		<td width="3%"></td>
		<td width="30%" align="center">Task name</td>
		<td width="10%" align="center">State</td>
		<td width="10%" align="center">Percentage</td>
		<td width="10%" align="center">Time</td>
		<td width="40%" align="center">Description</td>
		<td width="5%"></td>
	</tr>
</table>
<button type="button" id="btnCustomTaskAdd" style="display:none">Add</button><br><br>	
<div id="issuesmiscplan" style="display:none">
	Issues: <textarea id="issues" class="text_area"></textarea>
	Misc: <textarea id="misc" class="text_area"></textarea>
	Plan for next week: <textarea id="plan" class="text_area"></textarea>
</div>
<br><br>
<div align="right">
	<input type="button" id="btnGenerateReport" onclick="generateReport()" value="Generate report" style="display:none"></input>	
</div>
</div>
</form>
</body>
</html>
