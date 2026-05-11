const EmergencyRequest = require('../models/EmergencyRequest');

exports.createRequest = async (req, res) => {
  try {
    const newReq = await EmergencyRequest.create({ ...req.body, requester: req.user.id });
    res.status(201).json(newReq);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

exports.getActiveRequests = async (req, res) => {
  try {
    const requests = await EmergencyRequest.find({ status: 'Active' }).populate('requester', 'name bloodGroup phone');
    res.json(requests);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
