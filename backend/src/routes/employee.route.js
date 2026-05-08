const express = require('express');
const router = express.Router();
const employeeController = require('../controllers/employee.controller');
const authMiddleware = require('../middlewares/authMiddleware');
const roleMiddleware = require('../middlewares/roleMiddleware');

router.use(authMiddleware);
router.use(roleMiddleware('admin'));

router.get('/', employeeController.getAllEmployees);
router.post('/', employeeController.createEmployee);
router.put('/:id', employeeController.updateEmployee);
router.delete('/:id', employeeController.deleteEmployee);

module.exports = router;
