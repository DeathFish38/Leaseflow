import api from "./client";

export async function getLease(propertyId) {
  const response = await api.get(`/properties/${propertyId}/lease`);
  return response.data;
}

export async function createLease(propertyId, data) {
  const response = await api.post(`/properties/${propertyId}/lease`, data);
  return response.data;
}

export async function updateLease(leaseId, data) {
  const response = await api.patch(`/lease/${leaseId}`, data);
  return response.data;
}

export async function deleteLease(leaseId) {
  await api.delete(`/lease/${leaseId}`);
}
