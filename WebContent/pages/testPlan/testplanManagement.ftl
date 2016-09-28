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
				onClick: onLeftClick
			}
		};
		
		var zNodesPlan = ${(testPlanJson)};
		var zNodesCase = ${(testCasesJson)};

		function beforeDrag(treeId, treeNodes) {
			if(treeNodes[0].diyType == 'testPlan' || treeNodes[0].diyType == 'caseFolder'){
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
			if(null != targetNode && (isCaseToCase(nodes[0], targetNode))){
				return true;
			}
			return false;
		}
		
		function canNext(treeId, nodes, targetNode){
			if(null != targetNode && (isCaseToCase(nodes[0], targetNode))){
				return true;
			}
			return false;
		}
		
		function canInner(treeId, nodes, targetNode){
			if(null != targetNode && (isCaseToCases(nodes[0], targetNode))){
				return true;
			}
			if(null != targetNode && (isCaseToPlans(nodes[0], targetNode))){
				return true;
			}
			return false;
		}
		
		function OnRightClick(event, treeId, treeNode) {
			/*if((isCaseFromPlan(treeNode) && isPlanFromJob(treeNode.getParentNode()))) {
				zTree = $.fn.zTree.getZTreeObj("testPlanTree");
				zTree.selectNode(treeNode);
				showRMenu("node", event.clientX, event.clientY);
			}*/
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
		
		$(document).ready(function(){
			$("#moduleId").change(function(){
				var module = this.value;
        		filterByModule(module);
        	});
			$.fn.zTree.init($("#testPlanTree"), setting, zNodesPlan);
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
			var treeObj = $.fn.zTree.getZTreeObj("testPlanTree");
			var nodes = treeObj.transformToArray(treeObj.getNodes());
			var json = $.toJSON( nodes ); 
			$("#testPlanJson").val(json);
			doFormSubmit('testPlanManageForm','doTestPlanManage.do')
		}
		
		function precious(){
			doFormSubmit('testPlanManageForm','goTestplanEdit.do');
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
<form id="testPlanManageForm"  name="testPlanManageForm" method="post">
	<input type="hidden" id="testPlanJson" name="testPlanJson"  value="${(testPlanJson)!''}"/>
	<input type="hidden" id="testplanId" name="testplanId" value="${(testplan.testplanId)!''}"/>
	<#if navigation?has_content>
		 ${navigation}
	</#if>
	<h1>Add test plan and test case</h1>
	<div class="content_wrap">
		<div class="vo"><br></div>
		<div class="zTreeDemoBackground left">
			<select id="moduleId" class="select" style="width:180px">
				<option value="" selected>Please Select</option>
				<#list moduleList as module>
					<option value="${module.moduleId}">${module.moduleName}</option>
				</#list> 
			</select>
			
			<ul id="testCaseTree" class="ztree"></ul>
		</div>
		<div class="right">
			<table align="center" class="tablecontent" id="caseDetailTable" width="100%">
				<tr><th><div id='caseDetail'></div><img src="${base}/images/arrow.png"/></th></tr>
				<tr ><th><font color="248fc3">Drag the test plans or test cases from the left box to the test job in right box and then click 'save'.</font></th></tr>
			</table>
		</div>
		<div class="middle">
			<button class="c" type="button" onclick="precious()">Precious</button>
			<button class="c" type="button" onclick="save()">Save</button>
			<ul id="testPlanTree" class="ztree"></ul>
			
		</div>
	</div>	
</form>
<div id="rMenu">
	<ul>
		<li id="m_add" onclick="ShowDetail();">Detail</li>
	</ul>
</div>
</body>
</html>
