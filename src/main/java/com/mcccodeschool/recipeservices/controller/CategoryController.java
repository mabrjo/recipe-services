package com.mcccodeschool.recipeservices.controller;

import com.mcccodeschool.recipeservices.dto.CategoryDTO;
import com.mcccodeschool.recipeservices.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/api/v2/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService collectionService) {
        this.categoryService = collectionService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllCategories() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error : " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        if (categoryDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found with id: " + id);
        }
        return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);
    }
}
