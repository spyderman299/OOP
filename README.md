# Hệ thống Quản lý Sinh viên

Ứng dụng quản lý sinh viên được xây dựng bằng Java Swing với MySQL database, áp dụng kiến trúc MVC.

## Yêu cầu hệ thống

- **JDK 17** hoặc cao hơn (khuyến nghị JDK 21)
- **MySQL 5.7** hoặc cao hơn
- **Windows** (để chạy file .bat)

## Hướng dẫn Cài đặt và Chạy trên Windows

### Bước 1: Cài đặt JDK

1. Tải JDK từ [Oracle](https://www.oracle.com/java/technologies/downloads/) hoặc [OpenJDK](https://adoptium.net/)
2. Cài đặt JDK vào thư mục, ví dụ: `C:\Program Files\Java\jdk-21`

### Bước 2: Cấu hình JAVA_HOME (Tùy chọn)

**Cách 1: Đặt biến môi trường JAVA_HOME**

1. Nhấn `Windows + R`, gõ `sysdm.cpl` và Enter
2. Chọn tab **Advanced** → **Environment Variables**
3. Trong **System variables**, nhấn **New**:
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Java\jdk-21` (đường dẫn đến JDK của bạn)
4. Nhấn **OK** để lưu

**Cách 2: Sửa trực tiếp trong file .bat**

Nếu không muốn đặt biến môi trường, mở file `compile.bat` và `run.bat`, tìm dòng:
```batch
set "JAVAC_EXE=C:\Program Files\Java\jdk-21\bin\javac.exe"
```
và
```batch
set "JAVA_EXE=C:\Program Files\Java\jdk-21\bin\java.exe"
```

Thay đổi đường dẫn cho phù hợp với vị trí JDK trên máy bạn, ví dụ:
- `C:\Program Files\Java\jdk-17\bin\javac.exe`
- `C:\Program Files\Java\jdk-1.8.0_301\bin\javac.exe`

### Bước 3: Cài đặt MySQL và Tạo Database

1. Cài đặt MySQL Server
2. Tạo database:
```sql
CREATE DATABASE student_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Import dữ liệu từ file `sql.txt`:
```bash
mysql -u root -p student_management < sql.txt
```

Hoặc mở MySQL Workbench, chọn database `student_management`, rồi chạy nội dung file `sql.txt`.

### Bước 4: Cấu hình Kết nối Database

Mở file `src/main/java/config/DatabaseConnection.java` và cập nhật thông tin:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "your_password"; // Thay bằng mật khẩu MySQL của bạn
```

### Bước 5: Compile ứng dụng

Mở **Command Prompt** hoặc **PowerShell** trong thư mục project, chạy:

```batch
compile.bat
```

Hoặc double-click vào file `compile.bat`.

Nếu thành công, bạn sẽ thấy:
```
========================================
Compilation successful!
========================================
```

### Bước 6: Chạy ứng dụng

```batch
run.bat
```

Hoặc double-click vào file `run.bat`.

---

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

---

## Tính năng chính

### Dashboard (Trang chủ)
- Hiển thị thống kê: Số lượng sinh viên, giáo viên, môn học, lớp học
- TEACHER: Chỉ hiển thị thống kê liên quan đến lớp của mình

### Quản lý Sinh viên
- Xem danh sách, tìm kiếm theo tên
- Thêm, sửa, xóa sinh viên (ADMIN)
- Xem chi tiết sinh viên

### Quản lý Giáo viên (chỉ ADMIN)
- Xem danh sách giáo viên
- Thêm, sửa, xóa giáo viên

### Quản lý Môn học
- Xem danh sách môn học
- Thêm, sửa, xóa môn học (ADMIN)
- TEACHER: Chỉ xem môn học mình dạy

### Quản lý Lớp học
- Xem danh sách lớp học
- Thêm, sửa, xóa lớp học (ADMIN)
- TEACHER: Chỉ xem và sửa lớp của mình

### Quản lý Điểm số
- Nhập điểm: Chuyên cần, Bài tập, Giữa kỳ, Cuối kỳ
- Tự động tính điểm tổng kết: `0.1×Chuyên cần + 0.2×Bài tập + 0.3×Giữa kỳ + 0.4×Cuối kỳ`
- Tự động xác định kết quả: Passed (>=5.0) / Failed (<5.0)

### Quản lý Người dùng (chỉ ADMIN)
- Tạo tài khoản người dùng
- Phân quyền (ADMIN, TEACHER, STUDENT)
- Liên kết với Giáo viên hoặc Sinh viên

---

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

---

## Khắc phục sự cố

### Lỗi "javac is not recognized"
- Kiểm tra đường dẫn JAVA_HOME hoặc sửa đường dẫn trong file .bat

### Lỗi "Cannot connect to database"
- Kiểm tra MySQL Server đang chạy
- Kiểm tra username/password trong `DatabaseConnection.java`
- Kiểm tra database `student_management` đã được tạo

### Lỗi "Class not found"
- Chạy `compile.bat` trước khi chạy `run.bat`
- Kiểm tra thư mục `out/` có chứa file `.class`

### Chữ trong bảng không hiển thị
- Đã được sửa: Chữ màu đen trên nền trắng

### Icon hiển thị ô vuông
- Đã được sửa: Sử dụng text thay vì emoji

---

## Kiến trúc MVC

- **Model**: Các class đại diện cho dữ liệu (User, Student, Teacher, ...)
- **View**: Giao diện người dùng (Java Swing)
- **Controller**: Xử lý logic và điều phối giữa View và Service
- **Service**: Business logic layer
- **Repository**: Data access layer (JDBC)

---

## Tác giả

Hệ thống Quản lý Sinh viên - Java Swing Application
