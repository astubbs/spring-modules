<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<c:set var="ctx" value="${rc.contextPath}"/>

<b>Welcome to Spring Lucene category page.</b><br/><br/>

<br/><br/>
<b>List of categories</b><br/><br/>
<table cellpadding="0" cellspacing="0" border="0">
  <c:forEach var="category" items="${categories}">
    <tr><td><a href="<c:out value="${ctx}"/>/category.html?id=<c:out value="${category.id}"/>&method=showCategory"><c:out value="${category.id}"/></a>&#160;-&#160;</td><td><c:out value="${category.name}"/></td></tr>
  </c:forEach>
</table>
