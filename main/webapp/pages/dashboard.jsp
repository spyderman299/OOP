<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ - Hệ thống Quản lý Sinh viên</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
</head>
<body>
    <jsp:include page="components/header.jsp" />
    
    <jsp:include page="components/sidebar.jsp" />
    
    <div class="main-content">
        <h1 class="page-title">Trang chủ</h1>
        
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                ${successMessage}
                <c:remove var="successMessage" scope="session" />
            </div>
        </c:if>
        
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                ${errorMessage}
                <c:remove var="errorMessage" scope="session" />
            </div>
        </c:if>
        
        <div class="dashboard-cards">
            <div class="stat-card card-blue">
                <div class="stat-card-header">
                    <span class="stat-card-title">Sinh viên</span>
                    <div class="stat-card-icon">👥</div>
                </div>
                <div class="stat-card-number" data-count="${studentCount}">${not empty studentCount ? studentCount : '---'}</div>
                <div class="stat-card-footer">
                    <span class="stat-card-description">Tổng số sinh viên</span>
                    <a href="${pageContext.request.contextPath}/students" class="stat-card-btn">Chi tiết</a>
                </div>
            </div>
            
            <div class="stat-card card-green">
                <div class="stat-card-header">
                    <span class="stat-card-title">Lớp học</span>
                    <div class="stat-card-icon">📖</div>
                </div>
                <div class="stat-card-number" data-count="${classCount}">${not empty classCount ? classCount : '---'}</div>
                <div class="stat-card-footer">
                    <span class="stat-card-description">Tổng số lớp học</span>
                    <a href="${pageContext.request.contextPath}/classes" class="stat-card-btn">Chi tiết</a>
                </div>
            </div>
            
            <div class="stat-card card-purple">
                <div class="stat-card-header">
                    <span class="stat-card-title">Môn học</span>
                    <div class="stat-card-icon">📚</div>
                </div>
                <div class="stat-card-number" data-count="${subjectCount}">${not empty subjectCount ? subjectCount : '---'}</div>
                <div class="stat-card-footer">
                    <span class="stat-card-description">Tổng số môn học</span>
                    <a href="${pageContext.request.contextPath}/subjects" class="stat-card-btn">Chi tiết</a>
                </div>
            </div>
            
            <div class="stat-card card-blue">
                <div class="stat-card-header">
                    <span class="stat-card-title">Giáo viên</span>
                    <div class="stat-card-icon">👨‍🏫</div>
                </div>
                <div class="stat-card-number" data-count="${teacherCount}">${not empty teacherCount ? teacherCount : '---'}</div>
                <div class="stat-card-footer">
                    <span class="stat-card-description">Tổng số giáo viên</span>
                    <a href="${pageContext.request.contextPath}/teachers" class="stat-card-btn">Chi tiết</a>
                </div>
            </div>
        </div>
        
        <!-- Charts Section -->
        <div class="charts-container">
            <!-- Chart 1: Số lượng sinh viên theo môn học -->
            <div class="chart-card">
                <h3>Số lượng Sinh viên theo Môn học</h3>
                <canvas id="studentsBySubjectChart"></canvas>
            </div>
            
            <!-- Chart 2: Tỷ lệ Đạt/Không đạt -->
            <div class="chart-card">
                <h3>Tỷ lệ Đạt/Không đạt</h3>
                <canvas id="passFailChart"></canvas>
            </div>
        </div>
        
        <div class="stat-card" style="margin-top: 2rem;">
            <h3>Chào mừng, ${user.username}!</h3>
            <p><strong>Vai trò:</strong> ${user.role}</p>
            <p style="margin-top: 1rem; color: #7f8c8d;">
                Hệ thống quản lý sinh viên giúp bạn quản lý thông tin sinh viên, lớp học, và điểm số một cách hiệu quả.
            </p>
        </div>
    </div>
    
    <jsp:include page="components/footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script>
        // Animate numbers on load
        document.addEventListener('DOMContentLoaded', function() {
            const numberElements = document.querySelectorAll('.stat-card-number[data-count]');
            numberElements.forEach(element => {
                const target = parseInt(element.getAttribute('data-count'));
                if (target > 0 && !isNaN(target)) {
                    element.textContent = '0';
                    setTimeout(() => {
                        let current = 0;
                        const increment = target / 30;
                        const timer = setInterval(() => {
                            current += increment;
                            if (current >= target) {
                                element.textContent = target;
                                clearInterval(timer);
                            } else {
                                element.textContent = Math.floor(current);
                            }
                        }, 30);
                    }, 500);
                }
            });
            
            // Initialize Charts
            initCharts();
        });
        
        function initCharts() {
            // Chart 1: Số lượng sinh viên theo môn học (Bar Chart)
            const studentsBySubjectCtx = document.getElementById('studentsBySubjectChart');
            if (studentsBySubjectCtx) {
                const subjectNames = [];
                const studentCounts = [];
                <c:if test="${not empty studentsBySubject}">
                    <c:forEach var="entry" items="${studentsBySubject}">
                        subjectNames.push('<c:out value="${entry.key}" escapeXml="true" />');
                        studentCounts.push(<c:out value="${entry.value}" />);
                    </c:forEach>
                </c:if>
                
                // Only create chart if there's data
                if (subjectNames.length > 0) {
                    new Chart(studentsBySubjectCtx, {
                        type: 'bar',
                        data: {
                            labels: subjectNames,
                            datasets: [{
                                label: 'Số lượng Sinh viên',
                                data: studentCounts,
                                backgroundColor: [
                                'rgba(52, 152, 219, 0.8)',
                                'rgba(46, 204, 113, 0.8)',
                                'rgba(241, 196, 15, 0.8)',
                                'rgba(231, 76, 60, 0.8)',
                                'rgba(155, 89, 182, 0.8)',
                                'rgba(26, 188, 156, 0.8)'
                            ],
                            borderColor: [
                                'rgba(52, 152, 219, 1)',
                                'rgba(46, 204, 113, 1)',
                                'rgba(241, 196, 15, 1)',
                                'rgba(231, 76, 60, 1)',
                                'rgba(155, 89, 182, 1)',
                                'rgba(26, 188, 156, 1)'
                            ],
                            borderWidth: 2,
                            borderRadius: 8
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: true,
                        plugins: {
                            legend: {
                                display: true,
                                position: 'top'
                            },
                            tooltip: {
                                backgroundColor: 'rgba(0, 0, 0, 0.8)',
                                padding: 12,
                                titleFont: { size: 14, weight: 'bold' },
                                bodyFont: { size: 13 }
                            }
                        },
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: {
                                    stepSize: 1,
                                    precision: 0
                                }
                            }
                        }
                    }
                    });
                } else {
                    studentsBySubjectCtx.parentElement.innerHTML = '<p style="text-align: center; color: #7f8c8d; padding: 2rem;">Chưa có dữ liệu để hiển thị</p>';
                }
            }
            
            // Chart 2: Tỷ lệ Đạt/Không đạt (Pie Chart)
            const passFailCtx = document.getElementById('passFailChart');
            if (passFailCtx) {
                const labels = [];
                const data = [];
                const colors = [];
                <c:if test="${not empty passFailStats}">
                    <c:forEach var="entry" items="${passFailStats}">
                        labels.push('<c:out value="${entry.key}" escapeXml="true" />');
                        data.push(<c:out value="${entry.value}" />);
                    </c:forEach>
                </c:if>
                
                // Set colors based on label
                labels.forEach((label, index) => {
                    if (label === 'Đạt') {
                        colors.push('rgba(46, 204, 113, 0.8)');
                    } else {
                        colors.push('rgba(231, 76, 60, 0.8)');
                    }
                });
                
                // Only create chart if there's data
                if (labels.length > 0 && data.some(d => d > 0)) {
                    new Chart(passFailCtx, {
                        type: 'pie',
                        data: {
                            labels: labels,
                            datasets: [{
                                label: 'Số lượng',
                                data: data,
                                backgroundColor: colors,
                            borderColor: [
                                'rgba(46, 204, 113, 1)',
                                'rgba(231, 76, 60, 1)'
                            ],
                            borderWidth: 2
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: true,
                        plugins: {
                            legend: {
                                display: true,
                                position: 'bottom'
                            },
                            tooltip: {
                                backgroundColor: 'rgba(0, 0, 0, 0.8)',
                                padding: 12,
                                titleFont: { size: 14, weight: 'bold' },
                                bodyFont: { size: 13 },
                                callbacks: {
                                    label: function(context) {
                                        const label = context.label || '';
                                        const value = context.parsed || 0;
                                        const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                        const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : 0;
                                        return label + ': ' + value + ' (' + percentage + '%)';
                                    }
                                }
                            }
                        }
                    }
                    });
                } else {
                    passFailCtx.parentElement.innerHTML = '<p style="text-align: center; color: #7f8c8d; padding: 2rem;">Chưa có dữ liệu để hiển thị</p>';
                }
            }
        }
    </script>
</body>
</html>
