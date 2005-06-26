<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<P>
<H2><c:if test="${command.new}">New </c:if>Pet</H2>
<spring:bind path="command">
  <FONT color="red">
    <B><c:out value="${status.errorMessage}"/></B>
  </FONT>
</spring:bind>
<P>
<B>Owner:</B> <c:out value="${command.owner.firstName}"/> <c:out value="${command.owner.lastName}"/>
<P>
<FORM method="POST">
  <B>Name:</B>
  <spring:bind path="command.name">
    <FONT color="red">
      <B><c:out value="${status.errorMessage}"/></B>
    </FONT>
    <BR><INPUT type="text" maxlength="30" size="30" name="name" value="<c:out value="${status.value}"/>" >
  </spring:bind>
  <P>
  <B>Birth Date:</B>
  <spring:bind path="command.birthDate">
    <FONT color="red">
      <B><c:out value="${status.errorMessage}"/></B>
    </FONT>
    <BR><INPUT type="text" maxlength="10" size="10" name="birthDate" value="<c:out value="${status.value}"/>" />
  </spring:bind>
  <BR>(yyyy-mm-dd)
  <P>
  <B>Type:</B>
  <spring:bind path="command.type">
    <FONT color="red">
      <B><c:out value="${status.errorMessage}"/></B>
    </FONT>
    <BR>
    <SELECT name="typeId">
      <c:forEach var="type" items="${types}">
        <c:if test="${command.type.id == type.id}">
          <OPTION selected="<c:out value="${command.type.id}"/>" value="<c:out value="${type.id}"/>"><c:out value="${type.name}"/></OPTION>
        </c:if>
        <c:if test="${command.type.id != type.id}">
          <OPTION value="<c:out value="${type.id}"/>"><c:out value="${type.name}"/></OPTION>
        </c:if>
      </c:forEach>
    </SELECT>
  </spring:bind>
  <P>
  <c:if test="${command.new}">
    <INPUT type = "submit" value="Add Pet"/>
  </c:if>
  <c:if test="${!command.new}">
    <INPUT type = "submit" value="Update Pet"/>
  </c:if>
</FORM>
<P>
<BR>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
