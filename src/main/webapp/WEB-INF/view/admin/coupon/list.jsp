<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Quản lý khuyến mãi - Danh sách</title>
    <link href="/css/styles.css" rel="stylesheet" />
    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="sb-nav-fixed">
<jsp:include page="../layout/header.jsp"/>
<div id="layoutSidenav">
    <jsp:include page="../layout/sidebar.jsp"/>
    <div id="layoutSidenav_content">
        <main>
            <div class="container-fluid px-4 mt-4">
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <script>
                        setTimeout(function() {
                            const alert = document.querySelector('.alert');
                            if (alert) {
                                let bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
                                bsAlert.close();
                            }
                        }, 3000);
                    </script>
                </c:if>

                <h1 class="mb-3">Danh sách mã khuyến mãi</h1>

                <div class="d-flex justify-content-between mb-3">
                    <div>
                        <select id="activeStatus" class="form-select" style="min-width: 200px;">
                            <option value="" ${empty activeStatus ? 'selected' : ''}>Tất cả</option>
                            <option value="true" ${activeStatus == 'true' ? 'selected' : ''}>Hoạt động</option>
                            <option value="false" ${activeStatus == 'false' ? 'selected' : ''}>Không hoạt động</option>
                        </select>
                    </div>
                    <div>
                        <a href="/admin/coupon/edit" class="btn btn-primary">Thêm mới</a>
                    </div>
                </div>

                <table class="table table-bordered table-hover">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Mã giảm giá</th>
                        <th>Giá trị giảm (%)</th>
                        <th>Số lượng</th>
                        <th>Ngày bắt đầu</th>
                        <th>Ngày kết thúc</th>
                        <th>Trạng thái</th>
                        <th>Tùy chọn</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${coupons}" var="coupon">
                        <tr>
                            <td>${coupon.id}</td>
                            <td>${coupon.code}</td>
                            <td><fmt:formatNumber value="${coupon.discountValue}" type="number" minFractionDigits="0" maxFractionDigits="2"/>%</td>
                            <td>${coupon.quantity}</td>
                            <td>${coupon.startDateStr}</td>
                            <td>${coupon.endDateStr}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${coupon.active}">
                                        <span class="text-success">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-danger">Không hoạt động</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="/admin/coupon/edit/${coupon.id}" class="btn btn-warning btn-sm">Sửa</a>
                                <a href="/admin/coupon/delete/${coupon.id}" class="btn btn-danger btn-sm"
                                   onclick="return confirm('Bạn có chắc muốn xóa mã này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <nav>
                    <ul class="pagination justify-content-center">
                        <li class="page-item">
                            <a class="${1 eq currentPage ? 'disabled page-link' : 'page-link'}"
                               href="/admin/coupon?page=${currentPage - 1}&activeStatus=${activeStatus}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        <c:forEach begin="1" end="${totalPages}" var="pageNum">
                            <li class="page-item ${pageNum eq currentPage ? 'active' : ''}">
                                <a class="page-link" href="/admin/coupon?page=${pageNum}&activeStatus=${activeStatus}">
                                    ${pageNum}
                                </a>
                            </li>
                        </c:forEach>
                        <li class="page-item">
                            <a class="${currentPage eq totalPages ? 'disabled page-link' : 'page-link'}"
                               href="/admin/coupon?page=${currentPage + 1}&activeStatus=${activeStatus}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </main>
        <jsp:include page="../layout/footer.jsp"/>
    </div>
</div>

<!-- Script -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="/js/scripts.js"></script>
<script>
    $(document).ready(function () {
        $('#activeStatus').change(function () {
            let activeStatus = $(this).val();
            location.href = `/admin/coupon?activeStatus=` + encodeURIComponent(activeStatus);
        });
    });
</script>
</body>
</html>
