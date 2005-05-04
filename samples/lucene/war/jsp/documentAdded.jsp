<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>

<body>

<b>The file <u><c:out value="${filename}"/></u> has been added to the index!</b><br/><br/>

<a href="/spring-lucene/lucene/index">Index informations</a>

</body>

</html>