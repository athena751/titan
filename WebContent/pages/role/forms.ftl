 <#macro userForm editable=true>
<input type="hidden" name="userIds" id="textfield" value="${(userIds[0])!""}"/>
  <table width="400" border="1" align="center">
    <tr>
      <td>用户名</td>
      <td>
      <#if editable>
      <input type="text" name="name" id="textfield" value="${name!""}"/>
      <#else>
      <input type="hidden" name="name" id="textfield" value="${name!""}"/>${name!""}
      </#if>
      
      </td>
    </tr>
    <tr>
      <td>密码</td>
      <td>
      <#if editable>
      <input type="text" name="password" id="textfield2" value="${password!""}"/>
      <#else>
      <input type="hidden" name="password" id="textfield" value="${password!""}"/>${password!""}
      </#if>
      </td>
    </tr>
    <tr>
      <td>有效性</td>
      <td>
      <#if editable>
      <input type="checkbox" name="enable" id="checkbox" value="true" <#if enable!false>checked="true"</#if>/>
      <#else>
      <input type="hidden" name="enable" id="textfield" value="<#if enable!false>true<#else>false</#if>"/>${enable!string("有效","无效")}
      </#if>
      </td>
    </tr>
    <tr>
      <td colspan="2"><div align="center">
        <input type="submit" name="button" id="button" value="提交" />
      </div></td>
    </tr>
  </table>
  </#macro>
  
<#macro resourceForm editable=true>
<input type="hidden" name="resourceIds" id="textfield" value="${(resourceIds[0])!""}"/>
  <table width="400" border="1" align="center">
    <tr>
      <td>名称</td>
      <td>
      <#if editable>
      <input type="text" name="name" id="textfield" value="${name!""}"/>
      <#else>
      <input type="hidden" name="name" id="textfield" value="${name!""}"/>${name!""}
      </#if>
      
      </td>
    </tr>
    <tr>
      <td>描述</td>
      <td>
      <#if editable>
      <input type="text" name="description" id="textfield" value="${description!""}"/>
      <#else>
      <input type="hidden" name="description" id="textfield" value="${description!""}"/>${description!""}
      </#if>
      
      </td>
    </tr>
    <tr>
      <td>方法</td>
      <td>
      <#if editable>
      <@Select name="urlMethod" elements=urlMethods nullEnable=true nullLabel="空"/>
      <#else>
      <input type="hidden" name="urlMethod" id="textfield" value="${urlMethod!""}"/>
      <@Select name="urlMethod" elements=urlMethods nullEnable=true nullLabel="空" editable=false/>
      </#if>
      </td>
    </tr>
    <tr>
      <td>类型</td>
      <td>
      <#if editable>
     <@Select name="resourceTypeId" elements=resourceTypes nullEnable=false/>
      <#else>
      <input type="hidden" name="resourceTypeId" id="textfield" value="${resourceTypeId!""}"/>
      <@Select name="resourceTypeId" elements=resourceTypes nullEnable=false editable=false/>
      </#if>
      </td>
    </tr>
	<tr>
      <td>优先级</td>
      <td>
      <#if editable>
      <input type="text" name="pri" id="textfield" value="${pri!""}"/>
      <#else>
      <input type="hidden" name="pri" id="textfield" value="${pri!""}"/>${pri!""}
      </#if>
      
      </td>
    </tr>
    <tr>
      <td colspan="2"><div align="center">
        <input type="submit" name="button" id="button" value="提交" />
      </div></td>
    </tr>
  </table>
  </#macro>
  
  <#macro roleForm editable=true>
<input type="hidden" name="roleIdAry" id="textfield" value="${(roleIdAry[0])!""}"/>
  <table width="400" border="1" align="center">
    <tr>
      <td>名称</td>
      <td>
      <#if editable>
      <input type="text" name="role.roleName" id="textfield" value="${role.roleName!""}"/>
      <#else>
      <input type="hidden" name="role.roleName" id="textfield" value="${role.roleName!""}"/>${role.roleName!""}
      </#if>
      
      </td>
    </tr>
    <tr>
      <td>描述</td>
      <td>
      <#if editable>
      <input type="text" name="role.roleDesc" id="textfield2" value="${role.roleDesc!""}"/>
      <#else>
      <input type="hidden" name="role.roleDesc" id="textfield" value="${role.roleDesc!""}"/>${role.roleDesc!""}
      </#if>
      </td>
    </tr>

    <tr>
      <td colspan="2"><div align="center">
        <input type="submit" name="button" id="button" value="提交" />
      </div></td>
    </tr>
  </table>
  </#macro>
  
  <#macro permissionForm editable=true>
<input type="hidden" name="permissionIds" id="textfield" value="${(permissionIds[0])!""}"/>
  <table width="400" border="1" align="center">
    <tr>
      <td>名称</td>
      <td>
      <#if editable>
      <input type="text" name="name" id="textfield" value="${name!""}"/>
      <#else>
      <input type="hidden" name="name" id="textfield" value="${name!""}"/>${name!""}
      </#if>
      
      </td>
    </tr>
    <tr>
      <td>描述</td>
      <td>
      <#if editable>
      <input type="text" name="description" id="textfield2" value="${description!""}"/>
      <#else>
      <input type="hidden" name="description" id="textfield" value="${description!""}"/>${description!""}
      </#if>
      </td>
    </tr>

    <tr>
      <td colspan="2"><div align="center">
        <input type="submit" name="button" id="button" value="提交" />
      </div></td>
    </tr>
  </table>
  </#macro>
  
  <#macro Select name elements nullEnable=true nullValue="null" nullLabel="请选择" onChange="null" editable=true>
	<select id="${name}" name="${name}" <#if onChange!="null">onChange="${onChange}"</#if> <#if !editable>disabled="true"</#if>>
	<#if nullEnable><option value="${nullValue}">${nullLabel}</option></#if>
	<#list elements as e>
	<option value="${e.value}" <#if e.selected>selected="selected"</#if>>${e.label}</option>
	</#list>
    </select>
</#macro>
