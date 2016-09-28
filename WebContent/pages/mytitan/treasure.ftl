<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<#include "/system/include/head.ftl">
<link rel="stylesheet" href="${base}/css/treasure/reset.css">
<link rel="stylesheet" href="${base}/css/treasure/atom.css">
<link rel="stylesheet" href="${base}/css/treasure/global.css">
<link rel="stylesheet" href="${base}/css/treasure/open.css">
<title>Treasure</title>
</head>
<body ontouchstart="" class="open-body">
<form id="treasureForm" action="" method="POST">
  <input type="hidden" id="messageId" name="messageId" type="text" value="${(messageId)!''}" />
  <div class="wrapper">
    
    <div class="bg rotate"></div>
    <div class="open-has ">
      <h3 class="title-close"> </h3>
      <h3 class="title-open">Congratulations!</h3>

      <div class="mod-chest">
        
        <div class="chest-close show ">
          <div class="gift"></div>
          <div class="tips">
            <i class="arrow"></i>
          </div>
        </div>
        <div class="chest-open ">
          <div class="mod-chest-cont open-cont">
            <div class="content">
              <div class="gift">
                <div class="icon"><img src="${base}/images/treasure/chest-icon-zuan.png"></div> x <div id="show0">0</div> 
              </div>
              <div class="func">
                <button class="chest-btn">OK</button>
              </div>
            </div>
          </div>
        </div>
        
      </div>
    </div>
    <div class="open-none" style="display:none">
      <div class="mod-chest">
        <div class="chest-open show"></div>
      </div>
      <div class="func">
        <button class="chest-btn">OPEN IT</button>
      </div>
    </div>
  </div>
  <script type="text/javascript" src="${base}/js/treasure/zepto.min.js"></script>
<script type="text/javascript">

  $(document).ready(function() {
  	 var randomVal = Math.round(Math.random() * 4 + 1)
  	 $("#show0").html(randomVal);
  });
  
  $(".chest-btn").click(function(){
  	var messageId = $('#messageId').val();
	$.ajax( {
			type : 'get',
			url : '${base}/mytitan/addPoint.do?t=' + new Date().getTime(),
			dataType: "text",
			data : { 'messageId' : messageId,
					 'tensNum' : $("#show0").html()
			},
			async: false,
			success : function(msg) {				      
			}
		});
		window.parent.location.reload();
  });

  $(".chest-close").click(function(){

    $(this).addClass("shake");
    var that=this;
    
    this.addEventListener("webkitAnimationEnd", function(){
      
      $(that).closest(".open-has").addClass("opened");
      setTimeout(function(){
        $(that).removeClass("show");
        $(that).closest(".mod-chest").find(".chest-open").addClass("show");
        setTimeout(function(){
          $(".chest-open").addClass("blur");
        },500)
      },200)
    }, false);
  })
</script>
</form>
</body>
</html>