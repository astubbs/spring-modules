<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>

<body>

	Please enter your search query:<br/>
	<form action="/spring-lucene/lucene/search" method="POST">
		<input type="text" name="string" value=""/>
		<input type="submit" value="search"/>
	</form>

</body>

</html>