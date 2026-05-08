const salaryModel = require('../models/salary.model');
const employeeModel = require('../models/employee.model');
const attendanceModel = require('../models/attendance.model');
const settingModel = require('../models/setting.model');

const calculateAndSaveSalary = async (user_id, month) => {
  const existingSalary = await salaryModel.checkExistingSalary(user_id, month);
  
  const employee = await employeeModel.findByUserId(user_id);
  if (!employee) {
    throw new Error('Data gaji pokok karyawan tidak ditemukan');
  }

  const startDate = `${month}-01`;
  const endDate = `${month}-31`; 

  const lateCount = await attendanceModel.countLateInMonth(user_id, startDate, endDate);
  
  const potonganPerTerlambat = await settingModel.getByKey('potongan_terlambat') || 50000;
  const totalPotongan = lateCount * parseInt(potonganPerTerlambat);
  const totalGaji = employee.gaji_pokok - totalPotongan;

  if (existingSalary) {
    await salaryModel.update(existingSalary.id, totalGaji, totalPotongan);
    return { id: existingSalary.id, month, total_gaji: totalGaji, total_potongan: totalPotongan, late_count: lateCount };
  } else {
    const id = await salaryModel.create({
      user_id,
      month,
      total_gaji: totalGaji,
      total_potongan: totalPotongan
    });
    return { id, month, total_gaji: totalGaji, total_potongan: totalPotongan, late_count: lateCount };
  }
};

const getMySalary = async (user_id) => {
  return await salaryModel.findByUserId(user_id);
};

const getAllSalary = async (filters) => {
  return await salaryModel.findAll(filters);
};

module.exports = {
  calculateAndSaveSalary,
  getMySalary,
  getAllSalary
};
