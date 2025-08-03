<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Danh sách khách hàng"/>
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
                        <c:when test="${msg == 'created'}">Thêm khách hàng thành công</c:when>
                        <c:when test="${msg == 'updated'}">Cập nhật khách hàng thành công</c:when>
                        <c:otherwise>Thao tác thành công</c:otherwise>
                    </c:choose>
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </div>
</c:if>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2>Danh sách khách hàng</h2>
</div>

<div class="row mb-3">
    <div class="col-sm-8">
        <input id="customerSearch" type="text" class="form-control" placeholder="Tìm kiếm..."/>
    </div>
    <div class="col-sm-4 mt-2 mt-sm-0">
        <select id="statusFilter" class="form-select">
            <option value="">Tất cả</option>
            <option value="processing">Đang may</option>
            <option value="completed">Hoàn thành</option>
        </select>
    </div>
</div>

<table id="customerTable" class="table table-striped table-hover">
    <thead>
    <tr>
        <th>#</th>
        <th>Họ tên</th>
        <th>Số điện thoại</th>
        <th>Email</th>
        <th>Tổng ĐH | Chưa xong</th>
        <th class="text-center">Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="c" items="${customers}" varStatus="st">
        <tr data-status="${c.pendingOrders > 0 ? 'processing' : 'completed'}">
            <td>${st.index + 1}</td>
            <td><a href="<c:url value='/customers/history?id=${c.id}'/>">${c.name}</a></td>
            <td><a href="<c:url value='/customers/history?id=${c.id}'/>">${c.phone}</a></td>
            <td>${c.email}</td>
            <td>${c.totalOrders} | ${c.pendingOrders}</td>
            <td class="text-center">
                <button type="button" class="btn btn-sm btn-outline-secondary btn-edit" title="Sửa"
                        data-id="${c.id}" data-name="${c.name}" data-phone="${c.phone}"
                        data-email="${c.email}" data-address="${c.address}">
                    <i class="fa fa-pencil-alt"></i>
                </button>
                <a href="<c:url value='/customers/history?id=${c.id}'/>" class="btn btn-sm btn-outline-primary" title="Lịch sử">
                    <i class="fa fa-eye"></i>
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<jsp:include page="/jsp/common/pagination.jsp"/>

<!-- Modal cập nhật khách hàng -->
<div class="modal fade" id="editCustomerModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form method="post" action="<c:url value='/customers/update'/>">
                <div class="modal-header">
                    <h5 class="modal-title">Sửa thông tin khách</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="id" id="edit-id"/>
                    <div class="mb-3">
                        <label class="form-label">Họ tên</label>
                        <input type="text" class="form-control" name="name" id="edit-name" required/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Số điện thoại</label>
                        <input type="text" class="form-control" name="phone" id="edit-phone" required/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" class="form-control" name="email" id="edit-email"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Địa chỉ</label>
                        <textarea class="form-control" name="address" id="edit-address"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    const search = document.getElementById('customerSearch');
    const filter = document.getElementById('statusFilter');
    function applyFilter() {
        const txt = search.value.toLowerCase();
        const status = filter.value;
        document.querySelectorAll('#customerTable tbody tr').forEach(row => {
            const rowText = row.textContent.toLowerCase();
            const rowStatus = row.dataset.status;
            const matchText = rowText.indexOf(txt) > -1;
            const matchStatus = !status || rowStatus === status;
            row.style.display = matchText && matchStatus ? '' : 'none';
        });
    }
    search.addEventListener('keyup', applyFilter);
    filter.addEventListener('change', applyFilter);

    document.querySelectorAll('.btn-edit').forEach(btn => {
        btn.addEventListener('click', function() {
            document.getElementById('edit-id').value = this.dataset.id;
            document.getElementById('edit-name').value = this.dataset.name;
            document.getElementById('edit-phone').value = this.dataset.phone;
            document.getElementById('edit-email').value = this.dataset.email;
            document.getElementById('edit-address').value = this.dataset.address;
            const modal = new bootstrap.Modal(document.getElementById('editCustomerModal'));
            modal.show();
        });
    });
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
