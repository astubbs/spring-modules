<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>XT Framework Samples Start Page</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Framework</h1>
        <h2>Choose an example</h2>
        <h3>XT Modeling Framework</h3>
        <ul>
            <li><a href='<c:url value="/model/ex1.action"/>'/>Example 1</a></li>
            <li><a href='<c:url value="/model/ex2.action?officeId=1"/>'/>Example 2, Part 1</a></li>
            <li><a href='<c:url value="/model/ex2.action?officeId=2"/>'/>Example 2, Part 2</a></li>
            <li><a href='<c:url value="/model/ex3.action"/>'/>Example 3</a></li>
        </ul>
        <h3>XT Ajax Framework</h3>
        <ul>
            <li><a href='<c:url value="/ajax/ex1.page?msg=Hello"/>'/>Example 1</a></li>
            <li><a href='<c:url value="/ajax/ex2.page"/>'/>Example 2</a></li>
            <li><a href='<c:url value="/ajax/ex3.action"/>'/>Example 3</a></li>
            <li><a href='<c:url value="/ajax/ex4.action"/>'/>Example 4</a></li>
            <li><a href='<c:url value="/ajax/ex5.action"/>'/>Example 5</a></li>
            <li><a href='<c:url value="/ajax/ex6.action?officeId=1"/>'/>Example 6, Part 1</a></li>
            <li><a href='<c:url value="/ajax/ex6.action?officeId=2"/>'/>Example 6, Part 2</a></li>
            <li><a href='<c:url value="/ajax/ex7.action"/>'/>Example 7</a></li>
            <li><a href='<c:url value="/ajax/ex8.action"/>'/>Example 8</a></li>
        </ul>
    </body>
</html>