<!DOCTYPE html>
<html>

<head>

  <meta charset="UTF-8">

  <title>Canva</title>
  <link rel="stylesheet" href="css/style.css" media="screen" type="text/css" />
  <link rel="stylesheet" type="text/css" href="${base}/css/font/style.css" />
  <style>
html,body,div,span,applet,object,iframe,h1,h2,h3,h4,h5,h6,p,blockquote,pre,a,abbr,acronym,address,big,cite,code,del,dfn,em,img,ins,kbd,q,s,samp,small,strike,strong,sub,sup,tt,var,b,u,i,center,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,tr,th,td,article,aside,canvas,details,embed,figure,figcaption,footer,header,hgroup,menu,nav,output,ruby,section,summary,time,mark,audio,video{margin:0;padding:0;border:0;font-size:100%;font:inherit;vertical-align:baseline}article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,section{display:block}body{line-height:1}ol,ul{list-style:none}blockquote,q{quotes:none}blockquote:before,blockquote:after,q:before,q:after{content:'';content:none}table{border-collapse:collapse;border-spacing:0}

</style>

    <style>
html, body {
	margin: 0;	
	padding: 0;
}

body {
	background: #171717;
	color: #999;
	font: 100%/18px helvetica, arial, sans-serif;
}

a {
	color: #2fa1d6;
	font-weight: bold;
	text-decoration: none;
}

a:hover {
	color: #fff;	
}

#canvas-container {
	background: #000 url(${base}/images/fire/bg.jpg);
  height: 400px;
	left: 50%;
	margin: -200px 0 0 -300px;
	position: absolute;
	top: 50%;
  width: 600px;
	z-index: 2;
}

canvas {
	cursor: crosshair;
	display: block;
	position: relative;
	z-index: 3;
}

canvas:active {
	cursor: crosshair;
}

#skyline {
	background: url(skyline.png) repeat-x 50% 0;
	bottom: 0;
	height: 135px;
	left: 0;
	position: absolute;
	width: 100%;
	z-index: 1;	
}

#mountains1 {
	background: url(mountains1.png) repeat-x 40% 0;
	bottom: 0;
	height: 200px;
	left: 0;
	position: absolute;
	width: 100%;
	z-index: 1;	
}

#mountains2 {
	background: url(mountains2.png) repeat-x 30% 0;
	bottom: 0;
	height: 250px;
	left: 0;
	position: absolute;
	width: 100%;
	z-index: 1;	
}

#gui {
	right: 0;
	position: fixed;
	top: 0;
	z-index: 3;
}

</style>

    <script src="${base}/js/fire/prefixfree.min.js"></script>

</head>

<body>
<form id="fireForm" action="" method="POST">
	<div style="text-align:center;clear:both;">
	</div>
	  <div id="gui"></div>		
	<div id="canvas-container">
  <div class="loader font1">
  <span>${(winnerName)!''}</span>
  <br><br>
  <span class="span2">C</span>
  <span class="span3">o</span>
  <span class="span4">n</span>
  <span class="span5">g</span>
  <span class="span6">r</span>
  <span class="span7">a</span>
  <span class="span2">t</span>
  <span class="span3">u</span>
  <span class="span4">l</span>
  <span class="span5">a</span>
  <span class="span6">t</span>
  <span class="span7">i</span>
  <span class="span2">o</span>
  <span class="span3">n</span>
  <span class="span4">s</span>
  <span class="span5">!</span>
</div>
	  <div id="mountains2"></div>
	  <div id="mountains1"></div>
	  <div id="skyline"></div>
	</div>
</form>

  <script type="text/javascript" src="${base}/js/commons.js"></script>
	<script type="text/javascript" src="${base}/js/jquery-1.9.0.min.js"></script>
	<script type="text/javascript" src="${base}/js/jquery.jBox.src.js"></script>
	<script src="${base}/js/fire/index.js" type="text/javascript"></script>  
</body>

</html>