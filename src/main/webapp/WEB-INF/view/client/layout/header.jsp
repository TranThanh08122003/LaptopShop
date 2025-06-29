<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- Navbar start -->

<style>
    .text-running {
        display: inline-block;
        opacity: 0;
        animation: textAppear 0.5s ease-in-out forwards, textRunning 4s linear infinite, textDisappear 4s linear infinite;
        transform-origin: right center;
    }

    @keyframes textAppear {
        0% {
            opacity: 0;
            transform: translateX(50px);
        }
        100% {
            opacity: 1;
            transform: translateX(0);
        }
    }

    @keyframes textRunning {
        0% { transform: translateX(50%); }
        100% { transform: translateX(-25%); }
    }

    @keyframes textDisappear {
        0%, 75% {
            opacity: 1;
        }
        100% {
            opacity: 0;
        }
    }
</style>

<div class="container-fluid fixed-top" style = "padding: 0px">
    <div class="top_slide ">
        <p class = "top_banner text-running">Hà Nội: 153 Lê Thanh Nghị, Hai Bà Trưng | 35/1194 đường Láng, Đống Đa</p>
    </div>
    <div class="container px-0">
        <nav class="navbar navbar-light bg-white navbar-expand-xl">
            <a href="/" class="navbar-brand">
                <img src="/client/img/logo_mobi.webp" class="img-logo">
            </a>
            <button class="navbar-toggler py-2 px-3" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarCollapse">
                <span class="fa fa-bars text-primary"></span>
            </button>
            <div class="collapse navbar-collapse bg-white justify-content-between ms-xxl-5 me-xxl-3" id="navbarCollapse">
                <div class="navbar-nav">
                    <a href="/" class="nav-item nav-link active">Trang Chủ</a>
                    <a href="/products" class="nav-item nav-link">Sản Phẩm</a>
                        <div class="nav-item dropdown">
                            <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">Danh mục</a>
                            <div class="dropdown-menu rounded-0 m-0">
                                        <c:forEach var="category" items="${categories}">
                                            <li>
                                                <a href="/products?categoryId=${category.id
                                                    }<c:if test='${not empty param.sort}'>&amp;sort=${param.sort}</c:if
                                                    ><c:if test='${not empty param.page}'>&amp;page=${param.page}</c:if>">
                                                    ${category.name}
                                                </a>
                                    </li>
                                </c:forEach>
                            </div>
                        </div>    
                </div>
                <div class="d-flex me-0">
                    <button
                            class="btn-search btn border border-secondary btn-md-square rounded-circle bg-white me-4"
                            data-bs-toggle="modal" data-bs-target="#searchModal"><i
                            class="fas fa-search text-primary"></i></button>
                    <c:if test="${not empty pageContext.request.userPrincipal}">


                    <a href="/cart" class="position-relative me-4 my-auto">
                        <i class="fa fa-shopping-bag fa-2x"></i>
                        <span
                                class="position-absolute  rounded-circle d-flex align-items-center justify-content-center text-light px-1"
                                style="background: var(--bs-red); top: -5px; left: 15px; height: 20px; min-width: 20px;"
                                id="sumCart">${sessionScope.sum}
                        </span>
                    </a>

                    <div class="dropdown my-auto">
                        <a href="#" class="dropdown" role="button" id="dropdownMenuLink"
                           data-bs-toggle="dropdown" aria-expanded="false" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            <i class="fas fa-user fa-2x"></i>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end p-4"
                            aria-labelledby="dropdownMenuLink">
                            <li class="d-flex align-items-center flex-column" style="min-width: 300px;">
                                <img style="width: 150px; height: 150px; border-radius: 50%; overflow: hidden;"
                                     src="/images/avatar/${sessionScope.avatar}" />
                                <div class="text-center my-3">
                                    <c:out value="${sessionScope.fullName}" />
                                </div>
                            </li>
                            <li><a class="dropdown-item" href="/show-info">Quản lý tài khoản</a></li>
                            <li><a class="dropdown-item" href="/order-history">Lịch sử mua hàng</a></li>
                            <li><a class="dropdown-item" href="/forgot-password/verify-email">Đổi mật khẩu</a></li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li>
                                <form method="post" action="/logout" style="margin: 0">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="dropdown-item">Đăng xuất</button>
                                </form>
                            </li>
                        </ul>
                    </div>
                    </c:if>
                    <c:if test="${empty pageContext.request.userPrincipal}">
                        <a href="/login" class="a-login btn btn-primary text-light text-center" style="line-height: 30px">Đăng Nhập</a>
                    </c:if>
                </div>
            </div>
        </nav>
    </div>
</div>
<!-- Navbar End -->