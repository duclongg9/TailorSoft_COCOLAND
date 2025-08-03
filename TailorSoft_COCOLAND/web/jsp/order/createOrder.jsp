<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Thêm đơn hàng"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <h2>Thêm đơn hàng</h2>
        <form action="" method="post" id="orderForm" class="mt-3">
            <ul class="nav nav-tabs" id="orderWizard" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="step1-tab" data-bs-toggle="tab" data-bs-target="#step1" type="button" role="tab">1. Khách hàng</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="step2-tab" data-bs-toggle="tab" data-bs-target="#step2" type="button" role="tab">2. Sản phẩm</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="step3-tab" data-bs-toggle="tab" data-bs-target="#step3" type="button" role="tab">3. Vật tư & giá</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="step4-tab" data-bs-toggle="tab" data-bs-target="#step4" type="button" role="tab">4. Thanh toán</button>
                </li>
            </ul>
            <div class="tab-content border border-top-0 p-3" id="orderWizardContent">
                <div class="tab-pane fade show active" id="step1" role="tabpanel">
                    <div class="mb-3">
                        <label class="form-label">Khách hàng</label>
                        <select name="customerId" id="customerSelect" class="form-select" required>
                            <option></option>
                            <c:forEach var="c" items="${customers}">
                                <option value="${c.id}">${c.name} - ${c.phone}</option>
                            </c:forEach>
                        </select>
                        <c:url var="customerCreateUrl" value="/customers/create">
                            <c:param name="returnUrl" value="/orders/create"/>
                        </c:url>
                        <div class="form-text"><a href="${customerCreateUrl}">Thêm khách mới</a></div>
                    </div>
                </div>
                <div class="tab-pane fade" id="step2" role="tabpanel">
                    <div id="itemsContainer">
                        <button type="button" class="btn btn-outline-primary mt-2" id="addItemBtn">+ Chọn sản phẩm</button>
                    </div>
                    <template id="itemTemplate">
                        <div class="card mb-3">
                            <div class="card-body">
                                <div class="text-end">
                                    <button type="button" class="btn-close remove-item" aria-label="Xóa"></button>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Loại sản phẩm</label>
                                        <select class="form-select productTypeSelect" name="productTypeId___INDEX__" required>                                            
                                        <option value="">--Chọn loại--</option>
                                            <c:forEach var="pt" items="${productTypes}">
                                                <option value="${pt.id}">${pt.name}</option>
                                            </c:forEach>
                                        </select>
                                        <c:url var="ptCreateUrl" value="/product-types/create">
                                            <c:param name="returnUrl" value="/orders/create"/>
                                        </c:url>
                                        <div class="form-text"><a href="${ptCreateUrl}">Thêm loại sản phẩm</a></div>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label">Số lượng</label>
                                        <input type="number" class="form-control" name="quantity___INDEX__" value="1" min="1" required/>
                                    </div>
                                </div>
                                <div class="row measurement-fields d-none"></div>
                            </div>
                        </div>
                    </template>
                </div>
                <div class="tab-pane fade" id="step3" role="tabpanel">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Tổng tiền</label>
                            <input type="number" name="total" class="form-control" step="1000" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Chọn vải</label>
                            <div id="materialsContainer"></div>
                            <button type="button" class="btn btn-outline-primary mt-2" id="addMaterialBtn">+ Thêm vải khác</button>
                            <c:url var="materialCreateUrl" value="/materials/create">
                                <c:param name="returnUrl" value="/orders/create"/>
                            </c:url>
                            <div class="form-text"><a href="${materialCreateUrl}">Thêm vải mới</a></div>
                            <template id="materialTemplate">
                                <div class="input-group mb-2">
                                    <select class="form-select" name="materialId__INDEX__" required>
                                        <option value="">--Chọn vải--</option>
                                        <c:forEach var="m" items="${materials}">
                                            <option value="${m.id}">${m.name}</option>
                                        </c:forEach>
                                    </select>
                                    <input type="number" class="form-control" name="materialQty__INDEX__" placeholder="Số lượng" min="0.1" step="0.1" required>
                                    <button type="button" class="btn btn-outline-danger remove-material">&times;</button>
                                </div>
                            </template>
                        </div>
                    </div>
                </div>
                <div class="tab-pane fade" id="step4" role="tabpanel">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Đã cọc</label>
                            <input type="number" name="deposit" class="form-control" step="1000" required>
                        </div>
                        <div class="col-md-3 mb-3">
                            <label class="form-label">Ngày đặt</label>
                            <input type="date" name="orderDate" class="form-control" required>
                        </div>
                        <div class="col-md-3 mb-3">
                            <label class="form-label">Ngày giao</label>
                            <input type="date" name="deliveryDate" class="form-control" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Trạng thái</label>
                        <select name="status" class="form-select" required>
                            <option value="Dang may">Đang may</option>
                            <option value="Hoan thanh">Hoàn thành</option>
                        </select>
                    </div>
                    <div class="text-center mb-3">
                        <img src="<c:url value='/img/payment-qr.jpg'/>" alt="QR Code" class="img-fluid" style="max-width:300px;">
                    </div>
                    <div class="alert alert-secondary">Tổng: <span id="summaryTotal">0</span> ₫ - Đã cọc: <span id="summaryDeposit">0</span> ₫</div>
                </div>
            </div>
            <div class="d-flex justify-content-between mt-3">
                <button type="button" class="btn btn-secondary" id="prevBtn">Trước</button>
                <button type="button" class="btn btn-primary" id="nextBtn">Tiếp</button>
                <button type="submit" class="btn btn-success d-none" id="finishBtn">Hoàn tất</button>
            </div>
        </form>
    </div>
</div>
<style>
    .measurement-input:invalid { border-color: #dc3545; }
</style>
<script>
    document.addEventListener('DOMContentLoaded', function () {
    if (typeof $ === 'function' && $.fn.select2) {
        $('#customerSelect').select2({placeholder:'Chọn khách hàng',width:'100%'});
    }
    let current = 0;
    const orderTabs = ['step1','step2','step3','step4'];
    function showStep(i){
        const tabEl = document.querySelector(`#${orderTabs[i]}-tab`);
        if (window.bootstrap && window.bootstrap.Tab) {
            new bootstrap.Tab(tabEl).show();
        } else {
            document.querySelectorAll('#orderWizard .nav-link').forEach(l => l.classList.remove('active'));
            document.querySelectorAll('#orderWizardContent .tab-pane').forEach(p => p.classList.remove('show','active'));
            tabEl.classList.add('active');
            const pane = document.getElementById(orderTabs[i]);
            if (pane) pane.classList.add('show','active');
        }
        document.getElementById('prevBtn').style.display = i===0?'none':'inline-block';
        document.getElementById('nextBtn').classList.toggle('d-none', i===orderTabs.length-1);
        document.getElementById('finishBtn').classList.toggle('d-none', i!==orderTabs.length-1);
    }
    function validateStep(i){
        const pane = document.getElementById(orderTabs[i]);
        const inputs = pane.querySelectorAll('input, select');
        for(const el of inputs){
            if(!el.checkValidity()){
                el.reportValidity();
                return false;
            }
        }
        return true;
    }
    showStep(0);
    document.getElementById('nextBtn').addEventListener('click',()=>{
        if(current<orderTabs.length-1 && validateStep(current)){
            current++;
            showStep(current);
        }
    });
    document.getElementById('prevBtn').addEventListener('click',()=>{
        if(current>0){
            current--;
            showStep(current);
        }
    });

    const mtUrl = '<c:url value="/product-types/measurement-types"/>';
    let itemIndex = 0;
    const addItemBtn = document.getElementById('addItemBtn');
    function updateAddItemBtn(){
        const hasItem = document.querySelectorAll('#itemsContainer .card').length > 0;
        addItemBtn.textContent = hasItem ? '+ Thêm sản phẩm khác' : '+ Chọn sản phẩm';
    }
    function addItem(){
        const idx = itemIndex;
        const tpl = document.getElementById('itemTemplate').innerHTML.replace(/__INDEX__/g, idx);
        const div = document.createElement('div');
        div.innerHTML = tpl;
        const item = div.firstElementChild;

        const container = document.getElementById('itemsContainer');
        container.insertBefore(item, addItemBtn);
        item.querySelector('.remove-item').addEventListener('click', () => { item.remove(); updateAddItemBtn(); });
        const select = item.querySelector('.productTypeSelect');
        select.addEventListener('change', function(){
            const ptId = this.value;
            const fields = item.querySelector('.measurement-fields');
            fields.innerHTML = '';
            fields.classList.add('d-none');
            if(!ptId) return;
            fetch(mtUrl + '?productTypeId=' + ptId)
                .then(res => { if(!res.ok) throw new Error(); return res.json(); })
                .then(data => {
                    data.forEach(mt => {
                        const col = document.createElement('div');
                        col.className = 'col-md-6 mb-3';
                        col.innerHTML = `<label class="form-label">${mt.name} (${mt.unit})</label>` +
                            `<input type="number" step="0.1" class="form-control measurement-input" name="item${idx}_m${mt.id}" placeholder="${mt.unit}" required>`;
                        fields.appendChild(col);
                    });
                    fields.classList.remove('d-none');
                    const inputs = fields.querySelectorAll('.measurement-input');
                    inputs.forEach((inp, i) => {
                        inp.addEventListener('keydown', e => {
                            if(e.key === 'Enter'){
                                e.preventDefault();
                                const next = inputs[i+1];
                                if(next){ next.focus(); }
                            }
                        });
                    });
                })
                .catch(() => {
                    fields.innerHTML = '<div class="text-danger">Không lấy được kích thước</div>';
                    fields.classList.remove('d-none');
                });
        });
        itemIndex++;
        updateAddItemBtn();
    }
    addItemBtn.addEventListener('click', addItem);
    addItem();
    const totalInput = document.querySelector('input[name="total"]');
    const depositInput = document.querySelector('input[name="deposit"]');
    function validatePayment(){
        if(Number(depositInput.value) > Number(totalInput.value)){
            depositInput.setCustomValidity('Đã cọc không được vượt quá tổng tiền');
        }else{
            depositInput.setCustomValidity('');
        }
    }
    function updateSummary(){
        validatePayment();
        document.getElementById('summaryTotal').textContent = totalInput.value || 0;
        document.getElementById('summaryDeposit').textContent = depositInput.value || 0;
    }
    totalInput.addEventListener('input', updateSummary);
    depositInput.addEventListener('input', updateSummary);
    let materialIndex = 0;
    function addMaterial(){
        const tpl = document.getElementById('materialTemplate').innerHTML.replace(/__INDEX__/g, materialIndex);
        const div = document.createElement('div');
        div.innerHTML = tpl;
        const row = div.firstElementChild;
        row.querySelector('.remove-material').addEventListener('click', () => row.remove());
        document.getElementById('materialsContainer').appendChild(row);
        materialIndex++;
    }
    document.getElementById('addMaterialBtn').addEventListener('click', addMaterial);
    addMaterial();
    updateSummary();
    const orderForm = document.getElementById('orderForm');
    orderForm.addEventListener('keydown', e => {
        if(e.key === 'Enter' && e.target.tagName !== 'TEXTAREA'){
            e.preventDefault();
        }
    });
    orderForm.addEventListener('submit', function(e){
        if(!validateStep(current) || !this.checkValidity()){
            e.preventDefault();
            this.reportValidity();
            return;
        }
        if(!confirm('Xác nhận đã thanh toán đơn hàng?')){
            e.preventDefault();
        }
    });
    });
</script>
<jsp:include page="/jsp/common/footer.jsp"/>
