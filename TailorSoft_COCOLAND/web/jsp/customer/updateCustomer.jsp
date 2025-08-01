<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update Customer</title>
</head>
<body>
<h2>Update Customer</h2>
<form action="" method="post">
    <input type="hidden" name="id" value="${customer.id}"/>
    Name: <input type="text" name="name" value="${customer.name}"/><br/>
    Phone: <input type="text" name="phone" value="${customer.phone}"/><br/>
    Email: <input type="text" name="email" value="${customer.email}"/><br/>
    <input type="submit" value="Save"/>
</form>
</body>
</html>
