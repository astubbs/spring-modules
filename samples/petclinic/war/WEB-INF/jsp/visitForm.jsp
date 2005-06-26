<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<P>
<H2><c:if test="${command.new}">New </c:if>Visit:</H2>
<spring:bind path="command">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B><BR>
  </FONT>
</spring:bind>
<P>
<B>Pet:</B>
<TABLE border="true">
  <TH>Name</TH><TH>Birth Date</TH><TH>Type</TH><TH>Owner</TH>
  <TR>
    <TD><c:out value="${command.pet.name}"/></TD>
    <TD><fmt:formatDate value="${command.pet.birthDate}" pattern="yyyy-MM-dd"/></TD>
    <TD><c:out value="${command.pet.type.name}"/></TD>
    <TD><c:out value="${command.pet.owner.firstName}"/> <c:out value="${command.pet.owner.lastName}"/></TD>
  </TR>
</TABLE>
<P>
<FORM method="POST">
  <B>Date:</B>
  <spring:bind path="command.date">
    <FONT color="red">
      <B><c:out value="${status.errorMessage}"/></B>
    </FONT>
    <BR><INPUT type="text" maxlength="10" size="10" name="date" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <BR>(yyyy-mm-dd)
  <P>
  <B>Description:</B>
  <spring:bind path="command.description">
    <FONT color="red">
      <B><c:out value="${status.errorMessage}"/></B>
    </FONT>
    <BR><TEXTAREA rows="10" cols="25" name="description"><c:out value="${status.value}"/></TEXTAREA>
  </spring:bind>
  <P>
  <INPUT type="hidden" name="petId" value="<c:out value="${command.pet.id}"/>"/>
  <INPUT type = "submit" value="Add Visit"  />
</FORM>
<P>
<BR>
<B>Previous Visits:</B>
<TABLE border="true">
  <TH>Date</TH><TH>Description</TH>
  <c:forEach var="visit" items="${command.pet.visits}">
		<c:if test="${!visit.new}">
    <TR>
      <TD><fmt:formatDate value="${visit.date}" pattern="yyyy-MM-dd"/></TD>
      <TD><c:out value="${visit.description}"/></TD>
    </TR>
		</c:if>
  </c:forEach>
</TABLE>
<P>
<BR>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
