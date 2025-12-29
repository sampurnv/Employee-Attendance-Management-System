import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Typography,
  Button,
  Box,
  Alert,
  Card,
  CardContent,
  Divider,
} from '@mui/material';
import { AccessTime, CheckCircle } from '@mui/icons-material';
import { attendanceService } from '../services/attendanceService';

const Attendance = () => {
  const [todayAttendance, setTodayAttendance] = useState(null);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    loadTodayAttendance();
  }, []);

  const loadTodayAttendance = async () => {
    try {
      const response = await attendanceService.getTodayAttendance();
      if (response.status === 204) {
        setTodayAttendance(null);
      } else {
        setTodayAttendance(response.data);
      }
    } catch (error) {
      console.error('Error loading attendance:', error);
    }
  };

  const handleCheckIn = async () => {
    setLoading(true);
    setMessage({ type: '', text: '' });
    try {
      await attendanceService.checkIn({});
      setMessage({ type: 'success', text: 'Checked in successfully!' });
      loadTodayAttendance();
    } catch (error) {
      setMessage({
        type: 'error',
        text: error.response?.data?.message || 'Failed to check in',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleCheckOut = async () => {
    setLoading(true);
    setMessage({ type: '', text: '' });
    try {
      await attendanceService.checkOut({});
      setMessage({ type: 'success', text: 'Checked out successfully!' });
      loadTodayAttendance();
    } catch (error) {
      setMessage({
        type: 'error',
        text: error.response?.data?.message || 'Failed to check out',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" gutterBottom>
        Attendance
      </Typography>

      {message.text && (
        <Alert severity={message.type} sx={{ mb: 2 }}>
          {message.text}
        </Alert>
      )}

      <Card sx={{ mb: 3 }}>
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Today's Attendance
          </Typography>
          <Divider sx={{ mb: 2 }} />

          {todayAttendance ? (
            <Box>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <CheckCircle color="success" sx={{ mr: 1 }} />
                <Typography>
                  Status: {todayAttendance.status}
                </Typography>
              </Box>
              <Typography variant="body1" gutterBottom>
                Check-In Time: {todayAttendance.checkInTime || 'N/A'}
              </Typography>
              <Typography variant="body1" gutterBottom>
                Check-Out Time: {todayAttendance.checkOutTime || 'Not checked out yet'}
              </Typography>
              {todayAttendance.workingHours && (
                <Typography variant="body1" gutterBottom>
                  Working Hours: {todayAttendance.workingHours.toFixed(2)} hours
                </Typography>
              )}
              {todayAttendance.isLate && (
                <Alert severity="warning" sx={{ mt: 2 }}>
                  You were late today
                </Alert>
              )}
            </Box>
          ) : (
            <Typography color="textSecondary">
              No attendance record for today
            </Typography>
          )}
        </CardContent>
      </Card>

      <Paper sx={{ p: 3 }}>
        <Typography variant="h6" gutterBottom>
          Actions
        </Typography>
        <Box sx={{ display: 'flex', gap: 2, mt: 2 }}>
          <Button
            variant="contained"
            color="primary"
            startIcon={<AccessTime />}
            onClick={handleCheckIn}
            disabled={loading || (todayAttendance && !todayAttendance.checkOutTime)}
            fullWidth
          >
            Check In
          </Button>
          <Button
            variant="contained"
            color="secondary"
            startIcon={<CheckCircle />}
            onClick={handleCheckOut}
            disabled={loading || !todayAttendance || todayAttendance.checkOutTime}
            fullWidth
          >
            Check Out
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default Attendance;
