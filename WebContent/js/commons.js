/*
required:mootools 1.2
*/

/********************************
	do action function
********************************/
function doPageRedirect(url){
	window.location=url;
}

function doPageBack(){
	window.history.back();
}

function doFormSubmit(formId,action){
	 document.getElementById(formId).action=action;
	 document.getElementById(formId).submit();
}

function Trim(str){
    return str.replace(/(^\s*)|(\s*$)/g, "");  
}

/*
 *rem:全选
 *par:checkAll 全选按钮的名称
 *par:checkItem 需要被全选的按钮数组的名称  
*/
function doCheckAll(checkAll, checkItem) {
	var ob1 = document.all(checkAll);
	var ob2 = document.all(checkItem);
	try {
		for (i = 0; i < ob2.length; i++) {
			ob2[i].checked = ob1.checked;
		}
	}
	catch (ex) {
	}
	try {
		ob2.checked = ob1.checked;
	}
	catch (ex) {
	}
}


//显示指定id的html
function doDisplayHtml(controlId) {
	var objHtmlControl = document.getElementById(controlId);    
	objHtmlControl.style.display = "inline";
	//或者
	//objHtmlControl.style.display = "block";
}

//隐藏指定id的html
function doHideHtml(controlId) {
	var objHtmlControl = document.getElementById(controlId);    
	objHtmlControl.style.display = "none";
}

//弹出新窗口
function doOpenNewWindow(page) {
	window.open(page);
}


//改变select下拉的值
function doChangeSelect(selectId,selectValue,selectedValue,label){
	var options = $(selectId).getElements("option");
	options.each(function(item, index){
	    if(item.value==selectValue){
			item.value=selectedValue;
			item.text=label;
			item.selected="selected";
		}
	});
}

/********************************
	getter function
********************************/

function getSystemTimeRd(){
	var nowTime=new Date();
	return nowTime.getTime();
}

//通过input,textarea的id，获得input值
function getInputValue(inputId) {
	return $(inputId).get("value");
}

function getRadioGroupValue(formId, radioGroupName) {
	//var elems=Form.getInputs(formId,'radio',radioGroupName);
	var elems = document.getElementsByName(radioGroupName);
	for (var i = 0; i < elems.length; i++) {
		if (elems[i].checked) {
			return elems[i].value;
		}
	}
	return null;
}

/********************************
	setter function
********************************/

function setDivHtml(divId,content){
	$(divId).set("html",content);
}

function setInputValue(inputId,newValue){
	$(inputId).set("text",newValue);
}

/********************************
	check radio function
********************************/

function checkRadio(formId, radioGroupName) {
	//var elems=Form.getInputs(formId,'radio',radioGroupName);
	var elems = document.getElementsByName(radioGroupName);
	for (var i = 0; i < elems.length; i++) {
		if (elems[i].checked) {
			return true;
		}
	}
	return false;
}

function IsNum(s)
{
    if(s!=null){
        var r,re;
        re = /\d*/i;
        r = s.match(re);
        return (r==s)?true:false;
    }
    return false;
}