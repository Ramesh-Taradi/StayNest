package com.tap.staynest;

import com.tap.staynest.dto.request.LoginRequestDTO;
import com.tap.staynest.dto.response.PGResponseDTO;
import com.tap.staynest.exception.PGNotFoundException;
import com.tap.staynest.model.PG;
import com.tap.staynest.repository.PGRepository;
import com.tap.staynest.repository.UserRepository;
import com.tap.staynest.service.AuthService;
import com.tap.staynest.service.JwtService;
import com.tap.staynest.service.PGService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HomePageTest {

    // -------------------------------------------------------
    // Mocks for AuthService
    // -------------------------------------------------------
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    // -------------------------------------------------------
    // Mocks for PGService
    // -------------------------------------------------------
    @Mock
    private PGRepository pgRepository;

    @InjectMocks
    private PGService pgService;

    private PG pg1;
    private PG pg2;

    // -------------------------------------------------------
    // Setup: Runs before every test
    // -------------------------------------------------------
    @BeforeEach
    void setUp() {
        // Create sample PG data
        pg1 = new PG();
        pg1.setId(1L);
        pg1.setName("Sunrise PG");
        pg1.setLocation("Pune");
        pg1.setRent(new BigDecimal("6000"));

        pg2 = new PG();
        pg2.setId(2L);
        pg2.setName("Comfort Stay");
        pg2.setLocation("Pune - Kothrud");
        pg2.setRent(new BigDecimal("7500"));
    }

    // -------------------------------------------------------
    // TEST 1: Login with valid credentials returns JWT token
    // (equivalent to: loginPage.login("test@gmail.com", "12345"))
    // -------------------------------------------------------
    @Test
    void testLogin_WithValidCredentials_ReturnsToken() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("12345");

        when(jwtService.generateToken("test@gmail.com")).thenReturn("mock-jwt-token");

        // Act
        String token = authService.login(loginRequest);

        // Assert
        assertNotNull(token);
        assertEquals("mock-jwt-token", token);

        // Verify authentication was called
        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );

        System.out.println("TEST 1 PASSED: Login successful, Token = " + token);
    }

    // -------------------------------------------------------
    // TEST 2: Login with wrong credentials throws exception
    // -------------------------------------------------------
    @Test
    void testLogin_WithInvalidCredentials_ThrowsException() {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("wrong@gmail.com");
        loginRequest.setPassword("wrongpass");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            authService.login(loginRequest);
        });

        System.out.println("TEST 2 PASSED: Bad credentials exception thrown correctly");
    }

    // -------------------------------------------------------
    // TEST 3: Search PG by location - Results found
    // (equivalent to: homePage.searchPG("Pune"))
    // -------------------------------------------------------
    @Test
    void testSearchPG_ByLocation_ReturnsResults() {
        // Arrange
        when(pgRepository.findByLocationContainingIgnoreCase("Pune"))
                .thenReturn(List.of(pg1, pg2));

        // Act
        List<PGResponseDTO> results = pgService.searchByLocation("Pune");

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("Sunrise PG", results.get(0).getName());
        assertEquals("Pune", results.get(0).getLocation());

        System.out.println("TEST 3 PASSED: Search found " + results.size() + " PGs in Pune");
    }

    // -------------------------------------------------------
    // TEST 4: Search PG by location - No Results
    // -------------------------------------------------------
    @Test
    void testSearchPG_NoResults_ReturnsEmptyList() {
        // Arrange
        when(pgRepository.findByLocationContainingIgnoreCase("Mumbai"))
                .thenReturn(List.of());

        // Act
        List<PGResponseDTO> results = pgService.searchByLocation("Mumbai");

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());

        System.out.println("TEST 4 PASSED: No PGs found in Mumbai, empty list returned");
    }

    // -------------------------------------------------------
    // TEST 5: Get all PGs (like loading Home Page PG list)
    // -------------------------------------------------------
    @Test
    void testGetAllPGs_ReturnsAllPGs() {
        // Arrange
        when(pgRepository.findAll()).thenReturn(List.of(pg1, pg2));

        // Act
        List<PGResponseDTO> allPGs = pgService.getAllPGs();

        // Assert
        assertNotNull(allPGs);
        assertEquals(2, allPGs.size());

        System.out.println("TEST 5 PASSED: Total PGs loaded = " + allPGs.size());
    }

    // -------------------------------------------------------
    // TEST 6: Get PG by ID - PG Not Found
    // -------------------------------------------------------
    @Test
    void testGetPGById_NotFound_ThrowsException() {
        // Arrange
        when(pgRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(PGNotFoundException.class, () -> {
            pgService.getPGById(99L);
        });

        System.out.println("TEST 6 PASSED: PGNotFoundException thrown for invalid ID");
    }
}
