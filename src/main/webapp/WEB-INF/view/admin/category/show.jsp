<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<link rel="shortcut icon" type="image/png" href="/images/auth-bg/icon.png"/>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>Dashboard - Quản lý danh mục</title>
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
                <h1 class="mt-4">Quản lý danh mục</h1>
                <ol class="breadcrumb mb-4">
                    <li class="breadcrumb-item">Thống kê</li>
                    <li class="breadcrumb-item active">Danh mục</li>
                </ol>
                <div class="mt-4">
                    <div class="row">
                        <div class="col-12 mx-auto">
                            <div class="d-flex justify-content-between">
                                <h2>Danh sách danh mục</h2>
                                <div class="d-flex" style="max-height: 40px">
                                <input class="form-control me-2" type="search" id="keyword" name="keyword" placeholder="Tìm kiếm" aria-label="Search" value="${keyword}">
                                    <button class="btn btn-outline-success" type="submit" id="searchBtn" style="min-width: 100px">Tìm kiếm</button>
                                </div>
                                <a href="/admin/category/create" class="btn btn-primary">Thêm danh mục</a>
                            </div>

                            <hr>
                            <table class="table table-striped table-hover table-responsive-md">
                                <thead>
                                <tr>
                                    <th scope="col">ID</th>
                                    <th scope="col">Tên danh mục</th>
                                    <th scope="col">Mô tả</th>
                                    <th scope="col">Tùy chọn</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="category" items="${categories}">
                                    <tr>
                                        <th>${category.id}</th>
                                        <td>${category.name}</td>
                                        <td>${category.description}</td>
                                        <td>
                                            <form action="/admin/category/update/${category.id}" method="get" style="display:inline;">
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                <button type="submit" class="btn btn-warning">Cập nhật</button>
                                            </form>
                                            <form action="/admin/category/delete/${category.id}" method="post" style="display:inline;"
                                                onsubmit="return confirm('Bạn có chắc chắn muốn xóa danh mục này?');">
                                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                                <button type="submit" class="btn btn-danger">Xóa</button>
                                            </form>

                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                            <nav aria-label="Page navigation example">
                                <ul class="pagination justify-content-center">
                                    <li class="page-item">
                                        <a class="${1 eq currentPage ? 'disabled page-link' : 'page-link'}"
                                           href="/admin/category?page=${currentPage - 1}" aria-label="Previous">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>

<c:if test="${totalPages > 0}">
    <c:forEach begin="0" end="${totalPages -1}" varStatus="loop">
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
                                           href="/admin/category?page=${currentPage + 1}" aria-label="Next">
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="/js/scripts.js"></script>
<script>
    $(document).ready(() => {
        $('.page-link').click(function (event) {
            event.preventDefault();
            let page = $(this).attr('href');
            let keyword = $('#keyword').val();
            page += `&keyword=` + encodeURIComponent(keyword); 
            location.href = page;
        });


        $('#searchBtn').click(() => {
            let keyword = $('#keyword').val();
            location.href = `/admin/category?keyword=` + encodeURIComponent(keyword); 
        });
    });
</script>
</body>
</html>
