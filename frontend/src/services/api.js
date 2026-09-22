import axios from 'axios';

const API_BASE_URL = (import.meta.env.VITE_API_URL || '').replace(/\/$/, '');

const api = axios.create({
  baseURL: API_BASE_URL || '/',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';    }
    return Promise.reject(error);
  }
);

// ─── Auth ────────────────────────────────────────────────────────────────────
export const authApi = {
  login: (data) => api.post('/auth/login', data),
  register: (data) => api.post('/auth/register', data),
  logout: (refreshToken) => api.post('/auth/logout', refreshToken ? { refreshToken } : {}),
  changePassword: (data) => api.post('/auth/change-password', data),
  forgotPassword: (email) => api.post('/auth/forgot-password', { email }),
  resetPassword: (data) => api.post('/auth/reset-password', data),
  refreshToken: (refreshToken) => api.post('/auth/refresh', { refreshToken }),
};

// ─── Customers ───────────────────────────────────────────────────────────────
export const customerApi = {
  getAll: () => api.get('/api/customers'),
  getById: (id) => api.get(`/api/customers/${id}`),
  create: (data) => api.post('/api/customers', data),
  update: (id, data) => api.put(`/api/customers/${id}`, data),
  delete: (id) => api.delete(`/api/customers/${id}`),
  search: (q) => api.get(`/api/customers/search?query=${encodeURIComponent(q)}`),
};

// ─── Policies ────────────────────────────────────────────────────────────────
export const policyApi = {
  getAll: () => api.get('/api/policies'),
  getById: (id) => api.get(`/api/policies/${id}`),
  getByCustomer: (customerId) => api.get(`/api/policies/customer/${customerId}`),
  create: (data) => api.post('/api/policies', data),
  update: (id, data) => api.put(`/api/policies/${id}`, data),
  delete: (id) => api.delete(`/api/policies/${id}`),
  renew: (id) => api.put(`/api/policies/${id}/renew`),
};

// ─── Claims ──────────────────────────────────────────────────────────────────
export const claimApi = {
  getAll: () => api.get('/api/claims'),
  getById: (id) => api.get(`/api/claims/${id}`),
  getByPolicy: (policyId) => api.get(`/api/claims/policy/${policyId}`),
  create: (data) => api.post('/api/claims', data),
  update: (id, data) => api.put(`/api/claims/${id}`, data),
  approve: (id, data) => api.put(`/api/claims/${id}/approve`, data),
  reject: (id, reason) => api.put(`/api/claims/${id}/reject`, { reason }),
};

// ─── Payments ────────────────────────────────────────────────────────────────
export const paymentApi = {
  getAll: () => api.get('/api/payments'),
  getById: (id) => api.get(`/api/payments/${id}`),
  create: (data) => api.post('/api/payments', data),
  getByPolicy: (policyId) => api.get(`/api/payments/policy/${policyId}`),
};

// ─── Admin ───────────────────────────────────────────────────────────────────
export const adminApi = {
  getDashboard: () => api.get('/api/admin/dashboard'),
  getUsers: () => api.get('/api/admin/users'),
  updateUserRole: (userId, role) => api.put(`/api/admin/users/${userId}/role?role=${role}`),
  toggleUserStatus: (userId) => api.put(`/api/admin/users/${userId}/status`),
  getAgents: () => api.get('/api/admin/agents'),
  createAgent: (data) => api.post('/api/admin/agents', data),
  getEmployees: () => api.get('/api/admin/employees'),
  createEmployee: (data) => api.post('/api/admin/employees', data),
};

// ─── Documents ───────────────────────────────────────────────────────────────
export const documentApi = {
  upload: (formData) => api.post('/api/documents/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),
  getByCustomer: (customerId) => api.get(`/api/documents/customer/${customerId}`),
  downloadUrl: (id) => `${API_BASE_URL}/api/documents/${id}/download`,
  verify: (id) => api.put(`/api/documents/${id}/verify`),
  reject: (id) => api.put(`/api/documents/${id}/reject`),
  getAll: () => api.get('/api/documents'),
};

// ─── Premiums ────────────────────────────────────────────────────────────────
export const premiumApi = {
  getSchedule: (policyId) => api.get(`/api/premiums/policy/${policyId}`),
  pay: (scheduleId, method, remarks) =>
    api.post(`/api/premiums/${scheduleId}/pay?paymentMethod=${method}&remarks=${encodeURIComponent(remarks || '')}`),
  getOverdue: () => api.get('/api/premiums/overdue'),
};

// ─── Reports ─────────────────────────────────────────────────────────────────
export const reportApi = {
  policyPdfUrl: `${API_BASE_URL}/api/reports/policies/pdf`,
  claimPdfUrl: `${API_BASE_URL}/api/reports/claims/pdf`,
  excelUrl: `${API_BASE_URL}/api/reports/business/excel`,
};

// ─── Notifications ───────────────────────────────────────────────────────────
export const notificationApi = {
  getAll: () => api.get('/api/notifications/my'),
  getUnread: () => api.get('/api/notifications/my/unread'),
  markRead: (id) => api.put(`/api/notifications/${id}/read`),
  markAllRead: () => api.put('/api/notifications/my/read-all'),
  getUnreadCount: () => api.get('/api/notifications/my/unread-count'),
};

// ─── Transactions ────────────────────────────────────────────────────────────
export const transactionApi = {
  getAll: () => api.get('/api/transactions'),
  getByPolicy: (policyId) => api.get(`/api/transactions/policy/${policyId}`),
};

// ─── Search ──────────────────────────────────────────────────────────────────
export const searchApi = {
  query: (q) => api.get(`/api/search?query=${encodeURIComponent(q)}`),
};

export default api;
