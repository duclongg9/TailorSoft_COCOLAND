<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="extraParams" value="" />
<c:forEach var="entry" items="${paramValues}">
    <c:if test="${entry.key ne 'page'}">
        <c:forEach var="val" items="${entry.value}">
            <c:set var="extraParams" value="${extraParams}&${entry.key}=${val}" />
        </c:forEach>
    </c:if>
</c:forEach>
<c:if test="${totalPages > 1}">
<nav aria-label="Page navigation">
    <ul class="pagination">
        <c:forEach begin="1" end="${totalPages}" var="i">
            <li class="page-item ${i == currentPage ? 'active' : ''}">
                <a class="page-link" href="?page=${i}${extraParams}">${i}</a>
            </li>
        </c:forEach>
    </ul>
</nav>
</c:if>
