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
                        <th></th>
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
                            <td>
                                <div class="btn-group btn-group-sm">
                                    <button type="button" class="btn btn-outline-secondary view-detail" data-id="${d.id}" data-qty="${d.quantity}" data-note="${d.note}"><i class="fa fa-eye"></i></button>
                                    <button type="button" class="btn btn-outline-primary edit-detail" data-id="${d.id}" data-qty="${d.quantity}" data-note="${d.note}"><i class="fa fa-pen"></i></button>
                                </div>
                            </td>
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

<div class="modal fade" id="editDetailModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form id="editDetailForm" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Cập nhật chi tiết</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="detailId" id="detailId">
                <div class="mb-3">
                    <label class="form-label">Số lượng</label>
                    <input type="number" min="1" class="form-control" name="quantity" id="quantity">
                </div>
                <div class="mb-3">
                    <label class="form-label">Ghi chú</label>
                    <textarea class="form-control" name="note" id="note" rows="2"></textarea>
                </div>
                <div id="measurementList"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-primary" id="saveBtn">Lưu</button>
            </div>
        </form>
    </div>
</div>

<script>
    $(function () {
        const baseUrl = '${pageContext.request.contextPath}';
        const modalEl = document.getElementById('editDetailModal');
        const modal = new bootstrap.Modal(modalEl);

        function loadMeasurements(id, disabled) {
            $('#measurementList').empty();
            $.getJSON(baseUrl + '/order-details/measurements', {id: id}, function (list) {
                list.forEach(function (m) {
                    const item = `<div class="mb-3">
                            <label class="form-label">${m.name} (${m.unit})</label>
                            <input type="number" step="0.01" class="form-control" name="m_${m.id}" value="${m.value}" ${disabled ? 'disabled' : ''}>
                        </div>`;
                    $('#measurementList').append(item);
                });
                modal.show();
            });
        }

        $('.view-detail').on('click', function () {
            const id = $(this).data('id');
            const qty = $(this).data('qty');
            const note = $(this).data('note');
            $('#detailId').val(id);
            $('#quantity').val(qty).prop('disabled', true);
            $('#note').val(note).prop('disabled', true);
            $('#saveBtn').hide();
            loadMeasurements(id, true);
        });

        $('.edit-detail').on('click', function () {
            const id = $(this).data('id');
            const qty = $(this).data('qty');
            const note = $(this).data('note');
            $('#detailId').val(id);
            $('#quantity').val(qty).prop('disabled', false);
            $('#note').val(note).prop('disabled', false);
            $('#saveBtn').show();
            loadMeasurements(id, false);
        });

        $('#editDetailForm').on('submit', function (e) {
            e.preventDefault();
            $.post(baseUrl + '/order-details/update', $(this).serialize())
                .done(function () {
                    location.reload();
                });
        });
    });
</script>

<jsp:include page="/jsp/common/footer.jsp"/>
