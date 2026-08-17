import api from "./client";

export async function getPayments(leaseId) {
  const response = await api.get(`/leases/${leaseId}/payments`);
  return response.data;
}

export async function createPayment(leaseId, data) {
  const response = await api.post(`/leases/${leaseId}/payments`, data);
  return response.data;
}

export async function updatePayment(paymentId, data) {
  const response = await api.patch(`/payments/${paymentId}`, data);
  return response.data;
}

export async function markPaymentPaid(paymentId) {
  const response = await api.patch(`/payments/${paymentId}/mark-paid`);
  return response.data;
}

export async function deletePayment(paymentId) {
  await api.delete(`/payments/${paymentId}`);
}
