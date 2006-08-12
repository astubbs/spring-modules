<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>XT Modeling Framework : Example 3</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Modeling Framework</h1>
        <h3 align="center">Create a new Office and use composite specifications for validating it ...</h3>
        <p>
            This example uses XT composite specifications for validating the new office ...
        </p>
        <form method="POST" action="">
            <table>
                <spring:bind path="command.name">
                <tr>
                    <td>Name: </td>
                    <td><input type="text" name="${status.expression}" value="${status.value}"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <c:forEach items="${status.errorMessages}" var="errorMessage">
                            <c:out value="${errorMessage}"/><br>
                        </c:forEach>
                    </td>
                </tr>
                </spring:bind>
                <spring:bind path="command.officeId">
                <tr>
                    <td>Id: </td>
                    <td><input type="text" name="${status.expression}" value="${status.value}"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <c:forEach items="${status.errorMessages}" var="errorMessage">
                            <c:out value="${errorMessage}"/><br>
                        </c:forEach>
                    </td>
                </tr>
                </spring:bind>
                <tr>
                    <td colspan="2">Employees</td>
                </tr>
                <spring:bind path="command.employees">
                    <c:forEach items="${employees}" var="emp" varStatus="loop">
                        <tr>
                            <td><input type="checkbox" name="${status.expression}" value="${emp.matriculationCode}"></td>
                            <td><c:out value="${emp.firstname}"/>&nbsp;<c:out value="${emp.surname}"/></td>
                        </tr>
                    </c:forEach>
                    <tr>
                    <td colspan="2">
                        <c:forEach items="${status.errorMessages}" var="errorMessage">
                            <c:out value="${errorMessage}"/><br>
                        </c:forEach>
                    </td>
                </tr>
                </spring:bind>
            </table>
            <div><input type="submit" value="Create"></div>
        </form>
    </body>
</html>