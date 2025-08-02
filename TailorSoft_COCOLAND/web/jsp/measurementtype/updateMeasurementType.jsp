<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Cập nhật loại số đo"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <h2>Cập nhật loại số đo</h2>
        <form action="" method="post" class="mt-3">
            <input type="hidden" name="id" value="${measurementType.id}">
            <div class="mb-3">
                <label class="form-label">Tên số đo</label>
                <input type="text" name="name" class="form-control" value="${measurementType.name}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Bộ phận cơ thể</label>
                <select name="bodyPart" class="form-select" required>
                    <option value="Thân trên" ${measurementType.bodyPart=='Thân trên'?'selected':''}>Thân trên</option>
                    <option value="Tay" ${measurementType.bodyPart=='Tay'?'selected':''}>Tay</option>
                    <option value="Thân dưới" ${measurementType.bodyPart=='Thân dưới'?'selected':''}>Thân dưới</option>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label">Đơn vị</label>
                <input type="text" name="unit" class="form-control" value="${measurementType.unit}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Ghi chú</label>
                <textarea name="note" class="form-control" rows="3">${measurementType.note}</textarea>
            </div>
            <button type="submit" class="btn btn-primary">Cập nhật</button>
        </form>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>
