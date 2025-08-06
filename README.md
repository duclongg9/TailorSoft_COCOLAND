# TailorSoft COCOLAND

Đây là ứng dụng web Java đơn giản giúp quản lý các công việc may đo. Ứng dụng minh họa các thao tác CRUD cơ bản thông qua servlet, các lớp DAO và trang JSP. Toàn bộ giao diện đã được Việt hóa. Cơ sở dữ liệu mẫu nằm tại `db/cocoland_schema.sql`.

## Chức năng
- **Khách hàng** – liệt kê, thêm mới, cập nhật và xem lịch sử đặt may theo từng sản phẩm với nút xem chi tiết đơn hàng; bảng khách hàng hiển thị thêm ngày tạo và liên kết tới lịch sử đặt may
- **Kho vải** – quản lý các loại vải trong kho
- **Đơn hàng** – theo dõi danh sách và chi tiết đơn hàng
- **Số đo** – quản lý danh mục các loại số đo (tạo, cập nhật, xóa, liệt kê); đây là danh mục định nghĩa chung không lưu số đo theo từng khách hàng
- **Loại sản phẩm** – quản lý loại sản phẩm và các thông số đi kèm
- **Loại số đo** – quản lý các loại thông số đo (tạo, cập nhật, xóa, liệt kê)

## Xây dựng
Chạy `ant compile` để biên dịch dự án và `ant test` để chạy các kiểm thử mặc định. Điều chỉnh thông tin kết nối trong `src/java/dao/connect/DBConnect.java` cho môi trường của bạn.

### Gửi thông báo đơn hàng
Ứng dụng có hỗ trợ gửi email và Zalo ZNS khi tạo đơn hàng.

#### Thiết lập Gmail
Tạo file `src/conf/email.properties` và điền thông tin đăng nhập:

```
email=duclongg9@gmail.com
password=APP_PASS
```

Nếu file này không tồn tại hoặc thiếu khóa, hệ thống sẽ bỏ qua việc gửi email và ghi log cảnh báo.

#### Thiết lập Zalo ZNS
1. Đăng ký/đăng nhập **Zalo Official Account** tại https://oa.zalo.me/manage.
2. Kích hoạt dịch vụ **Zalo Notification Service** và tạo template thông báo đơn hàng (chứa các biến `items`, `total`, `paid`, `order_date`, `appointment_date`).
3. Sau khi template được duyệt, lấy **Template ID** và **Access Token** tại phần cấu hình OA.
4. Whitelist số điện thoại nhận tin (ví dụ `0388888865` ⇒ `84388888865`).
5. Khai báo biến môi trường:
   ```bash
   export ZALO_ACCESS_TOKEN=your_OA_access_token
   export ZALO_TEMPLATE_ID=123456
   ```

Số điện thoại gửi ZNS phải ở dạng `84xxxxxxxxx`; phương thức gửi trong ứng dụng sẽ tự chuẩn hóa chuỗi nhập vào.

Ví dụ gọi trong luồng xử lý đơn hàng:
```java
NotificationService ns = new NotificationService();
ns.sendOrderEmail(customer, order, details);
ns.sendOrderZns("84388888865", order, details);
```

