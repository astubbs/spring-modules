<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<b>Document informations</b><br/><br>
Document identified by the following term : <c:out value="${identifierName}"/>=<c:out value="${identifierValue}"/><br/><br/>

<table border="1">
	<thead>
		<td>&nbsp;</td>
		<td>Field name</td>
		<td>Field value</td>
		<td>Indexed</td>
		<td>Stored</td>
	</thead>
	<c:forEach items="${fields}" var="field" varStatus="status">
		<tr>
			<td><c:out value="${status.count}"/></td>
			<td><c:out value="${field.name}"/></td>
			<td><c:out value="${field.value}"/></td>
			<td><c:out value="${field.indexed}"/></td>
			<td><c:out value="${field.stored}"/></td>
		</tr>
	</c:forEach>
</table>