package com.tap.staynest.repository;

import com.tap.staynest.model.PG;
import com.tap.staynest.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByPg(PG pg);
}
