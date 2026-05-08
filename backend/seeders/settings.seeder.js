const db = require('../src/config/db');

const seedSettings = async () => {
  const settings = [
    ['max_late_time', '08:00:00', 'Jam batas masuk (HH:mm:ss)'],
    ['potongan_terlambat', '50000', 'Potongan gaji per keterlambatan (Rp)']
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
