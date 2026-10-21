package com.example.libraryreg.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryreg.ui.theme.*

@Composable
fun HomeScreen(onRegister: () -> Unit, onList: () -> Unit, onSearch: () -> Unit, onManage: () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AppTopBar("LibraryReg", "Pendaftaran Anggota Perpustakaan") } // tanpa tombol back
    ) { pad ->
        Column(Modifier.padding(pad).padding(20.dp)) {
            Text("Pilih menu untuk memulai", color = Muted, fontSize = 14.sp)
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                MenuCard(Modifier.weight(1f), "Pendaftaran Anggota", "Isi formulir baru", Icons.Default.Person, Blue, onRegister)
                MenuCard(Modifier.weight(1f), "Daftar Anggota", "Lihat semua data", Icons.AutoMirrored.Filled.List, Green, onList)
            }
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                MenuCard(Modifier.weight(1f), "Pencarian Data", "Nama atau nomor induk", Icons.Default.Search, Orange, onSearch)
                MenuCard(Modifier.weight(1f), "Kelola Data", "Ubah dan hapus", Icons.Default.Edit, Purple, onManage)
            }
        }
    }
}

@Composable
private fun MenuCard(
    modifier: Modifier, title: String, subtitle: String,
    icon: ImageVector, color: Color, onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(Modifier.padding(16.dp)) {
            Box(
                Modifier.size(44.dp).background(color, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(icon, contentDescription = null, tint = Color.White) }
            Spacer(Modifier.height(14.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(subtitle, color = Muted, fontSize = 13.sp)
        }
    }
}
