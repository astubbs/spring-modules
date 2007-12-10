<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- Dynamic content: --%> 
<div>
    This is a <c:out value="${param.jspVar}"/> generated HTML!
</div>

<%-- Dynamic Javascript: --%>
<script type="text/javascript">
    alert("This is a ${requestScope.jspVar2} generated Javascript!");
</script>
