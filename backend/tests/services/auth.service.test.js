const authService = require('../../src/services/auth.service');
const userModel = require('../../src/models/user.model');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

jest.mock('../../src/models/user.model');
jest.mock('bcrypt');
jest.mock('jsonwebtoken');

describe('Auth Service', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('login should return user and token on success', async () => {
    const mockUser = { id: 1, name: 'Admin', email: 'admin@mail.com', password: 'hashedpassword', role: 'admin' };
    userModel.findByEmail.mockResolvedValue(mockUser);
    bcrypt.compare.mockResolvedValue(true);
    jwt.sign.mockReturnValue('mocktoken');

    const result = await authService.login('admin@mail.com', 'admin123');

    expect(result.user.email).toBe('admin@mail.com');
    expect(result.token).toBe('mocktoken');
  });

  test('login should throw error on invalid credentials', async () => {
    userModel.findByEmail.mockResolvedValue(null);

    await expect(authService.login('wrong@mail.com', 'any')).rejects.toThrow('Email atau password salah');
  });
});
