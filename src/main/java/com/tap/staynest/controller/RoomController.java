package com.tap.staynest.controller;

import com.tap.staynest.dto.request.RoomRequestDTO;
import com.tap.staynest.dto.response.RoomResponseDTO;
import com.tap.staynest.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/pgs/{pgId}/rooms")
    public ResponseEntity<RoomResponseDTO> addRoom(
            @PathVariable Long pgId,
            @Valid @RequestBody RoomRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.addRoom(pgId, requestDTO));
    }

    @GetMapping("/pgs/{pgId}/rooms")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByPG(@PathVariable Long pgId) {
        return ResponseEntity.ok(roomService.getRoomsByPG(pgId));
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<RoomResponseDTO> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomRequestDTO requestDTO) {
        return ResponseEntity.ok(roomService.updateRoom(id, requestDTO));
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
