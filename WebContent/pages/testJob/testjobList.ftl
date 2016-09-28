<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<style>
.parent { background:#FFF38F;cursor:pointer;}
#dvCompute{position:absolute;visibility:hidden;}
</style>
<div id="dvCompute"></div>
<script>

$(document).ready(function(){ 
  	$(document).keydown(function(){ 
		if(event.keyCode == 13) 
			$("#filter").click();}
	);
	$("#project").change(function(){
		var project = this.value;
	
		if('' != project){
			//get sprint list of the project selected
	  		$.ajax( {
				type : 'get',
				url : 'getSprintsByPorject.do',
				data : { 'projectId' : project },
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success : function(data) {
					var y = document.getElementById("testjobVo.sprintId");
					for(var j=y.length-1; j>0; j--){
						y.remove(j);
					}
					if(data.length != 0){
						var x = document.getElementById("testjobVo.sprintId");
						for(var i=0; i<data.length; i++){
							var option=document.createElement("option");
						    option.value = data[i].sprintId;
							option.text = data[i].sprintName;
							x.add(option,x.options[null]);
						}
					}
				}
			});
		  }
  	});
  	
	    $('tr.parent').click(function(){ 
	        $(this).siblings("."+this.id).toggle(); 
	    });  

});

function doRun(id,type){
    if(type == "Manual"){
     doPageRedirect('goMTestJobRun.do?testjobId='+id);
     }else{
     doPageRedirect('goTestJobRun.do?testjobId='+id);
     }
     
   //}else{
	/*$.ajax({
	    type: 'get',
	    url: '${base}/test/checkIsTestJobRunning.do?t=' + new Date().getTime(),
	    data: {
	        'testjobId': id
	    },
	    async: false,
	    success: function(msg) {
	        if (msg == "running") {
	        	alert("This test job is running!");
	        } else {
	            doPageRedirect('goTestJobRun.do?testjobId='+id);
	        }
	
	    }
	});*/
   //}
}

function doJobShow(id){
		$.ajax( {
			type : 'get',
			url : 'getJobShow.do?t=' + new Date().getTime(),
			dataType: "text",
			data : { 'testjobId' : id },
			async: false,
			success : function(msg) {
				var json = eval('(' + msg + ')');
				var name='';
				var kongge = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
				var wid=350;
				$(json).each(function(){
					var child = this.children;
					$(child).each(function(){
				        var eachname = this.name;
						var caseCode = this.caseCode;
						if(caseCode!=undefined){
							name = name+caseCode+kongge;						
						}else{
							var planCode= this.planCode;
							name = name+planCode+kongge;
						}
				        name = name + eachname+'<br/>';
				        var p = compute(name+eachname);
				        if(p.w>330 && wid!='auto'){
				        	wid='auto';
				        }
				        var childchild = this.children;
				        if(childchild!=''){
				        	$(childchild).each(function(){
				        		var childchildName = this.name;
				        		var codecode = this.caseCode;
				        		name = name+kongge+codecode+kongge+ childchildName+'<br/>';
				        		var p = compute(kongge+codecode+kongge+ childchildName);
				        		if(p.w>330 && wid!='auto'){
						        	wid='auto';
						        }
				        	});
				        }
					});
			    });
			    if(name==''){
			    	name = 'No case';
			    }
				$.jBox.info(name,'Test Job',{width: wid});
			}
	});
}

function compute(v) {
    var d = document.getElementById('dvCompute');
    d.innerHTML = v;
    return { w: d.offsetWidth, h: d.offsetHeight };
}
function doResult(id){
	doPageRedirect('goJobAutoRunList.do?testjobId='+id);
}

function checkBox(type,name){
	var testjobIds = document.getElementsByName(name);
	var count = 0;
	if(testjobIds != null && testjobIds.length > 0){
     for(var i = 0 ; testjobIds.length > i; i++){
        if(testjobIds[i].checked){
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


function gettestjobId(name){
	var testjobIds = document.getElementsByName(name);
	if(testjobIds != null && testjobIds.length > 0){
     for(var i = 0 ; testjobIds.length > i; i++){
        if(testjobIds[i].checked){
        	return testjobIds[i].value;
	   	}
     }
	}
}

function doRowDelete(id){
   if(confirm('Confirm the delete?')){
      doPageRedirect('dotestjobRemove.do?testjobId='+id);
   }
}

</script>
</head>

<body>
<form id="testjobDoId" action="" method="POST">
<#if navigation?has_content>
	 ${navigation}
</#if>
<table width="100%" border="0">
	<tr><td align="left">
		<div class="title">TestJob
			&nbsp;&nbsp;&nbsp;&nbsp;<a class="tooltips" href="#" onclick="doPageRedirect('goTestjobCreate.do')">
		<img src="${base}/images/icon/11.png" /><span>Add&nbsp;&nbsp;TestJob</span> </a>
		</div>
	</td><!--<td align="right">
		<div class="table_link"><ul>
          	<li><a href="#" onclick="doPageRedirect('goTestjobCreate.do')">Add</a></li>
        </ul></div>
	</td>--></tr>
</table>
<div class="box">
      <table class="display" width="100%">
          <thead>
		        <th width="5%">Tag</th>
		        <th width="10%">Code</th>
		        <th width="10%">Name</th>
		        <th width="10%">Type</th>
		        <th width="5%">State</th>
		        <th width="10%">Project</th>
		        <th width="7%">Sprint</th>
		        <th width="10%">Owner</th>
		        <th width="20%">Description</th>
		        <th width="13%">Action</th>
			</thead>
		<tbody>	
         <#if testjobVoList?has_content >
     	 <#list testjobingroupvoList as testjobingroupvo>
     	 	<tr class="parent" id="${(testjobingroupvo.tag)!''}"><td colspan="13">+${(testjobingroupvo.tag)!''}</td></tr>
     	 	<#list testjobingroupvo.testjobVoList as testjob>
				<tr class="mousechange" class="${(testjob.tag)!''}">
					<td align="center">${(testjob.tag)!''}</td>
					<td align="center">${(testjob.testjobCode)!''}</td>
					<td align="center"><a href="#" onclick="doPageRedirect('goJobRunList.do?testjobId=${(testjob.testjobID)}')">${(testjob.testjobName)!''}</a></td>
					<td align="center">${(testjob.testjobType)!''}</td>
					<td align="center">
					 <#if 'FAIL'=='${testjob.testjobState!""}'><div><table><tr><td align="center" bgcolor="#FF0000"><B><font color="white">&nbsp;&nbsp;&nbsp;&nbsp;${(testjob.testjobState)!''}&nbsp;&nbsp;&nbsp;&nbsp;</font></B></td></tr></table></div></#if>
					 <#if 'PASS'=='${testjob.testjobState!""}'><div><table><tr><td align="center" bgcolor="green"><B><font color="white">&nbsp;&nbsp;&nbsp;&nbsp;${(testjob.testjobState)!''}&nbsp;&nbsp;&nbsp;&nbsp;</font></B></td></tr></table></div></#if>
					 <#if 'ACTIVE'=='${testjob.testjobState!""}'><div><table><tr><td align="center" bgcolor="lightgray"><B><font color="">&nbsp;&nbsp;${(testjob.testjobState)!''}&nbsp;&nbsp;</font></B></td></tr></table></div></#if>
					 <#if 'ABORT'=='${testjob.testjobState!""}'><div><table><tr><td align="center" bgcolor="gray"><B><font color="white">&nbsp;&nbsp;${(testjob.testjobState)!''}&nbsp;&nbsp;</font></B></td></tr></table></div></#if>
					 <#if 'RUNNING'=='${testjob.testjobState!""}'><div><table><tr><td align="center" bgcolor="lightgreen"><B><font color="">${(testjob.testjobState)!''}</font></B></td></tr></table></div></#if>	
					 <#if 'PENDING'=='${testjob.testjobState!""}'><div><table><tr><td align="center" bgcolor="#00F5FF"><B><font color="">${(testjob.testjobState)!''}</font></B></td></tr></table></div></#if>	 		
					</td>
					<td align="center">${(testjob.project)!''}</td>
					<td align="center">${(testjob.sprint)!''}</td>
					<td align="center">${(testjob.owner)!''}</td>
					<td align="center">${(testjob.description)!''}</td>
					<td align="center"><div class="table_link">
						<a class="tooltips" href="#" onclick="doJobShow('${(testjob.testjobID)}')">Detail</a>&nbsp;&nbsp;&nbsp;&nbsp;
						<a class="tooltips" href="#" onclick="doRun('${(testjob.testjobID)}' ,'${(testjob.testjobType)}')"><img src="${base}/images/icon/iconpng.png" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Run&nbsp;&nbsp;&nbsp;&nbsp;</span></a>&nbsp;&nbsp;&nbsp;&nbsp;
					    <#if '${testjob.owner!""}'=='${currentUserName!""}'||'${isAdmin!""}'=='1'>
					    <a class="tooltips" href="#" onclick="doPageRedirect('goTestjobEdit.do?testjobId=${(testjob.testjobID)}')"><img src="${base}/images/icon/24.png" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;&nbsp;</span></a>&nbsp;&nbsp;&nbsp;&nbsp;
					    <a class="tooltips" href="#" onclick="doRowDelete('${(testjob.testjobID)}')"><img src="${base}/images/icon1/er_s.gif" /><span>&nbsp;&nbsp;&nbsp;&nbsp;Delete&nbsp;&nbsp;&nbsp;&nbsp;</span></a> 
					    </#if>
					    
					</div></td>
				</tr>
			</#list>
	     </#list>
 		 </#if>
      </tbody>    
        </table>     	
	</div>
</div>
</form>
</body>
</html>
