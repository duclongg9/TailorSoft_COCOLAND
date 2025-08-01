<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Chi tiết đơn hàng</title>
</head>
<body>
<h2>Chi tiết đơn hàng</h2>
<c:if test="${order != null}">
    <p>Mã: ${order.id}</p>
    <p>Khách hàng: ${order.customerId}</p>
    <p>Ngày đặt: ${order.orderDate}</p>
    <p>Ngày giao: ${order.deliveryDate}</p>
    <p>Trạng thái: ${order.status}</p>
    <p>Tổng tiền: ${order.total}</p>
    <p>Đã cọc: ${order.deposit}</p>
</c:if>
</body>
</html>
