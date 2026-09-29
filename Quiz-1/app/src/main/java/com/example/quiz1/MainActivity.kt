package com.example.quiz1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Student(
    val nim: String,
    val name: String,
    val studyProgram: String
)

private val studyPrograms = listOf(
    "Informatika",
    "Sistem Informasi",
    "Teknik Komputer",
    "Desain Komunikasi Visual",
    "Manajemen",
    "Akuntansi"
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                StudentManagerApp()
            }
        }
    }
}


@Composable
fun StudentManagerApp() {

    val students = remember {

        mutableStateListOf(
            Student(
                "2301001",
                "Budi Santoso",
                "Informatika"
            ),
            Student(
                "2301002",
                "Siti Aminah",
                "Sistem Informasi"
            ),
            Student(
                "2301003",
                "Andi Wijaya",
                "Teknik Komputer"
            )
        )
    }

    var currentScreen by remember {
        mutableStateOf("home")
    }

    var selectedStudent by remember {
        mutableStateOf<Student?>(null)
    }

    when (currentScreen) {

        "home" -> {

            HomeScreen(
                students = students,
                onAdd = {
                    selectedStudent = null
                    currentScreen = "add"
                },

                onEdit = { student ->
                    selectedStudent = student
                    currentScreen = "edit"
                },

                onDelete = { student ->
                    selectedStudent = student
                },

                selectedStudent = selectedStudent,

                onConfirmDelete = {
                    selectedStudent?.let { student ->
                        students.remove(student)

                    }
                    selectedStudent = null
                },

                onCancelDelete = {
                    selectedStudent = null
                }
            )
        }

        "add" -> {

            StudentFormScreen(
                title = "Tambah Mahasiswa",
                student = null,
                onBack = {
                    currentScreen = "home"
                },

                onSave = { newStudent ->
                    students.add(newStudent)
                    currentScreen = "home"
                }
            )
        }

        "edit" -> {

            StudentFormScreen(
                title = "Edit Mahasiswa",
                student = selectedStudent,
                onBack = {
                    currentScreen = "home"
                },

                onSave = { updatedStudent ->

                    val index = students.indexOfFirst {
                        it.nim == selectedStudent?.nim
                    }

                    if (index != -1) {
                        students[index] = updatedStudent
                    }

                    currentScreen = "home"
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    students: List<Student>,
    onAdd: () -> Unit,
    onEdit: (Student) -> Unit,
    onDelete: (Student) -> Unit,
    selectedStudent: Student?,
    onConfirmDelete: () -> Unit,
    onCancelDelete: () -> Unit

) {

    var searchQuery by remember {
        mutableStateOf("")
    }

    var menuExpanded by remember {
        mutableStateOf(false)
    }

    val filteredStudents = students.filter {

        it.name.contains(
            searchQuery,
            ignoreCase = true
        ) ||

                it.nim.contains(
                    searchQuery,
                    ignoreCase = true
                ) ||

                it.studyProgram.contains(
                    searchQuery,
                    ignoreCase = true
                )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Student Manager",
                        fontWeight = FontWeight.Bold
                    )
                },

                actions = {
                    Box {
                        Text(
                            text = "⋮",
                            fontSize = 28.sp,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable {
                                    menuExpanded = true
                                }
                        )

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = {
                                menuExpanded = false
                            }

                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text("↻  Refresh")
                                },
                                onClick = {
                                    menuExpanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("ⓘ  Tentang Aplikasi")
                                },
                                onClick = {
                                    menuExpanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Keluar")
                                },
                                onClick = {
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF7F8FA)
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdd,
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ) {

                Text(
                    "+",
                    fontSize = 28.sp
                )
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            // SEARCH

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },

                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text("Cari mahasiswa...")
                },

                leadingIcon = {
                    Text("⌕")
                },

                trailingIcon = {

                    if (searchQuery.isNotEmpty()) {
                        Text(
                            text = "×",
                            fontSize = 22.sp,
                            modifier = Modifier
                                .clickable {
                                    searchQuery = ""
                                }
                                .padding(8.dp)
                        )
                    }
                },

                shape = RoundedCornerShape(20.dp),

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Jumlah mahasiswa: ${filteredStudents.size}",
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (filteredStudents.isEmpty()) {
                EmptyState(
                    onAdd = onAdd
                )

            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        filteredStudents,
                        key = {
                            it.nim
                        }
                    ) { student ->

                        StudentCard(
                            student = student,
                            onEdit = {
                                onEdit(student)
                            },
                            onDelete = {
                                onDelete(student)
                            }
                        )
                    }
                }
            }
        }
    }


    if (selectedStudent != null) {

        AlertDialog(
            onDismissRequest = onCancelDelete,
            title = {
                Text(
                    "Hapus Mahasiswa?",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Text(
                    "Apakah Anda yakin ingin menghapus data mahasiswa ${selectedStudent.name}?"
                )
            },

            confirmButton = {
                Button(
                    onClick = onConfirmDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    )
                ) {
                    Text("Hapus")
                }
            },

            dismissButton = {
                OutlinedButton(
                    onClick = onCancelDelete
                ) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun StudentCard(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit

) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8EEF5)),

                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student.name
                        .first()
                        .uppercase(),

                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF607D8B)
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = student.name,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "NIM: ${student.nim}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Text(
                    text = student.studyProgram,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "✎",
                fontSize = 22.sp,
                modifier = Modifier
                    .clickable {
                        onEdit()
                    }
                    .padding(8.dp)
            )

            Text(
                text = "\uD83D\uDDD1\uFE0F",
                fontSize = 20.sp,
                color = Color.Red,
                modifier = Modifier
                    .clickable {
                        onDelete()
                    }
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun EmptyState(
    onAdd: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "\uD83D\uDDD1\uFE0F",
            fontSize = 50.sp,
            color = Color(0xFF90A4AE)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Belum ada data mahasiswa",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Tekan tombol + untuk menambahkan mahasiswa pertama."
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = onAdd
        ) {

            Text("Tambah Mahasiswa")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormScreen(
    title: String,
    student: Student?,
    onBack: () -> Unit,
    onSave: (Student) -> Unit

) {

    var nim by remember {
        mutableStateOf(student?.nim ?: "")
    }

    var name by remember {
        mutableStateOf(student?.name ?: "")
    }

    var selectedProgram by remember {
        mutableStateOf(
            student?.studyProgram ?: ""
        )
    }

    var programExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text(title)
                },

                navigationIcon = {
                    Text(
                        text = "‹",
                        fontSize = 35.sp,
                        modifier = Modifier
                            .clickable {
                                onBack()
                            }
                            .padding(
                                horizontal = 16.dp
                            )
                    )
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "NIM",
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(
                value = nim,
                onValueChange = {
                    nim = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text("Masukkan NIM")
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Nama",
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },

                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text("Masukkan nama mahasiswa")
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "Program Studi",
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            ExposedDropdownMenuBox(
                expanded = programExpanded,
                onExpandedChange = {
                    programExpanded = !programExpanded
                }
            ) {

                OutlinedTextField(
                    value = selectedProgram,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),

                    placeholder = {
                        Text("Pilih program studi")
                    },

                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = programExpanded
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = programExpanded,
                    onDismissRequest = {
                        programExpanded = false
                    }
                ) {

                    studyPrograms.forEach { program ->

                        DropdownMenuItem(

                            text = {
                                Text(program)
                            },

                            onClick = {
                                selectedProgram = program
                                programExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(32.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                OutlinedButton(
                    onClick = onBack
                ) {

                    Text("Batal")
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Button(

                    onClick = {

                        if (
                            nim.isNotBlank() &&
                            name.isNotBlank() &&
                            selectedProgram.isNotBlank()
                        ) {

                            onSave(
                                Student(
                                    nim = nim,
                                    name = name,
                                    studyProgram = selectedProgram
                                )
                            )
                        }
                    }
                )
                {
                    Text("Simpan")
                }
            }
        }
    }
}