<#macro pager query="query">
<center>
	<input type="hidden" id="pageNo" name="page.pageNo" value="${page.pageNo!""}">
	<input type="hidden" id="flag" name="page.flag">
	<font size="2">Total${page.dataCount!""}&nbsp;&nbsp;</font>
	<#if (0 == page.pageTotal || 1 == page.pageNo) >
	<img src="/titan/scripts/img/firstg.gif" clase="cursor:hand;"/>
	<#else>
	<img class="pageButton" src="/titan/scripts/img/first.gif"  onclick="javascript:$('#flag')[0].value='top';query();"/>
	</#if>
	<#if (0 == page.pageTotal || 1 == page.pageNo) >
	<img src="/titan/scripts/img/prevg.gif" />
	<#else>
	<img class="pageButton" src="/titan/scripts/img/prev.gif" onclick="javascript:$('#flag')[0].value='fore';query();" />
	</#if> 
	<#if (0 == page.pageTotal || page.pageTotal == page.pageNo) >
	<img src="/titan/scripts/img/nextg.gif"/>
	<#else>
	<img class="pageButton" src="/titan/scripts/img/next.gif" onclick="$('#flag')[0].value='next';query();"/>
	</#if>
	<#if (0 == page.pageTotal || page.pageTotal == page.pageNo) >
	<img src="/titan/scripts/img/lastg.gif" />
	<#else>
	<img class="pageButton" src="/titan/scripts/img/last.gif" onclick="javascript:$('#flag')[0].value='bottom';query();"/>
	</#if>
	<font size="2">&nbsp;&nbsp;${page.pageNo!""}/${page.pageTotal!""}Page&nbsp;&nbsp;To<input type="text" id="pageNoGoto" name="page.pageNoGoto" value="${page.pageNo!""}" style="Text-Align:right" size="1" maxlength="3">Page&nbsp;</font>
	<input type="button" name="pageNobutton" value='Submit' onclick="javascript:$('#flag')[0].value='goto';<#if query?exists>${query}()</#if>" class="button_default" onmouseout="this.style.color='#202020'" onmouseover="this.style.color='#650000'"> 
</center>
</#macro>
