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
<table width="100%" border="0">
	<tr>
		<td align="left">
			<div class="title">Quix defects</div>
		</td>
	</tr>
</table>
<div class="box">
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <th width="10%">Defect No.</th>
		        <th width="38%">Defect Info</th>
		        <th width="6%">State</th>
		        <th width="6%">Priority</th>
		        <th width="8%">Submitted By</th>
		        <th width="6%">Developer</th>
		        <th width="10%">Submit date</th>
		        <th width="10%">Last update:</th>
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
				<td align="center">${(mysubitteddefect.submitDate)!''}</td>
				<td align="center">${(mysubitteddefect.lastupdateDate)!''}</td>
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
