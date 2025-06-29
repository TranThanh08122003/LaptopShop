package com.TCL.example.controller.client;

import com.TCL.example.domain.Category;
import com.TCL.example.domain.DTO.RegisterDTO;
import com.TCL.example.domain.Order;
import com.TCL.example.domain.Product;
import com.TCL.example.domain.User;
import com.TCL.example.domain.request.ResetPassword;
import com.TCL.example.service.CategoryService;
import com.TCL.example.service.OrderService;
import com.TCL.example.service.ProductService;
import com.TCL.example.service.UploadService;
import com.TCL.example.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final ProductService productService;

    private final UserService userService;
    private final OrderService orderService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryService categoryService;
        @ModelAttribute("categories")
        public List<Category> populateCategories() {
            return categoryService.getAllCategories();
        }
    @GetMapping("/")
    public String getHomePage(Model model) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> products = this.productService.fetchProducts(pageable);
        List<Product> productList = new ArrayList<>(products.getContent()); // Convert to modifiable list

        // Sắp xếp sản phẩm theo rate theo thứ tự giảm dần
        productList.sort((p1, p2) -> {
            double avgRate1 = this.productService.getAvgRate(p1);
            double avgRate2 = this.productService.getAvgRate(p2);
            return Double.compare(avgRate2, avgRate1);
        });
        model.addAttribute("products", productList);
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
                                    BindingResult  registerUserbindingResult
                                 ) {

        List<FieldError> errors = registerUserbindingResult.getFieldErrors();
        for (FieldError error : errors ) {
            System.out.println (error.getField() + " - " + error.getDefaultMessage());
        }

        if (registerUserbindingResult.hasErrors()) {
            return "client/auth/register";
        }

        User user = this.userService.registerDTOtoUser(registerDTO);

        String hashPassword = this.passwordEncoder.encode(user.getPassword());

        user.setPassword(hashPassword);
        user.setRole(this.userService.getRoleByName("USER"));
        // save
        this.userService.handleSaveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {

        return "client/auth/login";
    }

    @GetMapping("/access-denied")
    public String getDenyPage(Model model) {

        return "client/auth/deny";
    }

    @GetMapping("/order-history")
    public String getOrderHistoryPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User currentUser = new User();
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        List<Order> orders = this.orderService.fetchOrdersByUser(currentUser);
        model.addAttribute("orders", orders);
        return "client/cart/order-history";
    }

    @GetMapping("/show-info")
    public String getShowInfoPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User currentUser = new User();
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        return "client/info/show-info";
    }
    @GetMapping("/info-setting")
    public String getInfoSettingPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User currentUser = new User();
        long id = (long) session.getAttribute("id");
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        return "client/info/info-setting";
    }

    @PostMapping("/info-setting")
    public String handleInfoSetting(Model model,@ModelAttribute("user") User user,
                                    @RequestParam("uploadAvatarFile") MultipartFile file) {
        User currentUser = this.userService.getUserById(user.getId());

        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        model.addAttribute("user", currentUser);
        if (currentUser != null) {
            currentUser.setDob(user.getDob());
            currentUser.setFullName(user.getFullName());
            currentUser.setAddress(user.getAddress());
            currentUser.setPhoneNumber(user.getPhoneNumber());
            currentUser.setGender(user.getGender());
            currentUser.setAvatar(avatar);
            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/show-info";
    }

    @GetMapping("/forgot-password/verify-email")
    public String verifyEmail(Model model) {
        return "client/auth/verifyEmail";
    }

    @GetMapping("/forgot-password/verify-otp")
    public String verifyOtp(Model model) {
        return "client/auth/verifyOtp";
    }

   @GetMapping("/forgot-password/reset-password/{email}")
   public String resetPassword(Model model, @PathVariable String email) {
       User user = this.userService.getUserByEmail(email);
       model.addAttribute("email", email);
       model.addAttribute("user", user);
       model.addAttribute("resetPassword", new ResetPassword());
       return "client/auth/resetPassword";
   }
   
}
