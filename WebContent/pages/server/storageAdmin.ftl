<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>

</script>
</head>

<body>
<form id="storageListForm" action="" method="POST">
<#if navigation?has_content>
	${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Storage Management&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doPageRedirect('goStorageReg.do')">
		<img src="${base}/images/icon/11.png" /><span>&nbsp;&nbsp;Register&nbsp;&nbsp;</span> </a></div>
	</td><!--<td align="right">
		<div class="table_link"><ul>		      	
          	<li><a href="#" onclick="doPageRedirect('goStorageReg.do')">Register</a></li>
        </ul></div>
	</td>--></tr>
</table>
<div class="box">
      <table class="display" id="pageTable" width="100%">
          <thead>
		        <th width="30%">Name</th>
		        <th width="10%">type</th>
		        <th width="10%">Capacity</th>
		        <th width="10%">SN</th>
		        <th width="10%">PN</th>
		        <th width="10%">Slot</th>
		        <th width="10%">Slot In Used</th>
		        <th width="15%">Description</th>
		        <th width="5%">Action</th>
		</thead>	
		<tbody>	
         <#if storageList?has_content >
     	 <#list storageList as storage>
			<tr class="mousechange">			
				<td align="center"><a href="#" onclick="doPageRedirect('goStorageView.do?storageId=${(storage.storageId)}')">${(storage.storageName)!''}</td>
				<td align="center">${(storage.storageType)!''}</td>
				<td align="center">${(storage.capacity)!''}</td>
				<td align="center">${(storage.sn)!''}</td>
				<td align="center">${(storage.pn)!''}</td>
				<td align="center">${(storage.slotNum)!''}</td>
				<td align="center">${(storage.slotUsed)!''}</td>
				<td align="center">${(storage.description)!''}</td>
				<td align="right">
				<div class="table_link">				    
				    <a class="tooltips" href="#" onclick="doPageRedirect('goStorageUpdate.do?storageId=${(storage.storageId)}')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;</span></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
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
