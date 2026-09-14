package com.example.profile_card

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.profile_card.ui.theme.Profile_CardTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Profile_CardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileCard(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        Text(
            text = "PROFIL MAHASISWA",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)

        )
        Column(
            modifier = Modifier.fillMaxWidth(),

        )
        {
            Row(modifier = Modifier.padding(top = 16.dp)){
                Column{
                    Text("Nama", fontWeight = FontWeight.Medium)
                    Text("Matahari Gracio Sinaga", fontSize = 18.sp)
                }
            }
            Row(modifier = Modifier.padding(top = 16.dp)){
                Column{
                    Text("NRP", fontWeight = FontWeight.Medium)
                    Text("5053251049", fontSize = 18.sp)
                }
            }
            Row(modifier = Modifier.padding(top = 16.dp)){
                Column{
                    Text("Program Studi", fontWeight = FontWeight.Medium)
                    Text("Rekayasa Perangkat Lunak", fontSize = 18.sp)
                }
            }
            Row(modifier = Modifier.padding(top = 16.dp)){
                Column{
                    Text("Email", fontWeight = FontWeight.Medium)
                    Text("matahari.sinaga@gmail.com", fontSize = 18.sp)
                }
            }
            Row(modifier = Modifier.padding(top = 16.dp)){
                Column{
                    Text("Angkatan", fontWeight = FontWeight.Medium)
                    Text("25", fontSize = 18.sp)
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mahasiswa",
                fontSize = 14.sp
            )

            VerticalDivider(
                modifier = Modifier.height(24.dp),
                thickness = 1.dp
            )

            Text(
                text = "Semester 3",
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileCard_Preview() {
    Profile_CardTheme {
        ProfileCard()
    }
}