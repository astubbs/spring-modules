<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
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
        <script type="text/javascript" src='<c:url value="/js/behaviours/removeSubscriptionsPage.js"/>'></script>
    </head>
    <body>
        <center>
            <%@include file="/WEB-INF/jsp/includes/header.jsp" %>
            
            <div id="content">
                
                <div id="sidebar">
                    <%@include file="/WEB-INF/jsp/includes/menu.jsp" %>
                </div>
                
                <div id="mainbar">
                    <h1>Remove Subscriptions</h1>
                    <c:if test="${!empty command.subscriptions}">
                        <form:form commandName="command">
                            <table>
                                <c:forEach items="${command.subscriptions}" var="subscription">
                                    <tr>
                                        <td>
                                            <form:checkbox path="subscriptionsToRemove" value="${subscription.name}"/>
                                        </td>
                                        <td>&nbsp;${subscription.name}</td>
                                    </tr>
                                </c:forEach>
                                <tr>
                                    <td>
                                        <input type="button" id="removeSubscriptions" value="Remove" 
                                        confirmMessage="<fmt:message key='remove.subscriptions.confirm.message'/>"/>
                                    </td>
                                    <td><div id="loading"/></td>
                                </tr>
                            </table>
                        </form:form>
                    </c:if>
                    <c:if test="${empty command.subscriptions}">
                        <p>No subscriptions.</p>
                    </c:if>
                </div>
                
            </div>
            
            <%@include file="/WEB-INF/jsp/includes/footer.jsp" %>
        </center>
    </body>
</html>
