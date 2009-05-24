<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Blog</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
<a href="${f:url('create')}">Create</a>
<table>
<thead>
<tr><th>Title</th><th>Content</th></tr>
</thead>
<tbody>
<c:forEach var="e" items="${blogList}">
<tr>
<td>${f:h(e.title)}</td><td>${f:h(e.content)}</td>
<c:set var="editUrl" value="edit?key=${e.key}"/>
<c:set var="deleteUrl" value="delete?key=${e.key}"/>
<td><a href="${f:url(editUrl)}">Edit</a></td>
<td><a href="${f:url(deleteUrl)}" onclick="return confirm('delete OK?')">Delete</a></td>
</tr>
</c:forEach>
</tbody>
</table>
</body>
</html>
