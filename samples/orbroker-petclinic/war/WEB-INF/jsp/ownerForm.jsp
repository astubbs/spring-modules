<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<P>
<H2><c:if test="${owner.new}">New </c:if>Owner:</H2>
<spring:bind path="owner">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
</spring:bind>
<P>
<FORM method="POST">
  <spring:nestedPath path="owner">
    <jsp:include page="/WEB-INF/jsp/fields/firstName.jsp"/>
    <jsp:include page="/WEB-INF/jsp/fields/lastName.jsp"/>
    <jsp:include page="/WEB-INF/jsp/fields/address.jsp"/>
    <jsp:include page="/WEB-INF/jsp/fields/city.jsp"/>
    <jsp:include page="/WEB-INF/jsp/fields/telephone.jsp"/>
  </spring:nestedPath>
  <c:if test="${owner.new}">
    <INPUT type = "submit" value="Add Owner"  />
  </c:if>
  <c:if test="${!owner.new}">
    <INPUT type = "submit" value="Update Owner"  />
  </c:if>
</FORM>
<P>
<BR>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
