package com.example.libraryreg

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryreg.data.AppDatabase
import com.example.libraryreg.data.Member
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MemberViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).memberDao()

    val query = MutableStateFlow("")   // teks pencarian
    val filter = MutableStateFlow("")  // "" = semua jenis

    @OptIn(ExperimentalCoroutinesApi::class)
    val members: StateFlow<List<Member>> = combine(query, filter) { q, f -> q.trim() to f }
        .flatMapLatest { (q, f) -> dao.observe(q, f) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun observe(id: Int) = dao.observeById(id)
    suspend fun load(id: Int) = dao.getById(id)
    suspend fun isDuplicate(nomorInduk: String, excludeId: Int) =
        dao.countByNomorInduk(nomorInduk, excludeId) > 0

    fun save(f: MemberForm, id: Int?) = viewModelScope.launch {
        val m = Member(
            id = id ?: 0,
            namaLengkap = f.nama.trim(), nomorInduk = f.nomorInduk.trim(),
            programStudi = f.programStudi.trim(), nomorTelepon = f.telepon.trim(),
            email = f.email.trim(), alamat = f.alamat.trim(), jenis = f.jenis
        )
        if (id == null) dao.insert(m) else dao.update(m)
    }

    fun delete(m: Member) = viewModelScope.launch { dao.delete(m) }
}
