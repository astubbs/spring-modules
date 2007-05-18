<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr">
<head>
  <title><fmt:message key="application.title"/></title>

  <link href="style.css" rel="stylesheet" type="text/css"/>
  <link href="<c:out value="${ctx}"/>/images/favicon.ico" rel="SHORTCUT ICON" />
</head>

<body bgcolor="#e5e5e5" style="margin: 8px 0px;">

	<table cellpadding="5" cellspacing="5" border="0">
		<tr>
			<td colspan="2" valign="center">
						<img src="<c:url value="/images/lucene-logo.gif"/>" border="0"/>
						&nbsp;&nbsp;<strong><tiles:getAsString name="title"/></strong></td>
		</tr>
		<tr>
			<td valign="top"><tiles:insert attribute="menu"/></td>
			<td valign="top"><tiles:insert attribute="body"/></td>
		</tr>
    </table>

</body>
</html>

