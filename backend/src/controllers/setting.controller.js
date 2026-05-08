const settingModel = require('../models/setting.model');
const { sendSuccess, sendError } = require('../utils/responseFormatter');

const getAllSettings = async (req, res) => {
  try {
    const settings = await settingModel.getAll();
    sendSuccess(res, 'Settings berhasil diambil', settings);
  } catch (error) {
    sendError(res, error.message);
  }
};

const updateSetting = async (req, res) => {
  try {
    const { key, value } = req.body;
    if (!key || value === undefined) {
      return sendError(res, 'Key dan Value wajib diisi', 400);
    }
    await settingModel.update(key, value);
    sendSuccess(res, `Setting ${key} berhasil diperbarui`);
  } catch (error) {
    sendError(res, error.message);
  }
};

module.exports = {
  getAllSettings,
  updateSetting
};
