package com.payhere.cafe.controller;

import com.payhere.cafe.dto.CategoryDTO;
import com.payhere.cafe.model.CommonResponse;
import com.payhere.cafe.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/category")
@RestController
@Log4j2
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @DeleteMapping
    public ResponseEntity<CommonResponse<String>> deleteCategory(@RequestBody @Valid CategoryDTO categoryDTO, HttpServletRequest request) {
        logger.debug("카테고리를 삭제합니다.");
        CommonResponse<String> successResponse = new CommonResponse<>(CommonResponse.Meta.builder().code(200).message("ok").build(),
                categoryService.deleteCategory(categoryDTO.getName(), request));
        return ResponseEntity.ok(successResponse);
    }

}
