<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/zTree/zTreeStyle.css" />
<link rel="stylesheet" type="text/css" href="${base}/css/zTree/zTreePage.css" />
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.nodeControl.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="${base}/js/jquery.json-2.4.min.js"></script>
<script>
		var setting = {
			edit: {
				drag: {
					isCopy: false,
					isMove: false
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
				onRightClick: OnRightClick,
				onClick: onLeftClick
			}
		};
		
		var zNodesJob = ${(testJobResultJson)};

		function beforeDrag(treeId, treeNodes) {
			return false;
		}
		
		$(document).ready(function(){
			$.fn.zTree.init($("#testJobTree"), setting, zNodesJob);
			rMenu = $("#rMenu");
		});
		
		function showLog(str) {
			var log = $("#log");
			//log.append("<li class='dark'>"+str+"</li>");
			$("<li class='dark'>"+str+"</li>").appendTo(log);
			if(log.children("li").length > 10) {
				log.get(0).removeChild(log.children("li")[0]);
			}
		}
		
		function showRunHistory(str) {
			var history = $("#history");
			//log.append("<li class='dark'>"+str+"</li>");
			$("<li class='dark'>"+str+"</li>").appendTo(history);
			if(history.children("li").length > 10) {
				history.get(0).removeChild(history.children("li")[0]);
			}
		}
		
		function gotoRunjob(runjobId){
			doPageRedirect('testjobResult.do?runJobId='+runjobId);
		}
		
		function onLeftClick(event, treeId, treeNode) {
			$("#log").empty();
			$("#history").empty();
			$("#relateLog").empty();
			$("#relateLog").hide();
			var runJobId = $("#runJobId").val();
			if((treeNode.diyType == 'caseInPlan' || treeNode.diyType == 'case') /*&& typeof(treeNode.icon) != 'undefined'*/) {
				$("#trCommand").html(treeNode.diyCommand);
			    if(typeof(treeNode.icon) != 'undefined' && treeNode.icon.indexOf('5.png') > 0){
			    	var runcaseId = treeNode.diyRuncaseId;
			    	$("#trDefect").show();
			    	$.ajax({
				        type: 'post',
				        url: 'getDefectByRunCaseId.do?t=' + new Date().getTime(),
				        dataType: "text",
				        data: {
				            'runCaseId': runcaseId
				        },
				        async: false,
				        success: function(msg) {
				        	$("#trDefect").html(msg);
				        }
				    });	
			    }
			    else{
			    	$("#trDefect").hide();
			    }
				var caseId = treeNode.id;
				$.ajax({
			        type: 'post',
			        url: 'getRunCaseHistory.do?t=' + new Date().getTime(),
			        dataType: "text",
			        data: {
			            'testCaseId': caseId,
			            'testjobId': "${(testjobrunVo.testjobId)!''}"
			        },
			        async: false,
			        success: function(msg) {
			            showRunHistory(msg);
			        }
			    });
				$.ajax({
			        type: 'post',
			        url: 'getRunCaseLog.do?t=' + new Date().getTime(),
			        dataType: "text",
			        data: {
			            'testCaseId': caseId,
			            'runJobId': runJobId
			        },
			        async: false,
			        success: function(msg) {
			        	//alert(msg);
			            //var arr = $.parseJSON(msg);
			            showLog(msg);
			            if(arr.length > 1){
			                var tmp = '';
			                for(i = 1; i < arr.length; i++){
			                    tmp += '<a href="#" onclick="showRelateLog(\''+ arr[i] +'\')">'+arr[i]+'</a><br>';
			                }
			                $("<li class='dark'>"+tmp+"</li>").appendTo($("#relateLog"));
			                $("#relateLog").show();
			            }
			        }
			    });
			}
		}
		
		function OnRightClick(event, treeId, treeNode) {
			if(isCaseFromJob(treeNode) || (isCaseFromPlan(treeNode) && isPlanFromJob(treeNode.getParentNode()))) {
				zTree = $.fn.zTree.getZTreeObj("testJobTree");
				zTree.selectNode(treeNode);
				showRMenu("node", event.clientX, event.clientY);
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
		
		function showRelateLog(path){
			$("#log").empty();
			$.ajax({
		        type: 'post',
		        url: 'getRunCaseRelateLog.do?t=' + new Date().getTime(),
		        dataType: "text",
		        data: {
		            'relatelogPath': path,
		        },
		        async: false,
		        success: function(msg) {
		            showLog(msg);
		        }
		    });
		}
		
		function getDefectDetailInfo(defectNo){
			$.ajax({
		        type: 'post',
		        url: 'getDefectDetailInfo.do?t=' + new Date().getTime(),
		        dataType: "text",
		        data: {
		            'defectNo': defectNo,
		        },
		        async: false,
		        success: function(msg) {
		            jBox.alert(msg);
		        }
		    });
		}
		
		function editDefect() {
			var treeObj = $.fn.zTree.getZTreeObj("testJobTree");
			var nodes = treeObj.getSelectedNodes();
			var runcaseId = nodes[0].diyRuncaseId;
			var caseId = nodes[0].id;
			var runJobId = $("#runJobId").val();
			var jobId = '';
			var planId = '';
			if('case' == nodes[0].diyType){
				jobId = nodes[0].getParentNode().id;
			}
			else if('caseInPlan'){
				jobId = nodes[0].getParentNode().getParentNode().id;
				planId = nodes[0].getParentNode().id;
			}
            var html = '<div class="msg-div"> <p>Defect type:</p>'
            		  +'<select id="defecttype" name="defecttype"  class="select" style="width:160px">'
            		  +'<option value="rally">Rally</option>'
            		  +'<option value="quix">Quix</option>'
            		  +'</select>'
            		  +'<p>Defect number:</p>'
            		  +'<input type="text" id="defectNo" name="defectNo" style="width:75px;"/>'
            		  +'</div>';
			
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
                        var defectType = document.getElementById("defecttype").value;//$("testcasepara");
                        var defectNo = document.getElementById("defectNo").value;//$("testcasepara");
                        $.ajax( {
							type : 'post',
							url : 'doSaveCaseDefect.do?t=' + new Date().getTime(),
							dataType: "text",
							data : { 'defectType' : defectType,
									 'defectNo' : defectNo,
									 'runCaseId' : runcaseId
							 },
							async: false,
							success : function(msg) {
								if(msg == 'success'){
									//jBox.nextState('<div class="msg-div">Submitting...</div>');
			                        // 或 jBox.goToState('state2')
			                        window.setTimeout(function () { jBox.goToState('state2'); }, 10);
								}
								else if(msg == 'noDefect'){
									window.setTimeout(function () {  jBox.goToState('state3'); }, 10);
								}
								else if(msg == 'duplicated'){
									window.setTimeout(function () {  jBox.goToState('state4'); }, 10);
								}
							}
						});
                    }

                    return false;
                }
            };
            states.state2 = {
                content: 'Success',
                buttons: {'OK': 0 }, // no buttons
                submit: function (v, h, f) {
                	if (v == 0) {
                		window.setTimeout(function () {  refreshHistory(caseId, jobId, runcaseId); }, 10);
                        return true; // close the window
                    }
                	return false;
                }
            };
            states.state3 = {
                content: 'No defect found!',
                buttons: {'OK': 0 } // no buttons
            };
            states.state4 = {
                content: 'Duplicated defect',
                buttons: {'OK': 0 } // no buttons
            };
            jBox.open(states, 'Case defect', 450, 'auto');
        }
        
        function refreshHistory(caseId, jobId, runcaseId){
        	$("#history").empty();
        	$.ajax({
				type: 'post',
				url: 'getRunCaseHistory.do?t=' + new Date().getTime(),
				dataType: "text",
				data: {
					'testCaseId': caseId,
					'testjobId': "${(testjobrunVo.testjobId)!''}"
				},
				async: false,
				success: function(msg) {
					showRunHistory(msg);
				}
			});	
			$.ajax({
				type: 'post',
				url: 'getDefectByRunCaseId.do?t=' + new Date().getTime(),
				dataType: "text",
				data: {
				    'runCaseId': runcaseId
				},
				async: false,
				success: function(msg) {
					$("#trDefect").html(msg);
				}
			});	
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
<form id="testJobRunForm"  name="testJobRunForm" method="post">
	<input type="hidden" id="runJobId" name="runJobId"  value="${(runJobId)!''}"/>
	<#if navigation?has_content>
	 	${navigation}
	</#if>
	<h1>Run test job result</h1>
	<div class="box">
	    <center><table class="tlist" id="pageTable" width="70%">
	    <#if testjobrunVo?has_content >
			<tr>
				<td  width="10%"><h2>Name</h2></td>
				<td colspan="3" >${(testjobrunVo.testjobName)!''}</td>					
			</tr>  
			<tr>
				<td width="10%"><h2>Code</h2></td>
				<td width="40%">${(testjobrunVo.testjobCode)!''}</td>
				<td width="10%"><h2>Owner</h2></td>
				<td >${(testjobrunVo.owner)!''}</td>
			</tr> 
			<tr>
				<td width="10%"><h2>Start time</h2></td>
				<td >${(testjobrunVo.startTime)!''}</td>
				<td width="10%"><h2>End time</h2></td>
				<td >${(testjobrunVo.endTime)!''}</td>
			</tr> 
			<tr>
				<td width="10%"><h2>Success</h2></td>
				<td >${(testjobrunVo.successCount)!''}</td>
				<td width="10%"><h2>Fail</h2></td>
				<td >${(testjobrunVo.failCount)!''}</td>
			</tr>			
		</#if>
			<tr>
				<td width="10%"><h2>Command:</h2></td>
				<td width="90%" colspan="3" ><div id='trCommand'><div></td>
			</tr>      	 	   
		</table>
		</center>
	</div>	
	<div class="content_wrap" width="100%">
		<div id='trDefect' class="addDefect"></div>
		<div class="vo"><br></div>
		<div class="zTreeDemoBackground left">
			<ul id="testJobTree" class="ztree"></ul>
		</div>
		<div class="right" >
			<ul id="history" class="minilog"></ul>
		</div>
		<div class="middle">
			<ul id="log" class="minilog"></ul>
			<ul id="relateLog" style='display:none'></ul>
		</div>
	</div>
	<button class="c" type="button" id='showResult' style='display:none' onclick="showResultPage()">result</button>
</form>
<div id="rMenu">
	<ul>
		<li id="m_del" onclick="editDefect();">Edit defect</li>
	</ul>
</div>
</body>
</html>
