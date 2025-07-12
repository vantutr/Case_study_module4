package com.codegym.fashionshop.service.impl;

import com.codegym.fashionshop.dto.ProductVariantDto;
import com.codegym.fashionshop.model.Product;
import com.codegym.fashionshop.model.ProductVariant;
import com.codegym.fashionshop.repository.ProductRepository;
import com.codegym.fashionshop.repository.ProductVariantRepository;
import com.codegym.fashionshop.service.IProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductVariantServiceImpl implements IProductVariantService {

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addStock(ProductVariantDto variantDto) {
        // Tìm xem biến thể với size này đã tồn tại cho sản phẩm chưa
        Optional<ProductVariant> existingVariantOpt = variantRepository
                .findByProduct_IdAndSizeIgnoreCase(variantDto.getProductId(), variantDto.getSize());

        if (existingVariantOpt.isPresent()) {
            // Nếu đã tồn tại, cộng thêm số lượng vào kho
            ProductVariant existingVariant = existingVariantOpt.get();
            existingVariant.setQuantity(existingVariant.getQuantity() + variantDto.getQuantity());
            variantRepository.save(existingVariant);
        } else {
            // Nếu chưa tồn tại, tạo một biến thể mới
            Product product = productRepository.findById(variantDto.getProductId()).orElse(null);
            if (product != null) {
                ProductVariant newVariant = new ProductVariant();
                newVariant.setProduct(product);
                newVariant.setSize(variantDto.getSize().toUpperCase());
                newVariant.setQuantity(variantDto.getQuantity());
                variantRepository.save(newVariant);
            }
        }
    }

    @Override
    public void update(ProductVariantDto variantDto) {
        // Tìm biến thể hiện có bằng ID
        Optional<ProductVariant> variantOpt = variantRepository.findById(variantDto.getId());
        if (variantOpt.isPresent()) {
            ProductVariant variant = variantOpt.get();
            variant.setSize(variantDto.getSize().toUpperCase());
            variant.setQuantity(variantDto.getQuantity()); // Ghi đè số lượng tồn kho
            variantRepository.save(variant);
        }
        // Có thể throw exception nếu không tìm thấy
    }

    @Override
    public Long deleteById(Long variantId) {
        // Tìm biến thể để lấy productId trước khi xóa
        Optional<ProductVariant> variantOpt = variantRepository.findById(variantId);
        if (variantOpt.isPresent()) {
            ProductVariant variant = variantOpt.get();
            Long productId = variant.getProduct().getId();
            variantRepository.deleteById(variantId);
            return productId;
        }
        return null; // Hoặc throw exception
    }
}