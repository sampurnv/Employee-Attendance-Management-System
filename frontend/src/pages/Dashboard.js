import React from 'react';
import {
  Container,
  Grid,
  Paper,
  Typography,
  Box,
  Card,
  CardContent,
} from '@mui/material';
import {
  People,
  Assignment,
  CheckCircle,
  Event,
} from '@mui/icons-material';
import { useAuth } from '../context/AuthContext';

const Dashboard = () => {
  const { user } = useAuth();

  const statsCards = [
    {
      title: 'Total Employees',
      value: '0',
      icon: <People fontSize="large" />,
      color: '#1976d2',
    },
    {
      title: 'Present Today',
      value: '0',
      icon: <CheckCircle fontSize="large" />,
      color: '#2e7d32',
    },
    {
      title: 'Pending Leaves',
      value: '0',
      icon: <Assignment fontSize="large" />,
      color: '#ed6c02',
    },
    {
      title: 'Upcoming Holidays',
      value: '0',
      icon: <Event fontSize="large" />,
      color: '#9c27b0',
    },
  ];

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" gutterBottom>
        Welcome, {user?.username}!
      </Typography>
      <Typography variant="body1" color="textSecondary" gutterBottom>
        Role: {user?.roles?.join(', ')}
      </Typography>

      <Grid container spacing={3} sx={{ mt: 2 }}>
        {statsCards.map((card, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <Card sx={{ height: '100%' }}>
              <CardContent>
                <Box
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                  }}
                >
                  <Box>
                    <Typography color="textSecondary" gutterBottom variant="h6">
                      {card.title}
                    </Typography>
                    <Typography variant="h4">{card.value}</Typography>
                  </Box>
                  <Box sx={{ color: card.color }}>
                    {card.icon}
                  </Box>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Grid container spacing={3} sx={{ mt: 2 }}>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Recent Activities
            </Typography>
            <Typography color="textSecondary">
              No recent activities
            </Typography>
          </Paper>
        </Grid>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Quick Actions
            </Typography>
            <Typography color="textSecondary">
              Use the navigation menu to access different features
            </Typography>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default Dashboard;
