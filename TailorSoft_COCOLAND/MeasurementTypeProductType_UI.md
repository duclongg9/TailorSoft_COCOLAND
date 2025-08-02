# Wireframes Quản lý Loại Số Đo & Loại Sản Phẩm May

Dưới đây là hai mẫu wire-frame (ASCII-UI) minh họa cho các màn hình quản lý loại số đo và thêm loại sản phẩm may kèm gán các số đo liên quan.

## A. Khởi tạo & quản lý Loại số đo (Measurement Type)
```
┌─────────────────────────────[ QUẢN LÝ LOẠI SỐ ĐO ]────────────────────────────┐
│  ▸ Danh sách •  Thêm mới •  Sửa                                               │
├───────────────────────────────────────────────────────────────────────────────┤
│  Tìm kiếm: [______________________]  Bộ phận: [▼ Tất cả ]  + Thêm số đo       │
│                                                                               │
│  ┌───────────┬──────────────┬────────────┬───────────────────────────────┐    │
│  │  #        │  Tên số đo   │  Bộ phận   │  Ghi chú                      │    │
│  ├───────────┼──────────────┼────────────┼───────────────────────────────┤    │
│  │  1        │  Vòng cổ     │  Thân trên │  …                             │    │
│  │  2        │  Dài tay     │  Tay áo    │  …                             │    │
│  │  3        │  Vòng eo     │  Thân dưới │  …                             │    │
│  │  …        │              │            │                                │    │
│  └───────────┴──────────────┴────────────┴───────────────────────────────┘    │
└───────────────────────────────────────────────────────────────────────────────┘

            ⇣ Khi nhấn “+ Thêm số đo” (hoặc Sửa dòng) xuất hiện Modal ⇣

┌───────────────────────[ POPUP • THÊM / SỬA LOẠI SỐ ĐO ]───────────────────────┐
│  Tên số đo      : [_____________________________]                             │
│  Bộ phận cơ thể : [ Thân trên ▼ ]    (dropdown group: Thân trên, Tay, Chân…) │
│  Mô tả / Ghi chú : [ _________________________________________________ ]      │
│                                                                               │
│                         ( Hủy )                (  Lưu  )                       │
└───────────────────────────────────────────────────────────────────────────────┘
```

**Ý chính**

- Measurement Type độc lập: chỉ lưu tên + nhóm bộ phận.
- Một vị trí đo (ví dụ “Vòng cổ”) dùng chung cho nhiều loại sản phẩm.
- Tìm kiếm & filter nhanh theo nhóm bộ phận để dễ chọn khi gán cho sản phẩm.

## B. Thêm Loại sản phẩm may (Product Type) & gắn số đo
```
┌─────────────────────────[ THÊM LOẠI SẢN PHẨM MAY ]────────────────────────────┐
│  Tên loại sản phẩm :  [_____________________________________]                │
│  Mã / Ký hiệu (tuỳ) :  [________]                                             │
│                                                                               │
│  Chọn các LOẠI SỐ ĐO áp dụng  (có thể tick nhiều – data từ bảng A)           │
│  ┌───────────────────────────────────────────────────────────────────────┐     │
│  │  Bộ phận: [▼ Tất cả]   🔍 Tìm số đo…  ___________________________     │     │
│  │                                                                       │     │
│  │   (dạng “Selection Tags” – click để bật/tắt)                          │     │
│  │   ┏━━━━━━━━━┓  ┏━━━━━━━━━┓  ┏━━━━━━━━┓  ┏━━━━━━━┓                    │     │
│  │   ┃ Vòng cổ ┃  ┃ Vòng ngực┃  ┃ Vai   ┃  ┃ Dài tay┃ …                  │     │
│  │   ┗━━━━━━━━━┛  ┗━━━━━━━━━┛  ┗━━━━━━━━┛  ┗━━━━━━━┛                    │     │
│  │   (Tag được chọn đổi màu/hilight)                                      │     │
│  └───────────────────────────────────────────────────────────────────────┘     │
│                                                                               │
│  Số đo đã chọn:  [ Vòng cổ ] [ Vai ] [ Dài tay ] …  (hiển thị tag nhỏ)        │
│                                                                               │
│                          ( Hủy )                 (  Lưu loại sản phẩm )        │
└───────────────────────────────────────────────────────────────────────────────┘
```

**Luồng dữ liệu**

1. User tạo Measurement Type (Popup ở phần A) → ghi vào bảng `measurement_type`.
2. Khi thêm Product Type, hệ thống lấy danh sách `measurement_type` để render tag-list.
3. Sau khi Lưu, ghi quan hệ N‑N vào bảng `product_measurement` (`product_type_id`, `measurement_type_id`).
4. Trong form Nhập số đo (giai đoạn Order), chọn “Vest” ⇒ truy vấn bảng `product_measurement` ⇒ hiển thị đúng trường.

**Lời khuyên triển khai nhanh**

| Thành phần          | Thư viện / kỹ thuật gợi ý                                            |
|---------------------|-----------------------------------------------------------------------|
| Tag-selector        | `bootstrap-tagsinput` hoặc tự dùng badge + JS toggle.                 |
| Filter bộ phận      | Dropdown + JS lọc tag (`data-group="upper-body"`).                  |
| Modal CRUD          | Bootstrap Modal; validate tên trùng lặp.                              |
| Persist N‑N         | Transaction: insert `product_type` → bulk-insert mapping.             |

