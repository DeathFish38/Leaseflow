import api from "./client";

export async function getProperties() {
  const response = await api.get("/properties");
  return response.data;
}

export async function getProperty(id) {
  const response = await api.get(`/properties/${id}`);
  return response.data;
}

export async function createProperty(data) {
  const response = await api.post("/properties", data);
  return response.data;
}

export async function updateProperty(id, data) {
  const response = await api.patch(`/properties/${id}`, data);
  return response.data;
}

export async function deleteProperty(id) {
  await api.delete(`/properties/${id}`);
}