<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <script type="text/javascript" src='../js/springxt.js'></script>
        <script type="text/javascript" src='../js/custom.js'></script>
        <title>XT Ajax Framework : Example 7</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Ajax Framework</h1>
        <h3 align="center">Take a look at XT Ajax exception mapping ...</h3>
        <p>
            This example shows how XT Ajax Framework can map exceptions originated
            during Ajax request processing to proper resolvers that will take care of
            making some given actions.
        </p>
        <form method="POST" action="">
            <table>
                <tr>
                    <td><input type="button" name="b1" value="Raise exception during Ajax request handling in controller" onclick="XT.doAjaxSubmit('raiseException', this);"></td>
                </tr>
                <tr>
                    <td><input type="button" name="b2" value="Raise exception during Ajax request processing" onclick="XT.doAjaxSubmit('raiseException', this);"></td>
                </tr>
                <tr>
                    <td><input type="button" name="b3" value="Raise exception after Ajax request processing because of no ajax-redirect prefix" onclick="XT.doAjaxSubmit('returnNoResponse', this);"></td>
                </tr>
                <tr>
                    <td><div id="loading"></div></td>
                </tr>
            </table>
        </form>
    </body>
</html>