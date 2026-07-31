package com.leaseflow.backend.property.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.leaseflow.backend.property.dto.CreatePropertyRequest;
import com.leaseflow.backend.property.dto.PropertyResponse;
import com.leaseflow.backend.property.dto.UpdatePropertyRequest;
import com.leaseflow.backend.property.service.PropertyService;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(PropertyController.class)
class PropertyControllerTest {


@Autowired
private MockMvc mockMvc;

@Autowired
private ObjectMapper objectMapper;

@MockitoBean
private PropertyService propertyService;

@Test
void createProperty_shouldReturnCreated() throws Exception {

    Long userId = 1L;

    CreatePropertyRequest request = new CreatePropertyRequest(
            "Home",
            "123 Main Street",
            "Melbourne",
            "VIC",
            "3000",
            null,
            null
    );

    PropertyResponse response = new PropertyResponse(
            1L,
            "Home",
            "123 Main Street",
            "Melbourne",
            "VIC",
            "3000",
            null,
            null
    );

    when(propertyService.createProperty(
            eq(userId),
            any(CreatePropertyRequest.class)))
            .thenReturn(response);

    mockMvc.perform(post("/api/users/{userId}/properties", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nickname").value("Home"))
            .andExpect(jsonPath("$.suburb").value("Melbourne"));

    verify(propertyService).createProperty(
            eq(userId),
            any(CreatePropertyRequest.class));
}

@Test
void getAllProperties_shouldReturnOk() throws Exception {

    Long userId = 1L;

    PropertyResponse response = new PropertyResponse(
            1L,
            "Home",
            "123 Main Street",
            "Melbourne",
            "VIC",
            "3000",
            null,
            null
    );

    when(propertyService.getAllProperties(userId))
            .thenReturn(List.of(response));

    mockMvc.perform(get("/api/users/{userId}/properties", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].nickname").value("Home"));

    verify(propertyService).getAllProperties(userId);
}

@Test
void getPropertyById_shouldReturnOk() throws Exception {

    Long propertyId = 1L;

    PropertyResponse response = new PropertyResponse(
            propertyId,
            "Home",
            "123 Main Street",
            "Melbourne",
            "VIC",
            "3000",
            null,
            null
    );

    when(propertyService.getPropertyById(propertyId))
            .thenReturn(response);

    mockMvc.perform(get("/api/properties/{propertyId}", propertyId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nickname").value("Home"));

    verify(propertyService).getPropertyById(propertyId);
}

@Test
void updateProperty_shouldReturnOk() throws Exception {

    Long propertyId = 1L;

    UpdatePropertyRequest request = new UpdatePropertyRequest(
            "Updated Home",
            null,
            null,
            null,
            null,
            null,
            null
    );

    PropertyResponse response = new PropertyResponse(
            propertyId,
            "Updated Home",
            "123 Main Street",
            "Melbourne",
            "VIC",
            "3000",
            null,
            null
    );

    when(propertyService.updateProperty(
            eq(propertyId),
            any(UpdatePropertyRequest.class)))
            .thenReturn(response);

    mockMvc.perform(patch("/api/properties/{propertyId}", propertyId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nickname").value("Updated Home"));

    verify(propertyService).updateProperty(
            eq(propertyId),
            any(UpdatePropertyRequest.class));
}

@Test
void deleteProperty_shouldReturnNoContent() throws Exception {

    Long propertyId = 1L;

    mockMvc.perform(delete("/api/properties/{propertyId}", propertyId))
            .andExpect(status().isNoContent());

    verify(propertyService).deleteProperty(propertyId);
}
}
