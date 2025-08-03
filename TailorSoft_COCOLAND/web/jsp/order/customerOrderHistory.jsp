<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Hồ sơ khách hàng"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <h2>Hồ sơ khách hàng</h2>
        <div class="mb-3">
            <h4>${customer.name}</h4>
            <p>${customer.phone} • ${customer.email}</p>
            <p>Địa chỉ: ${customer.address}</p>
            <p>Tổng đơn: ${totalOrders} | Đang may: ${pendingOrders} | Đã hoàn thành: ${completedOrders}</p>
        </div>
        <h5 class="mt-4">📃 Lịch sử đơn hàng</h5>
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>STT</th>
                <th>Ngày đặt</th>
                <th>Ngày giao</th>
                <th>Sản phẩm</th>
                <th>Trạng thái</th>
                <th>Tổng</th>
                <th>👁</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${orders}" varStatus="st">
                <tr>
                    <td>${st.count}</td>
                    <td><fmt:formatDate value="${o.orderDate}" pattern="yyyy-MM-dd"/></td>
                    <td><fmt:formatDate value="${o.deliveryDate}" pattern="yyyy-MM-dd"/></td>
                    <td>${o.productType}</td>
                    <td>${o.status}</td>
                    <td class="currency">${o.total}</td>
                    <td>
                        <a href="<c:url value='/orders/detail?id=${o.id}'/>" class="btn btn-sm btn-outline-secondary">
                            <i class="fa fa-eye"></i>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <jsp:include page="/jsp/common/pagination.jsp"/>
    </div>
</div>
<script>
    document.querySelectorAll('.currency').forEach(function(el){
        var num = parseFloat(el.textContent);
        el.textContent = new Intl.NumberFormat('vi-VN',{style:'currency',currency:'VND'}).format(num);
    });
</script>
<jsp:include page="/jsp/common/footer.jsp"/>
