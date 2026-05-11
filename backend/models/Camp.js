const mongoose = require('mongoose');

const campSchema = new mongoose.Schema({
  name: { type: String, required: true },
  organizer: { type: String, required: true },
  location: { type: String, required: true },
  date: { type: Date, required: true },
  volunteersNeeded: { type: Number, default: 5 },
  registeredDonors: [{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }]
}, { timestamps: true });

module.exports = mongoose.model('Camp', campSchema);
