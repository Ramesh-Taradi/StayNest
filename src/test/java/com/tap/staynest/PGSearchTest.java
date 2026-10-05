package com.tap.staynest;

import com.tap.staynest.dto.response.PGResponseDTO;
import com.tap.staynest.dto.response.RoomResponseDTO;
import com.tap.staynest.exception.PGNotFoundException;
import com.tap.staynest.model.PG;
import com.tap.staynest.model.Room;
import com.tap.staynest.repository.PGRepository;
import com.tap.staynest.repository.RoomRepository;
import com.tap.staynest.service.PGService;
import com.tap.staynest.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PGSearchTest {

    // -------------------------------------------------------
    // Mocks for PGService
    // -------------------------------------------------------
    @Mock
    private PGRepository pgRepository;

    @InjectMocks
    private PGService pgService;

    // -------------------------------------------------------
    // Mocks for RoomService
    // -------------------------------------------------------
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private PG pg;
    private Room room;

    // -------------------------------------------------------
    // Setup: Runs before every test
    // -------------------------------------------------------
    @BeforeEach
    void setUp() {
        // Sample PG in Pune
        pg = new PG();
        pg.setId(1L);
        pg.setName("Sunrise PG");
        pg.setLocation("Pune");
        pg.setRent(new BigDecimal("6000"));

        // Sample Room belonging to the PG
        room = new Room();
        room.setId(1L);
        room.setType("Single");
        room.setRent(new BigDecimal("6000"));
        room.setTotalRooms(5);
        room.setAvailableRooms(3);  // Rooms available → Book Now should show
        room.setPg(pg);
    }

    // -------------------------------------------------------
    // TEST 1: Search PG by location returns results
    // (equivalent to: homePage.searchPG("Pune"))
    // -------------------------------------------------------
    @Test
    void searchAndViewPGTest_SearchByLocation_ReturnsResults() {
        // Arrange
        when(pgRepository.findByLocationContainingIgnoreCase("Pune"))
                .thenReturn(List.of(pg));

        // Act
        List<PGResponseDTO> results = pgService.searchByLocation("Pune");

        // Assert
        assertNotNull(results);
        assertFalse(results.isEmpty(), "Search results should not be empty");
        assertEquals(1, results.size());
        assertEquals("Pune", results.get(0).getLocation());

        System.out.println("TEST 1 PASSED: Found " + results.size() + " PG(s) in Pune");
    }

    // -------------------------------------------------------
    // TEST 2: View PG Details - PG found by ID
    // (equivalent to: homePage.clickViewPG() → pgDetailsPage)
    // -------------------------------------------------------
    @Test
    void searchAndViewPGTest_GetPGById_ReturnsPGDetails() {
        // Arrange
        when(pgRepository.findById(1L)).thenReturn(Optional.of(pg));

        // Act
        PGResponseDTO pgDetails = pgService.getPGById(1L);

        // Assert
        assertNotNull(pgDetails);
        assertEquals("Sunrise PG", pgDetails.getName());
        assertEquals("Pune", pgDetails.getLocation());

        System.out.println("TEST 2 PASSED: PG details found - " + pgDetails.getName());
    }

    // -------------------------------------------------------
    // TEST 3: Book Now displayed - Room is available
    // (equivalent to: assertTrue(pgDetailsPage.isBookNowDisplayed()))
    // -------------------------------------------------------
    @Test
    void searchAndViewPGTest_BookNowDisplayed_WhenRoomsAvailable() {
        // Arrange
        when(pgRepository.findById(1L)).thenReturn(Optional.of(pg));
        when(roomRepository.findByPg(pg)).thenReturn(List.of(room));

        // Act: Get rooms for PG
        List<RoomResponseDTO> rooms = roomService.getRoomsByPG(1L);

        // Assert: At least one room has available rooms > 0 (Book Now should show)
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty(), "Rooms list should not be empty");

        boolean isBookNowDisplayed = rooms.stream()
                .anyMatch(r -> r.getAvailableRooms() > 0);

        assertTrue(isBookNowDisplayed, "Book Now button should be displayed");

        System.out.println("TEST 3 PASSED: Book Now is displayed - available rooms = "
                + rooms.get(0).getAvailableRooms());
    }

    // -------------------------------------------------------
    // TEST 4: Book Now NOT displayed - No rooms available
    // (opposite case: no rooms → Book Now hidden)
    // -------------------------------------------------------
    @Test
    void searchAndViewPGTest_BookNowHidden_WhenNoRoomsAvailable() {
        // Arrange: Set available rooms to 0
        room.setAvailableRooms(0);
        when(pgRepository.findById(1L)).thenReturn(Optional.of(pg));
        when(roomRepository.findByPg(pg)).thenReturn(List.of(room));

        // Act
        List<RoomResponseDTO> rooms = roomService.getRoomsByPG(1L);

        // Assert: No rooms available → Book Now should NOT be shown
        boolean isBookNowDisplayed = rooms.stream()
                .anyMatch(r -> r.getAvailableRooms() > 0);

        assertFalse(isBookNowDisplayed, "Book Now button should NOT be displayed");

        System.out.println("TEST 4 PASSED: Book Now is hidden - no available rooms");
    }

    // -------------------------------------------------------
    // TEST 5: Search PG - Invalid location returns empty list
    // -------------------------------------------------------
    @Test
    void searchAndViewPGTest_InvalidLocation_ReturnsEmptyList() {
        // Arrange
        when(pgRepository.findByLocationContainingIgnoreCase("Delhi"))
                .thenReturn(List.of());

        // Act
        List<PGResponseDTO> results = pgService.searchByLocation("Delhi");

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty(), "No PGs should be found in Delhi");

        System.out.println("TEST 5 PASSED: No PGs found in Delhi - empty list returned");
    }

    // -------------------------------------------------------
    // TEST 6: View PG Details - PG Not Found throws exception
    // -------------------------------------------------------
    @Test
    void searchAndViewPGTest_PGNotFound_ThrowsException() {
        // Arrange
        when(pgRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PGNotFoundException.class, () -> {
            pgService.getPGById(99L);
        });

        System.out.println("TEST 6 PASSED: PGNotFoundException thrown for invalid PG ID");
    }
}
