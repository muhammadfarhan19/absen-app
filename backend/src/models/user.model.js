const db = require('../config/db');

const findByEmail = async (email) => {
  const [rows] = await db.execute('SELECT * FROM users WHERE email = ?', [email]);
  return rows[0];
};

const create = async (userData) => {
  const { name, email, password, role } = userData;
  const [result] = await db.execute(
    'INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)',
    [name, email, password, role || 'karyawan']
  );
  return result.insertId;
};

const findById = async (id) => {
  const [rows] = await db.execute('SELECT id, name, email, role FROM users WHERE id = ?', [id]);
  return rows[0];
};

const update = async (id, userData) => {
  const { name, email, role } = userData;
  await db.execute('UPDATE users SET name = ?, email = ?, role = ? WHERE id = ?', [name, email, role, id]);
};

const deleteUser = async (id) => {
  await db.execute('DELETE FROM users WHERE id = ?', [id]);
};

module.exports = {
  findByEmail,
  create,
  findById,
  update,
  deleteUser,
};
