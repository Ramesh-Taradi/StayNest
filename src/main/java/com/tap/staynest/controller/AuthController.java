package com.tap.staynest.controller;

import com.tap.staynest.dto.request.LoginRequestDTO;
import com.tap.staynest.dto.request.RegisterRequestDTO;
import com.tap.staynest.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Duration;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequestDTO requestDTO, Model model) {
        authService.register(requestDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute LoginRequestDTO requestDTO,
            HttpServletResponse response,
            Model model) {

        try {
            String token = authService.login(requestDTO);

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return "redirect:/";

        } catch (AuthenticationException e) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }
}
