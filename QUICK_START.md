# Quick Setup Guide

## 🚀 Fastest Way to Get Started

### Using Docker Compose (Recommended - 5 minutes)

1. **Clone and Start**
```bash
git clone https://github.com/sampurnv/Employee-Attendance-Management-System.git
cd Employee-Attendance-Management-System
docker-compose up --build
```

2. **Access the Application**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html

3. **Create Your First User**
- Go to http://localhost:3000/register
- Fill in the registration form
- Select roles (EMPLOYEE, MANAGER, or ADMIN)
- Click Register

That's it! 🎉

---

## 📝 Quick Testing Guide

### Test User Registration
1. Go to Register page
2. Create a user with ADMIN role
3. Login with credentials

### Test Attendance Management
1. Login as Employee
2. Go to Attendance page
3. Click "Check In"
4. Wait a few seconds/minutes
5. Click "Check Out"
6. View your working hours

### Test Leave Management
1. Go to Leaves page
2. Click "Apply Leave"
3. Select leave type and dates
4. Submit the application
5. Login as Manager/Admin to approve

### Test Employee Management (Admin/Manager only)
1. Login as Admin or Manager
2. Go to Employees page
3. View all employees

---

## 🔑 Default Credentials

When employees are created via the API, they get:
- Username: First part of their email (before @)
- Password: `Welcome@123`

---

## 📊 API Testing with Swagger

1. Open http://localhost:8080/api/swagger-ui.html
2. Click on "Authentication" section
3. Try the `/auth/register` endpoint
4. Copy the token from response
5. Click "Authorize" button at top
6. Enter: `Bearer {your-token}`
7. Now you can test all authenticated endpoints

---

## 🛠️ Development Mode

### Backend Development
```bash
cd backend
mvn spring-boot:run
```
Access at: http://localhost:8080/api

### Frontend Development
```bash
cd frontend
npm install
npm start
```
Access at: http://localhost:3000

### MongoDB
Make sure MongoDB is running on localhost:27017

---

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Check what's using the port
lsof -i :8080  # Backend
lsof -i :3000  # Frontend
lsof -i :27017 # MongoDB

# Kill the process
kill -9 <PID>
```

### Docker Issues
```bash
# Clean up everything
docker-compose down -v
docker system prune -a

# Rebuild
docker-compose up --build
```

### MongoDB Connection Issues
```bash
# Check MongoDB is running
docker ps | grep mongo

# View MongoDB logs
docker logs attendance-mongodb
```

### Backend Build Issues
```bash
cd backend
mvn clean install -U
```

### Frontend Build Issues
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

---

## 📱 Sample API Calls

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### Check In
```bash
curl -X POST http://localhost:8080/api/attendance/check-in \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json"
```

### Apply Leave
```bash
curl -X POST http://localhost:8080/api/leaves \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "leaveType": "CASUAL",
    "startDate": "2024-02-01",
    "endDate": "2024-02-03",
    "reason": "Personal work"
  }'
```

---

## 🎯 Quick Feature Checklist

After setup, you can test:
- ✅ User Registration
- ✅ User Login
- ✅ JWT Authentication
- ✅ Role-based Access Control
- ✅ Check In/Check Out
- ✅ View Attendance
- ✅ Apply for Leave
- ✅ Approve/Reject Leave (Manager/Admin)
- ✅ View Employees
- ✅ Create Employees (Admin/Manager)
- ✅ Dashboard Statistics

---

## 📚 Next Steps

1. Explore the API documentation at `/api/swagger-ui.html`
2. Check out `API_DOCUMENTATION.md` for detailed endpoint info
3. Read `README.md` for comprehensive guide
4. Customize configurations in `application.properties`
5. Add more features as needed!

---

## 💡 Tips

- Use Chrome DevTools Network tab to debug API calls
- Check browser console for frontend errors
- Use MongoDB Compass to view database: mongodb://localhost:27017
- Backend logs show in terminal when running locally
- Use Postman for advanced API testing

---

## 🆘 Need Help?

- Check the README.md for detailed documentation
- Review API_DOCUMENTATION.md for API details
- Open an issue on GitHub
- Check application logs for errors

Happy coding! 🚀
