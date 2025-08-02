<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh sách loại sản phẩm</title>
</head>
<body>
<h2>Danh sách loại sản phẩm</h2>
<a href="<c:url value='/product-types/create'/>">Thêm loại</a>
<table border="1">
    <tr>
        <th>Mã</th>
        <th>Tên loại</th>
    </tr>
    <c:forEach var="pt" items="${productTypes}">
        <tr>
            <td>${pt.id}</td>
            <td>${pt.name}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
