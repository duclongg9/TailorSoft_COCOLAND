
Thư mục này chứa mã nguồn của ứng dụng web TailorSoft COCOLAND. Các chức năng được tách thành các gói phục vụ quản lý khách hàng, kho vải, đơn hàng và số đo với các lớp DAO cùng trang JSP đơn giản. Tất cả giao diện JSP đều hiển thị tiếng Việt.

## Cấu hình gửi email

Ứng dụng sử dụng duy nhất tệp `email.properties` để lấy thông tin đăng nhập khi gửi mail. Tạo tệp `src/conf/email.properties` với nội dung:

```
email=youraddress@gmail.com
password=your-app-password
```

Tệp này cần nằm trong classpath khi build để tính năng gửi mail hoạt động.
