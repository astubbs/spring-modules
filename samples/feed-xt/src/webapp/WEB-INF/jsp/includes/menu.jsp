<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="submenu">
    <img src="<c:url value='/images/spring.gif'/>" alt="Spring" class="logo" />
    <br /><br /><br /><br />
    <img src="<c:url value='/images/springmodules.gif'/>" alt="Spring Modules" class="logo" />
    <br /><br />
</div>
<h1>Menu</h1>
<div class="submenu">
    <a href="<c:url value='/start.action'/>">Start Page</a>
    <c:if test="${user != null}">
        <a href="<c:url value='/personal/home.page'/>">Home / Control Panel</a>
        <a href="<c:url value='/logout.action'/>">Log Out</a>
    </c:if>
</div>
<c:if test="${user == null}">
    <h1>Links</h1>
    <div class="submenu">
        <a href="https://www.springframework.org/">Spring Framework</a>	
        <a href="https://springmodules.dev.java.net/">Spring Modules</a>			 
    </div>
    <h1>Sign Up</h1>
    <a href="<c:url value='/signup.action'/>">Sign Up!</a>
</c:if>