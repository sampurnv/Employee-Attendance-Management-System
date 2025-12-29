import api from './api';

export const leaveService = {
  applyLeave: (data) => api.post('/leaves', data),
  approveLeave: (leaveId) => api.put(`/leaves/${leaveId}/approve`),
  rejectLeave: (leaveId, rejectionReason) => 
    api.put(`/leaves/${leaveId}/reject`, null, { 
      params: { rejectionReason } 
    }),
  getMyLeaves: () => api.get('/leaves/my-leaves'),
  getEmployeeLeaves: (employeeId) => api.get(`/leaves/employee/${employeeId}`),
  getPendingLeaves: () => api.get('/leaves/pending'),
  getAllLeaves: () => api.get('/leaves'),
};
