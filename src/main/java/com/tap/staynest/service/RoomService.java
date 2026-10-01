package com.tap.staynest.service;

import com.tap.staynest.dto.request.RoomRequestDTO;
import com.tap.staynest.dto.response.RoomResponseDTO;
import com.tap.staynest.exception.PGNotFoundException;
import com.tap.staynest.exception.RoomNotFoundException;
import com.tap.staynest.model.PG;
import com.tap.staynest.model.Room;
import com.tap.staynest.repository.PGRepository;
import com.tap.staynest.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PGRepository pgRepository;

    public RoomResponseDTO addRoom(Long pgId, RoomRequestDTO requestDTO) {
        PG pg = pgRepository.findById(pgId)
                .orElseThrow(() -> new PGNotFoundException("PG not found"));

        Room room = createRoom(new Room(), requestDTO);
        room.setPg(pg);
        Room savedRoom = roomRepository.save(room);
        return convertToResponseDTO(savedRoom);
    }

    public List<RoomResponseDTO> getRoomsByPG(Long pgId) {
        PG pg = pgRepository.findById(pgId)
                .orElseThrow(() -> new PGNotFoundException("PG not found"));

        return roomRepository.findByPg(pg)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public RoomResponseDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));
        return convertToResponseDTO(room);
    }

    public RoomResponseDTO updateRoom(Long id, RoomRequestDTO requestDTO) {
        Room oldRoom = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));

        Room updatedRoom = createRoom(oldRoom, requestDTO);
        Room savedRoom = roomRepository.save(updatedRoom);
        return convertToResponseDTO(savedRoom);
    }

    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException("Room not found"));
        roomRepository.delete(room);
    }

    private Room createRoom(Room room, RoomRequestDTO requestDTO) {
        room.setType(requestDTO.getType());
        room.setRent(requestDTO.getRent());
        room.setTotalRooms(requestDTO.getTotalRooms());
        room.setAvailableRooms(requestDTO.getAvailableRooms());
        room.setImageUrl(requestDTO.getImageUrl());
        return room;
    }

    private RoomResponseDTO convertToResponseDTO(Room room) {
        RoomResponseDTO responseDTO = new RoomResponseDTO();
        responseDTO.setId(room.getId());
        responseDTO.setType(room.getType());
        responseDTO.setRent(room.getRent());
        responseDTO.setTotalRooms(room.getTotalRooms());
        responseDTO.setAvailableRooms(room.getAvailableRooms());
        responseDTO.setImageUrl(room.getImageUrl());
        responseDTO.setPgId(room.getPg().getId());
        return responseDTO;
    }
}
