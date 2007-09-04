<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <script type="text/javascript" src='../js/springxt.js'></script>
        <script type="text/javascript" src='../js/custom.js'></script>
        <script type="text/javascript" src='../js/prototype.js'></script>
        <script type="text/javascript" src='../js/scriptaculous.js?load=effects'></script>
        <title>XT Ajax Framework : Example 3</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Ajax Framework</h1>
        <h3 align="center">Take a look at XT ajax-enabled validation ...</h3>
        <p>
            This example uses XT Ajax Framework for adding ajax-enabled validation to 
            <a href='<c:url value="/model/ex1.action"/>'>XT Modeling Framework Example 1</a>.<br/>
            Try inserting duplicated matriculation codes or assigning the employee to a full office ...
        </p>
        <form method="POST" action="">
            <table>
                <tr>
                    <td colspan="2">
                        <div id="employee._"/>
                    </td>
                </tr>
                <tr>
                    <td>Matriculation code :</td>
                    <td>
                        <spring:bind path="command.matriculationCode">
                            <input type="text" name="${status.expression}" value="${status.value}">
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="employee.duplicated.code"/>
                        <div id="employee.null.code"/>
                    </td>
                </tr>
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
                <tr>
                    <td>Password :</td>
                    <td>
                        <spring:bind path="command.password">
                            <input type="password" name="${status.expression}" value="${status.value}">
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="employee.short.password"/>
                    </td>
                </tr>
                <tr>
                    <td>Select the employee office :</td>
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
                    <td colspan="2">
                        <div id="office.full"/>
                        <div id="office.not.found"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="button" value="Add" 
                               onclick="XT.doAjaxSubmit('validate', this);">
                    </td>
                    <td><div id="loading"></div></td>
                </tr>
            </table>
        </form>
    </body>
</html>