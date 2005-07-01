<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page import="java.util.Enumeration"%>

<%@ include file="/jsp/layout/taglibs.jsp"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr">
<head>
  <title><fmt:message key="application.title"/></title>

  <c:set var="ctx" value="${rc.contextPath}" />
  <link href="style.css" rel="stylesheet" type="text/css"/>
  <!-- <c:out value="${ctx}"/> -->
  <link href="<c:out value="${ctx}"/>/images/favicon.ico" rel="SHORTCUT ICON" />
</head>

<body bgcolor="#e5e5e5" style="margin: 8px 0px;">

	<table cellpadding="5" cellspacing="5" border="0">
		<tr>
			<td colspan="2"><tiles:insert attribute="title"/></td>
		</tr>
		<tr>
			<td valign="top"><tiles:insert attribute="menu"/></td>
			<td valign="top"><tiles:insert attribute="body"/></td>
		</tr>
    </table>

</body>
</html>

