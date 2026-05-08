const attendanceModel = require('../models/attendance.model');
const settingModel = require('../models/setting.model');

const getTodayDateString = () => {
  const now = new Date();
  return now.toISOString().split('T')[0];
};

const getCurrentTimeString = () => {
  const now = new Date();
  return now.toTimeString().split(' ')[0];
};

const checkIn = async (user_id) => {
  const date = getTodayDateString();
  const time = getCurrentTimeString();

  const existingAttendance = await attendanceModel.checkTodayAttendance(user_id, date);
  if (existingAttendance) {
    throw new Error('Anda sudah melakukan absen masuk hari ini');
  }

  const maxLateTime = await settingModel.getByKey('max_late_time') || '08:00:00';
  const status = time > maxLateTime ? 'terlambat' : 'hadir';

  const id = await attendanceModel.createCheckIn({
    user_id,
    date,
    check_in: time,
    status
  });

  return { id, date, check_in: time, status };
};

const checkOut = async (user_id) => {
  const date = getTodayDateString();
  const time = getCurrentTimeString();

  const existingAttendance = await attendanceModel.checkTodayAttendance(user_id, date);
  
  if (!existingAttendance) {
    throw new Error('Anda belum absen masuk hari ini');
  }

  if (existingAttendance.check_out) {
    throw new Error('Anda sudah melakukan absen pulang hari ini');
  }

  await attendanceModel.updateCheckOut(existingAttendance.id, time);

  return { id: existingAttendance.id, date, check_out: time };
};

const getMyAttendance = async (user_id) => {
  return await attendanceModel.findByUserId(user_id);
};

const getAllAttendance = async (filters) => {
  return await attendanceModel.findAll(filters);
};

module.exports = {
  checkIn,
  checkOut,
  getMyAttendance,
  getAllAttendance
};
