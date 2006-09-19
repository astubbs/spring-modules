<%@ page import="org.springframework.dao.DataAccessException"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<%
Exception ex = (Exception) request.getAttribute("exception");
%>

<H2>Data access failure: <%= ex.getMessage() %></H2>
<P>


<%
ex.printStackTrace(new java.io.PrintWriter(out));
%>

<P>
<BR>
<A href="<c:url value="/welcome.htm"/>">Home</A>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
