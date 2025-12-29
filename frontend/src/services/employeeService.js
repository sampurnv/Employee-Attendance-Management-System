import api from './api';

export const employeeService = {
  createEmployee: (data) => api.post('/employees', data),
  getEmployee: (id) => api.get(`/employees/${id}`),
  getAllEmployees: () => api.get('/employees'),
  getActiveEmployees: () => api.get('/employees/active'),
  updateEmployee: (id, data) => api.put(`/employees/${id}`, data),
  deleteEmployee: (id) => api.delete(`/employees/${id}`),
  activateEmployee: (id) => api.put(`/employees/${id}/activate`),
};
