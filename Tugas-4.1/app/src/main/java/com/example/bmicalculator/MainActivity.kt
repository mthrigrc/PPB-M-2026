package com.example.bmicalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                BMICalculatorApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICalculatorApp() {

    var weight by remember {
        mutableStateOf("")
    }

    var height by remember {
        mutableStateOf("")
    }

    var bmi by remember {
        mutableStateOf<Double?>(null)
    }

    var category by remember {
        mutableStateOf("")
    }

    // BMI calculation
    fun calculateBMI() {

        val weightValue = weight.toDoubleOrNull()
        val heightValue = height.toDoubleOrNull()

        if (weightValue != null &&
            heightValue != null &&
            heightValue > 0
        ) {

            // Convert height from cm to meter
            val heightInMeter = heightValue / 100

            // BMI formula
            val result = weightValue /
                    (heightInMeter * heightInMeter)

            bmi = result

            // Determine BMI category
            category = when {
                result < 18.5 -> "Underweight"
                result < 25 -> "Normal"
                result < 30 -> "Overweight"
                else -> "Obese"
            }
        }
    }

    // Reset everything
    fun reset() {
        weight = ""
        height = ""
        bmi = null
        category = ""
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "BMI Calculator",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },

                actions = {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 28.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
                .padding(horizontal = 16.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // Weight input
            InputSection(
                label = "Berat Badan (kg)",
                value = weight,
                onValueChange = {
                    weight = it
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // Height input
            InputSection(
                label = "Tinggi Badan (cm)",
                value = height,
                onValueChange = {
                    height = it
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // Calculate button
            Button(

                onClick = {
                    calculateBMI()
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),

                shape = RoundedCornerShape(8.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C2BD9)
                )

            ) {

                Text(
                    text = "▣  Hitung BMI",
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // Reset button
            OutlinedButton(

                onClick = {
                    reset()
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),

                shape = RoundedCornerShape(8.dp),

                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF1976D2)
                )

            ) {

                Text(
                    text = "↻  Reset",
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // Category button
            Button(

                onClick = {
                    // Category is already displayed below
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),

                shape = RoundedCornerShape(8.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF20B957)
                )

            ) {

                Text(
                    text = "▥  Lihat Kategori",
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // BMI result
            ResultCard(
                bmi = bmi,
                category = category
            )
        }
    }
}


@Composable
fun InputSection(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(8.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEAF3FB)
        )

    ) {

        Column(
            modifier = Modifier.padding(10.dp)
        ) {

            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF333333)
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            OutlinedTextField(

                value = value,

                onValueChange = { newValue ->

                    // Only allow numbers and decimal points
                    if (
                        newValue.isEmpty() ||
                        newValue.matches(
                            Regex("^\\d*\\.?\\d*$")
                        )
                    ) {
                        onValueChange(newValue)
                    }
                },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),

                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}


@Composable
fun ResultCard(
    bmi: Double?,
    category: String
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(8.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F7FC)
        )

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(
                text = "Hasil BMI",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = bmi?.let {
                    String.format(
                        Locale.US,
                        "%.1f",
                        it
                    )
                } ?: "--",

                fontSize = 28.sp,

                fontWeight = FontWeight.Bold,

                color = Color(0xFF1F2937)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (category.isNotEmpty()) {

                Surface(

                    shape = RoundedCornerShape(20.dp),

                    color = Color(0xFF20B957)

                ) {

                    Text(

                        text = category,

                        modifier = Modifier.padding(
                            horizontal = 20.dp,
                            vertical = 7.dp
                        ),

                        color = Color.White,

                        fontSize = 13.sp,

                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}