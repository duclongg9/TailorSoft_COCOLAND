COCOLAND_TailorSystem/
├── src/
│   └── java/
│       ├── controller/                  # Các servlet chia theo module chức năng
│       │   ├── customer/               # Quản lý khách hàng
│       │   │   ├── CustomerListController.java
│       │   │   ├── CustomerCreateController.java
│       │   │   └── CustomerUpdateController.java
│       │   ├── order/                  # Quản lý đơn hàng may
│       │   │   ├── OrderListController.java
│       │   │   └── OrderDetailController.java
│       │   ├── material/               # Quản lý vật tư (vải, chỉ…)
│       │   │   ├── MaterialListController.java
│       │   │   └── MaterialCreateController.java
│       │   ├── measurement/            # Quản lý số đo
│       │   │   ├── MeasurementListController.java
│       │   │   └── MeasurementCreateController.java
│       │   └── staff/                  # Quản lý nhân viên/thợ may
│       │       ├── StaffListController.java
│       │       └── StaffUpdateController.java
│       ├── dao/                        # DAO chia giống controller
│       │   ├── customer/
│       │   ├── order/
│       │   ├── material/
│       │   ├── measurement/
│       │   └── staff/
│       │   └── connect/                # DBConnect.java (kết nối cơ sở dữ liệu)
│       ├── model/                      # Entity/POJO (Customer.java, Order.java, …)
│       └── util/                       # Tiện ích chung (EmailUtil, Validator, …)
│
└── web/
    ├── jsp/                            # Trang JSP chia theo module
    │   ├── customer/
    │   │   ├── listCustomer.jsp
    │   │   ├── createCustomer.jsp
    │   │   └── updateCustomer.jsp
    │   ├── order/
    │   │   ├── listOrder.jsp
    │   │   └── orderDetail.jsp
    │   ├── material/
    │   │   ├── listMaterial.jsp
    │   │   └── createMaterial.jsp
    │   ├── measurement/
    │   │   ├── listMeasurement.jsp
    │   │   └── createMeasurement.jsp
    │   ├── staff/
    │   │   ├── listStaff.jsp
    │   │   └── updateStaff.jsp
    │   └── template/                   # Header/Footer/Navbar/Sidebar
    │       ├── header.jsp
    │       ├── footer.jsp
    │       ├── navbar.jsp
    │       ├── sidebar.jsp
    │       └── login.jsp
    └── assets/                         # CSS/JS/Images dùng chung
        ├── css/
        ├── js/
        └── images/