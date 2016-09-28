<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#include "/system/include/head.ftl">
<#import "/commons/pagerSorter.ftl" as pagerSorter>
<@pagerSorter.sorterPageJs/>	
<@pagerSorter.sorterPage/>
<script>
	$(document).ready(function() {
			$('#pageTable1').DataTable( {
				aaSorting: [[ 0, "desc" ]],
				dom: 'lfrtip<"clear">T',
				tableTools: {
		            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
		        }
			});
			
			$('#pageTable2').DataTable( {
				aaSorting: [[ 2, "desc" ]],
				dom: 'lfrtip<"clear">T',
				tableTools: {
		            "sSwfPath": "${base}/js/dataTools/copy_csv_xls_pdf.swf"
		        }
			});
			
			$("#bntUserAdd").click(function(){	
		         var newTr = "<tr><td>Award</td><td><input class='input_text' type='text'></input></td><td>&nbsp;&nbsp;&nbsp;&nbsp;TO&nbsp;&nbsp;</td>"
		            + "<td><select class='select' style='width:180px'>"
		            + "<option value=''selected >Please Select</option>"
		            + "<#list userList as user>"
					+ "<option value='${user.userId}'>${user.userCode}</option>"
					+ "</#list>"
		            + "</select></td>"
					+ "<td><button type='button' class='bntDelTr'>Delete</button></td></tr>";
		        $("#tbluser tr:last").after(newTr);
		        $("#commit").show();
  	        });
  	        
  	        $("#awardPointInfo").delegate(".bntDelTr", "click", function(){
		         $(this).closest("tr").remove();
		         trArray = document.getElementById("tbluser").rows;
		         if(trArray.length==1){
		           $("#commit").hide();
		         }
  	        });
  	
		});	
	
    function refreshAll(){
		if(confirm('Confirm refresh, it will take a few minutes?')){
			$.ajax( {
				type : 'get',
				url : '${base}/mytitan/syncPoint.do?t=' + new Date().getTime(),
				beforeSend:function(){jBox.tip("Refreshing...", 'loading');},
				success : function(data) {				      
					window.setTimeout(function () { jBox.tip('Refresh success!', 'success'); }, 2000);
					refreshPage();
				}
			});
		}
	}
	
	function usePoints(){
		doFormSubmit('pointListForm','goUsePoint.do');
	}
	
	function newUsePoints(){
		doFormSubmit('pointListForm','goNewUsePoint.do');
	}
	
	function save(){
	    saveAwardPoints();	
	    if(check()){	
		    doFormSubmit('pointListForm','doAwardPoint.do');
        }
    }
    
    function check(){
        return true;
    }
    

    function saveAwardPoints(){
        var awardPoints = [];
        var awardUsers = [];
        var total = 0;
        trArray = document.getElementById("tbluser").rows;
	    for(var i=1; i<trArray.length; i++){
	        var awardNum = trArray[i].cells[1].getElementsByTagName("input")[0].value;
	        if(IsNum(awardNum)){
	            if(parseInt(awardNum)>0){
	                awardPoints.push(awardNum);
	                total = parseInt(total) + parseInt(awardNum);
	                if(total<=0){
	                    alert("please check your input!");
                        return;
	                }
	            }else{
	            	alert("please check your input");
                    return;
	            }
	        }else{
	            alert("please check your input");
                return;
	        }
	         
            if(trArray[i].cells[3].getElementsByTagName("select")[0].value != ''){
                awardUsers.push(trArray[i].cells[3].getElementsByTagName("select")[0].value);
            }else{
                alert("please check your input");
                return;              
            }
	    }
	    
	    if('${(myAwardPoint)!""}'!=""){
	        var myAwardPoints = parseInt('${(myAwardPoint)!""}');
	        if(total>myAwardPoints){
	            alert("You only have "+ myAwardPoints + " points to award.");
	            return;
	        } 
	        document.getElementById("awardPoints").value = awardPoints;
	        document.getElementById("awardUsers").value = awardUsers;
	        doFormSubmit('pointListForm','doAwardPoint.do');
	    }else{
	        alert("You don't have any award points, please contact Admin!")
	    }
    }

</script>

<style>
body{ background-color:#FFF}
.showlist_main{width:99%;}
.showlist_mainnopadding table{top:0;}
</style>
</head>

<body>
<form id="pointListForm" action="" method="POST">
<#if navigation?has_content>
	${navigation}
</#if>
<div id="pointInfo">
<table width="100%" border="0">
	<tr>
		<td align="left">
			<div class="title">My Point : ${(mypoint)!''} </div>
		</td>
	</tr>
</table>
</div>
<div class="showlist_main showlist_mainnopadding"></div>
 
	   <div class="table_link">
        <ul>
        </ul></div>
<div class="clear"></div>
        <table id="pageTable1" class="display" width="100%" border="0" cellpadding="0" cellspacing="0">
         	<thead>
				<th width="10%" >Date</th>
		        <th width="8%" >Point</th>
		        <th width="8%" >Total</th>
		     	<th width="8%" >Type</th>
		     	<th width="12%" >Detail</th>
			</thead>
          
          <#if phList?has_content >
     	 <#list phList as ph>
			<tr class="table_padding" class="mousechange">
				<td align="left">${(ph.strcreateDate).toString()!""}</td>
				<td><div align="center">${(ph.pointFare)!''}</div></td>
				<td><div align="center">${(ph.pointTotal)!''}</div></td>
				<td align="center">${(ph.type)!''}</td>
				<td>${(ph.objectId)!''}</td>
			</tr>
			 </#list>
 		 </#if>
          
        </table>
        <table width="100%" border="0">
	        <tr></tr>
			<tr><td align="certer">
			     <div class="table_link"><ul>
				<li>
					<a href="#" onclick="newUsePoints()">New Shopping mall</a>
				</li>
				</ul></div>
				</td>
			</tr>
		</table>
<div id="awardPointInfo">
    <table width="100%" border="0">
	<tr>
		<td align="left">
			<div class="title">My Award Point :  ${(myAwardPoint)!''} </div>
		</td>
		<td align="left" width="80%">
	    <input type="hidden" name="awardPoints" id="awardPoints" value=""/>	
	    <input type="hidden" name="awardUsers" id="awardUsers" value="" />
	        <table id="tbluser" width="60%">
	            <tr id="awards"></tr>
		    </table>
		<a class="tooltips" id="bntUserAdd" ><img src="${base}/images/icon/11.png" /></a>
		<button type="button" id="commit" onclick="saveAwardPoints()" style="display:none;">commit</button>		
		<span id="userIdMessage"></span>
	    </td>
    </tr>
    </table>
</div>
<div class="showlist_main showlist_mainnopadding"></div>
 
	   <div class="table_link"><ul></ul></div>
       <div class="clear"></div>
        <table id="pageTable2" class="display" width="100%" border="0" cellpadding="0" cellspacing="0">
         	<thead>
				<th width="10%" >Date</th>
		        <th width="8%" >Point</th>
		     	<th width="8%" >TO</th>
			</thead>
          
          <#if aphList?has_content >
     	    <#list aphList as aph>
			<tr class="table_padding" class="mousechange">
				<td align="left">${(aph.strcreateDate).toString()!""}</td>
				<td><div align="center">${(aph.pointFare)!''}</div></td>
				<td align="center">${(aph.objectId)!''}</td>
			</tr>
			</#list>
 		 </#if>
        </table>
</form>
</body>
</html>
