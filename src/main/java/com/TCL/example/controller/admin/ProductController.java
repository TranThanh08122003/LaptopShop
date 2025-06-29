package com.TCL.example.controller.admin;

import com.TCL.example.service.CategoryService;
import com.TCL.example.service.FactoryService;
import com.TCL.example.domain.Product;
import com.TCL.example.domain.Category;
import com.TCL.example.domain.ProductImage;
import com.TCL.example.service.ProductImageService;
import com.TCL.example.service.ProductService;
import com.TCL.example.service.UploadService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;
    private  final UploadService uploadService;
    private final CategoryService categoryService; 
    private final ProductImageService productImageService;
    private final FactoryService factoryService;
    public ProductController(ProductService productService,
                             UploadService uploadService,
                             ProductImageService productImageService,
                             CategoryService categoryService,
                             FactoryService factoryService) {
        this.productService = productService;
        this.uploadService = uploadService;
        this.productImageService = productImageService;
        this.categoryService = categoryService;
        this.factoryService = factoryService;
    }

    @GetMapping("/admin/product")
    public String getProductPage(Model model,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) String factory,
                                    @RequestParam(required = false) Long categoryId,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(required = false) String factoryName
                                     ) {
        Pageable pageable = PageRequest.of(page-1, 10);
         Page<Product> products = productService.getAllProducts(name, factory, categoryId, pageable);
    List<Category> categories = productService.getAllCategories();

    model.addAttribute("products", products.getContent());
    model.addAttribute("totalPages", products.getTotalPages());
    model.addAttribute("currentPage", page);
    model.addAttribute("name", name);
    model.addAttribute("factory", factory);
    model.addAttribute("categoryId", categoryId);
    model.addAttribute("categories", categories);
    model.addAttribute("factoryList", factoryService.findAll()); // ✅ thêm dòng này
    return "admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("factoryList", factoryService.findAll()); 
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String createProduct(Model model,
                                @ModelAttribute("newProduct") @Valid Product product,
                                BindingResult newProductBindingResult,
                                @RequestParam("uploadProductFile") MultipartFile[] files,
                                RedirectAttributes redirectAttributes) {

        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for (FieldError error : errors ) {
            System.out.println (error.getField() + " - " + error.getDefaultMessage());
        }

        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }


        List<String> ProductImages = this.uploadService.UploadFiles(files, "product");
        List<ProductImage> productImageList = new ArrayList<>();
        for(String image : ProductImages){
            ProductImage productImage = new ProductImage();
            productImage.setImage(image);
            productImage.setProduct(product);
            productImageList.add(productImage);
        }
        product.setProductImages(productImageList);
        this.productService.handleSaveProduct(product);
        this.productImageService.handleSaveProductImage(productImageList);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm thành công!");
        return "redirect:/admin/product";
    }

    @RequestMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.fetchProductById(id).get();
        model.addAttribute("product", product);
        model.addAttribute("id", id);
        model.addAttribute("ProductImages", product.getProductImages());
        return "admin/product/detail";
    }

@GetMapping("/admin/product/update/{id}")
public String getUpdateProductPage(Model model, @PathVariable long id) {
    Optional<Product> currentProduct = this.productService.fetchProductById(id);

    if (currentProduct.isEmpty()) {
        model.addAttribute("errorMessage", "Không tìm thấy sản phẩm với ID: " + id);
        return "redirect:/admin/product";
    }

    Product product = currentProduct.get();
    model.addAttribute("newProduct", product);
    model.addAttribute("id", id);
    model.addAttribute("ProductImages", product.getProductImages());
    model.addAttribute("categories", categoryService.findAll());
    model.addAttribute("factoryList", factoryService.findAll());

    return "admin/product/update";
}


@PostMapping("/admin/product/update")
public String updateProduct(@ModelAttribute("newProduct") @Valid Product product,
                            BindingResult bindingResult,
                            @RequestParam("uploadProductFile") MultipartFile[] files,
                            @RequestParam("category.id") Long categoryId,
                            RedirectAttributes redirectAttributes,
                            Model model) {

    if (bindingResult.hasErrors()) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("factoryList", factoryService.findAll());
        return "admin/product/update";
    }

    Optional<Product> optionalProduct = productService.fetchProductById(product.getId());
    if (optionalProduct.isPresent()) {
        Product currentProduct = optionalProduct.get();

        // Cập nhật thông tin
        currentProduct.setName(product.getName());
        currentProduct.setPrice(product.getPrice());
        currentProduct.setQuantity(product.getQuantity());
        currentProduct.setSold(product.getSold());
        currentProduct.setFactory(product.getFactory());
        currentProduct.setTarget(product.getTarget());
        currentProduct.setProcessor(product.getProcessor());
        currentProduct.setRam(product.getRam());
        currentProduct.setStorage(product.getStorage());
        currentProduct.setDisplay(product.getDisplay());
        currentProduct.setResolution(product.getResolution());
        currentProduct.setGraphicCard(product.getGraphicCard());
        currentProduct.setWeight(product.getWeight());
        currentProduct.setShortDesc(product.getShortDesc());
        currentProduct.setDetailDesc(product.getDetailDesc());

        // Cập nhật category
        Category category = categoryService.findById(categoryId);
        currentProduct.setCategory(category);

        // Nếu có file ảnh mới thì xử lý ảnh
        if (files != null && files.length > 0 && !files[0].getOriginalFilename().isBlank()) {
            productImageService.deleteProductImagesByProductId(currentProduct.getId());
            currentProduct.getProductImages().clear();

            List<String> uploaded = uploadService.UploadFiles(files, "product");
            List<ProductImage> imageList = new ArrayList<>();
            for (String img : uploaded) {
                ProductImage productImage = new ProductImage();
                productImage.setImage(img);
                productImage.setProduct(currentProduct);
                imageList.add(productImage);
            }
            currentProduct.setProductImages(imageList);
            productImageService.handleSaveProductImage(imageList);
        }

        // Lưu lại sản phẩm
        productService.handleSaveProduct(currentProduct);

        redirectAttributes.addFlashAttribute("successMessage", "Sửa sản phẩm thành công!");
    } else {
        redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm để cập nhật.");
    }

    return "redirect:/admin/product";
}


    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model,@PathVariable long id) {
        model.addAttribute("newProduct", new Product());
        model.addAttribute("id", id);
        return "admin/product/delete";
    }

@PostMapping("/admin/product/delete")
public String postDeleteProduct(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
    Optional<Product> optionalProduct = productService.fetchProductById(id);
    if (optionalProduct.isPresent()) {
        Product currentProduct = optionalProduct.get();
        productImageService.deleteProductImagesByProductId(currentProduct.getId());
        productService.deleteProductById(currentProduct.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
    } else {
        redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm cần xoá.");
    }
    return "redirect:/admin/product";
}

    @GetMapping("/api/products/search")
    public ResponseEntity<Object> searchProducts(@RequestParam("keyword") String keyword) {
        List<Product> products = this.productService.searchProduct(keyword);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/admin/product/add-quantity/{id}")
    public String getAddQuantityPage(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.fetchProductById(id);
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            return "admin/product/add_quantity";
        }
        model.addAttribute("errorMessage", "Không tìm thấy sản phẩm.");
        return "redirect:/admin/product";
    }

    @PostMapping("/admin/product/add-quantity")
    public String postAddQuantity(@RequestParam Long id,
                                @RequestParam Long addedQuantity,
                                RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productService.fetchProductById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setQuantity(product.getQuantity() + addedQuantity);
            productService.handleSaveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm số lượng thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm.");
        }
        return "redirect:/admin/product";
    }

}
