<%@ page session="false" %>

<%@ include file="/WEB-INF/jsp/taglibs.jsp" %>

<c:set var="ctx" value="${rc.contextPath}"/>

<html>

<script type="text/javascript" src="<c:url value="/validator.js"/>"></script>

<validator:javascript formName="personData" staticJavascript="false" xhtml="true" cdata="false"/>

<body>

    <h3>Contact Informations</h3>

    <form action="personWizard.html?_target1" method="post" onsubmit="return validatePersonData(this);">

        <input type="hidden" name="_page" value="0" />

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

        </table>

        <br/>

        <input type="submit" value="<fmt:message key="wizard.next"/>"/>

    </form>

</body>

</html>
