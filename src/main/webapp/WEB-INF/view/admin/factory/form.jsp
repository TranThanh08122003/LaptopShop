<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form:form method="post" modelAttribute="factory" action="/admin/factory/save">
    <form:hidden path="id"/>
    
    <div class="mb-3">
        <label>Mã nhãn hiệu:</label>
        <form:input path="code" cssClass="form-control"/>
    </div>

    <div class="mb-3">
        <label>Tên hiển thị:</label>
        <form:input path="name" cssClass="form-control"/>
    </div>

    <div class="mb-3 form-check">
        <form:checkbox path="active" cssClass="form-check-input"/>
        <label class="form-check-label">Hoạt động</label>
    </div>

    <button type="submit" class="btn btn-success">Lưu</button>
</form:form>
