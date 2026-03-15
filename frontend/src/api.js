import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

export default api;

// --- Customers ---
export const getCustomers = (search) => api.get('/customers', { params: search ? { search } : {} });
export const getCustomer = (id) => api.get(`/customers/${id}`);
export const createCustomer = (data) => api.post('/customers', data);
export const updateCustomer = (id, data) => api.put(`/customers/${id}`, data);
export const deleteCustomer = (id) => api.delete(`/customers/${id}`);
export const getCustomerStats = (id) => api.get(`/customers/${id}/stats`);

// --- Orders ---
export const getOrders = (status) => api.get('/orders', { params: status ? { status } : {} });
export const getOrder = (id) => api.get(`/orders/${id}`);
export const createOrder = (data) => api.post('/orders', data);
export const updateStatus = (id, status) => api.patch(`/orders/${id}/status`, { status });
export const updateAmount = (id, data) => api.patch(`/orders/${id}/amount`, data);
export const updateDeliveryDate = (id, date) => api.patch(`/orders/${id}/delivery-date`, { deliveryDate: date });
export const getOrderDetails = (id) => api.get(`/orders/${id}/details`);
export const updateOrderDetail = (detailId, data) => api.put(`/orders/details/${detailId}`, data);
export const sendEmail = (id) => api.post(`/orders/${id}/send-email`);
export const uploadDepositImage = (id, file) => {
  const fd = new FormData(); fd.append('file', file);
  return api.post(`/orders/${id}/upload-deposit`, fd, { headers: { 'Content-Type': 'multipart/form-data' } });
};
export const uploadFullImage = (id, file) => {
  const fd = new FormData(); fd.append('file', file);
  return api.post(`/orders/${id}/upload-full`, fd, { headers: { 'Content-Type': 'multipart/form-data' } });
};

// --- Materials ---
export const getMaterials = (q) => api.get('/materials', { params: q ? { q } : {} });
export const getLowStock = () => api.get('/materials/low-stock');
export const createMaterial = (data) => api.post('/materials', data);
export const updateMaterial = (id, data) => api.put(`/materials/${id}`, data);
export const deleteMaterial = (id) => api.delete(`/materials/${id}`);
export const uploadMaterialImage = (id, file) => {
  const fd = new FormData(); fd.append('file', file);
  return api.post(`/materials/${id}/upload-image`, fd, { headers: { 'Content-Type': 'multipart/form-data' } });
};

// --- Product Types ---
export const getProductTypes = () => api.get('/product-types');
export const createProductType = (data) => api.post('/product-types', data);
export const updateProductType = (id, data) => api.put(`/product-types/${id}`, data);
export const deleteProductType = (id) => api.delete(`/product-types/${id}`);
export const uploadProductTypeImage = (id, file) => {
  const fd = new FormData(); fd.append('file', file);
  return api.post(`/product-types/${id}/upload-image`, fd, { headers: { 'Content-Type': 'multipart/form-data' } });
};

// --- Measurement Types ---
export const getMeasurementTypes = (productTypeId) =>
  api.get('/measurement-types', { params: productTypeId ? { productTypeId } : {} });
export const createMeasurementType = (data) => api.post('/measurement-types', data);
export const updateMeasurementType = (id, data) => api.put(`/measurement-types/${id}`, data);
export const deleteMeasurementType = (id) => api.delete(`/measurement-types/${id}`);

// --- Measurements ---
export const getMeasurements = (params) => api.get('/measurements', { params });
export const createMeasurement = (data) => api.post('/measurements', data);
export const updateMeasurement = (id, data) => api.put(`/measurements/${id}`, data);

// --- Dashboard ---
export const getDashboard = () => api.get('/dashboard');

// --- Files ---
export const uploadFile = (file) => {
  const fd = new FormData(); fd.append('file', file);
  return api.post('/files/upload', fd, { headers: { 'Content-Type': 'multipart/form-data' } });
};

export const imgUrl = (filename) =>
  filename ? `http://localhost:8080/uploads/${filename}` : null;
