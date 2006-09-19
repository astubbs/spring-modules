<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<P>
<H2>Find Owners:</H2>
<spring:bind path="owner">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
</spring:bind>
<P>
<FORM method="POST">
  <spring:nestedPath path="owner">
    <jsp:include page="/WEB-INF/jsp/fields/lastName.jsp"/>
  </spring:nestedPath>
  <INPUT type = "submit" value="Find Owners"  />
</FORM>
<P>
<BR>
<A href="<c:url value="/addOwner.htm"/>">Add Owner</A>
<P>
<BR>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
