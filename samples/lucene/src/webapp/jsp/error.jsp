<%@ page session="false"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<html>

<body>

An error occured!<br/><br/>
<b>Message:</b> <c:out value="${exception.message}"/><br/><br/>
<b>Stack:</b>
<pre><c:out value="${exception.stackTrace}"/></pre>
</body>

</html>