package com.tap.staynest.dto.request;

import jakarta.validation.constraints.NotBlank;

public class BookingRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    private Long roomId;

    public BookingRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
