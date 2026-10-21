package com.example.libraryreg.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.libraryreg.MemberViewModel
import com.example.libraryreg.data.Member
import com.example.libraryreg.ui.theme.*

@Composable
fun MemberListScreen(
    vm: MemberViewModel,
    focusSearch: Boolean,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onDetail: (Int) -> Unit
) {
    val members by vm.members.collectAsStateWithLifecycle()
    val query by vm.query.collectAsStateWithLifecycle()
    val filter by vm.filter.collectAsStateWithLifecycle()
    var toDelete by remember { mutableStateOf<Member?>(null) }
    val focus = remember { FocusRequester() }

    LaunchedEffect(Unit) { if (focusSearch) focus.requestFocus() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("Daftar Anggota", "Cari berdasarkan nama atau nomor induk", onBack) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd, containerColor = Blue, contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = "Tambah anggota")
            }
        }
    ) { pad ->
        Column(Modifier.padding(pad).padding(horizontal = 20.dp)) {
            Text("Pencarian", fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { vm.query.value = it },
                modifier = Modifier.fillMaxWidth().focusRequester(focus),
                placeholder = { Text("Cari nama atau nomor induk") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Line
                )
            )
            Row(
                Modifier.padding(vertical = 12.dp).horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Semua" to "", "Mahasiswa" to "Mahasiswa", "Dosen" to "Dosen", "Staf" to "Staf")
                    .forEach { (label, value) ->
                        FilterChip(
                            selected = filter == value,
                            onClick = { vm.filter.value = value },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Blue, selectedLabelColor = Color.White
                            )
                        )
                    }
            }
            if (members.isEmpty()) {
                Text("Belum ada data anggota", color = Muted, modifier = Modifier.padding(top = 24.dp))
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(members, key = { it.id }) { m ->
                        MemberItem(m, onView = { onDetail(m.id) }, onDelete = { toDelete = m })
                    }
                }
            }
        }
    }

    toDelete?.let { m ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("Hapus data anggota?") },
            text = { Text("Data ${m.namaLengkap} akan dihapus permanen.") },
            confirmButton = {
                TextButton(onClick = { vm.delete(m); toDelete = null }) {
                    Text("Ya, hapus", color = Danger)
                }
            },
            dismissButton = { TextButton(onClick = { toDelete = null }) { Text("Batal") } },
            containerColor = Color.White
        )
    }
}

@Composable
private fun MemberItem(m: Member, onView: () -> Unit, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(m.namaLengkap, fontWeight = FontWeight.Bold)
                Text("${m.nomorInduk} · ${m.jenis}", color = Muted, fontSize = 13.sp)
            }
            SmallButton("Lihat", Blue, onView)
            Spacer(Modifier.width(8.dp))
            SmallButton("Hapus", Danger, onDelete)
        }
    }
}

@Composable
private fun SmallButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(44.dp),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White)
    ) { Text(text, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
}
