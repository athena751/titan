<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<script>

function cancel() {
   doPageRedirect('typeList.do')
}

</script>
</head>

<body>
<form id="valueListForm" action="" method="POST">
<table width="40%" border="0">
	<tr><td align="left">
		<div class="title">Meta Data value of ${(enumType.typeName)!''}</div>
	</td><td align="right">
		<div class="table_link"><ul>		      	
          	<li><a href="#" onclick="doPageRedirect('goValueAdd.do?typeId=${(enumType.typeId)!''}')">Add</a></li>
        </ul></div>
	</td></tr>
</table>
<div class="box">
      <table class="tlist" id="pageTable" width="40%">
          <thead>
		        <th width="10%">value Name</th>		       
		        <th width="15%">Description</th>
		        <th width="5%">Action</th>
		</thead>			
		<tbody>	
         <#if enumValueList?has_content >
     	 <#list enumValueList as value>
			<tr class="mousechange">			
				<td align="left">${(value.enumValue)!''}</td>				
				<td align="center">${(value.description)!''}</td>
				<td align="right">
				<div class="table_link"><ul>
				    		    
<!--				<li><a href="#" onclick="doDisable('${(value.enumId)}')">Delete</a></li>                          --!>
				    <li><a href="#" onclick="doPageRedirect('goValueUpdate.do?enumId=${(value.enumId)}')">Edit</a></li>				   
				</ul></div>
			</tr>
			 </#list>
 		 </#if>
 		 </tbody>      
        </table>   
        <table width="40%" border="0">
	      <tr><td align="center">
	        <button class="c" type="button" onclick="cancel()" >Return</button>
	      <td></tr>
</table>  	
	</div>
</div>
</form>
</body>
</html>
