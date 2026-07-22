package com.leaseflow.backend.property.mapper;

import org.springframework.stereotype.Component;

import com.leaseflow.backend.property.dto.CreatePropertyRequest;
import com.leaseflow.backend.property.dto.PropertyResponse;
import com.leaseflow.backend.property.dto.UpdatePropertyRequest;
import com.leaseflow.backend.property.entity.Property;

@Component
public class PropertyMapper {

    public Property toEntity(CreatePropertyRequest request) {

        Property property = new Property();

        property.setNickname(request.nickname());
        property.setAddressLine1(request.addressLine1());
        property.setSuburb(request.suburb());
        property.setState(request.state());
        property.setPostcode(request.postcode());
        property.setMoveInDate(request.moveInDate());
        property.setMoveOutDate(request.moveOutDate());

        return property;
    }

    public PropertyResponse toResponse(Property property) {

        return new PropertyResponse(
                property.getId(),
                property.getNickname(),
                property.getAddressLine1(),
                property.getSuburb(),
                property.getState(),
                property.getPostcode(),
                property.getMoveInDate(),
                property.getMoveOutDate());
    }

    // upadate entity schema
    public void updateEntity(Property property, UpdatePropertyRequest request) {
        if (request.nickname() != null) {
            property.setNickname(request.nickname());
        }

        if (request.addressLine1() != null) {
            property.setAddressLine1(request.addressLine1());
        }

        if (request.suburb() != null) {
            property.setSuburb(request.suburb());
        }

        if (request.state() != null) {
            property.setState(request.state());
        }

        if (request.postcode() != null) {
            property.setPostcode(request.postcode());
        }

        if (request.moveInDate() != null) {
            property.setMoveInDate(request.moveInDate());
        }

        if (request.moveOutDate() != null) {
            property.setMoveOutDate(request.moveOutDate());
        }
    }
}