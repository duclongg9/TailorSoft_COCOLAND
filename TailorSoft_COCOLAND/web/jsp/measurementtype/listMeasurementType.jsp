<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Quản lý loại số đo"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Quản lý loại số đo</h2>
            <a href="<c:url value='/measurement-types/create'/>" class="btn btn-primary">+ Thêm số đo</a>
        </div>
        <form class="row g-2 mb-3" method="get">
            <div class="col-md-6">
                <input type="text" name="q" value="${param.q}" class="form-control" placeholder="Tìm kiếm...">
            </div>
            <div class="col-md-4">
                <select name="bodyPart" class="form-select">
                    <option value="">Tất cả bộ phận</option>
                    <c:forEach var="bp" items="${bodyParts}">
                        <option value="${bp}" ${bp==param.bodyPart?"selected":''}>${bp}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-secondary w-100">Lọc</button>
            </div>
        </form>
        <table class="table table-striped">
            <thead class="table-dark">
                <tr>
                    <th>STT</th>
                    <th>Tên số đo</th>
                    <th>Bộ phận</th>
                    <th>Đơn vị</th>
                    <th>Ghi chú</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="mt" items="${measurementTypes}" varStatus="st">
                    <tr>
                        <td>${st.index + 1}</td>
                        <td>${mt.name}</td>
                        <td>${mt.bodyPart}</td>
                        <td>${mt.unit}</td>
                        <td>${mt.note}</td>
                        <td>
                            <a href="<c:url value='/measurement-types/update?id=${mt.id}'/>" class="btn btn-sm btn-outline-primary">Sửa</a>
                            <a href="<c:url value='/measurement-types/delete?id=${mt.id}'/>" class="btn btn-sm btn-outline-danger" onclick="return confirm('Xóa số đo này?');">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <jsp:include page="/jsp/common/pagination.jsp"/>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>
