<%@ page session="false"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<br/><br/>
<b>Category <c:out value="${category.name}"/></b>:<br/><br/>
Id: <c:out value="${category.id}"/><br/>
Name: <c:out value="${category.name}"/>