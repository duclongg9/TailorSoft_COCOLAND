<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Thêm vải"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
<h2>Thêm vải</h2>
<form action="" method="post" enctype="multipart/form-data">
    <div class="row">
        <div class="col-md-6">
            <div class="mb-3">
                <label class="form-label">Tên vải</label>
                <input type="text" name="name" class="form-control" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Màu sắc</label>
                <input type="text" name="color" class="form-control" required/>
            </div>
        </div>
        <div class="col-md-6">
            <div class="mb-3">
                <label class="form-label">Xuất xứ</label>
                <input type="text" name="origin" class="form-control"/>
            </div>
            <div class="mb-3">
                <label class="form-label">Giá (₫)</label>
                <input type="number" name="price" class="form-control" min="0" step="1000" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Số lượng (m)</label>
                <input type="number" name="quantity" class="form-control" min="0" step="0.1" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Ảnh hóa đơn</label>
                <input type="file" name="invoiceImage" class="form-control" accept="image/*"/>
            </div>
        </div>
    </div>
    <button type="submit" class="btn btn-primary">Lưu</button>
</form>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>
