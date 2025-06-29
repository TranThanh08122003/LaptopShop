package com.TCL.example.controller.client;

import com.TCL.example.domain.*;
import com.TCL.example.domain.DTO.ProductCriteriaDTO;
import com.TCL.example.repository.CartRepository;
import com.TCL.example.service.ProductService;
import com.TCL.example.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ProductService productService;
    private final UserService userService;
    private final CartRepository cartRepository;

    @GetMapping("/product/{id}")
    public String getProductPage(Model model, @PathVariable long id) {
        Product product = this.productService.fetchProductById(id).get();
        List<Map<String, Object>> productCountByFactory = this.productService.countProductsByFactory();
        List<Comment> comments = product.getComments();
        double totalRate = 0;
        for(Comment comment : comments){
            totalRate += comment.getRate();
        }
        double avgRate = totalRate != 0 ? totalRate / comments.size() : 0;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String email = ((UserDetails) principal).getUsername();
                User user = this.userService.getUserByEmail(email);
                Cart cart = this.cartRepository.findByUser(user);
                if (cart != null) {
                    List<CartDetail> cartDetail = cart.getCartDetails();
                    for (CartDetail cd : cartDetail) {
                        if (cd.getProduct().getId() == id) {
                            model.addAttribute("cartQuantity", cd.getQuantity());
                        }
                    }
                }
            }
        }

        model.addAttribute("product", product);
        model.addAttribute("id", id);
        model.addAttribute("ProductImages", product.getProductImages());
        model.addAttribute("productCountByFactory", productCountByFactory);
        model.addAttribute("comments", product.getComments());
        model.addAttribute("avgRate", avgRate);

        return "client/product/detail";
    }

    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        long productId = id;
        String email = (String) session.getAttribute("email");
        this.productService.handleAddProductToCart(email, productId, session,1);

        return "redirect:/";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {
        User currentUser = new User();
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);
        Cart cart = this.productService.fetchCartByUser(currentUser);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        double totalPrice = 0;
        for (CartDetail cartDetail : cartDetails) {
            totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
        }

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cart", cart);
            if(session != null) {
        session.setAttribute("totalPrice", totalPrice);
    }
        return "client/cart/show";
    }

    @PostMapping("/delete-cart-product/{id}")
    public String deleteCartProduct(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long cartDetailId = id;
        this.productService.handleRemoveCartDetail(cartDetailId, session);
        return "redirect:/cart";
    }

@GetMapping("/checkout")
public String getCheckOutPage(Model model, HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    User currentUser = new User();
    long id = (long) session.getAttribute("id");
    currentUser.setId(id);

    Cart cart = this.productService.fetchCartByUser(currentUser);
    List<CartDetail> cartDetails = cart == null ? new ArrayList<>() : cart.getCartDetails();

    double totalPrice = 0;
    for (CartDetail cd : cartDetails) {
        totalPrice += cd.getPrice() * cd.getQuantity();
    }

    // Nếu có giá đã giảm trong session thì dùng
    Double finalPrice = (Double) session.getAttribute("finalPrice");
    if (finalPrice == null) {
        finalPrice = totalPrice;
    }

    model.addAttribute("cartDetails", cartDetails);
    model.addAttribute("totalPrice", finalPrice); // ⚠ dùng finalPrice thay vì totalPrice gốc
    model.addAttribute("cart", cart);

    return "client/cart/checkout";
}


    @PostMapping("/confirm-checkout")
    public String getCheckOutPage(@ModelAttribute("cart") Cart cart) {
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        this.productService.handleUpdateCartBeforeCheckout(cartDetails);
        return "redirect:/checkout";
    }

@PostMapping("/place-order")
public String handlePlaceOrder(
        HttpServletRequest request,
        @RequestParam("receiverName") String receiverName,
        @RequestParam("receiverAddress") String receiverAddress,
        @RequestParam("receiverPhone") String receiverPhone,
        @RequestParam(value = "couponCode", required = false) String couponCode) {

    HttpSession session = request.getSession(false);
    if (session == null) {
        return "redirect:/cart";
    }

    // Tránh xử lý trùng lặp
    Boolean orderPlaced = (Boolean) session.getAttribute("orderPlaced");
    if (Boolean.TRUE.equals(orderPlaced)) {
        return "redirect:/thanks";
    }

    // Đặt hàng
    long id = (long) session.getAttribute("id");
    User currentUser = new User();
    currentUser.setId(id);

    this.productService.handlePlaceOrder(currentUser, session, receiverName, receiverAddress, receiverPhone, couponCode);

    // Đánh dấu đã xử lý
    session.setAttribute("orderPlaced", true);

    return "redirect:/thanks";
}


    @GetMapping("/thanks")
    public String getThanksPage() {
        return "client/cart/thanks";
    }

    @PostMapping("/add-product-from-view-detail")
    public String handleAddProductFromViewDetail(@RequestParam("quantity") long quantity,
                                                 @RequestParam("id") long id,
                                                 HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String email = (String) session.getAttribute("email");
        this.productService.handleAddProductToCart(email, id, session, quantity);
        return "redirect:/product/" + id;
    }

@GetMapping("/products")
public String getProductPage(Model model,
                             ProductCriteriaDTO productCriteriaDTO,
                             HttpServletRequest request) {
    int page = 1;
    try {
        if (productCriteriaDTO.getPage().isPresent()) {
            page = Integer.parseInt(productCriteriaDTO.getPage().get());
        }
    } catch (Exception e) {
        // ignore
    }

    Pageable pageable = PageRequest.of(page - 1, 6);
    if (productCriteriaDTO.getSort() != null && productCriteriaDTO.getSort().isPresent()) {
        String sort = productCriteriaDTO.getSort().get();
        if (sort.equals("gia-tang-dan")) {
            pageable = PageRequest.of(page - 1, 6, Sort.by(Product_.PRICE).ascending());
        } else if (sort.equals("gia-giam-dan")) {
            pageable = PageRequest.of(page - 1, 6, Sort.by(Product_.PRICE).descending());
        }
    }

    // ⚠️ Lọc theo danh mục nếu có
    Page<Product> prs = productService.fetchProductsWithSpec(pageable, productCriteriaDTO);
    List<Product> products = prs.getContent();

    String qs = request.getQueryString();
    if (qs != null && !qs.isBlank()) {
        qs = qs.replaceAll("page=" + page, "");
    }
    model.addAttribute("categories", productService.getAllCategories());
    model.addAttribute("products", products);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", prs.getTotalPages());
    model.addAttribute("queryString", qs);

    return "client/product/show";
}

}

