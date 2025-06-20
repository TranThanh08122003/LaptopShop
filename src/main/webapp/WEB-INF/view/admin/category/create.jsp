<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <title>Dashboard - Thêm danh mục</title>
    <link href="/css/styles.css" rel="stylesheet"/>
    <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
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
                    <li class="breadcrumb-item"><a href="/admin">Thống kê</a></li>
                    <li class="breadcrumb-item active">Thêm danh mục</li>
                </ol>

                <div class="container container-xl mt-5">
                    <div class="row">
                        <div class="col-md-6 col-12 mb-3 mx-auto">
                            <h2>Thêm danh mục</h2>
                            <hr>
                            <form:form method="post" action="/admin/category/create" modelAttribute="newCategory" class="row g-3">
                                <div class="col-12 mb-3">
                                    <c:set var="errorName">
                                        <form:errors path="name" cssClass="invalid-feedback"/>
                                    </c:set>
                                    <label class="form-label">Tên danh mục:</label>
                                    <form:input path="name" type="text" class="form-control ${not empty errorName ? 'is-invalid' : ''}"/>
                                    ${errorName}
                                </div>

                                <div class="col-12 mb-3">
                                    <c:set var="errorDescription">
                                        <form:errors path="description" cssClass="invalid-feedback"/>
                                    </c:set>
                                    <label class="form-label">Mô tả:</label>
                                    <form:input path="description" type="text" class="form-control ${not empty errorDescription ? 'is-invalid' : ''}"/>
                                    ${errorDescription}
                                </div>

                                <div class="col-12 mb-5">
                                    <button type="submit" class="btn btn-primary">Thêm danh mục</button>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </main>
        <jsp:include page="../layout/footer.jsp"/>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
<script src="/js/scripts.js"></script>
</body>
</html>
