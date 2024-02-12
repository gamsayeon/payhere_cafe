package com.payhere.cafe.modelmapper;

import com.payhere.cafe.dto.ProductDTO;
import com.payhere.cafe.model.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductModelMapper {
    private final ModelMapper modelMapper;

    public Product convertToEntity(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        return product;
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }
}
