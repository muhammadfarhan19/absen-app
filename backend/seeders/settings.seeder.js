const db = require('../src/config/db');

const seedSettings = async () => {
  const settings = [
    ['max_late_time', '08:00:00', 'Jam batas masuk (HH:mm:ss)'],
    ['potongan_terlambat', '50000', 'Potongan gaji per keterlambatan (Rp)'],
    ['bpjs_kesehatan_rate', '0.01', 'Persentase potongan BPJS Kesehatan karyawan (0.01 = 1%)'],
    ['bpjs_ketenagakerjaan_rate', '0.02', 'Persentase potongan BPJS Ketenagakerjaan karyawan (0.02 = 2%)']
  ];

  for (const setting of settings) {
    await db.execute(
      'INSERT IGNORE INTO settings (setting_key, setting_value, description) VALUES (?, ?, ?)',
      setting
    );
  }

  console.log('Settings seeded successfully');
};

module.exports = seedSettings;
