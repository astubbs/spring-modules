<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Spring Modules XT Examples Start Page</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>Spring Modules XT Framework</h1>
        <h2>Choose an example</h2>
        <h3>Spring Modules XT Domain Development Framework</h3>
        <ul>
            <li><a href='<c:url value="/model/ex1.action"/>'/>Example 1</a></li>
            <li><a href='<c:url value="/model/ex2.action?office=1"/>'/>Example 2, Part 1</a></li>
            <li><a href='<c:url value="/model/ex2.action?office=2"/>'/>Example 2, Part 2</a></li>
            <li><a href='<c:url value="/model/ex3.action"/>'/>Example 3</a></li>
        </ul>
        <h3>Spring Modules XT Ajax Framework</h3>
        <ul>
            <li><a href='<c:url value="/ajax/ex1.page"/>'/>Example 1</a></li>
            <li><a href='<c:url value="/ajax/ex2.page"/>'/>Example 2</a></li>
            <li><a href='<c:url value="/ajax/ex3.action"/>'/>Example 3</a></li>
            <li><a href='<c:url value="/ajax/ex4.action"/>'/>Example 4</a></li>
        </ul>
    </body>
</html>