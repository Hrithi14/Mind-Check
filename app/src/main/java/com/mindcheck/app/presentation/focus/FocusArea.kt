package com.mindcheck.app.presentation.focus

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mindcheck.app.core.designsystem.theme.AccentFitness
import com.mindcheck.app.core.designsystem.theme.AccentHabits
import com.mindcheck.app.core.designsystem.theme.AccentScrolling
import com.mindcheck.app.core.designsystem.theme.AccentSleep
import com.mindcheck.app.core.designsystem.theme.AccentStress

enum class FocusArea(
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color
) {
    SLEEP(
        title = "Sleep Hygiene",
        subtitle = "Rest & Circadian Balance",
        description = "Maintain consistent sleep schedules, bedtime winding down, and refreshed morning energy.",
        icon = Icons.Outlined.Bedtime,
        accentColor = AccentSleep
    ),
    STRESS(
        title = "Stress Management",
        subtitle = "Exam & Academic Calm",
        description = "De-escalate study anxiety, manage deadline pressures, and learn 60-second somatic resets.",
        icon = Icons.Outlined.SelfImprovement,
        accentColor = AccentStress
    ),
    SCROLLING(
        title = "Mindful Scrolling",
        subtitle = "Digital Habits & Focus",
        description = "Recognize late-night phone rabbit holes and restore healthy boundaries with social media.",
        icon = Icons.Outlined.Smartphone,
        accentColor = AccentScrolling
    ),
    HABITS(
        title = "Daily Micro-Habits",
        subtitle = "Sustainable Daily Routines",
        description = "Build small, non-intimidating wins: hydration, sunlight exposure, and study breaks.",
        icon = Icons.Outlined.TaskAlt,
        accentColor = AccentHabits
    ),
    FITNESS(
        title = "Movement & Fitness",
        subtitle = "Physical Vitality",
        description = "Counter long sitting sessions with light walks, stretching, and physical body release.",
        icon = Icons.AutoMirrored.Outlined.DirectionsRun,
        accentColor = AccentFitness
    )
}
