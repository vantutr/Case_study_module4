package com.codegym.fashionshop.service.impl;

import com.codegym.fashionshop.dto.ProductDto;
import com.codegym.fashionshop.model.Category;
import com.codegym.fashionshop.model.Product;
import com.codegym.fashionshop.repository.CategoryRepository;
import com.codegym.fashionshop.repository.ProductRepository;
import com.codegym.fashionshop.service.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    private final String UPLOAD_DIRECTORY = "C:/fashionshop_uploads/products/";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Inject ServletContext để lấy đường dẫn thực của thư mục lưu ảnh
    @Autowired
    private ServletContext servletContext;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void save(ProductDto productDto) {
        Product product;
        String oldImageUrl = null;

        if (productDto.getId() != null) {
            product = productRepository.findById(productDto.getId()).orElse(new Product());
            oldImageUrl = product.getImageUrl();
        } else {
            product = new Product();
        }

        MultipartFile imageFile = productDto.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uniqueFileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

                File uploadDir = new File(UPLOAD_DIRECTORY);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                File destination = new File(uploadDir.getAbsolutePath() + File.separator + uniqueFileName);
                imageFile.transferTo(destination);
                product.setImageUrl(uniqueFileName);

                // Đoạn code xóa file cũ đã được sửa lại ở đây
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    File oldImageFile = new File(UPLOAD_DIRECTORY + oldImageUrl);
                    if (oldImageFile.exists()) {
                        oldImageFile.delete();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        if (productDto.getPrice() != null) {
            product.setPrice(java.math.BigDecimal.valueOf(productDto.getPrice()));
        }

        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
            product.setCategory(category);
        }

        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public long countTotalProducts() {
        return productRepository.count();
    }

    @Override
    public ProductDto findDtoById(Long id) {
        // 1. Tìm Product Entity từ database
        Product product = this.findById(id);

        if (product != null) {
            // 2. Tạo một đối tượng DTO mới
            ProductDto productDto = new ProductDto();

            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setImageUrl(product.getImageUrl());

            if (product.getPrice() != null) {
                productDto.setPrice(product.getPrice().doubleValue());
            }


            // (vì tên thuộc tính khác nhau: 'category' và 'categoryId')
            if (product.getCategory() != null) {
                productDto.setCategoryId(product.getCategory().getId());
            }

            // 5. Trả về DTO đã hoàn thiện
            return productDto;
        }

        return null;
    }

    @Override
    public Product findByIdWithVariants(Long id) {
        return productRepository.findByIdWithVariants(id).orElse(null);
    }

    @Override
    public List<Product> search(String keyword) {
        // Tìm theo tên sản phẩm trước
        List<Product> productsByName = productRepository.findByNameContainingIgnoreCase(keyword);

        // Dùng Set để tránh kết quả trùng lặp
        Set<Product> resultSet = new HashSet<>(productsByName);

        // Thử chuyển đổi keyword thành số để tìm theo ID
        try {
            Long id = Long.parseLong(keyword);
            productRepository.findById(id).ifPresent(resultSet::add);
        } catch (NumberFormatException e) {
            // Nếu không phải là số thì bỏ qua
        }

        return new ArrayList<>(resultSet);
    }

    @Override
    public List<Product> findNewestProducts() {
        return productRepository.findNewestProductsWithVariants();
    }

    @Override
    public List<Product> findBestsellerProducts(int limit) {
        // Bước 1: Lấy danh sách ID đã được sắp xếp
        List<Long> ids = productRepository.findBestsellerProductIds(PageRequest.of(0, limit));

        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        // Bước 2: Lấy thông tin sản phẩm từ danh sách ID
        List<Product> unsortedProducts = productRepository.findByIdsWithVariants(ids);

        // Bước 3: Sắp xếp lại danh sách sản phẩm theo đúng thứ tự của danh sách ID
        Map<Long, Product> productMap = unsortedProducts.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        return ids.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findRelatedProducts(Product product, int limit) {
        if (product.getCategory() == null) {
            return new ArrayList<>();
        }
        // Lấy tất cả sản phẩm trong cùng danh mục và loại trừ chính nó
        return productRepository.findByCategory(product.getCategory(), PageRequest.of(0, limit + 1))
                .stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Product> findAll(Specification<Product> spec, Pageable pageable) {
        // Bước 1: Lấy trang sản phẩm với các 'variants' chưa được tải (lazy)
        // Việc này để lấy đúng thông tin phân trang (tổng số sản phẩm, tổng số trang)
        Page<Product> lazyProductPage = productRepository.findAll(spec, pageable);
        List<Product> lazyProducts = lazyProductPage.getContent();

        if (lazyProducts.isEmpty()) {
            return lazyProductPage; // Trả về trang rỗng nếu không có sản phẩm
        }

        // Bước 2: Lấy danh sách ID từ các sản phẩm đã tìm được
        List<Long> ids = lazyProducts.stream().map(Product::getId).collect(Collectors.toList());

        // Bước 3: Dùng danh sách ID để tải lại sản phẩm với đầy đủ 'variants' (eager)
        // Chúng ta sử dụng lại phương thức findByIdsWithVariants đã tạo trước đó
        List<Product> fullyLoadedProducts = productRepository.findByIdsWithVariants(ids);

        // Bước 4: Sắp xếp lại danh sách đã tải đầy đủ theo đúng thứ tự của trang
        Map<Long, Product> productMap = fullyLoadedProducts.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<Product> sortedFullyLoadedProducts = ids.stream()
                .map(productMap::get)
                .collect(Collectors.toList());

        // Bước 5: Trả về một đối tượng Page mới với nội dung đã được tải đầy đủ
        return new PageImpl<>(sortedFullyLoadedProducts, pageable, lazyProductPage.getTotalElements());
    }
}