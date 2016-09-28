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
});


function viewDetail(){
	doPageRedirect('comsumptionCount.do');
}

function report() {
            var textbox = "<div style='padding:10px;'>How many points you have：<input type='text' id='rcPoint' name='rcPoint' value='' /></div>";
            var submit = function (v, h, f) {
                if (f.rcPoint == '') {
                    jBox.tip("How many points do you have?", 'error', { focusId: "rcPoint" }); 
                    return false;
                }

                $.ajax({
					type : 'get',
					url : '${base}/mytitan/calculatePoint.do?t=' + new Date().getTime(),
					dataType: "text",
					data : { 
							 'rcPoint' : f.rcPoint
					 },
					success : function(msg) {	
						jBox.tip(msg);
						return true;
					}
				});
                
            };
            jBox(textbox, { title: "Report", submit: submit, loaded: function (h) {
            }
            });
        }
        
function complete(consumeId){
    doPageRedirect('completeConsume.do?consumeId=' + consumeId);
}
function gamble(){
    doPageRedirect('gambleConsume.do');
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
			  	My Orders <a class="tooltips" href="#" onclick="report()"><img src="${base}/images/icon/57.png" /><span>Report</span> </a><a class="tooltips" href="#" onclick="gamble()"><img src="${base}/images/icon/58.png" /><span>Report</span> </a>
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
		     	<th width="8%" >User</th>
		     	<th width="8%" >Action</th>
			</thead>
          
          <#if consumeList?has_content >
     	 <#list consumeList as consume>
			<tr class="table_padding" class="mousechange">
				<td align="left">${(consume.strConsumeDate).toString()!""}</td>
				<td><div align="center">${(consume.goodsName)!''}</div></td>
				<td><div align="center">${(consume.price)!''}</div></td>
				<td align="center">${(consume.status)!''}</td>
				<td align="center">${(consume.userCode)!''}</td>
				<td align="center">
					<#if '${consume.status!""}'=='submitted'>
						<#if '${consume.type!""}'!='3'>
				    		<a class="tooltips" href="#" onclick="complete('${(consume.consumeId)}')"><img src="${base}/images/icon/ipod.gif" /><span>Finish&nbsp;&nbsp;Order</span></a>
						</#if>
					</#if>
				</td>
			</tr>
			 </#list>
 		 </#if>
          
        </table>
</form>
</body>
</html>
