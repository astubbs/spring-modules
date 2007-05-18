<%@ page session="false"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>

<b>Welcome to Spring Lucene sample application.</b><br/><br/>
- To do make a search on the index, click <a href="<c:url value="/search.html"/>">here</a>.<br/>
- To index documents or text, click <a href="<c:url value="/index.html"/>">here</a>.<br/>
- To manage the categories, click <a href="<c:url value="/categories.html"/>">here</a>.