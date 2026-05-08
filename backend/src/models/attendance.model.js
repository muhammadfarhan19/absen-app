const db = require('../config/db');

const checkTodayAttendance = async (user_id, date) => {
  const [rows] = await db.execute(
    'SELECT * FROM attendance WHERE user_id = ? AND date = ?',
    [user_id, date]
  );
  return rows[0];
};

const createCheckIn = async (attendanceData) => {
  const { user_id, date, check_in, status } = attendanceData;
  const [result] = await db.execute(
    'INSERT INTO attendance (user_id, date, check_in, status) VALUES (?, ?, ?, ?)',
    [user_id, date, check_in, status]
  );
  return result.insertId;
};

const updateCheckOut = async (id, check_out) => {
  await db.execute(
    'UPDATE attendance SET check_out = ? WHERE id = ?',
    [check_out, id]
  );
};

const findByUserId = async (user_id) => {
  const [rows] = await db.execute(
    'SELECT * FROM attendance WHERE user_id = ? ORDER BY date DESC',
    [user_id]
  );
  return rows;
};

const findAll = async (filters = {}) => {
  let query = `
    SELECT a.*, u.name 
    FROM attendance a 
    JOIN users u ON a.user_id = u.id 
  `;
  const params = [];

  const conditions = [];
  if (filters.date) {
    conditions.push('a.date = ?');
    params.push(filters.date);
  }
  if (filters.user_id) {
    conditions.push('a.user_id = ?');
    params.push(filters.user_id);
  }
  if (filters.name) {
    conditions.push('u.name LIKE ?');
    params.push(`%${filters.name}%`);
  }

  if (conditions.length > 0) {
    query += ' WHERE ' + conditions.join(' AND ');
  }

  query += ' ORDER BY a.date DESC';
  
  const [rows] = await db.execute(query, params);
  return rows;
};

const countLateInMonth = async (user_id, start_date, end_date) => {
  const [rows] = await db.execute(`
    SELECT COUNT(*) as late_count 
    FROM attendance 
    WHERE user_id = ? AND status = 'terlambat' AND date >= ? AND date <= ?
  `, [user_id, start_date, end_date]);
  return rows[0].late_count;
};

module.exports = {
  checkTodayAttendance,
  createCheckIn,
  updateCheckOut,
  findByUserId,
  findAll,
  countLateInMonth
};
