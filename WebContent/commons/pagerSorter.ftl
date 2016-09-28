<#macro sorterPageJs>
	<link rel="stylesheet" type="text/css" href="${base}/css/jquery.dataTables.css">
	<link rel="stylesheet" type="text/css" href="${base}/css/dataTables.tableTools.css">
	<link rel="stylesheet" type="text/css" href="${base}/css/shCore.css">
	<link rel="stylesheet" type="text/css" href="${base}/css/tip.css">
	<style type="text/css" class="init"></style>
	<script type="text/javascript" language="javascript" src="${base}/js/jquery.dataTables.js"></script>
	<script type="text/javascript" language="javascript" src="${base}/js/dataTables.tableTools.js"></script>
	<script type="text/javascript" language="javascript" src="${base}/js/shCore.js"></script>
</#macro>	

<#macro sorterPage pageTable="pageTable" pager="pager" sortLeft="0" sortRight="1">
	<script type="text/javascript" language="javascript" class="init">
		$(document).ready(function() {
			$('#pageTable').DataTable( {
				dom: 'lfrtip<"clear">T',
				tableTools: {
		            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
		        }
			});
		});	
	</script>
</#macro>	