<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm số lượng</title>
    <link href="/css/styles.css" rel="stylesheet"/>
</head>
<body>
    <div class="container mt-4">
        <h2>Thêm số lượng cho: ${product.name}</h2>
        <p>Số lượng hiện tại: <strong id="currentQty">${product.quantity}</strong></p>
        <p>Số lượng sau khi thêm: <strong id="newQty">${product.quantity}</strong></p>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const currentQty = parseInt(document.getElementById("currentQty").innerText);
        const addedInput = document.querySelector("input[name='addedQuantity']");
        const newQty = document.getElementById("newQty");

        addedInput.addEventListener("input", () => {
            const added = parseInt(addedInput.value) || 0;
            newQty.innerText = currentQty + added;
        });
    });
</script>
        <form method="post" action="/admin/product/add-quantity" >
            <input type="hidden" name="id" value="${product.id}"/>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <div class="mb-3">
                <label for="addedQuantity" class="form-label">Số lượng muốn thêm:</label>
                <input type="number" class="form-control" name="addedQuantity" required min="1"/>
            </div>
            <button type="submit" class="btn btn-primary">Cập nhật</button>
            <a href="/admin/product" class="btn btn-secondary">Quay lại</a>
        </form>
    </div>
</body>
</html>
