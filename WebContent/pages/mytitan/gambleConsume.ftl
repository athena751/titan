<!DOCTYPE html>
<html>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Gamble</title>
	<head>
	<#include "/system/include/head.ftl">
	<script type="text/javascript" src="${base}/js/jquery.jBox.src.js"></script>
	<script type="text/javascript">
		
	$(function(){
		var alldata0 = $('#selectedNumbers').val().split(",");
		var num0 = alldata0.length - 1;
		var show0 = $("#show0");
		var btn = $("#btn");
		var open0 = false;
		
		/*var alldata1 = $('#digitsNum').val().split(",");
		var num1 = alldata1.length - 1;
		var show1 = $("#show1");
		var btn0 = $("#btn0");
		var open1 = false;*/
		
		var btn1 = $("#btn1");
	
		function change0(){
			var randomVal = Math.round(Math.random() * num0);
			var prizeName = alldata0[randomVal];
			show0.text(prizeName);
		}
		
		/*function change1(){
			var randomVal = Math.round(Math.random() * num1);
			var prizeName = alldata1[randomVal];
			show1.text(prizeName);
		}*/
		
		function run0(){
			if(!open0){
				timer0=setInterval(change0,30);
				btn.removeClass('start').addClass('stop').text('Stop');
				open0 = true;
			}else{
				clearInterval(timer0);
				btn.removeClass('stop').addClass('start').text('Start');
				open0 = false;
			}
		}
		
		/*function run1(){
			if(!open1){
				timer1=setInterval(change1,30);
				btn0.removeClass('start').addClass('stop').text('Stop');
				open1 = true;
			}else{
				clearInterval(timer1);
				btn0.removeClass('stop').addClass('start').text('Start');
				open1 = false;
			}
		}*/
		
		function submit(){
			var num = $("#show0").text();
			if(num == '0'){
				$.jBox.tip('The Gamble haven\'t started!');
				return;
			}
			$.jBox("iframe:firing.do?selectedNum=" + num, {
			    title: "Fire",
			    width: 800,
			    height: 600,
			    buttons: { 'Close': true }
			});
		}
		
		btn.click(function(){run0();})
		btn1.click(function(){submit();})
		
	})
		
	</script>
	<style>
	body{ background:#fff;}
	.wrap{ width:300px; margin:100px auto;}
	.show{ width:300px; height:300px; background-color:#ff3300; line-height:300px; text-align:center; color:#fff; font-size:150px; -moz-border-radius:150px; -webkit-border-radius:150px; border-radius:150px; background-image: -webkit-gradient(linear,0% 0%, 0% 100%, from(#FF9600), to(#F84000), color-stop(0.5,#fb6c00)); -moz-box-shadow:2px 2px 10px #BBBBBB; -webkit-box-shadow:2px 2px 10px #BBBBBB; box-shadow:2px 2px 10px #BBBBBB;}
	.btn a{ display:block; width:120px; height:50px; margin:30px auto; text-align:center; line-height:50px; text-decoration:none; color:#fff; -moz-border-radius:25px; -webkit-border-radius:25px; border-radius:25px;}
	.btn a.start{ background:#80b600;}
	.btn a.start:hover{ background:#75a700;}
	.btn a.stop{ background:#00a2ff;}
	.btn a.stop:hover{ background:#008bdb;}
	</style>
	
	</head>

	<body>
	<form id="gambleForm" action="" method="POST">
		<input type="hidden" id="selectedNumbers" type="text" class="input_text" value="${(selectedNumbers)!''}" />
		<div class="wrap">
			<div class="show" id="show0">0</div>
			<div class="btn">
				<a href="javascript:void(0)" class="start" id="btn">Start</a>
				<a href="javascript:void(0)" class="start" id="btn1">Submit</a>
			</div>
		</div>
	</form>
	</body>
</html>