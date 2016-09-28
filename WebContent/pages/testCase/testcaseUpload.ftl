<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/uploadify.css" />
<script type="text/javascript" src="${base}/js/uploadify/jquery.uploadify.js"></script>
<script type="text/javascript">
$(document).ready(function(){ 
  	var isNew = $("#isNew").val();
  	if(isNew=="true"){
  		$("#btnCancel").show();
	}

	$('#file_upload').uploadify({
		'swf'             : '${base}/js/uploadify/uploadify.swf',
		'uploader'        : 'doTestcaseBatchUpload.do',
		'checkExisting'   : '${base}/js/uploadify/check-exists.php',
		'fileObjName'     : 'file',
		'formData'        : {'caseId':document.getElementById("caseId").value},
		'auto'            : false,
		'removeCompleted' : false,
		'requeueErrors'   : true,
		'onUploadStart' : function(file) {
            //$("#file_upload").uploadify("settings", "someOtherKey", 2);
        },
		'onUploadSuccess' : function(file, data, response) {
			var table = document.getElementById("tblFile");
			var row = table.insertRow(table.rows.length);
			var cell1 = row.insertCell(0);
			var cell2 = row.insertCell(1);
			cell1.innerHTML = data;
			cell2.innerHTML = "<button type='button' class='bntDelTr'>Delete</button>";
		}
	});
	
  	//delete tr for table
 	$(".box").delegate(".bntDelTr", "click", function(){	
		var fileName = $(this).closest("tr").children("td").first().text();	
		$(this).closest("tr").remove();
		$.ajax({
				type : 'post',
				url : 'doFileDelete.do',
				data : { 
					'caseId' : document.getElementById("caseId").value,
					'delFileName' : fileName 
				},
				success : function(data) {
				}
		});
  	});
});
function onPrecious(){
	doFormSubmit('uploadform','goTestcaseUpdate.do');
}
function onBack(){
	doPageRedirect('testcaseList.do');
}
function onCancel(){
	var isNew = document.getElementById("isNew").value;
	if(isNew == "true"){
		doFormSubmit('uploadform','doTestcaseDelete.do');
	}
}
</script>
</head>

<body>
<form id="uploadform" method="post" enctype="multipart/form-data">
<input type="hidden" id="isNew" name="isNew" value="${isNew}">
<input type="hidden" id="caseId" name="caseId" value="${caseId}">
<#if navigation?has_content>
	 ${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">Test Case Upload</div>
	</td><td align="right">
	</td></tr>
</table>
<div class="box">
	<table class="tablecontent" width="100%">
		<tr>
			<td class="tablecontent_title" align="right" width="10%">File</td>
			<td align="left" width="90%">
				<table id="tblFile" width="100%">
				<#if fileList?has_content >
					<#list fileList as file >
					<tr>
						<td>${file}</td>
						<td><button type='button' class='bntDelTr'>Delete</button></td>
					</tr>
					</#list>
			 	</#if>      
				</table>
			</td>
		</tr>
		<tr>
			<td class="tablecontent_title" align="right" width="10%">File Upload</td>
			<td align="left" width="90%">
				<input type="file" name="file_upload" id="file_upload" />
				<a href="javascript:$('#file_upload').uploadify('upload', '*')">Upload Files</a>
				 | 
				<a href="javascript:$('#file_upload').uploadify('cancel', '*')">Clear the Queue</a>
			</td>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr><td align="center">
			<button type="button" id="btnPrecious" onclick="onPrecious()">Precious</button>
			<button type="button" id="btnCancel" onclick="onCancel()" style="display:none">Cancel</button>
			<button type="button" id="btnOK" onclick="onBack()">OK</button>
		</td></tr>
	</table>
</div>
</form>
</body>
</html>
