package com.payhere.cafe.service;

import com.payhere.cafe.dto.ProductDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ProductService {
    ProductDTO registerProduct(ProductDTO productDTO, HttpServletRequest request);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO, HttpServletRequest request);

    String deleteProduct(Long productId, HttpServletRequest request);

    List<ProductDTO> selectProduct(int page, HttpServletRequest request);

    List<ProductDTO> searchProduct(String name, HttpServletRequest request);
}
