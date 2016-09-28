<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script type="text/javascript" language="javascript" class="init">
		$(document).ready(function() {
			$('#pageTable1').DataTable( {
				dom: 'lfrtip<"clear">T',
				tableTools: {
		            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
		        }
			});
		});	
	</script>
<script>
	function refreshAll(){
		if(confirm('Confirm refresh, it will take a few minutes?')){
			$.ajax( {
				type : 'get',
				url : '${base}/mytitan/refreshDefect.do?t=' + new Date().getTime(),
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
			url : '${base}/mytitan/getDefectTask.do?t=' + new Date().getTime(),
			data : { 'usNum' : usNum },
			success : function(msg) {				      
				jBox.alert(msg);
			}
		});
	}
	
	function refreshPage(){
		doPageRedirect('mydefectList.do')
	}
	
	function getDefectDetailInfo(defectNo){
		$.ajax({
		    type: 'post',
		    url: '${base}/test/getDefectDetailInfo.do?t=' + new Date().getTime(),
		    dataType: "text",
		    data: {
		        'defectNo': defectNo,
		    },
		    async: false,
		    success: function(msg) {
		        jBox.alert(msg);
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
			<div class="title">My Defect</div>
		</td>
	</tr>
</table>
<div class="box">
	  <table width="100%" border="0">
			<tr><td align="left">
				<div class="title">My submitted：</div>
				</td>
			</tr>
	  </table>
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <th width="10%">Defect No.</th>
		        <th width="38%">Defect Info</th>
		        <th width="6%">State</th>
		        <th width="6%">Priority</th>
		        <th width="8%">Submitted By</th>
		        <th width="6%">Developer</th>
		        <th width="10%">Last update:</th>
		        <th width="10%"></th>
			</thead>	
		<tbody>	
         <#if mysubitteddefectList?has_content >
     	 <#list mysubitteddefectList as mysubitteddefect>
			<tr class="mousechange">
				<td align="center"><a href="#" onclick="getDefectDetailInfo('${(mysubitteddefect.defectNum)!''}')">${(mysubitteddefect.defectNum)!''}</a></td>
				<td align="center">${(mysubitteddefect.defectName)!''}</td>
				<td align="center">${(mysubitteddefect.state)!''}</td>
				<td align="center">${(mysubitteddefect.priority)!''}
				<td align="center">${(mysubitteddefect.submittedBy)!''}</td>
				<td align="center">${(mysubitteddefect.developer)!''}</td>
				<td align="center">${(mysubitteddefect.lastupdateDate)!''}</td>
				<td align="center">
				<a id="bntTaskShow" class="tooltips" href="#" onclick="showTasks('${(mysubitteddefect.defectNum)!''}')">
				<img src="${base}/images/icon/page.gif" /><span>Show&nbsp;&nbsp;tasks</span> </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<!--<button type="button" id="bntTaskShow" onclick="showTasks('${(mysubitteddefect.defectNum)!''}')">Show tasks</button>--></td>
			</tr>
			 </#list>
			 </#if>
      </tbody>    
        </table>
        
        <table width="100%" border="0">
			<tr><td align="left">
				<div class="title">My own：</div>
				</td>
			</tr>
	  </table>
      <table class="display" id="pageTable1" width="100%">
          <thead>
		        <th width="10%">Defect No.</th>
		        <th width="40%">Defect Info</th>
		        <th width="10%">State</th>
		        <th width="10%">Priority</th>
		        <th width="10%">Submitted By</th>
		        <th width="10%">Developer</th>
		        <th width="10%">Last update:</th>
			</thead>	
		<tbody>	
         <#if myowneddefectList?has_content >
     	 <#list myowneddefectList as myowneddefect>
			<tr class="mousechange">
				<td align="center"><a href="#" onclick="getDefectDetailInfo('${(mysubitteddefect.defectNum)!''}')">${(myowneddefect.defectNum)!''}</a></td>
				<td align="center">${(myowneddefect.defectName)!''}</td>
				<td align="center">${(myowneddefect.state)!''}</td>
				<td align="center">${(myowneddefect.priority)!''}
				<td align="center">${(myowneddefect.submittedBy)!''}</td>
				<td align="center">${(myowneddefect.developer)!''}</td>
				<td align="center">${(myowneddefect.lastupdateDate)!''}</td>
			</tr>
			 </#list>
		</#if>
      </tbody>    
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
        </div>
	</div>
</div>
</form>
</body>
</html>
