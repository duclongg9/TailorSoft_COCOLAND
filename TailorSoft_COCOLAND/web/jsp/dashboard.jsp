<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Trang chủ"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="row g-3">
        <div class="col-md-4">
            <div class="card text-bg-primary">
                <div class="card-body">
                    <h5 class="card-title">Khách hàng</h5>
                    <p class="card-text fs-3">${customerCount}</p>
                    <a href="<c:url value='/customers'/>" class="stretched-link text-white">Xem chi tiết</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-bg-success">
                <div class="card-body">
                    <h5 class="card-title">Đơn hàng</h5>
                    <p class="card-text fs-3">${orderCount}</p>
                    <a href="<c:url value='/orders'/>" class="stretched-link text-white">Xem chi tiết</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-bg-danger">
                <div class="card-body">
                    <h5 class="card-title">Vải sắp hết</h5>
                    <p class="card-text fs-3">${lowStockCount}</p>
                    <a href="<c:url value='/materials'/>" class="stretched-link text-white">Xem chi tiết</a>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>
