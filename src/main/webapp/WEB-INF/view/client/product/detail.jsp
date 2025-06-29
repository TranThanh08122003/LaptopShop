<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <link rel="shortcut icon" type="image/png" href="/images/auth-bg/icon.png"/>
    <title> ${product.name} - LaptopHaui</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">

    <!-- Google Web Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
            href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
            rel="stylesheet">

    <!-- Icon Font Stylesheet -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css"
          rel="stylesheet">

    <!-- Libraries Stylesheet -->
    <link href="/client/lib/lightbox/css/lightbox.min.css" rel="stylesheet">
    <link href="/client/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">


    <!-- Customized Bootstrap Stylesheet -->
    <link href="/client/css/bootstrap.min.css" rel="stylesheet">

    <!-- Template Stylesheet -->
    <link href="/client/css/style.css" rel="stylesheet">
    <meta name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link href="https://cdnjs.cloudflare.com/ajax/libs/jquery-toast-plugin/1.3.2/jquery.toast.min.css"
          rel="stylesheet">
</head>
</head>

<body>
<input type="hidden" id="productId" value="${product.id}" />
<!-- Spinner Start -->
<div id="spinner"
     class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50  d-flex align-items-center justify-content-center">
    <div class="spinner-grow text-primary" role="status"></div>
</div>
<!-- Spinner End -->

<jsp:include page="../layout/header.jsp"/>

<!-- Single Product Start -->
<div class="container-fluid py-6 mt-5">
    <div class="container py-5">
        <div class="row">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="/">Home</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Chi Tiết Sản Phẩm
                    </li>
                </ol>
            </nav>
        </div>
        <div class="row g-4 mb-5">

            <div class="col-lg-8 col-xl-9">
                <div class="row g-4">
                    <div class="col-lg-6">
                        <div id="carouselId" class="carousel slide position-relative" data-bs-ride="carousel">
                            <div class="carousel-inner" role="listbox">
                               <c:forEach var="ProductImage" items="${ProductImages}" varStatus="status">
                                    <div class="carousel-item rounded ${status.index == 0 ? 'active' : ''}">
                                        <img src="/images/product/${ProductImage.image}"
                                            class="img-fluid w-100 h-100 rounded"
                                            alt="Product image ${status.index + 1}">
                                    </div>
                                </c:forEach>

                            </div>
                            <button class="carousel-control-prev" type="button" data-bs-target="#carouselId"
                                    data-bs-slide="prev">
                                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                <span class="visually-hidden">Previous</span>
                            </button>
                            <button class="carousel-control-next" type="button" data-bs-target="#carouselId"
                                    data-bs-slide="next">
                                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                <span class="visually-hidden">Next</span>
                            </button>
                            <!-- Carousel controls here -->
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <h4 class="fw-bold mb-3"> ${product.name}</h4>
                        <p>Kho: ${product.quantity}</p>
                        <input type="hidden" id="quantityP" value="${product.quantity}">
                        <p class="mb-3">${product.factory}</p>
                        <h5 class="fw-bold mb-3">
                            <fmt:formatNumber type="number" value="${product.price}"/> đ

                        </h5>
                        <div class="d-flex mb-4">
                            <c:forEach var="i" begin="1" end="5">
                                <c:choose>
                                    <c:when test="${i <= avgRate}">
                                        <i class="fa fa-star star-primary"></i>
                                    </c:when>
                                    <c:when test="${i - 1 < avgRate && i > avgRate}">
                                        <i class="fa fa-star-half-alt star-primary"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa fa-star"></i>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <p>${avgRate}</p>
                        </div>
                        <p class="mb-4">
                            ${product.shortDesc}
                        </p>

                        <input type="hidden" id="cartQuantity" value="${cartQuantity}">

                        <div class="input-group quantity mb-5" style="width: 120px;">
                            <div class="input-group-btn">
                                <button class="btn btn-sm btn-minus rounded-circle bg-light border">
                                    <i class="fa fa-minus"></i>
                                </button>
                            </div>
                            <input type="number"
                                   class="form-control form-control-sm text-center border-0" value="1"
                                   data-cart-detail-index="0">
                            <div class="input-group-btn">
                                <button class="btn btn-sm btn-plus rounded-circle bg-light border">
                                    <i class="fa fa-plus"></i>
                                </button>
                            </div>
                        </div>
<%--                        <form action="/add-product-from-view-detail" method="post"--%>
<%--                              modelAttribute="product">--%>
                            <input type="hidden" name="${_csrf.parameterName}"
                                   value="${_csrf.token}"/>
                            <input class="form-control d-none" type="text" value="${product.id}"
                                   name="id"/>

                            <input class="form-control d-none" type="text" name="quantity" value="1"
                                   id="cartDetails0.quantity"/>
                            <button data-product-id="${product.id}"
                                    class="btnAddToCartDetail btn btn-secondary rounded-pill px-4 py-2 mb-4"><i
                                    class="fa fa-shopping-bag me-2"></i>
                                Thêm vào giỏ
                            </button>
<%--                        </form>--%>

                    </div>
                    <div class="col-lg-12">
                        <nav>
                            <div class="nav nav-tabs mb-3">
                                <button class="nav-link active border-white border-bottom-0"
                                        type="button" role="tab" id="nav-about-tab" data-bs-toggle="tab"
                                        data-bs-target="#nav-about" aria-controls="nav-about"
                                        aria-selected="true">Mô tả
                                </button>
                                <button class="nav-link border-white border-bottom-0" type="button" role="tab"
                                        id="nav-mission-tab" data-bs-toggle="tab" data-bs-target="#nav-mission"
                                        aria-controls="nav-mission" aria-selected="false">Cấu hình chi tiết
                                </button>
                            </div>
                        </nav>
                        <div class="tab-content mb-5">
                            <div class="tab-pane active" id="nav-about" role="tabpanel"
                                 aria-labelledby="nav-about-tab">
                                <p>
                                    ${product.detailDesc}
                                </p>

                            </div>
                            <div class="tab-pane" id="nav-mission" role="tabpanel" aria-labelledby="nav-mission-tab">
                                <div style="background: #f8f9fa" class="py-2 px-2">
                                    <div class="d-flex">
                                        <img width="26" height="26" src="/images/performance/graphic-card.png"
                                             alt="graphic card" class="fas fa-apple-alt me-2">
                                        <p>${product.graphicCard}</p>
                                    </div>
                                    <div class="d-flex">
                                        <img width="26" height="26" src="/images/performance/laptop.png"
                                             alt="graphic card" class="fas fa-apple-alt me-2">
                                        <p>${product.display}, ${product.resolution}</p>
                                    </div>
                                    <div class="d-flex">
                                        <img width="26" height="26" src="/images/performance/cpu-tower.png"
                                             alt="graphic card" class="fas fa-apple-alt me-2">
                                        <p>${product.processor}</p>
                                    </div>
                                    <div class="d-flex">
                                        <img width="26" height="26" src="/images/performance/ram.png" alt="graphic card"
                                             class="fas fa-apple-alt me-2">
                                        <p>${product.ram}</p>
                                    </div>
                                    <div class="d-flex">
                                        <img width="26" height="26" src="/images/performance/ssd.png" alt="graphic card"
                                             class="fas fa-apple-alt me-2">
                                        <p>${product.storage}</p>
                                    </div>
                                    <div class="d-flex">
                                        <img width="26" height="26" src="/images/performance/weight-scale.png"
                                             alt="graphic card" class="fas fa-apple-alt me-2">
                                        <p>${product.weight}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div>
                        <h4 class="mb-5 fw-bold">Bình luận</h4>
                        <c:if test="${comments.size() == 0}">
                            <p>Chưa có bình luận nào</p>
                        </c:if>
                        <c:forEach var="comment" items="${comments}">
                            <div id="comments-section" class="row g-4">
                                <div class="col-lg-12">
                                    <div class="border-bottom rounded p-3 mb-3">
                                        <h5>${comment.userName}</h5>
                                        <p>${comment.message}</p>
                                        <div class="d-flex mb-2">
                                            <c:forEach var="i" begin="1" end="5">
                                                <c:choose>
                                                    <c:when test="${i <= comment.rate}">
                                                        <i class="fa fa-star star-primary"></i>
                                                    </c:when>
                                                    <c:when test="${i - 1 < comment.rate && i > comment.rate}">
                                                        <i class="fa fa-star-half-alt star-primary"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="fa fa-star"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </div>
                                        <button class="btn btn-link reply-button" data-comment-id="${comment.id}">Reply</button>
                                        <div id="reply-form-${comment.id}" class="reply-form" style="display: none;">
                                            <input type="hidden" class="parentCommentId" value="${comment.id}" />
                                            <input type="text" class="name" placeholder="Tên của bạn" />
                                            <input type="email" class="email" placeholder="Email của bạn" />
                                            <textarea class="replyMessage" placeholder="Phản hồi..."></textarea>
                                            <button class="submitReply">Gửi</button>
                                        </div>

                                        <c:forEach var="reply" items="${comment.replies}">
                                            <div class="border-bottom rounded p-3 mb-3 ms-5">
                                                <h5>${reply.userName}</h5>
                                                <p>${reply.message}</p>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    </div>

                    <form method="post">
                        <h4 class="mb-5 fw-bold">Đánh giá và nhận xét</h4>
                        <div class="row g-4">
                            <c:if test="${empty pageContext.request.userPrincipal}">
                                <div class="col-lg-6">
                                    <div class="border-bottom rounded">
                                        <input type="text" id="name" class="form-control border-0 me-4" placeholder="Họ tên *">
                                    </div>
                                </div>
                                <div class="col-lg-6">
                                    <div class="border-bottom rounded">
                                        <input id="email" type="email" class="form-control border-0" placeholder="Email *">
                                    </div>
                                </div>
                            </c:if>
                            <div class="col-lg-12">
                                <div class="border-bottom rounded my-4">
                                    <textarea name="" id="content" class="form-control border-0" cols="30" rows="6" placeholder="Nội dung *" spellcheck="false"></textarea>
                                </div>
                            </div>

                            <div class="col-lg-12">
                                <div class="d-flex justify-content-between py-3 mb-5">
                                    <div class="d-flex align-items-center">
                                        <p class="mb-0 me-3">Please rate:</p>
                                        <div class="d-flex align-items-center" style="font-size: 12px;">
                                            <i class="fa fa-star text-muted" data-value="1"></i>
                                            <i class="fa fa-star" data-value="2"></i>
                                            <i class="fa fa-star" data-value="3"></i>
                                            <i class="fa fa-star" data-value="4"></i>
                                            <i class="fa fa-star" data-value="5"></i>
                                        </div>
                                    </div>
                                    <input type="hidden" id="rating" name="rating" value="0">

                                    <a href="#" class="btn border border-secondary text-primary rounded-pill px-4 py-3 submit-comment"> Đánh giá</a>
                                </div>
                            </div>
                        </div>
                        <div>
                            <input type="hidden" name="${_csrf.parameterName}" id="csrf"
                                   value="${_csrf.token}"/>
                        </div>

                    </form>

                </div>
            <div class="col-lg-4 col-xl-3">
                <div class="row g-4 fruite">
                    <div class="col-lg-12">

                        <div class="mb-4">
                            <h4>Hãng sản xuất</h4>
                            <ul class="list-unstyled fruite-categorie">
                                <li>
                                    <div class="d-flex justify-content-between fruite-name">
                                        <a href="#"><img width="28" height="28"
                                                         src="https://img.icons8.com/deco/48/mac-os.png" alt="mac-os"
                                                         class="fas fa-apple-alt me-2"/>Apples</a>
                                        <span>(${productCountByFactory[3].count})</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="d-flex justify-content-between fruite-name">
                                        <a href="#"><img width="28" height="28"
                                                         src="https://img.icons8.com/color/48/dell--v1.png"
                                                         alt="dell--v1" class="fas fa-apple-alt me-2"/>Dell</a>
                                        <span>(${productCountByFactory[1].count})</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="d-flex justify-content-between fruite-name">
                                        <a href="#"><img width="28" height="28"
                                                         src="https://img.icons8.com/nolan/64/asus--v1.png"
                                                         alt="asus--v1" class="fas fa-apple-alt me-2"/></i>Asus</a>
                                        <span>(${productCountByFactory[0].count})</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="d-flex justify-content-between fruite-name">
                                        <a href="#"><img width="30" height="30"
                                                         src="https://cdn.icon-icons.com/icons2/3911/PNG/512/acer_logo_icon_247729.png"
                                                         alt="lenovo" class="fas fa-apple-alt me-2"/>Acer</a>
                                        <span>(${productCountByFactory[4].count})</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="d-flex justify-content-between fruite-name">
                                        <a href="#"><img width="30" height="30"
                                                         src="https://img.icons8.com/color/48/000000/lenovo.png"
                                                         alt="lenovo" class="fas fa-apple-alt me-2"/>Lenovo</a>
                                        <span>(${productCountByFactory[2].count})</span>
                                    </div>
                                </li>
                                <li>
                                    <div class="d-flex justify-content-between fruite-name">
                                        <a href="#"><img width="32" height="32" src="/images/product/lg_logo.jpg"
                                                         alt="lenovo" class="fas fa-apple-alt me-2"/>LG</a>
                                        <span>(${productCountByFactory[5].count})</span>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>


<!-- Single Product End -->

<jsp:include page="../layout/footer.jsp"/>


<!-- Back to Top -->
<a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top"><i
        class="fa fa-arrow-up"></i></a>


<!-- JavaScript Libraries -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="/client/lib/easing/easing.min.js"></script>
<script src="/client/lib/waypoints/waypoints.min.js"></script>
<script src="/client/lib/lightbox/js/lightbox.min.js"></script>
<script src="/client/lib/owlcarousel/owl.carousel.min.js"></script>
<script
        src="https://cdnjs.cloudflare.com/ajax/libs/jquery-toast-plugin/1.3.2/jquery.toast.min.js"></script>


<!-- Template Javascript -->
<script src="/client/js/main.js"></script>
<script>
    $(document).ready(function () {
        const csrfToken = $('#csrf').val();
        const productId = $('#productId').val(); // gán từ input hidden
        console.log("productId:", productId); 
        const sessionName = '${sessionScope.fullName}';
        const sessionEmail = '${sessionScope.email}';

        // Xử lý đánh giá sao
        $('.fa-star').click(function () {
            const value = $(this).data('value');
            $('#rating').val(value);
            $('.fa-star').removeClass('text-muted');
            $('.fa-star').each(function () {
                if ($(this).data('value') <= value) {
                    $(this).addClass('text-muted');
                }
            });
        });

        // Gửi đánh giá sản phẩm
        $('.submit-comment').click(function () {
            const rating = $('#rating').val();
            const name = sessionName || $('#name').val();
            const email = sessionEmail || $('#email').val();
            const content = $('#content').val();

            sendComment(productId, {
                rate: rating,
                userName: name,
                email: email,
                message: content,
                parentCommentId: null
            }, function () {
                if (productId) {
                    location.href = `/product/`+ productId;
                } else {
                    console.error("Missing productId. Cannot redirect.");
                }
            });
        });

        // Mở form phản hồi
        $('.reply-button').click(function () {
            const commentId = $(this).data('comment-id');
            $(`#reply-form-${commentId}`).toggle();
        });

        // Gửi phản hồi bình luận
        $('.submitReply').click(function () {
            const form = $(this).closest('.reply-form');
            const parentCommentId = form.find('.parentCommentId').val();
            const message = form.find('.replyMessage').val();
            const name = sessionName || form.find('.name').val();
            const email = sessionEmail || form.find('.email').val();

            sendComment(productId,{
                rate: null,
                userName: name,
                email: email,
                message: message,
                parentCommentId: parentCommentId
            }, function () {
                const html = `
                    <div class="reply">
                        <p><strong>${name}</strong></p>
                        <p>${message}</p>
                    </div>`;
                $(`#reply-container-${parentCommentId}`).append(html);
                form.find('.replyMessage').val('');
                form.hide();
            });
        });

        function sendComment(productId, data, onSuccess) {
        const url = "/api/products/" + productId + "/comments";
        console.log("Calling URL:", url);
            $.ajax({
                url: url,
                type: 'POST',
                contentType: 'application/json',
                headers: {
                    'X-CSRF-TOKEN': csrfToken
                },
                data: JSON.stringify(data),
                success: function (response) {
                    $.toast({
                        text: 'Đánh giá thành công',
                        showHideTransition: 'slide',
                        bgColor: '#28a745',
                        textColor: 'white',
                        allowToastClose: true,
                        hideAfter: 1000,
                        stack: 5,
                        textAlign: 'left',
                        position: 'top-right',
                        icon: 'success'
                    });
                    if (onSuccess) onSuccess();
                },
                error: function (xhr) {
                    $.toast({
                        text: 'Đánh giá thất bại',
                        showHideTransition: 'slide',
                        bgColor: '#dc3545',
                        textColor: 'white',
                        allowToastClose: true,
                        hideAfter: 1000,
                        stack: 5,
                        textAlign: 'left',
                        position: 'top-right',
                        icon: 'error'
                    });
                    console.error("Error response:", xhr.responseText);
                }
            });
        }

    });
</script>

</body>

</html>
