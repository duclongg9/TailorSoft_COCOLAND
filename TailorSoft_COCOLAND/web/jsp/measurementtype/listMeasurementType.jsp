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
        <th>Sửa</th>
        <th>Xóa</th>
    </tr>
    <c:forEach var="mt" items="${measurementTypes}">
        <tr>
            <td>${mt.id}</td>
            <td>${mt.name}</td>
            <td>${mt.unit}</td>
            <td><a href="<c:url value='/measurement-types/update?id=${mt.id}'/>">Sửa</a></td>
            <td><a href="<c:url value='/measurement-types/delete?id=${mt.id}'/>" onclick="return confirm('Bạn có chắc muốn xóa?');">Xóa</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
