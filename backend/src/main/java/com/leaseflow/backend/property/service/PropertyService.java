package com.leaseflow.backend.property.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.leaseflow.backend.common.exception.property.PropertyNotFoundException;
import com.leaseflow.backend.common.exception.user.UserNotFoundException;
import com.leaseflow.backend.property.dto.CreatePropertyRequest;
import com.leaseflow.backend.property.dto.PropertyResponse;
import com.leaseflow.backend.property.dto.UpdatePropertyRequest;
import com.leaseflow.backend.property.entity.Property;
import com.leaseflow.backend.property.mapper.PropertyMapper;
import com.leaseflow.backend.property.repository.PropertyRepository;
import com.leaseflow.backend.users.entity.User;
import com.leaseflow.backend.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyService {

    // validate user
    // create new property with it associated user id
    // get all property
    // update property
    // delete property

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final PropertyMapper propertyMapper;

    public PropertyResponse createProperty(Long userId, CreatePropertyRequest request) {
        User owner = getUserById(userId);

        Property property = propertyMapper.toEntity(request);
        property.setOwner(owner);

        Property savedProperty = propertyRepository.save(property);

        return propertyMapper.toResponse(savedProperty);
    }

    // get all
    public List<PropertyResponse> getAllProperties(Long userId) {
        getUserById(userId);
        return propertyRepository.findByOwnerId(userId)
                .stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    // get by a property by id
    public PropertyResponse getPropertyById(Long propertyId) {
        return propertyMapper.toResponse(getProperty(propertyId));
    }

    // update service
    public PropertyResponse updateProperty(Long propertyId, UpdatePropertyRequest request) {
        Property property = getProperty(propertyId);
        propertyMapper.updateEntity(property, request);
        Property updatedProperty = propertyRepository.save(property);
        return propertyMapper.toResponse(updatedProperty);

    }

    // delete
    public void deleteProperty(Long propertyId) {
        Property property = getProperty(propertyId);
        propertyRepository.delete(property);
    }

    // helper methods
    // return user object
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    // return property object
    private Property getProperty(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
    }
}
