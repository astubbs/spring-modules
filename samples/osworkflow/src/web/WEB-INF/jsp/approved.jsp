<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
<title>Document Approved</title>
</head>
<body>
<h1>Approved</h1>

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

<a href="../index.jsp">Home</a> | <a href="../underway">Underway</a> | <a href="../logout">Logout</a>
</body>
</html>