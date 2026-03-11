# 📋 Task Manager

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg?style=flat&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-green.svg)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange.svg)
![Room Database](https://img.shields.io/badge/Database-Room-blue)
![DataStore](https://img.shields.io/badge/Preferences-DataStore-lightgrey)

A clean, modern **Personal Task Manager** Android app built with **Kotlin** and **Jetpack Compose**. Add, view, update, and delete daily tasks with a beautiful pastel-accented UI, powered by **Room Database** and **MVVM** architecture.

## 📥 Download
[**Download the latest APK here**](./TaskManager-debug.apk)

*Note: The standalone debug APK for Android devices is available directly in the root of the repository as `TaskManager-debug.apk`.*

---

## ✨ Features

- **Task CRUD** — Create, read, update, and delete tasks with a polished dialog UI
- **Task List** — Beautiful alternating-color task cards (purple, white, green) with priority badges
- **Task Schedule** — Horizontal week calendar + vertical timeline with colored pill task blocks
- **Status Tracking** — Filter tasks by status: *In progress*, *In review*, *On hold*, *Done*
- **Priority System** — High / Medium / Low priority with lime-green badge indicators
- **Real-time Statistics** — Dedicated analytics screen calculating total, pending, completed tasks, and week-over-week growth indicators
- **User Profile & Customization** — Personalize the app with custom avatar colors and dynamic Light/Dark/System theme toggles using **Jetpack DataStore**
- **Dark Bottom Navigation** — 4-tab nav bar with pill-highlight on selected icon
- **Reactive UI** — Real-time updates powered by `StateFlow` + Room's `Flow` 
- **Local Persistence** — All data persisted via Room Database

---


## 🏗️ Architecture

```
MVVM (Model–View–ViewModel)
```

```
┌─────────────────────────────────────────────────┐
│                    UI Layer                      │
│  Screens → Components → Theme                   │
│  (Jetpack Compose + Material 3)                  │
├─────────────────────────────────────────────────┤
│                ViewModel Layer                   │
│  TaskViewModel (StateFlow + viewModelScope)      │
├─────────────────────────────────────────────────┤
│                  Data Layer                      │
│  Repository → DAO → Room Database                │
└─────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
app/src/main/java/com/example/taskmanager/
├── MainActivity.kt                # Entry point
├── data/
│   ├── Task.kt                    # Room Entity
│   ├── TaskDao.kt                 # Data Access Object
│   ├── TaskDatabase.kt            # Room Database (Singleton)
│   ├── TaskRepository.kt          # Repository pattern
│   └── UserPreferencesRepository.kt # DataStore preferences repository
├── ui/
│   ├── components/
│   │   ├── AddEditTaskDialog.kt   # Add/Edit task dialog
│   │   ├── AvatarRow.kt           # Team member avatars
│   │   ├── ScheduleTaskBlock.kt   # Timeline pill blocks
│   │   ├── StatusChip.kt          # Status filter chips
│   │   ├── TaskCard.kt            # Alternating color task cards
│   │   ├── TopBar.kt              # Avatar + title + action buttons
│   │   └── WeekCalendarRow.kt     # Horizontal week calendar
│   ├── navigation/
│   │   └── BottomNavGraph.kt      # Bottom nav + NavHost
│   ├── screens/
│   │   ├── ManageTasksScreen.kt   # Schedule timeline screen
│   │   ├── ProfileScreen.kt       # User profile and customization 
│   │   ├── StatisticsScreen.kt    # Dynamic analytics screen
│   │   └── TaskListScreen.kt      # Main task list screen
│   └── theme/
│       ├── Color.kt               # 11 design tokens
│       ├── Spacing.kt             # Padding and margin dimensions
│       ├── Theme.kt               # Dynamic light/dark theme handling
│       └── Type.kt                # Custom typography scale
└── viewmodel/
    ├── TaskViewModel.kt           # StateFlow + CRUD operations
    └── UserPreferencesViewModel.kt# StateFlow + DataStore preferences
```

---

## 🎨 Design System

### Color Palette

| Token | Hex | Usage |
|-------|-----|-------|
| `DarkSurface` | `#1A1A1A` | TopBar buttons, BottomNav, hero card |
| `OffWhiteBackground` | `#F7F7F7` | App background |
| `PurpleAccent` | `#C5B8F5` | Primary task cards, avatar |
| `LimeGreen` | `#C8E86A` | Selected date, priority badges, FAB |
| `CardGreen` | `#C8E6C9` | Alternating card color |
| `CardYellow` | `#FFF9C4` | Accent cards |
| `CardBlue` | `#B2EBF2` | Accent cards |
| `MutedText` | `#6B6B6B` | Secondary text |

### Typography
- **Hero**: ExtraBold, 36sp
- **Screen titles**: Bold, 24–26sp  
- **Card titles**: Bold, 18sp
- **Body**: Normal, 14sp
- **Labels**: SemiBold, 11–12sp

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Kotlin |
| UI Framework | Jetpack Compose (Material 3) |
| Architecture | MVVM |
| Database | Room |
| State Management | StateFlow / Flow |
| Navigation | Navigation Compose |
| Min SDK | 26 |
| Target SDK | 36 |
| Build System | Gradle (Kotlin DSL) |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 11+
- Android SDK 36

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
