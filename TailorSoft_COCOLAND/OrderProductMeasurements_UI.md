# Wireframe: Nhập số đo theo Loại sản phẩm

Dưới đây là bố cục đề xuất cho bước "Thêm sản phẩm" trong đơn hàng. Mỗi loại sản phẩm sẽ hiển thị các trường số đo phù hợp.

## Màn hình
```
┌──────────────────────────[ STEP 2 ‑ THÊM SẢN PHẨM ]───────────────────────────┐
│  ▸ 1. Chọn khách  ▹ 2. Thêm SP  ▸ 3. Vật tư & giá  ▸ 4. Xác nhận             │
└───────────────────────────────────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────────────────────────────┐
│  Loại sản phẩm : [ Vest ▼ ]   Số lượng : [ 1 ]                               │
│                                                                              │
│  → Khi dropdown “Loại sản phẩm” đổi từ Vest → Quần (hoặc khác)               │
│    vùng “Thông tin số đo” bên dưới **tự động hiển thị đúng các trường**.     │
├───────────────────────────────────────────────────────────────────────────────┤
│  Thông tin số đo (Vest)                                                      │
│  ┌───────────────────────────────┬───────────────────────────────┐           │
│  │ Vòng cổ [____ cm]             │ Vòng ngực [____ cm]           │           │
│  ├───────────────────────────────┼───────────────────────────────┤           │
│  │ Vai      [____ cm]            │ Dài tay   [____ cm]           │           │
│  ├───────────────────────────────┼───────────────────────────────┤           │
│  │ Hạ eo    [____ cm]            │ Hạ mông   [____ cm]           │           │
│  └───────────────────────────────┴───────────────────────────────┘           │
│  (Các trường này được lấy từ cấu hình “Vest ↔ Loại số đo” đã định nghĩa)    │
│                                                                              │
│  + Thêm sản phẩm khác  |   Tiếp tục ▸                                         │
└───────────────────────────────────────────────────────────────────────────────┘
```

## Giải thích bố cục

| Khu vực | Ý nghĩa & hành vi |
| --- | --- |
| Dropdown “Loại sản phẩm” | Lấy dữ liệu `product_type`; khi thay đổi → gọi AJAX để lấy danh sách `measurement_type` đã liên kết và render vùng số đo. |
| Bảng lưới số đo | Hiển thị dạng 2 cột (hoặc 3 tùy màn hình); mỗi ô gồm Label + Input. Label lấy từ `measurement_type.name`; Input `type="number"` `step="0.1"` và placeholder cm. |
| Nhiều sản phẩm | Nếu đơn hàng có 2 Vest + 1 Quần: nhấn “+ Thêm sản phẩm khác” để nhân bản khung; mỗi khung có dropdown riêng. |
| Responsive | Trên mobile: lưới chuyển thành 1 cột dọc để dễ nhập bằng ngón tay. |
| UX detail | Tô viền đỏ nếu bỏ trống. Tooltip giải thích “Hạ eo đo từ…”. Phím Enter chuyển sang input kế tiếp. |

## Tip triển khai nhanh

- JSP/Thymeleaf: dùng `<c:forEach>` render danh sách số đo.
- Front-end: Bootstrap 5 `row` + `col-md-6` cho 2 cột; trên màn hình nhỏ tự xuống hàng.
- AJAX endpoint `/measurements/byProductType?id=…` trả JSON `[{"id":1,"name":"Vòng cổ"}, …]`.
- JS: lắng nghe phím Enter để tự chuyển focus sang ô nhập kế tiếp.
