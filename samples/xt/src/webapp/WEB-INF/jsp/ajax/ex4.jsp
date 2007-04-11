<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <script type="text/javascript" src='../js/springxt-min.js'></script>
        <script type="text/javascript" src='../js/custom.js'></script>
        <script type="text/javascript" src='../js/prototype.js'></script>
        <script type="text/javascript" src='../js/scriptaculous.js?load=effects'></script>
        <title>XT Ajax Framework : Example 4</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Ajax Framework</h1>
        <h3 align="center">List employees with XT ajax-enabled dynamic tables ...</h3>
        <p>
            This example uses XT Ajax Framework for listing employees in offices.
        </p>
        <form method="POST" action="">
            <table>
            <tr>
                <td>Choose an office:</td>
                <td>
                    <spring:bind path="command.office">
                        <select id="offices" name="${status.expression}">
                            <option>Select one ...</option>
                            <c:forEach items="${offices}" var="office">
                                <spring:transform value="${office}" var="officeString"/>
                                <option value="${officeString}">${office.name}</option>
                            </c:forEach>
                        </select>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td><input type="button" value="List" onclick="XT.doAjaxSubmit('listEmployees', this);"></td>
            </tr>
            <tr>
                <td><div id="loading"></div></td>
            </tr>
            <tr>
                <td id="message"></td>
            </tr>
            <tr>
                <td colspan="2">
                    <table border="1">
                        <thead>
                            <tr>
                                <th>Firstname</th>
                                <th>Surname</th>
                                <th>Matriculation Code</th>
                            </tr>
                        </thead>
                        <tbody id="employees">
                        </tbody>
                    </table>
                </td>
            </tr>
        </form>
    </body>
</html>
