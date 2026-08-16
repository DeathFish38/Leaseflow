package com.leaseflow.backend.property.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.leaseflow.backend.auth.service.AuthService;
import com.leaseflow.backend.common.exception.property.PropertyNotFoundException;
import com.leaseflow.backend.property.dto.CreatePropertyRequest;
import com.leaseflow.backend.property.dto.PropertyResponse;
import com.leaseflow.backend.property.dto.UpdatePropertyRequest;
import com.leaseflow.backend.property.entity.Property;
import com.leaseflow.backend.property.mapper.PropertyMapper;
import com.leaseflow.backend.property.repository.PropertyRepository;
import com.leaseflow.backend.users.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyService {

    // validate user
    // create new property with it associated user id
    // get all property
    // update property
    // delete property

    private final AuthService authService;
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    public PropertyResponse createProperty(CreatePropertyRequest request) {
        User owner = authService.getAuthenticatedUser();
        Property property = propertyMapper.toEntity(request);
        property.setOwner(owner);
        Property savedProperty = propertyRepository.save(property);
        return propertyMapper.toResponse(savedProperty);
    }

    // get all
    public List<PropertyResponse> getAllProperties() {
        User owner = authService.getAuthenticatedUser();
        return propertyRepository.findByOwnerId(owner.getId())
                .stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    // get by a property by id
    public PropertyResponse getPropertyById(Long propertyId) {
        return propertyMapper.toResponse(getPropertyForAuthenticatedUser(propertyId));
    }

    // update service
    public PropertyResponse updateProperty(Long propertyId, UpdatePropertyRequest request) {
        Property property = getPropertyForAuthenticatedUser(propertyId);
        propertyMapper.updateEntity(property, request);
        Property updatedProperty = propertyRepository.save(property);
        return propertyMapper.toResponse(updatedProperty);
    }

    // delete
    public void deleteProperty(Long propertyId) {
        Property property = getPropertyForAuthenticatedUser(propertyId);
        propertyRepository.delete(property);
    }

    // helper methods
    // return property object
    private Property getPropertyForAuthenticatedUser(Long propertyId) {
        User user = authService.getAuthenticatedUser();
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));

        if (!property.getOwner().getId().equals(user.getId())) {
            throw new PropertyNotFoundException(propertyId);
        }
        return property;
    }
}
