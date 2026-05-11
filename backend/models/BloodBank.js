const mongoose = require('mongoose');

const bloodBankSchema = new mongoose.Schema({
  name: { type: String, required: true },
  address: { type: String, required: true },
  phone: { type: String, required: true },
  location: {
    lat: { type: Number, required: true },
    lng: { type: Number, required: true }
  },
  inventory: [{
    bloodGroup: { type: String, required: true },
    units: { type: Number, default: 0 }
  }]
}, { timestamps: true });

module.exports = mongoose.model('BloodBank', bloodBankSchema);
