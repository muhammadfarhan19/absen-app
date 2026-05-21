# Changelog - Backend Aplikasi Penggajian & Absensi

Dokumen ini mendokumentasikan rincian perubahan (changelog) pada repositori backend.

---

## [1.1.0] - 2026-05-21

### ➕ Added (Ditambahkan)
* **File Migrasi Database Baru**: `006_add_bpjs_to_salary.sql` untuk menambahkan kolom `bpjs_kesehatan` dan `bpjs_ketenagakerjaan` bertipe `DECIMAL(15, 2)` pada tabel `salary`.
* **Konfigurasi Global BPJS**: Menambahkan parameter konfigurasi global pada tabel `settings` melalui seeder (`settings.seeder.js`):
  * `bpjs_kesehatan_rate` = `0.01` (1% iuran pekerja untuk BPJS Kesehatan).
  * `bpjs_ketenagakerjaan_rate` = `0.02` (2% iuran pekerja untuk BPJS Ketenagakerjaan JHT).

### 🔄 Changed (Diubah)
* **`src/models/salary.model.js`**:
  * Memperbarui query `create` untuk menyisipkan nilai kolom `bpjs_kesehatan` dan `bpjs_ketenagakerjaan`.
  * Memperbarui query `update` untuk mengubah rincian potongan BPJS pada slip gaji yang ada.
* **`src/services/salary.service.js`**:
  * Mengintegrasikan pembacaan nilai rate iuran BPJS dari database (`settings` table).
  * Menghitung besaran potongan BPJS Kesehatan ($1\% \times \text{Gaji Pokok}$) dan BPJS Ketenagakerjaan ($2\% \times \text{Gaji Pokok}$) secara otomatis.
  * Menambahkan total potongan BPJS ke dalam akumulasi `total_potongan` dan mengurangi total gaji bersih (`total_gaji`) yang diterima karyawan.
  * Mengembalikan rincian breakdown potongan BPJS Kesehatan & Ketenagakerjaan pada respons API kalkulasi gaji.
* **`tests/services/salary.service.test.js`**:
  * Memperbarui test suite untuk menguji akurasi kalkulasi denda keterlambatan digabung dengan potongan otomatis BPJS menggunakan Jest Mocking.
  * Memperbarui asersi nilai expected output agar sesuai dengan logika perhitungan potongan yang baru.

### 🧹 Removed (Dihapus)
* Tidak ada penghapusan fitur atau file pada versi ini.

---

*Catatan: Semua perubahan telah lulus uji fungsionalitas melalui unit testing Express Service Layer.*
