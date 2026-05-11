const mongoose = require('mongoose');

const donorLocationSchema = new mongoose.Schema({
  user: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, unique: true },
  location: {
    type: { type: String, enum: ['Point'], required: true },
    coordinates: { type: [Number], required: true } // [longitude, latitude]
  },
  lastUpdated: { type: Date, default: Date.now }
});

donorLocationSchema.index({ location: '2dsphere' });

module.exports = mongoose.model('DonorLocation', donorLocationSchema);
