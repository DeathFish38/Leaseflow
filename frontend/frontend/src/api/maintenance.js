import api from "./client";

export async function getMaintenance(propertyId) {
  const response = await api.get(`/properties/${propertyId}/maintenance`);
  return response.data;
}

export async function createMaintenance(propertyId, data) {
  const response = await api.post(
    `/properties/${propertyId}/maintenance`,
    data
  );
  return response.data;
}

export async function updateMaintenance(maintenanceId, data) {
  const response = await api.patch(
    `/maintenance/${maintenanceId}`,
    data
  );
  return response.data;
}

export async function startMaintenance(maintenanceId) {
  const response = await api.patch(
    `/maintenance/${maintenanceId}/start`
  );
  return response.data;
}

export async function completeMaintenance(maintenanceId) {
  const response = await api.patch(
    `/maintenance/${maintenanceId}/complete`
  );
  return response.data;
}

export async function deleteMaintenance(maintenanceId) {
  await api.delete(`/maintenance/${maintenanceId}`);
}
