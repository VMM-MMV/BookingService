package com.example.demo.locations;

import com.example.demo.locations.dto.LocationDTO;
import com.example.demo.locations.dto.LocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<LocationDTO> getAllLocations() {
        return locationRepository.getAll();
    }

    @Transactional(readOnly = true)
    public LocationDTO getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        return LocationMapper.toDto(location);
    }

    @Transactional
    public LocationDTO createLocation(LocationDTO locationDto) {
        Location location = LocationMapper.toEntity(locationDto);
        location = locationRepository.save(location);
        return LocationMapper.toDto(location);
    }

    @Transactional
    public LocationDTO updateLocation(Long id, LocationDTO updatedLocationDTO) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        LocationMapper.updateEntityFromDto(updatedLocationDTO, existingLocation);
        existingLocation = locationRepository.save(existingLocation);

        return LocationMapper.toDto(existingLocation);
    }

    @Transactional
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}
