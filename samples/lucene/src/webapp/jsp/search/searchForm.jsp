<%@ page session="false"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>


<b>Welcome to Spring Lucene search page.</b><br/><br/>

<br/><br/>
-------------------------------------------------
<br/><br/>
<b>Search on the content.</b><br/><br/>
Please enter your search query:<br/>
<form action="<c:url value="/search.html"/>" method="POST">
	<input type="hidden" name="fieldName" value="contents"/>
	<input type="text" name="string" value=""/>
	<input type="submit" value="search"/>
</form>

<br/><br/>
-------------------------------------------------
<br/><br/>
<b>Documents for a category.</b><br/><br/>
Please select your category:<br/>
<form action="<c:url value="/search.html"/>" method="POST">
	<input type="hidden" name="fieldName" value="category"/>
	<input type="text" name="string" value=""/>
	<input type="submit" value="search"/>
</form>