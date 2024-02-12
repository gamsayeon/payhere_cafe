package com.payhere.cafe.service;

import jakarta.servlet.http.HttpServletRequest;

public interface CategoryService {
    String deleteCategory(String name, HttpServletRequest request);
}
