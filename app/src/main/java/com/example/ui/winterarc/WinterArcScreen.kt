package com.example.ui.winterarc

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DefaultMotivation
import com.example.ui.MainViewModel
import com.example.ui.theme.ApexGold
import com.example.ui.theme.BuildXBlack
import com.example.ui.theme.BuildXCardBackground
import com.example.ui.theme.BuildXCardBorder
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EnergyOrange
import com.example.ui.theme.NeonMint
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WinterArcScreen(viewModel: MainViewModel) {
    val user by viewModel.user.collectAsState()
    val winterDays by viewModel.winterArcDays.collectAsState()
    val completedDaysCount by viewModel.completedWinterDaysCount.collectAsState()
    val milestones by viewModel.milestones.collectAsState()

    var selectedDayNumber by remember { mutableIntStateOf(user?.winterArcDay ?: 1) }
    var showAddMilestoneDialog by remember { mutableStateOf(false) }
    var showDay90ReviewDialog by remember { mutableStateOf(false) }

    var newMilestoneTitle by remember { mutableStateOf("") }
    var newMilestoneTarget by remember { mutableStateOf("50") }
    var newMilestoneTargetDay by remember { mutableStateOf("30") }

    val activeDayEntity = winterDays.firstOrNull { it.dayNumber == selectedDayNumber }
    val progressPercent = ((completedDaysCount.toFloat() / 90f) * 100).toInt()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuildXBlack)
            .padding(horizontal = 18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CHALLENGE PROTOCOL",
                        color = ElectricCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "The 90-Day Winter Arc",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Review button for Day 90
                Button(
                    onClick = { showDay90ReviewDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = BuildXCardBackground),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Day 90 Review", color = ElectricCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Overall Arc Progress Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Winter Arc Progress", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("$completedDaysCount / 90 Days ($progressPercent%)", color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { completedDaysCount.toFloat() / 90f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = ElectricCyan,
                        trackColor = BuildXBlack
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Daily non-negotiable discipline: Calisthenics routine, hydration, mental focus, cold shower, clean food.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Day Selector Carousel (1 through 90)
            Text(
                text = "SELECT CHALLENGE DAY (1 - 90)",
                color = ElectricCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(winterDays) { day ->
                    val isSelected = day.dayNumber == selectedDayNumber
                    val isCompleted = day.isAllTasksCompleted

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when {
                                    isSelected -> ElectricCyan
                                    isCompleted -> NeonMint.copy(alpha = 0.2f)
                                    else -> BuildXCardBackground
                                }
                            )
                            .border(
                                1.dp,
                                when {
                                    isSelected -> ElectricCyan
                                    isCompleted -> NeonMint
                                    else -> BuildXCardBorder
                                },
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedDayNumber = day.dayNumber }
                            .testTag("winter_arc_day_chip_${day.dayNumber}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "D${day.dayNumber}",
                                color = if (isSelected) BuildXBlack else TextPrimary,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                            if (isCompleted) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) BuildXBlack else NeonMint)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Active Day Checklist Card
        activeDayEntity?.let { day ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "DAY ${day.dayNumber} PROTOCOL",
                                    color = ElectricCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = day.routineTitle,
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Rest / Sick Day Protection Toggle
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Healing,
                                    contentDescription = "Rest Mode",
                                    tint = if (day.isRestOrRecoveryDay) NeonMint else TextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Switch(
                                    checked = day.isRestOrRecoveryDay,
                                    onCheckedChange = { viewModel.toggleRestRecovery(day.dayNumber, it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = BuildXBlack,
                                        checkedTrackColor = NeonMint
                                    ),
                                    modifier = Modifier.testTag("rest_day_switch")
                                )
                            }
                        }

                        if (day.isRestOrRecoveryDay) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonMint.copy(alpha = 0.15f))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "Recovery & Injury Protection Active: Your streak is safe! Today focuses on tissue recovery, joint mobility, deep sleep, and clean nutrition without penalty.",
                                    color = NeonMint,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Daily 5 Non-Negotiable Tasks",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Task 1: Workout
                        TaskRow(
                            title = if (day.isRestOrRecoveryDay) "Active Mobility & Stretching" else "Calisthenics: ${day.routineTitle}",
                            icon = Icons.Default.FitnessCenter,
                            checked = day.routineDone,
                            onToggle = { viewModel.toggleWinterArcTask(day, "routine") }
                        )

                        // Task 2: Hydration
                        TaskRow(
                            title = "Hydration: Minimum 2.5L - 3L Pure Water",
                            icon = Icons.Default.LocalDrink,
                            checked = day.hydrationDone,
                            onToggle = { viewModel.toggleWinterArcTask(day, "hydration") }
                        )

                        // Task 3: Mental Growth
                        TaskRow(
                            title = "Mindset: 10 Pages Reading / Mental Reflection",
                            icon = Icons.Default.MenuBook,
                            checked = day.mentalGrowthDone,
                            onToggle = { viewModel.toggleWinterArcTask(day, "mental") }
                        )

                        // Task 4: Cold Shower / Discipline
                        TaskRow(
                            title = "Discipline: Cold Water Finish or Meditation",
                            icon = Icons.Default.Shower,
                            checked = day.coldShowerDone,
                            onToggle = { viewModel.toggleWinterArcTask(day, "cold") }
                        )

                        // Task 5: Clean Nutrition
                        TaskRow(
                            title = "Nutrition: Zero Junk / Sufficient Protein",
                            icon = Icons.Default.Restaurant,
                            checked = day.cleanNutritionDone,
                            onToggle = { viewModel.toggleWinterArcTask(day, "nutrition") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Personal Milestones Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PERSONAL MILESTONES",
                    color = ElectricCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                IconButton(onClick = { showAddMilestoneDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Milestone", tint = ElectricCyan)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(milestones) { milestone ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = milestone.isAchieved,
                            onCheckedChange = { viewModel.toggleMilestone(milestone.id, it) },
                            colors = CheckboxDefaults.colors(checkedColor = NeonMint, checkmarkColor = BuildXBlack)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = milestone.title,
                                color = if (milestone.isAchieved) NeonMint else TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Target by Day ${milestone.targetDay} • Target: ${milestone.targetRepsOrDays}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (milestone.isAchieved) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = "Achieved", tint = ApexGold, modifier = Modifier.size(24.dp))
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Add Milestone Dialog
    if (showAddMilestoneDialog) {
        AlertDialog(
            onDismissRequest = { showAddMilestoneDialog = false },
            title = { Text("Add Personal Milestone", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newMilestoneTitle,
                        onValueChange = { newMilestoneTitle = it },
                        label = { Text("Milestone Goal (e.g. 50 Diamond Pushups)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newMilestoneTarget,
                        onValueChange = { newMilestoneTarget = it },
                        label = { Text("Target Number (Reps or Days)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newMilestoneTargetDay,
                        onValueChange = { newMilestoneTargetDay = it },
                        label = { Text("Target Winter Arc Day (1-90)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val target = newMilestoneTarget.toIntOrNull() ?: 50
                        val day = newMilestoneTargetDay.toIntOrNull() ?: 30
                        if (newMilestoneTitle.isNotBlank()) {
                            viewModel.addNewMilestone(newMilestoneTitle, target, day)
                            showAddMilestoneDialog = false
                            newMilestoneTitle = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
                ) {
                    Text("Add Milestone", color = BuildXBlack, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMilestoneDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = BuildXCardBackground
        )
    }

    // Day 90 Review Dialog
    if (showDay90ReviewDialog) {
        AlertDialog(
            onDismissRequest = { showDay90ReviewDialog = false },
            title = {
                Text("Winter Arc 90-Day Review", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
            },
            text = {
                Column {
                    Text(
                        "90 Days of Uncompromising Calisthenics & Mental Toughness.",
                        color = ElectricCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("• Completed Days: $completedDaysCount / 90", color = TextPrimary, fontSize = 13.sp)
                    Text("• Current Rank: ${user?.currentRank} (${user?.xp} Total XP)", color = TextPrimary, fontSize = 13.sp)
                    Text("• Total Push-Up Reps: ${user?.totalPushUpReps}", color = TextPrimary, fontSize = 13.sp)
                    Text("• Consistency Streak: ${user?.streakDays} Days", color = TextPrimary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Review Outcome: You forged physical bodyweight mastery without spending money on expensive gyms or supplements.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDay90ReviewDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
                ) {
                    Text("Close Review", color = BuildXBlack, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = BuildXCardBackground
        )
    }
}

@Composable
fun TaskRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (checked) BuildXBlack else BuildXDarkSurface)
            .clickable { onToggle() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(icon, contentDescription = null, tint = if (checked) NeonMint else ElectricCyan, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                color = if (checked) TextSecondary else TextPrimary,
                fontSize = 13.sp,
                fontWeight = if (checked) FontWeight.Normal else FontWeight.SemiBold
            )
        }

        Icon(
            imageVector = if (checked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (checked) NeonMint else TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

private val BuildXDarkSurface = com.example.ui.theme.BuildXDarkSurface
