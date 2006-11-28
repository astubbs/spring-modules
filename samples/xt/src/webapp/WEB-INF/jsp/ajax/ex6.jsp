<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <script type="text/javascript" src='../js/springxt-min.js'></script>
        <script type="text/javascript" src='../js/prototype.js'></script>
        <script type="text/javascript" src='../js/scriptaculous.js?load=effects,dragdrop'></script>
        <script type="text/javascript">
            function onDropEmployee(employee, office) {
                XT.doAjaxAction("dragEmployee", null, {employeeId : employee.id});
            }
        </script>
        <title>XT Ajax Framework : Example 6</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Ajax Framework</h1>
        <h3 align="center">Fill an office using Scriptaculous draggables ...</h3>
        <p>
            Fill an office thanks to Scriptaculous integration ...
        </p>
        <form method="POST" action="">
            <table>
            <tr>
                <td>Drag employees from this list ...</td>
            </tr>
            <tr>
                <td>
                    <ul>
                        <c:forEach items="${employees}" var="emp">
                            <li id="${emp.matriculationCode}">${emp.firstname}&nbsp;${emp.surname}</li>
                            <script type="text/javascript">new Draggable("${emp.matriculationCode}", {revert : true});</script>
                        </c:forEach>
                    </ul>
                </td>
            </tr>
            <tr>
                <td>To this office ...</td>
            </tr>
            <tr>
                <td>
                    <ul id="office">
                        <li>${command.name}
                            <spring:bind path="command.employees">
                                <ul id="employees">
                                    <c:forEach items="${command.employees}" var="emp">
                                        <li>
                                            ${emp.firstname}&nbsp;${emp.surname}
                                            <input type="hidden" name="${status.expression}" value="${emp.matriculationCode}">
                                        </li>
                                    </c:forEach>
                                </ul>
                            </spring:bind>
                        </li>
                        <script type="text/javascript">
                            Droppables.add("office", {onDrop : function(draggable, droppable) { onDropEmployee(draggable, droppable); } });
                        </script>
                    </ul>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="office.full">
                    </div>
                </td>
            </tr>
            <tr id="success" style="display: none;"></tr>
            <tr id="buttons">
                <td><input type="button" value="Submit" onclick="XT.doAjaxSubmit('validate', this);"></td>
            </tr>
        </form>
    </body>
</html>
