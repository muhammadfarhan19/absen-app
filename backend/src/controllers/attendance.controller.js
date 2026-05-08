const attendanceService = require('../services/attendance.service');
const { sendSuccess, sendError } = require('../utils/responseFormatter');

const checkIn = async (req, res) => {
  try {
    const result = await attendanceService.checkIn(req.user.id);
    sendSuccess(res, 'Absen masuk berhasil', result);
  } catch (error) {
    sendError(res, error.message, 400);
  }
};

const checkOut = async (req, res) => {
  try {
    const result = await attendanceService.checkOut(req.user.id);
    sendSuccess(res, 'Absen pulang berhasil', result);
  } catch (error) {
    sendError(res, error.message, 400);
  }
};

const getMyAttendance = async (req, res) => {
  try {
    const attendance = await attendanceService.getMyAttendance(req.user.id);
    sendSuccess(res, 'Riwayat absen berhasil diambil', attendance);
  } catch (error) {
    sendError(res, error.message);
  }
};

const getAllAttendance = async (req, res) => {
  try {
    const attendance = await attendanceService.getAllAttendance(req.query);
    sendSuccess(res, 'Semua data absensi berhasil diambil', attendance);
  } catch (error) {
    sendError(res, error.message);
  }
};

module.exports = {
  checkIn,
  checkOut,
  getMyAttendance,
  getAllAttendance,
};
