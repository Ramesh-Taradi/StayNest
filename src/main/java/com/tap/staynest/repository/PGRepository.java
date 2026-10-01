package com.tap.staynest.repository;

import com.tap.staynest.model.PG;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PGRepository extends JpaRepository<PG, Long> {
    List<PG> findByLocationContainingIgnoreCase(String location);
}
