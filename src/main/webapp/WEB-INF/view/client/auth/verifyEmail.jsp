<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>Forgot Password</title>

    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
          rel="stylesheet">

    <!-- Icon Font Stylesheet -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Libraries Stylesheet -->
    <link href="/client/lib/lightbox/css/lightbox.min.css" rel="stylesheet">
    <link href="/client/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">


    <!-- Customized Bootstrap Stylesheet -->
    <link href="/client/css/bootstrap.min.css" rel="stylesheet">

    <!-- Template Stylesheet -->
    <link href="/client/css/style.css" rel="stylesheet">

    <link href="https://cdnjs.cloudflare.com/ajax/libs/jquery-toast-plugin/1.3.2/jquery.toast.min.css"
          rel="stylesheet">
</head>
<body>

<jsp:include page="../layout/header.jsp"/>


<div class="container h-100" style="margin-top: 158px ">
    <div class="row h-100 justify-content-center align-items-center py-7">
        <form id="emailForm" class="col-lg-6 bg-primary rounded p-5" onsubmit="return false;">
    <h2 class="text-center text-white mb-4">Xác thực Email</h2>
    <div class="mb-3">
        <label for="email" class="form-label text-white">Email: </label>
        <input class="form-control" id="email" name="email" />
    </div>
    <input type="hidden" id="csrfToken" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <div class="d-grid gap-2">
        <button type="submit" class="btn btn-light submitEmail">Gửi Email</button>
    </div>
</form>
    </div>
</div>




<jsp:include page="../layout/footer.jsp"/>






<!-- JavaScript Libraries -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script
        src="https://cdnjs.cloudflare.com/ajax/libs/jquery-toast-plugin/1.3.2/jquery.toast.min.js"></script>
<!-- Template Javascript -->
<script src="/client/js/main.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
$(document).ready(() => {
    $('#emailForm .submitEmail').click((e) => {
        e.preventDefault();

        const email = $('#email').val().trim();
        const csrfToken = $('#csrfToken').val();

        if (!email) {
            $.toast({
                text: "Vui lòng nhập email",
                bgColor: '#dc3545',
                textColor: 'white',
                position: 'top-right',
                icon: 'error'
            });
            return;
        }

        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(email)) {
            $.toast({
                text: "Email không hợp lệ",
                bgColor: '#dc3545',
                textColor: 'white',
                position: 'top-right',
                icon: 'error'
            });
            return;
        }

        const $button = $('#emailForm .submitEmail');
        $button.prop('disabled', true);

$.ajax({
    url: '/forgot-password/verify-email',  // không có tham số trong URL nữa
    type: 'POST',
    contentType: 'application/json',
    headers: {
        'X-CSRF-TOKEN': csrfToken
    },
    data: JSON.stringify({ email }),  // gửi email trong body JSON
    success: function () {
        $.toast({
            text: "Gửi email xác thực thành công",
            bgColor: '#28a745',
            textColor: 'white',
            position: 'top-right',
            icon: 'success'
        });
        setTimeout(() => {
            window.location.href = '/forgot-password/verify-otp';
        }, 5000);
    },
    error: function (xhr) {
        let message = "Gửi email xác thực thất bại";
        if (xhr.responseText) {
            message = xhr.responseText;
        }
        $.toast({
            text: message,
            bgColor: '#dc3545',
            textColor: 'white',
            position: 'top-right',
            icon: 'error'
        });
    },
    complete: function () {
        $button.prop('disabled', false);
    }
});

    });
});
</script>

</body>
</html>