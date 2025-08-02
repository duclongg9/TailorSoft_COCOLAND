<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Thêm loại số đo"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <h2>Thêm loại số đo</h2>
        <form action="" method="post" class="mt-3">
            <div class="mb-3">
                <label class="form-label">Tên số đo</label>
                <input type="text" name="name" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Bộ phận cơ thể</label>
                <select name="bodyPart" class="form-select" required>
                    <option value="Thân trên">Thân trên</option>
                    <option value="Tay">Tay</option>
                    <option value="Thân dưới">Thân dưới</option>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label">Đơn vị</label>
                <input type="text" name="unit" value="cm" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Ghi chú</label>
                <textarea name="note" class="form-control" rows="3"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Lưu</button>
        </form>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>
