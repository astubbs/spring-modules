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
        <script type="text/javascript" src='<c:url value="/js/behaviours/addSubscriptionPage.js"/>'></script>
    </head>
    <body>
        <center>
            <%@include file="/WEB-INF/jsp/includes/header.jsp" %>
            
            <div id="content">
                
                <div id="sidebar">
                    <%@include file="/WEB-INF/jsp/includes/menu.jsp" %>
                </div>
                
                <div id="mainbar">
                    <h1>Add Subscription</h1>
                    <form:form commandName="command">
                        <table>
                            <tr>
                                <td>
                                    <div id="onSuccessMessage"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="onErrorsMessage"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Subscription name (unique):
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:input path="name" id="name.field"/>
                                </td>
                                <td>
                                    <div id="subscription.empty.name"/>
                                    <div id="subscription.duplicated.name"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Subscription feed URL:
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:input path="feedUrl" id="url.field"/>
                                </td>
                                <td>
                                    <div id="subscription.empty.url"/>
                                    <div id="subscription.malformed.url"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="button" id="addSubscription" value="Add"/>
                                </td>
                                <td><div id="loading"/></td>
                            </tr>
                        </table>
                    </form:form>
                </div>
                
            </div>
            
            <%@include file="/WEB-INF/jsp/includes/footer.jsp" %>
        </center>
    </body>
</html>
