<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Danh sách đơn hàng"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <c:if test="${not empty msg}">
            <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
                <div id="toastMessage" class="toast align-items-center text-bg-success" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="d-flex">
                        <div class="toast-body">
                            <c:choose>
                                <c:when test="${msg == 'created'}">Thêm đơn hàng thành công</c:when>
                                <c:otherwise>Thao tác thành công</c:otherwise>
                            </c:choose>
                        </div>
                        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                    </div>
                </div>
            </div>
        </c:if>
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Danh sách đơn hàng</h2>
            <a href="<c:url value='/orders/create'/>" class="btn btn-primary"><i class="fa fa-plus"></i> Thêm đơn</a>
        </div>
        <div class="row g-2 mb-3">
            <div class="col-md-3"><input id="orderSearch" type="text" class="form-control" placeholder="Tìm kiếm..."></div>
            <div class="col-md-3">
                <select id="statusFilter" class="form-select">
                    <option value="">Tất cả trạng thái</option>
                    <option value="Dang may">Đang may</option>
                    <option value="Hoan thanh">Hoàn thành</option>
                </select>
            </div>
            <div class="col-md-2"><input id="monthFilter" type="month" class="form-control"/></div>
            <div class="col-md-4">
                <select id="customerFilter" class="form-select">
                    <option value="">Tất cả khách</option>
                    <c:forEach var="c" items="${customers}">
                        <option value="${c.id}">${c.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <table id="orderTable" class="table table-striped table-hover">
            <thead>
            <tr>
                <th class="d-none">Mã</th>
                <th>Khách hàng</th>
                <th>Ngày đặt</th>
                <th>Ngày giao</th>
                <th>Trạng thái</th>
                <th class="text-end">Tổng tiền</th>
                <th class="text-end">Đã cọc</th>
                <th class="text-end">Còn lại</th>
                <th class="text-center">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${orders}">
                <c:set var="orderMonth"><fmt:formatDate value="${o.orderDate}" pattern="yyyy-MM"/></c:set>
                <tr data-status="${o.status}" data-order-date="${orderMonth}" data-customer-id="${o.customerId}">
                    <td class="d-none">${o.id}</td>
                    <td><span data-bs-toggle="tooltip" title="${o.customerPhone} / ${o.customerEmail}">${o.customerName}</span></td>
                    <td><fmt:formatDate value="${o.orderDate}" pattern="dd-MM-yyyy"/></td>
                    <td><fmt:formatDate value="${o.deliveryDate}" pattern="dd-MM-yyyy"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${o.status == 'Hoan thanh'}"><span class="badge text-bg-success">Hoàn thành</span></c:when>
                            <c:otherwise><span class="badge text-bg-info">Đang may</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-end"><fmt:formatNumber value="${o.total}" type="number" groupingUsed="true"/> ₫</td>
                    <td class="text-end"><fmt:formatNumber value="${o.deposit}" type="number" groupingUsed="true"/> ₫</td>
                    <td class="text-end"><fmt:formatNumber value="${o.total - o.deposit}" type="number" groupingUsed="true"/> ₫</td>
                    <td class="text-center">
                        <a href="<c:url value='/orders/detail?id=${o.id}'/>" class="btn btn-sm btn-outline-secondary me-1" title="Chi tiết"><i class="fa fa-eye"></i></a>
                        <a href="#" class="btn btn-sm btn-outline-primary me-1" title="Sửa"><i class="fa fa-pen"></i></a>
                        <a href="#" class="btn btn-sm btn-outline-success" title="In"><i class="fa fa-print"></i></a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <jsp:include page="/jsp/common/pagination.jsp"/>
        <script>
            function applyFilters() {
                const search = document.getElementById('orderSearch').value.toLowerCase();
                const status = document.getElementById('statusFilter').value;
                const month = document.getElementById('monthFilter').value;
                const customer = document.getElementById('customerFilter').value;
                document.querySelectorAll('#orderTable tbody tr').forEach(row => {
                    const text = row.textContent.toLowerCase();
                    const rowStatus = row.getAttribute('data-status');
                    const rowMonth = row.getAttribute('data-order-date');
                    const rowCustomer = row.getAttribute('data-customer-id');
                    let show = text.indexOf(search) > -1;
                    if (status && rowStatus !== status) show = false;
                    if (month && rowMonth !== month) show = false;
                    if (customer && rowCustomer !== customer) show = false;
                    row.style.display = show ? '' : 'none';
                });
            }
            document.getElementById('orderSearch').addEventListener('keyup', applyFilters);
            document.getElementById('statusFilter').addEventListener('change', applyFilters);
            document.getElementById('monthFilter').addEventListener('change', applyFilters);
            document.getElementById('customerFilter').addEventListener('change', applyFilters);
            $('#customerFilter').select2({placeholder: 'Khách hàng', width:'100%'});
            const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            tooltipTriggerList.map(el => new bootstrap.Tooltip(el));
        </script>
        <c:if test="${not empty msg}">
            <script>
                window.addEventListener('load', function () {
                    const toast = new bootstrap.Toast(document.getElementById('toastMessage'));
                    toast.show();
                });
            </script>
        </c:if>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>
