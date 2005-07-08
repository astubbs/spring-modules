<%@ page session="false"%>

<%@ include file="/jsp/layout/taglibs.jsp"%>

<c:set var="ctx" value="${rc.contextPath}"/>

<b>Welcome to Spring Lucene sample application.</b><br/><br/>
- To do make a search on the index, click <a href="<c:out value="${ctx}"/>/search.html">here</a>.<br/>
- To index documents or text, click <a href="<c:out value="${ctx}"/>/index.html">here</a>.<br/>
- To manage the categories, click <a href="<c:out value="${ctx}"/>/categories.html">here</a>.