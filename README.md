# 📋 Task Manager

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg?style=flat&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-green.svg)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange.svg)
![Hilt](https://img.shields.io/badge/DI-Hilt-yellow)
![Room Database](https://img.shields.io/badge/Database-Room-blue)
![DataStore](https://img.shields.io/badge/Preferences-DataStore-lightgrey)
![WorkManager](https://img.shields.io/badge/WorkManager-Notifications-red)

A clean, modern **Personal Task Manager** Android app built with **Kotlin** and **Jetpack Compose**. Add, view, update, and delete daily tasks with a beautiful pastel-accented UI, powered by **Hilt**, **Room Database**, and **MVVM** architecture.

## 📥 Download
[**Download the latest APK here**](./TaskManager-debug.apk)

*Note: The standalone debug APK for Android devices is available directly in the root of the repository as `TaskManager-debug.apk`.*

---

## ✨ Features

- **Dependency Injection** — Fully migrated to **Hilt** for robust and scalable dependency management.
- **Search & Filter** — Quickly find tasks using the real-time search bar or filter by status (*In progress*, *In review*, *On hold*).
- **Task Reminders** — Background notifications powered by **WorkManager** to ensure you never miss a deadline.
- **Task CRUD** — Create, read, update, and delete tasks with a polished dialog UI.
- **Task List** — Beautiful alternating-color task cards with priority badges.
- **Task Schedule** — Horizontal week calendar + vertical timeline with colored pill task blocks.
- **Real-time Statistics** — Dedicated analytics screen calculating total, pending, completed tasks, and performance growth.
- **User Profile & Customization** — Personalize avatar colors and dynamic Light/Dark/System theme toggles.
- **Testing Suite** — Integrated **MockK** and **Turbine** for comprehensive unit and Flow testing.

---

## 🏗️ Architecture

```
MVVM (Model–View–ViewModel) + Clean Architecture Principles
```

```
┌─────────────────────────────────────────────────┐
│                    UI Layer                      │
│  Screens → Components → Theme                   │
│  (Jetpack Compose + Material 3)                  │
├─────────────────────────────────────────────────┤
│                ViewModel Layer                   │
│  TaskViewModel (StateFlow + Hilt)                │
├─────────────────────────────────────────────────┤
│                  Data Layer                      │
│  Repository → DAO → Room Database                │
└─────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
app/src/main/java/com/example/taskmanager/
├── TaskApplication.kt             # Hilt Application class
├── MainActivity.kt                # Entry point
├── di/                            # Dependency Injection modules
├── data/
│   ├── Task.kt                    # Room Entity
│   ├── TaskDao.kt                 # Data Access Object
│   ├── TaskDatabase.kt            # Room Database
│   ├── TaskRepository.kt          # Repository pattern
│   └── UserPreferencesRepository.kt # DataStore preferences
├── notification/
│   ├── NotificationHelper.kt      # Notification builder
│   ├── TaskReminderManager.kt     # WorkManager scheduler
│   └── TaskReminderWorker.kt      # Background worker
├── ui/
│   ├── components/                # Reusable UI components
│   ├── navigation/                # Compose Navigation
│   ├── screens/                   # App screens (Home, Schedule, Stats, Profile)
│   └── theme/                     # Design tokens and Theme
└── viewmodel/
    ├── TaskViewModel.kt           # Search, filter, and CRUD logic
    └── UserPreferencesViewModel.kt# Theme and profile management
```

---

## 🎨 Design System

### Color Palette

| Token | Hex | Usage |
|-------|-----|-------|
| `DarkSurface` | `#1A1A1A` | TopBar, BottomNav, Primary buttons |
| `PurpleAccent` | `#C5B8F5` | Primary task cards, avatar |
| `LimeGreen` | `#C8E86A` | Priority badges, FAB, Success states |
| `CardYellow` | `#FFF9C4` | Growth analytics accent |
| `MutedText` | `#6B6B6B` | Secondary information |

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin 2.0.21 |
| UI Framework | Jetpack Compose (Material 3) |
| DI | Dagger Hilt |
| Database | Room |
| Background Tasks | WorkManager |
| Testing | MockK, Turbine, JUnit4 |
| State Management | StateFlow / Flow |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 11+
- Android SDK 35

### Build & Run
```bash
# Clone the repository
git clone https://github.com/krypton-arch/TaskManager.git

# Open in Android Studio and run, or build via CLI:
cd TaskManager
./gradlew assembleDebug

# Install on connected device
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
