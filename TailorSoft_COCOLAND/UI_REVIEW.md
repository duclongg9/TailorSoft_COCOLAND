# Rà soát & đề xuất cải tiến giao diện

Mục tiêu:
- Đồng nhất UX/UI với kiến trúc đã thống nhất (sidebar/topbar, popup, selection-tag, …).
- Thân thiện người dùng, dễ nhập liệu, hiển thị rõ ràng.
- Dễ bảo trì, sẵn sàng mở rộng (chỉ hướng, không cung cấp mã nguồn).

## 1. Trang chủ / Dashboard
### Hiện tại
- Chỉ có tiêu đề “Chào mừng…” và danh sách 5 liên kết kiểu bullet.

### Đề xuất
| Hạng mục | Cải thiện |
| --- | --- |
| Bố cục | Sử dụng layout chung (header + sidebar trái). Sidebar chứa các menu chính. |
| Nội dung | Hiển thị card widget (tổng số khách, đơn đang may, tồn kho vải thấp…), link “Xem chi tiết”. |
| Giao diện | Dùng Bootstrap 5 hoặc AdminLTE: card, list-group, icon FontAwesome. |
| Trải nghiệm | Khi click menu, phần nội dung load bằng include/AJAX, không reload toàn trang. |

## 2. Danh sách Khách hàng
### Hiện tại
- Bảng HTML cơ bản, đầu bảng có link “Thêm khách hàng”.
- Cột “Mã” hiển thị ID, cột “Sửa” là link đơn thuần.

### Đề xuất
| Vấn đề | Giải pháp |
| --- | --- |
| Khó đọc/không canh | Áp dụng `table table-striped table-hover`. Thêm `thead-dark` để nổi bật tiêu đề. |
| Hành động | Bỏ chữ “Sửa” dạng text → dùng button icon nhỏ (✏️) trong cột “Actions”. Thêm trash 🗑️ nếu cần xóa. |
| Tìm kiếm nhanh | Thêm ô Search (client-side hoặc server-side) để lọc theo tên/điện thoại. |
| Thêm khách | Di chuyển “Thêm khách” sang button góc phải (Primary). Gọi popup nhập thông tin. |
| Ẩn/hiển thị ID | Nếu người dùng cuối không cần ID → ẩn cột “Mã”, chỉ hiển thị khi hover/tooltip. |

## 3. Form Cập nhật / Thêm Khách
| Vấn đề | Giải pháp |
| --- | --- |
| Form thô | Gói mỗi trường trong `<div class="mb-3">`, label đặt trên input, class `form-control`. |
| Kiểu dữ liệu | Điện thoại: `type="tel"`, Email: `type="email"`. |
| Xác thực & thông báo | Bật HTML5 validation + hiển thị toast “Cập nhật thành công”. |

## 4. Danh sách & Form Vải (Kho vải)
| Vấn đề | Giải pháp |
| --- | --- |
| Định dạng số | Format giá (1 350 000 ₫) và số lượng (120,5 m) bằng hàm hiển thị, căn phải. |
| Column width | Dùng `text-truncate` với `max-width` để màu sắc dài không bể bảng. |
| Thêm vải | Form bố trí hai cột (Tên, Màu) // (Xuất xứ, Giá, Số lượng) giúp gọn hơn. |
| Tồn kho thấp | Thêm badge/red highlight cho hàng có Số lượng < ngưỡng. |

## 5. Danh sách & Form Loại Sản phẩm
| Cần bổ sung | Đề xuất triển khai |
| --- | --- |
| Liên kết loại → số đo | Ở bảng liệt kê, thêm cột “Số đo áp dụng” (hiển thị dạng badge). |
| Form chọn số đo | Thay checkbox thô bằng Selection Tags (UI badge-toggle). |
| Lọc nhanh | Cho phép lọc theo nhóm “Thân trên / Thân dưới”. |

## 6. Đơn hàng
### Danh sách
| Vấn đề | Giải pháp |
| --- | --- |
| “Khách hàng” hiển thị ID | Hiển thị tên (tooltip: sđt). |
| Ngày, tiền tệ | Format DD‑MM‑YYYY; tiền: dấu phẩy, đơn vị “₫”. |
| Trạng thái | Badge màu: Đang tháng may (info), Hoàn thành (success)… |
| Xem chi tiết | Icon 👁 hoặc nút “Chi tiết” trong cột Actions. |

### Form Thêm đơn
| Vấn đề | Giải pháp |
| --- | --- |
| Mã khách | `Select2` search hoặc nút “Thêm khách mới” (popup). |
| Ngày đặt/ngày giao | Datepicker. |
| Trạng thái | Dropdown với danh sách trạng thái cố định. |
| Tổng tiền / Đã thanh toán | `type="number" step="1000"`; hiển thị gợi ý đơn vị VNĐ. |
| Chọn sản phẩm + số đo | Sau bước này mở wizard/tab để nhập chi tiết sản phẩm & số đo. |

### Chi tiết đơn
- Hiển thị thông tin khách, danh sách sản phẩm (Vest…), số đo và các đợt thanh toán.
- Có nút In phiếu và Cập nhật trạng thái.

## 7. Danh sách Loại Số Đo
| Mô-đun | Nội dung |
| --- | --- |
| List/CRUD | Chỉ quản lý tên số đo & ghi chú mô tả (không chứa số trị cụ thể). |
| Phân nhóm hiển thị | Ví dụ group “Thân trên”, “Thân dưới” để dễ chọn trong form loại sản phẩm. |

## 8. Phong cách & Kỹ thuật chung
| Tiêu chí | Đề xuất cụ thể |
| --- | --- |
| Framework UI | Dùng Bootstrap 5 + icons (FontAwesome), hoặc AdminLTE nếu cần sidebar sẵn. |
| Template JSP | Tách `header.jsp`, `sidebar.jsp`, `footer.jsp`; dùng `<jsp:include>` để inject. |
| Responsive | `container-fluid`, các bảng có `overflow-x:auto` trên mobile. |
| Toast/Alert | Sử dụng Bootstrap Toast cho mọi thao tác thành công/thất bại. |
| Validation | Kết hợp HTML5 + JavaScript (Parsley/JustValidate) cho front‑end; backend vẫn kiểm tra. |
| Intl Format | Hàm Java `NumberFormat`/JS `Intl.NumberFormat` cho tiền tệ, ngày tháng. |

## 9. Các bước triển khai cải tiến
1. Thiết lập layout chuẩn (header‑sidebar‑footer).
2. Áp dụng Bootstrap vào tất cả bảng & form hiện có.
3. Thêm plugin `Select2` + `Datepicker` cho ô tìm khách & ngày.
4. Tách logic `MeasurementType` / `ProductType` đúng với CSDL.
5. Hoàn thiện luồng popup tạo khách trong trang đơn hàng.
6. Bổ sung badge trạng thái, định dạng tiền tệ, số lượng.
7. Kiểm thử responsive trên mobile / tablet.

