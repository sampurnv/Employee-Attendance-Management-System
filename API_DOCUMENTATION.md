# API Documentation

## Employee Attendance Management System - REST API

Base URL: `http://localhost:8080/api`

Swagger UI: `http://localhost:8080/api/swagger-ui.html`

---

## Authentication Endpoints

### 1. User Login
**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": "507f1f77bcf86cd799439011",
  "username": "john_doe",
  "email": "john@example.com",
  "roles": ["EMPLOYEE"],
  "employeeId": "507f1f77bcf86cd799439012"
}
```

### 2. User Registration
**Endpoint:** `POST /auth/register`

**Request Body:**
```json
{
  "username": "jane_doe",
  "email": "jane@example.com",
  "password": "password123",
  "roles": ["EMPLOYEE"]
}
```

**Response:** Same as login response

---

## Employee Management Endpoints

### 3. Create Employee
**Endpoint:** `POST /employees`

**Authorization:** Required (ADMIN or MANAGER)

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "phone": "+1234567890",
  "address": "123 Main St, City",
  "dateOfBirth": "1990-01-15",
  "joiningDate": "2024-01-01",
  "departmentId": "507f1f77bcf86cd799439013",
  "designation": "Software Engineer",
  "role": "EMPLOYEE",
  "casualLeaveBalance": 12,
  "sickLeaveBalance": 12,
  "paidLeaveBalance": 12
}
```

### 4. Get All Employees
**Endpoint:** `GET /employees`

**Authorization:** Required

### 5. Get Employee by ID
**Endpoint:** `GET /employees/{id}`

**Authorization:** Required

### 6. Update Employee
**Endpoint:** `PUT /employees/{id}`

**Authorization:** Required (ADMIN or MANAGER)

### 7. Delete Employee
**Endpoint:** `DELETE /employees/{id}`

**Authorization:** Required (ADMIN)

### 8. Activate Employee
**Endpoint:** `PUT /employees/{id}/activate`

**Authorization:** Required (ADMIN)

---

## Attendance Management Endpoints

### 9. Check In
**Endpoint:** `POST /attendance/check-in`

**Authorization:** Required

**Request Body (Optional):**
```json
{
  "remarks": "On time",
  "checkInTime": "09:00:00"
}
```

### 10. Check Out
**Endpoint:** `POST /attendance/check-out`

**Authorization:** Required

**Request Body (Optional):**
```json
{
  "remarks": "Completed work",
  "checkOutTime": "18:00:00"
}
```

### 11. Get Today's Attendance
**Endpoint:** `GET /attendance/today`

**Authorization:** Required

**Response:**
```json
{
  "id": "507f1f77bcf86cd799439014",
  "employeeId": "507f1f77bcf86cd799439012",
  "employeeName": "John Doe",
  "date": "2024-01-15",
  "checkInTime": "09:00:00",
  "checkOutTime": "18:00:00",
  "status": "PRESENT",
  "workingHours": 9.0,
  "isLate": false,
  "isEarlyCheckout": false,
  "remarks": "On time",
  "manualEntry": false,
  "createdAt": "2024-01-15T09:00:00"
}
```

### 12. Get Employee Attendance History
**Endpoint:** `GET /attendance/employee/{employeeId}`

**Authorization:** Required (ADMIN or MANAGER)

**Query Parameters:**
- `startDate` (required): Start date (YYYY-MM-DD)
- `endDate` (required): End date (YYYY-MM-DD)

### 13. Get All Attendance Records
**Endpoint:** `GET /attendance`

**Authorization:** Required (ADMIN or MANAGER)

**Query Parameters:**
- `startDate` (required): Start date (YYYY-MM-DD)
- `endDate` (required): End date (YYYY-MM-DD)

### 14. Manual Attendance Entry
**Endpoint:** `POST /attendance/manual`

**Authorization:** Required (ADMIN)

**Query Parameters:**
- `employeeId` (required)
- `date` (required): Date (YYYY-MM-DD)
- `checkInTime` (optional): Time (HH:mm:ss)
- `checkOutTime` (optional): Time (HH:mm:ss)
- `status` (required): PRESENT, ABSENT, HALF_DAY, or LEAVE
- `remarks` (optional)

---

## Leave Management Endpoints

### 15. Apply for Leave
**Endpoint:** `POST /leaves`

**Authorization:** Required

**Request Body:**
```json
{
  "leaveType": "CASUAL",
  "startDate": "2024-02-01",
  "endDate": "2024-02-03",
  "reason": "Family vacation"
}
```

**Leave Types:**
- `CASUAL` - Casual Leave
- `SICK` - Sick Leave
- `PAID` - Paid Leave
- `UNPAID` - Unpaid Leave

### 16. Get My Leave Applications
**Endpoint:** `GET /leaves/my-leaves`

**Authorization:** Required

### 17. Get Employee Leave Applications
**Endpoint:** `GET /leaves/employee/{employeeId}`

**Authorization:** Required (ADMIN or MANAGER)

### 18. Get Pending Leave Applications
**Endpoint:** `GET /leaves/pending`

**Authorization:** Required (ADMIN or MANAGER)

### 19. Get All Leave Applications
**Endpoint:** `GET /leaves`

**Authorization:** Required (ADMIN or MANAGER)

### 20. Approve Leave
**Endpoint:** `PUT /leaves/{leaveId}/approve`

**Authorization:** Required (ADMIN or MANAGER)

### 21. Reject Leave
**Endpoint:** `PUT /leaves/{leaveId}/reject`

**Authorization:** Required (ADMIN or MANAGER)

**Query Parameters:**
- `rejectionReason` (required): Reason for rejection

---

## Common Response Codes

- `200 OK` - Success
- `201 Created` - Resource created successfully
- `204 No Content` - Success with no response body
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource
- `500 Internal Server Error` - Server error

---

## Error Response Format

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input data",
  "path": "/api/employees",
  "validationErrors": {
    "email": "Email should be valid",
    "firstName": "First name is required"
  }
}
```

---

## Authentication

All endpoints except `/auth/login` and `/auth/register` require authentication.

**Header Format:**
```
Authorization: Bearer {token}
```

**Token Expiration:**
- Access Token: 24 hours
- Refresh Token: 7 days

---

## Rate Limiting

Currently no rate limiting is implemented. Consider implementing for production use.

---

## CORS Configuration

Allowed Origins:
- `http://localhost:3000`
- `http://localhost:3001`

Allowed Methods: `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`

---

## Database Collections

### users
Stores user authentication and profile information

### employees
Stores employee details, leave balances, and department assignments

### attendance
Stores daily attendance records

### leaves
Stores leave applications and their status

### departments
Stores department information

### holidays
Stores holiday calendar

---

## Notes

1. All date fields use ISO 8601 format: `YYYY-MM-DD`
2. All time fields use 24-hour format: `HH:mm:ss`
3. All timestamps use ISO 8601 format: `YYYY-MM-DDThh:mm:ss`
4. MongoDB ObjectIds are used for all ID fields
5. Default password for new employees is `Welcome@123`
6. Office hours are configurable in `application.properties`
