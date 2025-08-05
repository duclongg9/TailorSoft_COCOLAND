<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Danh sách số đo"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <div class="d-flex justify-content-between mb-3">
            <h2>Danh sách số đo</h2>
            <a class="btn btn-primary" href="<c:url value='/measurements/create'/>">Thêm số đo</a>
        </div>
        <table class="table table-striped table-hover">
            <thead class="table-dark">
            <tr>
                <th>STT</th>
                <th>Khách hàng</th>
                <th>Loại sản phẩm</th>
                <th>Thông số</th>
                <th>Giá trị</th>
                <th>Đơn vị</th>
                <th>Ghi chú</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="m" items="${measurements}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${m.customerName}</td>
                    <td>${m.productTypeName}</td>
                    <td>${m.measurementTypeName}</td>
                    <td>${m.value}</td>
                    <td>${m.unit}</td>
                    <td>${m.note}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <jsp:include page="/jsp/common/pagination.jsp"/>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>
