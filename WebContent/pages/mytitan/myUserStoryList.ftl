<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>
<script type="text/javascript">
	 function refreshAll(){
		if(confirm('Confirm refresh, it will take a few minutes?')){
			$.ajax( {
				type : 'get',
				url : '${base}/mytitan/refreshUs.do?t=' + new Date().getTime(),
				beforeSend:function(){jBox.tip("Refreshing...", 'loading');},
				success : function(data) {				      
					window.setTimeout(function () { jBox.tip('Refresh success!', 'success'); }, 2000);
					refreshPage();
				}
			});
		}
	}
	
	function showTasks(usNum){
		$.ajax( {
			type : 'get',
			url : '${base}/mytitan/getUsTask.do?t=' + new Date().getTime(),
			data : { 'usNum' : usNum },
			success : function(msg) {				      
				jBox.alert(msg);
			}
		});
	}
	
	function refreshPage(){
		doPageRedirect('myusList.do')
	}
</script>

<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
</head>

<body>
<form id="usListForm" action="${base}/mytitan/myusList.do" method="POST">
<#if navigation?has_content>
	${navigation}
</#if>
<div class="showlist_main showlist_mainnopadding"></div>
 
	   <div class="table_link">
        <ul>
        </ul></div>
<div class="clear"></div>
        <table id="pageTable" class="display" width="100%" border="0" cellpadding="0" cellspacing="0">
         	<thead>
				<th width="10%" >Number</th>
		        <th width="25%" >Name</th>
		     	<th width="8%" >PlanEstimate</th>
		     	<th width="8%" >TaskActualTotal</th>
		     	<th width="8%" >TaskEstimates</th>
		     	<th width="8%" >TaskTodos</th>
		        <th width="10%" >State</th>
		        <th width="8%" ></th>
			</thead>
          
          <#if usInfoList?has_content >
     	 <#list usInfoList as usInfo>
			<tr class="table_padding" class="mousechange">
				<td align="center">${(usInfo.usNum)!''}</td>
				<td><div align="left">${(usInfo.usName)!''}</div></td>
				<td>${(usInfo.planEstimate)!''}</td>
				<td>${(usInfo.taskActuals)!''}</td>
				<td>${(usInfo.taskEstimates)!''}</td>
				<td>${(usInfo.taskTodos)!''}</td>
				<td>${(usInfo.state)!''}</td>
				<td><a id="bntTaskShow" class="tooltips" href="#" onclick="showTasks('${(usInfo.usNum)!''}')">
				<img src="${base}/images/icon/page.gif" /><span>Show&nbsp;&nbsp;tasks</span> </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<!--<button type="button" id="bntTaskShow" onclick="showTasks('${(usInfo.usNum)!''}')">Show tasks</button>--></td>
			</tr>
			 </#list>
 		 </#if>
          
        </table>
        <table width="100%" border="0">
	        <tr></tr>
			<tr><td align="certer">
			     <div class="table_link"><ul>
				<li>
					<a href="#" onclick="refreshAll()">Refresh</a>
				</li>
				</ul></div>
				</td>
			</tr>
			</table>
</form>
</body>
</html>
