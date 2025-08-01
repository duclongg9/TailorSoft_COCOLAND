<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Materials</title>
</head>
<body>
<h2>Materials</h2>
<a href="<c:url value='/materials/create'/>">Create Material</a>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Color</th>
        <th>Origin</th>
        <th>Price</th>
        <th>Quantity</th>
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
