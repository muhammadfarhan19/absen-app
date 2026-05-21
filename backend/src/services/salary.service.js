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
  
  const bpjsKesehatanRate = await settingModel.getByKey('bpjs_kesehatan_rate') || 0.01;
  const bpjsKetenagakerjaanRate = await settingModel.getByKey('bpjs_ketenagakerjaan_rate') || 0.02;

  const bpjsKesehatan = Math.round(employee.gaji_pokok * parseFloat(bpjsKesehatanRate));
  const bpjsKetenagakerjaan = Math.round(employee.gaji_pokok * parseFloat(bpjsKetenagakerjaanRate));

  const potonganPerTerlambat = await settingModel.getByKey('potongan_terlambat') || 50000;
  const dendaTerlambat = lateCount * parseInt(potonganPerTerlambat);
  
  const totalPotongan = dendaTerlambat + bpjsKesehatan + bpjsKetenagakerjaan;
  const totalGaji = employee.gaji_pokok - totalPotongan;

  if (existingSalary) {
    await salaryModel.update(existingSalary.id, totalGaji, totalPotongan, bpjsKesehatan, bpjsKetenagakerjaan);
    return { 
      id: existingSalary.id, 
      month, 
      total_gaji: totalGaji, 
      total_potongan: totalPotongan, 
      late_count: lateCount,
      bpjs_kesehatan: bpjsKesehatan,
      bpjs_ketenagakerjaan: bpjsKetenagakerjaan
    };
  } else {
    const id = await salaryModel.create({
      user_id,
      month,
      total_gaji: totalGaji,
      total_potongan: totalPotongan,
      bpjs_kesehatan: bpjsKesehatan,
      bpjs_ketenagakerjaan: bpjsKetenagakerjaan
    });
    return { 
      id, 
      month, 
      total_gaji: totalGaji, 
      total_potongan: totalPotongan, 
      late_count: lateCount,
      bpjs_kesehatan: bpjsKesehatan,
      bpjs_ketenagakerjaan: bpjsKetenagakerjaan
    };
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
