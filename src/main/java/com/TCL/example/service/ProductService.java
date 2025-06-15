package com.TCL.example.service;

import com.TCL.example.domain.*;
import com.TCL.example.domain.DTO.ProductCriteriaDTO;
import com.TCL.example.repository.*;
import com.TCL.example.service.specification.ProductSpecs;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TCL.example.service.UserService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CouponRepository couponRepository;
    private final CategoryRepository categoryRepository;
public ProductService(ProductRepository productRepository,
                      CartRepository cartRepository,
                      CartDetailRepository cartDetailRepository,
                      UserService userService,
                      OrderRepository orderRepository,
                      OrderDetailRepository orderDetailRepository,
                      CouponRepository couponRepository,
                      CategoryRepository categoryRepository) { 
    this.productRepository = productRepository;
    this.cartRepository = cartRepository;
    this.cartDetailRepository = cartDetailRepository;
    this.userService = userService;
    this.orderRepository = orderRepository;
    this.orderDetailRepository = orderDetailRepository;
    this.couponRepository = couponRepository;
    this.categoryRepository = categoryRepository;
}

    public Product handleSaveProduct(Product product){
        Product savedProduct = this.productRepository.save(product);
        return savedProduct;
    }

    public Optional<Product> fetchProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }




    public Page<Product> fetchProductsWithSpec(Pageable page, ProductCriteriaDTO productCriteriaDTO) {
        if(productCriteriaDTO.getTarget() == null
                && productCriteriaDTO.getFactory() == null
                && productCriteriaDTO.getPrice() == null){
            return this.productRepository.findAll(page);
        }

        Specification<Product> combinedSpec = Specification.where(null);

        if (productCriteriaDTO.getCategoryId().isPresent()) {
            Long categoryId = productCriteriaDTO.getCategoryId().get();
            combinedSpec = combinedSpec.and((root, query, cb) ->
                    cb.equal(root.get("category").get("id"), categoryId));
        }
        if (productCriteriaDTO.getTarget() != null && productCriteriaDTO.getTarget().isPresent()) {
            Specification<Product> currentSpecs = ProductSpecs.matchListTarget(productCriteriaDTO.getTarget().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }
        if (productCriteriaDTO.getFactory() != null && productCriteriaDTO.getFactory().isPresent()) {
            Specification<Product> currentSpecs = ProductSpecs.matchListFactory(productCriteriaDTO.getFactory().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        if(productCriteriaDTO.getPrice() != null && productCriteriaDTO.getPrice().isPresent()){
            Specification<Product> currentSpecs = this.buildPriceSpecification(productCriteriaDTO.getPrice().get());
            combinedSpec = combinedSpec.and(currentSpecs);
        }

        return this.productRepository.findAll(combinedSpec, page);
    }

    public Specification<Product>  buildPriceSpecification(List<String> price) {
        Specification<Product> combinedSpec = Specification.where(null);
        for (String p : price){
            double min = 0;
            double max = 0;
            switch (p) {
                case "duoi-10-trieu":
                    min = 1;
                    max = 10000000;
                    break;
                case "10-toi-15-trieu":
                    min = 10000000;
                    max = 15000000;
                    break;
                case "15-toi-20-trieu":
                    min = 15000000;
                    max = 20000000;
                    break;
                case "tren-20-trieu":
                    min = 20000000;
                    max = 500000000;
                    break;
            }
            if(min != 0 && max != 0){
                Specification<Product> rangeSpec = ProductSpecs.matchMultiplePrice(min, max);
                combinedSpec = combinedSpec.or(rangeSpec);
            }
        }

        return combinedSpec;
    }

    public Page<Product> fetchProducts(Pageable page) {
        return this.productRepository.findAll(page);
    }


    public void handleAddProductToCart(String email, long productId, HttpSession session, long quantity) {

        User user = this.userService.getUserByEmail(email);

        if(user != null){
            //check user đâ có Cart chưa? nếu chưa thì tạo mới
            Cart cart = this.cartRepository.findByUser(user);

            if(cart == null){
                // tạo mới cart
                Cart otherCart = new Cart();
                otherCart.setUser(user);
                otherCart.setSum(0);

                cart = this.cartRepository.save(otherCart);
            }

            //lưu cart-detail

            Optional<Product> productOptional = this.productRepository.findById(productId);
            if(productOptional.isPresent()){
                Product realProduct = productOptional.get();
                //check xem product đã có trong cart chưa

                CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, realProduct);

                if(oldDetail == null){
                    CartDetail cd =  new CartDetail();
                    cd.setCart(cart);
                    cd.setProduct(realProduct);
                    cd.setPrice(realProduct.getPrice());
                    cd.setQuantity(quantity);
                    this.cartDetailRepository.save(cd);

                    //update cart sum

                    int s = cart.getSum() + 1;
                    cart.setSum(s);
                    this.cartRepository.save(cart);
                    session.setAttribute("sum", s);
                }else {
                    oldDetail.setQuantity(oldDetail.getQuantity() + quantity);
                    this.cartDetailRepository.save(oldDetail);
                }


            }
        }


    }

    public Cart fetchCartByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public void handleRemoveCartDetail(long cartDetailId, HttpSession session) {
        Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(cartDetailId);
        if (cartDetailOptional.isPresent()) {
            CartDetail cartDetail = cartDetailOptional.get();

            Cart currentCart = cartDetail.getCart();
            // delete cart-detail
            this.cartDetailRepository.deleteById(cartDetailId);

            // update cart
            if (currentCart.getSum() > 1) {
                // update current cart
                int s = currentCart.getSum() - 1;
                currentCart.setSum(s);
                session.setAttribute("sum", s);
                this.cartRepository.save(currentCart);
            } else {
                // delete cart (sum = 1)
                this.cartRepository.deleteById(currentCart.getId());
                session.setAttribute("sum", 0);
            }
        }
    }

    public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails) {
        for (CartDetail cartDetail : cartDetails) {
            Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());

            if (cdOptional.isPresent()) {
                CartDetail currentCartDetail = cdOptional.get();
                currentCartDetail.setQuantity(cartDetail.getQuantity());
                this.cartDetailRepository.save(currentCartDetail);
            }
        }
    }
@Transactional
public void handlePlaceOrder(User user, HttpSession session,
                             String receiverName, String receiverAddress, String receiverPhone,
                             String couponCode) {

    Cart cart = this.cartRepository.findByUser(user);
    if (cart != null) {
        List<CartDetail> cartDetails = cart.getCartDetails();
        if (cartDetails != null) {

            Order order = new Order();
            order.setUser(user);
            order.setReceiverName(receiverName);
            order.setReceiverAddress(receiverAddress);
            order.setReceiverPhone(receiverPhone);
            order.setStatus("PENDING");
            order.setCreateAt(new Timestamp(System.currentTimeMillis()));

            Double finalPrice = (Double) session.getAttribute("finalPrice");

            if (finalPrice == null) {
                double totalPrice = 0;
                for (CartDetail cartDetail : cartDetails) {
                    totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
                }
                finalPrice = totalPrice;
            }

            order.setTotalPrice(finalPrice);

            // Xử lý giảm số lượng coupon nếu có
            if (couponCode != null && !couponCode.isEmpty()) {
                Coupon coupon = couponRepository.findByCode(couponCode);
                if (coupon != null && coupon.getQuantity() > 0) {
                    System.out.println("Số lượng coupon trước: " + coupon.getQuantity());
                    coupon.setQuantity(coupon.getQuantity() - 1);
                    coupon = couponRepository.save(coupon);
                    couponRepository.flush();
                    System.out.println("Số lượng coupon sau: " + coupon.getQuantity());

                    order.setCoupon(coupon); // bây giờ coupon đã được quản lý
                }
            }


            order = this.orderRepository.save(order);


            for (CartDetail cartDetail : cartDetails) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cartDetail.getProduct());
                orderDetail.setPrice(cartDetail.getPrice());
                orderDetail.setQuantity(cartDetail.getQuantity());
                this.orderDetailRepository.save(orderDetail);

                Product product = cartDetail.getProduct();
                product.setQuantity(product.getQuantity() - cartDetail.getQuantity());
                this.productRepository.save(product);
            }

            for (CartDetail cartDetail : cartDetails) {
                this.cartDetailRepository.deleteById(cartDetail.getId());
            }

            this.cartRepository.deleteById(cart.getId());

            session.setAttribute("sum", 0);
            session.removeAttribute("finalPrice");
            session.removeAttribute("appliedCouponCode"); // nếu có
        }
    }
}




    public List<Product> searchProduct(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    public List<Map<String, Object>> countProductsByFactory() {
        return productRepository.countProductsByFactory();
    }

    public Page<Product> getAllProducts(String name, String factory, Long categoryId, Pageable pageable) {
        return productRepository.filterProductByNameFactoryAndCategory(name, factory, categoryId, pageable);
    }

    public double getAvgRate(Product product) {
        List<Comment> comments = product.getComments();
        double totalRate = 0;
        int count = 0;
        for(Comment comment : comments){
            totalRate += comment.getRate();
            count++;
        }
        double avgRate = totalRate != 0 ? totalRate / count : 0;
        return avgRate;
    }

    public List<Product> findByCategoryId(Long categoryId) {
    return productRepository.findByCategoryId(categoryId);
    }
    public List<Product> findAll() {
    return productRepository.findAll();
    }
    public List<Category> getAllCategories() {
    return categoryRepository.findAll();
}
    
}
