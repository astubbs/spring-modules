<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
<title>
</title>
</head>
<body>
<strong>Current Instance:</strong> <c:out value="${instanceId}"/><br>
<h2>Available Actions</h2>
<c:forEach var="action" items="${availableActions}">
    <a href="execute/<c:out value="${action.view}"/>"><c:out value="${action.name}"/></a><br>
</c:forEach>
<h2>Current Steps</h2>
<c:forEach var="step" items="${currentSteps}">
	<c:out value="${step.name}"/><br>
</c:forEach>
<h2>History Steps</h2>
<c:forEach var="step" items="${historySteps}">
	<c:out value="${step.name}"/><br>
</c:forEach>
</body>
</html>