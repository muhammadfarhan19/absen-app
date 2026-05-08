const db = require('../config/db');

const findAll = async () => {
  const [rows] = await db.execute(`
    SELECT e.id, e.user_id, u.name, u.email, u.role, e.gaji_pokok 
    FROM employees e 
    JOIN users u ON e.user_id = u.id
  `);
  return rows;
};

const findById = async (id) => {
  const [rows] = await db.execute(`
    SELECT e.id, e.user_id, u.name, u.email, u.role, e.gaji_pokok 
    FROM employees e 
    JOIN users u ON e.user_id = u.id 
    WHERE e.id = ?
  `, [id]);
  return rows[0];
};

const findByUserId = async (user_id) => {
  const [rows] = await db.execute(`
    SELECT e.id, e.user_id, u.name, u.email, u.role, e.gaji_pokok 
    FROM employees e 
    JOIN users u ON e.user_id = u.id 
    WHERE e.user_id = ?
  `, [user_id]);
  return rows[0];
};

const create = async (employeeData) => {
  const { user_id, gaji_pokok } = employeeData;
  const [result] = await db.execute(
    'INSERT INTO employees (user_id, gaji_pokok) VALUES (?, ?)',
    [user_id, gaji_pokok]
  );
  return result.insertId;
};

const update = async (id, employeeData) => {
  const { gaji_pokok } = employeeData;
  await db.execute('UPDATE employees SET gaji_pokok = ? WHERE id = ?', [gaji_pokok, id]);
};

const deleteEmployee = async (id) => {
  await db.execute('DELETE FROM employees WHERE id = ?', [id]);
};

module.exports = {
  findAll,
  findById,
  findByUserId,
  create,
  update,
  deleteEmployee
};
