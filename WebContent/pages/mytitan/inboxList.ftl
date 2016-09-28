<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>
	$(document).ready(function() {
		$('#points', parent.document).html(${mypoint});
		$('#inbox', parent.document).html('(' + ${inboxCount} + ')');
	});

	function openBox(boxId){
		$.jBox("iframe:openTreasureBox.do?messageId=" + boxId, {
		    title: "Treasure Box",
		    width: 800,
		    height: 600,
		    buttons: {  }
		});
	}
</script>
</head>

<body>
<form id="inboxform" action="" method="POST">
<div class="box">
	  <table width="100%" border="0">
			<tr><td align="left">
				<div class="title">My Message</div>
				</td>
			</tr>
	  </table>
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <th width="10%">Name</th>
		        <th width="10%">Type</th>
		        <th width="10%">Date</th>
		        <th width="8%">Status</th>
		        <th width="8%">Action</th>
			</thead>	
		<tbody>	
         <#if inboxList?has_content >
     	 <#list inboxList as inbox>
			<tr class="mousechange">
				<td align="center">${(inbox.msgName)!''}</td>
				<td align="center">${(inbox.msgType)!''}</td>
				<td align="center">${(inbox.createTime)!''}</td>
				<td align="center">${(inbox.isReaded)!''}</td>
				<td align="center">
					<#if '${(inbox.isReaded)!""}'=='1'>
						<a class="tooltips" href="#" onclick="openBox('${(inbox.msgId)!''}')"><img src="${base}/images/icon/shield.gif" /><span>Open&nbsp;&nbsp;it</span></a>
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
