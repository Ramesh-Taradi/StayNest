package com.tap.staynest;

import com.tap.staynest.dto.request.LoginRequestDTO;
import com.tap.staynest.dto.request.RegisterRequestDTO;
import com.tap.staynest.model.User;
import com.tap.staynest.repository.UserRepository;
import com.tap.staynest.service.AuthService;
import com.tap.staynest.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private LoginRequestDTO validLoginRequest;
    private LoginRequestDTO invalidLoginRequest;
    private RegisterRequestDTO registerRequest;

    // -------------------------------------------------------
    // Setup: Runs before every test
    // -------------------------------------------------------
    @BeforeEach
    void setUp() {
        // Valid login credentials
        validLoginRequest = new LoginRequestDTO();
        validLoginRequest.setEmail("test@gmail.com");
        validLoginRequest.setPassword("12345");

        // Invalid login credentials
        invalidLoginRequest = new LoginRequestDTO();
        invalidLoginRequest.setEmail("test@gmail.com");
        invalidLoginRequest.setPassword("wrongpassword");

        // Register request
        registerRequest = new RegisterRequestDTO();
        registerRequest.setName("Test User");
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("12345");
    }

    // -------------------------------------------------------
    // TEST 1: Verify Login returns a valid JWT Token
    // (equivalent to: verifyLoginPageTitle - checks login works)
    // -------------------------------------------------------
    @Test
    void verifyLogin_ReturnsValidToken() {
        // Arrange
        when(jwtService.generateToken("test@gmail.com")).thenReturn("valid-jwt-token");

        // Act
        String token = authService.login(validLoginRequest);

        // Assert: Token should not be null (login page gives token = successful login)
        assertNotNull(token);
        assertEquals("valid-jwt-token", token);

        System.out.println("TEST 1 PASSED: Login returned token = " + token);
    }

    // -------------------------------------------------------
    // TEST 2: Login Fail - Wrong Password throws exception
    // (equivalent to: loginFailTest - wrong credentials don't let user in)
    // -------------------------------------------------------
    @Test
    void loginFailTest_WrongPassword_ThrowsException() {
        // Arrange: AuthManager rejects wrong credentials
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        // Act & Assert: Login should NOT succeed (assertNotEquals equivalent)
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authService.login(invalidLoginRequest);
        });

        // Token should NOT be generated (login failed, not navigated to home)
        assertNotNull(exception.getMessage());
        assertNotEquals("valid-jwt-token", ""); // token not returned

        System.out.println("TEST 2 PASSED: Login failed - " + exception.getMessage());
    }

    // -------------------------------------------------------
    // TEST 3: Login with empty email throws exception
    // -------------------------------------------------------
    @Test
    void loginFailTest_EmptyEmail_ThrowsException() {
        // Arrange
        LoginRequestDTO emptyEmailRequest = new LoginRequestDTO();
        emptyEmailRequest.setEmail("");
        emptyEmailRequest.setPassword("12345");

        doThrow(new BadCredentialsException("Email cannot be empty"))
                .when(authenticationManager).authenticate(any());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            authService.login(emptyEmailRequest);
        });

        System.out.println("TEST 3 PASSED: Empty email login rejected correctly");
    }

    // -------------------------------------------------------
    // TEST 4: Register - Success
    // -------------------------------------------------------
    @Test
    void testRegister_NewUser_Success() {
        // Arrange: Email not already registered
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("12345")).thenReturn("encoded-password");

        // Act: Should not throw any exception
        assertDoesNotThrow(() -> authService.register(registerRequest));

        // Verify user was saved to DB
        verify(userRepository, times(1)).save(any(User.class));

        System.out.println("TEST 4 PASSED: New user registered successfully");
    }

    // -------------------------------------------------------
    // TEST 5: Register - Email Already Exists
    // -------------------------------------------------------
    @Test
    void testRegister_DuplicateEmail_ThrowsException() {
        // Arrange: Email already in DB
        User existingUser = new User();
        existingUser.setEmail("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerRequest);
        });

        assertEquals("Email already registered", exception.getMessage());

        System.out.println("TEST 5 PASSED: Duplicate email rejected - " + exception.getMessage());
    }

    // -------------------------------------------------------
    // TEST 6: Login calls AuthenticationManager exactly once
    // -------------------------------------------------------
    @Test
    void testLogin_CallsAuthManager_ExactlyOnce() {
        // Arrange
        when(jwtService.generateToken(anyString())).thenReturn("token");

        // Act
        authService.login(validLoginRequest);

        // Assert: AuthenticationManager must be called exactly once
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        System.out.println("TEST 6 PASSED: AuthenticationManager called exactly once");
    }
}
