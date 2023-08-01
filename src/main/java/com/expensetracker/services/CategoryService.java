package com.expensetracker.services;

import java.util.List;

import com.expensetracker.expensetracker.domain.Category;
import com.expensetracker.expensetracker.exceptions.EtBadRequestException;
import com.expensetracker.expensetracker.exceptions.EtResourceNotFoundException;

public interface CategoryService {
    
    List<Category> fetchCategories(Integer userId);

    Category fetchCategoryById(Integer userId ,Integer categoryId ) throws EtResourceNotFoundException;

    Category addCategory(Integer userId , String title , String description) throws EtBadRequestException;

    void updateCategory(Integer userId , Integer categoryId , Category category) throws EtBadRequestException;

    void removeCategoryWithAllTransactions(Integer userId , Integer categoryId) throws EtResourceNotFoundException;

}
