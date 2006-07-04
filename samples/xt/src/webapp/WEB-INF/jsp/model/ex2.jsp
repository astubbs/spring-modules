<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Spring Modules XT Modeling Framework : Example 2</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>Spring Modules XT Framework</h1>
        <h3 align="center">Remove employees from our Office <c:out value="${param.office}"/> with introductor collection ...</h3>
        <p>
            This example uses Spring Modules XT introductor collection for managing selectable collections of employees ...
        </p>
        <form method="POST" action="">
            <table>
                <tr>
                    <th>Surname</th>
                    <th>Remove</th>
                </tr>
                <c:forEach items="${command.selectableEmployees}" var="emp" varStatus="loop">
                        <spring:bind path="command.selectableEmployees[${loop.index}].selected">
                        <tr>
                            <td><c:out value="${emp.surname}"/></td>
                            <td><input type="checkbox" name="${status.expression}"></td>
                        </tr>
                        </spring:bind>
                </c:forEach>
            </table>
            <div><input type="submit" value="Remove"></div>
        </form>
    </body>
</html>