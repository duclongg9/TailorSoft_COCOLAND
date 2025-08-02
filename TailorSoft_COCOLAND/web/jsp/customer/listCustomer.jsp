<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <a href="<c:url value='/customers/create'/>" class="btn btn-primary">Thêm khách hàng</a>
</div>

<input id="customerSearch" type="text" class="form-control mb-3" placeholder="Tìm kiếm..."/>

<table id="customerTable" class="table table-striped table-hover">
    <thead>
    <tr>
        <th class="d-none">Mã</th>
        <th>Họ tên</th>
        <th>Số điện thoại</th>
        <th>Email</th>
        <th>Địa chỉ</th>
        <th class="text-center">Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="c" items="${customers}">
        <tr>
            <td class="d-none">${c.id}</td>
            <td>${c.name}</td>
            <td>${c.phone}</td>
            <td>${c.email}</td>
            <td>${c.address}</td>
            <td class="text-center">
                <a href="<c:url value='/customers/update?id=${c.id}'/>" class="btn btn-sm btn-outline-secondary" title="Sửa">
                    <i class="fa fa-pencil-alt"></i>
                </a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
 </table>

 <script>
    document.getElementById('customerSearch').addEventListener('keyup', function () {
        const filter = this.value.toLowerCase();
        document.querySelectorAll('#customerTable tbody tr').forEach(function (row) {
            const text = row.textContent.toLowerCase();
            row.style.display = text.indexOf(filter) > -1 ? '' : 'none';
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
