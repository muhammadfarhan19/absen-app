const express = require('express');
const router = express.Router();
const attendanceController = require('../controllers/attendance.controller');
const authMiddleware = require('../middlewares/authMiddleware');
const roleMiddleware = require('../middlewares/roleMiddleware');

router.use(authMiddleware);

router.post('/check-in', attendanceController.checkIn);
router.post('/check-out', attendanceController.checkOut);
router.get('/me', attendanceController.getMyAttendance);

// Admin only
router.get('/', roleMiddleware('admin'), attendanceController.getAllAttendance);

module.exports = router;
