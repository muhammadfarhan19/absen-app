const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const userModel = require('../models/user.model');

const login = async (email, password) => {
  const user = await userModel.findByEmail(email);
  if (!user) {
    throw new Error('Email atau password salah');
  }

  const isPasswordValid = await bcrypt.compare(password, user.password);
  if (!isPasswordValid) {
    throw new Error('Email atau password salah');
  }

  const token = jwt.sign(
    { id: user.id, role: user.role, name: user.name },
    process.env.JWT_SECRET,
    { expiresIn: '24h' }
  );

  return {
    user: {
      id: user.id,
      name: user.name,
      email: user.email,
      role: user.role,
    },
    token,
  };
};

module.exports = {
  login,
};
