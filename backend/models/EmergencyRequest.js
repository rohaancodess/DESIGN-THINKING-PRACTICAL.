const mongoose = require('mongoose');

const emergencyRequestSchema = new mongoose.Schema({
  requester: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  patientName: { type: String, required: true },
  bloodGroup: { type: String, required: true },
  hospital: { type: String, required: true },
  unitsRequired: { type: Number, required: true },
  emergencyLevel: { type: String, enum: ['Normal', 'Urgent', 'Critical'], required: true },
  prescriptionImage: { type: String }, // URL
  contactNumber: { type: String, required: true },
  location: {
    lat: { type: Number },
    lng: { type: Number }
  },
  status: { type: String, enum: ['Active', 'Fulfilled', 'Cancelled'], default: 'Active' }
}, { timestamps: true });

module.exports = mongoose.model('EmergencyRequest', emergencyRequestSchema);
