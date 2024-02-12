package com.payhere.cafe.controller;

import com.payhere.cafe.dto.ProductDTO;
import com.payhere.cafe.model.CommonResponse;
import com.payhere.cafe.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @PostMapping
    public ResponseEntity<CommonResponse<ProductDTO>> registerProduct(@RequestBody @Valid ProductDTO productDTO,
                                                                      HttpServletRequest request) {
        logger.debug("상품을 등록합니다.");
        CommonResponse<ProductDTO> successResponse = new CommonResponse<>(CommonResponse.Meta.builder().code(200).message("ok").build(),
                productService.registerProduct(productDTO, request));
        return ResponseEntity.ok(successResponse);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<CommonResponse<ProductDTO>> updateProduct(@PathVariable("productId") Long productId,
                                                                    @RequestBody @Valid ProductDTO productDTO,
                                                                    HttpServletRequest request) {
        logger.debug("상품을 수정합니다.");
        CommonResponse<ProductDTO> successResponse = new CommonResponse<>(CommonResponse.Meta.builder().code(200).message("ok").build(),
                productService.updateProduct(productId, productDTO, request));
        return ResponseEntity.ok(successResponse);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonResponse<String>> deleteProduct(@PathVariable("productId") Long productId,
                                                                HttpServletRequest request) {
        logger.debug("상품을 수정합니다.");
        CommonResponse<String> successResponse = new CommonResponse<>(CommonResponse.Meta.builder().code(200).message("ok").build(),
                productService.deleteProduct(productId, request));
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ProductDTO>>> selectProduct(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                          HttpServletRequest request) {
        logger.debug("상품을 수정합니다.");
        CommonResponse<List<ProductDTO>> successResponse = new CommonResponse<>(CommonResponse.Meta.builder().code(200).message("ok").build(),
                productService.selectProduct(page, request));
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<ProductDTO>>> searchProduct(@RequestBody ProductDTO productDTO,
                                                                          HttpServletRequest request) {
        logger.debug("상품을 수정합니다.");
        CommonResponse<List<ProductDTO>> successResponse = new CommonResponse<>(CommonResponse.Meta.builder().code(200).message("ok").build(),
                productService.searchProduct(productDTO.getName(), request));
        return ResponseEntity.ok(successResponse);
    }
}
