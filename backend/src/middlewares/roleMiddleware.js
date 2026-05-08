const { sendError } = require('../utils/responseFormatter');

const roleMiddleware = (requiredRole) => {
  return (req, res, next) => {
    if (!req.user || req.user.role !== requiredRole) {
      return sendError(res, `Akses ditolak. Hanya ${requiredRole} yang diizinkan.`, 403);
    }
    next();
  };
};

module.exports = roleMiddleware;
