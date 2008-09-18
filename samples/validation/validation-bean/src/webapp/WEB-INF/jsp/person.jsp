<%@ page session="false" %>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<html>

    <head>

            <title>Person Data</title>

    </head>

    <body>

        <spring:bind path="person">
            <c:if test="${status.error}">
                <ul>
                    <c:forEach items="${status.errorMessages}" var="errorMessage">
                    <li><font color="red">${status.errorMessage}</font></li>
                    </c:forEach>
                </ul>
            </c:if>
        </spring:bind>

        <h3>Personal Data</h3>

        <form action="" method="post">

            <table>

                <tr>
                    <td><fmt:message key="person.first.name"/></td>
                    <td>
                        <spring:bind path="person.firstName">
                            <input type="text" name="firstName" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <font color="red"><c:out value="${status.errorMessage}"/></font>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="person.last.name"/></td>
                    <td>
                        <spring:bind path="person.lastName">
                            <input type="text" name="lastName" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <font color="red"><c:out value="${status.errorMessage}"/></font>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="person.email"/></td>
                    <td>
                        <spring:bind path="person.email">
                            <input type="text" name="email" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <font color="red"><c:out value="${status.errorMessage}"/></font>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="person.password"/></td>
                    <td>
                        <spring:bind path="person.password">
                            <input type="password" name="password" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <font color="red"><c:out value="${status.errorMessage}"/></font>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="person.verify.password"/></td>
                    <td>
                        <spring:bind path="person.verifyPassword">
                            <input type="password" name="verifyPassword" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <font color="red"><c:out value="${status.errorMessage}"/></font>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="person.lucky.number"/></td>
                    <td>
                        <spring:bind path="person.luckyNumber">
                            <input type="text" name="${status.expression}" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <font color="red"><c:out value="${status.errorMessage}"/></font>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="person.unlucky.number"/></td>
                    <td>
                        <spring:bind path="person.unluckyNumber">
                            <input type="text" name="${status.expression}" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <font color="red"><c:out value="${status.errorMessage}"/></font>
                        </spring:bind>
                    </td>
                </tr>
            </table>

            <br/>

            <input type="submit" value="<fmt:message key="form.submit"/>"/>

        </form>

    </body>

</html>
