# Login App — Kotlin, Jetpack Compose, MVVM & Room

## Demo account
- Username: `admin`
- Password: `12345`

## Open in Android Studio
1. Extract `LoginMVVMRoom.zip`.
2. Open Android Studio → **Open** → select the extracted `LoginMVVMRoom` folder (the folder containing `settings.gradle.kts`).
3. Use JDK 17 and install Android SDK Platform 35 if Android Studio asks for it.
4. Allow Gradle Sync to finish.
5. Start an emulator (API 24 or newer) or connect an Android device with USB debugging enabled.
6. Press **Run**.

## Project structure
```text
app/src/main/java/com/example/loginapp/
├── MainActivity.kt
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   ├── User.kt
│   │   └── UserDao.kt
│   └── repository/
│       └── UserRepository.kt
├── ui/
│   ├── LoginScreen.kt
│   └── theme/
│       ├── Color.kt
│       └── Theme.kt
└── viewmodel/
    ├── LoginViewModel.kt
    └── LoginViewModelFactory.kt
```

## Login flow
1. Compose screen collects `LoginUiState` from the ViewModel.
2. User taps Login; the ViewModel validates empty fields.
3. ViewModel calls the Repository in a coroutine.
4. Repository calls `UserDao.login()`.
5. Room queries the `users` table in SQLite.
6. ViewModel publishes success or error state; Compose redraws automatically.
