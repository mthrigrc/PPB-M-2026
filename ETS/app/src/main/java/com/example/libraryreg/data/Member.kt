package com.example.libraryreg.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "members", indices = [Index(value = ["nomorInduk"], unique = true)])
data class Member(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaLengkap: String,
    val nomorInduk: String,      // NIM / NIP
    val programStudi: String,
    val nomorTelepon: String,
    val email: String,
    val alamat: String,
    val jenis: String            // Mahasiswa / Dosen / Staf
)
