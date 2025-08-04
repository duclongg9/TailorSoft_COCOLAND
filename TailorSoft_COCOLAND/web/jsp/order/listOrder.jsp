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
                    <option value="Don huy">Đơn hủy</option>
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
                <th>STT</th>
                <th class="d-none">ID</th>
                <th>Khách hàng</th>
                <th>Ngày đặt</th>
                <th>Ngày giao</th>
                <th>Trạng thái</th>
                <th class="text-end">Tổng tiền</th>
                <th class="text-end">Đã thanh toán</th>
                <th class="text-end">Còn lại</th>
                <th class="text-center">Actions</th>
                <th class="text-center">Thanh toán</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${orders}" varStatus="st">
                <c:set var="orderMonth"><fmt:formatDate value="${o.orderDate}" pattern="yyyy-MM"/></c:set>
                <tr data-status="${o.status}" data-order-date="${orderMonth}" data-customer-id="${o.customerId}">
                    <td>${st.index + 1}</td>
                    <td class="d-none">${o.id}</td>
                    <td><span data-bs-toggle="tooltip" title="${o.customerPhone} / ${o.customerEmail}">${o.customerName}</span></td>
                    <td><fmt:formatDate value="${o.orderDate}" pattern="dd-MM-yyyy"/></td>
                    <td><fmt:formatDate value="${o.deliveryDate}" pattern="dd-MM-yyyy"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${o.status eq 'Don huy'}"><span class="badge text-bg-secondary">Đơn hủy</span></c:when>
                            <c:when test="${o.status eq 'Hoan thanh'}"><span class="badge text-bg-success">Hoàn thành</span></c:when>
                            <c:otherwise><span class="badge text-bg-info">Đang may</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td class="text-end"><fmt:formatNumber value="${o.total}" type="number" groupingUsed="true"/> ₫</td>
                    <td class="text-end"><fmt:formatNumber value="${o.deposit}" type="number" groupingUsed="true"/> ₫</td>
                    <td class="text-end"><fmt:formatNumber value="${o.total - o.deposit}" type="number" groupingUsed="true"/> ₫</td>
                    <td class="text-center">
                        <a href="<c:url value='/orders/detail?id=${o.id}'/>" class="btn btn-sm btn-outline-secondary me-1" title="Chi tiết"><i class="fa fa-eye"></i></a>
                        <!-- Thay thế thẻ <a ...> bằng nút để chuyển trạng thái -->
                        <button type="button"
                                class="btn btn-sm btn-outline-primary me-1 toggle-status"
                                data-id="${o.id}"
                                data-status="${o.status}"
                                title="Chuyển trạng thái">
                            <i class="fa fa-pen"></i>
                        </button>
                        <a href="#" class="btn btn-sm btn-outline-success me-1" title="In"><i class="fa fa-print"></i></a>
                        <c:if test="${o.status ne 'Don huy'}">
                            <form action="${pageContext.request.contextPath}/orders/cancel"
                                  method="post" class="d-inline"
                                  onsubmit="return confirm('Hủy đơn hàng này?');">
                               <input type="hidden" name="id" value="${o.id}"/>
                               <button class="btn btn-outline-danger btn-sm" title="Hủy">
                                   <i class="fa fa-times"></i>
                               </button>
                            </form>
                        </c:if>
                    </td>
                    <td class="text-center">
                        <c:if test="${o.deposit >= o.total}">
                            <i class="fa fa-check text-success" title="Đã thanh toán"></i>
                        </c:if>
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
        <script>
        (() => {
          const ctx = '${pageContext.request.contextPath}';

          // Xử lý click bút chì
          document.querySelectorAll('.toggle-status').forEach(btn => {
            btn.addEventListener('click', () => {
              const id     = btn.dataset.id;
              const status = btn.dataset.status;   // 'Dang may' | 'Hoan thanh'
              const next   = status === 'Dang may' ? 'Hoan thanh' : 'Dang may';
              const msg    = status === 'Dang may'
                             ? 'Đánh dấu đơn này đã hoàn thành?'
                             : 'Đổi lại trạng thái về “Đang may”?';

              if (!confirm(msg)) return;

              fetch(ctx + '/orders/toggle-status', {
                method: 'POST',
                headers: {'Content-Type':'application/x-www-form-urlencoded'},
                body: new URLSearchParams({id})
              })
              .then(r => { if(!r.ok) throw new Error(); return r.json(); })
              .then(o => {
                // o = {status: 'Hoan thanh'} hoặc 'Dang may'
                btn.dataset.status = o.status;

                // Cập badge trong hàng
                const badge = btn.closest('tr').querySelector('td:nth-child(6) span');
                if (o.status === 'Hoan thanh') {
                  badge.className = 'badge text-bg-success';
                  badge.textContent = 'Hoàn thành';
                } else {
                  badge.className = 'badge text-bg-info';
                  badge.textContent = 'Đang may';
                }
              })
              .catch(() => alert('Không cập nhật được!'));
            });
          });
        })();
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
