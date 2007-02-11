<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Error Page</title>
        <link href='<c:url value="/springxt.css"/>' rel="stylesheet" type="text/css">
    </head>
    <body>
        <div id="titleBar"></div>
        <h1>XT Framework</h1>
        <h3>There was an error while processing your request ...</h3>
        <p>
            Error message:
            <p>
                <c:if test="${exception != null}">
                      <c:out value="${exception.message}"/>
                </c:if>
                <c:if test="${param.exceptionMessage != null}">
                      <c:out value="${param.exceptionMessage}"/>
                </c:if>
            </p>
        </p>
    </body>
</html>
