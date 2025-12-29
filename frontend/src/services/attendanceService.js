import api from './api';

export const attendanceService = {
  checkIn: (data) => api.post('/attendance/check-in', data),
  checkOut: (data) => api.post('/attendance/check-out', data),
  getTodayAttendance: () => api.get('/attendance/today'),
  getEmployeeAttendance: (employeeId, startDate, endDate) => 
    api.get(`/attendance/employee/${employeeId}`, { 
      params: { startDate, endDate } 
    }),
  getAllAttendance: (startDate, endDate) => 
    api.get('/attendance', { params: { startDate, endDate } }),
  manualEntry: (data) => api.post('/attendance/manual', null, { params: data }),
};
