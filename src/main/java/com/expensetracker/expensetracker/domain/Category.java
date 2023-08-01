package com.expensetracker.expensetracker.domain;

public class Category {
    private Integer categoryId;
    private Integer userId;
    private String title;
    private String descriptionn;
    private Double totalExpense;
    
    public Category(Integer categoryId, Integer userId, String title, String descriptionn, Double totalExpense) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.title = title;
        this.descriptionn = descriptionn;
        this.totalExpense = totalExpense;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return descriptionn;
    }
    public void setDescription(String descriptionn) {
        this.descriptionn = descriptionn;
    }
    public Double getTotalExpense() {
        return totalExpense;
    }
    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    
}
