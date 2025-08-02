# TailorSoft COCOLAND

Đây là ứng dụng web Java đơn giản giúp quản lý các công việc may đo. Ứng dụng minh họa các thao tác CRUD cơ bản thông qua servlet, các lớp DAO và trang JSP. Toàn bộ giao diện đã được Việt hóa. Cơ sở dữ liệu mẫu nằm tại `db/cocoland_schema.sql`.

## Chức năng
- **Khách hàng** – liệt kê, thêm mới và cập nhật khách hàng
- **Kho vải** – quản lý các loại vải trong kho
- **Đơn hàng** – theo dõi danh sách và chi tiết đơn hàng
- **Số đo** – lưu trữ các số đo của khách hàng
- **Loại sản phẩm** – quản lý loại sản phẩm và các thông số đi kèm

- **Loại số đo** – quản lý các loại thông số đo (tạo, cập nhật, xóa, liệt kê)


## Xây dựng
Chạy `ant compile` để biên dịch dự án và `ant test` để chạy các kiểm thử mặc định. Điều chỉnh thông tin kết nối trong `src/java/dao/connect/DBConnect.java` cho môi trường của bạn.

