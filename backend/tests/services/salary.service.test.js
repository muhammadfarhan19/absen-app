const salaryService = require('../../src/services/salary.service');
const salaryModel = require('../../src/models/salary.model');
const employeeModel = require('../../src/models/employee.model');
const attendanceModel = require('../../src/models/attendance.model');
const settingModel = require('../../src/models/setting.model');

jest.mock('../../src/models/salary.model');
jest.mock('../../src/models/employee.model');
jest.mock('../../src/models/attendance.model');
jest.mock('../../src/models/setting.model');

describe('Salary Service', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('calculateAndSaveSalary', () => {
    test('should calculate salary correctly with deductions', async () => {
      const user_id = 1;
      const month = '2023-10';
      
      employeeModel.findByUserId.mockResolvedValue({ gaji_pokok: 5000000 });
      attendanceModel.countLateInMonth.mockResolvedValue(2); // 2 times late
      settingModel.getByKey.mockResolvedValue('50000'); // 50k per late
      salaryModel.checkExistingSalary.mockResolvedValue(null);
      
      const result = await salaryService.calculateAndSaveSalary(user_id, month);

      // 5,000,000 - (2 * 50,000) = 4,900,000
      expect(result.total_gaji).toBe(4900000);
      expect(result.total_potongan).toBe(100000);
      expect(salaryModel.create).toHaveBeenCalled();
    });

    test('should update existing salary record if exists', async () => {
      employeeModel.findByUserId.mockResolvedValue({ gaji_pokok: 5000000 });
      attendanceModel.countLateInMonth.mockResolvedValue(0);
      settingModel.getByKey.mockResolvedValue('50000');
      salaryModel.checkExistingSalary.mockResolvedValue({ id: 99 });

      await salaryService.calculateAndSaveSalary(1, '2023-10');

      expect(salaryModel.update).toHaveBeenCalledWith(99, 5000000, 0);
    });
  });
});
