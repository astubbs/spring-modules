<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<h2>Recipes:</h2>
<table border="1">
  <th>Name</th><th>Category</th>
  <c:forEach var="recipe" items="${recipes}">
    <tr>
      <td><c:out value="${recipe.title}"/></td>
      <td><c:out value="${recipe.category}"/></td>
    </tr>
  </c:forEach>
</table>