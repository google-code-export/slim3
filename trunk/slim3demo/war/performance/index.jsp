<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>performance Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>
Source:
<ul>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/performance/GetLLController.java" target="_blank">Controller for Low level API</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/performance/GetSlim3Controller.java" target="_blank">Controller for Slim3</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/performance/GetObjectifyController.java" target="_blank">Controller for Objectify</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/controller/performance/GetJDOController.java" target="_blank">Controller for JDO</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/cool/service/PerformanceService.java" target="_blank">PerformanceService</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/cool/model/Bar.java" target="_blank">Model for Slim3</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/cool/model/BarObjectify.java" target="_blank">Model for Objectify</a></li>
<li><a href="http://code.google.com/p/slim3/source/browse/trunk/slim3demo/src/slim3/demo/cool/model/BarJDO.java" target="_blank">Model for JDO</a></li>
</ul>
<hr />

The number of entities: ${count}
<table>
<tr>
<td>${getLL} millis</td>
<td>${getSlim3} millis</td>
<td>${getObjectify} millis</td>
<td>${getJDO} millis</td>
</tr>
<tr>
<td>
<form method="post" action="getLL">
<input type="submit" value="Low level API:query"/>
</form>
</td>
<td>
<form method="post" action="getSlim3">
<input type="submit" value="Slim3:query"/>
</form>
</td>
<td>
<form method="post" action="getObjectify">
<input type="submit" value="Objectify:query"/>
</form>
</td>
<td>
<form method="post" action="getJDO">
<input type="submit" value="JDO:query"/>
</form>
</td>
</tr>
</table>

</body>
</html>
