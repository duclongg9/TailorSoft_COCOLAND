# TailorSoft COCOLAND - Hệ thống Quản lý May mặc 🚀

Dự án quản lý cửa hàng may mặc tích hợp Backend Java (Spring Boot) và Frontend React. Tài liệu này hướng dẫn bạn cách chạy local và triển khai (deploy) toàn bộ hệ thống lên **Render** (Miễn phí).

---

## 📁 Cấu trúc dự án
- `/backend`: API Server (Spring Boot + JPA).
- `/frontend`: Giao diện người dùng (React + Vite).

---

## 💻 1. Hướng dẫn chạy Local (Máy cá nhân)

### Yêu cầu:
- JDK 17+
- Node.js 18+
- MySQL Server

### Các bước thực hiện:
1. **Database**: Tạo database tên `cocoland_schema` trong MySQL.
2. **Backend**:
   - Truy cập thư mục `backend`.
   - Chạy: `./mvnw spring-boot:run`
   - API sẽ chạy tại: `http://localhost:8080`
3. **Frontend**:
   - Truy cập thư mục `frontend`.
   - Chạy: `npm install` rồi `npm run dev`
   - Giao diện sẽ chạy tại: `http://localhost:5173`

---

## 🌐 2. Hướng dẫn Deploy lên Render (Tất cả trong một)

Bạn sẽ tạo 3 thành phần trên Render theo thứ tự sau:

### Bước 2.1: Tạo Database (PostgreSQL)
1. Trên Dashboard Render, chọn **New** -> **PostgreSQL**.
2. Đặt tên (ví dụ: `cocoland-db`). Chọn gói **Free**.
3. Sau khi tạo xong, copy dòng **Internal Database URL** (dùng cho backend).

### Bước 2.2: Deploy Backend (Web Service)
1. Chọn **New** -> **Web Service**. Kết nối với Repo Github của bạn.
2. Cấu hình:
   - **Name**: `cocoland-backend`
   - **Root Directory**: `backend`
   - **Runtime**: `Docker` (Nếu có Dockerfile) HOẶC **Java**
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/cocoland-backend-1.0.0.jar`
3. Thêm **Environment Variables**:
   - `SPRING_DATASOURCE_URL`: (Dán Internal Database URL từ bước 2.1)
   - `SPRING_DATASOURCE_USERNAME`: (Username của DB bước 2.1)
   - `SPRING_DATASOURCE_PASSWORD`: (Password của DB bước 2.1)
   - `GMAIL_USER`: (Email của bạn)
   - `GMAIL_PASS`: (Mật khẩu ứng dụng Gmail)

### Bước 2.3: Deploy Frontend (Static Site)
1. Chọn **New** -> **Static Site**. Kết nối với Repo Github.
2. Cấu hình:
   - **Name**: `cocoland-frontend`
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Publish Directory**: `dist`
3. Thêm **Environment Variables**:
   - `VITE_API_URL`: `https://cocoland-backend.onrender.com/api` (Thay bằng URL backend của bạn)
   - `VITE_BASE_URL`: `https://cocoland-backend.onrender.com`

---

## 🔧 Phụ lục: Lưu ý về ảnh (Uploads)
Vì Render là môi trường tạm thời, ảnh bạn upload lên sẽ mất khi server restart. 
- **Giải pháp tạm thời**: Chỉ dùng cho demo.
- **Giải pháp lâu dài**: Đăng ký dịch vụ **Cloudinary** (miễn phí) để lưu trữ ảnh vĩnh viễn.

---
*Chúc bạn triển khai dự án thành công!*
