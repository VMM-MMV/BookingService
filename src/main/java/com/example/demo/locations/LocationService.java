package com.example.demo.locations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
    }

    @Transactional
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    @Transactional
    public Location updateLocation(Long id, Location updatedLocation) {
        return locationRepository.findById(id)
                .map(existingLocation -> {
                    existingLocation.setName(updatedLocation.getName());
                    existingLocation.setAddress(updatedLocation.getAddress());
                    return locationRepository.save(existingLocation);
                })
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
    }

    @Transactional
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}

