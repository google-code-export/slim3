<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="html" uri="http://struts.apache.org/tags-html"%>  
<%@taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>  
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@taglib prefix="s" uri="http://www.slim3.org/tags"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>

<html>
<head>
<title>Multiple Select index</title>
<link rel="stylesheet" type="text/css" href="../css/global.css" />
</head>
<body>

<h1>Multiple Select index</h1>

<html:errors/>
<s:form>
multiselect:${f:h(multiselect)}<br />
<html:select property="multiselect" multiple="true" size="3">
<html:option value="1">One</html:option>
<html:option value="2">Two</html:option>
<html:option value="3">Three</html:option>
</html:select>
<br />
<input type="submit" name="submit" value="submit"/>
</s:form>
</body>
</html>