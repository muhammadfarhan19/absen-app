const db = require('../config/db');

const checkExistingSalary = async (user_id, month) => {
  const [rows] = await db.execute(
    'SELECT * FROM salary WHERE user_id = ? AND month = ?',
    [user_id, month]
  );
  return rows[0];
};

const create = async (salaryData) => {
  const { user_id, month, total_gaji, total_potongan } = salaryData;
  const [result] = await db.execute(
    'INSERT INTO salary (user_id, month, total_gaji, total_potongan) VALUES (?, ?, ?, ?)',
    [user_id, month, total_gaji, total_potongan]
  );
  return result.insertId;
};

const update = async (id, total_gaji, total_potongan) => {
  await db.execute(
    'UPDATE salary SET total_gaji = ?, total_potongan = ? WHERE id = ?',
    [total_gaji, total_potongan, id]
  );
};

const findByUserId = async (user_id) => {
  const [rows] = await db.execute(
    'SELECT * FROM salary WHERE user_id = ? ORDER BY month DESC',
    [user_id]
  );
  return rows;
};

const findAll = async (filters = {}) => {
  let query = `
    SELECT s.*, u.name 
    FROM salary s 
    JOIN users u ON s.user_id = u.id 
  `;
  const params = [];
  const conditions = [];

  if (filters.month) {
    conditions.push('s.month = ?');
    params.push(filters.month);
  }
  if (filters.user_id) {
    conditions.push('s.user_id = ?');
    params.push(filters.user_id);
  }
  if (filters.name) {
    conditions.push('u.name LIKE ?');
    params.push(`%${filters.name}%`);
  }

  if (conditions.length > 0) {
    query += ' WHERE ' + conditions.join(' AND ');
  }

  query += ' ORDER BY s.month DESC';

  const [rows] = await db.execute(query, params);
  return rows;
};

module.exports = {
  checkExistingSalary,
  create,
  update,
  findByUserId,
  findAll
};
