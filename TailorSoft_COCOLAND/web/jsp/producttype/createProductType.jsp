<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Thêm loại sản phẩm</title>
</head>
<body>
<h2>Thêm loại sản phẩm</h2>
<form action="" method="post">
    Tên loại: <input type="text" name="name"/><br/>
    <c:forEach var="mt" items="${measurementTypes}">
        <label><input type="checkbox" name="measurementTypeId" value="${mt.id}"/>${mt.name}</label><br/>
    </c:forEach>
    <input type="submit" value="Lưu"/>
</form>
</body>
</html>
