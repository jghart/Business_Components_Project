package com.ecommerce.glowshop.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleBasedAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String ADMIN_DASHBOARD = "/admin/dashboard";
    private static final String CUSTOMER_DASHBOARD = "/dashboard";

    public RoleBasedAuthenticationSuccessHandler() {
        setDefaultTargetUrl(CUSTOMER_DASHBOARD);
        setAlwaysUseDefaultTargetUrl(false);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        if (hasRole(authentication, "ROLE_ADMIN")) {
            getRedirectStrategy().sendRedirect(request, response, ADMIN_DASHBOARD);
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private static boolean hasRole(Authentication authentication, String role) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (role.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
