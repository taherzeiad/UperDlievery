const express = require('express');
const app = express();
app.use(express.json());

const port = 3000;

// --- Auth Endpoints ---
app.post('/auth/signup', (req, res) => {
    res.json({ status: "success", token: "mock_jwt_token_123", message: "User registered" });
});

app.post('/auth/request-otp', (req, res) => {
    res.json({ status: "success", message: "OTP sent to " + req.body.phone });
});

app.post('/auth/verify-otp', (req, res) => {
    res.json({ status: "success", token: "mock_jwt_token_123", message: "Verified" });
});

// --- Driver Endpoints ---
app.get('/driver/profile', (req, res) => {
    res.json({
        id: "drv_001",
        name: "Taher Al-Sayer",
        level: "Gold Member",
        totalEarned: 1250.50,
        hoursOnline: 45.2,
        totalDistanceKm: 120.0,
        totalJobs: 54,
        currentLat: 60.1699,
        currentLng: 24.9384,
        currencySymbol: "$"
    });
});

app.get('/driver/history', (req, res) => {
    res.json([
        { id: "1", riderName: "Ali", date: "2026-08-12", price: 25.0, distanceKm: 5.2, pickupAddress: "Point A", dropoffAddress: "Point B", paymentTags: ["CASH"] }
    ]);
});

// --- Rides Endpoints ---
app.get('/rides/incoming', (req, res) => {
    res.json([
        {
            id: "ride_101",
            riderName: "Sarah Ahmed",
            price: 45.0,
            distanceKm: 3.5,
            pickupAddress: "Main Street, Tower 5",
            pickupLat: 60.1719,
            pickupLng: 24.9350,
            dropoffAddress: "City Mall",
            dropoffLat: 60.1750,
            dropoffLng: 24.9410,
            tags: ["CARD", "DISCOUNT"]
        }
    ]);
});

app.listen(port, () => {
    console.log(`Mock Server running at http://localhost:${port}`);
});
