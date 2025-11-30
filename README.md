# OOP
Hệ thống Quản lý Sinh viên - Java Web Application

Hệ thống “Quản lý sinh viên” được xây dựng nhằm hỗ trợ tự động hóa các nghiệp vụ trong môi trường đào tạo, bao gồm: lưu trữ thông tin sinh viên, giáo viên, môn học, lớp học phần và quản lý điểm số. Ứng dụng được phát triển dưới dạng web, sử dụng ngôn ngữ Java với mô hình Servlet/JSP, chạy trên Apache Tomcat và kết nối cơ sở dữ liệu MySQL thông qua thư viện JDBC.
Dự án cho phép nhiều loại người dùng đăng nhập như Admin – Giáo viên – Sinh viên, mỗi vai trò sẽ có quyền hạn và giao diện tương ứng. Hệ thống đảm bảo tính chính xác, toàn vẹn dữ liệu, dễ sử dụng và dễ mở rộng.

## Tổng quan

Hệ thống quản lý sinh viên là một ứng dụng web đầy đủ tính năng cho phép:
- Quản lý thông tin sinh viên, giáo viên, môn học và lớp học
- Nhập và quản lý điểm số với công thức tính tự động
- Phân quyền truy cập dựa trên vai trò (ADMIN, TEACHER, STUDENT)
- Dashboard với thống kê và biểu đồ trực quan
- Xử lý exception và logging đầy đủ

##  Công nghệ sử dụng
Dự án được xây dựng bằng Java Web thuần sử dụng Servlet/JSP chạy trên Apache Tomcat, kết nối cơ sở dữ liệu MySQL thông qua JDBC (MySQL Connector/J), không sử dụng các framework như Spring hay Hibernate. Giao diện được hiện thực bằng JSP, HTML, CSS, JavaScript và một số thư viện JS (Chart.js) để trực quan hóa dữ liệu.
- **Java**: JDK 21
- **Web Server**: Apache Tomcat 10.1.49
- **Database**: MySQL với charset UTF-8
- **Frontend**: JSP, HTML, CSS, JavaScript, Chart.js
- **Backend**: Java Servlets, JSP, JSTL
- **Database Driver**: MySQL Connector/J 8.0.33
- **Build Tool**: Batch scripts (Windows)

## Cấu trúc dự án

```
────main/
│       ├── java/
│       │   └── com/
│       │       └── studentmanagement/
│       │           ├── config/
│       │           │   └── DatabaseConnection.java      # Singleton pattern cho DB connection
│       │           ├── model/                           # Entity classes
│       │           │   ├── Person.java                  # Abstract class
│       │           │   ├── Student.java                 # extends Person
│       │           │   ├── Teacher.java                 # extends Person
│       │           │   ├── Subject.java
│       │           │   ├── Homeroom.java
│       │           │   ├── Enrollment.java
│       │           │   └── User.java
│       │           ├── dao/                             # Data Access Layer
│       │           │   ├── BaseDAO.java                 # Abstract generic DAO
│       │           │   ├── StudentDAO.java
│       │           │   ├── TeacherDAO.java
│       │           │   ├── SubjectDAO.java
│       │           │   ├── HomeroomDAO.java
│       │           │   ├── EnrollmentDAO.java
│       │           │   └── UserDAO.java
│       │           ├── service/                         # Business Logic Layer
│       │           │   ├── IStudentService.java         # Interface
│       │           │   ├── StudentService.java
│       │           │   └── AuthenticationService.java
│       │           ├── servlet/                         # Controllers
│       │           │   ├── LoginServlet.java
│       │           │   ├── LogoutServlet.java
│       │           │   ├── DashboardServlet.java
│       │           │   ├── StudentServlet.java
│       │           │   ├── TeacherServlet.java
│       │           │   ├── SubjectServlet.java
│       │           │   ├── ClassServlet.java
│       │           │   ├── EnrollmentServlet.java
│       │           │   └── UserServlet.java
│       │           ├── filter/                          # Servlet Filters
│       │           │   ├── CharacterEncodingFilter.java
│       │           │   └── AuthenticationFilter.java
│       │           ├── exception/                       # Custom Exceptions
│       │           │   ├── DatabaseException.java
│       │           │   ├── ValidationException.java
│       │           │   └── FileException.java
│       │           └── util/                            # Utilities
│       │               ├── FileUtil.java
│       │               └── LoggerUtil.java
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml                              # Servlet configuration
│           │   ├── lib/                                 # JAR dependencies
│           │   │   ├── mysql-connector-j-8.0.33.jar
│           │   │   ├── jakarta.servlet.jsp.jstl-api-2.0.0.jar
│           │   │   └── jakarta.servlet.jsp.jstl-2.0.0.jar
│           │   └── classes/                             # Compiled classes
│           ├── css/
│           │   └── style.css                            # Stylesheet
│           ├── js/
│           │   └── main.js                              # JavaScript utilities
│           ├── pages/
│           │   ├── login.jsp
│           │   ├── dashboard.jsp                        # Dashboard với biểu đồ
│           │   ├── components/                          # Reusable components
│           │   │   ├── header.jsp
│           │   │   ├── sidebar.jsp
│           │   │   └── footer.jsp
│           │   ├── student/                             # Student management pages
│           │   │   ├── list.jsp
│           │   │   ├── form.jsp
│           │   │   └── detail.jsp
│           │   ├── teacher/                             # Teacher management pages
│           │   ├── subject/                             # Subject management pages
│           │   ├── class/                               # Class management pages
│           │   ├── enrollment/                          # Enrollment management pages
│           │   ├── user/                                # User management pages
│           │   └── error/                               # Error pages
│           │       ├── 404.jsp
│           │       └── 500.jsp
│           └── index.jsp                                # Redirect to login                                
├── sql.txt                                              # Database schema and data
└── README.md                                            # This file
```

### Tài khoản đăng nhập mặc định

| Username      | Password  | Role    | Mô tả             |
|---------------|-----------|---------|-------------------|
| admin         | admin123  | ADMIN   | Quản trị viên     |
| gv001         | 123456    | TEACHER | Giáo viên         |
| b23dccn001    | 123456    | STUDENT | Sinh viên         |

**Cập nhật lần cuối**: 30/11/2025

### Link báo cáo 
https://docs.google.com/document/d/1GH7De6PcWffy5DVxp2OdFJsO6HS7KWx_V8VXowVN5WQ/edit?usp=sharing
