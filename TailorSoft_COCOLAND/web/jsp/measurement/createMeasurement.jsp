<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Thêm số đo</title>
</head>
<body>
<h2>Thêm số đo</h2>
<form action="" method="get">
    Mã khách hàng: <input type="text" name="customerId" value="${param.customerId}"/><br/>
    Loại sản phẩm:
    <select name="productTypeId" onchange="this.form.submit()">
        <option value="">--Chọn loại--</option>
        <c:forEach var="pt" items="${productTypes}">
            <option value="${pt.id}" ${pt.id==selectedProductType?'selected':''}>${pt.name}</option>
        </c:forEach>
    </select>
</form>

<c:if test="${not empty measurementTypes}">
    <form action="" method="post">
        <input type="hidden" name="customerId" value="${param.customerId}"/>
        <input type="hidden" name="productTypeId" value="${selectedProductType}"/>
        <c:forEach var="mt" items="${measurementTypes}">
            <div>
                <label>${mt.name} (${mt.unit})</label>
                <input type="number" step="0.1" name="value_${mt.id}"/><br/>
                <input type="text" name="note_${mt.id}" placeholder="Ghi chú"/><br/>
            </div>
        </c:forEach>
        <input type="submit" value="Lưu"/>
    </form>
</c:if>
</body>
</html>
