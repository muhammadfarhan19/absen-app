const fs = require('fs');
const path = require('path');
const db = require('../src/config/db');

const runMigrations = async () => {
  const migrationsDir = path.join(__dirname);
  const files = fs.readdirSync(migrationsDir).filter(f => f.endsWith('.sql')).sort();

  for (const file of files) {
    console.log(`Running migration: ${file}`);
    const sql = fs.readFileSync(path.join(migrationsDir, file), 'utf8');
    const queries = sql.split(';').filter(q => q.trim() !== '');

    for (const query of queries) {
      if (query.includes('-- UP')) {
        const upQuery = query.split('-- DOWN')[0].replace('-- UP', '').trim();
        if (upQuery) await db.execute(upQuery);
      }
    }
  }

  console.log('Migrations completed successfully');
  process.exit(0);
};

runMigrations().catch(err => {
  console.error('Migration failed:', err);
  process.exit(1);
});
