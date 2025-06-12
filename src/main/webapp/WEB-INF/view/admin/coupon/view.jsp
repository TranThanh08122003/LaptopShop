<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<link rel="shortcut icon" type="image/png" href="/images/auth-bg/icon.png"/>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <title>Dashboard - SB Admin</title>
    <link href="/css/styles.css" rel="stylesheet"/>
    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
</head>
<body class="sb-nav-fixed">
<jsp:include page="../layout/header.jsp"/>
<div id="layoutSidenav">
    <jsp:include page="../layout/sidebar.jsp"/>

    <div id="layoutSidenav_content">
        <main>
            <div class="container-fluid px-4">
                <h1 class="mt-4">Quản lý khuyến mãi</h1>
                <ol class="breadcrumb mb-4">
                    <li class="breadcrumb-item">Thống kê</li>
                    <li class="breadcrumb-item active">Khuyến mãi</li>
                </ol>

                <div class="mt-4">
                    <div class="row">
                        <div class="col-12 mx-auto">
                            <div class="d-flex justify-content-between">
                                <h2>
                                    Danh sách mã khuyến mãi
                                </h2>
                                <!-- Nếu bạn muốn filter theo trạng thái active thì có thể bổ sung -->
                                <select id="activeStatus" style="min-width: 200px; max-height: 40px">
                                    <option value="" ${empty activeStatus ? 'selected' : ''}>Tất cả</option>
                                    <option value="true" ${activeStatus == 'true' ? 'selected' : ''}>Hoạt động</option>
                                    <option value="false" ${activeStatus == 'false' ? 'selected' : ''}>Không hoạt động</option>
                                </select>
                            </div>

                            <hr>

                            <table class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th scope="col">ID</th>
                                    <th scope="col">Mã giảm giá</th>
                                    <th scope="col">Giá trị giảm (%)</th>
                                    <th scope="col">Số lượng</th>
                                    <th scope="col">Ngày bắt đầu</th>
                                    <th scope="col">Ngày kết thúc</th>
                                    <th scope="col">Trạng thái</th>
                                    <th scope="col">Tùy chọn</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${coupons}" var="coupon">
                                    <tr>
                                        <td>${coupon.id}</td>
                                        <td>${coupon.code}</td>
                                        <td><fmt:formatNumber value="${coupon.discountValue}" type="number" minFractionDigits="0" maxFractionDigits="2"/>%</td>
                                        <td>${coupon.quantity}</td>
                                        <td><fmt:formatDate value="${coupon.startDate}" pattern="dd/MM/yyyy"/></td>
                                        <td><fmt:formatDate value="${coupon.endDate}" pattern="dd/MM/yyyy"/></td>
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
                                            <a href="/admin/coupon/${coupon.id}" class="btn btn-success btn-sm">Xem</a>
                                            <a href="/admin/coupon/update/${coupon.id}" class="btn btn-warning btn-sm">Cập nhật</a>
                                            <a href="/admin/coupon/delete/${coupon.id}" class="btn btn-danger btn-sm" 
                                               onclick="return confirm('Bạn có chắc muốn xóa mã này?');">Xóa</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                            <nav aria-label="Page navigation example">
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
                    </div>
                </div>
            </div>
        </main>
        <jsp:include page="../layout/footer.jsp"/>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
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
