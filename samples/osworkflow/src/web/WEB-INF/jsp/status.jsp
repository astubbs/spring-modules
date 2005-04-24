<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
<title>
</title>
</head>
<body>
<c:out value="${instanceId}"/><br>
<c:forEach var="action" items="${availableActions}">
    <a href="/execute/<c:out value="${action.view}"/>"><c:out value="${action.name}"/></a><br>
</c:forEach>
</body>
</html>