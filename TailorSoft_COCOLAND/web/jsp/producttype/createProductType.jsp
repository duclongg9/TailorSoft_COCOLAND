<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Thêm loại sản phẩm"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <h2>Thêm loại sản phẩm may</h2>
        <form action="" method="post" id="productTypeForm" class="mt-3">
            <input type="hidden" name="returnUrl" value="${param.returnUrl}"/>
            <div class="mb-3">
                <label class="form-label">Tên loại sản phẩm</label>
                <input type="text" name="name" class="form-control" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Mã / Ký hiệu</label>
                <input type="text" name="code" class="form-control">
            </div>
            <div class="mb-3">
                <label class="form-label">Chọn các loại số đo áp dụng</label>
                <div class="row g-2 mb-2">
                    <div class="col-md-4">
                        <select id="bodyFilter" class="form-select">
                            <option value="">Tất cả bộ phận</option>
                            <c:forEach var="bp" items="${bodyParts}">
                                <option value="${bp}">${bp}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-8">
                        <input type="text" id="mtSearch" class="form-control" placeholder="Tìm số đo...">
                    </div>
                </div>
                <div id="mtTags">
                    <c:forEach var="mt" items="${measurementTypes}">
                        <span class="mt-tag" data-id="${mt.id}" data-body="${mt.bodyPart}">${mt.name}</span>
                    </c:forEach>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Số đo đã chọn:</label>
                <div id="selectedTags"></div>
            </div>
            <button type="submit" class="btn btn-primary">Lưu loại sản phẩm</button>
        </form>
    </div>
</div>
<style>
    .mt-tag{cursor:pointer;border:1px solid #ccc;padding:4px 8px;border-radius:4px;margin:4px;display:inline-block;}
    .mt-tag.selected{background-color:#0d6efd;color:#fff;border-color:#0d6efd;}
</style>
<script>
    const tagContainer=document.getElementById('mtTags');
    const selectedContainer=document.getElementById('selectedTags');
    const form=document.getElementById('productTypeForm');
    function refreshSelected(){
        selectedContainer.innerHTML='';
        form.querySelectorAll('input[name="measurementTypeId"]').forEach(el=>el.remove());
        tagContainer.querySelectorAll('.mt-tag.selected').forEach(tag=>{
            const id=tag.dataset.id;
            const span=document.createElement('span');
            span.className='badge bg-info text-dark me-1';
            span.textContent=tag.textContent;
            selectedContainer.appendChild(span);
            const input=document.createElement('input');
            input.type='hidden';
            input.name='measurementTypeId';
            input.value=id;
            form.appendChild(input);
        });
    }
    tagContainer.querySelectorAll('.mt-tag').forEach(tag=>{
        tag.addEventListener('click',()=>{tag.classList.toggle('selected');refreshSelected();});
    });
    const filterSelect=document.getElementById('bodyFilter');
    const searchInput=document.getElementById('mtSearch');
    function applyFilter(){
        const bp=filterSelect.value.toLowerCase();
        const q=searchInput.value.toLowerCase();
        tagContainer.querySelectorAll('.mt-tag').forEach(tag=>{
            const matchBody=!bp||tag.dataset.body.toLowerCase()===bp;
            const matchText=tag.textContent.toLowerCase().includes(q);
            tag.style.display=(matchBody&&matchText)?'':'none';
        });
    }
    filterSelect.addEventListener('change',applyFilter);
    searchInput.addEventListener('keyup',applyFilter);
</script>
<jsp:include page="/jsp/common/footer.jsp"/>
