<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<c:set var="ctx" value="${rc.contextPath}"/>

<b>Welcome to Spring Lucene category page.</b><br/><br/>

<br/><br/>
<b>Category <c:out value="${category.name}"/></b>:<br/><br/>
Id: <c:out value="${category.id}"/><br/>
Name: <c:out value="${category.name}"/>