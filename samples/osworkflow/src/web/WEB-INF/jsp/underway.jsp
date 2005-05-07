<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
<title>Workflow Status</title>
</head>
<body>
	<h2>Workflows Underway</h2>
	<c:forEach var="id" items="${ids}">
		<a href="status?instanceId=<c:out value="${id}"/>"><c:out value="${id}"/></a><br>
	</c:forEach>
</body>