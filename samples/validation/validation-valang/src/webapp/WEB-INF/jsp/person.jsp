<%@ page session="false" %>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<html>

    <head>

            <title>Person Data</title>

            <valang:codebase includeScriptTags="true"/>

    </head>

    <body>

        <h3>Personal Data</h3>

        <spring:bind path="person.*">
            <font color="red">
                <b><c:out value="${status.value}"/></b>
                <b><c:out value="${status.errorMessage}"/></b>
            </font>
        </spring:bind>


        <form action="" method="post">

            <valang:validate commandName="person"/>

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

            </table>

            <br/>

            <input type="submit" value="<fmt:message key="form.submit"/>"/>

        </form>

        <div id="valangLogDiv"></div>

    </body>

</html>
