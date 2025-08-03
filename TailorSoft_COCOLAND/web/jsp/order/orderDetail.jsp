<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Chi tiết đơn hàng"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <h2>Chi tiết đơn hàng</h2>
        <c:if test="${order != null}">
            <div class="card mb-4">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Khách hàng:</strong> ${order.customerName}</p>
                            <p><strong>Ngày đặt:</strong> <fmt:formatDate value="${order.orderDate}" pattern="dd-MM-yyyy"/></p>
                            <p><strong>Ngày giao:</strong> <fmt:formatDate value="${order.deliveryDate}" pattern="dd-MM-yyyy"/></p>
                            <p><strong>Trạng thái:</strong> ${order.status}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${order.total}" type="number" groupingUsed="true"/> ₫</p>
                            <p><strong>Đã cọc:</strong> <fmt:formatNumber value="${order.deposit}" type="number" groupingUsed="true"/> ₫</p>
                            <p><strong>Còn lại:</strong> <fmt:formatNumber value="${order.total - order.deposit}" type="number" groupingUsed="true"/> ₫</p>
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${not empty details}">
                <table class="table table-striped mb-4">
                    <thead>
                    <tr>
                        <th>Sản phẩm</th>
                        <th>Tên vải</th>
                        <th>Đơn giá</th>
                        <th>Số lượng</th>
                        <th>Ghi chú</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="d" items="${details}">
                        <tr>
                            <td>${d.productType}</td>
                            <td>${d.materialName}</td>
                            <td><fmt:formatNumber value="${d.unitPrice}" type="number" groupingUsed="true"/> ₫</td>
                            <td>${d.quantity}</td>
                            <td>${d.note}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <div class="d-flex gap-2 mb-3">
                <a href="#" class="btn btn-outline-primary btn-sm"><i class="fa fa-pen"></i> Cập nhật trạng thái</a>
                <a href="#" class="btn btn-outline-success btn-sm"><i class="fa fa-coins"></i> Thêm thanh toán</a>
                <a href="#" class="btn btn-outline-secondary btn-sm"><i class="fa fa-print"></i> In phiếu</a>
            </div>
        </c:if>
        <a href="<c:url value='/orders'/>">Quay lại</a>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>
