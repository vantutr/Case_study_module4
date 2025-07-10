package com.codegym.fashionshop.service.impl;

import com.codegym.fashionshop.dto.ProductDto;
import com.codegym.fashionshop.model.Category;
import com.codegym.fashionshop.model.Product;
import com.codegym.fashionshop.repository.CategoryRepository;
import com.codegym.fashionshop.repository.ProductRepository;
import com.codegym.fashionshop.service.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

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
        // Lấy file ảnh từ DTO
        MultipartFile imageFile = productDto.getImageFile();
        String fileName = imageFile.getOriginalFilename();

        // Tạo hoặc tìm đối tượng Product
        Product product = new Product();
        if(productDto.getId() != null){
            product = findById(productDto.getId());
        }

        // Xử lý upload file nếu có file mới được chọn
        if (fileName != null && !fileName.isEmpty()) {
            try {
                // Tạo tên file duy nhất để tránh trùng lặp
                String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

                // Lấy đường dẫn thực đến thư mục uploads
                String uploadPath = servletContext.getRealPath("/static/uploads/products/");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }

                // Lưu file vào thư mục
                File destination = new File(uploadDir.getAbsolutePath() + File.separator + uniqueFileName);
                imageFile.transferTo(destination);

                // Cập nhật tên file mới vào DTO để lưu vào DB
                productDto.setImageUrl(uniqueFileName);

            } catch (IOException e) {
                e.printStackTrace();
                // Có thể xử lý lỗi upload ở đây
            }
        }

        // Chuyển đổi thông tin từ DTO sang Entity
        BeanUtils.copyProperties(productDto, product);

        // Lấy đối tượng Category từ DB và gán cho sản phẩm
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        product.setCategory(category);

        // Lưu sản phẩm vào DB
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

            // 3. Sao chép các thuộc tính có tên giống nhau từ Entity sang DTO
            BeanUtils.copyProperties(product, productDto);

            // 4. Gán categoryId cho DTO một cách thủ công
            // (vì tên thuộc tính khác nhau: 'category' và 'categoryId')
            if (product.getCategory() != null) {
                productDto.setCategoryId(product.getCategory().getId());
            }

            // 5. Trả về DTO đã hoàn thiện
            return productDto;
        }

        return null;
    }
}