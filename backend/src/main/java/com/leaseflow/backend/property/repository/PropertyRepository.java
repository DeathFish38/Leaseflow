package com.leaseflow.backend.property.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leaseflow.backend.property.entity.Property;


public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByOwnerId(Long ownerId);

    //aggregate for dashboard
    long countByOwnerId(Long userId); 
}
