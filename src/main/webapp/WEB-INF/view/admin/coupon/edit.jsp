<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>
        <c:choose>
            <c:when test="${coupon.id != null}">Chỉnh sửa mã giảm giá</c:when>
            <c:otherwise>Thêm mã giảm giá</c:otherwise>
        </c:choose>
    </title>
    <link href="/css/styles.css" rel="stylesheet" />
</head>
<body>
<jsp:include page="../layout/header.jsp"/>
<div class="container mt-4">
    <h1>
        <c:choose>
            <c:when test="${coupon.id != null}">Chỉnh sửa mã giảm giá</c:when>
            <c:otherwise>Thêm mã giảm giá</c:otherwise>
        </c:choose>
    </h1>

    <!-- Tạo biến formAction dựa theo coupon.id -->
    <c:set var="formAction" value="/admin/coupon/create" />
    <c:if test="${coupon.id != null}">
        <c:set var="formAction" value="/admin/coupon/update/${coupon.id}" />
    </c:if>

    <form action="${formAction}" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <div class="mb-3">
            <label for="code" class="form-label">Mã giảm giá</label>
            <input type="text" class="form-control" id="code" name="code" required value="${coupon.code}" />
        </div>

        <div class="mb-3">
            <label for="discountValue" class="form-label">Giá trị giảm (%)</label>
            <input type="number" step="0.01" min="0" max="100" class="form-control" id="discountValue" name="discountValue" required value="${coupon.discountValue}" />
        </div>

        <div class="mb-3">
            <label for="quantity" class="form-label">Số lượng</label>
            <input type="number" min="0" class="form-control" id="quantity" name="quantity" required value="${coupon.quantity}" />
        </div>

        <div class="mb-3">
            <label for="startDate" class="form-label">Ngày bắt đầu</label>
            <input type="date" class="form-control" id="startDate" name="startDate" required value="${coupon.startDate}" />
        </div>

        <div class="mb-3">
            <label for="endDate" class="form-label">Ngày kết thúc</label>
<input type="date" class="form-control" id="endDate" name="endDate" required value="${coupon.endDate}" />        </div>

        <div class="mb-3 form-check">
            <!-- Luôn gửi false nếu checkbox không được chọn -->
            <input type="checkbox" class="form-check-input" id="active" name="active" value="true" ${coupon.active ? 'checked' : ''}/>
            <label class="form-check-label" for="active">Hoạt động</label>
        </div>

        <button type="submit" class="btn btn-primary">Lưu</button>
        <a href="/admin/coupon" class="btn btn-secondary">Hủy</a>
    </form>
</div>
<jsp:include page="../layout/footer.jsp"/>
</body>
</html>
