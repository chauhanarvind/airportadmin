package com.airport.admin.airport_admin.features.location;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    private LocationService(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations(){
        return locationRepository.findAll();
    }

    public Location createLocation(LocationDto locationDto){
        if(locationRepository.findByLocationName(locationDto.getLocationName()).isPresent()){
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
