const seedUsers = require('./users.seeder');
const seedEmployees = require('./employees.seeder');
const seedSettings = require('./settings.seeder');

const runSeeders = async () => {
  try {
    await seedUsers();
    await seedEmployees();
    await seedSettings();
    process.exit(0);
  } catch (error) {
    console.error('Seeder failed:', error);
    process.exit(1);
  }
};

runSeeders();
