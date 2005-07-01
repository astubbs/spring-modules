<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<b>Query results</b><br/><br>
Results for the query : <c:out value="${query.string}"/><br/><br/>

<table border="1">
	<thead>
		<td>&nbsp;</td>
		<td>Id</td>
		<td>Source</td>
		<td>Score</td>
		<td>Categorie</td>
	</thead>
	<c:forEach items="${results}" var="result" varStatus="status">
		<tr>
			<td><c:out value="${status.count}"/></td>
			<td>
			  <c:choose>
			    <c:when test="">
			      <c:out value="${result.id}"/>
			    </c:when>
			    <c:otherwise>
			      <a href="documentInfos.html?identifierName=id&identifierValue=<c:out value="${result.id}"/>"><c:out value="${result.id}"/></a>
			    </c:otherwise>
			  </c:choose>
			</td>
			<td><c:out value="${result.source}"/></td>
			<td><c:out value="${result.score}"/></td>
			<td><c:out value="${result.category}"/></td>
		</tr>
	</c:forEach>
</table>