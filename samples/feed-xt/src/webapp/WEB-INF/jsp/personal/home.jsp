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
        <script type="text/javascript" src='<c:url value="/js/feedxt.js"/>'></script>
    </head>
    <body>
        <center>
            <%@include file="/WEB-INF/jsp/includes/header.jsp" %>
            
            <div id="content">
                
                <div id="sidebar">
                    <%@include file="/WEB-INF/jsp/includes/menu.jsp" %>
                </div>
                
                <div id="mainbar">
                    <h1>Control Panel</h1>
                    <p>
                        Welcome, ${user.firstname}!<br/>
                        Here you can manage your feed subscriptions.
                    </p>
                    <p>
                        <img src="<c:url value='/images/new_sub.gif'/>"/>&nbsp;<a href="<c:url value='/personal/addSubscription.action'/>">Add subscriptions</a>
                        <br/>
                        <img src="<c:url value='/images/remove_sub.gif'/>"/>&nbsp;<a href="<c:url value='/personal/removeSubscriptions.action'/>">Remove subscriptions</a>
                        <br/>
                        <img src="<c:url value='/images/view_feeds.gif'/>"/>&nbsp;<a href="<c:url value='/personal/viewer.page'/>">View feeds</a>
                    </p>
                </div>
                
            </div>
            
            <%@include file="/WEB-INF/jsp/includes/footer.jsp" %>
        </center>
    </body>
</html>
