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
                        <div class="form-text"><a href="<c:url value='/customers/create'/>" target="_blank">Thêm khách mới</a></div>
                    </div>
                </div>
                <div class="tab-pane fade" id="step2" role="tabpanel">
                    <div id="itemsContainer"></div>
                    <button type="button" class="btn btn-outline-primary mt-2" id="addItemBtn">+ Thêm sản phẩm khác</button>
                    <template id="itemTemplate">
                        <div class="card mb-3">
                            <div class="card-body">
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Loại sản phẩm</label>
                                        <select class="form-select productTypeSelect" name="productTypeId__INDEX__" required>
                                            <option value="">--Chọn loại--</option>
                                            <c:forEach var="pt" items="${productTypes}">
                                                <option value="${pt.id}">${pt.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="form-label">Số lượng</label>
                                        <input type="number" class="form-control" name="quantity__INDEX__" value="1" min="1" required/>
                                    </div>
                                </div>
                                <div class="row measurement-fields"></div>
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
                            <input type="text" class="form-control" placeholder="Tìm vải..." disabled>
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
<script>
    $('#customerSelect').select2({placeholder:'Chọn khách hàng',width:'100%'});
    const tabs = new bootstrap.Tab(document.querySelector('#step1-tab'));
    let current = 0;
    const orderTabs = ['step1','step2','step3','step4'];
    function showStep(i){
        const tabEl = document.querySelector(`#${orderTabs[i]}-tab`);
        new bootstrap.Tab(tabEl).show();
        document.getElementById('prevBtn').style.display = i===0?'none':'inline-block';
        document.getElementById('nextBtn').classList.toggle('d-none', i===orderTabs.length-1);
        document.getElementById('finishBtn').classList.toggle('d-none', i!==orderTabs.length-1);
    }
    showStep(0);
    document.getElementById('nextBtn').addEventListener('click',()=>{if(current<3){current++;showStep(current);}});
    document.getElementById('prevBtn').addEventListener('click',()=>{if(current>0){current--;showStep(current);}});

    const mtUrl = '<c:url value="/product-types/measurement-types"/>';
    let itemIndex = 0;
    function addItem(){
        const idx = itemIndex;
        const tpl = document.getElementById('itemTemplate').innerHTML.replace(/__INDEX__/g, idx);
        const div = document.createElement('div');
        div.innerHTML = tpl;
        const item = div.firstElementChild;
        document.getElementById('itemsContainer').appendChild(item);
        const select = item.querySelector('.productTypeSelect');
        select.addEventListener('change', function(){
            const ptId = this.value;
            const fields = item.querySelector('.measurement-fields');
            fields.innerHTML = '';
            if(!ptId) return;
            fetch(mtUrl + '?productTypeId=' + ptId)
                .then(res => res.json())
                .then(data => {
                    data.forEach(mt => {
                        const col = document.createElement('div');
                        col.className = 'col-md-6 mb-3';
                        col.innerHTML = `<label class="form-label">${mt.name} (${mt.unit})</label>`+
                            `<input type="number" step="0.1" class="form-control" name="item${idx}_m${mt.id}" placeholder="cm">`;
                        fields.appendChild(col);
                    });
                });
        });
        itemIndex++;
    }
    document.getElementById('addItemBtn').addEventListener('click', addItem);
    addItem();
</script>
<jsp:include page="/jsp/common/footer.jsp"/>
