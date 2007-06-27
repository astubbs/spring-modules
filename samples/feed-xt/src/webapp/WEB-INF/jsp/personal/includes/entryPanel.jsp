<%@page contentType="text/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div>
    <div class="entryHead">
        <c:if test="${!empty requestScope.entry.publishedDate}">
            <div>
                Date : <fmt:formatDate value="${requestScope.entry.publishedDate}"/>
            </div>
        </c:if>
        <c:if test="${!empty requestScope.entry.link}">
            <div><a href="${requestScope.entry.link}">Read the original post</a></div>
        </c:if>
    </div>
    <div class="entryContent">
        <c:forEach items="${requestScope.entry.contents}" var="content">
            <p>
                <c:choose>
                    <c:when test="${!empty content}">
                        ${content}
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="invalid.entry.content"/>
                    </c:otherwise>
                </c:choose>
            </p>
        </c:forEach>
    </div>
</div>