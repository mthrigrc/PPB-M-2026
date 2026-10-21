package com.example.libraryreg

import android.util.Patterns

data class MemberForm(
    val nama: String,
    val nomorInduk: String,
    val programStudi: String,
    val telepon: String,
    val email: String,
    val alamat: String,
    val jenis: String
)

/** Validasi form. Mengembalikan peta kunci-field -> pesan error (kosong jika valid). */
object MemberValidator {
    fun validate(f: MemberForm): Map<String, String> {
        val e = mutableMapOf<String, String>()
        if (f.nama.isBlank()) e["nama"] = "Nama lengkap wajib diisi"

        val ni = f.nomorInduk.trim()
        if (ni.isEmpty()) e["nomorInduk"] = "NIM/NIP wajib diisi"
        else if (!ni.all { it.isDigit() } || ni.length !in 8..18)
            e["nomorInduk"] = "NIM/NIP harus berupa angka 8–18 digit"

        if (f.programStudi.isBlank()) e["programStudi"] = "Program studi/unit wajib diisi"

        val tlp = f.telepon.trim()
        if (tlp.isEmpty()) e["telepon"] = "Nomor telepon wajib diisi"
        else if (!tlp.all { it.isDigit() } || tlp.length !in 10..13)
            e["telepon"] = "Nomor telepon harus 10–13 digit angka"

        val mail = f.email.trim()
        if (mail.isEmpty()) e["email"] = "Email wajib diisi"
        else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) e["email"] = "Format email tidak valid"

        if (f.alamat.isBlank()) e["alamat"] = "Alamat wajib diisi"
        return e
    }
}
