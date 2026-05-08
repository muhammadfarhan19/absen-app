const express = require('express');
const router = express.Router();
const settingController = require('../controllers/setting.controller');
const authMiddleware = require('../middlewares/authMiddleware');
const roleMiddleware = require('../middlewares/roleMiddleware');

router.use(authMiddleware);
router.use(roleMiddleware('admin'));

router.get('/', settingController.getAllSettings);
router.put('/', settingController.updateSetting);

module.exports = router;
