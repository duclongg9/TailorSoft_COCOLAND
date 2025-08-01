<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh sách đơn hàng</title>
</head>
<body>
<h2>Danh sách đơn hàng</h2>
<table border="1">
    <tr>
        <th>Mã</th>
        <th>Khách hàng</th>
        <th>Ngày đặt</th>
        <th>Ngày giao</th>
        <th>Trạng thái</th>
        <th>Tổng tiền</th>
        <th>Đã cọc</th>
        <th></th>
    </tr>
    <c:forEach var="o" items="${orders}">
        <tr>
            <td>${o.id}</td>
            <td>${o.customerId}</td>
            <td>${o.orderDate}</td>
            <td>${o.deliveryDate}</td>
            <td>${o.status}</td>
            <td>${o.total}</td>
            <td>${o.deposit}</td>
            <td><a href="<c:url value='/orders/detail?id=${o.id}'/>">Xem</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
