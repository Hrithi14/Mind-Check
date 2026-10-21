# MIND CHECK — Project Specification & Architecture Proposal

> **Document Type:** Project Specification & Architecture Proposal  
> **Status:** Proposal — Pending User Approval  
> **Version:** 1.0.0 (Phase 0)  
> **Target Platform:** Native Android (Kotlin + Jetpack Compose)  
> **Classification:** Non-clinical wellness tracking & self-awareness tool  

---

## 1. Project Objective & Problem Statement

### 1.1 Objective
**Mind Check** is a native Android application designed to help university and college students monitor, understand, and improve their everyday mental wellbeing. The application acts as a personal, compassionate, and proactive self-awareness companion that enables students to observe patterns across their mood, stress levels, sleep hygiene, energy, and smartphone usage habits.

### 1.2 Problem Statement
Students face unprecedented levels of academic pressure, irregular sleep schedules, digital overstimulation, and social anxiety. Most existing solutions either:
1. Impose clinical, intimidating medical terminology or assessments that alienate users.
2. Require tedious, multi-step manual logging that causes high attrition within days.
3. Fail to connect digital screen habits with emotional burnout and exhaustion.
4. Compromise student privacy by aggressively syncing deeply personal emotional logs to remote servers.

### 1.3 Core Philosophical Principle
**Awareness without Diagnosis:** Mind Check **never** claims to diagnose, treat, or prevent clinical psychological disorders (such as Clinical Depression, Generalized Anxiety Disorder, or Bipolar Disorder). It is strictly an informational, lifestyle-oriented awareness tool designed to detect early burnout trends and encourage timely self-care, healthy boundaries, and professional support seeking.

---

## 2. Target Users & Value Proposition

### 2.1 Primary Audience
- **Undergraduate & Postgraduate Students:** Experiencing deadline crunches, exam stress, irregular circadian rhythms, and study fatigue.
- **Young Adults & Self-Improvers:** Seeking actionable daily check-ins to curb digital distractions and build sustainable micro-habits.

### 2.2 User Personas
1. **The Overwhelmed Crammer (Alex, 20):** Stays up until 3:00 AM scrolling social media before exams, wakes up fatigued, and doesn't connect late-night screen time with next-day anxiety spikes.
2. **The High-Achiever on the Edge (Priya, 22):** Juggles coursework, internships, and societies; ignores creeping exhaustion until complete burnout occurs. Needs lightweight 60-second micro-interventions.
3. **The Reluctant Seeker (Jordan, 19):** Hesitant to visit campus counseling due to perceived stigma, but wants a private, local space to log emotions and find verified campus/emergency support numbers when overwhelmed.

### 2.3 Value Proposition
- **Frictionless 30-Second Check-ins:** Micro-interactions built with Jetpack Compose for effortless logging.
- **Holistic Contextual Correlation:** Connects digital screen habits, sleep duration, and daily stress into meaningful trends.
- **Immediate Grounding in Crises:** Provides "Urge Mode" and "60-Second Reset" for acute stress surges.
- **Privacy-First Sovereignty:** All personal thoughts and emotional logs remain on the student's physical device by default.

---

## 3. Technology Stack

| Category | Technology | Rationale & Selection Criteria |
| :--- | :--- | :--- |
| **Language** | Kotlin 2.0+ | Modern, concise, memory-safe, coroutine-native language standard for Android. |
| **UI Toolkit** | Jetpack Compose | Declarative UI toolkit enabling fluid animations, reactive state, and rapid iteration. |
| **Design System** | Material Design 3 (Material You) | Dynamic theming, accessible typography, student-friendly modern ergonomics. |
| **Architecture** | MVVM + Clean Architecture Principles | Separation of concerns: Presentation (Compose + ViewModel) $\to$ Domain (UseCases) $\to$ Data (Repository + Data Sources). |
| **Local Database** | Room (SQLite ORM) with KSP | Robust local persistence, type-safe queries, relational data support, Coroutine/Flow integration. |
| **Key-Value Storage** | Jetpack Preferences DataStore | Asynchronous, transactional storage for onboarding flags, user preferences, and theme choices. |
| **Dependency Injection** | Dagger Hilt | Compile-time dependency injection tailored for Android Jetpack components. |
| **Async & Reactive** | Kotlin Coroutines & Flow (`StateFlow`, `SharedFlow`) | Reactive state streams with lifecycle-aware UI collection. |
| **Navigation** | Jetpack Navigation Compose | Type-safe declarative backstack and screen navigation. |
| **System Integrations** | Android `UsageStatsManager` | Reads screen time and app usage metrics with explicit user permission. |
| **Cloud (Post-MVP)** | Firebase Auth & Cloud Firestore | Optional authenticated backup, opt-in peer accountability, and push notifications. |
| **Tooling & IDE** | Android Studio (Koala / Ladybug or latest stable) + Android SDK | Gradle Kotlin DSL (`build.gradle.kts`), Version Catalogs (`libs.versions.toml`), Android Emulator. |

---

## 4. Proposed Android Project Architecture

The application adopts **Clean Architecture with MVVM (Model-View-ViewModel)** to guarantee testability, maintainability, and clean separation between UI components and business rules.

```
                  ┌──────────────────────────────────────────────┐
                  │              PRESENTATION LAYER              │
                  │  Jetpack Compose Screens & Reusable UI Kits   │
                  │              Material 3 Theming              │
                  └───────────────────────┬──────────────────────┘
                                          │ Observes UIState (StateFlow)
                                          │ Emits UIEvent
                                          ▼
                  ┌──────────────────────────────────────────────┐
                  │                 VIEWMODELS                   │
                  │  State Holders, Presentation Logic, Coroutines│
                  └───────────────────────┬──────────────────────┘
                                          │ Invokes Use Cases
                                          ▼
                  ┌──────────────────────────────────────────────┐
                  │                 DOMAIN LAYER                 │
                  │     Use Cases / Interactors, Domain Models    │
                  │      (Pure Kotlin — No Android Framework)    │
                  └───────────────────────┬──────────────────────┘
                                          │ Calls Repository Interfaces
                                          ▼
                  ┌──────────────────────────────────────────────┐
                  │                  DATA LAYER                  │
                  │   Repository Implementations, Room Entities,  │
                  │  Room DAOs, DataStore, System Managers       │
                  └───────────────────────┬──────────────────────┘
                                          │
                        ┌─────────────────┴─────────────────┐
                        ▼                                   ▼
             ┌─────────────────────┐             ┌─────────────────────┐
             │ Local Storage (Room)│             │ System APIs         │
             │ SQLite / DataStore  │             │ (UsageStatsManager) │
             └─────────────────────┘             └─────────────────────┘
```

### 4.1 Recommended Package Structure

```
com.mindcheck.app/
│
├── core/
│   ├── designsystem/          # Material 3 Color Schemes, Typography, Shapes, Common Components
│   ├── common/                # Result wrappers, Dispatcher providers, Constants, Extensions
│   └── util/                  # Date/Time formatters, Calculation utilities
│
├── data/
│   ├── local/
│   │   ├── database/          # MindCheckDatabase, TypeConverters, Migrations
│   │   ├── dao/               # MoodDao, SleepDao, HabitDao, JournalDao
│   │   ├── entity/            # Room Entity definitions
│   │   └── datastore/         # UserPreferencesDataStore
│   ├── repository/            # Repository implementations (e.g., MoodRepositoryImpl)
│   └── system/                # ScreenTimeDataSource (UsageStatsManager wrapper)
│
├── domain/
│   ├── model/                 # Pure domain data classes (MoodEntry, SleepSession, WellbeingSnapshot)
│   ├── repository/            # Abstract repository interfaces
│   └── usecase/               # Single-responsibility use cases
│       ├── CalculateWellbeingScoreUseCase.kt
│       ├── GetDailyInsightsUseCase.kt
│       ├── RecordCheckInUseCase.kt
│       └── GetScreenTimeSummaryUseCase.kt
│
├── presentation/
│   ├── navigation/            # MindCheckNavHost, Screen routes, Navigation graphs
│   ├── dashboard/             # Home Dashboard Screen, ViewModel, UI State
│   ├── checkin/               # Mood & Stress Check-in Screen, ViewModel, UI State
│   ├── sleep/                 # Sleep & Energy Tracker Screen, ViewModel
│   ├── screentime/            # Screen Time breakdown Screen, ViewModel
│   ├── insights/              # Trends, Charts, Correlation Screen, ViewModel
│   ├── habits/                # Goals and Habits Screen, ViewModel
│   ├── reset/                 # 60-Second Reset (Breathing guide)
│   ├── urgemode/              # Acute Urge Grounding Screen (5-4-3-2-1 exercise)
│   ├── support/               # Helpline Directory & Crisis Contacts
│   ├── companion/             # AI Wellness Companion (Conversational empathetic listener)
│   └── buddy/                 # Optional Accountability Buddy (Future enhancement)
│
└── di/                        # Hilt Modules (DatabaseModule, RepositoryModule, AppModule)
```

---

## 5. Planned Application Screens & Responsibilities

| # | Screen Name | Route | Core Responsibilities |
| :--- | :--- | :--- | :--- |
| 1 | **Onboarding & Consent** | `onboarding_flow` | Welcome carousel, non-diagnostic disclaimer, explicit permission disclosures (Screen usage, notification schedule), and emergency safety notice. |
| 2 | **Home Dashboard** | `home_dashboard` | Daily wellbeing summary, quick-action check-in launcher, today's score card, quick habit checklist, and one-tap emergency reset button. |
| 3 | **Daily Check-In** | `daily_checkin` | Visual mood slider/emoji picker (1–5 scale), stress meter (1–10 scale), emotion tag chips (e.g., anxious, proud, exhausted), and optional brief text notes. |
| 4 | **Sleep & Energy Tracker** | `sleep_tracker` | Bedtime/wake-up time logger, sleep quality rating (Poor to Restful), subjective energy gauge, and sleep debt visualization. |
| 5 | **Screen Usage Insights** | `screen_usage` | Visual breakdown of daily screen time, category highlights (Social, Gaming, Productivity), and pick-up frequency (requires `PACKAGE_USAGE_STATS`). |
| 6 | **Wellbeing Insights & Trends** | `insights_trends` | 7-day and 30-day trend charts, mood vs. sleep correlations, screen time impact overlays, and positive habit completion rate. |
| 7 | **Goals & Habits** | `habits_goals` | Creation and tracking of daily micro-habits (e.g., "Drink 2L water", "10-minute walk", "No screen 30m before bed") with streak counters. |
| 8 | **60-Second Reset** | `sixty_second_reset` | Interactive box breathing or 4-7-8 breathing animation with haptic feedback and gentle ambient audio cues to alleviate acute tension. |
| 9 | **Urge Mode (SOS Grounding)** | `urge_mode` | High-stress / panic grounding tool (5-4-3-2-1 sensory technique), physical tension release prompts, and immediate de-escalation steps. |
| 10 | **Get Support** | `get_support` | Direct one-touch calling for national crisis hotlines, regional student helplines, campus counseling centers, and safety planning guide. |
| 11 | **AI Wellness Companion** | `ai_companion` | On-device / cloud assisted empathetic reflective listener with strict non-clinical safety rails, offering journaling prompts and supportive dialogue. |
| 12 | **Accountability Buddy** | `accountability_buddy` | *(Future Phase)* Optional peer link allowing mutual check-in nudges and shared streak achievements without revealing raw private journals. |
| 13 | **Settings & Data Sovereignty** | `settings_privacy` | Export data to JSON/CSV, delete all local records, toggle system permissions, adjust notification quiet hours, and re-read medical disclaimers. |

---

## 6. Detailed Feature Breakdown

### 6.1 Daily Mood and Stress Check-In
- **Input Modalities:** Single-screen flow under 30 seconds.
  - Primary Mood Index: 5-level animated icon selector (Awful, Down, Neutral, Good, Great).
  - Stress Scale: Continuous or discrete 1–10 slider with color transition (Green $\to$ Yellow $\to$ Crimson).
  - Contextual Tags: Tags covering Academic, Social, Family, Sleep, Health, and Digital overstimulation.
  - Reflective Thought: Optional single-line or multi-line encrypted text entry.
- **Cadence:** Up to 3 check-ins per day (Morning intention, Afternoon pulse, Evening reflection) or a single consolidated daily log.

### 6.2 Sleep and Energy Tracking
- **Parameters:** Sleep start time, sleep end time, calculated duration, restfulness score (1–5), and morning physical energy level (1–5).
- **Heuristic Indicators:** Detects sudden drops in sleep duration (< 5 hours) or prolonged sleep deficits over 3 consecutive days.

### 6.3 Home Dashboard
- **Hero Element:** Today’s Wellbeing Index dial / status ring.
- **Dynamic Greeting:** Contextual time-of-day encouragement.
- **Action Hub:** "Check In Today", "Quick Breathe", "Urge SOS", and "View Trends".
- **Habit Progress:** Visual progress ring for daily completed micro-goals.

### 6.4 Screen Usage Tracking
- **Mechanism:** Integrates with Android's native `UsageStatsManager`.
- **Metrics Collected:** Total daily screen-on time, unlocked counts, and breakdown of time spent in user-flagged distracting app categories.
- **Privacy Boundary:** Mind Check **never** inspects app content, keystrokes, messages, or URLs—only aggregate duration per package name.
- **Graceful Fallback:** If permission is denied or revoked by the user, the app offers manual screen-time estimation without blocking any other feature.

### 6.5 Wellbeing Score (Heuristic Awareness Index)
> **CRITICAL COMPLIANCE NOTICE:** The Wellbeing Score is an algorithmic heuristic for personal reflection. It is **NOT** a clinically validated psychometric tool (such as PHQ-9 or GAD-7) and does **NOT** diagnose clinical states.
- **Formula Foundation:**
  $$\text{Wellbeing Score} = w_m \cdot M + w_s \cdot S + w_e \cdot E + w_u \cdot U + w_h \cdot H$$
  Where:
  - $M$: Normalized mood and inverse stress score (0–100).
  - $S$: Sleep duration and quality score compared to a 7–9 hour target (0–100).
  - $E$: Energy level rating (0–100).
  - $U$: Healthy digital usage score (rewards balanced screen time) (0–100).
  - $H$: Daily habit completion percentage (0–100).
- **Scale:** 0 to 100 with qualitative tiers:
  - `80 - 100`: *Thriving & Balanced*
  - `60 - 79`: *Steady & Managing*
  - `40 - 59`: *Experiencing Strain*
  - `0 - 39`: *Depleted — Time for Self-Care & Reset*

### 6.6 Personal Insights & Correlation Engine
- **Pattern Detection (Rule-based local heuristics):**
  - "When your screen time exceeds 4.5 hours after 10 PM, your next-day mood average drops by 25%."
  - "Consistent 7+ hour sleep over the last 4 days correlated with your lowest reported stress."
- **Privacy:** Computed 100% locally on the device; no logs are uploaded to any server to generate insights.

### 6.7 Goals and Habits
- **Design:** Micro-habits that take under 10 minutes to finish, avoiding overwhelming task lists.
- **Default Catalog:**
  - "Drink a full glass of water upon waking"
  - "Step outside for 10 minutes of sunlight"
  - "Digital sunset: screens off 30 minutes before sleep"
  - "10-minute uninterrupted study block"
- **Streaks & Encouragement:** Positive reinforcement streaks without punitive gamification.

### 6.8 60-Second Reset
- **Purpose:** Fast somatic de-escalation for exam anxiety or cognitive overload.
- **Implementation:** Visual pulsating circle guiding breathing rhythms (Box Breathing: 4s Inhale, 4s Hold, 4s Exhale, 4s Hold).
- **Sensory:** Subtle rhythmic haptic vibrations (vibrator service) allowing eyes-closed usage.

### 6.9 Urge Mode (Emergency Grounding)
- **Purpose:** Acute crisis intervention when students feel trapped by sudden panic, emotional spikes, or destructive impulsive urges.
- **Activities:**
  - 5-4-3-2-1 Sensory Grounding: Step-by-step interactive cards (5 things you see, 4 you can touch, 3 you can hear, 2 you can smell, 1 you can taste).
  - Cold Splash / Physical Shakeout reminder.
  - Immediate direct link to crisis numbers.

### 6.10 Get Support
- **Contents:**
  - National Crisis Lifelines (e.g., 988 in North America, Tele-MANAS/KIRAN in India, regional equivalents configurable by country).
  - Free 24/7 Crisis Text Lines.
  - Campus Counseling directory template (customizable campus phone/email).
  - Clear emergency guidance: "If you or someone you know is in immediate danger, call local emergency services immediately."

### 6.11 AI Wellness Companion (Planned Staged Rollout)
- **Role:** Non-clinical reflective listening and guided journaling assistant.
- **Safety Guardrails:**
  - Hardened system prompt: Strict instructions that it is an empathetic AI listener, not a doctor or therapist.
  - Immediate trigger detection: Any mention of self-harm, suicide, or severe crisis immediately short-circuits the AI response and displays emergency lifeline buttons.
- **Delivery:** Prototype will start with local structured reflective prompts; conversational LLM capabilities will be introduced with safety boundary tests.

### 6.12 Optional Accountability Buddy
- **Role:** Peer-to-peer encouragement for habits and daily check-in consistency.
- **Privacy Shield:** Buddies can see check-in streaks (e.g., "Alex completed their daily check-in!"), but **never** see mood scores, journal text, or specific stress notes.

---

## 7. Data Privacy, Safety & User Consent Requirements

### 7.1 Local-First Architecture
All sensitive user inputs—including mood ratings, personal journal notes, stress levels, and habit records—are stored exclusively in the local Room database on the physical device.

### 7.2 Explicit Permissions & Transparency
- **`PACKAGE_USAGE_STATS`:** The app will clearly inform users why screen usage tracking is requested, how it is used solely on-device to compute digital wellbeing metrics, and provide a clear one-tap path to system settings. The app must function completely if this permission is withheld.
- **`POST_NOTIFICATIONS` (Android 13+):** Used exclusively for gentle user-scheduled check-in reminders. No spam or commercial pushes.

### 7.3 Medical Disclaimer & Non-Diagnostic Guarantee
- Prominently shown during first launch onboarding and accessible at all times in Settings:
  > *"Mind Check is a self-care awareness application and is not a medical device. It does not provide medical advice, diagnosis, or clinical treatment. If you are experiencing mental health crises or severe distress, please consult a healthcare professional or contact emergency services."*

### 7.4 Data Sovereignty
- **Data Export:** User can export their history to a human-readable JSON or CSV file at any time.
- **Nuke Option (Right to be Forgotten):** A single "Erase All App Data" button cleanly wipes the Room database, DataStore preferences, and caches.

---

## 8. Proposed Local and Cloud Data Storage Strategy

### 8.1 Local Storage (Room Database Schema)

```
┌─────────────────────────────────┐       ┌─────────────────────────────────┐
│           MoodEntry             │       │          SleepEntry             │
├─────────────────────────────────┤       ├─────────────────────────────────┤
│ id: Long (PK, AutoGenerate)     │       │ id: Long (PK, AutoGenerate)     │
│ timestamp: Long                 │       │ date: String (YYYY-MM-DD)       │
│ moodLevel: Int (1 - 5)          │       │ bedTime: Long                   │
│ stressLevel: Int (1 - 10)       │       │ wakeTime: Long                  │
│ tags: List<String> (Converter)  │       │ durationMinutes: Int            │
│ note: String?                   │       │ qualityRating: Int (1 - 5)      │
│ createdAt: Long                 │       │ energyRating: Int (1 - 5)       │
└─────────────────────────────────┘       └─────────────────────────────────┘

┌─────────────────────────────────┐       ┌─────────────────────────────────┐
│             Habit               │       │          HabitLog               │
├─────────────────────────────────┤       ├─────────────────────────────────┤
│ id: Long (PK, AutoGenerate)     │       │ id: Long (PK, AutoGenerate)     │
│ title: String                   │       │ habitId: Long (FK -> Habit.id)  │
│ description: String             │       │ date: String (YYYY-MM-DD)       │
│ category: String                │       │ isCompleted: Boolean            │
│ targetPerWeek: Int              │       │ completedAt: Long?              │
│ isArchived: Boolean             │       └─────────────────────────────────┘
│ createdAt: Long                 │
└─────────────────────────────────┘

┌─────────────────────────────────┐       ┌─────────────────────────────────┐
│        WellbeingSnapshot        │       │       ScreenUsageSummary        │
├─────────────────────────────────┤       ├─────────────────────────────────┤
│ id: Long (PK, AutoGenerate)     │       │ id: Long (PK, AutoGenerate)     │
│ date: String (YYYY-MM-DD, Unique│       │ date: String (YYYY-MM-DD, Unique│
│ overallScore: Int (0 - 100)     │       │ totalScreenTimeMinutes: Long    │
│ moodScore: Int                  │       │ unlockCount: Int                │
│ sleepScore: Int                 │       │ topDistractingApps: String(JSON)│
│ digitalScore: Int               │       │ calculatedAt: Long              │
│ habitScore: Int                 │       └─────────────────────────────────┘
└─────────────────────────────────┘
```

### 8.2 Key-Value Storage (Jetpack Preferences DataStore)
- `IS_ONBOARDING_COMPLETED` (Boolean)
- `USER_DISPLAY_NAME` (String)
- `DAILY_REMINDER_TIME` (String, e.g., "20:30")
- `SCREEN_TIME_PERMISSION_DISMISSED` (Boolean)
- `PREFERRED_THEME_MODE` (System / Dark / Light)
- `CAMPUS_SUPPORT_NAME` & `CAMPUS_SUPPORT_PHONE` (Strings)

### 8.3 Future Cloud Strategy (Phase 6+)
- **Firebase Auth:** Optional anonymous sign-in or Google SSO for multi-device sync.
- **Cloud Firestore:** Encrypted backup of user logs with client-side or rule-based protections.
- **Firebase Cloud Messaging:** Configurable server-triggered gentle reminders and buddy nudges.

---

## 9. Phased Development Roadmap

- **Phase 0:** Planning & Specification *(Current Phase)*
- **Phase 1:** Android Studio Project Foundation & Material 3 Design System
- **Phase 2:** Local Persistence Layer (Room Database, DAOs, Repositories, Domain Models)
- **Phase 3:** Core MVP Screens (Home Dashboard, Mood/Stress Check-In, Sleep Logger, 60s Reset)
- **Phase 4:** Digital Wellbeing Heuristics & Grounding (Screen time integration, Wellbeing Score engine, Urge Mode, Habit tracking)
- **Phase 5:** Support Resources & AI Wellness Companion
- **Phase 6:** Polish, Backup Sync & Optional Accountability Buddy

---

## 10. Testing & Verification Requirements

### 10.1 Unit Testing
- **Frameworks:** JUnit 5, MockK, Kotlinx Coroutines Test, Turbine.
- **Coverage Areas:**
  - `CalculateWellbeingScoreUseCase`: Verify mathematical edge cases (e.g., missing sleep record, zero screen time, extreme stress).
  - ViewModels: Verify `UIState` emissions and action handling using Turbine.
  - TypeConverters & Utilities: Verify date/time conversions and JSON parsing.

### 10.2 Integration & Database Testing
- **Framework:** AndroidX Test with in-memory Room database.
- **Coverage Areas:**
  - CRUD operations on `MoodDao`, `SleepDao`, `HabitDao`.
  - Transaction rollbacks and foreign key constraints between `Habit` and `HabitLog`.

### 10.3 Compose UI & Verification Testing
- **Framework:** `ComposeTestRule` (`createAndroidComposeRule`).
- **Coverage Areas:**
  - Check-in slider responsiveness and button state transitions.
  - Navigation backstack fidelity.
  - Accessibility testing (TalkBack content descriptions, minimum touch target sizes of 48x48 dp).

---

## 11. Git and GitHub Collaboration Guidelines

### 11.1 Branching Model
- `main`: Production-ready, stable codebase. Direct commits prohibited.
- `develop`: Integration branch for completed feature phases.
- `feature/<feature-name>`: Scoped branches for individual tasks (e.g., `feature/room-database-setup`, `feature/daily-checkin-ui`).
- `fix/<issue-name>`: Targeted bug fixes.

### 11.2 Conventional Commit Standards
All commits must follow the conventional commit format:
- `feat: add Room database entities and DAOs for mood tracking`
- `ui: implement 60-second breathing animation in Compose`
- `fix: resolve crash when UsageStatsManager returns empty package list`
- `docs: add development plan and architecture diagrams`
- `refactor: extract wellbeing score algorithm into isolated UseCase`

### 11.3 Clean `.gitignore` Policy
The repository must maintain a comprehensive Android `.gitignore` excluding:
- `.gradle/`, `build/`, `*/build/`
- Local environment files (`local.properties`)
- Keystores (`*.jks`, `*.keystore`)
- IDE caches (`.idea/caches/`, `.idea/libraries/`, `*.iml`)
- Temporary crash/log outputs.
