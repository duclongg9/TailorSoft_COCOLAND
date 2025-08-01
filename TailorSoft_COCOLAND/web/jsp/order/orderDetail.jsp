<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Order Detail</title>
</head>
<body>
<h2>Order Detail</h2>
<c:if test="${order != null}">
    <p>ID: ${order.id}</p>
    <p>Customer: ${order.customerId}</p>
    <p>Order Date: ${order.orderDate}</p>
    <p>Delivery Date: ${order.deliveryDate}</p>
    <p>Status: ${order.status}</p>
    <p>Total: ${order.total}</p>
    <p>Deposit: ${order.deposit}</p>
</c:if>
</body>
</html>
