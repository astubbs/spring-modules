<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>

<body>

Results for the query : <c:out value="${query.string}"/><br/><br/>

<table border="1">
	<thead>
		<td>&nbsp;</td>
		<td>Source</td>
		<td>Score</td>
	</thead>
	<c:forEach items="${results}" var="result" varStatus="status">
		<tr>
			<td><c:out value="${status.count}"/></td>
			<td><c:out value="${result.source}"/></td>
			<td><c:out value="${result.score}"/></td>
		</tr>
	</c:forEach>
</table>

<br/>
<a href="/spring-lucene/lucene/search">New search</a>

</body>

</html>