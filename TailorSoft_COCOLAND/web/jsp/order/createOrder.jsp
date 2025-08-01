<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Thêm đơn hàng</title>
</head>
<body>
<h2>Thêm đơn hàng</h2>
<form action="" method="post">
    Mã khách hàng: <input type="text" name="customerId"/><br/>
    Ngày đặt (yyyy-MM-dd): <input type="text" name="orderDate"/><br/>
    Ngày giao (yyyy-MM-dd): <input type="text" name="deliveryDate"/><br/>
    Trạng thái: <input type="text" name="status"/><br/>
    Tổng tiền: <input type="text" name="total"/><br/>
    Đã cọc: <input type="text" name="deposit"/><br/>
    <input type="submit" value="Lưu"/>
</form>
</body>
</html>
