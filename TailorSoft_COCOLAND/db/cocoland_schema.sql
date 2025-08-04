
DROP DATABASE IF EXISTS cocoland_schema;
CREATE DATABASE cocoland_schema CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cocoland_schema;

-- BẢNG LOẠI SẢN PHẨM
CREATE TABLE loai_san_pham (
    ma_loai INT PRIMARY KEY AUTO_INCREMENT,
    ten_loai VARCHAR(100) NOT NULL,
    ky_hieu VARCHAR(50)
);

-- BẢNG LOẠI THÔNG SỐ
CREATE TABLE loai_thong_so (
    ma_thong_so INT PRIMARY KEY AUTO_INCREMENT,
    ten_thong_so VARCHAR(100) NOT NULL,
    don_vi VARCHAR(10) DEFAULT 'cm',
    bo_phan VARCHAR(50),
    ghi_chu TEXT
);

-- QUAN HỆ LOẠI SẢN PHẨM – THÔNG SỐ
CREATE TABLE loai_sp_thong_so (
    ma_loai INT,
    ma_thong_so INT,
    PRIMARY KEY (ma_loai, ma_thong_so),
    FOREIGN KEY (ma_loai) REFERENCES loai_san_pham(ma_loai),
    FOREIGN KEY (ma_thong_so) REFERENCES loai_thong_so(ma_thong_so)
);

-- KHÁCH HÀNG
CREATE TABLE khach_hang (
    ma_khach INT PRIMARY KEY AUTO_INCREMENT,
    ho_ten VARCHAR(100),
    so_dien_thoai VARCHAR(20),
    email VARCHAR(100),
    dia_chi TEXT,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SỐ ĐO
CREATE TABLE thong_so_do (
    ma_do INT PRIMARY KEY AUTO_INCREMENT,
    ma_khach INT,
    ma_loai INT,
    ma_thong_so INT,
    gia_tri FLOAT,
    ghi_chu TEXT,
    FOREIGN KEY (ma_khach) REFERENCES khach_hang(ma_khach),
    FOREIGN KEY (ma_loai) REFERENCES loai_san_pham(ma_loai),
    FOREIGN KEY (ma_thong_so) REFERENCES loai_thong_so(ma_thong_so)
);

-- ĐƠN HÀNG
CREATE TABLE don_hang (
    ma_don INT PRIMARY KEY AUTO_INCREMENT,
    ma_khach INT,
    ngay_dat DATE,
    ngay_giao DATE,
    trang_thai VARCHAR(30) CHECK (trang_thai IN ('Dang may', 'Hoan thanh', 'Don huy')),
    tong_tien DECIMAL(12,2),
    da_coc DECIMAL(12,2),
    FOREIGN KEY (ma_khach) REFERENCES khach_hang(ma_khach)
);

-- CHI TIẾT ĐƠN
CREATE TABLE chi_tiet_don (
    ma_ct INT PRIMARY KEY AUTO_INCREMENT,
    ma_don INT,
    loai_sp VARCHAR(50),
    ten_vai VARCHAR(100),
    don_gia DECIMAL(10,2),
    so_luong INT,
    ghi_chu TEXT,
    FOREIGN KEY (ma_don) REFERENCES don_hang(ma_don)
);

-- KHO VẢI MỞ RỘNG
CREATE TABLE kho_vai (
    ma_vai INT PRIMARY KEY AUTO_INCREMENT,
    ten_vai VARCHAR(100),
    mau_sac VARCHAR(50),
    quyen_vai VARCHAR(100),
    xuat_xu VARCHAR(100),
    gia_thanh DECIMAL(12,2), -- VND per meter
    so_luong FLOAT, -- meters remaining
    don_vi VARCHAR(10) DEFAULT 'm',
    hinh_hoa_don VARCHAR(255) -- path to image file
);

-- DỮ LIỆU MẪU

-- 1. Loại sản phẩm
INSERT INTO loai_san_pham (ten_loai, ky_hieu) VALUES
('Vest','V'), ('Áo sơ mi','ASM'), ('Quần âu','QA');

-- 2. Loại thông số
INSERT INTO loai_thong_so (ten_thong_so, bo_phan) VALUES
('Vòng cổ','Thân trên'),
('Dài tay','Tay'),
('Vai','Thân trên'),
('Ngực','Thân trên'),
('Eo','Thân dưới'),
('Mông','Thân dưới'),
('Ống quần','Thân dưới'),
('Dài quần','Thân dưới');

-- 3. Ánh xạ loại sản phẩm - thông số
INSERT INTO loai_sp_thong_so VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),
(2,1),(2,2),(2,3),(2,4),
(3,5),(3,6),(3,7),(3,8);

-- 4. Khách hàng
INSERT INTO khach_hang (ho_ten, so_dien_thoai, email, dia_chi) VALUES
('Nguyen Van A', '0909123456', 'a@example.com', '123 Lê Lợi'),
('Tran Thi B', '0911222333', 'b@example.com', '45 Pasteur');

-- 5. Kho vải
INSERT INTO kho_vai (ten_vai, mau_sac, quyen_vai, xuat_xu, gia_thanh, so_luong, hinh_hoa_don) VALUES
('Kate silk', 'Xanh navy', 'Quyển 1', 'Ý', 215000, 120.5, 'hoa_don_01.jpg'),
('Cotton cao cấp', 'Trắng', 'Quyển 3', 'Việt Nam', 135000, 200, 'hoa_don_02.jpg');

-- 6. Đơn hàng
INSERT INTO don_hang (ma_khach, ngay_dat, ngay_giao, trang_thai, tong_tien, da_coc) VALUES
(1, '2025-08-01', '2025-08-10', 'Dang may', 2500000, 1000000);

-- 7. Chi tiết đơn hàng
INSERT INTO chi_tiet_don (ma_don, loai_sp, ten_vai, don_gia, so_luong, ghi_chu) VALUES
(1, 'Vest nam', 'Kate silk', 1250000, 1, 'May theo form slim fit');

-- 8. Thông số đo thực tế của khách
INSERT INTO thong_so_do (ma_khach, ma_loai, ma_thong_so, gia_tri) VALUES
(1, 1, 1, 38.5),
(1, 1, 2, 60.0),
(1, 1, 3, 44.0);