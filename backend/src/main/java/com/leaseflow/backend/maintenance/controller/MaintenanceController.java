package com.leaseflow.backend.maintenance.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leaseflow.backend.maintenance.dto.CreateMaintenanceRequest;
import com.leaseflow.backend.maintenance.dto.MaintenanceResponse;
import com.leaseflow.backend.maintenance.dto.UpdateMaintenanceRequest;
import com.leaseflow.backend.maintenance.service.MaintenanceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MaintenanceController {

        private final MaintenanceService maintenanceService;

        // create new maintenance request
        @PostMapping("/properties/{propertyId}/maintenance")
        public ResponseEntity<MaintenanceResponse> createMaintenance(
                        @PathVariable Long propertyId,
                        @Valid @RequestBody CreateMaintenanceRequest request) {

                MaintenanceResponse response = maintenanceService.createMaintenance(propertyId, request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        // get all maintenance requests for a property
        @GetMapping("/properties/{propertyId}/maintenance")
        public ResponseEntity<List<MaintenanceResponse>> getMaintenanceByProperty(
                        @PathVariable Long propertyId) {

                List<MaintenanceResponse> response = maintenanceService.getMaintenanceByProperty(propertyId);

                return ResponseEntity.ok(response);
        }

        // get maintenance request by id
        @GetMapping("/maintenance/{maintenanceId}")
        public ResponseEntity<MaintenanceResponse> getMaintenanceById(
                        @PathVariable Long maintenanceId) {

                MaintenanceResponse response = maintenanceService.getMaintenanceById(maintenanceId);

                return ResponseEntity.ok(response);
        }

        // update maintenance request
        @PatchMapping("/maintenance/{maintenanceId}")
        public ResponseEntity<MaintenanceResponse> updateMaintenance(
                        @PathVariable Long maintenanceId,
                        @Valid @RequestBody UpdateMaintenanceRequest request) {

                MaintenanceResponse response = maintenanceService.updateMaintenance(
                                maintenanceId,
                                request);

                return ResponseEntity.ok(response);
        }

        // mark maintenance as in progress
        @PatchMapping("/maintenance/{maintenanceId}/start")
        public ResponseEntity<MaintenanceResponse> startMaintenance(
                        @PathVariable Long maintenanceId) {

                MaintenanceResponse response = maintenanceService.startMaintenance(maintenanceId);

                return ResponseEntity.ok(response);
        }

        // mark maintenance as completed
        @PatchMapping("/maintenance/{maintenanceId}/complete")
        public ResponseEntity<MaintenanceResponse> completeMaintenance(
                        @PathVariable Long maintenanceId) {

                MaintenanceResponse response = maintenanceService.completeMaintenance(maintenanceId);

                return ResponseEntity.ok(response);
        }

        // delete maintenance request
        @DeleteMapping("/maintenance/{maintenanceId}")
        public ResponseEntity<Void> deleteMaintenance(
                        @PathVariable Long maintenanceId) {

                maintenanceService.deleteMaintenance(maintenanceId);

                return ResponseEntity.noContent().build();
        }
}
