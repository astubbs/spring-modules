<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <script type="text/javascript" src='<c:url value="/js/springxt.js"/>'></script>  
        <title>XT Ajax Framework : Example 2</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Ajax Framework</h1>
        <h3 align="center">Choose some Ajax enabled offices and employees ...</h3>
        <p>
            This example uses XT Ajax Framework for ajax-enabling a simple page with no Spring MVC controller.
        </p>
        <form name="" action="">
            <table>
                <tr>
                    <td>Press to load Offices :</td>
                    <td><input type="button" value="Press" onclick="doAjaxAction('loadOffices', this);"></td>
                </tr>
                <tr>
                    <td>Select an office :</td>
                    <td>
                        <select id="offices" name="officeId" onchange="doAjaxAction('officeSelection', this);">
                            <option>--- ---</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Select an employee :</td>
                    <td>
                        <select id="employees" name="matriculationCode" onchange="doAjaxAction('employeeSelection', this);">
                            <option>--- ---</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Employee firstname :</td>
                    <td>
                        <input id="firstname" type="text" name="firstname" value="">
                    </td>
                </tr>
                <tr>
                    <td>Employee surname :</td>
                    <td>
                        <input id="surname" type="text" name="surname" value="">
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>