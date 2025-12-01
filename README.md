# Hệ thống Quản lý Sinh viên

Ứng dụng quản lý sinh viên được xây dựng bằng Java Swing với MySQL database, áp dụng kiến trúc MVC.

## Yêu cầu hệ thống

- **JDK 17** hoặc cao hơn (khuyến nghị JDK 21)
- **MySQL 5.7** hoặc cao hơn
- **Windows** (để chạy file .bat)

## Tài khoản Đăng nhập Mặc định

| Role    | Username    | Password  |
|---------|-------------|-----------|
| ADMIN   | admin       | admin123  |
| TEACHER | gv001       | 123456    |
| TEACHER | gv002       | 123456    |
| STUDENT | b23dccn001  | 123456    |

---

## Phân quyền theo Role

### ADMIN (Toàn quyền)
- Quản lý tất cả Sinh viên, Giáo viên, Môn học, Lớp học
- Quản lý Điểm số tất cả các lớp
- Quản lý Người dùng (tạo/sửa/xóa tài khoản)

### TEACHER (Giáo viên)
- **Chỉ** quản lý các lớp học mà mình dạy
- **Chỉ** quản lý sinh viên thuộc các lớp của mình
- **Chỉ** quản lý môn học mà mình dạy
- **Chỉ** quản lý điểm số của sinh viên trong lớp của mình
- Không thể thêm/xóa sinh viên, lớp học, môn học (chỉ xem và sửa)

### STUDENT (Sinh viên)
- Chỉ xem điểm số của bản thân
- Không có quyền chỉnh sửa


## Cấu trúc Project

```
app_student_management/
├── compile.bat              # Script compile
├── run.bat                  # Script chạy ứng dụng
├── run-portable.bat         # Script chạy (tự tìm Java)
├── sql.txt                  # Database schema và dữ liệu mẫu
├── lib/                     # Thư viện JAR
│   └── mysql-connector-j-9.5.0.jar
├── out/                     # Class files đã compile
└── src/
    ├── Main.java            # Điểm khởi đầu
    └── main/java/
        ├── config/          # Cấu hình database
        ├── model/           # Model classes (POJO)
        ├── repository/      # Data Access Layer
        ├── service/         # Business Logic Layer
        ├── controller/      # Controller Layer
        ├── view/            # View Layer (UI)
        ├── component/       # UI Components
        └── utility/         # Helper functions
```

## Kiến trúc MVC

- **Model**: Các class đại diện cho dữ liệu (User, Student, Teacher, ...)
- **View**: Giao diện người dùng (Java Swing)
- **Controller**: Xử lý logic và điều phối giữa View và Service
- **Service**: Business logic layer
- **Repository**: Data access layer (JDBC)

---

## Link báo cáo
https://docs.google.com/document/d/1GH7De6PcWffy5DVxp2OdFJsO6HS7KWx_V8VXowVN5WQ/edit?usp=sharing



