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
        <script type="text/javascript" src='<c:url value="/js/behaviours/signupPage.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/calendar/calendar.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/calendar/anchor.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/calendar/date.js"/>'></script>
        <script type="text/javascript" src='<c:url value="/js/calendar/window.js"/>'></script>
        <script type="text/javascript">
            var cal = new CalendarPopup();
            cal.showNavigationDropdowns();
            cal.setYearSelectStartOffset(80);
        </script>
    </head>
    <body>
        <center>
            <%@include file="/WEB-INF/jsp/includes/header.jsp" %>
            
            <div id="content">
                
                <div id="sidebar">
                    <%@include file="/WEB-INF/jsp/includes/menu.jsp" %>
                </div>
                
                <div id="mainbar">
                    <h1>Sign Up</h1>
                    <form:form commandName="command">
                        <table>
                            <tr>
                                <td>
                                    Firstname:
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:input path="firstname" />
                                </td>
                                <td>
                                    <div id="user.empty.firstname"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Surname:
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:input path="surname" />
                                </td>
                                <td>
                                    <div id="user.empty.surname"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Birthdate:
                                    <a href="#" onclick="cal.select(document.getElementById('calendarInput'),'calendarAnchor','dd/MM/yyyy'); return false;">&nbsp;&nbsp;&nbsp;(select)</A>
                                    <a id="calendarAnchor" name="calendarAnchor" />
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:input path="birthdate" readonly="true" id="calendarInput"/>
                                </td>
                                <td>
                                    <div id="user.empty.birthdate"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Username:
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:input path="username" id="usernameInput"/>
                                </td>
                                <td>
                                    <div id="user.empty.username"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <div id="username.validation"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Password (at least 5 characters):
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:password path="password" />
                                </td>
                                <td>
                                    <div id="user.empty.password"/>
                                    <div id="user.short.password"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Confirm password:
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:password path="confirmedPassword" />
                                </td>
                                <td>
                                    <div id="user.wrong.password"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="button" id="signup" value="Sign Up"/>
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
