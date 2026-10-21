package com.example.libraryreg.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    // Pencarian berdasarkan nama atau nomor induk + filter jenis (kosong = semua)
    @Query(
        """SELECT * FROM members
           WHERE (:q = '' OR namaLengkap LIKE '%' || :q || '%' OR nomorInduk LIKE '%' || :q || '%')
             AND (:jenis = '' OR jenis = :jenis)
           ORDER BY namaLengkap COLLATE NOCASE"""
    )
    fun observe(q: String, jenis: String): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    fun observeById(id: Int): Flow<Member?>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getById(id: Int): Member?

    @Query("SELECT COUNT(*) FROM members WHERE nomorInduk = :nomorInduk AND id != :excludeId")
    suspend fun countByNomorInduk(nomorInduk: String, excludeId: Int): Int

    @Insert suspend fun insert(member: Member)
    @Update suspend fun update(member: Member)
    @Delete suspend fun delete(member: Member)
}
