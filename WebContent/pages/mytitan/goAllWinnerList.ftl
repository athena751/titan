<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
	function complete(winnerId){
	    doPageRedirect('completeWinner.do?winnerId=' + winnerId);
	}
</script>
</head>

<body>
<form id="inboxform" action="" method="POST">
<div class="box">
	  <table width="100%" border="0">
			<tr><td align="left">
				<div class="title">All Winner</div>
				</td>
			</tr>
	  </table>
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <th width="10%">Name</th>
		        <th width="10%">User Name</th>
		        <th width="10%">Date</th>
		        <th width="8%">Status</th>
		        <th width="8%">Action</th>
			</thead>	
		<tbody>	
         <#if winnerList?has_content >
     	 <#list winnerList as winner>
			<tr class="mousechange">
				<td align="center">${(winner.goodsName)!''}</td>
				<td align="center">${(winner.userCode)!''}</td>
				<td align="center">${(winner.strcreateDate)!''}</td>
				<td align="center">${(winner.status)!''}</td>
				<td align="center">
					<#if '${(winner.status)!""}'=='submitted'>
						<a class="tooltips" href="#" onclick="complete('${(winner.winnerId)}')"><img src="${base}/images/icon/ipod.gif" /><span>Finish&nbsp;&nbsp;Order</span></a>
					</#if>
				</td>
			</tr>
			 </#list>
			 </#if>
      </tbody>    
        </table>
</div>
</form>
</body>
</html>
