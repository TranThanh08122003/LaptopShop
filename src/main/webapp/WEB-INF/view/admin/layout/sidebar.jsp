<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-dark" id="sidenavAccordion">
        <div class="sb-sidenav-menu">
            <div class="nav">
                <div class="sb-sidenav-menu-heading">Tùy chọn</div>
                <a class="nav-link" href="/admin">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Thống kê
                </a>
                <c:if test="${sessionScope.role == 'MANAGER'}">
                    <a class="nav-link" href="/admin/user">
                        <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                        Tài khoản
                    </a>
                </c:if>
                <c:if test="${sessionScope.role == 'MANAGER'}">
                <a class="nav-link" href="/admin/product">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Sản phẩm
                </a>

                <a class="nav-link" href="/admin/order">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Đơn hàng
                </a>
                <a class="nav-link" href="/admin/category">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Danh mục
                </a>
                <a class="nav-link" href="/admin/coupon">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Khuyến mãi
                </a>
                <a class="nav-link" href="/admin/factory">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Hãng
                </a>

                </c:if>

            </div>
        </div>
        <div class="sb-sidenav-footer">
            <div class="small">Đăng nhập với vai trò:</div>
            <c:out value="${sessionScope.roleDescription}" />
        </div>
    </nav>
</div>