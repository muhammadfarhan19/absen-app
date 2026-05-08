const jwt = require('jsonwebtoken');
const { sendError } = require('../utils/responseFormatter');

const authMiddleware = (req, res, next) => {
  const authHeader = req.headers.authorization;

  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return sendError(res, 'Akses ditolak. Token tidak ditemukan.', 401);
  }

  const token = authHeader.split(' ')[1];

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    req.user = decoded; // Contains id, role, dll
    next();
  } catch (error) {
    return sendError(res, 'Token tidak valid atau sudah kedaluwarsa.', 401);
  }
};

module.exports = authMiddleware;
