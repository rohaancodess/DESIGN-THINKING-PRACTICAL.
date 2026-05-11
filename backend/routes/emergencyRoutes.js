const express = require('express');
const router = express.Router();
const { protect } = require('../middleware/authMiddleware');
const { createRequest, getActiveRequests } = require('../controllers/emergencyController');

router.post('/', protect, createRequest);
router.get('/active', protect, getActiveRequests);

module.exports = router;
