<%@ page session="false"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<br/><br/>
<b>List of categories</b><br/><br/>
<table cellpadding="0" cellspacing="0" border="0">
  <c:forEach var="category" items="${categories}">
    <tr><td><a href="<c:url value="/category.html?id=${category.id}"/>&method=showCategory"><c:out value="${category.id}"/></a>&#160;-&#160;</td><td><c:out value="${category.name}"/></td></tr>
  </c:forEach>
</table>
