<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
<title>Workflow Status</title>
</head>
<body>
<strong>Current State:</strong> <c:out value="${state}"/><br>

<c:if test="${document != null}">
<h2>Current Document</h2>
<table border="0">
	<tr>
		<td>Title:</td>
		<td><c:out value="${document.title}"/></td>
	</tr>
	<tr>
		<td>Content:</td>
		<td><c:out value="${document.content}"/></td>
	</tr>
</table>
</c:if>

<c:if test="${comments != null}">
<h2>Editor Comments</h2>
<c:forEach var="comment" items="${comments}">
	<p><c:out value="${comment.content}"/></p>
</c:forEach>
</c:if>

<!-- List the available actions -->
<h3>Available Actions</h3>
<c:forEach var="action" items="${availableActions}">
    <a href="execute/<c:out value="${action.view}"/>"><c:out value="${action.name}"/></a><br>
</c:forEach>

<!-- Show current step(s) -->
<h3>Current Step(s)</h3>
<c:forEach var="step" items="${currentSteps}">
	<c:out value="${step.name}"/><br>
</c:forEach>

<!-- Show previous executed step(s) -->
<h3>History Steps</h3>
<c:forEach var="step" items="${historySteps}">
	<c:out value="${step.name}"/><br>
</c:forEach>

<br>
<a href="../index.jsp">Home</a> | <a href="underway">Underway</a> | <a href="logout">Logout</a>
</body>
</html>