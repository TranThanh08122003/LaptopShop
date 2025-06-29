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
    <title>Dashboard - Quản lý Nhãn hiệu</title>
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
                <h1 class="mt-4">Quản lý nhãn hiệu</h1>
                <ol class="breadcrumb mb-4">
                    <li class="breadcrumb-item">Thống kê</li>
                    <li class="breadcrumb-item active">Nhãn hiệu</li>
                </ol>

                <div class="mt-4">
                    <div class="row">
                        <div class="col-12 mx-auto">
                            <div class="d-flex justify-content-between">
                                <select id="activeStatus" style="min-width: 200px; max-height: 40px">
                                    <option value="" ${empty activeStatus ? 'selected' : ''}>Tất cả</option>
                                    <option value="true" ${activeStatus == 'true' ? 'selected' : ''}>Hoạt động</option>
                                    <option value="false" ${activeStatus == 'false' ? 'selected' : ''}>Không hoạt động</option>
                                </select>
                                <h2>
<button class="btn btn-primary mb-3" onclick="openCreateFactoryModal()">Thêm nhãn hiệu</button>
                                </h2>

                            </div>

                            <hr>

                            <table class="table table-bordered table-hover">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Mã nhãn hiệu</th>
                                    <th>Tên hiển thị</th>
                                    <th>Trạng thái</th>
                                    <th>Tùy chọn</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${factories}" var="factory">
                                    <tr>
                                        <td>${factory.id}</td>
                                        <td>${factory.code}</td>
                                        <td>${factory.name}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${factory.active}">
                                                    <span class="text-success">Hoạt động</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-danger">Không hoạt động</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="/admin/factory/${factory.id}" class="btn btn-success btn-sm">Xem</a>
                                            <button class="btn btn-warning btn-sm" onclick="openUpdateFactoryModal(${factory.id})">Cập nhật</button>
                                            <a href="/admin/factory/delete/${factory.id}" class="btn btn-danger btn-sm" 
                                               onclick="return confirm('Bạn có chắc muốn xóa nhãn hiệu này?');">Xóa</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                            <nav aria-label="Page navigation example">
                                <ul class="pagination justify-content-center">
                                    <li class="page-item">
                                        <a class="${1 eq currentPage ? 'disabled page-link' : 'page-link'}"
                                           href="/admin/factory?page=${currentPage - 1}&activeStatus=${activeStatus}" aria-label="Previous">
                                            <span aria-hidden="true">&laquo;</span>
                                        </a>
                                    </li>

                                    <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                        <li class="page-item ${pageNum eq currentPage ? 'active' : ''}">
                                            <a class="page-link" href="/admin/factory?page=${pageNum}&activeStatus=${activeStatus}">
                                                ${pageNum}
                                            </a>
                                        </li>
                                    </c:forEach>

                                    <li class="page-item">
                                        <a class="${currentPage eq totalPages ? 'disabled page-link' : 'page-link'}"
                                           href="/admin/factory?page=${currentPage + 1}&activeStatus=${activeStatus}" aria-label="Next">
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
            location.href = `/admin/factory?activeStatus=` + encodeURIComponent(activeStatus);
        });
    });
</script>
<script>
    function openCreateFactoryModal() {
        $('#factoryModal').modal('show');
        $('#factoryModalBody').html('<div class="text-center">Đang tải...</div>');

        $.ajax({
            url: '/admin/factory/create', // controller trả về JSP chứa <form:form>
            type: 'GET',
            success: function (data) {
                $('#factoryModalBody').html(data);
            },
            error: function () {
                $('#factoryModalBody').html('<div class="text-danger">Lỗi tải form</div>');
            }
        });
    }
    function openUpdateFactoryModal(factoryId) {
    $('#factoryModal').modal('show');
    $('#factoryModalBody').html('<div class="text-center">Đang tải...</div>');

    $.ajax({
        url: '/admin/factory/update/' + factoryId,
        type: 'GET',
        success: function (data) {
            $('#factoryModalLabel').text('Cập nhật nhãn hiệu');
            $('#factoryModalBody').html(data);
        },
        error: function () {
            $('#factoryModalBody').html('<div class="text-danger">Lỗi tải form cập nhật</div>');
        }
    });
}

</script>

<!-- Modal -->
<div class="modal fade" id="factoryModal" tabindex="-1" aria-labelledby="factoryModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="factoryModalLabel">Thêm nhãn hiệu</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
      </div>
      <div class="modal-body" id="factoryModalBody">
        <!-- Nội dung form sẽ được load vào đây -->
        <div class="text-center">Đang tải...</div>
      </div>
    </div>
  </div>
</div>

</body>

</html>
