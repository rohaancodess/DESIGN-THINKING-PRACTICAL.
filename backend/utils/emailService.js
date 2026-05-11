const nodemailer = require('nodemailer');

const transporter = nodemailer.createTransport({
  service: "gmail",
  auth: {
    user: process.env.SMTP_EMAIL,
    pass: process.env.SMTP_APP_PASSWORD
  }
});

const sendOtpEmail = async (to, otp) => {
  const mailOptions = {
    from: `"BloodLink AI" <${process.env.SMTP_EMAIL}>`,
    to: to,
    subject: "BloodLink AI - Your Verification Code",
    html: `
      <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
        <h2 style="color: #D32F2F; text-align: center;">BloodLink AI</h2>
        <p style="font-size: 16px; color: #333;">Hello,</p>
        <p style="font-size: 16px; color: #333;">Your One-Time Password (OTP) for verifying your email is:</p>
        <div style="background-color: #f8d7da; padding: 15px; text-align: center; border-radius: 5px; margin: 20px 0;">
          <h1 style="color: #721c24; margin: 0; letter-spacing: 5px;">${otp}</h1>
        </div>
        <p style="font-size: 14px; color: #666;">This code is valid for 5 minutes. Do not share it with anyone.</p>
        <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">
        <p style="font-size: 12px; color: #999; text-align: center;">"Sharing Blood. Saving lives."</p>
      </div>
    `
  };

  await transporter.sendMail(mailOptions);
};

module.exports = { sendOtpEmail };
