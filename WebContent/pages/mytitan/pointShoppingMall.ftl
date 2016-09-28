<!DOCTYPE html>
<head>
<#include "/system/include/head.ftl">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Points shopping mall</title>
<link rel="stylesheet" type="text/css" href="${base}/css/shoppingmall/default.css" />
<link rel="stylesheet" type="text/css" href="${base}/css/shoppingmall/styles.css" />
<script src="${base}/js/shoppingmall/stopExecutionOnTimeout.js" type="text/javascript"></script>
<script src="${base}/js/shoppingmall/main.js" type="text/javascript"></script>
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
<form id="shoppingmallForm" action="" method="POST">
<input type="hidden" id="jsonObj" name="jsonObj"/>
<div class="cart-icon-top">
</div>

<div class="cart-icon-bottom">
</div>

<div id="checkout">
	CHECKOUT
</div>

<div id="header">	
	<ul>
        <li><a href="#">Welcome To Titan Points Shopping Mall!</a></li>
    </ul>		
</div>

<div id="sidebar">
	<h3>CART</h3>
    <div id="cart">
    	<span class="empty">No items in cart.</span>       
    </div>
</div>
</form>
<div id="grid">
	<#list goodsList as goods>
    	<#if '${goods.goodsId!""}'=='1'>
		    <div class="product">
		        <div class="make3D">
		            <div class="product-front">
		                <div class="shadow"></div>
		                <img src="${base}/images/shoppingmall/img/${(goods.img)!''}" alt="" />
		                <div class="image_overlay"></div>
		                <div id="addtocart" class="add_to_cart" style="display: none">Add to cart</div>
		                <div class="view_gallery">View gallery</div>                
		                <div class="stats">        	
		                    <div class="stats-container">
		                        <span class="product_price"></span>
		                        <span class="product_name">${(goods.goodsName)!''}</span>    
		                        
		                        <div class="product-options">
		                        <strong>Rule</strong>
		                        <span>${(goods.describe)!''}</span>
		                        <div class="colors">
		                            <input id="price" type="text" class="input_text"/>
		                        </div>
		                        <div class="colockbox" id="colockbox1">
		                        	<input type="hidden" id="startdate" type="text" class="input_text" value="${(goods.startDate)!''}" />
		                			<input type="hidden" id="enddate" type="text" class="input_text" value="${(goods.endDate)!''}" />
		                            <span id="day" class="num">00</span><span class="str">D</span><span id="hour" class="num">00</span><span class="str">H</span><span id="minute" class="num">00</span><span class="str">M</span><span id="second" class="num">00</span><span class="str">S</span>
		                        </div>
		                    </div>                       
		                    </div>                         
		                </div>
		            </div>
		            
		            <div class="product-back">
		                <div class="shadow"></div>
		                <div class="carousel">
		                    <ul class="carousel-container">
		                        <img src="${base}/images/shoppingmall/img/${(goods.img)!''}" alt="" />
		                    </ul>
		                    <div class="arrows-perspective">
		                        <div class="carouselPrev">
		                            <div class="y"></div>
		                            <div class="x"></div>
		                        </div>
		                        <div class="carouselNext">
		                            <div class="y"></div>
		                            <div class="x"></div>
		                        </div>
		                    </div>
		                </div>
		                <div class="flip-back">
		                    <div class="cy"></div>
		                    <div class="cx"></div>
		                </div>
		            </div>	  
		        </div>	
		    </div>
		</#if>    
	</#list>
    
    <#if goodsList?has_content >
    	<#list goodsList as goods>
    		<#if '${goods.goodsId!""}'!='1'>
			    <div class="product">
			        <div class="make3D">
			            <div class="product-front">
			                <div class="shadow"></div>
			                <img src="${base}/images/shoppingmall/img/${(goods.img)!''}" alt="" />
			                <div class="image_overlay"></div>
			                <div class="add_to_cart">Add to cart</div>
			                <div class="view_gallery">View gallery</div>                
			                <div class="stats">        	
			                    <div class="stats-container">
			                        <span class="product_price">${(goods.price)!''}</span>
			                        <input type="hidden" type="text" class="input_text" value="${(goods.price)!''}" />
			                        <span class="product_name">${(goods.goodsName)!''}</span>    
			                        
			                        <div class="product-options">
			                        <strong>Describe</strong>
			                        <span>${(goods.describe)!''}</span>
			                    </div>                       
			                    </div>                         
			                </div>
			            </div>
			            
			            <div class="product-back">
			                <div class="shadow"></div>
			                <div class="carousel">
			                    <ul class="carousel-container">
			                        <li><img src="${base}/images/shoppingmall/img/${(goods.img)!''}" alt="" /></li>
			                    </ul>
			                    <div class="arrows-perspective">
			                        <div class="carouselPrev">
			                            <div class="y"></div>
			                            <div class="x"></div>
			                        </div>
			                        <div class="carouselNext">
			                            <div class="y"></div>
			                            <div class="x"></div>
			                        </div>
			                    </div>
			                </div>
			                <div class="flip-back">
			                    <div class="cy"></div>
			                    <div class="cx"></div>
			                </div>
			            </div>	  
			        </div>	
			    </div>
			</#if>    
		</#list>
 	</#if>
</div>
</div>
</body>
</html>