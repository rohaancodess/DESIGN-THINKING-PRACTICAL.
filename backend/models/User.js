const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
  name: { type: String, required: true },
  email: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  bloodGroup: { type: String, default: 'Unknown' },
  city: { type: String, default: 'Unknown' },
  donationCount: { type: Number, default: 0 },
  verified: { type: Boolean, default: false },
  role: { type: String, enum: ['user', 'admin'], default: 'user' },
  healthStatus: { type: String, default: 'Healthy' },
  donorLevel: { type: String, default: 'Bronze' },
  xp: { type: Number, default: 0 },
  availability: { type: Boolean, default: true },
  fcmToken: { type: String }
}, { timestamps: true });

module.exports = mongoose.model('User', userSchema);
