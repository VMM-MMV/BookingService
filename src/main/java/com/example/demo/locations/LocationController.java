package com.example.demo.locations;

import com.example.demo.locations.dto.LocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/all")
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<LocationDTO> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        LocationDTO location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }

    @PostMapping("/create")
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDto) {
        LocationDTO createdLocation = locationService.createLocation(locationDto);
        return ResponseEntity.ok(createdLocation);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Long id, @RequestBody LocationDTO updatedLocationDTO) {
        LocationDTO updatedLocation = locationService.updateLocation(id, updatedLocationDTO);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
