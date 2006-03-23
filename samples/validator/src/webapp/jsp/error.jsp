<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>

<body>

An error occured!<br/><br/>
<b>Message:</b> <c:out value="${exception.message}"/><br/><br/>
<b>Stack:</b>
<pre><c:out value="${exception.stackTrace}"/></pre>
</body>

</html>