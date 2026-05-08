const employeeService = require('../../src/services/employee.service');
const userModel = require('../../src/models/user.model');
const employeeModel = require('../../src/models/employee.model');
const bcrypt = require('bcrypt');

jest.mock('../../src/models/user.model');
jest.mock('../../src/models/employee.model');
jest.mock('bcrypt');

describe('Employee Service', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('createEmployee', () => {
    test('should create user and employee record', async () => {
      const data = { name: 'John', email: 'john@mail.com', password: 'password', gaji_pokok: 5000000 };
      userModel.findByEmail.mockResolvedValue(null);
      bcrypt.hash.mockResolvedValue('hashed');
      userModel.create.mockResolvedValue(1);
      employeeModel.create.mockResolvedValue(10);

      const result = await employeeService.createEmployee(data);

      expect(userModel.create).toHaveBeenCalled();
      expect(employeeModel.create).toHaveBeenCalledWith({ user_id: 1, gaji_pokok: 5000000 });
      expect(result.name).toBe('John');
    });

    test('should throw error if email exists', async () => {
      userModel.findByEmail.mockResolvedValue({ id: 1 });
      await expect(employeeService.createEmployee({ email: 'exists@mail.com' }))
        .rejects.toThrow('Email sudah terdaftar');
    });
  });

  describe('deleteEmployee', () => {
    test('should delete user (cascade handled by DB)', async () => {
      employeeModel.findById.mockResolvedValue({ id: 1, user_id: 5 });
      await employeeService.deleteEmployee(1);
      expect(userModel.deleteUser).toHaveBeenCalledWith(5);
    });
  });
});
