# Employee Attendance Management System

A full-stack Employee Attendance Management System that allows organizations to manage employees, track daily attendance, working hours, leaves, and generate reports. Built with Spring Boot, MongoDB, and React.

## 🚀 Features

### 🔐 Authentication & Authorization
- User registration and login
- JWT-based authentication
- Role-based access control (Admin / Manager / Employee)
- Password encryption using BCrypt
- Session & token expiration handling

### 👤 Employee Management (Admin / Manager)
- Add, update, delete employees
- Assign roles and departments
- Activate / deactivate employees
- View employee attendance history
- Manage employee leave balances

### ⏱️ Attendance Management
- Daily Check-In / Check-Out
- Auto timestamp capture
- Working hours calculation
- Late arrival detection
- Manual attendance correction (Admin only)
- Attendance status tracking (Present, Absent, Half-day, Leave)

### 📅 Leave Management
- Apply for leave (Employee)
- Leave types: Casual, Sick, Paid, Unpaid
- Leave approval / rejection (Manager / Admin)
- Leave balance tracking
- Leave history

### 📊 Reports & Analytics
- Daily, weekly, monthly attendance reports
- Employee-wise attendance tracking
- Leave management overview

## 🛠️ Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** (JWT Authentication)
- **MongoDB**
- **Spring Data MongoDB**
- **RESTful APIs**
- **Maven**
- **Swagger/OpenAPI** (API Documentation)

### Frontend
- **React 18**
- **JavaScript (ES6+)**
- **Material UI**
- **Axios**
- **React Router**

### DevOps
- **Docker**
- **Docker Compose**

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- Java 17 or higher
- Node.js 18 or higher
- MongoDB 7.0 or higher
- Docker and Docker Compose (for containerized deployment)
- Maven 3.9 or higher

## 🚦 Getting Started

### Option 1: Running with Docker Compose (Recommended)

1. **Clone the repository**
```bash
git clone https://github.com/sampurnv/Employee-Attendance-Management-System.git
cd Employee-Attendance-Management-System
```

2. **Build and run with Docker Compose**
```bash
docker-compose up --build
```

3. **Access the applications**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html

### Option 2: Running Locally

#### Backend Setup

1. **Navigate to backend directory**
```bash
cd backend
```

2. **Configure MongoDB**
Edit `src/main/resources/application.properties` and update MongoDB URI if needed:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/attendance_db
```

3. **Build the application**
```bash
mvn clean install
```

4. **Run the backend**
```bash
mvn spring-boot:run
```

The backend will start on http://localhost:8080

#### Frontend Setup

1. **Navigate to frontend directory**
```bash
cd frontend
```

2. **Install dependencies**
```bash
npm install
```

3. **Configure API URL**
Create a `.env` file in the frontend directory:
```
REACT_APP_API_URL=http://localhost:8080/api
```

4. **Start the development server**
```bash
npm start
```

The frontend will start on http://localhost:3000

## 📚 API Documentation

Once the backend is running, access the Swagger UI at:
```
http://localhost:8080/api/swagger-ui.html
```

### Key API Endpoints

#### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

#### Employee Management
- `GET /api/employees` - Get all employees
- `POST /api/employees` - Create employee
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee

#### Attendance Management
- `POST /api/attendance/check-in` - Check in
- `POST /api/attendance/check-out` - Check out
- `GET /api/attendance/today` - Get today's attendance
- `GET /api/attendance/employee/{id}` - Get employee attendance history

#### Leave Management
- `POST /api/leaves` - Apply for leave
- `GET /api/leaves/my-leaves` - Get user's leaves
- `PUT /api/leaves/{id}/approve` - Approve leave
- `PUT /api/leaves/{id}/reject` - Reject leave

## 🔑 Default Configuration

### Office Hours
- Start Time: 09:00
- End Time: 18:00
- Working Hours: 9 hours
- Late Threshold: 15 minutes

### Default User Roles
When registering employees, you can assign the following roles:
- **EMPLOYEE**: Basic user with attendance and leave access
- **MANAGER**: Can manage employees and approve leaves
- **ADMIN**: Full system access

### Default Employee Password
When creating employees through the API, the default password is: `Welcome@123`

## 🗄️ Database Schema

### Collections

#### Users
- Stores user authentication information
- Links to Employee records

#### Employees
- Employee personal and professional information
- Leave balances
- Department assignments

#### Attendance
- Daily attendance records
- Check-in/check-out times
- Working hours calculation

#### Leaves
- Leave applications
- Approval status
- Leave types and durations

#### Departments
- Department information
- Manager assignments

#### Holidays
- Holiday calendar
- Holiday dates and descriptions

## 🔒 Security Features

- JWT token-based authentication
- BCrypt password encryption
- Role-based access control
- CORS configuration
- Secure API endpoints
- Token expiration handling

## 📱 User Interface Features

- Responsive design for mobile and desktop
- Material UI components
- Role-based navigation
- Form validation
- Loading states
- Error handling
- Success/error notifications

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 📦 Building for Production

### Backend
```bash
cd backend
mvn clean package -DskipTests
```
The JAR file will be created in `target/` directory.

### Frontend
```bash
cd frontend
npm run build
```
The production build will be created in `build/` directory.

## 🐳 Docker Deployment

### Build individual images

**Backend:**
```bash
cd backend
docker build -t attendance-backend .
```

**Frontend:**
```bash
cd frontend
docker build -t attendance-frontend .
```

### Run with Docker Compose
```bash
docker-compose up -d
```

### Stop services
```bash
docker-compose down
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

## 👥 Authors

- Development Team

## 📞 Support

For support, please open an issue in the GitHub repository.

## 🎯 Future Enhancements

- Email notifications for leave approvals/rejections
- Push notifications
- CSV/Excel/PDF report exports
- Advanced analytics and dashboards
- Mobile application
- Biometric integration
- Geolocation-based attendance
- Shift management
- Overtime tracking
- Performance reviews integration

## 📝 Notes

- Make sure MongoDB is running before starting the backend
- The backend needs to be running before starting the frontend
- Default JWT secret should be changed in production
- Configure email settings in `application.properties` for email notifications
- All dates follow ISO 8601 format (YYYY-MM-DD)
- All times follow 24-hour format (HH:mm)
