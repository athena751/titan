<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>	
<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>

<script>

$(document).ready(function() {
            var type = $("input[name='settingType']:checked").val();
			
			if($('#settingType').val() == 'SVN' || type == 'SVN' ){
				$('#divSvn').show();
				$('#divRally').hide();
				$('#divQuix').hide();
				$('#btnSVN').show();
				$('#btnRally').hide();
				$('#btnQuix').hide();
			}
			else if($('#settingType').val() == 'Rally' || type == 'Rally' ){
				$('#divSvn').hide();
				$('#divRally').show();
				$('#divQuix').hide();
				$('#btnSVN').hide();
				$('#btnRally').show();
				$('#btnQuix').hide();
			}
			else if($('#settingType').val() == 'Quix' || type == 'Quix'){
				$('#divSvn').hide();
				$('#divRally').hide();
				$('#divQuix').show();
				$('#btnSVN').hide();
				$('#btnRally').hide();
				$('#btnQuix').show();
			}
			else{
				$('#divSvn').show();
				$('#divRally').hide();
				$('#divQuix').hide();
				$('#btnSVN').show();
				$('#btnRally').hide();
				$('#btnQuix').hide();
			}
			$('#pageTableSvn').DataTable( {
				dom: 'lfrtip',
				tableTools: {
		            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
		        }
			});
			$('#pageTableRally').DataTable( {
				dom: 'lfrtip',
				tableTools: {
		            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
		        }
			});
			$('#pageTableQuix').DataTable( {
				dom: 'lfrtip',
				tableTools: {
		            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
		        }
			});
		});	
function doUpdateSvn(){
	if(checkBox('update','reportaryIdAry')){
		doFormSubmit('reportaryForm','${base}/project/goReportaryUpdate.do');
	}
}

function doUpdateRally(){
	if(checkBox('update','rallyNameIdAry')){
		doFormSubmit('reportaryForm','${base}/project/goUpdateRally.do');
	}
}

function doUpdateQuix(){
	if(checkBox('update','quixNameIdAry')){
		doFormSubmit('reportaryForm','${base}/project/goUpdateQuix.do');
	}
}

function checkBox(type,name){
	var roleIds = document.getElementsByName(name);
	var count = 0;
	if(roleIds != null && roleIds.length > 0){
     for(var i = 0 ; roleIds.length > i; i++){
        if(roleIds[i].checked){
	    	count = count + 1;
	   	}
     }
	}
	 if(count == 0){
		 alert('Please select at least one!');
		 return false;
	 }else if (type=='update' && count > 1){
	 	 alert('Please choose only one!');
	 	 return false;
	 }else{
	 	 return true;
	 }
}

function syncCommit(reportaryId){
	$.ajax( {
		type : 'post',
		url : '${base}/project/syncCommit.do?t=' + new Date().getTime(),
		async: true,
		data : { 'reportaryId' : reportaryId },
		beforeSend:function(){jBox.tip("Refreshing...", 'loading');},
		success : function(msg) {				      
			window.setTimeout(function () { jBox.tip('Refresh success!', 'success'); }, 2000);
		}
	});
}

function syncRally(projectNameInRally, component){
	$.ajax( {
		type : 'post',
		url : '${base}/project/syncRally.do?t=' + new Date().getTime(),
		data : { 
			'projectnameinRally' : projectNameInRally,
			'component' : component
		 },
		beforeSend:function(){jBox.tip("Refreshing...", 'loading');},
		success : function(msg) {				      
			window.setTimeout(function () { jBox.tip('Refresh success!', 'success'); }, 2000);
		}
	});
}

function syncQuix(projectNameInQuix, component){
	$.ajax( {
		type : 'post',
		url : '${base}/project/syncQuix.do?t=' + new Date().getTime(),
		data : { 
			'projectnameinQuix' : projectNameInQuix,
			'component' : component
		 },
		beforeSend:function(){jBox.tip("Refreshing...", 'loading');},
		success : function(msg) {				      
			window.setTimeout(function () { jBox.tip('Refresh success!', 'success'); }, 2000);
		}
	});
}

function showSVNDetail(reportaryId){
	doFormSubmit('reportaryForm','${base}/mytitan/goReportaryView.do?reportaryId='+reportaryId);
}
function showRallyDetail(projectnameinRally, component){
	doFormSubmit('reportaryForm','${base}/project/goRallyDetailView.do?projectnameinRally='+projectnameinRally+'&component='+component);
}
function showQuixDetail(projectnameinQuix, component){
	doFormSubmit('reportaryForm','${base}/project/goQuixDetailView.do?projectnameinQuix='+projectnameinQuix+'&component='+component);
}
function doDeleteSvn(){
	if(checkBox('update','reportaryIdAry')){
		if(confirm('Delete repository will remove all the submit info, are you confirm to remove?')){
			doFormSubmit('reportaryForm','${base}/project/doReportaryDelete.do');
		}
	}
}
function doDeleteRally(){
	if(checkBox('update','rallyNameIdAry')){
		if(confirm('Are you confirm to remove?')){
			doFormSubmit('reportaryForm','${base}/project/doRallyDelete.do');
		}
	}
}
function doDeleteQuix(){
	if(checkBox('update','quixNameIdAry')){
		if(confirm('Are you confirm to remove?')){
			doFormSubmit('reportaryForm','${base}/project/doQuixDelete.do');
		}
	}
}
function changeType(){
	var type = $("input[name='settingType']:checked").val()
	if(type == 'SVN' ){
		$('#divSvn').show();
		$('#btnSVN').show();
		$('#divRally').hide();
		$('#btnRally').hide();
		$('#divQuix').hide();
		$('#btnQuix').hide();
	}
	else if(type == 'Rally' ){
		$('#divSvn').hide();
		$('#btnSVN').hide();
		$('#divRally').show();
		$('#btnRally').show();
		$('#divQuix').hide();
		$('#btnQuix').hide();
	}
	else if(type == 'Quix' ){
		$('#divSvn').hide();
		$('#btnSVN').hide();
		$('#divRally').hide();
		$('#btnRally').hide();
		$('#divQuix').show();
		$('#btnQuix').show();
	}
}
</script>
</head>

<body>
		<form id="reportaryForm" action="" method="POST">
			<input type="hidden" id="settingType" value="${(settingType)!''}" />
			<div class="title">
				Project settings
			</div>
			<table width="100%" border="0">
				<tr>
					<td >
						<div class="box">
							<br>
							<br>
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td WIDTH="80%" ALIGN="LEFT" VALIGN="TOP">
										<div style="min-height: 40px">	
											<h3>																	
												<a herf="###" onclick="changeType()"><input	type="radio" name="settingType" value="SVN" checked
													<#if '${settingType!""}'=='SVN'>checked</#if>/>SVN Setting</a>&nbsp;&nbsp;&nbsp;| &nbsp;&nbsp;													
												<a herf="###" onclick="changeType()"><input	type="radio" name="settingType" value="Rally"
													<#if '${settingType!""}'=='Rally'>checked</#if>/>Rally Setting</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp; 
												<a herf="###" onclick="changeType()"><input type="radio" name="settingType" value="Quix"
													<#if '${settingType!""}'=='Quix'>checked</#if>/>QuIX Setting</a>													
											</h3>
										</div>
									</td>
									<td WIDTH="20%" align="right">
									<div id="btnSVN" class="table_link" style="display: none;">
										<ul>
											<li>
												<a href="#" onclick="doDeleteSvn()">Delete</a>
											</li>
											<li>
												<a href="#" onclick="doUpdateSvn()">Update </a>
											</li>
											<li>
												<a href="#"	onclick="doPageRedirect('${base}/project/goProjectAddReport.do?projectId=${(projectId)}')">Add</a>
											</li>
										</ul>
									</div>
									
									 <div id="btnRally" class="table_link" style="display: none;">
									        <ul>
									       	  	<li><a href="#" onclick="doDeleteRally()">Delete</a></li>
									          	<li><a href="#" onclick="doUpdateRally()">Update</a></li>
									          	<li><a href="#" onclick="doPageRedirect('${base}/project/goProjectAddRally.do?projectId=${(projectId)}')">Add</a></li>
									        </ul>
									 </div>
									 
									 <div id="btnQuix" class="table_link" style="display: none;">
									        <ul>
									       	  	<li><a href="#" onclick="doDeleteQuix()">Delete</a></li>
									          	<li><a href="#" onclick="doUpdateQuix()">Update</a></li>
									          	<li><a href="#" onclick="doPageRedirect('${base}/project/goProjectAddQuix.do?projectId=${(projectId)}')">Add</a></li>
									        </ul>
									 </div>
									</td>
								</tr>
							</table>
						</div>
						</div>
					</td>
				</tr>
				<tr>
					<td width="70%" valign="top">
						<div id="divSvn" style="display: none;">
							<table id="pageTableSvn" width="99%" border="0" cellpadding="0"	cellspacing="0" class="tlist">
								<thead>
									<th width="4%" class="table_header_complex_c">
										<!--<input type="checkbox" id="allId" onclick="doAll(this ,'groupIdAry')" />-->
									</th>
									<th width="15%" class="table_header_complex_c">
										Module
									</th>
									<th width="30%" class="table_header_complex_c">
										Url
									</th>
									<th width="15%" class="table_header_complex_c">
										Auto refresh
									</th>
									<th width="15%" class="table_header_complex_c">
										Remark
									</th>
									<th width="30%" class="table_header_complex_c">
										Action
									</th>
								</thead>
								<tbody>
									<#if reportaryList?has_content > <#list reportaryList as
									reportary>
									<tr class="table_padding"
										onMouseOver="this.style.backgroundColor='#F7F7F7';"
										onMouseOut="this.style.backgroundColor='#FFFFFF';">
										<td align="center">
											<input type="radio" id="reportaryIdAry" name="reportaryIdAry"
												value="${(reportary.reportaryId)!''}" />
										</td>
										<td align="center">
											${(reportary.moduleName)!''}
										</td>
										<td align="center">
											${(reportary.svnUrl)!''}
										</td>
										<td align="center">
											<#if '${reportary.autoRefresh!""}'=='1'>Yes<#else>No</#if>
										</td>
										<td align="center">
											${(reportary.remark)!''}
										</td>
										<td align="center">
											<button type="button" id="btnSyncSvn"
												onclick="syncCommit('${(reportary.reportaryId)!''}')">
												Sync commit
											</button>
											&nbsp;
											<button type="button" id="btnDetail"
												onclick="showSVNDetail('${(reportary.reportaryId)!''}')">
												View detail
											</button>
										</td>
									</tr>
									</#list> </#if>
								</tbody>
							</table>
						</div>
						<div id="divRally" style="display: none;">
							<div class="clear"></div>
							<table id="pageTableRally" width="100%" border="0"
								cellpadding="0" cellspacing="0" class="tlist">
								<thead>
									<th width="4%" class="table_header_complex_c">
										<!--<input type="checkbox" id="allId" onclick="doAll(this ,'groupIdAry')" />-->
									</th>
									<th width="25%" class="table_header_complex_c">
										Name in rally
									</th>
									<th width="25%" class="table_header_complex_c">
										Component
									</th>
									<th width="25%" class="table_header_complex_c">
										Auto refresh
									</th>
									<th width="25%" class="table_header_complex_c">
										Action
									</th>
								</thead>
								<tbody>
									<#if projectRallyList?has_content > <#list projectRallyList as
									projectRally>
									<tr class="table_padding"
										onMouseOver="this.style.backgroundColor='#F7F7F7';"
										onMouseOut="this.style.backgroundColor='#FFFFFF';">
										<td align="center">
											<input type="radio" id="rallyNameIdAry" name="rallyNameIdAry"
												value="${(projectRally.rallyquixId)!''}" />
										</td>
										<td align="center">
											${(projectRally.nameinRally)!''}
										</td>
										<td align="center">
											${(projectRally.component)!''}
										</td>
										<td align="center">
											<#if '${projectRally.autoRefresh!""}'=='1'>Yes<#else>No</#if>
										</td>
										<td align="center">
											<button type="button" id="btnSyncRally"
												onclick="syncRally('${(projectRally.nameinRally)!''}','${(projectRally.component)!''}')">
												Sync rally
											</button>
											&nbsp;
											<button type="button" id="btnDetail"
												onclick="showRallyDetail('${(projectRally.nameinRally)!''}','${(projectRally.component)!''}')">
												View detail
											</button>
										</td>
									</tr>
									</#list> </#if>
								</tbody>
							</table>
						</div>
						<div id="divQuix" style="display: none;">
							
							
							<div class="clear"></div>
							<table id="pageTableQuix" width="100%" border="0" cellpadding="0"
								cellspacing="0" class="tlist">
								<thead>
									<th width="4%" class="table_header_complex_c">
										<!--<input type="checkbox" id="allId" onclick="doAll(this ,'groupIdAry')" />-->
									</th>
									<th width="25%" class="table_header_complex_c">
										Name in Quix
									</th>
									<th width="25%" class="table_header_complex_c">
										Component
									</th>
									<th width="25%" class="table_header_complex_c">
										Auto refresh
									</th>
									<th width="25%" class="table_header_complex_c">
										Action
									</th>
								</thead>
								<tbody>
									<#if projectQuixList?has_content > <#list projectQuixList as
									projectQuix>
									<tr class="table_padding"
										onMouseOver="this.style.backgroundColor='#F7F7F7';"
										onMouseOut="this.style.backgroundColor='#FFFFFF';">
										<td align="center">
											<input type="radio" id="quixNameIdAry" name="quixNameIdAry"
												value="${(projectQuix.rallyquixId)!''}" />
										</td>
										<td align="center">
											${(projectQuix.nameinQuix)!''}
										</td>
										<td align="center">
											${(projectQuix.component)!''}
										</td>
										<td align="center">
											<#if '${projectQuix.autoRefresh!""}'=='1'>Yes<#else>No</#if>
										</td>
										<td align="center">
											<button type="button" id="btnSyncQuix"
												onclick="syncQuix('${(projectQuix.nameinQuix)!''}','${(projectQuix.component)!''}')">
												Sync QuIX
											</button>
											&nbsp;
											<button type="button" id="btnDetail"
												onclick="showQuixDetail('${(projectQuix.nameinQuix)!''}','${(projectQuix.component)!''}')">
												View detail
											</button>
										</td>
									</tr>
									</#list> </#if>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
