<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh sách vải</title>
</head>
<body>
<h2>Danh sách vải</h2>
<a href="<c:url value='/materials/create'/>">Thêm vải</a>
<table border="1">
    <tr>
        <th>Mã</th>
        <th>Tên vải</th>
        <th>Màu sắc</th>
        <th>Xuất xứ</th>
        <th>Giá</th>
        <th>Số lượng</th>
    </tr>
    <c:forEach var="m" items="${materials}">
        <tr>
            <td>${m.id}</td>
            <td>${m.name}</td>
            <td>${m.color}</td>
            <td>${m.origin}</td>
            <td>${m.price}</td>
            <td>${m.quantity}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
