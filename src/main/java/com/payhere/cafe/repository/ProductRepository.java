package com.payhere.cafe.repository;

import com.payhere.cafe.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);

    void deleteByProductId(Long productId);

    Page<Product> findByUserId(Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM product p WHERE p.name LIKE %:name%", nativeQuery = true)
    List<Product> findByNameLike(String name);

    @Query(value = "SELECT * FROM product p WHERE p.choseong_name LIKE %:name%", nativeQuery = true)
    List<Product> findByChoseongNameLike(String name);
}
