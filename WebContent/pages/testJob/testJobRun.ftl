<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<link rel="stylesheet" type="text/css" href="${base}/css/zTree/zTreeStyle.css" />
<link rel="stylesheet" type="text/css" href="${base}/css/zTree/zTreePage.css" />
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="${base}/js/zTree/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="${base}/js/jquery.json-2.4.min.js"></script>
<script>
		var setting = {
			check: {
				enable: true
			},
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
				onClick: onLeftClick
			}
		};
		
		var zNodesJob = ${(testJobJson)};

		function beforeDrag(treeId, treeNodes) {
			return false;
		}
		
		function onLeftClick(event, treeId, treeNode) {
			$("#log").empty();
			$("#history").empty();
			$("#relateLog").empty();
			$("#relateLog").hide();
			var caseId = treeNode.id;
			
			if((treeNode.diyType == 'caseInPlan' || treeNode.diyType == 'case')){
				$.ajax({
			        type: 'post',
			        url: 'getRunCaseHistory.do?t=' + new Date().getTime(),
			        dataType: "text",
			        data: {
			        	'testjobId': '${(testjobId)}',
			            'testCaseId': caseId
			        },
			        async: false,
			        success: function(msg) {
			            showRunHistory(msg);
			        }
			    });
			}
			else if((treeNode.diyType == 'caseInPlan' || treeNode.diyType == 'case')){
				$.ajax({
			        type: 'post',
			        url: 'getRunCaseHistory.do?t=' + new Date().getTime(),
			        dataType: "text",
			        data: {
			            'testCaseId': caseId
			        },
			        async: false,
			        success: function(msg) {
			            showRunHistory(msg);
			        }
			    });
			}if((treeNode.diyType == 'caseInPlan' || treeNode.diyType == 'case') && typeof(treeNode.icon) != 'undefined') {
				$.ajax({
			        type: 'post',
			        url: 'getRunCaseLog.do?t=' + new Date().getTime(),
			        dataType: "text",
			        data: {
			            'testCaseId': caseId,
			            'runJobId': loopRunJobId
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
		
		function getRallyDetailInfo(defectNo){
			$.ajax({
		        type: 'post',
		        url: 'getRallyDetailInfo.do?t=' + new Date().getTime(),
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
		
		function gotoRunjob(runjobId){
			doPageRedirect('testjobResult.do?runJobId='+runjobId);
		}
		
		$(document).ready(function(){
			$.fn.zTree.init($("#testJobTree"), setting, zNodesJob);
			var treeObj = $.fn.zTree.getZTreeObj("testJobTree");
			treeObj.checkAllNodes(true);
			rMenu = $("#rMenu");
			refreshCaseState();
		});
		
		function showLog(str) {
			var log = $("#log");
			//log.append("<li class='dark'>"+str+"</li>");
			$("<li class='dark'>"+str+"</li>").appendTo(log);
			if(log.children("li").length > 100) {
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
		
		function saveDefaultParameter(){
			if(confirm('Original data will be recoveryed, continue?')){
				if(!checkPara()){
					alert("Please set all the parameters!");
					return;
				}
				var defaultParaJson = createParaJson();
				$.ajax({
				    type: 'get',
				    url: '${base}/test/saveDefaultPara.do?t=' + new Date().getTime(),
				    dataType: "text",
				    data: {
				    	'testjobId': '${(testjobId)}',
				        'paraJson': defaultParaJson
				    },
				    async: false,
				    success: function(msg) {
				        if (msg == "running") {
				        	alert("This test job is running!");
				        }
				        else{
				        	//nodes[0].getParentNode().icon = '${base}/images/zTree/loading.gif';
				        	//treeObj.updateNode(nodes[0]);
				        	createRunJob(nodes[0].id);
				        }
				    }
				});
			}
		}
		
		var loopNodes;
		var loopRunJobId = '';
		var paraJson;
		
		function run(){
			if(!checkPara()){
				alert("Please set all the parameters!");
				return;
			}
			showLog('Preparing for running...');
			var treeObj = $.fn.zTree.getZTreeObj("testJobTree");
			var allNodes = treeObj.transformToArray(treeObj.getNodes());
			for(var i = 0; i < allNodes.length; i++){
				allNodes[i].icon = '';
				treeObj.updateNode(allNodes[i]);
			}
			var nodes = treeObj.getCheckedNodes(true);
			loopNodes = nodes;
			paraJson = createParaJson();
			$.ajax({
			    type: 'get',
			    url: '${base}/test/checkIsTestJobRunning.do?t=' + new Date().getTime(),
			    dataType: "text",
			    data: {
			        'testjobId': nodes[0].id
			    },
			    async: false,
			    success: function(msg) {
			        if (msg == "running") {
			        	alert("This test job is running!");
			        }
			        else{
			        	//nodes[0].getParentNode().icon = '${base}/images/zTree/loading.gif';
			        	//treeObj.updateNode(nodes[0]);
			        	createRunJob(nodes[0].id);
			        }
			    }
			});
		}
		
		function stop(){
			showLog('Test job will be finished after this case!');
			var treeObj = $.fn.zTree.getZTreeObj("testJobTree");
			var allNodes = treeObj.transformToArray(treeObj.getNodes());
			for(var i = 0; i < allNodes.length; i++){
				allNodes[i].icon = '';
				treeObj.updateNode(allNodes[i]);
			}
			var nodes = treeObj.getCheckedNodes(true);
			loopNodes = nodes;
			paraJson = createParaJson();
			$.ajax({
			    type: 'get',
			    url: '${base}/test/checkIsTestJobRunning.do?t=' + new Date().getTime(),
			    dataType: "text",
			    data: {
			        'testjobId': nodes[0].id
			    },
			    async: false,
			    success: function(msg) {
			        if (msg != "running") {
			        	alert("This test job has finished!");
			        }
			        else{
			        	stopRunJob(nodes[0].id);
			        }
			    }
			});
		}
		
		function stopRunJob(jobId){
			$.ajax({
			    type: 'get',
			    url: 'doStopRunJob.do?t=' + new Date().getTime(),
			    dataType: "text",
			    data: {
			        'testjobId': jobId,
			    },
			    async: false,
			    success: function(msg) {
			    	refreshCaseState();
					showLog('Stopped');
			    }
			});
		}
		
		function createRunCase(){
			var jobId = loopNodes[0].id;
			var s = '[';
			for(var i = 0; i < loopNodes.length; i++){
				if (loopNodes[i].diyType == 'case' || loopNodes[i].diyType == 'caseInPlan') {
					var caseId = loopNodes[i].id;
		            var planId = '';
		            if (loopNodes[i].diyType == 'caseInPlan') {
		                planId = loopNodes[i].getParentNode().id;
		            }
					s += '{"runJobId":"';
					s += loopRunJobId;
					s += '",';
					s += '"testCaseId":"';
					s += caseId;
					s += '",';
					s += '"testPlanId":"';
					s += planId;
					s += '"},';
				}
			}
			s += ']';
			$.ajax({
				type: 'get',
				url: '${base}/test/doCreateRunCase.do?t=' + new Date().getTime(),
				dataType: "text",
				data: {
					'runjobcaseJson': s
				},
				async: false,
				success: function(msg) {
					runCases();
				}
			});
		}
		
		function runCases(){
			refreshCaseState();
			$.ajax({
				type: 'get',
				url: '${base}/test/doCasesRun.do?t=' + new Date().getTime(),
				dataType: "text",
				data: {
		            'runJobId': loopRunJobId,
		            'paraJson': paraJson
				},
				success: function(msg) {
				}
			});
		}
		
		
		function refreshCaseState(){
			var loopId = '';
			var isRun = true;
			if(('${(testjob.state)}' == 'RUNNING' && '${(runJobId)!''}' != '') || loopRunJobId != ''){
				var treeObj = $.fn.zTree.getZTreeObj("testJobTree");
				if(null != treeObj){
					var nodes = treeObj.transformToArray(treeObj.getNodes());
					var jobId = nodes[0].id;
					for(var i = 0; i < nodes.length; i++){
						if (nodes[i].diyType == 'case' || nodes[i].diyType == 'caseInPlan') {
							if(typeof(nodes[i].icon) != 'undefined' && (nodes[i].icon.indexOf('3.png') > 0 || nodes[i].icon.indexOf('5.png') > 0)){
								continue;
							}
					        if(loopRunJobId == ''){
					        	loopRunJobId = '${(runJobId)!''}';
					        }
							var caseId = nodes[i].id;
					        var planId = '';
					        if (nodes[i].diyType == 'caseInPlan') {
					            planId = nodes[i].getParentNode().id;
					        }
							$.ajax({
								type: 'post',
								url: '${base}/test/getCaseState.do?t=' + new Date().getTime(),
								dataType: "text",
								data: {
				                    'testCaseId': caseId,
				                    'testPlanId': planId,
				                    'runJobId': loopRunJobId,
				                    'testjobId': jobId
								},
								async: false,
								success: function(msg) {
									if(msg.indexOf('#') > 0){
										if(isRun == true){
											i = 0;
										}
										isRun = false;
										msg = msg.replace('#', '');
									}
									if ("success" == msg && i != 0) {
										treeObj.checkNode(nodes[i], true, true);
				                        nodes[i].icon = '${base}/images/zTree/3.png';
				                        if(nodes[i].diyType == 'case' && typeof(nodes[i].getParentNode().icon) == 'undefined'){
				                        	nodes[i].getParentNode().icon = '${base}/images/zTree/3.png';
				                        	treeObj.updateNode(nodes[i].getParentNode());
				                        }
				                        else if(nodes[i].diyType == 'caseInPlan' && typeof(nodes[i].getParentNode().icon) == 'undefined'){
				                        	nodes[i].getParentNode().icon = '${base}/images/zTree/3.png';
				                        	treeObj.updateNode(nodes[i].getParentNode());
				                        	if(typeof(nodes[i].getParentNode().getParentNode().icon) == 'undefined'){
				                        		nodes[i].getParentNode().getParentNode().icon = '${base}/images/zTree/3.png';
				                        		treeObj.updateNode(nodes[i].getParentNode().getParentNode());
				                        	}
				                        }
				                    } else if("fail" == msg && i != 0) {
				                    	treeObj.checkNode(nodes[i], true, true);
				                    	if(nodes[i].diyType == 'case'){
				                        	nodes[i].getParentNode().icon = '${base}/images/zTree/5.png';
				                        	treeObj.updateNode(nodes[i].getParentNode());
				                        }
				                        else if(nodes[i].diyType == 'caseInPlan'){
				                        	nodes[i].getParentNode().icon = '${base}/images/zTree/5.png';
				                        	treeObj.updateNode(nodes[i].getParentNode());
				                        	nodes[i].getParentNode().getParentNode().icon = '${base}/images/zTree/5.png';
				                        	treeObj.updateNode(nodes[i].getParentNode().getParentNode());
				                        }
				                        nodes[i].icon = '${base}/images/zTree/5.png';
				                    } else if("running" == msg && i != 0){
				                    	treeObj.checkNode(nodes[i], true, true);
				                    	//nodes[i].getParentNode().icon = '${base}/images/zTree/loading.gif';
				                    } else if("notRun" == msg && i != 0){
				                   		 treeObj.checkNode(nodes[i], true, true);
				                    } else if("notSelect" == msg && i != 0){
				                    	treeObj.checkNode(nodes[i], false, true);
				                    }
				                    treeObj.updateNode(nodes[i]);
								}
								
							});
						}
					}
				}
				if(!isRun){
					if('' != loopId){
						window.clearTimeout(loopId);
					}
				}
				else{
					loopId = setTimeout("refreshCaseState()", 20000);
				}
			}
		}
		
		function getCaseState(){
			var treeObj = $.fn.zTree.getZTreeObj("testJobTree");
			var nodes = treeObj.transformToArray(treeObj.getNodes());
			for(var i = 0; i < nodes.length; i++){
				if (nodes[i].diyType == 'case' || nodes[i].diyType == 'caseInPlan') {
					var caseId = nodes[i].id;
			        var runjobId = loopRunJobId;
			        var planId = '';
			        if (nodes[i].diyType == 'caseInPlan') {
			            planId = nodes[i].getParentNode().id;
			        }
					$.ajax({
						type: 'post',
						url: '${base}/test/getCaseState.do?t=' + new Date().getTime(),
						dataType: "text",
						data: {
		                    'testCaseId': caseId,
		                    'testPlanId': planId,
		                    'runJobId': runjobId
						},
						async: false,
						success: function(msg) {
							if ("success" == msg) {
								nodes[i].icon = '${base}/images/zTree/3.png';
		                        treeObj.updateNode(nodes[i]);
							}
						}
					});
				}
			}
		}
		
		/*
		function loopRun() {
		    var jobId = loopNodes[0].id;
		    node = loopNodes[loopCount];
		    if ('begin' == loopStatus) {
		        if (node.diyType == 'case' || node.diyType == 'caseInPlan') {
		            showLog(node.name + 'is running...');
		            loopStatus = 'end'
		            loopCount--;
		        }
		    } else if ('end' == loopStatus) {
		        if (node.diyType == 'case' || node.diyType == 'caseInPlan') {
		            var caseId = node.id;
		            var planId = '';
		            if (node.diyType == 'caseInPlan') {
		                planId = node.getParentNode().id;
		            }
		            $.ajax({
		                type: 'post',
		                url: 'doCaseRun.do?t=' + new Date().getTime(),
		                dataType: "text",
		                data: {
		                    'testjobId': jobId,
		                    'testCaseId': caseId,
		                    'testPlanId': planId,
		                    'runJobId': loopRunJobId,
		                    'paraJson': paraJson
		                },
		                async: false,
		                success: function(msg) {
		                    showLog(msg);
		                    if ("success" == msg) {
		                        node.icon = '${base}/images/zTree/3.png';
		                        if(node.diyType == 'case' && typeof(node.getParentNode().icon) == 'undefined'){
		                        	node.getParentNode().icon = '${base}/images/zTree/3.png';
		                        	loopTreeObj.updateNode(node.getParentNode());
		                        }
		                        else if(node.diyType == 'caseInPlan' && typeof(node.getParentNode().icon) == 'undefined'){
		                        	node.getParentNode().icon = '${base}/images/zTree/3.png';
		                        	loopTreeObj.updateNode(node.getParentNode());
		                        	if(typeof(node.getParentNode().getParentNode().icon) == 'undefined'){
		                        		node.getParentNode().getParentNode().icon = '${base}/images/zTree/3.png';
		                        		loopTreeObj.updateNode(node.getParentNode().getParentNode());
		                        	}
		                        }
		                    } else {
		                    	if(node.diyType == 'case'){
		                        	node.getParentNode().icon = '${base}/images/zTree/5.png';
		                        	loopTreeObj.updateNode(node.getParentNode());
		                        }
		                        else if(node.diyType == 'caseInPlan'){
		                        	node.getParentNode().icon = '${base}/images/zTree/5.png';
		                        	loopTreeObj.updateNode(node.getParentNode());
		                        	node.getParentNode().getParentNode().icon = '${base}/images/zTree/5.png';
		                        	loopTreeObj.updateNode(node.getParentNode().getParentNode());
		                        }
		                        node.icon = '${base}/images/zTree/5.png';
		                    }
		
		                    loopTreeObj.updateNode(node);
		                    loopStatus = 'begin';
		                }
		            });
		        }
		    }
		    loopCount++;
		    loopId = setTimeout("loopRun()", 500);
		    if (loopCount == loopNodes.length) {
		    	updateTestJobStatus(jobId);
		    	$("#showResult").show();
		        window.clearTimeout(loopId);
		    }
		}*/
		
		function checkPara(){
			var p =  $("select[id^='parameterVo']");
			for(var i=0; i<p.length; i++){
				if(p[i].value == ''){
					return false;
				}
			}
			var s =  $("input[id^='parameterVo']");
			for(var i=0; i<s.length; i++){
				if(s[i].value == ''){
					return false;
				}
			}
			return true;
		}
		
		function createParaJson(){
			var pList = $("select[id^='parameterVo']");
			var s = '[{';
			for(var i=0; i<pList.length; i++){
				s += '"';
				s += pList[i].name;
				s += '": "'
				s += pList[i].value;
				s += '",';
			}
			var sList = $("input[id^='parameterVo']");
			for(var i=0; i<sList.length; i++){
				s += '"';
				s += sList[i].name;
				s += '": "'
				s += sList[i].value;
				s += '",';
			}
			s += '}]';
			return s;
		}
		
		function updateTestJobStatus(jobId){
			$.ajax({
			    type: 'post',
			    url: 'doupdatejobStatus.do?t=' + new Date().getTime(),
			    dataType: "text",
			    data: {
			        'testjobId': jobId,
			        'runJobId': loopRunJobId
			    },
			    async: false,
			    success: function(msg) {
			    }
			});
		}
		
		function createRunJob(jobId){
			var parameterJson = createParaJson();
			$.ajax({
			    type: 'get',
			    url: 'doCreateRunJob.do?t=' + new Date().getTime(),
			    dataType: "text",
			    data: {
			        'testjobId': jobId,
			        'paraJson': parameterJson
			    },
			    async: false,
			    success: function(msg) {
					loopRunJobId = msg;
					showLog('Running...');
					createRunCase();
			    }
			});
		}
		
		function showResultPage(){
			doPageRedirect('testjobResult.do?runJobId='+loopRunJobId);
		}
		
		function doShowAll(id){
     		doPageRedirect('goJobRunList.do?testjobId='+id);
     	}
</script>
</head>
<body>
<form id="testJobRunForm"  name="testJobRunForm" method="post">
	<input type="hidden" id="testJobJson" name="testJobJson"  value="${(testJobJson)!''}"/>
	<#if navigation?has_content>
	 	${navigation}
	</#if>
	<h1>Run ${testjob.testjobName}</h1>
	<div class="box">
		
		<div class="content_wrap" >
			<#if paraHtml?has_content >
				<div class="para">
					<h2>Parameters</h2>
					<br>
					${(paraHtml)}
					<br>
					<center><button class="c" type="button" onclick="saveDefaultParameter()">Save as default</button></center>
				
				</div>
				
			</#if> 
			<div class="left">
				<h2>TestJob</h2>
				<ul id="testJobTree" class="ztree"></ul>
				<button class="c" type="button" onclick="run()">run</button>
				<button class="c" type="button" onclick="stop()">stop</button>
				<button class="c" type="button" id='showResult' style='display:none' onclick="showResultPage()">result</button>
			</div>
			<div class="right">
				<h2>History</h2>
				<ul id="history" class="minilog"></ul>
			</div>
			<div class="middle">
				<h2>Log</h2>
				<ul id="log" class="minilog"></ul>
				<ul id="relateLog" style='display:none'></ul>
			</div>
		</div>
	</div>	
	<div class="box">
		<div class="bottom">
			<h2>Lastest 5 run test job</h2>
		    <table class="tlist" id="pageTable" width="100%">
				<tr>
					<th width="10%" align="left">Time</th>		
					<th width="20%">Result</th>
					<th width="20%">Run Owner</th>
					<th width="20%">Action</th>
				</tr>     
			<#if runJobList?has_content >
			    <#list runJobList as runJob>
					<tr class="mousechange">
						<td align="left">${(runJob.startTime)!''}</td>	
						<td align="center">
						 <#if 'FAIL'=='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="#FF0000"><B>${(runJob.result)!''}</B></td></tr></table></div></#if>
						 <#if 'PASS'=='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="green"><B>${(runJob.result)!''}</B></td></tr></table></div></#if>
						 <#if 'ACTIVE'=='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="lightgray"><B>${(runJob.result)!''}</B></td></tr></table></div></#if>
						 <#if 'ABORT'=='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="gray"><B>${(runJob.result)!''}</B></td></tr></table></div></#if>
						 <#if 'RUNNING'=='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="lightgreen"><B>${(runJob.result)!''}</B></td></tr></table></div></#if>	
						 <#if 'FAIL'!='${runJob.result!""}'&&'PASS'!='${runJob.result!""}'&&'ACTIVE'!='${runJob.result!""}'&&'ABORT'!='${runJob.result!""}'&&'RUNNING'!='${runJob.result!""}'><div><table><tr><td align="center" bgcolor="lightgray"><B>NONE</B></td></tr></table></div></#if>		
						 	 						 		
						</td>
						<td align="center">${(runJob.owner.userCode)!''}</td>
						<td align="center"><div class="table_link">
						
						 	 <a class="tooltips" href="#" onclick="doPageRedirect('testjobResult.do?runJobId=${(runJob.runJobId)}')"><img src="${base}/images/icon/27.png" /><span>View&nbsp;&nbsp;Detail</span></a>&nbsp;&nbsp;&nbsp;
						  
						 </td>
					</tr>
			    </#list>
				
		 	</#if>      
		 	<tr>
		 		<td colspan="4" align="right">		 		
		 		<a href="#" onclick="doShowAll('${(testjobId)}')">Show All</a></td>
		 	</tr>
			</table>
		</div>	
	</div>
</form>
</body>
</html>