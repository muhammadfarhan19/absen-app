const salaryService = require('../services/salary.service');
const { sendSuccess, sendError } = require('../utils/responseFormatter');

const getMySalary = async (req, res) => {
  try {
    const salary = await salaryService.getMySalary(req.user.id);
    sendSuccess(res, 'Data gaji berhasil diambil', salary);
  } catch (error) {
    sendError(res, error.message);
  }
};

const getAllSalary = async (req, res) => {
  try {
    const salary = await salaryService.getAllSalary(req.query);
    sendSuccess(res, 'Semua data gaji berhasil diambil', salary);
  } catch (error) {
    sendError(res, error.message);
  }
};

const calculateSalary = async (req, res) => {
  try {
    const { user_id, month } = req.body;
    if (!user_id || !month) {
      return sendError(res, 'User ID dan Bulan (YYYY-MM) wajib diisi', 400);
    }
    const result = await salaryService.calculateAndSaveSalary(user_id, month);
    sendSuccess(res, 'Gaji berhasil dihitung dan disimpan', result);
  } catch (error) {
    sendError(res, error.message, 400);
  }
};

module.exports = {
  getMySalary,
  getAllSalary,
  calculateSalary,
};
