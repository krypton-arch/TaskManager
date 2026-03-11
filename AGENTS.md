# AGENTS.md — Personal Task Manager App (Android / Kotlin / Jetpack Compose)

## Project Overview

You are building a **Personal Task Manager** Android application using **Kotlin** and **Jetpack Compose**. The app allows users to add, view, update, and delete daily tasks. All data is persisted locally using **Room Database**. The architecture follows **MVVM (Model–View–ViewModel)**.

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** MVVM
- **Database:** Room (local persistence)
- **State Management:** `StateFlow` / `Flow` from ViewModel to UI
- **Min SDK:** 26 | **Target SDK:** 35
- **Build System:** Gradle (Kotlin DSL)

---

## UI Design Reference

The UI is inspired by two reference designs. Together they define a **clean, minimal, pastel-accented** task management aesthetic.

---

### Design Reference 1 — Dashboard & Statistics Style

**Source:** Dark-header design with green/yellow/blue accent cards.

#### Color Palette (Ref 1)
- Background: Off-white `#F2F2F0`
- Dark surface (TopBar, BottomBar, FAB): `#1A1A1A`
- Hero card: Soft green `#C8E6C9`
- Accent yellow card: `#FFF9C4`
- Accent blue card: `#B2EBF2`
- Muted text: `#6B6B6B`
- Chip/tag background: `#E8E8E8`

#### Layout Notes (Ref 1)
- Dark TopBar with white title and avatar + bell icons
- Dark BottomNav, icon-only, pill highlight on selected tab
- Hero summary card (green) at top of task list showing bold task count
- Cards alternate green → white → yellow in `LazyColumn`
- Stats screen: large bold greeting hero text + yellow growth card + blue progress card

---

### Design Reference 2 — Task Schedule & Manage Style

**Source:** Light minimal design with purple/lavender + lime green accent system.

#### Color Palette (Ref 2)
- Background: Pure white `#FFFFFF` / near-white `#F7F7F7`
- Primary accent (purple): `#C5B8F5` (lavender/soft purple) — used for task cards, selected states
- Secondary accent (green): `#C8E86A` (lime yellow-green) — used for selected calendar date, priority badges, task events
- Dark pill / toggle: `#1A1A1A`
- Card background white: `#FFFFFF`
- Task card border/shadow: subtle, `1.dp`, `Color(0xFFE0E0E0)`
- Priority badge: lime green `#C8E86A` with dark text
- Status chips: `In progress` (lime green), `In review` (lavender), `On hold` (white outlined)
- Time label chips: dark rounded pill `#1A1A1A` with white text

#### Typography (Ref 2)
- Screen title: "Manage your task" / "Task schedule" — Large, `FontWeight.Bold`, ~24–28sp
- Task card title: Bold, 16–18sp, white text on colored card
- Time/date label: 11–12sp, muted or pill-enclosed
- Status chip text: 11sp, `FontWeight.SemiBold`

#### App Icon Style
- Rounded square icon with white background
- Two horizontal rounded pill shapes — purple (top, wide) and green (bottom, narrow) — stacked and left-aligned
- This can be reflected as the app's launcher icon design

---

## Merged UI System (Apply Both References)

### Final Color Tokens — `Color.kt`

```kotlin
// Surfaces
val DarkSurface = Color(0xFF1A1A1A)
val OffWhiteBackground = Color(0xFFF7F7F7)
val CardWhite = Color(0xFFFFFFFF)

// Accent — from Ref 1
val CardGreen = Color(0xFFC8E6C9)
val CardYellow = Color(0xFFFFF9C4)
val CardBlue = Color(0xFFB2EBF2)

// Accent — from Ref 2
val PurpleAccent = Color(0xFFC5B8F5)
val LimeGreen = Color(0xFFC8E86A)

// Text
val PrimaryText = Color(0xFF1A1A1A)
val MutedText = Color(0xFF6B6B6B)
val ChipBackground = Color(0xFFE8E8E8)
```

### Shape System
- Cards: `RoundedCornerShape(24.dp)`
- Small chips/badges: `RoundedCornerShape(50)` (full pill)
- Calendar selected date: `CircleShape` with `LimeGreen` fill
- Dialogs: `RoundedCornerShape(20.dp)`
- Task schedule time blocks: `RoundedCornerShape(50)` pill shape

### Typography
- Hero/headline: `FontWeight.ExtraBold`, 32–40sp
- Screen titles: `FontWeight.Bold`, 24–28sp
- Task card titles: `FontWeight.Bold`, 16–18sp, white on colored card
- Body: `FontWeight.Normal`, 14sp
- Labels/chips: `FontWeight.SemiBold`, 11–12sp

---

## Screen-by-Screen UI Spec

### Screen 1 — Task List Screen (`TaskListScreen.kt`)

**Top Section:**
- White TopBar: title "Task Manager" left-aligned, `+` FAB-style icon button (dark circle) top-right, bell icon next to it
- User avatar (circular, 40.dp) below top bar, left side
- Greeting: "Manage your task" in bold 26sp below avatar

**Hero Next-Task Card:**
- Full-width dark pill card (`#1A1A1A`, `RoundedCornerShape(50)`)
- Left: label "Next Meeting" with clock icon + time range (e.g., "02:15 PM – 02:30 PM")
- Right: lime green circle arrow button `→`
- Style: white text on dark background

**Status Row:**
- Horizontal row of status filter chips: `In progress` (LimeGreen), `In review` (PurpleAccent), `On hold` (outlined)
- Each chip shows count badge (small dark circle with number)
- Right side: calendar icon button + arrow icon button (dark circles)

**Task Cards (LazyColumn):**
- Card background: `PurpleAccent` for active/current tasks
- Show: task title (bold white), due date/time (small, white with clock icon), priority badge (`High Priority` in lime green pill), team count ("3 In team")
- At the bottom of each card: small circular avatars of team members + `+` add button

**Alternating card colors:** PurpleAccent → White → CardGreen → White → ...

---

### Screen 2 — Task Schedule Screen (`ManageTasksScreen.kt`)

**Header:**
- Back arrow `←` top-left
- Title "Task schedule" bold 24sp
- Right: Calendar toggle icon + "Calendar" label button (pill shape)

**Calendar Row (Horizontal week strip):**
- 7 days displayed horizontally: `S M T W T F S`
- Each day: day-name label (muted, 11sp) above date number (bold, 16sp)
- Selected date: `LimeGreen` circle background, dark number
- Other dates: white circle or no background
- Today indicator: small dot below number

**Time-based Task Schedule (vertical timeline):**
- Left column: time labels (`08 AM`, `09 AM`, etc.) in muted text, 11sp
- Right column: task blocks as pill-shaped cards, spanning their time duration
  - Green pill: `#C8E86A` — e.g., "Design registration process" with count badge
  - Purple pill: `#C5B8F5` — e.g., "User flow admin panel" with time label on right
  - White outlined pill: e.g., "Web application design"
  - Dark pill: `#1A1A1A` — e.g., "Dashboard design" with white text + count badge + time label
- Time range label: small dark pill on the right of each task block showing "10AM–1PM"

**FAB:** Lime green circle at bottom-right with `+` icon (dark)

---

## Component Specs

### `TaskCard.kt`
```
Card(
  backgroundColor = PurpleAccent or alternating,
  shape = RoundedCornerShape(24.dp),
  elevation = 0.dp
) {
  Column(padding = 16.dp) {
    Text(task.title, style = Bold 18sp, color = White)
    Row { Icon(clock); Text(task.dueDate, style = 12sp muted) }
    Row(horizontalArrangement = SpaceBetween) {
      PriorityBadge("High Priority")   // lime green pill
      Text("3 In team", muted)
    }
    AvatarRow()  // small circular avatars + + button
  }
}
```

### `ScheduleTaskBlock.kt`
```
Box(
  modifier = Modifier
    .fillMaxWidth(fraction)   // width based on duration
    .height(56.dp)
    .clip(RoundedCornerShape(50)),
  backgroundColor = LimeGreen / PurpleAccent / DarkSurface
) {
  Row {
    Text(task.title)
    Spacer()
    TimePill("10AM-1PM")   // small dark pill
    CountBadge(task.count) // small dark circle with number
  }
}
```

### `WeekCalendarRow.kt`
- Horizontal `LazyRow` of 7 day items
- Each item: `Column { Text(dayName); Box(CircleShape) { Text(dayNum) } }`
- Selected: `Box` fill = `LimeGreen`, text = `DarkSurface`
- Unselected: transparent background

### `StatusChip.kt`
```
Surface(
  shape = RoundedCornerShape(50),
  color = chipColor,
  border = if outlined then BorderStroke(1.dp, MutedText)
) {
  Row { Text(label); Badge(count) }
}
```

### `TopBar.kt`
- Left: circular avatar (40.dp) + "Manage your task" title
- Right: `+` button (dark circle 40.dp) + bell icon (dark circle 40.dp)
- Background: White / `OffWhiteBackground`
- No elevation

### `BottomNavigation` (from Ref 1)
- Background: `DarkSurface`
- Icons: `GridView`, `BarChart`, `Settings`, `Person`
- Selected: white pill highlight behind icon
- Unselected: muted grey icon

---

## Project Structure

```
app/src/main/java/com/example/taskmanager/
├── data/
│   ├── Task.kt
│   ├── TaskDao.kt
│   ├── TaskDatabase.kt
│   └── TaskRepository.kt
├── viewmodel/
│   └── TaskViewModel.kt
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Type.kt
│   │   └── Theme.kt
│   ├── screens/
│   │   ├── TaskListScreen.kt
│   │   └── ManageTasksScreen.kt
│   ├── components/
│   │   ├── TopBar.kt
│   │   ├── TaskCard.kt
│   │   ├── ScheduleTaskBlock.kt
│   │   ├── WeekCalendarRow.kt
│   │   ├── StatusChip.kt
│   │   ├── AvatarRow.kt
│   │   └── AddEditTaskDialog.kt
│   └── navigation/
│       └── BottomNavGraph.kt
└── MainActivity.kt
```

---

## Room Database — MVVM

### Entity — `Task.kt`
```kotlin
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    val title: String,
    val description: String,
    val dueDate: String
)
```

### DAO — `TaskDao.kt`
- `@Insert(onConflict = REPLACE) suspend fun insert(task: Task)`
- `@Query("SELECT * FROM tasks ORDER BY taskId DESC") fun getAllTasks(): Flow<List<Task>>`
- `@Update suspend fun update(task: Task)`
- `@Delete suspend fun delete(task: Task)`

### Database, Repository, ViewModel
- Follow standard MVVM pattern
- `StateFlow<List<Task>>` in ViewModel via `stateIn`
- `viewModelScope.launch` for all DB operations
- Never access DAO from UI directly

---

## Coding Rules

### Do
- Use Material 3 exclusively (`androidx.compose.material3.*`)
- Use `Scaffold` for layout with `topBar`, `bottomBar`, `floatingActionButton`
- All colors from `Color.kt` — never inline hex strings
- `RoundedCornerShape(50)` for pills, `RoundedCornerShape(24.dp)` for cards
- `FontWeight.ExtraBold` for hero text, `FontWeight.Bold` for card titles
- `Flow` → `StateFlow` → `collectAsState()` for all DB observation

### Don't
- No XML layouts
- No `LiveData`
- No `GlobalScope`
- No card elevation/shadow — use color contrast only
- No `RecyclerView`
- No hardcoded color values inline

---

## Dependencies (`build.gradle.kts`)

```kotlin
implementation(platform("androidx.compose:compose-bom:2024.05.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.navigation:navigation-compose:2.7.7")
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
```

Add `id("kotlin-kapt")` to plugins block.

---

## File Generation Order

1. `Color.kt` → `Type.kt` → `Theme.kt`
2. `Task.kt` → `TaskDao.kt` → `TaskDatabase.kt` → `TaskRepository.kt`
3. `TaskViewModel.kt`
4. `StatusChip.kt` → `WeekCalendarRow.kt` → `AvatarRow.kt`
5. `TaskCard.kt` → `ScheduleTaskBlock.kt` → `TopBar.kt` → `AddEditTaskDialog.kt`
6. `TaskListScreen.kt` → `ManageTasksScreen.kt`
7. `BottomNavGraph.kt`
8. `MainActivity.kt`
9. Update `build.gradle.kts`

---

## Verification Checklist

- [ ] `Color.kt` defines all 11 color tokens; no inline hex in composables
- [ ] TopBar: light background, avatar + title left, dark circle `+` and bell right
- [ ] Task List screen: greeting, dark next-meeting pill card, status chip row, purple task cards in LazyColumn
- [ ] Each task card: bold white title, time, `High Priority` lime badge, avatar row
- [ ] Manage screen: back arrow, "Task schedule" title, horizontal week calendar, vertical timeline with colored pill task blocks
- [ ] Selected calendar day: LimeGreen circle
- [ ] Schedule task blocks: colored pills (lime, purple, dark, white) with time labels and count badges
- [ ] Dark BottomNav with pill-highlighted selected icon, 4 tabs
- [ ] FAB: lime green circle with dark `+` on Manage screen; dark circle on Task List screen
- [ ] Room: all 4 CRUD ops, `Flow` return on read
- [ ] ViewModel: `StateFlow`, `viewModelScope`, no direct DAO access from UI
- [ ] `AddEditTaskDialog` works for both add and edit
- [ ] UI updates reactively on DB change

---

*Place this file at the project root. Compatible with Google Firebase Studio, Gemini in Android Studio, Cursor, and Codex agents.*
