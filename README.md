# BloodLink AI

**“Sharing Blood. Saving lives.”**

BloodLink AI is a modern Android application built to streamline blood donation requests and connections. It features a custom email-based OTP authentication system, a deep-red emergency theme, and a robust Node.js/MongoDB backend.

## Architecture & Tech Stack

### Android App
* **Language**: Kotlin
* **UI**: Jetpack Compose (Material 3)
* **Architecture**: Clean Architecture + MVVM
* **Networking**: Retrofit, OkHttp
* **Local Storage**: EncryptedSharedPreferences (Security Crypto) for JWTs
* **Navigation**: Jetpack Compose Navigation

### Backend Server
* **Language**: Node.js
* **Framework**: Express.js
* **Database**: MongoDB (Mongoose)
* **Authentication**: JWT (JSON Web Tokens), bcrypt
* **Email Service**: Nodemailer (Gmail SMTP) for Custom OTPs

## Prerequisites

- Android Studio
- Node.js (v16+)
- MongoDB (Local instance or Atlas URI)
- A Gmail account with an App Password generated for SMTP

## Setup Instructions

### 1. Backend Setup

1. Open a terminal and navigate to the `backend` directory:
   ```bash
   cd backend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Copy `.env.example` to a new file named `.env`:
   ```bash
   cp .env.example .env
   ```
4. Update the `.env` file with your details:
   - Set `MONGO_URI` to your MongoDB connection string.
   - Set `SMTP_EMAIL` to your Gmail address.
   - Set `SMTP_APP_PASSWORD` to your generated Gmail App Password.
5. Start the server:
   ```bash
   node server.js
   ```

### 2. Android App Setup

1. Open the Android project in Android Studio.
2. In `app/src/main/java/com/designthinkingpractical/app/MainActivity.kt`, verify the `baseUrl` for Retrofit points to your backend.
   - For an Android Emulator, use `http://10.0.2.2:5000/`.
   - For a physical device on the same network, use your computer's local IP address (e.g., `http://192.168.1.x:5000/`).
3. Sync the project with Gradle files.
4. Run the app on an emulator or physical device.

## Features implemented

- [x] Deep red emergency theme and modern minimal layout
- [x] Local MongoDB user models and OTP expiry TTL indexes
- [x] Gmail SMTP based OTP dispatch
- [x] JWT based authentication flow
- [x] Retrofit networking layer with Clean Architecture
- [x] Fully composed Auth Screens (Login, OTP Verification, Home)
