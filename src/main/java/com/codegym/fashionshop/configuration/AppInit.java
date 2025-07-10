package com.codegym.fashionshop.configuration;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    // Giữ nguyên các phương thức này
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfiguration.class, WebSecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    // BỎ HOÀN TOÀN PHƯƠNG THỨC getServletFilters()
    // THAY THẾ BẰNG onStartup() ĐỂ KIỂM SOÁT TUYỆT ĐỐI

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // --- ĐĂNG KÝ FILTER THỦ CÔNG THEO ĐÚNG THỨ TỰ ---

        // 1. Đăng ký CharacterEncodingFilter ĐẦU TIÊN để giải quyết lỗi font
        FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("characterEncodingFilter", new CharacterEncodingFilter());
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(null, false, "/*");


        // 2. Đăng ký MultipartFilter THỨ HAI để xử lý file upload trước security
        // Tên "multipartFilter" phải khớp với tên bean đã định nghĩa trong AppConfiguration
        FilterRegistration.Dynamic multipartFilter = servletContext.addFilter("multipartFilter", new DelegatingFilterProxy());
        multipartFilter.addMappingForUrlPatterns(null, false, "/*");


        // 3. Đăng ký Spring Security Filter THỨ BA
        FilterRegistration.Dynamic securityFilter = servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy());
        securityFilter.addMappingForUrlPatterns(null, false, "/*");


        // 4. Gọi super.onStartup() CUỐI CÙNG để Spring tiếp tục khởi tạo
        super.onStartup(servletContext);
    }
}