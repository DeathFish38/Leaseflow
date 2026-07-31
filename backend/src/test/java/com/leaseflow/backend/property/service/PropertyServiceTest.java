package com.leaseflow.backend.property.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.leaseflow.backend.property.dto.CreatePropertyRequest;
import com.leaseflow.backend.property.dto.PropertyResponse;
import com.leaseflow.backend.property.dto.UpdatePropertyRequest;
import com.leaseflow.backend.property.entity.Property;
import com.leaseflow.backend.property.mapper.PropertyMapper;
import com.leaseflow.backend.property.repository.PropertyRepository;
import com.leaseflow.backend.users.entity.User;
import com.leaseflow.backend.users.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PropertyMapper propertyMapper;

    @InjectMocks
    private PropertyService propertyService;

    private User user;
    private Property property;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        property = new Property();
        property.setId(1L);
        property.setOwner(user);
        property.setAddressLine1("123 Main Street");
        property.setSuburb("Melbourne");
        property.setState("VIC");
        property.setPostcode("3000");
    }

    @Test
    void createProperty_shouldCreateProperty() {

        CreatePropertyRequest request = mock(CreatePropertyRequest.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(propertyMapper.toEntity(request))
                .thenReturn(property);

        when(propertyRepository.save(property))
                .thenReturn(property);

        PropertyResponse response = mock(PropertyResponse.class);

        when(propertyMapper.toResponse(property))
                .thenReturn(response);

        PropertyResponse result = propertyService.createProperty(1L, request);

        assertSame(response, result);

        assertEquals(user, property.getOwner());

        verify(userRepository).findById(1L);
        verify(propertyMapper).toEntity(request);
        verify(propertyRepository).save(property);
        verify(propertyMapper).toResponse(property);
    }

    @Test
    void createProperty_shouldThrowWhenUserDoesNotExist() {

        CreatePropertyRequest request = mock(CreatePropertyRequest.class);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());
        verify(propertyRepository, never()).save(any());
    }

    @Test
    void getAllProperties_shouldReturnProperties() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(propertyRepository.findByOwnerId(1L))
                .thenReturn(List.of(property));

        PropertyResponse response = mock(PropertyResponse.class);

        when(propertyMapper.toResponse(property))
                .thenReturn(response);

        List<PropertyResponse> result = propertyService.getAllProperties(1L);

        assertEquals(1, result.size());
        assertSame(response, result.get(0));

        verify(propertyRepository).findByOwnerId(1L);
    }

    @Test
    void getAllProperties_shouldThrowWhenUserDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        verify(propertyRepository, never()).findByOwnerId(anyLong());
    }

    @Test
    void getPropertyById_shouldReturnProperty() {

        when(propertyRepository.findById(1L))
                .thenReturn(Optional.of(property));

        PropertyResponse response = mock(PropertyResponse.class);

        when(propertyMapper.toResponse(property))
                .thenReturn(response);

        PropertyResponse result = propertyService.getPropertyById(1L);

        assertSame(response, result);

        verify(propertyRepository).findById(1L);
    }

    @Test
    void getPropertyById_shouldThrowWhenNotFound() {

        when(propertyRepository.findById(1L))
                .thenReturn(Optional.empty());
    }

    @Test
    void updateProperty_shouldUpdateProperty() {

        UpdatePropertyRequest request = mock(UpdatePropertyRequest.class);

        when(propertyRepository.findById(1L))
                .thenReturn(Optional.of(property));

        when(propertyRepository.save(property))
                .thenReturn(property);

        PropertyResponse response = mock(PropertyResponse.class);

        when(propertyMapper.toResponse(property))
                .thenReturn(response);

        PropertyResponse result = propertyService.updateProperty(1L, request);

        assertSame(response, result);

        verify(propertyMapper).updateEntity(property, request);
        verify(propertyRepository).save(property);
    }

    @Test
    void deleteProperty_shouldDeleteProperty() {

        when(propertyRepository.findById(1L))
                .thenReturn(Optional.of(property));

        propertyService.deleteProperty(1L);

        verify(propertyRepository).delete(property);
    }
}