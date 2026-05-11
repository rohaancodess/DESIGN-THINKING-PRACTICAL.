const BloodBank = require('../models/BloodBank'); // I'll need to create this model too

exports.getAllBloodBanks = async (req, res) => {
  try {
    const bloodBanks = await BloodBank.find();
    res.json(bloodBanks);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};

exports.getBloodBankById = async (req, res) => {
  try {
    const bloodBank = await BloodBank.findById(req.params.id);
    if (!bloodBank) return res.status(404).json({ message: 'Blood bank not found' });
    res.json(bloodBank);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
};
