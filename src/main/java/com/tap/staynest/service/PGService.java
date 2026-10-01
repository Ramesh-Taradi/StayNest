package com.tap.staynest.service;

import com.tap.staynest.dto.request.PGRequestDTO;
import com.tap.staynest.dto.response.PGResponseDTO;
import com.tap.staynest.exception.PGNotFoundException;
import com.tap.staynest.model.PG;
import com.tap.staynest.repository.PGRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PGService {

    @Autowired
    private PGRepository pgRepository;

    public PGResponseDTO addPG(PGRequestDTO requestDTO) {
        PG pg = createPG(new PG(), requestDTO);
        PG savedPG = pgRepository.save(pg);
        return convertToResponseDTO(savedPG);
    }

    public List<PGResponseDTO> getAllPGs() {
        return pgRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public PGResponseDTO getPGById(Long id) {
        PG pg = pgRepository.findById(id)
                .orElseThrow(() -> new PGNotFoundException("PG not found"));
        return convertToResponseDTO(pg);
    }

    public PGResponseDTO updatePG(Long id, PGRequestDTO requestDTO) {
        PG oldPg = pgRepository.findById(id)
                .orElseThrow(() -> new PGNotFoundException("PG not found"));
        PG updatedPg = createPG(oldPg, requestDTO);
        PG updatedPG = pgRepository.save(updatedPg);
        return convertToResponseDTO(updatedPG);
    }

    public List<PGResponseDTO> searchByLocation(String location) {
        return pgRepository.findByLocationContainingIgnoreCase(location)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public void deletePG(Long id) {
        PG pg = pgRepository.findById(id)
                .orElseThrow(() -> new PGNotFoundException("PG not found"));
        pgRepository.delete(pg);
    }

    public Page<PGResponseDTO> getPGsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return pgRepository.findAll(pageable).map(this::convertToResponseDTO);
    }

    public Page<PGResponseDTO> getPGsWithPaginationAndSorting(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return pgRepository.findAll(pageable).map(this::convertToResponseDTO);
    }

    private PG createPG(PG pg, PGRequestDTO requestDTO) {
        pg.setName(requestDTO.getName());
        pg.setLocation(requestDTO.getLocation());
        pg.setRent(requestDTO.getRent());
        pg.setImageUrl(requestDTO.getImageUrl());
        return pg;
    }

    private PGResponseDTO convertToResponseDTO(PG pg) {
        PGResponseDTO responseDTO = new PGResponseDTO();
        responseDTO.setId(pg.getId());
        responseDTO.setName(pg.getName());
        responseDTO.setLocation(pg.getLocation());
        responseDTO.setRent(pg.getRent());
        responseDTO.setImageUrl(pg.getImageUrl());
        return responseDTO;
    }
}
