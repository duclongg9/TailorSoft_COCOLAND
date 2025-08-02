<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Lịch sử đặt may</title>
</head>
<body>
<h2>Lịch sử đặt may</h2>
<table border="1">
    <tr>
        <th>Sản phẩm</th>
        <th>Chi tiết</th>
    </tr>
    <c:forEach var="d" items="${details}">
        <tr>
            <td>${d.productType}</td>
            <td><a href="<c:url value='/orders/detail?id=${d.orderId}'/>">Xem chi tiết đơn hàng</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
