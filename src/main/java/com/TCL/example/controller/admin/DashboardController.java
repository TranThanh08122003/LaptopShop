package com.TCL.example.controller.admin;


import com.TCL.example.service.OrderService;
import com.TCL.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/admin")
    public String getDashboard(Model model) {
        model.addAttribute("countUsers",this.userService.countUsers());
        model.addAttribute("countOrders",this.userService.countOrders());
        model.addAttribute("countProducts",this.userService.countProducts());
        model.addAttribute("totalAmount",this.orderService.getTotalAmountByMonth());
        return "admin/dashboard/show";
    }
}