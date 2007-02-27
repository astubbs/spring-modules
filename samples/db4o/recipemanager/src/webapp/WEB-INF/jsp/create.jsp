<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<h2>Create a new recipe</h2>
<form method="post">
  <table width="95%" bgcolor="f8f8ff" border="0" cellspacing="0" cellpadding="5">
    <tr>
      <td alignment="right" width="20%">Title</td>
      <spring:bind path="recipe.title">
        <td width="20%">
          <input type="text" name="title" value="<c:out value="${status.value}"/>">
        </td>
        <td width="60%">
          <font color="red"><c:out value="${status.errorMessage}"/></font>
        </td>
      </spring:bind>
    </tr>
  </table>
  <br>

  <spring:hasBindErrors name="user">
    <b>Please correct the following errors</b><br>
    <c:forEach items="${errors.globalErrors}" var="error">
       <font color="red">
       	<spring:message code="${error.code}" />
       </font>
     </c:forEach>
  </spring:hasBindErrors>

  <br><br>
  <input type="submit" alignment="center" value="Save">
</form>