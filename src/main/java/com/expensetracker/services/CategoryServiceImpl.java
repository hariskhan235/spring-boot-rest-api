package com.expensetracker.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.expensetracker.domain.Category;
import com.expensetracker.expensetracker.exceptions.EtBadRequestException;
import com.expensetracker.expensetracker.exceptions.EtResourceNotFoundException;
import com.expensetracker.expensetracker.repositories.CategoryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> fetchCategories(Integer userId) {
        return categoryRepository.findAll(userId);
    }

    @Override
    public Category fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        return categoryRepository.findById(userId, categoryId);
    }

    @Override
    public Category addCategory(Integer userId, String title, String descriptionn) throws EtBadRequestException {

        int categoryId = categoryRepository.create(userId, title, descriptionn);
        return categoryRepository.findById(userId, categoryId);
    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {
        categoryRepository.update(userId, categoryId, category);

    }

    @Override
    public void removeCategoryWithAllTransactions(Integer userId, Integer categoryId)
            throws EtResourceNotFoundException {
        this.fetchCategoryById(userId, categoryId);
        categoryRepository.removeById(userId, categoryId);
    }

}
