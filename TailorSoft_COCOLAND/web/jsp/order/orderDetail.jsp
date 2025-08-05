<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.google.gson.Gson" %>
<%
  Object msObj = request.getAttribute("measurements");
  String measurementsJson = msObj != null ? new Gson().toJson(msObj) : "{}";
%>
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
                            <p><strong>Trạng thái:</strong> <span id="orderStatus">${order.status}</span></p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${order.total}" type="number" groupingUsed="true"/> ₫</p>
                            <p><strong>Đã thanh toán:</strong> <fmt:formatNumber value="${order.deposit}" type="number" groupingUsed="true"/> ₫</p>
                            <p><strong>Còn lại:</strong> <fmt:formatNumber value="${order.total - order.deposit}" type="number" groupingUsed="true"/> ₫</p>
                            <button type="button" class="btn btn-outline-primary btn-sm" id="editOrderBtn" data-id="${order.id}" data-total="${order.total}" data-deposit="${order.deposit}"><i class="fa fa-pen"></i> Sửa tiền</button>
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
                                <button class="btn btn-outline-info btn-sm view-detail me-1"
                                        title="Xem"
                                        data-detail-id="${d.id}"
                                        data-quantity="${d.quantity}">
                                    <i class="bi bi-eye"></i>
                                </button>
                                <button class="btn btn-outline-secondary btn-sm edit-detail"
                                        title="Sửa"
                                        data-detail-id="${d.id}"
                                        data-product-type-id="${d.productTypeId}"
                                        data-quantity="${d.quantity}"
                                        data-unit-price="${d.unitPrice}">
                                    <i class="bi bi-pencil-square"></i>
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
            <div class="d-flex gap-2 mb-3">
                <button type="button" class="btn btn-outline-primary btn-sm" id="toggleStatusBtn" data-id="${order.id}"><i class="fa fa-pen"></i> Cập nhật trạng thái</button>
                <button type="button" class="btn btn-outline-success btn-sm" data-bs-toggle="modal" data-bs-target="#uploadPaymentModal" data-order-id="${order.id}"><i class="fa fa-coins"></i> Thêm thanh toán</button>
                <a href="#" class="btn btn-outline-secondary btn-sm"><i class="fa fa-print"></i> In phiếu</a>
            </div>
            <c:if test="${not empty order.depositImage || not empty order.fullImage}">
                <div class="card mb-4">
                    <div class="card-body">
                        <div class="row">
                            <c:if test="${not empty order.depositImage}">
                                <div class="col-md-6 mb-3">
                                    <h6 class="mb-2">Ảnh cọc</h6>
                                    <img src="<c:url value='/uploads/${order.depositImage}'/>" alt="Ảnh cọc" class="img-fluid mb-2" style="max-height:200px;">
                                    <button type="button" class="btn btn-sm btn-secondary" onclick="showPayment('<c:url value="/uploads/${order.depositImage}"/>')">Xem</button>
                                </div>
                            </c:if>
                            <c:if test="${not empty order.fullImage}">
                                <div class="col-md-6 mb-3">
                                    <h6 class="mb-2">Ảnh thanh toán còn lại</h6>
                                    <img src="<c:url value='/uploads/${order.fullImage}'/>" alt="Ảnh thanh toán" class="img-fluid mb-2" style="max-height:200px;">
                                    <button type="button" class="btn btn-sm btn-secondary" onclick="showPayment('<c:url value="/uploads/${order.fullImage}"/>')">Xem</button>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:if>
        <a href="<c:url value='/orders'/>">Quay lại</a>
</div>
</div>

<div class="modal fade" id="viewDetailModal" tabindex="-1" aria-labelledby="viewDetailLbl" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-scrollable">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="viewDetailLbl">Chi tiết sản phẩm</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <div class="row g-3 mb-3">
          <div class="col-md-6">
            <label class="form-label">Loại sản phẩm</label>
            <input type="text" class="form-control" id="vdProductTypeName" disabled>
          </div>
          <div class="col-md-6">
            <label class="form-label">Tên vải</label>
            <input type="text" class="form-control" id="vdMaterialName" disabled>
          </div>
        </div>

        <div class="row g-3 mb-3">
          <div class="col-md-4">
            <label class="form-label">Số lượng</label>
            <input type="number" class="form-control" id="vdQuantity" disabled>
          </div>
          <div class="col-md-4">
            <label class="form-label">Đơn giá</label>
            <input type="text" class="form-control" id="vdUnitPrice" disabled>
          </div>
          <div class="col-md-4">
            <label class="form-label">Ghi chú</label>
            <input type="text" class="form-control" id="vdNote" disabled>
          </div>
        </div>

        <h6 class="text-muted mb-2"><i class="bi bi-rulers"></i> Thông tin số đo</h6>
        <div class="row gy-3" id="vdMeasurements"></div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
      </div>
    </div>
  </div>
</div>

<div class="modal fade" id="editDetailModal" tabindex="-1" aria-labelledby="editDetailLbl" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-scrollable">
    <div class="modal-content">
      <form id="editDetailForm" action="<c:url value='/order-details/update'/>" method="post">
        <div class="modal-header">
          <h5 class="modal-title" id="editDetailLbl">Chỉnh sửa sản phẩm</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <input type="hidden" name="detailId" id="edDetailId">

          <div class="row g-3 mb-3">
            <div class="col-md-8">
              <label class="form-label">Loại sản phẩm</label>
              <!-- không cho đổi loại, nếu cần đổi thì xoá & tạo lại -->
              <input type="text" class="form-control" id="edProductTypeName" disabled>
            </div>
            <div class="col-md-4">
              <label class="form-label">Số lượng</label>
              <input type="number" class="form-control" name="quantity" id="edQuantity" min="1" required>
            </div>
          </div>
          <div class="row g-3 mb-3">
            <div class="col-md-6">
              <label class="form-label">Đơn giá</label>
              <input type="number" class="form-control" name="unitPrice" id="edUnitPrice" step="1000" min="0" required>
            </div>
            <div class="col-md-6">
              <label class="form-label">Ghi chú</label>
              <input type="text" class="form-control" name="note" id="edNote">
            </div>
          </div>
          
          <h6 class="text-muted mb-2"><i class="bi bi-rulers"></i> Thông tin số đo</h6>
          <div class="row gy-3" id="edMeasurements"></div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
          <button type="submit" class="btn btn-primary">Lưu</button>
        </div>
      </form>
    </div>
  </div>
</div>

<div class="modal fade" id="uploadPaymentModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form class="modal-content" method="post" enctype="multipart/form-data" action="<c:url value='/orders/payment-image'/>">
            <div class="modal-header">
                <h5 class="modal-title">Thêm thanh toán</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="orderId" value="${order.id}">
                <div class="mb-3">
                    <label class="form-label">Loại</label>
                    <select name="type" class="form-select">
                        <option value="deposit">Cọc</option>
                        <option value="full">Full</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Ảnh chuyển khoản</label>
                    <input type="file" name="paymentImage" class="form-control" accept="image/*" required>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-primary">Lưu</button>
            </div>
        </form>
    </div>
</div>

<div class="modal fade" id="paymentModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Ảnh chuyển khoản</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center">
                <img id="paymentModalImage" src="" alt="Ảnh chuyển khoản" class="img-fluid"/>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editOrderModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form id="editOrderForm" class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Cập nhật tiền đơn hàng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="orderId" id="orderId">
                <div class="mb-3">
                    <label class="form-label">Tổng tiền</label>
                    <input type="number" step="1000" class="form-control" name="total" id="orderTotal">
                </div>
                <div class="mb-3">
                    <label class="form-label">Đã thanh toán</label>
                    <input type="number" step="1000" class="form-control" name="deposit" id="orderDeposit">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-primary">Lưu</button>
            </div>
        </form>
    </div>
</div>

<script>
    const measurements = <%= measurementsJson %>;

    function showPayment(src) {
        document.getElementById('paymentModalImage').src = src;
        const modal = new bootstrap.Modal(document.getElementById('paymentModal'));
        modal.show();
    }

    document.addEventListener('DOMContentLoaded', () => {
      const orderUpdateUrl = '<c:url value="/orders/update-amount"/>';
      const toggleStatusUrl = '<c:url value="/orders/toggle-status"/>';
      const editModal  = new bootstrap.Modal(document.getElementById('editDetailModal'));
      const viewModal  = new bootstrap.Modal(document.getElementById('viewDetailModal'));
      const orderModal = new bootstrap.Modal(document.getElementById('editOrderModal'));
      const $form      = document.getElementById('editDetailForm');
      const $fields    = document.getElementById('edMeasurements');
      const $viewFields = document.getElementById('vdMeasurements');
      const orderForm  = document.getElementById('editOrderForm');
      const statusBtn  = document.getElementById('toggleStatusBtn');
      const statusText = document.getElementById('orderStatus');
      const addPaymentBtn = document.querySelector('[data-bs-toggle="modal"][data-bs-target="#uploadPaymentModal"]');

      const formatValue = v => {
        const num = Number(v);
        if (Number.isNaN(num)) return '';
        return Number.isInteger(num) ? num.toString() : num.toFixed(1);
      };

      document.querySelectorAll('.view-detail').forEach(btn => btn.addEventListener('click', () => {
        const detailId = btn.dataset.detailId;
        const cells    = btn.closest('tr').querySelectorAll('td');

        document.getElementById('vdProductTypeName').value = cells[0].textContent.trim();
        document.getElementById('vdMaterialName').value   = cells[1].textContent.trim();
        document.getElementById('vdUnitPrice').value      = cells[2].textContent.trim();
        document.getElementById('vdQuantity').value       = cells[3].textContent.trim();
        document.getElementById('vdNote').value           = cells[4].textContent.trim();

        const list = measurements[detailId];
        if (!Array.isArray(list) || list.length === 0) {
          $viewFields.innerHTML = '<p class="text-muted">Không có số đo.</p>';
        } else {
          $viewFields.innerHTML = '';
          list.forEach(m => {
            const col = document.createElement('div');
            col.className = 'col-md-6';
            col.innerHTML =
              '<label class="form-label">' + m.name + ' (' + m.unit + ')</label>' +
              '<input type="text" class="form-control" value="' + formatValue(m.value) + '" disabled>';
            $viewFields.appendChild(col);
          });
        }

        viewModal.show();
      }));

      document.querySelectorAll('.edit-detail').forEach(btn => btn.addEventListener('click', () => {
        const detailId = btn.dataset.detailId;
        const ptName   = btn.closest('tr').querySelector('td:nth-child(1)').textContent.trim();

        document.getElementById('edDetailId').value = detailId;
        document.getElementById('edProductTypeName').value = ptName;
        document.getElementById('edQuantity').value = btn.dataset.quantity;
        document.getElementById('edUnitPrice').value = btn.dataset.unitPrice;
        const noteText = btn.closest('tr').querySelector('td:nth-child(5)').textContent.trim();
        document.getElementById('edNote').value = noteText;

        const list = measurements[detailId];
        if (!Array.isArray(list) || list.length === 0) {
          $fields.innerHTML = '<p class="text-muted">Không có số đo.</p>';
        } else {
          $fields.innerHTML = '';
          list.forEach(m => {
            const col = document.createElement('div');
            col.className = 'col-md-6';
            col.innerHTML =
              '<label class="form-label">' + m.name + ' (' + m.unit + ')</label>' +
              '<input type="number" class="form-control" step="0.1" name="m_' + m.id +
              '" value="' + formatValue(m.value) + '" required>';
            $fields.appendChild(col);
          });
        }

        editModal.show();
      }));

      $form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new URLSearchParams(new FormData($form)).toString();
        try {
          const resp = await fetch($form.action, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData
          });
          if (!resp.ok) throw new Error('HTTP ' + resp.status);
          location.reload();
        } catch (err) {
          alert('Lưu thất bại'); console.error(err);
        }
      });

      const editOrderBtn = document.getElementById('editOrderBtn');
      if (editOrderBtn) {
        editOrderBtn.addEventListener('click', function (e) {
          const btn = e.currentTarget;
          document.getElementById('orderId').value = btn.dataset.id;
          document.getElementById('orderTotal').value = btn.dataset.total;
          document.getElementById('orderDeposit').value = btn.dataset.deposit;
          orderModal.show();
        });
      }
      
      if (addPaymentBtn) {
        addPaymentBtn.addEventListener('click', function () {
          const orderId = this.dataset.orderId;
          document.querySelector('#uploadPaymentModal input[name="orderId"]').value = orderId;
        });
      }



      orderForm.addEventListener('submit', async e => {
        e.preventDefault();
        const data = new URLSearchParams(new FormData(orderForm)).toString();
        try {
          const resp = await fetch(orderUpdateUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: data
          });
          location.reload();
        } catch (err) {
          alert('Cập nhật thất bại'); console.error(err);
        }
      });
      
      if (statusBtn) {
        statusBtn.addEventListener('click', async function (e) {
          e.preventDefault();
          const orderId = this.dataset.id;
          try {
            const resp = await fetch(toggleStatusUrl, {
              method: 'POST',
              headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
              body: 'id=' + orderId
            });
            if (!resp.ok) throw new Error('HTTP ' + resp.status);
            const data = await resp.json();
            statusText.textContent = data.status;
          } catch (err) {
            alert('Cập nhật trạng thái thất bại');
            console.error(err);
          }
        });
      }
    });
</script>

<jsp:include page="/jsp/common/footer.jsp"/>
