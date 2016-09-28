<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/zTree/zTreeStyle.css" />
<link rel="stylesheet" type="text/css" href="${base}/css/zTree/zTreePage.css" />
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.nodeControl.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="${base}/js/jquery.json-2.4.min.js"></script>
<script>
		/*var MoveTest = {
			dragTree2Dom: function(treeId, treeNodes) {
				return !treeNodes[0].isParent;
			}
		};*/
		var setting = {
			edit: {
				drag: {
					isCopy: false,
					isMove: true,
					prev: canPrev,
					next: canNext,
					inner: canInner
				},
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeDrag: beforeDrag,
				beforeDrop: beforeDrop,
				//onRightClick: OnRightClick,
				onDblClick: onDblClick,
				onClick: onLeftClick
			}
		};
		
		var zNodesJob = ${(testJobJson)};
		var zNodesCase = ${(testCaseJson)};

		function beforeDrag(treeId, treeNodes) {
			if(treeNodes[0].diyType == 'testJob' || treeNodes[0].diyType == 'caseFolder' || treeNodes[0].diyType == 'caseInPlan' || treeNodes[0].diyType == 'planFolder'){
				return false;
			}
			return true;
		}
		
		function beforeDrop(treeId, treeNodes, targetNode, moveType) {
			if(isCaseFromCases(treeNodes[0]) && isCaseToCases(treeNodes[0], targetNode)){
				return false;
			}
			return true;
		}
		
		
		function canPrev(treeId, nodes, targetNode){
			if(null != targetNode && (isCaseToCase(nodes[0], targetNode) || isPlanToPlan(nodes[0], targetNode))){
				return true;
			}
			if(null != targetNode && isCaseFromJob(nodes[0], targetNode) && isCaseToPlan(nodes[0], targetNode)){
				return true;
			}
			if(null != targetNode && isPlanFromJob(nodes[0], targetNode) && isPlanToCase(nodes[0], targetNode)){
				return true;
			}
			return false;
		}
		
		function canNext(treeId, nodes, targetNode){
			if(null != targetNode && (isCaseToCase(nodes[0], targetNode) || isPlanToPlan(nodes[0], targetNode))){
				return true;
			}
			if(null != targetNode && isCaseFromJob(nodes[0], targetNode) && isCaseToPlan(nodes[0], targetNode)){
				return true;
			}
			if(null != targetNode && isPlanFromJob(nodes[0], targetNode) && isPlanToCase(nodes[0], targetNode)){
				return true;
			}
			return false;
		}
		
		function canInner(treeId, nodes, targetNode){
			if(null != targetNode && (isCaseToJob(nodes[0], targetNode) || isCaseToCases(nodes[0], targetNode))){
				return true;
			}
			if(null != targetNode && (isPlanToJob(nodes[0], targetNode) || isPlanToPlans(nodes[0], targetNode))){
				return true;
			}
			return false;
		}
		
		function OnRightClick(event, treeId, treeNode) {
			if(isCaseFromJob(treeNode) || (isCaseFromPlan(treeNode) && isPlanFromJob(treeNode.getParentNode()))) {
				zTree = $.fn.zTree.getZTreeObj("testJobTree");
				zTree.selectNode(treeNode);
				showRMenu("node", event.clientX, event.clientY);
			}
		}
		
		function onDblClick(event, treeId, treeNode){
			if(isCaseFromCases(treeNode) || (isCaseFromPlan(treeNode) && isPlanFromPlans(treeNode.getParentNode()))){
				var strHtml = '';
				$.ajax( {
					type : 'post',
					url : 'getCaseInfo.do?t=' + new Date().getTime(),
					dataType: "text",
					data : { 'testCaseId': treeNode.id },
					async: false,
					success : function(msg) {
						strHtml = msg;
					}
				});
            	var submit = function (v, h, f) {
                	return true;
            	};
				jBox(strHtml, { title: treeNode.name, submit: submit, loaded: function (h) {
            	}
            	});
			}
		}
		
		function onLeftClick(event, treeId, treeNode) {
			if((treeNode.diyType == 'caseInPlan' || treeNode.diyType == 'case')) {
				var caseId = treeNode.id;
				$.ajax({
			        type: 'post',
			        url: 'getTestCaseDetail.do?t=' + new Date().getTime(),
			        dataType: "text",
			        data: {
			            'caseId': caseId
			        },
			        async: false,
			        success: function(msg) {
			            $("#caseDetail").html(msg);
			        }
			    });
			}
		}
		
		function showRMenu(type, x, y) {
			$("#rMenu ul").show();
			if (type=="root") {
				$("#m_del").hide();
				$("#m_check").hide();
				$("#m_unCheck").hide();
			} else {
				$("#m_del").show();
				$("#m_check").show();
				$("#m_unCheck").show();
			}
			rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

			$("body").bind("mousedown", onBodyMouseDown);
		}
		function hideRMenu() {
			if (rMenu) rMenu.css({"visibility": "hidden"});
			$("body").unbind("mousedown", onBodyMouseDown);
		}
		function onBodyMouseDown(event){
			if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
				rMenu.css({"visibility" : "hidden"});
			}
		}
		
		$(document).ready(function(){
			$("#moduleId").change(function(){
				var module = this.value;
        		filterByModule(module);
        	});
			$.fn.zTree.init($("#testJobTree"), setting, zNodesJob);
			$.fn.zTree.init($("#testCaseTree"), setting, zNodesCase);
			rMenu = $("#rMenu");
		});
		
		function filterByModule(module){
			var treeObj = $.fn.zTree.getZTreeObj("testCaseTree");
			var hiddenNodes = treeObj.getNodesByParam("isHidden", true);
			treeObj.showNodes(hiddenNodes);
			if("" == module){
				return;
			}
			var nodes = treeObj.getNodesByFilter(filter); 
			treeObj.hideNodes(nodes);
		}
		
		function filter(node){
			var module = document.getElementById("moduleId").value;//$("#moduleId").value;
			return (node.diyModule != module && node.diyModule != 'none');
		}
		
		function save(){
			var treeObj = $.fn.zTree.getZTreeObj("testJobTree");
			var nodes = treeObj.transformToArray(treeObj.getNodes());
			$("#testJobJson").val($.toJSON( nodes ));
			$("#testJobJsonDB").val('['+$.toJSON( nodes[0] )+']');
			doFormSubmit('testJobManageForm','doTestJobManageSave.do');
		}
		
		function precious(){
			doFormSubmit('testJobManageForm','goTestjobEdit.do');
		}
		
		function saveAndRun(){
			var treeObj = $.fn.zTree.getZTreeObj("testJobTree");
			var nodes = treeObj.transformToArray(treeObj.getNodes());
			$("#testJobJson").val($.toJSON( nodes ));
			$("#testJobJsonDB").val('['+$.toJSON( nodes[0] )+']');
			var type = "${(testjob.jobType)!''}";
			if("Manual" == type){
				doFormSubmit('testJobManageForm','goMTestJobManageRun.do');
			}
			else if("Automated" == type){
				doFormSubmit('testJobManageForm','doTestJobManageRun.do');
			}
		}
		
		function caseManageBox() {
			var treeObj = $.fn.zTree.getZTreeObj("testJobTree");
			var nodes = treeObj.getSelectedNodes();
			var caseId = nodes[0].id;
			var jobId = '';
			var planId = '';
			if('case' == nodes[0].diyType){
				jobId = nodes[0].getParentNode().id;
			}
			else if('caseInPlan'){
				jobId = nodes[0].getParentNode().getParentNode().id;
				planId = nodes[0].getParentNode().id;
			}
			var strHtml = '';
			$.ajax( {
				type : 'post',
				url : 'goCaseManageBox.do?t=' + new Date().getTime(),
				dataType: "text",
				data : { 'testjobId' : jobId,
						 'testCaseId': caseId
				 },
				async: false,
				success : function(msg) {
					strHtml = msg;
				}
			});
            var html = strHtml;

            var data = {};
            var states = {};
            states.state1 = {
                content: html,
                buttons: { 'OK': 1, 'Cancel': 0 },
                submit: function (v, h, f) {
                    if (v == 0) {
                        return true; // close the window
                    }
                    else {
                        h.find('.errorBlock').hide('fast', function () { $(this).remove(); });

                        //data.amount = f.amount;  
                        var paras = document.getElementsByName("testcasepara");//$("testcasepara");
                        var serverId = document.getElementById("serverId");//$("testcasepara");
                        var jsonCase = '';
                        jsonCase += '[{"jobId":"'+jobId+'",';
                        jsonCase += '"caseId":"'+caseId+'",';
                        jsonCase += '"planId":"'+planId+'",';
                        jsonCase += '"serverId":"'+serverId.value+'",';
                        jsonCase += '"paras":[';
                        for(var i = 0; i < paras.length; i++){
                        	if(paras[i].value == ''){
                        		$('<div class="errorBlock" style="display: none;">Please provide all the parameter</div>').prependTo(h).show('slow');
                            	return false;
                        	}
                        	jsonCase += '{"paraId":"'+paras[i].id+'",';
                        	jsonCase += '"paraValue":"'+paras[i].value+'"';
                        	jsonCase += '},';
                        }
                        jsonCase += ']';
                        
                        jsonCase += '}]';
                        jsonCase = jsonCase.replace('},]}]','}]}]');
                        /*
                        var objCase = new Object();
                        objCase.jobId = jobId;
                        objCase.caseId = caseId;
                        for(var i = 0; i < paras.length; i++){
                        	if(paras[i].value == ''){
                        		$('<div class="errorBlock" style="display: none;">Please provide all the parameter</div>').prependTo(h).show('slow');
                            	return false;
                        	}
                        	alert('objCase.' + paras[i].id + '=paras[i].value');
                        	//eval('objCase.' + paras[i].id + '=paras[i].value');
                        	objCase.a=paras[i].value;
                        	alert('guolai');
                        	
                        }*/
                        $.ajax( {
							type : 'post',
							url : 'doCaseManageBox.do?t=' + new Date().getTime(),
							dataType: "text",
							data : { 'testCaseParaJson' : jsonCase
							 },
							async: false,
							success : function(msg) {
								if(msg == 'success'){
									jBox.nextState('<div class="msg-div">Submitting...</div>');
			                        // 或 jBox.goToState('state2')
			                        window.setTimeout(function () { jBox.nextState('Success'); }, 10);
								}
							}
						});
                    }

                    return false;
                }
            };
            states.state2 = {
                content: '',
                buttons: { } // no buttons
            };
            states.state3 = {
                content: '',
                buttons: {'OK': 0 } // no buttons
            };

            jBox.open(states, 'Case Manage', 450, 'auto');
        }
</script>
<style type="text/css">
	.dom_line {margin:2px;border-bottom:1px gray dotted;height:1px}
	.domBtnDiv {display:block;padding:2px;border:1px gray dotted;background-color:powderblue}
	.categoryDiv {display:inline-block; width:335px}
	.domBtn {display:inline-block;cursor:pointer;padding:2px;margin:2px 10px;border:1px gray solid;background-color:#FFE6B0}
	.domBtn_Disabled {display:inline-block;cursor:default;padding:2px;margin:2px 10px;border:1px gray solid;background-color:#DFDFDF;color:#999999}
	.dom_tmp {position:absolute;font-size:12px;}
	div#rMenu {position:absolute; visibility:hidden; top:0; background-color: #555;text-align: left;padding: 2px;}
	div#rMenu ul li{
	margin: 1px 0;
	padding: 0 5px;
	cursor: pointer;
	list-style: none outside none;
	background-color: #DFDFDF;
</style>
</head>
<body>
<form id="testJobManageForm"  name="testJobManageForm" method="post">
	<input type="hidden" id="testjobId" name="testjobId" value="${(testjob.testjobId)!''}"/>
	<input type="hidden" id="testjobType" value="${(testjob.jobType)!''}"/>
	<input type="hidden" id="testJobJson" name="testJobJson"  value="${(testJobJson)!''}"/>
	<input type="hidden" id="testJobJsonDB" name="testJobJsonDB"  value="${(testJobJsonDB)!''}"/>
	<#if navigation?has_content>
	 	${navigation}
	</#if>
	<h1>Add plan case to ${testjob.testjobName}</h1>
	
	<center><table width="80%" border="0" cellpadding="0" cellspacing="0" class="tablecontent" >
		<tr>
		  <td align="right" class="tablecontent_title" width="10%">Job Name:</td>
		  <td align="left" width="90%">${testjob.testjobName!""}</td>
		
		</tr>
		<tr>
		  <td align="right" class="tablecontent_title" width="10%">Job Type:</td>
		  <td align="left" width="90%">${testjob.jobType!""}</td>
		</tr>
		<tr>
		  <td align="right" class="tablecontent_title" width="10%">Server:</td>
		  <td align="left" width="90%">
		   <#list jobserverList as server>
		    ${server.serverName!""} ${server.distro!""}
		   </#list> 
		  </td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="10%">Job Owner:</td>
			<td align="left" width="90%">
				<select id="ownerId" name="testjob.ownerId"  class="select" style="width:180px" disabled="disabled">
					<option value="" selected readonly="true"> Please Select</option>
					<#list userList as user>
					  <option value="${user.userId}" <#if '${user.userId!""}'=='${testjob.ownerId!""}'>selected</#if>>${user.userCode}</option>		            
				    </#list>
					
				</select>
				<span id="ownerIdMessage"></span>
			</td>
		</tr>
		<tr>
			<td align="right" class="tablecontent_title" width="10%">Project:</td>
			<td align="left" width="90%">
				<select id="projectId" name="testjob.projectId"  class="select" style="width:180px" disabled="disabled">
					<option value="" selected>Please Select</option>
					<#list projectList as project>
						<option value="${project.projectId}"<#if '${testjob.projectId!""}'=='${project.projectId!""}'>selected</#if>>${project.projectName}</option>
					</#list> 
				</select>
				<span id="projectIdMessage"></span>
			</td>
		</tr>
		<tr>
		  <td align="right" class="tablecontent_title" width="10%">Description:</td>
		  <td align="left" width="90%">${testjob.remark!""}</td>
		</tr>

	</table></center>
	
	<div class="content_wrap">
		<div class="vo"><br></div>
		<div class="zTreeDemoBackground left">
			<select id="moduleId" class="select" style="width:180px">
				<option value="" selected>Please Select Module</option>
				<#list moduleList as module>
					<option value="${module.moduleId}">${module.moduleName}</option>
				</#list> 
			</select>
			
			<ul id="testCaseTree" class="ztree"></ul>
		</div>
		<div class="right">		
			<table class="tablecontent" id="caseDetailTable" width="100%"  border="1">
				<tr><th><div id='caseDetail'></div><img src="${base}/images/arrow.png"/></th></tr>
				<tr ><th><font color="248fc3">Drag the test plans or test cases from the left box to the test job in right box and then click 'save'.</font></th></tr>
			</table>
		</div>
		<div class="middle">
			<button class="c" type="button" onclick="precious()">Precious</button>
			<button class="c" type="button" onclick="save()">Save</button>
			<button class="c" type="button" onclick="saveAndRun()">Save and run</button>
			<ul id="testJobTree" class="ztree"></ul>
		</div>
		
	</div>
	
	
</form>
<div id="rMenu">
	<ul>
		<li id="m_add" onclick="ShowDetail();">Detail</li>
		<li id="m_del" onclick="caseManageBox();">Edit</li>
	</ul>
</div>
</body>
</html>
