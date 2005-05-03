<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>

<body>

<b>Index informations</b><br/><br>
<li>has deletions: <c:out value="${infos.hasDeletions}"/></li>
<li>number of indexed documents: <c:out value="${infos.numDocs}"/></li>

</body>

</html>