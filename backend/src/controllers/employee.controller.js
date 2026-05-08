const employeeService = require('../services/employee.service');
const { sendSuccess, sendError } = require('../utils/responseFormatter');

const getAllEmployees = async (req, res) => {
  try {
    const employees = await employeeService.getAllEmployees();
    sendSuccess(res, 'Daftar karyawan berhasil diambil', employees);
  } catch (error) {
    sendError(res, error.message);
  }
};

const createEmployee = async (req, res) => {
  try {
    const result = await employeeService.createEmployee(req.body);
    sendSuccess(res, 'Karyawan berhasil ditambahkan', result, 201);
  } catch (error) {
    sendError(res, error.message, 400);
  }
};

const updateEmployee = async (req, res) => {
  try {
    const result = await employeeService.updateEmployee(req.params.id, req.body);
    sendSuccess(res, 'Data karyawan berhasil diperbarui', result);
  } catch (error) {
    sendError(res, error.message, 400);
  }
};

const deleteEmployee = async (req, res) => {
  try {
    await employeeService.deleteEmployee(req.params.id);
    sendSuccess(res, 'Karyawan berhasil dihapus');
  } catch (error) {
    sendError(res, error.message, 400);
  }
};

module.exports = {
  getAllEmployees,
  createEmployee,
  updateEmployee,
  deleteEmployee,
};
