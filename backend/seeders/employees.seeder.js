const db = require('../src/config/db');

const seedEmployees = async () => {
  // Get karyawan users
  const [karyawan] = await db.execute("SELECT id FROM users WHERE role = 'karyawan'");

  const employeeData = [
    [karyawan[0].id, 5000000],
    [karyawan[1].id, 4500000]
  ];

  for (const emp of employeeData) {
    await db.execute(
      'INSERT IGNORE INTO employees (user_id, gaji_pokok) VALUES (?, ?)',
      emp
    );
  }

  console.log('Employees seeded successfully');
};

module.exports = seedEmployees;
