<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Cập nhật loại số đo</title>
</head>
<body>
<h2>Cập nhật loại số đo</h2>
<form action="<c:url value='/measurement-types/update'/>" method="post">
    <input type="hidden" name="id" value="${measurementType.id}"/>
    Tên thông số: <input type="text" name="name" value="${measurementType.name}"/><br/>
    Đơn vị: <input type="text" name="unit" value="${measurementType.unit}"/><br/>
    <input type="submit" value="Cập nhật"/>
</form>
</body>
</html>
