const express = require('express');
const router = express.Router();
const { protect } = require('../middleware/authMiddleware');
const { getAllBloodBanks, getBloodBankById } = require('../controllers/bloodBankController');

router.get('/', protect, getAllBloodBanks);
router.get('/:id', protect, getBloodBankById);

module.exports = router;
