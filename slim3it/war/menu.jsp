<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<div id="menu">
<ul>
<li><a href="${f:url('/blog/')}">Blog</a></li>
<li><a href="${f:url('/flexblog/')}">Blog with Flex</a></li>
<li><a href="${f:url('/locale/')}">Locale</a></li>
<li><a href="${f:url('/timezone/')}">TimeZone</a></li>
<li><a href="${f:url('/checkbox/')}">Checkbox</a></li>
<li><a href="${f:url('/multibox/')}">Multibox</a></li>
</ul>
</div>