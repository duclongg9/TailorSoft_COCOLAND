<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh sách số đo</title>
</head>
<body>
<h2>Danh sách số đo</h2>
<a href="<c:url value='/measurements/create'/>">Thêm số đo</a>
<table border="1">
    <tr>
        <th>Mã</th>
        <th>Khách hàng</th>
        <th>Loại sản phẩm</th>
        <th>Thông số</th>
        <th>Giá trị</th>
        <th>Ghi chú</th>
    </tr>
    <c:forEach var="m" items="${measurements}">
        <tr>
            <td>${m.id}</td>
            <td>${m.customerName}</td>
            <td>${m.productTypeName}</td>
            <td>${m.measurementTypeName}</td>
            <td>${m.value}</td>
            <td>${m.note}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
