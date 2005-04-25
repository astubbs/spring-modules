<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
  <head><title>Edit Document</title></head>
  <body>
		<h1>Edit Document</h1>
		<form method="post" >
		  <table border="0">
				<tr>
					<td>Title</td>
					<td>
						<spring:bind path="command.title">
	  					<input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
  					</spring:bind>
					</td>
				</tr>
				<tr>
					<td>Content</td>
					<td>
						<spring:bind path="command.content">
	  					<input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
  					</spring:bind>
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="Save"/></td>
					</tr>
				</table>
		</form>
  </body>
</html>