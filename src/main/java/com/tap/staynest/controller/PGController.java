package com.tap.staynest.controller;

import com.tap.staynest.dto.request.PGRequestDTO;
import com.tap.staynest.dto.response.PGResponseDTO;
import com.tap.staynest.service.PGService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pgs")
public class PGController {

    @Autowired
    private PGService pgService;

    @PostMapping
    public ResponseEntity<PGResponseDTO> addPG(@Valid @RequestBody PGRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pgService.addPG(requestDTO));
    }

    @GetMapping
    public ResponseEntity<List<PGResponseDTO>> getAllPGs() {
        return ResponseEntity.ok(pgService.getAllPGs());
    }

    @GetMapping("/search")
    public ResponseEntity<List<PGResponseDTO>> searchByLocation(@RequestParam String location) {
        return ResponseEntity.ok(pgService.searchByLocation(location));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<PGResponseDTO>> getPGsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(pgService.getPGsWithPagination(page, size));
    }

    @GetMapping("/page/sort")
    public ResponseEntity<Page<PGResponseDTO>> getPGsWithPaginationAndSorting(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        return ResponseEntity.ok(pgService.getPGsWithPaginationAndSorting(page, size, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PGResponseDTO> getPGById(@PathVariable Long id) {
        return ResponseEntity.ok(pgService.getPGById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PGResponseDTO> updatePG(@PathVariable Long id, @Valid @RequestBody PGRequestDTO requestDTO) {
        return ResponseEntity.ok(pgService.updatePG(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePG(@PathVariable Long id) {
        pgService.deletePG(id);
        return ResponseEntity.noContent().build();
    }
}
