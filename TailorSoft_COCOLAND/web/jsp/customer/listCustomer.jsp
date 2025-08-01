<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Customers</title>
</head>
<body>
<h2>Customers</h2>
<a href="<c:url value='/customers/create'/>">Create Customer</a>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Phone</th>
        <th>Email</th>
        <th></th>
    </tr>
    <c:forEach var="c" items="${customers}">
        <tr>
            <td>${c.id}</td>
            <td>${c.name}</td>
            <td>${c.phone}</td>
            <td>${c.email}</td>
            <td><a href="<c:url value='/customers/update?id=${c.id}'/>">Edit</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
