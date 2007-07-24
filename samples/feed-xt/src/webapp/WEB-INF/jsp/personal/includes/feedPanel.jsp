<%@page contentType="text/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="feedBody">
    <div class="feedHead">
        <h2>${requestScope.feed.title}</h2>
        <c:if test="${!empty requestScope.feed.author}">
            <div>Author : ${requestScope.feed.author}</div>
        </c:if>
        <c:if test="${!empty requestScope.feed.publishedDate}">
            <div>
                Date : <fmt:formatDate value="${requestScope.feed.publishedDate}"/>
            </div>
        </c:if>
    </div>
    <div class="feedContent">
        <c:forEach items="${requestScope.feed.entries}" var="entry" varStatus="counter">
            <div>
                <a href="" id="entry.${counter.index}" class="closed" subscription="${requestScope.subscription}" entryIndex="${counter.index}">
                    ${entry.title}
                </a>
                <br/>
                <div id="loading.entry.${counter.index}"></div>
                <div class="entryBody"></div>
            </div>
        </c:forEach>
    </div>
</div>
