package com.example.libraryreg.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryreg.MemberForm
import com.example.libraryreg.MemberValidator
import com.example.libraryreg.MemberViewModel
import com.example.libraryreg.ui.theme.Blue
import kotlinx.coroutines.launch

/** Dipakai untuk pendaftaran baru (memberId == null) dan ubah data (memberId != null). */
@Composable
fun FormScreen(vm: MemberViewModel, memberId: Int?, onBack: () -> Unit) {
    var nama by rememberSaveable { mutableStateOf("") }
    var nomorInduk by rememberSaveable { mutableStateOf("") }
    var prodi by rememberSaveable { mutableStateOf("") }
    var telepon by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var alamat by rememberSaveable { mutableStateOf("") }
    var jenis by rememberSaveable { mutableStateOf("Mahasiswa") }
    var errors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Mode ubah: isi form dengan data yang tersimpan
    LaunchedEffect(memberId) {
        if (memberId != null) vm.load(memberId)?.let {
            nama = it.namaLengkap; nomorInduk = it.nomorInduk; prodi = it.programStudi
            telepon = it.nomorTelepon; email = it.email; alamat = it.alamat; jenis = it.jenis
        }
    }

    fun submit() {
        val form = MemberForm(nama, nomorInduk, prodi, telepon, email, alamat, jenis)
        val result = MemberValidator.validate(form)
        if (result.isNotEmpty()) { errors = result; return }
        scope.launch {
            if (vm.isDuplicate(form.nomorInduk.trim(), memberId ?: 0)) {
                errors = mapOf("nomorInduk" to "Nomor induk sudah terdaftar")
            } else {
                vm.save(form, memberId)
                Toast.makeText(
                    context,
                    if (memberId == null) "Anggota berhasil didaftarkan" else "Data berhasil diubah",
                    Toast.LENGTH_SHORT
                ).show()
                onBack()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                if (memberId == null) "Formulir Pendaftaran" else "Ubah Data Anggota",
                "Semua kolom bertanda * wajib diisi",
                onBack
            )
        },
        bottomBar = {
            Box(Modifier.padding(horizontal = 20.dp, vertical = 12.dp).navigationBarsPadding()) {
                AppButton(if (memberId == null) "Daftar" else "Simpan", ::submit, Modifier.fillMaxWidth())
            }
        }
    ) { pad ->
        Column(
            Modifier.padding(pad).imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 4.dp)
        ) {
            LabeledField("Nama Lengkap *", nama, { nama = it; errors = errors - "nama" }, "Nama lengkap", errors["nama"])
            LabeledField("NIM / NIP *", nomorInduk, { nomorInduk = it; errors = errors - "nomorInduk" }, "Contoh: 231001001", errors["nomorInduk"], KeyboardType.Number)
            LabeledField("Program Studi / Unit *", prodi, { prodi = it; errors = errors - "programStudi" }, "Pilih program studi", errors["programStudi"])
            LabeledField("Nomor Telepon *", telepon, { telepon = it; errors = errors - "telepon" }, "08xxxxxxxxxx", errors["telepon"], KeyboardType.Phone)
            LabeledField("Email *", email, { email = it; errors = errors - "email" }, "nama@kampus.ac.id", errors["email"], KeyboardType.Email)
            LabeledField("Alamat *", alamat, { alamat = it; errors = errors - "alamat" }, "Alamat lengkap", errors["alamat"], singleLine = false)

            Text("Jenis Keanggotaan *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 12.dp, bottom = 6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Mahasiswa", "Dosen", "Staf").forEach { opsi ->
                    FilterChip(
                        selected = jenis == opsi,
                        onClick = { jenis = opsi },
                        label = { Text(opsi) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Blue,
                            selectedLabelColor = androidx.compose.ui.graphics.Color.White
                        )
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
