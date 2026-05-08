const bcrypt = require('bcrypt');
const userModel = require('../models/user.model');
const employeeModel = require('../models/employee.model');

const getAllEmployees = async () => {
  return await employeeModel.findAll();
};

const createEmployee = async (employeeData) => {
  const { name, email, password, gaji_pokok } = employeeData;

  const existingUser = await userModel.findByEmail(email);
  if (existingUser) {
    throw new Error('Email sudah terdaftar');
  }

  const hashedPassword = await bcrypt.hash(password, 10);

  const userId = await userModel.create({
    name,
    email,
    password: hashedPassword,
    role: 'karyawan',
  });

  const employeeId = await employeeModel.create({
    user_id: userId,
    gaji_pokok,
  });

  return { id: employeeId, user_id: userId, name, email, gaji_pokok };
};

const updateEmployee = async (id, employeeData) => {
  const { name, email, gaji_pokok } = employeeData;

  const employee = await employeeModel.findById(id);
  if (!employee) {
    throw new Error('Karyawan tidak ditemukan');
  }

  await userModel.update(employee.user_id, { name, email, role: employee.role });
  await employeeModel.update(id, { gaji_pokok });

  return { id, name, email, gaji_pokok };
};

const deleteEmployee = async (id) => {
  const employee = await employeeModel.findById(id);
  if (!employee) {
    throw new Error('Karyawan tidak ditemukan');
  }

  await userModel.deleteUser(employee.user_id);
};

module.exports = {
  getAllEmployees,
  createEmployee,
  updateEmployee,
  deleteEmployee,
};
