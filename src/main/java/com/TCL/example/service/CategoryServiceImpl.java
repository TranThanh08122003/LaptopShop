// package com.TCL.example.service;


// import com.TCL.example.domain.Category;
// import com.TCL.example.repository.CategoryRepository;
// import com.TCL.example.service.CategoryService;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class CategoryServiceImpl implements CategoryService {

//     private final CategoryRepository categoryRepository;

//     public CategoryServiceImpl(CategoryRepository categoryRepository) {
//         this.categoryRepository = categoryRepository;
//     }

//     @Override
//     public List<Category> getAllCategories() {
//         return categoryRepository.findAll();
//     }

//     @Override
//     public Category getCategoryById(Long id) {
//         return categoryRepository.findById(id).orElse(null);
//     }
// }
