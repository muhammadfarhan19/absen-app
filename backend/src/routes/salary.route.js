const express = require('express');
const router = express.Router();
const salaryController = require('../controllers/salary.controller');
const authMiddleware = require('../middlewares/authMiddleware');
const roleMiddleware = require('../middlewares/roleMiddleware');

router.use(authMiddleware);

router.get('/me', salaryController.getMySalary);

// Admin only
router.get('/', roleMiddleware('admin'), salaryController.getAllSalary);
router.post('/calculate', roleMiddleware('admin'), salaryController.calculateSalary);

module.exports = router;
