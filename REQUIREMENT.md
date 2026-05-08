# 📱 Project Requirements: Aplikasi Penggajian & Absensi Karyawan

## Table of Contents
1. [Project Summary](#project-summary)
2. [Tujuan Aplikasi (Objectives)](#tujuan-aplikasi-objectives)
3. [User & Role](#user--role)
4. [Fitur Utama Sistem (Main Features)](#fitur-utama-sistem-main-features)
5. [Business Logic (Aturan Sistem)](#business-logic-aturan-sistem)
6. [Kebutuhan Data (Database Schema)](#kebutuhan-data-database-schema)
7. [Kebutuhan Sistem (Technical Requirements)](#kebutuhan-sistem-technical-requirements)
8. [Output / Deliverables](#output--deliverables)
9. [Estimasi Pengerjaan (Timeline)](#estimasi-pengerjaan-timeline)
10. [Batasan Sistem & Future Scope](#batasan-sistem--future-scope)

---

## 1. Project Summary
**Aplikasi Penggajian & Absensi Karyawan** adalah sistem digital berbasis Android yang dirancang untuk mengelola kehadiran dan perhitungan gaji karyawan secara otomatis berdasarkan data absensi harian.

## 2. Tujuan Aplikasi (Objectives)
* **Digitalisasi Absensi:** Mengelola data kehadiran karyawan secara terpusat dan digital.
* **Automasi Penggajian:** Menghitung gaji secara otomatis berdasarkan kehadiran dan potongan keterlambatan.
* **Efisiensi Admin:** Mengurangi proses rekapitulasi data secara manual.
* **Transparansi:** Menyediakan akses langsung bagi karyawan untuk melihat riwayat kehadiran, gaji, dan potongan.

> **Intinya:** Mengubah proses absensi & penggajian dari manual menjadi otomatis dan terstruktur.

## 3. User & Role

### 👨‍💼 1. Admin
Memiliki kontrol penuh terhadap sistem.
* **Hak Akses:**
  * Mengelola data karyawan (CRUD).
  * Melihat seluruh data absensi.
  * Mengatur aturan gaji pokok & potongan.
  * Melihat dan mencetak laporan penggajian.
  * Memonitor keterlambatan karyawan.

### 👥 2. Karyawan
Pengguna akhir aplikasi untuk aktivitas harian.
* **Hak Akses:**
  * Melakukan absensi masuk (Check-in) dan pulang (Check-out).
  * Melihat riwayat kehadiran pribadi.
  * Melihat slip gaji dan detail potongan.

## 4. Fitur Utama Sistem (Main Features)

### 🔐 1. Authentication
* Login Admin & Karyawan.
* Role-based access control.

### 👨‍💼 2. Manajemen Karyawan (Admin Only)
* Tambah karyawan baru.
* Edit data karyawan (nama, email, jabatan, dll).
* Hapus / Nonaktifkan karyawan.
* Set gaji pokok per karyawan.

### ⏱️ 3. Absensi
* **Karyawan:**
  * Check-in (absen masuk).
  * Check-out (absen pulang).
* **Sistem:**
  * Mencatat waktu secara otomatis (server time).
  * Menentukan status: `Hadir` atau `Terlambat`.

### 📊 4. Monitoring Absensi (Admin Only)
* Melihat semua data absensi karyawan.
* Filter data per tanggal / nama karyawan.
* Rekapitulasi keterlambatan.

### 💰 5. Sistem Penggajian Otomatis
* **Komponen:**
  * Gaji Pokok.
  * Potongan Keterlambatan.
* **Mekanisme:**
  * Setiap keterlambatan memicu potongan otomatis.
  * Total gaji dihitung otomatis oleh sistem pada akhir periode.

### 📄 6. Laporan & Slip Gaji
* **Admin:** Laporan gaji seluruh karyawan & rekap absensi.
* **Karyawan:** Slip gaji pribadi bulanan & riwayat potongan.

## 5. Business Logic (Aturan Sistem)

### 🕐 Aturan Absensi
* **Limitasi:** 1 hari hanya diperbolehkan maksimal **1x Check-in** dan **1x Check-out**.
* **Validasi:** Check-out harus dilakukan setelah Check-in.

### ⏰ Aturan Keterlambatan
* Waktu batas masuk ditentukan sistem (misal: > 08:00 WIB).
* Jika waktu Check-in melebihi batas waktu:
  * Status absensi menjadi **"Terlambat"**.
  * Dikenakan potongan keterlambatan.

### 🧮 Perhitungan Gaji
```text
Total Gaji = Gaji Pokok − (Jumlah Terlambat × Nominal Potongan)
```
* **Gaji Pokok:** Ditentukan oleh Admin per karyawan.
* **Jumlah Terlambat:** Dihitung dari akumulasi status "Terlambat" pada periode tersebut.
* **Nominal Potongan:** Nilai tetap yang ditentukan oleh Admin (misal: Rp 50.000 / keterlambatan).

## 6. Kebutuhan Data (Database Schema)

| Tabel | Kolom / Atribut | Deskripsi |
| :--- | :--- | :--- |
| **1. Users** | `id`, `name`, `email`, `password`, `role` | Menyimpan data autentikasi (role: admin/karyawan). |
| **2. Employees** | `id`, `user_id`, `gaji_pokok` | Menyimpan data spesifik karyawan terkait gaji. |
| **3. Attendance** | `id`, `user_id`, `tanggal`, `check_in`, `check_out`, `status` | Menyimpan riwayat absen masuk, pulang, dan status (hadir/terlambat). |
| **4. Salary** | `id`, `user_id`, `bulan`, `total_gaji`, `total_potongan` | Menyimpan rekapitulasi gaji bulanan tiap karyawan. |

## 7. Kebutuhan Sistem (Technical Requirements)

### 📱 Frontend (Mobile App)
* **Platform:** Android
* **Bahasa Pemrograman:** Kotlin
* **Fokus:** UI/UX yang responsif, Konsumsi REST API

### ⚙️ Backend
* **Environment:** Node.js
* **Framework:** Express.js
* **Arsitektur:** REST API
* **Authentication:** JWT (JSON Web Token)

### 🗄️ Database
* **Sistem:** MySQL

## 8. Output / Deliverables
1. Aplikasi Android yang siap digunakan (APK).
2. Sistem absensi & penggajian otomatis berjalan lancar.
3. Dashboard fungsional untuk Admin & Karyawan.
4. Database MySQL yang terintegrasi.
5. Dokumentasi penggunaan singkat (Opsional).

## 9. Estimasi Pengerjaan (Timeline)
**Total Estimasi: 2 Minggu (MVP)**

* **Week 1 (Dasar & Absensi):**
  * Setup Project & Database.
  * Authentication (Login).
  * CRUD Manajemen Karyawan.
  * Fitur Absensi (Check-in/Check-out).
* **Week 2 (Penggajian & Finishing):**
  * Logic Penggajian Otomatis.
  * Modul Laporan & Slip Gaji.
  * UI/UX Refinement & Testing.

## 10. Batasan Sistem & Future Scope

### ⚠️ Batasan Scope (MVP)
Untuk menjaga timeline 2 minggu, fitur berikut **tidak** disertakan:
* ❌ GPS tracking / Geofencing.
* ❌ Face recognition / Deteksi Wajah.
* ❌ Multi-branch (Banyak cabang perusahaan).
* ❌ Perhitungan Payroll kompleks (Pajak PPh 21, BPJS, Tunjangan dinamis).

### 🚀 Pengembangan Lanjutan (Future Scope)
* GPS location validation saat absensi.
* Validasi foto selfie realtime.
* Export & Download PDF Slip Gaji.
* Push Notification untuk reminder absen & keterlambatan.
* Integrasi fingerprint / biometrik di aplikasi.

---
**Kesimpulan:**
Fokus utama fase MVP ini adalah memberikan solusi digitalisasi absensi yang dapat meningkatkan **Efisiensi Admin**, memberikan **Transparansi Karyawan**, dan **Automasi Perhitungan Gaji** dasar.