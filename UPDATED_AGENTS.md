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
- **The TopBar must remain visible and persistent across ALL screens.** Define it once in `MainActivity` Scaffold and pass it to every screen — do not redefine per screen.

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
- FAB's add action must call `viewModel.addTask(...)` → which calls `repository.insert(task)` → which calls `dao.insert(task)`. This chain must be strictly followed — UI never touches DAO or Repository directly.

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

---

## Modern Android Best Practices (Required)

The following practices are **mandatory** for a production-quality, modern, responsive Android app. The agent must apply all of them without being explicitly asked per file.

---

### Edge-to-Edge & WindowInsets
- Enable edge-to-edge rendering in `MainActivity`:
  ```kotlin
  WindowCompat.setDecorFitsSystemWindows(window, false)
  ```
- Apply `Modifier.windowInsetsPadding(WindowInsets.systemBars)` or use `Scaffold`'s built-in inset handling (`contentWindowInsets`) so content is never hidden behind the status bar or navigation bar.
- Do NOT hardcode top/bottom padding to compensate for system bars.

---

### Dark Mode & Dynamic Color
- `Theme.kt` must support both light and dark themes using `isSystemInDarkTheme()`.
- On Android 12+ (API 31+), enable **Dynamic Color** with `dynamicLightColorScheme` / `dynamicDarkColorScheme` as the primary scheme, falling back to the custom palette below API 31.
- Example structure:
  ```kotlin
  @Composable
  fun TaskManagerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
      val colorScheme = when {
          Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
              if (darkTheme) dynamicDarkColorScheme(LocalContext.current)
              else dynamicLightColorScheme(LocalContext.current)
          }
          darkTheme -> DarkColorScheme
          else -> LightColorScheme
      }
      MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
  }
  ```

---

### Accessibility
- Every icon-only `IconButton` must have a `contentDescription`:
  ```kotlin
  IconButton(onClick = { ... }) {
      Icon(Icons.Default.Add, contentDescription = "Add new task")
  }
- Every `Image` composable must have a meaningful or null `contentDescription`.
- `TaskCard` composable should use `Modifier.semantics { contentDescription = "Task: ${task.title}" }`.
- Minimum touch target size: `48.dp × 48.dp` for all interactive elements.

---

### Empty State UI
- In `TaskListScreen`, when `tasks.isEmpty()`, show an **empty state composable** instead of a blank `LazyColumn`:
  ```kotlin
  if (tasks.isEmpty()) {
      EmptyStateView(message = "No tasks yet. Tap + to add one!")
  } else {
      LazyColumn { ... }
  }
  ```
- `EmptyStateView`: centered column with an icon (e.g., `Icons.Default.CheckCircle`), a bold message, and a muted sub-label.

---

### Input Validation in AddEditTaskDialog
- The **Save/Confirm button must be disabled** if title is blank:
  ```kotlin
  val isSaveEnabled = title.isNotBlank()
  Button(onClick = { ... }, enabled = isSaveEnabled) { Text("Save") }
  ```
- Show a red helper text `"Title cannot be empty"` below the title `OutlinedTextField` if the user taps save with a blank title.
- `dueDate` field should use a `DatePickerDialog` or at minimum validate format as non-empty.

---

### Compose Preview Annotations
- Every composable must have at least one `@Preview` function directly below it:
  ```kotlin
  @Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO, name = "Light")
  @Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Dark")
  @Composable
  fun TaskCardPreview() {
      TaskManagerTheme { TaskCard(task = Task(1, "Sample", "Desc", "2026-03-11"), ...) }
  }
  ```
- Previews must cover both light and dark modes.

---

### Navigation — NavHost & NavController
- Use `rememberNavController()` in `MainActivity`.
- Define routes as a sealed class or `object`:
  ```kotlin
  sealed class Screen(val route: String) {
      object TaskList : Screen("task_list")
      object ManageTasks : Screen("manage_tasks")
  }
  ```
- `NavHost` in `BottomNavGraph.kt` maps each route to its screen composable.
- `BottomNavigationItem` uses `navController.navigate(screen.route)` with `launchSingleTop = true` and `restoreState = true`.

---

### Room Database Singleton
- `TaskDatabase` must use a thread-safe singleton:
  ```kotlin
  companion object {
      @Volatile private var INSTANCE: TaskDatabase? = null
      fun getDatabase(context: Context): TaskDatabase {
          return INSTANCE ?: synchronized(this) {
              Room.databaseBuilder(context.applicationContext, TaskDatabase::class.java, "task_db")
                  .fallbackToDestructiveMigration()
                  .build().also { INSTANCE = it }
          }
      }
  }
  ```

---

### Single Responsibility Composables
- Each composable file must do exactly **one thing**:
  - `TaskCard.kt` → renders one task card only
  - `TopBar.kt` → renders the app bar only
  - `AddEditTaskDialog.kt` → renders the add/edit dialog only
- If a composable exceeds ~80 lines, split it into sub-composables in the same file or a new component file.
- No business logic (DB calls, state management) inside composable functions — all state lives in ViewModel.

---

### Loading / Error UiState
- Define a sealed class for UI state in `TaskViewModel.kt`:
  ```kotlin
  sealed class TaskUiState {
      object Loading : TaskUiState()
      data class Success(val tasks: List<Task>) : TaskUiState()
      data class Error(val message: String) : TaskUiState()
  }
  ```
- `TaskListScreen` should show a `CircularProgressIndicator` while state is `Loading`.
- Wrap DB operations in `try/catch` inside `viewModelScope.launch` and emit `Error` state on failure.

---


---

## Spacing, Breathing Room & Responsive Layout

The current spec risks producing a cramped, claustrophobic UI. The following rules are **mandatory** to ensure the app feels open, airy, and comfortable to use.

---

### Global Spacing Scale

Define a spacing scale in a dedicated `Spacing.kt` file and use it everywhere. Never use arbitrary `dp` values inline.

```kotlin
// ui/theme/Spacing.kt
object Spacing {
    val xxs = 4.dp
    val xs  = 8.dp
    val sm  = 12.dp
    val md  = 16.dp
    val lg  = 24.dp
    val xl  = 32.dp
    val xxl = 48.dp
}
```

Reference via `Spacing.md`, `Spacing.lg`, etc. in all composables.

---

### Screen-Level Padding
- Every screen composable must have a **horizontal content padding of at least `Spacing.lg` (24.dp)** on both sides.
- Apply via `Modifier.padding(horizontal = Spacing.lg)` on the outermost `Column` or `LazyColumn` content padding.
- `LazyColumn` must use `contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.md)`.
- **Never** set horizontal padding to less than `16.dp` anywhere on screen content.

---

### Card Internal Padding
- All `Card` / `Surface` composables must have **internal padding of at least `Spacing.lg` (24.dp)** on all sides:
  ```kotlin
  Card(...) {
      Column(modifier = Modifier.padding(Spacing.lg)) { ... }
  }
  ```
- Do NOT use `Spacing.md (16.dp)` or less as card internal padding — it feels tight.

---

### Vertical Spacing Between Elements
- Between cards in `LazyColumn`: `Spacer(modifier = Modifier.height(Spacing.sm))` — minimum `12.dp`.
- Between a card's internal elements (title → subtitle → chips): `Spacer(modifier = Modifier.height(Spacing.xs))` — `8.dp`.
- Between screen sections (e.g., hero card → status row → task list header): `Spacer(modifier = Modifier.height(Spacing.lg))` — `24.dp`.
- Between the TopBar and first content element: `Spacer(modifier = Modifier.height(Spacing.md))` — `16.dp`.

---

### Typography Line Height & Letter Spacing
- Hero text (32–40sp): `lineHeight = 44.sp`, `letterSpacing = (-0.5).sp` — tighter tracking for large display text.
- Screen titles (24–28sp): `lineHeight = 34.sp`, `letterSpacing = (-0.25).sp`.
- Body text (14sp): `lineHeight = 22.sp`, `letterSpacing = 0.sp` — extra line height improves readability.
- Chip/label text (11–12sp): `lineHeight = 16.sp`, `letterSpacing = 0.3.sp` — slightly open tracking for small text.

---

### Card Height & Min Touch Targets
- Task cards: **minimum height `120.dp`** — never let a card be shorter than this.
- Status chips / filter chips: **height `36.dp`**, horizontal padding `Spacing.md (16.dp)` inside.
- All tappable elements: **minimum `48.dp × 48.dp`** touch target (use `Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)`).
- Icon buttons: size `40.dp` visual, wrapped in `48.dp` touch area via padding.

---

### LazyColumn Item Spacing
Use `verticalArrangement` on `LazyColumn` for consistent gaps, NOT manual `Spacer` inside each item:
```kotlin
LazyColumn(
    contentPadding = PaddingValues(horizontal = Spacing.lg, vertical = Spacing.lg),
    verticalArrangement = Arrangement.spacedBy(Spacing.sm)
) {
    items(tasks) { task -> TaskCard(task = task, ...) }
}
```

---

### Hero / Summary Card
- Minimum height: `160.dp`
- Internal padding: `Spacing.xl (32.dp)` top/bottom, `Spacing.lg (24.dp)` left/right
- Title font size: `36sp`, `ExtraBold`, line height `44.sp`
- Leave deliberate whitespace between the greeting line and the task count headline

---

### Schedule Timeline (ManageTasksScreen)
- Left time column width: fixed `52.dp` with right-padding `Spacing.sm (12.dp)`
- Gap between time column and task block: `Spacing.xs (8.dp)`
- Task block height: minimum `52.dp`, pill shape
- Vertical gap between timeline rows: `Spacing.lg (24.dp)` — one row per hour feels spacious
- Task blocks must NOT touch the screen edges — leave `Spacing.md (16.dp)` right margin

---

### Week Calendar Row
- Each day item: minimum `44.dp × 56.dp` (width × height)
- Gap between day items: `Spacing.xs (8.dp)`
- Selected date circle: `40.dp` diameter, not smaller
- Top padding above calendar row: `Spacing.md (16.dp)`
- Bottom padding below calendar row: `Spacing.lg (24.dp)` before the timeline begins

---

### Bottom Navigation Bar
- Height: `72.dp` (taller than default `56.dp`) for a less cramped feel
- Icon size: `24.dp`
- Vertical padding inside bar: `Spacing.sm (12.dp)` top and bottom
- Selected pill indicator: `height = 40.dp`, `width = 64.dp`, fully rounded

---

### Dialog (AddEditTaskDialog)
- Dialog width: `fillMaxWidth()` with `Modifier.padding(horizontal = Spacing.lg)`
- Internal padding: `Spacing.xl (32.dp)` on all sides
- Gap between each `OutlinedTextField`: `Spacer(Modifier.height(Spacing.md))`
- `OutlinedTextField` must have `modifier = Modifier.fillMaxWidth()`
- Action button row: `Spacer(Modifier.height(Spacing.lg))` above buttons

---

### Anti-Patterns to Avoid
- ❌ Do NOT stack multiple items without any `Spacer` between them
- ❌ Do NOT use `padding(4.dp)` or `padding(8.dp)` as card internal padding
- ❌ Do NOT let text sit flush against a card edge — always `>= 16.dp` from any edge
- ❌ Do NOT make clickable rows shorter than `48.dp`
- ❌ Do NOT use `fillMaxSize()` on a `Column` without `verticalScroll` if content might overflow
- ❌ Do NOT place two bold large-text elements next to each other without visual separation

---


---

## Animations & Motion

All animations must feel **native, purposeful, and consistent** with the existing UI's clean minimal aesthetic. No jarring transitions, no over-engineered effects. Motion should reinforce the app's structure — not distract from it.

---

### Animation Principles
- **Subtle over flashy:** Use short durations (150–350ms). Never exceed 400ms for UI transitions.
- **Easing:** Use `FastOutSlowInEasing` for elements entering the screen. Use `LinearOutSlowInEasing` for exits.
- **Purposeful:** Every animation must communicate something — a state change, a navigation event, or a user action result.
- **Consistent:** All cards, chips, and dialogs use the same motion language.

---

### Screen Transitions (Navigation)

In `BottomNavGraph.kt`, apply animated transitions on `NavHost` using `AnimatedNavHost` or Compose Navigation's `enterTransition` / `exitTransition`:

```kotlin
NavHost(
    navController = navController,
    startDestination = Screen.TaskList.route,
    enterTransition = {
        fadeIn(animationSpec = tween(250, easing = FastOutSlowInEasing)) +
        slideInHorizontally(initialOffsetX = { it / 8 }, animationSpec = tween(250))
    },
    exitTransition = {
        fadeOut(animationSpec = tween(200)) +
        slideOutHorizontally(targetOffsetX = { -it / 8 }, animationSpec = tween(200))
    }
)
```

- Tab switches: **fade + slight horizontal slide** (offset only 1/8 of screen width — not a full slide, just a nudge)
- Back navigation: reverse direction slide

---

### Task Cards — Entry Animation (LazyColumn)

Each `TaskCard` in the `LazyColumn` must animate in when first composed using `AnimatedVisibility` or `animateItemPlacement`:

```kotlin
// In LazyColumn:
items(tasks, key = { it.taskId }) { task ->
    TaskCard(
        task = task,
        modifier = Modifier.animateItem(
            fadeInSpec = tween(300),
            placementSpec = spring(stiffness = Spring.StiffnessMediumLow)
        )
    )
}
```

- New task added → card **slides down + fades in** from the top of the list
- Task deleted → card **shrinks vertically + fades out** before the list reflows
- Use `spring(stiffness = Spring.StiffnessMediumLow)` for the reflow — gives a gentle settle

---

### FAB — Scale Animation on Appear

The FAB should scale in when the Tasks screen is visible and scale out when navigating away:

```kotlin
AnimatedVisibility(
    visible = currentScreen == Screen.TaskList,
    enter = scaleIn(animationSpec = tween(200, easing = FastOutSlowInEasing)) + fadeIn(tween(200)),
    exit = scaleOut(animationSpec = tween(150)) + fadeOut(tween(150))
) {
    FloatingActionButton(onClick = { showDialog = true }) {
        Icon(Icons.Default.Add, contentDescription = "Add task")
    }
}
```

---

### Add/Edit Dialog — Enter & Exit

`AddEditTaskDialog` must not pop in abruptly. Wrap it in `AnimatedVisibility`:

```kotlin
AnimatedVisibility(
    visible = showDialog,
    enter = fadeIn(tween(200)) + scaleIn(initialScale = 0.92f, animationSpec = tween(220, easing = FastOutSlowInEasing)),
    exit  = fadeOut(tween(150)) + scaleOut(targetScale = 0.92f, animationSpec = tween(150))
) {
    AddEditTaskDialog(...)
}
```

- Scale origin: `0.92f → 1.0f` on enter (grows slightly from center)
- On dismiss: reverse (`1.0f → 0.92f`) + fade out

---

### Status Chips — Selection State Animation

When a status chip (`In progress`, `In review`, `On hold`) is selected/deselected, animate the background color:

```kotlin
val chipColor by animateColorAsState(
    targetValue = if (selected) LimeGreen else ChipBackground,
    animationSpec = tween(200)
)
Surface(color = chipColor, shape = RoundedCornerShape(50)) { ... }
```

---

### Task Card — Press / Ripple Feedback

- All `TaskCard` composables must use `Modifier.clickable(...)` with the default ripple — do NOT disable the ripple.
- On long-press or edit button tap: apply a subtle **scale-down** press effect:
```kotlin
val interactionSource = remember { MutableInteractionSource() }
val isPressed by interactionSource.collectIsPressedAsState()
val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.97f else 1f,
    animationSpec = spring(stiffness = Spring.StiffnessHigh)
)
Card(modifier = Modifier.scale(scale)) { ... }
```

---

### Calendar Day Selection Animation (WeekCalendarRow)

When a new day is tapped in the `WeekCalendarRow`:

```kotlin
val bgColor by animateColorAsState(
    targetValue = if (isSelected) LimeGreen else Color.Transparent,
    animationSpec = tween(180)
)
val textColor by animateColorAsState(
    targetValue = if (isSelected) DarkSurface else MutedText,
    animationSpec = tween(180)
)
Box(modifier = Modifier.background(bgColor, CircleShape)) {
    Text(dayNum, color = textColor)
}
```

---

### Bottom Navigation — Selected Indicator Animation

Animate the pill indicator sliding between tabs:

```kotlin
val indicatorOffset by animateDpAsState(
    targetValue = selectedTabIndex * tabWidth,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
)
Box(modifier = Modifier.offset(x = indicatorOffset).width(tabWidth).height(40.dp)
    .background(Color.White, RoundedCornerShape(50)))
```

- Use `Spring.DampingRatioMediumBouncy` for a slight, satisfying bounce when switching tabs.

---

### Schedule Task Block — Staggered Entry

In `ManageTasksScreen`, task blocks on the timeline should stagger their appearance:

```kotlin
tasks.forEachIndexed { index, task ->
    val visible = remember { MutableTransitionState(false).apply { targetState = true } }
    AnimatedVisibility(
        visibleState = visible,
        enter = fadeIn(tween(durationMillis = 250, delayMillis = index * 60)) +
                slideInHorizontally(initialOffsetX = { -40 }, animationSpec = tween(250, delayMillis = index * 60))
    ) {
        ScheduleTaskBlock(task = task)
    }
}
```

- Each block slides in from the left with a `60ms` stagger between items — creates a cascade effect that matches the timeline layout.

---

### Hero Summary Card — Number Count-Up

The task count in the `HeroSummaryCard` ("You have **N** tasks") should animate when the count changes:

```kotlin
val animatedCount by animateIntAsState(
    targetValue = taskCount,
    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
)
Text("You have $animatedCount tasks for today", style = HeroTextStyle)
```

---

### Required Imports

Add these to any file using animations:

```kotlin
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.scale
```

---

### Animation Anti-Patterns to Avoid
- ❌ Do NOT use `duration > 400ms` for any UI transition
- ❌ Do NOT animate size/position of the entire screen — only individual elements
- ❌ Do NOT use `LinearEasing` for enter animations — always use `FastOutSlowInEasing`
- ❌ Do NOT disable ripple on clickable elements
- ❌ Do NOT use bouncy spring on dialog entry — only on navigation indicators
- ❌ Do NOT animate decorative-only elements (background patterns, static icons)

---

