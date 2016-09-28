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
<form id="typeListForm" action="" method="POST">
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Meta Data Management
		&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doPageRedirect('goTypeAdd.do')">
		<img src="${base}/images/icon/11.png" /><span>Add&nbsp;&nbsp;Users</span> </a>
</div>
	</td><!--<td align="right">
		<div class="table_link"><ul>		      	
          	<li><a href="#" onclick="doPageRedirect('goTypeAdd.do')">Add</a></li>
        </ul></div>
	</td>--></tr>
</table>
<div class="box">
      <table class="tlist" id="pageTable" width="100%">
          <thead>
		        <th width="20%">Type Name</th>		       
		        <th width="60%">Description</th>
		        <th width="20%">Action</th>
		</thead>			
		<tbody>	
         <#if enumTypeList?has_content >
     	 <#list enumTypeList as type>
			<tr class="mousechange">			
				<td align="center">${(type.typeName)!''}</td>				
				<td align="center">${(type.description)!''}</td>
				<td align="center">
				
				<a class="tooltips" href="#"  onclick="doPageRedirect('goTypeUpdate.do?typeId=${(type.typeId)}')">
				<img src="${base}/images/icon/24.png" /><span>Edit</span> </a>&nbsp;&nbsp;&nbsp;
				<a class="tooltips" href="#"  onclick="doPageRedirect('goValueList.do?typeId=${(type.typeId)}')">
				<img src="${base}/images/icon/37.png" /><span>Manage</span> </a>&nbsp;&nbsp;&nbsp;
				</td>
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
