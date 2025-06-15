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
                    <c:if test="${not empty successMessage}">
                        <div id="flashMessage" class="alert alert-success alert-dismissible fade show mt-3" role="alert">
                            ${successMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                <h1 class="mt-4">Quản lý sản phẩm</h1>
                <ol class="breadcrumb mb-4">
                    <li class="breadcrumb-item">Thống kê</li>
                    <li class="breadcrumb-item active">Sản phẩm</li>

                </ol>
                <div class="mt-4">
                    <div class="row">
                        <div class="col-12 mx-auto">
                            <div class="d-flex justify-content-between">
                                <div class="d-flex" style="max-height: 40px">
                                    <input class="form-control me-2" type="search" id="keyword" name="keyword" placeholder="Tìm kiếm" aria-label="Search" style="min-width: 400px" value="${name}">
                                    <button class="btn btn-outline-success" type="submit" id="searchBtn" style="min-width: 100px">Tìm kiếm</button>
                                </div>
                                    <select id="factoryName" style="min-width: 200px; max-height: 40px">
                                        <option value="" ${factoryName == '' ? 'selected' : ''}>Tất cả</option>
                                        <option value="Asus" ${factoryName == 'Asus' ? 'selected' : ''}>Asus</option>
                                        <option value="Dell" ${factoryName == 'Dell' ? 'selected' : ''}>Dell</option>
                                        <option value="Lenovo" ${factoryName == 'Lenovo' ? 'selected' : ''}>Lenovo</option>
                                        <option value="LG" ${factoryName == 'LG' ? 'selected' : ''}>LG</option>
                                        <option value="Apple" ${factoryName == 'Apple' ? 'selected' : ''}>Apple</option>
                                        <option value="Acer" ${factoryName == 'Acer' ? 'selected' : ''}>Acer</option>
                                    </select>

                                    <select id="categoryId" style="min-width: 200px; max-height: 40px">
                                        <option value="" ${empty param.categoryId ? 'selected' : ''}>Tất cả danh mục</option>
                                        <c:forEach var="cat" items="${categories}">
                                            <option value="${cat.id}" ${param.categoryId == cat.id ? 'selected' : ''}>${cat.name}</option>
                                        </c:forEach>
                                    </select>
                                <a href="/admin/product/create" class="btn btn-primary">Thêm sản phẩm</a>
                            </div>

                            <hr>
                            <%-- desgin this table more vibrant and beautiful--%>
                            <table class="table table-striped table-hover table-responsive-md">
                                <thead>
                                <tr>
                                    <th scope="col">ID</th>
                                    <th scope="col">Tên sản phẩm</th>
                                    <th scope="col">Giá thành</th>
                                    <th scope="col">Nhãn hiệu</th>
                                    <th scope="col">Danh mục</th>
                                    <th scope="col">Tùy chọn</th>
                                </tr>
                                </thead>
                                <tbody>
                         <c:choose>
                            <c:when test="${not empty products}">
                                <c:forEach var="product" items="${products}">
                                    <tr>
                                        <th>${product.id}</th>
                                        <td>${product.name}</td>
                                        <td><fmt:formatNumber value="${product.price}" type="number" /> đ</td>
                                        <td>${product.factory}</td>
                                        <td>${product.category.name}</td>
                                        <td>
                                            <a href="/admin/product/${product.id}" class="btn btn-success">Xem</a>
                                            <a href="/admin/product/update/${product.id}" class="btn btn-warning">Cập nhật</a>
                                            <a href="/admin/product/delete/${product.id}" class="btn btn-danger">Xóa</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="text-center text-danger fw-bold">
                                        Không tìm thấy sản phẩm nào.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                                </tbody>
                            </table>
                            <nav aria-label="Page navigation example">
                                <ul class="pagination justify-content-center">
                                    <li class="page-item">
                                        <a class="${1 eq currentPage ? 'disabled page-link' : 'page-link'}"
                                           href="/admin/product?page=${currentPage - 1}" aria-label="Previous">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>
<c:if test="${totalPages > 0}">
    <c:forEach begin="0" end="${totalPages - 1}" varStatus="loop">
        <li class="page-item">
            <a class="${(loop.index + 1) eq currentPage ? 'active page-link' : 'page-link'}"
               href="#" data-page="${loop.index + 1}">
                ${loop.index + 1}
            </a>
        </li>
    </c:forEach>
</c:if>

                                    <li class="page-item">
                                        <a class="${currentPage eq totalPages ? 'disabled page-link' : 'page-link'}"
                                           href="/admin/product?page=${currentPage + 1}" aria-label="Next">
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
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
<script src="/js/scripts.js"></script>
<script>
    $(document).ready(() => {

        $('.page-link').click(function(event) {
            event.preventDefault();
            let page = $(this).attr('href');
            let keyword = $('#keyword').val();
            let factoryName = $('#factoryName').val();
            let categoryId = $('#categoryId').val();
            if (factoryName === 'Tất cả') factoryName = '';

            page += `&name=` + encodeURIComponent(keyword) +
                    `&factory=` + encodeURIComponent(factoryName) +
                    `&categoryId=` + encodeURIComponent(categoryId);

            location.href = page;
        });

        $('#categoryId').change(() => {
            let keyword = $('#keyword').val();
            let ft = $('#factoryName').val();
            let categoryId = $('#categoryId').val();
            if (ft === 'Tất cả') ft = '';
            location.href = `/admin/product?name=` + encodeURIComponent(keyword) +
                `&factory=` + encodeURIComponent(ft) +
                `&categoryId=` + encodeURIComponent(categoryId);
        });
        $('#searchBtn').click(() => {
            let keyword = $('#keyword').val();
            let ft = $('#factoryName').val();
            let categoryId = $('#categoryId').val();
            if (ft === 'Tất cả') {
                ft = '';
            }
            location.href = `/admin/product?name=` + encodeURIComponent(keyword) +
                `&factory=` + encodeURIComponent(ft) +
                `&categoryId=` + encodeURIComponent(categoryId);
        });


        $('#factoryName').change(() => {
            let keyword = $('#keyword').val();
            let ft = $('#factoryName').val();
            let categoryId = $('#categoryId').val();
            if (ft === 'Tất cả') {
                ft = '';
            }
            location.href = `/admin/product?name=` + encodeURIComponent(keyword) + `&factory=` + encodeURIComponent(ft);
        });
    });
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    window.addEventListener('DOMContentLoaded', () => {
        const flash = document.getElementById('flashMessage');
        if (flash) {
            setTimeout(() => {
                // Dùng Bootstrap collapse để ẩn có hiệu ứng
                const alert = new bootstrap.Alert(flash);
                alert.close();
            }, 3000); // 3000 ms = 3 giây
        }
    });
</script>


</body>
</html>
