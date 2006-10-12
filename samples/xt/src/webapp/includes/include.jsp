<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%-- Dynamic content: --%> 
<div>
    <c:out value="This is ${param.msg}!"/>
</div>

<%-- Dynamic Javascript: --%>
<script type="text/javascript">
    alert("This is ${param.msg}!");
</script>