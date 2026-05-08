# 📏 BACKEND CODE STYLE RULES

## Payroll & Attendance System (Node.js + Express + MySQL)

---

## 🏗️ 1. Architecture Pattern (WAJIB)

Gunakan layered architecture:

```
Model → Service → Controller → Route
```

### 🔹 Model

* Hanya berisi **query SQL**
* Tidak boleh ada business logic
* Semua query harus reusable & modular
* Gunakan connection pool (mysql2)

### 🔹 Service

* Tempat seluruh **business logic**
* Validasi logic (contoh: tidak boleh check-in 2x)
* Mengolah & menggabungkan data dari model

### 🔹 Controller

* Handle HTTP request & response
* Tidak boleh ada business logic
* Tidak boleh query langsung ke database
* Hanya:

  * ambil request
  * panggil service
  * return response

### 🔹 Route

* Mapping endpoint → controller
* Gunakan Express Router
* Tidak boleh ada logic

---

## 🗄️ 2. Database Rules (WAJIB)

### 🔹 Migration System

* SETIAP perubahan database **WAJIB pakai migration**
* Tidak boleh edit database manual di production

### Format file migration:

```
/migrations
  ├── 001_create_users_table.sql
  ├── 002_create_employees_table.sql
  ├── 003_create_attendance_table.sql
```

### Rules:

* Harus ada:

  * `UP` query (create/alter)
  * `DOWN` query (rollback)
* Gunakan versioning (001, 002, dst)

---

### 🌱 Seeder

* WAJIB ada seeder untuk:

  * admin default
  * sample karyawan

Format:

```
/seeders
  ├── users.seeder.js
  ├── employees.seeder.js
```

---

## 🧪 3. Unit Testing (WAJIB)

Setiap fitur WAJIB punya unit test.

### Tools:

* Jest / Vitest

### Coverage minimal:

* Service layer (WAJIB)
* Controller (opsional tapi disarankan)

### Contoh:

* Auth service → test login berhasil/gagal
* Attendance service → test:

  * tidak bisa check-in 2x
  * telat dihitung benar

---

## 📦 4. Naming Convention

### File Naming:

* snake_case untuk file database
* camelCase untuk JS

Contoh:

```
attendance.model.js
attendance.service.js
attendance.controller.js
attendance.route.js
```

---

### Variable & Function:

* camelCase

```
getUserById()
calculateSalary()
checkInAttendance()
```

---

### Constant:

* UPPER_CASE

```
MAX_LATE_TIME = "08:00"
```

---

## 🔐 5. Security Rules

* Password wajib di-hash (bcrypt)
* Gunakan JWT untuk auth
* Jangan expose:

  * password
  * raw query error
* Gunakan environment variables (.env)

---

## ⚙️ 6. Error Handling Standard

### Format Response:

#### Success

```
{
  "success": true,
  "message": "Success message",
  "data": {}
}
```

#### Error

```
{
  "success": false,
  "message": "Error message"
}
```

---

### Rules:

* Gunakan try-catch di service & controller
* Error harus descriptive
* Jangan return stack trace ke client

---

## 📡 7. API Rules

* Gunakan RESTful convention

Contoh:

```
GET    /employees
POST   /employees
PUT    /employees/:id
DELETE /employees/:id
```

---

## 🧹 8. Code Quality

* Gunakan async/await (NO callback hell)
* Hindari duplicate code (DRY)
* Gunakan helper di `/utils`
* Pisahkan config di `/config`

---

## 🚫 9. Forbidden Practices

DILARANG:

* Query DB di controller
* Business logic di route
* Hardcode config (URL, secret, dll)
* Tidak pakai migration
* Tidak membuat unit test

---

## 🚀 10. Definition of Done (DoD)

Fitur dianggap selesai jika:

* ✅ Endpoint berjalan
* ✅ Migration tersedia
* ✅ Seeder tersedia (jika perlu)
* ✅ Unit test ada & lulus
* ✅ Code mengikuti arsitektur
* ✅ Response sesuai standar

---

## 💡 Goal

Code harus:

* Mudah dibaca
* Mudah di-maintain
* Mudah di-scale
* Aman untuk production

---
