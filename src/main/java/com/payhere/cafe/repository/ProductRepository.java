package com.payhere.cafe.repository;

import com.payhere.cafe.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);

    void deleteByProductId(Long productId);

    Page<Product> findByUserId(Long userId, Pageable pageable);
}
