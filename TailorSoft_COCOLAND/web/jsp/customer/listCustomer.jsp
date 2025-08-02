<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh sách khách hàng</title>
</head>
<body>
<h2>Danh sách khách hàng</h2>
<a href="<c:url value='/customers/create'/>">Thêm khách hàng</a>
<table border="1">
    <tr>
        <th>Mã</th>
        <th>Họ tên</th>
        <th>Số điện thoại</th>
        <th>Email</th>
        <th>Ngày tạo</th>
        <th>Lịch sử đặt may</th>
        <th>Sửa</th>
    </tr>
    <c:forEach var="c" items="${customers}">
        <tr>
            <td>${c.id}</td>
            <td>${c.name}</td>
            <td>${c.phone}</td>
            <td>${c.email}</td>
            <td>${c.createdAt}</td>
            <td><a href="<c:url value='/customers/history?id=${c.id}'/>">Xem lịch sử</a></td>
            <td><a href="<c:url value='/customers/update?id=${c.id}'/>">Sửa</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
