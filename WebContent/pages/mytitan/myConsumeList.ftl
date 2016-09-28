<!DOCTYPE html>
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

function cancel(consumeId){
     if(confirm('Are you sure to cancel this order?')){
      doPageRedirect('cancellConsum.do?consumeId=' + consumeId);
     }
}

function viewDetail(){
	doPageRedirect('comsumptionCount.do');
}
function gambling(){
	doPageRedirect('goGamblingList.do');
}
</script>

<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
</head>

<body>
<form id="consumeListForm" action="" method="POST">
<#if navigation?has_content>
	${navigation}
</#if>
<table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			  	My Orders <a class="tooltips" href="#" onclick="viewDetail()"><img src="${base}/images/icon1/web_s.gif" /><span>View&nbsp;&nbsp;Order</span></a><a class="tooltips" href="#" onclick="gambling()"><img src="${base}/images/icon/57.png" /><span>Gamble</span> </a>
			</div>
		</div>
			</td><td align="right">
		</td></tr>
	</table>
<div class="showlist_main showlist_mainnopadding"></div>
 
	   <div class="table_link">
        <ul>
        </ul></div>
<div class="clear"></div>
        <table id="pageTable" class="display" width="100%" border="0" cellpadding="0" cellspacing="0">
         	<thead>
				<th width="10%">Date</th>
		        <th width="8%" >Name</th>
		        <th width="8%" >Cost</th>
		     	<th width="8%" >State</th>
		     	<th width="8%" >Action</th>
			</thead>
          
          <#if consumeList?has_content >
     	 <#list consumeList as consume>
			<tr class="table_padding" class="mousechange">
				<td align="left">${(consume.consumeDate).toString()!""}</td>
				<td><div align="center">${(consume.goodsName)!''}</div></td>
				<td><div align="center">${(consume.price)!''}</div></td>
				<td align="center">${(consume.status)!''}</td>
				<td align="center">
				<#if '${(consume.status)!""}'=='submitted'>
				   <a class="tooltips" href="#" onclick="cancel('${(consume.consumeId)}')"><img src="${base}/images/icon1/er_s.gif" /><span>Cancell&nbsp;&nbsp;Order</span></a>
				</#if>
				</td>
			</tr>
			 </#list>
 		 </#if>
          
        </table>
</form>
</body>
</html>
