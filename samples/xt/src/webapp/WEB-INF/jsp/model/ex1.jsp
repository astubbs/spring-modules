<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>XT Modeling Framework : Example 1</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Modeling Framework</h1>
        <h3 align="center">Insert a new employee with bean introductor ...</h3>
        <p>
            This example uses XT bean introductor for directly binding the employee office on the employee command object.<br/>
            Be careful: do not insert duplicated matriculation codes and do not put too many employees in the same office!
        </p>
        <form method="POST" action="">
            <table>
                <spring:bind path="command.matriculationCode">
                <tr>
                    <td>Matriculation code :</td>
                    <td>
                        <input type="text" name="${status.expression}" value="${status.value}">
                    </td>
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
                    <td>Firstname :</td>
                    <td>
                        <spring:bind path="command.firstname">
                            <input type="text" name="${status.expression}" value="${status.value}">
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td>Surname :</td>
                    <td>
                        <spring:bind path="command.surname">
                            <input type="text" name="${status.expression}" value="${status.value}">
                        </spring:bind>
                    </td>
                </tr>
                <spring:bind path="command.office">
                <tr>
                    <td>Select the employee office :</td>
                    <td>
                        <select name="${status.expression}">
                            <option>Select one ...</option>
                            <c:forEach items="${offices}" var="office">
                                <spring:transform value="${office}" var="officeString"/>
                                <option value="${officeString}">${office.name}</option>
                            </c:forEach>
                        </select>
                    </td>
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
                    <td><input type="submit" value="Add"></td>
                </tr>
            </table>
        </form>
    </body>
</html>