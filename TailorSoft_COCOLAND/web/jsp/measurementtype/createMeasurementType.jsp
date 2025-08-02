<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Thêm loại số đo</title>
</head>
<body>
<h2>Thêm loại số đo</h2>
<form action="<c:url value='/measurement-types/create'/>" method="post">
    Tên thông số: <input type="text" name="name"/><br/>
    Đơn vị: <input type="text" name="unit" value="cm"/><br/>
    <input type="submit" value="Lưu"/>
</form>
</body>
</html>
