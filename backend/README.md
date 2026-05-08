# 🚀 Backend: Aplikasi Penggajian & Absensi Karyawan

Dokumentasi Backend API untuk Sistem Absensi dan Penggajian Karyawan. Dibangun dengan Node.js, Express, dan MySQL mengikuti arsitektur **Model-Service-Controller-Route**.

---

## 🛠️ Tech Stack
* **Runtime:** Node.js
* **Framework:** Express.js
* **Database:** MySQL
* **Authentication:** JWT (JSON Web Token)
* **Testing:** Jest & Supertest
* **Security:** Bcrypt (Password Hashing)

---

## 📂 Folder Structure
```
backend/
├── migrations/          # SQL migration files
├── seeders/             # Initial data seeds
├── src/
│   ├── config/          # DB connection & Constants
│   ├── controllers/     # Request/Response handlers
│   ├── middlewares/     # Auth & Role authorization
│   ├── models/          # Raw SQL queries
│   ├── routes/          # API Endpoints mapping
│   ├── services/        # Business Logic
│   ├── utils/           # Helpers (Response formatter)
│   └── app.js           # Entry point
├── tests/               # Unit testing files
└── .env                 # Environment variables
```

---

## ⚙️ Setup & Installation

### 1. Prasyarat
* Node.js installed
* MySQL server running

### 2. Instalasi Dependensi
```bash
cd backend
npm install
```

### 3. Konfigurasi Environment
Buat file `.env` di folder root `backend/` dan sesuaikan:
```env
PORT=3000
DB_HOST=localhost
DB_USER=root
DB_PASS=your_password
DB_NAME=kepegawaian_db
JWT_SECRET=your_jwt_secret
```

### 4. Database Setup (Migration & Seeding)
Jalankan perintah berikut untuk membuat tabel dan mengisi data awal:
```bash
# Menjalankan migrasi tabel
npm run migrate

# Mengisi data awal (Admin default & Settings)
npm run seed
```

---

## 🔑 Authentication
Gunakan Bearer Token pada header untuk endpoint yang diproteksi:
`Authorization: Bearer <your_jwt_token>`

**User Default (dari seeder):**
* **Admin:** `admin@mail.com` | Password: `admin123`
* **Karyawan:** `budi@mail.com` | Password: `karyawan123`

---

## 📡 API Endpoints

### 🔐 1. Auth
| Method | Endpoint | Desc | Auth |
| :--- | :--- | :--- | :--- |
| POST | `/auth/login` | Login user & get token | Public |

### 👨‍💼 2. Employees (Admin Only)
| Method | Endpoint | Desc | Auth |
| :--- | :--- | :--- | :--- |
| GET | `/employees` | Ambil semua data karyawan | Admin |
| POST | `/employees` | Tambah karyawan baru | Admin |
| PUT | `/employees/:id` | Update data karyawan | Admin |
| DELETE | `/employees/:id` | Hapus karyawan | Admin |

### ⏱️ 3. Attendance
| Method | Endpoint | Desc | Auth |
| :--- | :--- | :--- | :--- |
| POST | `/attendance/check-in` | Absen masuk | Login |
| POST | `/attendance/check-out` | Absen pulang | Login |
| GET | `/attendance/me` | Lihat riwayat absen pribadi | Login |
| GET | `/attendance` | Lihat semua absen (Filter: `name`, `date`) | Admin |

### 💰 4. Salary
| Method | Endpoint | Desc | Auth |
| :--- | :--- | :--- | :--- |
| GET | `/salary/me` | Lihat slip gaji pribadi | Login |
| GET | `/salary` | Lihat semua gaji (Filter: `name`, `month`) | Admin |
| POST | `/salary/calculate` | Hitung ulang gaji bulanan | Admin |

### ⚙️ 5. Settings (Admin Only)
| Method | Endpoint | Desc | Auth |
| :--- | :--- | :--- | :--- |
| GET | `/settings` | Lihat aturan sistem (Jam masuk, Potongan) | Admin |
| PUT | `/settings` | Update aturan sistem | Admin |

---

## 🧪 Testing
Jalankan unit test untuk memastikan logika bisnis berjalan benar:
```bash
npm run test
```

---

## 📜 Business Logic Rules
1. **Late Rules:** Jam masuk default adalah **08:00:00**. Check-in setelah jam tersebut otomatis berstatus `terlambat`.
2. **Deduction:** Setiap keterlambatan akan memotong gaji sebesar **Rp 50.000** (Dapat diubah di menu Settings).
3. **Attendance Limit:** 1x Check-in dan 1x Check-out per hari.
4. **Salary Formula:** `Total Gaji = Gaji Pokok - (Jumlah Terlambat * Potongan)`.

---
**Author:** Antigravity AI Implementation.
