package com.example.demo.locations;

import com.example.demo.locations.dto.LocationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT new com.example.demo.locations.dto.LocationDTO(l.id, l.name, l.address) FROM Location l")
    List<LocationDTO> getAll();
}
