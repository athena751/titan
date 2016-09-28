<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<script type="text/javascript" src="${base}/js/manhuaDate.1.0.js"></script>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
	$(document).ready(function() {
			$('#pageTable1').DataTable( {
				aaSorting: [[ 0, "desc" ]],
				dom: 'lfrtip<"clear">T',
				tableTools: {
		            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
		        }
			});
		});	
	
	function viewReport(reportId){
		$.ajax({
			type : 'get',
			url : '${base}/mytitan/getReportFromDB.do?t=' + new Date().getTime(),
			dataType: "text",
			data : { 
					 'reportId' : reportId
			 },
			success : function(msg) {	
				jBox(msg, { title: "Weekly report", width: 'auto', height:'auto'});
			}
		});
	}
	
	function doSendReport(reportId){
		$.ajax({
			type : 'get',
			url : '${base}/mytitan/doSendReport.do?t=' + new Date().getTime(),
			dataType: "text",
			data : { 
					 'reportId' : reportId
			 },
			success : function(msg) {	
				jBox(msg, { title: "Weekly report", width: 'auto', height:'auto'});
				doPageRedirect('myworkloadList.do')
			}
		});
	}
</script>
</head>

<body>
<form id="defectinfoId" action="" method="POST">
<#if navigation?has_content>
	${navigation}
</#if>
<table width="100%" border="0">
	<tr>
		<td align="left">
			<div class="title">My Workload&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doPageRedirect('myworkloadEdit.do')">
		<img src="${base}/images/icon/11.png" /><span>Add&nbsp;&nbsp;Progect</span> </a></div>
		</td>
		<!--<td align="right"><td>
		<div class="table_link">
			<ul>
	          	<li><a href="#" onclick="doPageRedirect('myworkloadEdit.do')">Add</a></li>
	        </ul>
        </div>
	</td>-->
	</tr>
</table>
<div class="box">
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <th width="35%">Title</th>
		        <th width="00%">Send to</th>
		        <th width="10%">State</th>
		        <th width="20%">Create date</th>
		        <th width="10%"></th>
			</thead>	
		<tbody>	
		<#if weeklyreportList?has_content >
     	 <#list weeklyreportList as weeklyreport>
			<tr class="mousechange">
				<td align="center"><a href="#" onclick="viewReport('${(weeklyreport.reportId)}')">${(weeklyreport.title)!''}</a></td>
				<td align="center">${(weeklyreport.sendTo)!''}</td>
				<td align="center">${(weeklyreport.type)!''}</td>
				<td align="center">${(weeklyreport.strDate)!''}</td>
				<td align="center">
				    <a class="tooltips" href="#" onclick="doSendReport('${(weeklyreport.reportId)}')"><img src="${base}/images/icon/email.gif" /><span>&nbsp;&nbsp;Send&nbsp;&nbsp;</span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
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
