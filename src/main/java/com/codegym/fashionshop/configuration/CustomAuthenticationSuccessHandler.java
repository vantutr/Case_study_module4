package com.codegym.fashionshop.configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authority -> {
            try {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    redirectStrategy.sendRedirect(request, response, "/admin/dashboard");
                } else if (authority.getAuthority().equals("ROLE_USER")) {
                    redirectStrategy.sendRedirect(request, response, "/home");
                } else {
                    throw new IllegalStateException("Không tìm thấy quyền hợp lệ.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}