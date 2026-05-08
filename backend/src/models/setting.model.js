const db = require('../config/db');

const getAll = async () => {
  const [rows] = await db.execute('SELECT * FROM settings');
  return rows;
};

const getByKey = async (key) => {
  const [rows] = await db.execute('SELECT setting_value FROM settings WHERE setting_key = ?', [key]);
  return rows[0] ? rows[0].setting_value : null;
};

const update = async (key, value) => {
  await db.execute('UPDATE settings SET setting_value = ? WHERE setting_key = ?', [value, key]);
};

module.exports = {
  getAll,
  getByKey,
  update
};
