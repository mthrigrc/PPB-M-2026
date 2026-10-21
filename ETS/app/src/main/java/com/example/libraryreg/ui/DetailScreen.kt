package com.example.libraryreg.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryreg.MemberViewModel
import com.example.libraryreg.data.Member
import com.example.libraryreg.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(vm: MemberViewModel, memberId: Int, onBack: () -> Unit, onEdit: (Int) -> Unit) {
    val member by vm.observe(memberId).collectAsState(initial = null)
    var showDelete by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Detail Anggota", "Lihat, ubah, atau hapus data", onBack) }
    ) { pad ->
        val m = member ?: return@Scaffold
        Column(
            Modifier.padding(pad).verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(56.dp).background(Blue, CircleShape), contentAlignment = Alignment.Center) {
                        Text(m.namaLengkap.first().uppercase(), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(m.namaLengkap, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(m.jenis, color = Muted, fontSize = 13.sp)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailRow("NIM / NIP", m.nomorInduk)
                    DetailRow("Program Studi / Unit", m.programStudi)
                    DetailRow("Nomor Telepon", m.nomorTelepon)
                    DetailRow("Email", m.email)
                    DetailRow("Alamat", m.alamat)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AppButton("Ubah", { onEdit(m.id) }, Modifier.weight(1f))
                AppButton("Hapus", { showDelete = true }, Modifier.weight(1f), Danger)
            }
        }

        if (showDelete) {
            DeleteSheet(m, onDismiss = { showDelete = false }) {
                vm.delete(m)
                showDelete = false
                onBack()
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column {
        Text(label, color = Muted, fontSize = 13.sp)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteSheet(m: Member, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Color.White) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp).navigationBarsPadding()) {
            Text("Hapus data anggota?", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Data ${m.namaLengkap} akan dihapus permanen.", color = Muted, fontSize = 13.sp,
                modifier = Modifier.padding(top = 6.dp, bottom = 14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Batal", fontWeight = FontWeight.Bold) }
                AppButton("Ya, hapus", onConfirm, Modifier.weight(1f), Danger)
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
