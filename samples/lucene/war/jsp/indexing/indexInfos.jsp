<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<c:set var="ctx" value="${rc.contextPath}"/>

<b>Welcome to Spring Lucene indexing page.</b><br/><br/>

<b>Index informations</b><br/><br>
number of indexed documents: <c:out value="${infos.numDocs}"/>

<br/><br/>
-------------------------------------------------
<br/><br/>
<b>Index a text</b><br/><br/>
<form action="<c:out value="${ctx}"/>/addDocument.html" method="POST">
<table cellpadding="0" cellspacing="0" border="0">
  <tr><td>title: </td><td><input type="text" name="title"/></td></tr>
  <tr><td>text: </td><td><textarea name="text"></textarea></td></tr>
  <tr><td>category: </td><td><select name="category">
    <c:forEach var="category" items="${categories}">
      <option name="<c:out value="${category.name}"/>"><c:out value="${category.name}"/></option>
    </c:forEach>
  </td></tr>
</table>
<input type="submit" value="index"/>
</form>

<br/><br/>
-------------------------------------------------
<br/><br/>
<b>Index an uploaded file</b><br/><br/>
<form action="<c:out value="${ctx}"/>/addUploadedDocument.html" method="POST" enctype="multipart/form-data">
<table cellpadding="0" cellspacing="0" border="0">
  <tr><td>name: </td><td><input type="text" name="filename"/></td></tr>
  <tr><td>file: </td><td><input type="file" name="file" onChange="filename.value=file.value"/></td></tr>
  <tr><td>category: </td><td><select name="category">
    <c:forEach var="category" items="${categories}">
      <option name="<c:out value="${category.name}"/>"><c:out value="${category.name}"/></option>
    </c:forEach>
  </td></tr>
</table>
<input type="submit" value="index"/>
</form>