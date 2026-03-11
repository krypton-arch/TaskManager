# 📋 Task Manager

A clean, modern **Personal Task Manager** Android app built with **Kotlin** and **Jetpack Compose**. Add, view, update, and delete daily tasks with a beautiful pastel-accented UI, powered by **Room Database** and **MVVM** architecture.

---

## ✨ Features

- **Task CRUD** — Create, read, update, and delete tasks with a polished dialog UI
- **Task List** — Beautiful alternating-color task cards (purple, white, green) with priority badges
- **Task Schedule** — Horizontal week calendar + vertical timeline with colored pill task blocks
- **Status Tracking** — Filter tasks by status: *In progress*, *In review*, *On hold*, *Done*
- **Priority System** — High / Medium / Low priority with lime-green badge indicators
- **Dark Bottom Navigation** — 4-tab nav bar with pill-highlight on selected icon
- **Reactive UI** — Real-time updates powered by `StateFlow` + Room's `Flow` 
- **Local Persistence** — All data persisted via Room Database

---

## 📸 Screenshots

| Task List | Task Schedule |
|-----------|--------------|
| Greeting hero + dark next-task card + status chips + alternating task cards | Week calendar with LimeGreen selected date + colored timeline blocks |

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
├── data/
│   ├── Task.kt                    # Room Entity
│   ├── TaskDao.kt                 # Data Access Object
│   ├── TaskDatabase.kt            # Room Database (Singleton)
│   └── TaskRepository.kt          # Repository pattern
├── viewmodel/
│   └── TaskViewModel.kt           # StateFlow + CRUD operations
├── ui/
│   ├── theme/
│   │   ├── Color.kt               # 11 design tokens
│   │   ├── Type.kt                # Custom typography scale
│   │   └── Theme.kt               # Material 3 light theme
│   ├── screens/
│   │   ├── TaskListScreen.kt      # Main task list
│   │   └── ManageTasksScreen.kt   # Schedule timeline
│   ├── components/
│   │   ├── TopBar.kt              # Avatar + title + action buttons
│   │   ├── TaskCard.kt            # Alternating color task cards
│   │   ├── ScheduleTaskBlock.kt   # Timeline pill blocks
│   │   ├── WeekCalendarRow.kt     # Horizontal week calendar
│   │   ├── StatusChip.kt          # Status filter chips
│   │   ├── AvatarRow.kt           # Team member avatars
│   │   └── AddEditTaskDialog.kt   # Add/Edit task dialog
│   └── navigation/
│       └── BottomNavGraph.kt      # Bottom nav + NavHost
└── MainActivity.kt                # Entry point
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
