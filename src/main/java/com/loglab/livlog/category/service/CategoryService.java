package com.loglab.livlog.category.service;

import com.loglab.livlog.category.entity.Category;
import com.loglab.livlog.category.repository.CategoryRepository;
import com.loglab.livlog.global.exception.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAllByUser(Long userId) {
        return categoryRepository.findByUserId(userId);
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Long id, String name) {
        Category entity = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Category not found"));
        entity = Category.builder()
                .id(id)
                .name(name)
                .user(entity.getUser())
                .activated(entity.getActivated())
                .build();
        return categoryRepository.save(entity);
    }

    public void delete(Long id) {
        Category entity = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Category not found"));
        entity.deactivate();
        categoryRepository.save(entity);
    }
}
