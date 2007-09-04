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
        <title>XT Ajax Framework : Example 8</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Ajax Framework</h1>
        <h3 align="center">Upload files within Ajax forms ...</h3>
        <p>
            This example uploads a file within an Ajax-enabled form ...
        </p>
        <form method="POST" enctype="multipart/form-data">
            <table>
                <tr>
                    <td>Upload directory :</td>
                    <td>
                        <spring:bind path="command.uploadDir">
                            <input type="text" name="${status.expression}" value="${status.value}">
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="no.dir.code"/>
                    </td>
                </tr>
                <tr>
                    <td>File to upload :</td>
                    <td>
                        <spring:bind path="command.file">
                            <input type="file" name="${status.expression}" value="${status.value}">
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="no.file.code"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="button" value="Upload" 
                               onclick="XT.doAjaxSubmit('validate', this, null, {'enableUpload' : true});">
                    </td>
                    <td><div id="loading"></div></td>
                </tr>
            </table>
        </form>
    </body>
</html>