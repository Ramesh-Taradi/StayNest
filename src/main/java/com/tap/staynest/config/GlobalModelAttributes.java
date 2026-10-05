package com.tap.staynest.config;

import com.tap.staynest.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private JwtService jwtService;

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return true;
        }
        String token = getJwtFromCookie(request);
        if (token != null) {
            try {
                String email = jwtService.extractEmail(token);
                return email != null && !jwtService.isTokenExpired(token);
            } catch (Exception ignored) {}
        }
        return false;
    }

    @ModelAttribute("currentUser")
    public String currentUser(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        String token = getJwtFromCookie(request);
        if (token != null) {
            try {
                String email = jwtService.extractEmail(token);
                if (email != null && !jwtService.isTokenExpired(token)) {
                    return email;
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        if (request == null) return null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
