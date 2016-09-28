<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
	function showChanges(commitId){
		$.ajax( {
			type : 'get',
			url : '${base}/mytitan/showChangesList.do?t=' + new Date().getTime(),
			data : { 'commitId' : commitId },
			success : function(msg) {				      
				$.jBox.open(msg, "Change List", 'auto', 400, { showType: 'show', iframeScrolling: 'yes' });
			}
		});
	}
</script>
</head>

<body>
<form id="defectinfoId" action="" method="POST">
<div class="box">
	  <table width="100%" border="0">
			<tr><td align="left">
				<div class="title">My Commit</div>
				</td>
			</tr>
	  </table>
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <th width="10%">Project name</th>
		        <th width="10%">Committed by</th>
		        <th width="10%">Committed time</th>
		        <th width="8%">Version</th>
		        <th width="8%">Code changed</th>
		        <th width="25%">Comment</th>
		        <th width="8%" ></th>
			</thead>	
		<tbody>	
         <#if commitList?has_content >
     	 <#list commitList as commit>
			<tr class="mousechange">
				<td align="center">${(commit.projectName)!''}</td>
				<td align="center">${(commit.committedBy)!''}</td>
				<td align="center">${(commit.commitTime)!''}</td>
				<td align="center">${(commit.revision)!''}</td>
				<td align="center">${(commit.codeChange)!''}</td>
				<td align="left">${(commit.comment)!''}</td>
				<td>
				<a id="bntTaskShow" class="tooltips" href="#" onclick="showChanges('${(commit.commitreportId)!''}')">
				<img src="${base}/images/icon/page.gif" /><span>Show&nbsp;&nbsp;changes</span> </a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<!--<button type="button" id="bntTaskShow" onclick="showChanges('${(commit.commitreportId)!''}')">Show changes</button>--></td>
			</tr>
			 </#list>
			 </#if>
      </tbody>    
        </table>
</div>
</form>
</body>
</html>
