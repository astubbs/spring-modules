<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
        <script type="text/javascript" src='<c:url value="/js/behaviours/startPage.js"/>'></script>
    </head>
    <body>
        <center>
            <%@include file="/WEB-INF/jsp/includes/header.jsp" %>
            
            <div id="content">
                
                <div id="sidebar">
                    <%@include file="/WEB-INF/jsp/includes/menu.jsp" %>
                </div>
                
                <div id="mainbar">
                    <h1>Introduction</h1>
                    <p>
                        Hello and welcome to Feed XT.
                        <br />
                        Feed XT is a web based Feed Reader built on top of the Spring Framework, Spring Modules and the latest Web 2.0
                        technologies.
                    </p>
                    <p>
                        Just sign up and enjoy its beauty, power and simplicity ... it will take you just a few moments! 
                    </p>
                    <c:if test="${user == null}">
                        <h1>Log In</h1>
                        <form:form commandName="command">
                            <table>
                                <tr>
                                    <td>
                                        Username:
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <form:input path="username" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Password:
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <form:password path="password" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div id="wrong.login"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="button" id="login" value="Log In"/>
                                    </td>
                                    <td><div id="loading" /></td>
                                </tr>
                            </table>
                        </form:form>
                    </c:if>
                </div>
                
            </div>
            
            <%@include file="/WEB-INF/jsp/includes/footer.jsp" %>
        </center>
    </body>
</html>
