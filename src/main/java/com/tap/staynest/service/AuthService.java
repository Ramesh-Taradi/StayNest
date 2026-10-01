package com.tap.staynest.service;

import com.tap.staynest.dto.request.LoginRequestDTO;
import com.tap.staynest.dto.request.RegisterRequestDTO;
import com.tap.staynest.model.User;
import com.tap.staynest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public void register(RegisterRequestDTO requestDTO) {
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
    }

    public String login(LoginRequestDTO requestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )
        );

        return jwtService.generateToken(requestDTO.getEmail());
    }
}
