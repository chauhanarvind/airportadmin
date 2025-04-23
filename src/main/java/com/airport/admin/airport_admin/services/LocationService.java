package com.airport.admin.airport_admin.services;

import com.airport.admin.airport_admin.dto.LocationDto;
import com.airport.admin.airport_admin.models.Location;
import com.airport.admin.airport_admin.repositories.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    private LocationService(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    public Location createLocation(LocationDto locationDto){
        if(locationRepository.findByLocatioName(locationDto.getLocationName()).isPresent()){
            throw  new RuntimeException("Location with this name already exits");
        }

        Location location = new Location();
        location.setLocationName(locationDto.getLocationName());
        location.setDescription(locationDto.getDescription());

        return locationRepository.save(location);
    }

    public Location updateLocation(LocationDto locationDto){
        Location location = locationRepository.findById(locationDto.getId())
                .orElseThrow(()-> new RuntimeException("Location does not exist"));

        location.setLocationName(locationDto.getLocationName());
        location.setDescription(locationDto.getDescription());

        return locationRepository.save(location);

    }

    public void deleteLocation(LocationDto locationDto){
        Location location = locationRepository.findById(locationDto.getId())
                .orElseThrow(()-> new RuntimeException("Location does not exist"));

        locationRepository.delete(location);
    }


}
