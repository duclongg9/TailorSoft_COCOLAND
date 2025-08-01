<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cập nhật khách hàng</title>
</head>
<body>
<h2>Cập nhật khách hàng</h2>
<form action="" method="post">
    <input type="hidden" name="id" value="${customer.id}"/>
    Họ tên: <input type="text" name="name" value="${customer.name}"/><br/>
    Số điện thoại: <input type="text" name="phone" value="${customer.phone}"/><br/>
    Email: <input type="text" name="email" value="${customer.email}"/><br/>
    <input type="submit" value="Lưu"/>
</form>
</body>
</html>
