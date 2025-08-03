<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Danh sách loại sản phẩm"/>
<jsp:include page="/jsp/common/header.jsp"/>
<jsp:include page="/jsp/common/sidebar.jsp"/>
<div class="container-fluid">
    <div class="mt-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Danh sách loại sản phẩm</h2>
            <a href="<c:url value='/product-types/create'/>" class="btn btn-primary">Thêm loại</a>
        </div>
        <table class="table table-striped">
            <thead class="table-dark">
                <tr>
                    <th>Mã</th>
                    <th>Tên loại</th>
                    <th>Ký hiệu</th>
                    <th>Số đo áp dụng</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="pt" items="${productTypes}">
                    <tr>
                        <td>${pt.id}</td>
                        <td>${pt.name}</td>
                        <td>${pt.code}</td>
                        <td>
                            <c:forEach var="mt" items="${productMeasurements[pt.id]}">
                                <span class="badge bg-info text-dark me-1">${mt.name}</span>
                            </c:forEach>
                        </td>
                        <td><a href="<c:url value='/product-types/update?id=${pt.id}'/>" class="btn btn-sm btn-outline-primary">Sửa</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <jsp:include page="/jsp/common/pagination.jsp"/>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>
