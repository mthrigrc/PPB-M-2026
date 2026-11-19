
package com.example.tugas_8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val FoodRed = Color(0xFFE53935)
private val FoodBackground = Color(0xFFF7F7F7)

data class Food(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val category: String,
    val emoji: String
)

data class CartItem(
    val food: Food,
    val quantity: Int
)

private val foodList = listOf(
    Food(1, "Nasi Goreng Spesial",
        "Nasi goreng dengan telur, ayam, dan bumbu spesial.",
        18000, "Makanan", "🍛"),
    Food(2, "Ayam Geprek",
        "Ayam crispy dengan sambal pedas dan nasi hangat.",
        20000, "Makanan", "🍗"),
    Food(3, "Mie Goreng",
        "Mie goreng spesial dengan sayuran dan telur.",
        15000, "Makanan", "🍜"),
    Food(4, "Burger Beef",
        "Burger daging sapi dengan keju dan sayuran.",
        25000, "Makanan", "🍔"),
    Food(5, "Kentang Goreng",
        "Kentang goreng renyah dengan saus pilihan.",
        12000, "Snack", "🍟"),
    Food(6, "Es Teh Manis",
        "Es teh segar dengan rasa manis yang pas.",
        5000, "Minuman", "🥤"),
    Food(7, "Jus Alpukat",
        "Jus alpukat segar dengan susu cokelat.",
        15000, "Minuman", "🥑"),
    Food(8, "Pizza Mini",
        "Pizza dengan topping keju dan sosis.",
        22000, "Snack", "🍕")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodOrderApp()
        }
    }
}

@Composable
fun FoodOrderApp() {

    var darkMode by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Beranda") }
    var selectedFood by remember { mutableStateOf<Food?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var showCheckout by remember { mutableStateOf(false) }
    var orderComplete by remember { mutableStateOf(false) }
    var paymentMethod by remember { mutableStateOf("Transfer Bank") }
    var address by remember { mutableStateOf("Jl. Pendidikan No. 10, Sidoarjo") }

    val cart = remember { mutableStateListOf<CartItem>() }
    val favorites = remember { mutableStateListOf<Int>() }

    val subtotal = cart.sumOf { it.food.price * it.quantity }
    val shipping = if (cart.isEmpty()) 0 else 5000
    val total = subtotal + shipping
    val cartCount = cart.sumOf { it.quantity }

    val colorScheme = if (darkMode) {
        darkColorScheme(
            primary = Color(0xFFFF625D),
            background = Color(0xFF121212),
            surface = Color(0xFF222222)
        )
    } else {
        lightColorScheme(
            primary = FoodRed,
            background = FoodBackground,
            surface = Color.White
        )
    }

    MaterialTheme(colorScheme = colorScheme) {

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,

            bottomBar = {
                if (selectedFood == null && !showCheckout && !orderComplete) {
                    NavigationBar {
                        val tabs = listOf(
                            "Beranda", "Wishlist", "Keranjang", "Profil"
                        )
                        val icons = listOf("⌂", "♡", "🛒", "♙")

                        tabs.forEachIndexed { index, tab ->
                            NavigationBarItem(
                                selected = selectedTab == tab,
                                onClick = { selectedTab = tab },
                                icon = {
                                    Text(
                                        icons[index],
                                        fontSize = 21.sp
                                    )
                                },
                                label = { Text(tab, fontSize = 10.sp) }
                            )
                        }
                    }
                }
            }
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                when {
                    orderComplete -> {
                        SuccessScreen(
                            onHome = {
                                orderComplete = false
                                selectedTab = "Beranda"
                            }
                        )
                    }

                    showCheckout -> {
                        CheckoutScreen(
                            address = address,
                            onAddressChange = { address = it },
                            paymentMethod = paymentMethod,
                            onPaymentChange = { paymentMethod = it },
                            total = total,
                            onBack = { showCheckout = false },
                            onOrder = {
                                cart.clear()
                                showCheckout = false
                                orderComplete = true
                            }
                        )
                    }

                    selectedFood != null -> {
                        FoodDetailScreen(
                            food = selectedFood!!,
                            onBack = { selectedFood = null },
                            onAdd = { food, quantity ->
                                val index = cart.indexOfFirst {
                                    it.food.id == food.id
                                }

                                if (index >= 0) {
                                    val old = cart[index]
                                    cart[index] = old.copy(
                                        quantity = old.quantity + quantity
                                    )
                                } else {
                                    cart.add(CartItem(food, quantity))
                                }

                                selectedFood = null
                            }
                        )
                    }

                    else -> {
                        when (selectedTab) {
                            "Beranda" -> HomeScreen(
                                searchQuery = searchQuery,
                                onSearchChange = { searchQuery = it },
                                selectedCategory = selectedCategory,
                                onCategoryChange = {
                                    selectedCategory = it
                                },
                                onFoodClick = { selectedFood = it },
                                onFavorite = { food ->
                                    if (food.id in favorites) {
                                        favorites.remove(food.id)
                                    } else {
                                        favorites.add(food.id)
                                    }
                                },
                                favorites = favorites.toList()
                            )

                            "Wishlist" -> WishlistScreen(
                                favorites = favorites.toList(),
                                onFoodClick = { selectedFood = it }
                            )

                            "Keranjang" -> CartScreen(
                                cart = cart.toList(),
                                onUpdate = { food, change ->
                                    val index = cart.indexOfFirst {
                                        it.food.id == food.id
                                    }
                                    if (index >= 0) {
                                        val newQuantity =
                                            cart[index].quantity + change

                                        if (newQuantity <= 0) {
                                            cart.removeAt(index)
                                        } else {
                                            cart[index] = cart[index].copy(
                                                quantity = newQuantity
                                            )
                                        }
                                    }
                                },
                                subtotal = subtotal,
                                shipping = shipping,
                                total = total,
                                onCheckout = { showCheckout = true }
                            )

                            "Profil" -> ProfileScreen(
                                darkMode = darkMode,
                                onThemeChange = { darkMode = it }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    onFoodClick: (Food) -> Unit,
    onFavorite: (Food) -> Unit,
    favorites: List<Int>
) {
    val categories = listOf("Semua", "Makanan", "Minuman", "Snack")

    val filteredFoods = foodList.filter {
        (selectedCategory == "Semua" ||
                it.category == selectedCategory) &&
                it.name.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "FoodOrder",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = FoodRed
                    )
                    Text("Mau makan apa hari ini?")
                }
                Text("🍽️", fontSize = 34.sp)
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = FoodRed
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text(
                        "Halo, Selamat Datang! 👋",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Pesan makanan favoritmu sekarang.",
                        color = Color.White
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari makanan...") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )
        }

        item {
            Text(
                "Kategori",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { onCategoryChange(category) },
                        label = { Text(category) }
                    )
                }
            }
        }

        item {
            Text(
                "Rekomendasi Makanan",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(filteredFoods, key = { it.id }) { food ->
            FoodCard(
                food = food,
                isFavorite = food.id in favorites,
                onClick = { onFoodClick(food) },
                onFavorite = { onFavorite(food) }
            )
        }

        if (filteredFoods.isEmpty()) {
            item {
                Text("Makanan tidak ditemukan.")
            }
        }
    }
}

@Composable
fun FoodCard(
    food: Food,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(85.dp)
                    .background(
                        Color(0xFFFFEEEE),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(food.emoji, fontSize = 42.sp)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    food.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    food.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    "Rp${food.price}",
                    color = FoodRed,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                if (isFavorite) "♥" else "♡",
                color = FoodRed,
                fontSize = 25.sp,
                modifier = Modifier.clickable { onFavorite() }
            )
        }
    }
}

@Composable
fun FoodDetailScreen(
    food: Food,
    onBack: () -> Unit,
    onAdd: (Food, Int) -> Unit
) {
    var quantity by remember(food.id) { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            "← Kembali",
            modifier = Modifier.clickable { onBack() },
            color = FoodRed
        )

        Spacer(Modifier.height(35.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .background(
                    Color(0xFFFFEEEE),
                    RoundedCornerShape(22.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(food.emoji, fontSize = 110.sp)
        }

        Spacer(Modifier.height(22.dp))

        Text(food.name, fontSize = 25.sp, fontWeight = FontWeight.Bold)

        Text(
            "Rp${food.price}",
            color = FoodRed,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))
        Text(food.description)

        Spacer(Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(onClick = {
                if (quantity > 1) quantity--
            }) {
                Text("−")
            }

            Text(
                "$quantity",
                modifier = Modifier.padding(horizontal = 25.dp),
                fontSize = 20.sp
            )

            OutlinedButton(onClick = { quantity++ }) {
                Text("+")
            }
        }

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = { onAdd(food, quantity) },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FoodRed
            )
        ) {
            Text("🛒  Tambahkan ke Keranjang")
        }
    }
}

@Composable
fun CartScreen(
    cart: List<CartItem>,
    onUpdate: (Food, Int) -> Unit,
    subtotal: Int,
    shipping: Int,
    total: Int,
    onCheckout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        Text("Keranjang Belanja", fontSize = 25.sp,
            fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(15.dp))

        if (cart.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("🛒\n\nKeranjang kamu masih kosong.",
                    fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cart, key = { it.food.id }) { item ->
                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.food.emoji, fontSize = 35.sp)
                            Spacer(Modifier.width(12.dp))

                            Column(Modifier.weight(1f)) {
                                Text(item.food.name,
                                    fontWeight = FontWeight.Bold)
                                Text("Rp${item.food.price}")
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "−",
                                        modifier = Modifier.clickable {
                                            onUpdate(item.food, -1)
                                        }.padding(8.dp)
                                    )
                                    Text("${item.quantity}")
                                    Text(
                                        "+",
                                        modifier = Modifier.clickable {
                                            onUpdate(item.food, 1)
                                        }.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider()
            SummaryRow("Subtotal", subtotal)
            SummaryRow("Ongkir", shipping)
            SummaryRow("Total", total, true)

            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FoodRed
                )
            ) {
                Text("Checkout")
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: Int, bold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text(
            "Rp$value",
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = if (bold) FoodRed else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CheckoutScreen(
    address: String,
    onAddressChange: (String) -> Unit,
    paymentMethod: String,
    onPaymentChange: (String) -> Unit,
    total: Int,
    onBack: () -> Unit,
    onOrder: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            "← Kembali",
            color = FoodRed,
            modifier = Modifier.clickable { onBack() }
        )

        Spacer(Modifier.height(20.dp))

        Text("Checkout", fontSize = 26.sp,
            fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(20.dp))

        Text("Alamat Pengiriman", fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = address,
            onValueChange = onAddressChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Alamat") }
        )

        Spacer(Modifier.height(25.dp))

        Text("Metode Pembayaran", fontWeight = FontWeight.Bold)

        listOf(
            "Transfer Bank",
            "E-Wallet (OVO, GoPay, DANA)",
            "QRIS"
        ).forEach { method ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = paymentMethod == method,
                    onClick = { onPaymentChange(method) }
                )
                Text(method)
            }
        }

        Spacer(Modifier.weight(1f))

        SummaryRow("Total Pembayaran", total, true)

        Button(
            onClick = onOrder,
            enabled = address.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FoodRed
            )
        ) {
            Text("Buat Pesanan")
        }
    }
}

@Composable
fun SuccessScreen(onHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("✅", fontSize = 90.sp)

        Text(
            "Pesanan Berhasil!",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(10.dp))

        Text("Terima kasih sudah memesan di FoodOrder!")

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = onHome,
            colors = ButtonDefaults.buttonColors(
                containerColor = FoodRed
            )
        ) {
            Text("Kembali ke Beranda")
        }
    }
}

@Composable
fun WishlistScreen(
    favorites: List<Int>,
    onFoodClick: (Food) -> Unit
) {
    val favoriteFoods = foodList.filter { it.id in favorites }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Wishlist", fontSize = 25.sp,
                fontWeight = FontWeight.Bold)
        }

        if (favoriteFoods.isEmpty()) {
            item {
                Text("Belum ada makanan favorit.")
            }
        }

        items(favoriteFoods, key = { it.id }) { food ->
            FoodCard(
                food = food,
                isFavorite = true,
                onClick = { onFoodClick(food) },
                onFavorite = {}
            )
        }
    }
}

@Composable
fun ProfileScreen(
    darkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Profil", fontSize = 27.sp,
            fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(25.dp))

        Text("👤", fontSize = 60.sp)

        Text("Pengguna FoodOrder",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp)

        Spacer(Modifier.height(30.dp))

        Text("Pengaturan Tema",
            fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode", modifier = Modifier.weight(1f))
            Switch(
                checked = darkMode,
                onCheckedChange = onThemeChange
            )
        }

        HorizontalDivider()

        Spacer(Modifier.height(15.dp))

        Text("FoodOrder v1.0.0",
            color = Color.Gray)
    }
}
