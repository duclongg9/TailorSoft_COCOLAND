<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Thêm số đo"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <h2>Thêm số đo</h2>
        <form action="" method="post">
            <div class="mb-3">
                <label class="form-label">Mã khách hàng</label>
                <input type="number" name="customerId" class="form-control" required/>
            </div>
            <div class="mb-3">
                <label class="form-label">Loại sản phẩm</label>
                <select name="productTypeId" id="productType" class="form-select" required>
                    <option value="">--Chọn loại--</option>
                    <c:forEach var="pt" items="${productTypes}">
                        <option value="${pt.id}">${pt.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div id="measurementFields"></div>
            <button type="submit" class="btn btn-primary mt-3">Lưu</button>
        </form>
    </div>
</div>
<script>
const baseUrl = '<c:url value="/product-types/measurement-types"/>';
document.getElementById('productType').addEventListener('change', function(){
    const ptId = this.value;
    const container = document.getElementById('measurementFields');
    container.innerHTML = '';
    if(!ptId) return;
    fetch(baseUrl + '?productTypeId=' + ptId)
        .then(res => res.json())
        .then(data => {
            data.forEach(mt => {
                const div = document.createElement('div');
                div.className = 'mb-3';
                div.innerHTML =
                    '<label class="form-label">' + mt.name + ' (' + mt.unit + ')</label>'+
                    '<input type="number" step="0.1" name="value_' + mt.id + '" class="form-control"/>'+
                    '<input type="text" name="note_' + mt.id + '" class="form-control mt-1" placeholder="Ghi chú"/>';
                container.appendChild(div);
            });
        });
});
</script>
<jsp:include page="/jsp/common/footer.jsp"/>
