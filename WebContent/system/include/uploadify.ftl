<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />

<link rel="stylesheet" type="text/css" href="${base}/css/uploadify.css" />
<script type="text/javascript" src="${base}/js/uploadify/jquery.uploadify.js"></script>
<script type="text/javascript">
$(document).ready(function(){ 
	$('#file_upload').uploadify({
		'swf'             : '${base}/js/uploadify/uploadify.swf',
		'uploader'        : 'doTestcaseBatchUpload.do',
		'checkExisting'   : '${base}/js/uploadify/check-exists.php',
		'fileObjName'     : 'file',
		'formData'        : {},
		'auto'            : false,
		'removeCompleted' : false,
		'requeueErrors'   : true,
		'onUploadStart' : function(file) {
            console.log('Attempting to upload: ' + file.name);
            //$("#file_upload").uploadify("settings", "someOtherKey", 2);
        },
		'onUploadSuccess' : function(file, data, response) {
		}
	});
});
</script>
