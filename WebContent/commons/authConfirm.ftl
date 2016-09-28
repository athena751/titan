<#function isAuth authName> 
<#if loginUser.authMap[authName]?exists>
	<#return true>
<#else>
	<#return false>
</#if>
</#function>