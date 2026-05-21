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
      settingModel.getByKey.mockImplementation(async (key) => {
        if (key === 'bpjs_kesehatan_rate') return '0.01';
        if (key === 'bpjs_ketenagakerjaan_rate') return '0.02';
        if (key === 'potongan_terlambat') return '50000';
        return null;
      });
      salaryModel.checkExistingSalary.mockResolvedValue(null);
      
      const result = await salaryService.calculateAndSaveSalary(user_id, month);

      // 5,000,000 - (2 * 50,000 + 50,000 + 100,000) = 4,750,000
      expect(result.total_gaji).toBe(4750000);
      expect(result.total_potongan).toBe(250000);
      expect(result.bpjs_kesehatan).toBe(50000);
      expect(result.bpjs_ketenagakerjaan).toBe(100000);
      expect(salaryModel.create).toHaveBeenCalled();
    });

    test('should update existing salary record if exists', async () => {
      employeeModel.findByUserId.mockResolvedValue({ gaji_pokok: 5000000 });
      attendanceModel.countLateInMonth.mockResolvedValue(0);
      settingModel.getByKey.mockImplementation(async (key) => {
        if (key === 'bpjs_kesehatan_rate') return '0.01';
        if (key === 'bpjs_ketenagakerjaan_rate') return '0.02';
        if (key === 'potongan_terlambat') return '50000';
        return null;
      });
      salaryModel.checkExistingSalary.mockResolvedValue({ id: 99 });

      await salaryService.calculateAndSaveSalary(1, '2023-10');

      // 5,000,000 - 150,000 = 4,850,000
      expect(salaryModel.update).toHaveBeenCalledWith(99, 4850000, 150000, 50000, 100000);
    });
  });
});
