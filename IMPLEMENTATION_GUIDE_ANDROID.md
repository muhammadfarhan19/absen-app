# 📱 Panduan Integrasi Android: Fitur Potongan BPJS Kesehatan & Ketenagakerjaan

Panduan ini dibuat khusus untuk membantu **Developer Android** mengintegrasikan fitur potongan BPJS Kesehatan dan BPJS Ketenagakerjaan otomatis yang baru saja diimplementasikan pada backend.

---

## 1. Rangkuman Perubahan pada Backend
* Kolom baru pada tabel database `salary`:
  * `bpjs_kesehatan` (Decimal/Double): Menyimpan nominal potongan BPJS Kesehatan karyawan.
  * `bpjs_ketenagakerjaan` (Decimal/Double): Menyimpan nominal potongan BPJS Ketenagakerjaan karyawan.
* Nilai potongan ini **secara otomatis dihitung oleh server** pada saat kalkulasi gaji (`POST /salary/calculate`) berdasarkan persentase iuran resmi dari gaji pokok:
  * **BPJS Kesehatan**: $1\%$ (disimpan dinamis di setting dengan key `bpjs_kesehatan_rate`).
  * **BPJS Ketenagakerjaan**: $2\%$ (disimpan dinamis di setting dengan key `bpjs_ketenagakerjaan_rate`).
* Jumlah potongan BPJS ini juga otomatis sudah diakumulasikan ke dalam field `total_potongan` dan sudah memotong `total_gaji` secara otomatis di sisi server.

---

## 2. Contoh Payload Respons API Baru
Respons JSON dari endpoint `/salary/me` dan `/salary` sekarang menyertakan properti `bpjs_kesehatan` dan `bpjs_ketenagakerjaan` secara otomatis:

```json
{
  "success": true,
  "message": "Daftar gaji berhasil diambil",
  "data": [
    {
      "id": 1,
      "user_id": 2,
      "name": "Budi",
      "month": "2026-05",
      "total_gaji": "4750000.00",
      "total_potongan": "250000.00",
      "bpjs_kesehatan": "50000.00",
      "bpjs_ketenagakerjaan": "100000.00"
    }
  ]
}
```

---

## 3. Langkah Modifikasi Kode Kotlin (Android)

### Langkah A: Update Model Response (`SalaryResponse.kt`)
Buka file Kotlin berikut:
📂 [SalaryResponse.kt](file:///home/farhan/Documents/workspaces/joki-it/absen-app/kepegawaian-android/app/src/main/java/com/app/payroll/data/remote/response/SalaryResponse.kt)

Tambahkan field `bpjsKesehatan` dan `bpjsKetenagakerjaan` menggunakan `@SerializedName` agar terpetakan dari JSON ke objek Kotlin dengan benar.

#### 🛠️ Rekomendasi Kode Baru:
```kotlin
package com.app.payroll.data.remote.response

import com.google.gson.annotations.SerializedName

data class SalaryResponse(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    val name: String?,
    val month: String,
    @SerializedName("total_gaji") val totalGaji: Double,
    @SerializedName("total_potongan") val totalPotongan: Double,
    
    // Properti Baru Potongan BPJS (Nullable untuk kompatibilitas data lama)
    @SerializedName("bpjs_kesehatan") val bpjsKesehatan: Double?,
    @SerializedName("bpjs_ketenagakerjaan") val bpjsKetenagakerjaan: Double?
)
```

---

## 4. Cara Menampilkan di Halaman Detail Slip Gaji (UI Binding)

Di bagian layout detail slip gaji, Anda dapat menampilkan informasi potongan ini secara terperinci. 

### Contoh Kode Helper Format Rupiah:
Pastikan nominal dikonversi menjadi format Rupiah Indonesia demi kenyamanan pengguna:
```kotlin
fun formatRupiah(amount: Double): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    return numberFormat.format(amount).replace(",00", "")
}
```

### Contoh Binding Data pada View Holder / Fragment:
```kotlin
// Bind Data ke TextView pada Slip Gaji / Card Item
binding.tvSalaryBasic.text = formatRupiah(item.totalGaji + item.totalPotongan) // Estimasi kasarnya
binding.tvTotalDeduction.text = formatRupiah(item.totalPotongan)
binding.tvNetSalary.text = formatRupiah(item.totalGaji)

// Bind Field Baru BPJS (Gunakan nilai default 0 jika data bernilai null/lama)
val bpjsKes = item.bpjsKesehatan ?: 0.0
val bpjsKet = item.bpjsKetenagakerjaan ?: 0.0

binding.tvBpjsKesehatan.text = formatRupiah(bpjsKes)
binding.tvBpjsKetenagakerjaan.text = formatRupiah(bpjsKet)
```

---

💡 **Tip Keamanan:** Karena kolom BPJS bernilai `nullable` (`Double?`) pada Kotlin, aplikasi Android Anda akan 100% aman (*crash-free*) meskipun memuat data slip gaji lama sebelum fitur BPJS ini diaktifkan di server.
