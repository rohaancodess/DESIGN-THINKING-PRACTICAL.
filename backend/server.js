require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const helmet = require('helmet');
const rateLimit = require('express-rate-limit');
const http = require('http');
const socketIo = require('socket.io');

const authRoutes = require('./routes/authRoutes');
const userRoutes = require('./routes/userRoutes');
const emergencyRoutes = require('./routes/emergencyRoutes');
const bloodBankRoutes = require('./routes/bloodBankRoutes');


const app = express();
const server = http.createServer(app);
const io = socketIo(server, { cors: { origin: "*" } });

// Security & Middleware
app.use(helmet());
app.use(cors());
app.use(express.json());

const apiLimiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 100 // limit each IP to 100 requests per windowMs
});
app.use('/api/', apiLimiter);

// Routes
app.use('/api/auth', authRoutes);
app.use('/api/user', userRoutes);
app.use('/api/emergency', emergencyRoutes);
app.use('/api/bloodbank', bloodBankRoutes);


// Socket.io for Real-time chat & location
io.on('connection', (socket) => {
  console.log('New client connected: ' + socket.id);
  
  socket.on('join_chat', (chatId) => {
    socket.join(chatId);
  });

  socket.on('send_message', (data) => {
    io.to(data.chatId).emit('receive_message', data);
  });
  
  socket.on('broadcast_emergency', (data) => {
    // In production, use geo-redis to find users within 10km and emit only to them
    console.log('Emergency Broadcast received:', data);
    socket.broadcast.emit('emergency_alert', data);
  });
  
  socket.on('sync_location', (data) => {
    // Update DonorLocation model in production
    // Broadcast to relevant hospitals/tracking screens
    socket.broadcast.emit('location_update', data);
  });

  socket.on('disconnect', () => {
    console.log('Client disconnected: ' + socket.id);
  });
});

const PORT = process.env.PORT || 5000;
const { MongoMemoryServer } = require('mongodb-memory-server');

const startServer = async () => {
  let mongoUri = process.env.MONGO_URI;
  
  try {
    // Try Atlas first
    await mongoose.connect(mongoUri);
    console.log('Connected to MongoDB Atlas');
  } catch (err) {
    console.log('Atlas connection failed, starting in-memory MongoDB...');
    const mongod = await MongoMemoryServer.create();
    mongoUri = mongod.getUri();
    await mongoose.connect(mongoUri);
    console.log('Connected to In-Memory MongoDB at:', mongoUri);
  }

  server.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
  });
};

startServer();
