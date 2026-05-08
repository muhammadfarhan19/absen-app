const bcrypt = require('bcrypt');
const db = require('../src/config/db');

const seedUsers = async () => {
  const adminPass = await bcrypt.hash('admin123', 10);
  const karyawanPass = await bcrypt.hash('karyawan123', 10);

  const users = [
    ['Admin System', 'admin@mail.com', adminPass, 'admin'],
    ['Budi Santoso', 'budi@mail.com', karyawanPass, 'karyawan'],
    ['Siti Aminah', 'siti@mail.com', karyawanPass, 'karyawan']
  ];

  for (const user of users) {
    await db.execute(
      'INSERT IGNORE INTO users (name, email, password, role) VALUES (?, ?, ?, ?)',
      user
    );
  }

  console.log('Users seeded successfully');
};

module.exports = seedUsers;
