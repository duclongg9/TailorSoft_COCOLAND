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
                        <button type="button" class="btn btn-outline-primary mt-2" id="addItemBtn">
                            + Chọn sản phẩm
                        </button>
                    </div>

                    <template id="itemTemplate">
                        <div class="card mb-3">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-start">
                                    <h6 class="fw-semibold mb-2">Sản phẩm #__INDEX__</h6>
                                    <button type="button" class="btn-close remove-item" aria-label="Xóa"></button>
                                </div>

                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Loại sản phẩm</label>
                                        <select class="form-select productTypeSelect" name="productTypeId___INDEX__" required>
                                            <option value="" selected>--Chọn loại--</option>
                                            <c:forEach var="pt" items="${productTypes}">
                                                <option value="${pt.id}">${pt.name}</option>
                                            </c:forEach>
                                        </select>
                                        <c:url var="ptCreateUrl" value="/product-types/create">
                                            <c:param name="returnUrl" value="/orders/create"/>
                                        </c:url>
                                        <small class="form-text"><a href="${ptCreateUrl}">Thêm loại sản phẩm</a></small>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label">Số lượng</label>
                                        <input type="number" class="form-control" name="quantity___INDEX__" value="1" min="1" required>
                                    </div>
                                </div>

                                <div class="measurement-wrapper mt-4 d-none">
                                    <h6 class="text-muted mb-2"><i class="bi bi-rulers"></i> Thông tin số đo (<span class="measure-title"></span>)</h6>
                                    <div class="row gy-3 measurement-fields"></div>
                                </div>
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
                                    <select class="form-select" name="materialId___INDEX__" required>
                                        <option value="">--Chọn vải--</option>
                                        <c:forEach var="m" items="${materials}">
                                            <option value="${m.id}">${m.name}</option>
                                        </c:forEach>
                                    </select>
                                    <input type="number" class="form-control" name="materialQty___INDEX__" placeholder="Số lượng" min="0.1" step="0.1" required>
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
            // Bước 1 dùng Select2 nên cần kiểm tra thủ công
            if(i === 0){
                const customer = document.getElementById('customerSelect');
                if(!customer.value){
                    // Thông báo thân thiện khi chưa chọn khách hàng
                    alert('Vui lòng chọn khách hàng');
                    if(customer.nextElementSibling){
                        customer.nextElementSibling.focus();
                    } else {
                        customer.focus();
                    }
                    return false;
                }
                return true;
            }
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
        const itemsContainer = document.getElementById('itemsContainer');
        const addItemBtn = document.getElementById('addItemBtn');

        function updateAddBtnLabel(){
            const hasItem = itemsContainer.querySelectorAll('.card').length > 0;
            addItemBtn.textContent = hasItem ? '+ Thêm sản phẩm khác' : '+ Chọn sản phẩm';
        }

        function addItem(){
            const tpl = document.getElementById('itemTemplate').innerHTML.replace(/__INDEX__/g, itemIndex);
            const wrap = document.createElement('div');
            wrap.innerHTML = tpl;
            const card = wrap.firstElementChild;

            card.querySelector('.remove-item').addEventListener('click', () => {
                card.remove();
                updateAddBtnLabel();
            });

            const select = card.querySelector('.productTypeSelect');
            select.addEventListener('change', () => loadMeasurements(select, card));

            if (typeof $ === 'function' && $.fn.select2) {
                $(select).select2({ placeholder: 'Chọn loại', dropdownParent: $(card), width: '100%' });
            }

            itemsContainer.insertBefore(card, addItemBtn);
            itemIndex++;
            updateAddBtnLabel();
        }

        async function loadMeasurements(select, card){
            const ptId = select.value;
            const wrapper = card.querySelector('.measurement-wrapper');
            const fields = wrapper.querySelector('.measurement-fields');
            const title = wrapper.querySelector('.measure-title');
            fields.innerHTML = '';
            wrapper.classList.add('d-none');

            if(!ptId) return;

            try {
                const resp = await fetch(mtUrl + '?productTypeId=' + ptId);
                if(!resp.ok) throw new Error('HTTP ' + resp.status);
                const data = await resp.json();
                if(!Array.isArray(data) || data.length === 0){
                    fields.innerHTML = '<p class="text-warning mb-0">Chưa cấu hình số đo cho loại sản phẩm này.</p>';
                } else {
                    data.forEach(mt => {
                        const col = document.createElement('div');
                        col.className = 'col-md-6';
                        col.innerHTML = `\n                            <label class="form-label">${mt.name} (${mt.unit})</label>\n                            <input type="number" class="form-control" step="0.1" name="item${select.name.match(/\d+/)[0]}_m${mt.id}" placeholder="${mt.unit}" required>\n                        `;
                        fields.appendChild(col);
                    });
                }
                const selText = select.options[select.selectedIndex].textContent.trim();
                title.textContent = selText;
                wrapper.classList.remove('d-none');
            } catch (err) {
                console.error(err);
                fields.innerHTML = '<p class="text-danger mb-0">Lỗi tải dữ liệu số đo</p>';
                wrapper.classList.remove('d-none');
            }
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
