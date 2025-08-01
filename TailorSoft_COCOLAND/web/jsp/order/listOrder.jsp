<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Orders</title>
</head>
<body>
<h2>Orders</h2>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Customer</th>
        <th>Order Date</th>
        <th>Delivery Date</th>
        <th>Status</th>
        <th>Total</th>
        <th>Deposit</th>
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
            <td><a href="<c:url value='/orders/detail?id=${o.id}'/>">View</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
