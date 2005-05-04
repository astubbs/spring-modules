<%@ page session="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>

<body>

<b>Index informations</b><br/><br>
<li>has deletions: <c:out value="${infos.hasDeletions}"/></li>
<li>number of indexed documents: <c:out value="${infos.numDocs}"/></li>

<br/><br/>
<b>Index a text</b><br/><br/>
<form action="/spring-lucene/lucene/addDocument" method="POST">
title: <input type="text" name="title"/><br/>
text: <textarea name="text"></textarea><br/>
<input type="submit" value="index"/>
</form>

<br/><br/>
<b>Index an uploaded file</b><br/><br/>
<form action="/spring-lucene/lucene/addUploadedDocument" method="POST" enctype="multipart/form-data">
name: <input type="text" name="filename"/><br/>
file: <input type="file" name="file" onChange="filename.value=file.value"/><br/>
<input type="submit" value="index"/>
</form>

</body>

</html>