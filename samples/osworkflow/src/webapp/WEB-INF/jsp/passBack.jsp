<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
  <head><title>Pass Back?</title></head>
  <body>
		<form method="POST">
		<table border="0">
			<tr>
				<td>Comment</td>
			</tr>
			<tr>
				<td>
					<spring:bind path="command.content">
	  				<textarea name="<c:out value="${status.expression}"/>"><c:out value="${status.value}"/></textarea>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td><input type="submit" value="Pass Back"/></td>
			</tr>
		</table>
		</form>
  </body>
</html>