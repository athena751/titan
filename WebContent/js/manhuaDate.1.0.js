$(function() {
	$.fn.manhuaDate = function(options) {
		var defaults = {
			Event : "click",		
			Left : 0,				
			Top : 22,				
			fuhao : "-",			
			isTime : false,			
			beginY : 1949,			
			endY : 2049				
		};
		var options = $.extend(defaults,options);		
		var stc;
		if($("#calender").length<=0){
			$("body").prepend("<div class='calender'><div class='calenderContent'><div class='calenderTable'><div class='getyear'><a class='preMonth' id='preMonth'>last</a><select id='year'></select><select id='month'></select><a class='nextMonth' id='nextMonth'>next</a></div><div class='tablebg'><table id='calender' class='calendertb' cellpadding='0' cellspacing='1'><tr bgcolor='#D6D6D6'><th class='weekend'>Sun</th><th>Mon</th><th>Tur</th><th>Wed</th><th>Thu</th><th>Fri</th><th class='weekend noborder'>Sat</th></tr><tr><td class='weekend2'></td><td></td><td></td><td></td><td></td><td></td><td class='weekend2 noborder'></td></tr><tr><td class='weekend2'></td><td></td><td></td><td></td><td></td><td></td><td class='weekend2 noborder'></td></tr><tr><td class='weekend2'></td><td></td><td></td><td></td><td></td><td></td><td class='weekend2 noborder'></td></tr><tr><td class='weekend2'></td><td></td><td></td><td></td><td></td><td></td><td class='weekend2 noborder'></td></tr><tr><td class='weekend2'></td><td></td><td></td><td></td><td></td><td></td><td class='weekend2'></td></tr><tr><td class='weekend2'></td><td></td><td></td><td></td><td></td><td></td><td class='weekend2'></td></tr></table></div></div></div></div>");	
		}
		var $mhInput = $(this);
		var isToday = true;
		var date = new Date();
		var nowYear = date.getFullYear();
		var nowMonth = date.getMonth() + 1;
		var today = date.getDate();
		var nowWeek = new Date(nowYear, nowMonth - 1, 1).getDay();
		var nowLastday = getMonthNum(nowMonth, nowYear);
		for(var i=options.beginY; i<=options.endY; i++){
			$("<option value='"+i+"'>"+i+"</option>").appendTo($("#year"));
		}
		for(var i=1; i<=12; i++){
			$("<option value='"+i+"'>"+i+"</option>").appendTo($("#month"));
		}		
		ManhuaDate(nowYear, nowMonth, nowWeek, nowLastday);
		$("#preMonth").click(function() {
			isToday = false;
			var year = parseInt($("#year").val());
			var month = parseInt($("#month").val());		
			month = month - 1;
			if (month < 1) {
				month = 12;
				year = year - 1;
			}
			if(nowYear==year && nowMonth==month){
				isToday = true;
			}
			var week = new Date(year, month - 1, 1).getDay();
			var lastday = getMonthNum(month, year);
			ManhuaDate(year, month, week, lastday);
		});		
		$("#year").change(function() {	
			isToday = false;				   
			var year = parseInt($(this).val());		
			var month = parseInt($("#month").val());	
			if(nowYear==year && nowMonth==month){
				isToday = true;
			}
			var week = new Date(year, month - 1, 1).getDay();
			var lastday = getMonthNum(month, year);
			ManhuaDate(year, month, week, lastday);
		});		
		$("#month").change(function() {
			isToday = false;
			var year = parseInt($("#year").val());		
			var month = parseInt($(this).val());	
			if(nowYear==year && nowMonth==month){
				isToday = true;
			}
			var week = new Date(year, month - 1, 1).getDay();
			var lastday = getMonthNum(month, year);
			ManhuaDate(year, month, week, lastday);
		});		
		 $("#nextMonth").click(function() {
			isToday = false;
			var year = parseInt($("#year").val());
			var month = parseInt($("#month").val());
		
			month = parseInt(month) + 1;
			if (parseInt(month) > 12) {
				month = 1;
				year = parseInt(year) + 1;
			}
			if(nowYear==year && nowMonth==month){
				isToday = true;
			}
			var week = new Date(year, month - 1, 1).getDay();
			var lastday = getMonthNum(month, year);
			ManhuaDate(year, month, week, lastday);
		});
		 
		 function ManhuaDate(year, month, week, lastday) {
			$("#year").val(year);
			$("#month").val(month)
			var table = document.getElementById("calender");
			var n = 1;
			for (var j = 0; j < week; j++) {
				table.rows[1].cells[j].innerHTML = "&nbsp;"
			}
			for (var j = week; j < 7; j++) {
				if (n == today && isToday) {				
					table.rows[1].cells[j].className="tdtoday";				
				}else {
					table.rows[1].cells[j].className="";
				}
				table.rows[1].cells[j].innerHTML = n;
				n++;
			}
			for (var i = 2; i < 7; i++) {
				for (j = 0; j < 7; j++) {
					if (n > lastday) {
						table.rows[i].cells[j].innerHTML = "&nbsp"
					}
					 else {
						if (n == today && isToday) {						
							table.rows[i].cells[j].className="tdtoday";						
						}else {
							table.rows[i].cells[j].className="";
						}
						table.rows[i].cells[j].innerHTML = n;
						n++;
					}
				}
			}
		}
		function getMonthNum(month, year) {
			month = month - 1;
			var LeapYear = ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ? true: false;
			var monthNum;
			switch (parseInt(month)) {
			case 0:
			case 2:
			case 4:
			case 6:
			case 7:
			case 9:
			case 11:
				monthNum = 31;
				break;
			case 3:
			case 5:
			case 8:
			case 10:
				monthNum = 30;
				break;
			case 1:
				monthNum = LeapYear ? 29: 28;
			}
			return monthNum;
		}		
		$("#calender td:not(.tdtoday)").hover(function() {
			$(this).addClass("hover")
		},function() {
			$(this).removeClass("hover");
		});		
		$("#calender td").die().live("click",function() {	
			var dv = $(this).html();
			if (dv != "&nbsp;"){
				 var str = "";
				 if (options.isTime){			
					var nd = new Date();
					str = $("#year").val() + options.fuhao + $("#month").val() + options.fuhao + dv + " "+ nd.getHours()+":"+nd.getMinutes()+":"+nd.getSeconds();
				 }else{
					str = $("#year").val() + options.fuhao + $("#month").val() + options.fuhao + dv;
				}				 
				$("input.dateVisited").val(str);
				$("input.dateVisited").removeClass('dateVisited')
				$(".calender").hide();
			}
		});
		$mhInput.live(options.Event,function(e){											
			$(this).addClass("dateVisited");
			if(stc){
				clearTimeout(stc);
			}
			var iof = $(this).offset();
			$(".calender").css({ "left" : iof.left+options.Left,"top" : iof.top+options.Top });
			$(".calender").show();	
		});		
		$(".calender").live("mouseleave",function(){ 
			stc = setTimeout(function (){			
				$(".calender").hide();
				clearTimeout(stc);
			},3000);	
		});
		$(".calender").live("mousemove",function(){     
			if(stc){
				clearTimeout(stc);
			}
			$(this).show();
		});	
		
		$("#year").die().live("click",function(){     
			if(stc){
				clearTimeout(stc);
			}			
		});	
		$("#month").die().live("click",function(){     
			if(stc){
				clearTimeout(stc);
			}			
		});	
	};
});