<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Danh sách vải"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
<c:if test="${not empty msg}">
    <div class="position-fixed top-0 end-0 p-3" style="z-index: 11">
        <div id="toastMessage" class="toast align-items-center text-bg-success" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">Thêm vải thành công</div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </div>
</c:if>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>Danh sách vải</h2>
    <a href="<c:url value='/materials/create'/>" class="btn btn-primary">Thêm vải</a>
</div>
<input id="materialSearch" type="text" class="form-control mb-3" placeholder="Tìm kiếm..."/>
<c:set var="lowStockThreshold" value="10"/>
<table id="materialTable" class="table table-striped table-hover">
    <thead class="table-dark">
    <tr>
        <th>Mã</th>
        <th>Tên vải</th>
        <th>Màu sắc</th>
        <th>Xuất xứ</th>
        <th class="text-end">Giá (₫)</th>
        <th class="text-end">Số lượng (m)</th>
        <th>Hóa đơn</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="m" items="${materials}">
        <tr class="${m.quantity < lowStockThreshold ? 'table-danger' : ''}">
            <td>${m.id}</td>
            <td>${m.name}</td>
            <td class="text-truncate" style="max-width:150px;">${m.color}</td>
            <td>${m.origin}</td>
            <td class="text-end"><fmt:formatNumber value="${m.price}" type="number" pattern="#,##0"/> ₫</td>
            <td class="text-end"><fmt:formatNumber value="${m.quantity}" type="number" pattern="#,##0.##"/> m</td>
            <td>
                <c:if test="${not empty m.invoiceImage}">
                    <img src="<c:url value='/uploads/${m.invoiceImage}'/>" alt="Hóa đơn" style="height:40px;" class="me-2">
                    <button type="button" class="btn btn-sm btn-secondary" onclick="showInvoice('<c:url value='/uploads/${m.invoiceImage}'/>')">Xem ảnh</button>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<jsp:include page="/jsp/common/pagination.jsp"/>
<script>
    document.getElementById('materialSearch').addEventListener('keyup', function () {
        const filter = this.value.toLowerCase();
        document.querySelectorAll('#materialTable tbody tr').forEach(function (row) {
            const text = row.textContent.toLowerCase();
            row.style.display = text.indexOf(filter) > -1 ? '' : 'none';
        });
    });
</script>
<div class="modal fade" id="invoiceModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Hóa đơn</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center">
                <img id="invoiceModalImage" src="" alt="Hóa đơn" class="img-fluid"/>
            </div>
        </div>
    </div>
</div>
<script>
    function showInvoice(src) {
        document.getElementById('invoiceModalImage').src = src;
        const modal = new bootstrap.Modal(document.getElementById('invoiceModal'));
        modal.show();
    }
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
