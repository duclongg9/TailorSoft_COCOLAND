<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Cập nhật khách hàng"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
<h2>Cập nhật khách hàng</h2>
<form action="" method="post">
    <input type="hidden" name="id" value="${customer.id}"/>
    <div class="mb-3">
        <label class="form-label">Họ tên</label>
        <input type="text" name="name" value="${customer.name}" class="form-control" required/>
    </div>
    <div class="mb-3">
        <label class="form-label">Số điện thoại</label>
        <input type="tel" name="phone" value="${customer.phone}" class="form-control" required/>
    </div>
    <div class="mb-3">
        <label class="form-label">Email</label>
        <input type="email" name="email" value="${customer.email}" class="form-control"/>
    </div>
    <div class="mb-3">
        <label class="form-label">Địa chỉ nhận hàng</label>
        <input type="text" name="address" value="${customer.address}" class="form-control"/>
    </div>
    <button type="submit" class="btn btn-primary">Lưu</button>
</form>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>
