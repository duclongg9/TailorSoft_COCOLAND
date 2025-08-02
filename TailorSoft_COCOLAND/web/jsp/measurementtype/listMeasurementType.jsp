<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Danh sách loại số đo</title>
</head>
<body>
<h2>Danh sách loại số đo</h2>
<a href="<c:url value='/measurement-types/create'/>">Thêm loại số đo</a>
<table border="1">
    <tr>
        <th>Mã</th>
        <th>Tên thông số</th>
        <th>Đơn vị</th>
    </tr>
    <c:forEach var="mt" items="${measurementTypes}">
        <tr>
            <td>${mt.id}</td>
            <td>${mt.name}</td>
            <td>${mt.unit}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
