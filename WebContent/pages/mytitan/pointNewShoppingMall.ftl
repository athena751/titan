<!DOCTYPE html>
<head>
<#include "/system/include/head.ftl">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>New Points shopping mall</title>
<link rel="stylesheet" type="text/css" href="${base}/css/newshoppingmall/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${base}/css/newshoppingmall/style.css" />
<link rel="stylesheet" type="text/css" href="http://libs.useso.com/js/font-awesome/4.2.0/css/font-awesome.min.css">
<script type="text/javascript">
$(function(){
	var cd = new Date();	
	var sd = new Date($('#startdate').val());
	var ed = new Date($('#enddate').val());		
	if(cd > sd && cd < ed){
		$("#addtocart").show();
		countDown($('#enddate').val(),"#colockbox1");
	}
});
function countDown(time,id){
	var day_elem = $(id).find('#day');
	var hour_elem = $(id).find('#hour');
	var minute_elem = $(id).find('#minute');
	var second_elem = $(id).find('#second');
	var end_time = new Date(time).getTime(),
	sys_second = (end_time-new Date().getTime())/1000;
	var timer = setInterval(function(){
		if (sys_second > 1) {
			sys_second -= 1;
			var day = Math.floor((sys_second / 3600) / 24);
			var hour = Math.floor((sys_second / 3600) % 24);
			var minute = Math.floor((sys_second / 60) % 60);
			var second = Math.floor(sys_second % 60);
			day_elem && $(day_elem).text(day);
			$(hour_elem).text(hour<10?"0"+hour:hour);
			$(minute_elem).text(minute<10?"0"+minute:minute);
			$(second_elem).text(second<10?"0"+second:second);
		} else { 
			clearInterval(timer);
		}
	}, 1000);
}
</script>
</head>
<body>
<div class="ct-pageWrapper">
  <main>
	<div class="container">
	  <div class="row">
		<div class="col-md-3">
		  <div class="widget">
			<h2 class="widget-header">Shopping Cart</h2>
			<div class="ct-cart"></div>
		  </div>
		</div>
		<div class="col-md-9">
		  <div class="row">
		  <#if goodsList?has_content >
		    	<#list goodsList as goods>
		    		<#if '${goods.type!""}'=='1'>
		    			<div class="col-sm-4">
						  <div class="ct-product">
							<div class="image"><img height="200px" width="200px" src="${base}/images/shoppingmall/img/${(goods.img)!''}" alt=""></div>
							<div class="inner"><a href="#" id="addtocart" class="btn btn-motive ct-product-button" style="display: none"><i class="fa fa-shopping-cart"></i></a>
							  <h2 class="ct-product-title">${(goods.goodsName)!''}</h2>
							  <p class="ct-product-description">
							  	<div class="colockbox" id="colockbox1">
		                        	<input type="hidden" id="startdate" type="text" class="input_text" value="${(goods.startDate)!''}" />
		                			<input type="hidden" id="enddate" type="text" class="input_text" value="${(goods.endDate)!''}" />
		                            <span id="day" class="num">00</span><span class="str">D</span><span id="hour" class="num">00</span><span class="str">H</span><span id="minute" class="num">00</span><span class="str">M</span><span id="second" class="num">00</span><span class="str">S</span>
		                        </div>
							  </p>
							  <span class="ct-product-price">${(goods.price)!''}</span>
							</div>
						  </div>
						</div>
					</#if>
		    		<#if '${goods.type!""}'=='2'>
		    			<div class="col-sm-4">
						  <div class="ct-product">
							<div class="image"><img height="200px" width="200px" src="${base}/images/shoppingmall/img/${(goods.img)!''}" alt=""></div>
							<div class="inner"><a href="#" class="btn btn-motive ct-product-button"><i class="fa fa-shopping-cart"></i></a>
							  <h2 class="ct-product-title">${(goods.goodsName)!''}</h2>
							  <p class="ct-product-description"></p><span class="ct-product-price">${(goods.price)!''}</span>
							</div>
						  </div>
						</div>
					</#if> 
					<#if '${goods.type!""}'=='3'>
		    			<div class="col-sm-4">
						  <div class="ct-product">
							<div class="image"><img height="200px" width="200px" src="${base}/images/shoppingmall/img/${(goods.img)!''}" alt=""></div>
							<div class="inner"><a href="#" class="btn btn-motive ct-product-button"><i class="fa fa-shopping-cart"></i></a>
							  <h2 class="ct-product-title">${(goods.goodsName)!''}</h2>
							  <p class="ct-product-description"></p><span class="ct-product-price">${(goods.price)!''}</span>
							</div>
						  </div>
						</div>
					</#if>
				</#list>
		 	</#if>
		  </div>
		</div>
	  </div>
	</div>
  </main>

<script src="${base}/js/newshoppingmall/shop.min.js" type="text/javascript"></script>
<script type="text/javascript">
$('body').ctshop({
  currency: '$',
  paypal: {
	currency_code: 'EUR'
  }
});
</script>
</div>

</body>
</html>