package com.payhere.cafe.repository;

import com.payhere.cafe.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(String name);

    void deleteByName(String name);
}
