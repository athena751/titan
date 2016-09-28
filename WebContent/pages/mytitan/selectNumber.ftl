<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width; initial-scale=1.0">
<title>Select number</title>
<script type="text/javascript" src="${base}/js/commons.js"></script>
<script type="text/javascript" src="${base}/js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="${base}/js/jquery.jBox.src.js"></script>
<script src="${base}/js/newshoppingmall/jquery.seat-charts.min.js" type="text/javascript"></script>
<style type="text/css">
.demo{width:700px; margin:40px auto 0 auto; min-height:450px;}
@media screen and (max-width: 360px) {.demo {width:340px}}

.front{width: 300px;margin: 5px 32px 45px 32px;background-color: #f0f0f0;	color: #666;text-align: center;padding: 3px;border-radius: 5px;}
.booking-details {float: right;position: relative;width:200px;height: 450px; }
.booking-details h3 {margin: 5px 5px 0 0;font-size: 16px;}
.booking-details p{line-height:26px; font-size:16px; color:#999}
.booking-details p span{color:#666}
div.seatCharts-cell {color: #182C4E;height: 25px;width: 25px;line-height: 25px;margin: 3px;float: left;text-align: center;outline: none;font-size: 13px;}
div.seatCharts-seat {color: #fff;cursor: pointer;-webkit-border-radius: 5px;-moz-border-radius: 5px;border-radius: 5px;}
div.seatCharts-row {height: 35px;}
div.seatCharts-seat.available {background-color: #B9DEA0;}
div.seatCharts-seat.focused {background-color: #76B474;border: none;}
div.seatCharts-seat.selected {background-color: #E6CAC4;}
div.seatCharts-seat.unavailable {background-color: #472B34;cursor: not-allowed;}
div.seatCharts-container {border-right: 1px dotted #adadad;width: 400px;padding: 20px;float: left;}
div.seatCharts-legend {padding-left: 0px;position: absolute;bottom: 16px;}
ul.seatCharts-legendList {padding-left: 0px;}
.seatCharts-legendItem{float:left; width:130px;margin-top: 10px;line-height: 2;}
span.seatCharts-legendDescription {margin-left: 5px;line-height: 30px;}
.checkout-button {display: block;width:80px; height:24px; line-height:20px;margin: 10px auto;border:1px solid #999;font-size: 14px; cursor:pointer}
#selected-seats {max-height: 150px;overflow-y: auto;overflow-x: none;width: 200px;}
#selected-seats li{float:left; width:72px; height:26px; line-height:26px; border:1px solid #d3d3d3; background:#f7f7f7; margin:6px; font-size:14px; font-weight:bold; text-align:center}
</style>

</head>

<body>
<form id="selectNumberForm" action="" method="POST">
<input type="hidden" id="consumeId" type="text" class="input_text" value="${(consumeId)!''}" />
<input type="hidden" id="selectedNumbers" type="text" class="input_text" value="${(selectedNumbers)!''}" />
<div id="main">
   <div class="demo">
   		<div id="seat-map">
			<div class="front">Please select number</div>					
		</div>
		<div class="booking-details">
			<p>Number：</p>
			<ul id="selected-seats"></ul>
					
			<button class="checkout-button" onclick="sumitSelect()">OK</button>
					
			<div id="legend"></div>
		</div>
		<div style="clear:both"></div>
   </div>
	
  <br/>
</div>
</form>
<script type="text/javascript" src="jquery.seat-charts.min.js"></script>
<script type="text/javascript">
var selectedNum = '';
$(document).ready(function() {
	var $cart = $('#selected-seats'); //座位区
	
	var sc = $('#seat-map').seatCharts({
		map: [  //座位图
			'aaaaaaaaaa',
            'aaaaaaaaaa',
            'aaaaaaaaaa',
            'aaaaaaaaaa',
			'aaaaaaaaaa',
			'aaaaaaaaaa',
			'aaaaaaaaaa',
			'aaaaaaaaaa',
			'aaaaaaaaaa',
            'aaaaaaaaaa'
		],
		naming : {
			top : false,
			getLabel : function (character, row, column) {
				return (row - 1) * 10 + column;
			}
		},
		legend : { 
			node : $('#legend'),
			items : [
				[ 'a', 'available',   'Selectable' ],
				[ 'a', 'unavailable', 'Selected']
			]					
		},
		click: function () { 
			if (this.status() == 'available') { 
				if($("#selected-seats li").length == 1){
					alert('You only can select one number!');
					return 'available';
				}
				selectedNum = this.settings.label;
				$('<li>'+this.settings.label+'</li>')
					.attr('id', 'cart-item-'+this.settings.id)
					.data('seatId', this.settings.id)
					.appendTo($cart);
				return 'selected';
			} else if (this.status() == 'selected') { 
					$('#cart-item-'+this.settings.id).remove();
					return 'available';
			} else if (this.status() == 'unavailable') { 
				return 'unavailable';
			} else {
				return this.style();
			}
		}
	});
	var selectedNums = $('#selectedNumbers').val();
	sc.get(eval(selectedNums)).status('unavailable');
		
});

function sumitSelect(){
	var consumeId = $('#consumeId').val();
	$.ajax({
			type : 'get',
			url : '${base}/mytitan/ifnumSelected.do?t=' + new Date().getTime(),
			dataType: "text",
			data : { 
					 'selectedNum' : selectedNum
			 },
			 async: false,
			success : function(msg) {
				if(msg == 'no'){
					$.ajax({
						type : 'get',
						url : '${base}/mytitan/doSubmitSelect.do?t=' + new Date().getTime(),
						dataType: "text",
						data : { 
								 'selectedNum' : selectedNum,
								 'consumeId': consumeId
						 },
						 async: false,
						success : function(msg) {
						}
					});
				}
				else{
					alert('This number has been selected!');
					return;
				}
			}
		});
		window.parent.location.reload();
	
}
</script>
</body>
</html>