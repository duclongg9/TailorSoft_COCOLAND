<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav id="sidebar" class="bg-dark text-white p-3">
    <h4 class="text-white">TailorSoft</h4>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item"><a href="<c:url value='/dashboard'/>" class="nav-link text-white">Dashboard</a></li>
        <li class="nav-item"><a href="<c:url value='/customers'/>" class="nav-link text-white">Khách hàng</a></li>
        <li class="nav-item"><a href="<c:url value='/materials'/>" class="nav-link text-white">Kho vải</a></li>
        <li class="nav-item"><a href="<c:url value='/orders'/>" class="nav-link text-white">Đơn hàng</a></li>
        <li class="nav-item"><a href="<c:url value='/measurement-types'/>" class="nav-link text-white">Loại số đo</a></li>
        <li class="nav-item"><a href="<c:url value='/product-types'/>" class="nav-link text-white">Loại sản phẩm</a></li>
    </ul>
</nav>
<main id="mainContent" class="flex-grow-1 p-4">
