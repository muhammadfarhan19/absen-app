const attendanceService = require('../../src/services/attendance.service');
const attendanceModel = require('../../src/models/attendance.model');
const settingModel = require('../../src/models/setting.model');

jest.mock('../../src/models/attendance.model');
jest.mock('../../src/models/setting.model');

describe('Attendance Service', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('checkIn', () => {
    test('should record attendance as "hadir" if before max_late_time', async () => {
      attendanceModel.checkTodayAttendance.mockResolvedValue(null);
      settingModel.getByKey.mockResolvedValue('08:00:00');
      attendanceModel.createCheckIn.mockResolvedValue(1);

      // Mock current time to be 07:00:00
      const RealDate = Date;
      global.Date = class extends RealDate {
        constructor() { return new RealDate('2023-10-10T07:00:00'); }
        toISOString() { return '2023-10-10T07:00:00.000Z'; }
        toTimeString() { return '07:00:00 GMT+0700'; }
      };

      const result = await attendanceService.checkIn(1);
      expect(result.status).toBe('hadir');
      
      global.Date = RealDate;
    });

    test('should record attendance as "terlambat" if after max_late_time', async () => {
      attendanceModel.checkTodayAttendance.mockResolvedValue(null);
      settingModel.getByKey.mockResolvedValue('08:00:00');
      
      const RealDate = Date;
      global.Date = class extends RealDate {
        constructor() { return new RealDate('2023-10-10T09:00:00'); }
        toISOString() { return '2023-10-10T09:00:00.000Z'; }
        toTimeString() { return '09:00:00 GMT+0700'; }
      };

      const result = await attendanceService.checkIn(1);
      expect(result.status).toBe('terlambat');
      
      global.Date = RealDate;
    });

    test('should throw error if already checked in', async () => {
      attendanceModel.checkTodayAttendance.mockResolvedValue({ id: 1 });
      await expect(attendanceService.checkIn(1)).rejects.toThrow('Anda sudah melakukan absen masuk hari ini');
    });
  });
});
