const authService = require('../services/auth.service');
const { sendSuccess, sendError } = require('../utils/responseFormatter');

const login = async (req, res) => {
  try {
    const { email, password } = req.body;
    if (!email || !password) {
      return sendError(res, 'Email dan password wajib diisi', 400);
    }
    const result = await authService.login(email, password);
    sendSuccess(res, 'Login berhasil', result);
  } catch (error) {
    sendError(res, error.message, 401);
  }
};

module.exports = {
  login,
};
