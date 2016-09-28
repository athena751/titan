<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<script type="text/javascript" src="${base}/js/jqplot/jquery.jqplot.js"></script>
<script type="text/javascript" src="${base}/js/jqplot/excanvas.js"></script>
<script type="text/javascript"  src="${base}/js/plugins/jqplot.pieRenderer.min.js"></script>
<script type="text/javascript"  src="${base}/js/plugins/jqplot.donutRenderer.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery.json-2.4.min.js"></script>
<link rel="stylesheet" href="${base}/css/jquery.jqplot.css" type="text/css"></link>

<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>

<script>
function showChart(){
	$("#chart_container").show();
	$.ajax( {
		type : 'get',
		url : '${base}/mytitan/getChartXml.do?t=' + new Date().getTime(),
		dataType: "json",
		success : function(data) {
			var view=[];
			for(var i=0;i<data.length; i++){	
					view.push([data[i].goodsName,data[i].price]);
			}
			var plot1 = $.jqplot('chart_container',[view], {
				seriesDefaults: {
					
	         		renderer: $.jqplot.PieRenderer,
	        		rendererOptions: {    
	           			showDataLabels: true,
	           			diameter:300,
	         		}
	     		},
	      		title : 'Recognition Points',
	       		legend: { 
	       			show:true,
	       			//location: 'e',
	       		},
       		
       		});		
		}
	});
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
</script>
</head>

<body>
 <form id="computionForm" name="computionForm" method="post">
 	<input type="hidden" id="json" name="json"/>
 	<#if navigation?has_content>
		${navigation}
	</#if>
	
	 
	 <table width="100%" border="0">
		<tr><td align="left">
			<div class="title">
			  	Recognition Points Consumption Report 
			  	<#if ccList?has_content >
					<a class="tooltips" href="#" onclick="showChart()">
					<img src="${base}/images/icon/chart_pie.gif" /><span>Show &nbsp;&nbsp;Chart</span> </a>
					<#if '${isManager!""}'=='Yes'>
						<a class="tooltips" href="#" onclick="report()">
						<img src="${base}/images/icon/57.png" /><span>Report</span> </a>
					</#if>
				</#if>
			</div>
		</div>
			</td><td align="right">
		</td></tr>
	</table>

<div class="box">
		<table id="pageTable" class="display" width="100%" border="0" cellpadding="0" cellspacing="0">
         	<thead>
				<th width="10%">Name</th>
		        <th width="8%" >Total</th>
		        <th width="8%" >Percent</th>
			</thead>
          
          <#if ccList?has_content >
     	 <#list ccList as cc>
			<tr class="table_padding" class="mousechange">
				<td><div align="center">${(cc.goodsName)!''}</div></td>
				<td align="center">${(cc.price).toString()!""}</td>
				<td><div align="center"></div></td>
			</tr>
			 </#list>
 		 </#if>
          
        </table>
<table width="100%" border="0">
	<tr>
		<td><div style="width:98%;height:500px;display:none " id="chart_container" align="center"></div></td>
	</tr>
</table>
</div>
</form>
</body>
</html>
