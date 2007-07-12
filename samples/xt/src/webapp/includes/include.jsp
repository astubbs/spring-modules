<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- Dynamic content: --%> 
<div>
    <c:out value="${param.msg}!"/>
</div>
<div>
    Today date : <fmt:formatDate value="${requestScope.date}"/>
</div>

<%-- Dynamic Javascript: --%>
<script type="text/javascript">
    alert("This is ${requestScope.msg}!");
</script>
