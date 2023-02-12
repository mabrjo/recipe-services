package com.mcccodeschool.recipeservices.service;

import com.mcccodeschool.recipeservices.dto.CategoryDTO;
import com.mcccodeschool.recipeservices.model.Category;
import com.mcccodeschool.recipeservices.model.Collection;
import com.mcccodeschool.recipeservices.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> allCategoriesDTO = new ArrayList<>();
        categoryRepository.findAll().stream().forEach((r) -> allCategoriesDTO.add(
                modelMapper.map(r, CategoryDTO.class)
        ));
        return allCategoriesDTO;
    }

    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()){
            CategoryDTO categoryDTO = new CategoryDTO(id, category.get().getName());
            return categoryDTO;
        } else {
            return null;
        }
    }
}
