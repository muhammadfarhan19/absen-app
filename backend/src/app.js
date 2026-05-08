const express = require('express');
const cors = require('cors');
require('dotenv').config();

const authRoutes = require('./routes/auth.route');
const employeeRoutes = require('./routes/employee.route');
const attendanceRoutes = require('./routes/attendance.route');
const salaryRoutes = require('./routes/salary.route');
const settingRoutes = require('./routes/setting.route');

const app = express();

app.use(cors());
app.use(express.json());

// Default Route
app.get('/', (req, res) => {
  res.json({ message: 'Welcome to Kepegawaian API (Strict Rules Implementation)' });
});

// Features Routes
app.use('/auth', authRoutes);
app.use('/employees', employeeRoutes);
app.use('/attendance', attendanceRoutes);
app.use('/salary', salaryRoutes);
app.use('/settings', settingRoutes);

const PORT = process.env.PORT || 3000;

if (process.env.NODE_ENV !== 'test') {
  app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
  });
}

module.exports = app;
