<<<<<<< HEAD
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
=======
```
COCOLAND_TailorSystem/
├── src/
│   └── java/
│       ├── controller/                        # Các servlet chia theo module chức năng
│       │   ├── customer/                      # Quản lý khách hàng
│       │   │   ├── CustomerListController.java
│       │   │   ├── CustomerCreateController.java
│       │   │   └── CustomerUpdateController.java
│       │   ├── order/                         # Quản lý đơn hàng may
│       │   │   ├── OrderListController.java
│       │   │   └── OrderDetailController.java
│       │   ├── material/                      # Quản lý vật tư (vải, chỉ…)
│       │   │   ├── MaterialListController.java
│       │   │   └── MaterialCreateController.java
│       │   ├── measurement/                   # Quản lý số đo
│       │   │   ├── MeasurementListController.java
│       │   │   └── MeasurementCreateController.java
│       │   └── staff/                         # Quản lý nhân viên/thợ may
│       │       ├── StaffListController.java
│       │       └── StaffUpdateController.java
│       ├── dao/                               # DAO chia giống controller
>>>>>>> e92c705 (.)
│       │   ├── customer/
│       │   ├── order/
│       │   ├── material/
│       │   ├── measurement/
<<<<<<< HEAD
│       │   └── staff/
│       │   └── connect/                # DBConnect.java (kết nối cơ sở dữ liệu)
│       ├── model/                      # Entity/POJO (Customer.java, Order.java, …)
│       └── util/                       # Tiện ích chung (EmailUtil, Validator, …)
│
└── web/
    ├── jsp/                            # Trang JSP chia theo module
=======
│       │   ├── staff/
│       │   └── connect/                       # DBConnect.java (kết nối cơ sở dữ liệu)
│       ├── model/                             # Entity/POJO (Customer.java, Order.java,…)
│       └── util/                              # Tiện ích chung (EmailUtil, Validator,…)
│
└── web/
    ├── jsp/                                   # Trang JSP chia theo module
>>>>>>> e92c705 (.)
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
<<<<<<< HEAD
    │   └── template/                   # Header/Footer/Navbar/Sidebar
=======
    │   └── template/                          # Header/Footer/Navbar/Sidebar/Login
>>>>>>> e92c705 (.)
    │       ├── header.jsp
    │       ├── footer.jsp
    │       ├── navbar.jsp
    │       ├── sidebar.jsp
    │       └── login.jsp
<<<<<<< HEAD
    └── assets/                         # CSS/JS/Images dùng chung
        ├── css/
        ├── js/
        └── images/
=======
    └── assets/                                # CSS/JS/Images dùng chung
        ├── css/
        ├── js/
        └── images/
```
>>>>>>> e92c705 (.)
