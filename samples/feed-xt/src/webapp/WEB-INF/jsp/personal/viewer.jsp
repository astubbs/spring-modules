<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html" />
        <title>Feed XT</title>
        <link href='<c:url value="/css/style.css"/>' rel="stylesheet" type="text/css" />
        <script type="text/javascript" src='<c:url value="/js/springxt.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/prototype.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/scriptaculous.js?load=effects"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/behaviour.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/feedxt.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/behaviours/viewerPage.js"/>'></script>
    </head>
    <body>
        <center>
            <%@include file="/WEB-INF/jsp/includes/header.jsp" %>
            
            <div id="content">
                
                <div id="sidebar">
                    <%@include file="/WEB-INF/jsp/includes/menu.jsp" %>
                    <h1>Subscriptions</h1>
                    <div class="submenu">
                        <c:if test="${!empty user.subscriptions}">
                            <c:forEach items="${user.subscriptions}" var="subscription">
                                <a class="feedLink" href="" feedName="${subscription.name}">${subscription.name}</a>	
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty user.subscriptions}">
                            <div>No subscriptions.</div>
                        </c:if> 
                    </div>
                </div>
                
                <div id="mainbar">
                    <h1>Feed Viewer&nbsp;&nbsp;&nbsp;<span id="loading"></span></h1>
                    <div id="viewer">
                        <p>Select a feed to view from your subscriptions in the sidebar.</p>
                    </div>
                    <div id="message"></div>
                </div>
                
            </div>
            
            <%@include file="/WEB-INF/jsp/includes/footer.jsp" %>
        </center>
    </body>
</html>
