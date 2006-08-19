<%@ page session="false" %>
<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<html>

    <head>

            <title>Person Data</title>

            <style type="text/css">

                .error {
                    color: red;
                }

                .error li{
                    list-style: circle;
                }

            </style>

            <valang:codebase includeScriptTags="true" fieldErrorsIdSuffix="_error" globalErrorsId="global_error"/>

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

            <div id="global_error" class="error">
                <!-- will be filled with global errors -->
            </div>

            <table>

                <tr>
                    <td><fmt:message key="person.first.name"/></td>
                    <td>
                        <spring:bind path="person.firstName">
                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <span id="<c:out value="${status.expression}"/>_error" class="error"><c:out value="${status.errorMessage}"/></span>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="person.last.name"/></td>
                    <td>
                        <spring:bind path="person.lastName">
                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <span id="<c:out value="${status.expression}"/>_error" class="error"><c:out value="${status.errorMessage}"/></span>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="person.email"/></td>
                    <td>
                        <spring:bind path="person.email">
                            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <span id="<c:out value="${status.expression}"/>_error" class="error"><c:out value="${status.errorMessage}"/></span>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="person.password"/></td>
                    <td>
                        <spring:bind path="person.password">
                            <input type="password" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <span id="<c:out value="${status.expression}"/>_error" class="error"><c:out value="${status.errorMessage}"/></span>
                        </spring:bind>
                    </td>
                </tr>
                <tr>
                    <td><fmt:message key="person.verify.password"/></td>
                    <td>
                        <spring:bind path="person.verifyPassword">
                            <input type="password" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" size="15" maxlength="60"/>
                            <span id="<c:out value="${status.expression}"/>_error" class="error"><c:out value="${status.errorMessage}"/></span>
                        </spring:bind>
                    </td>
                </tr>

            </table>

            <br/>

            <input type="submit" value="<fmt:message key="form.submit"/>"/>

        </form>

    </body>

</html>
