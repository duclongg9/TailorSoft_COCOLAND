<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Measurements</title>
</head>
<body>
<h2>Measurements</h2>
<a href="<c:url value='/measurements/create'/>">Create Measurement</a>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Customer</th>
        <th>Product Type</th>
        <th>Measurement Type</th>
        <th>Value</th>
        <th>Note</th>
    </tr>
    <c:forEach var="m" items="${measurements}">
        <tr>
            <td>${m.id}</td>
            <td>${m.customerId}</td>
            <td>${m.productTypeId}</td>
            <td>${m.measurementTypeId}</td>
            <td>${m.value}</td>
            <td>${m.note}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
