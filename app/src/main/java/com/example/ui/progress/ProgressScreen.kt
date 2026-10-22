package com.example.ui.progress

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.BuildXRepository
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
fun ProgressScreen(viewModel: MainViewModel) {
    val user by viewModel.user.collectAsState()
    val allWorkouts by viewModel.allWorkouts.collectAsState()
    val completedWinterDaysCount by viewModel.completedWinterDaysCount.collectAsState()
    val totalReps by viewModel.totalPushUpReps.collectAsState()
    val totalDuration by viewModel.totalDurationSeconds.collectAsState()
    val workoutCount by viewModel.totalWorkoutsCount.collectAsState()

    var selectedViewTimeframe by remember { mutableStateOf("Weekly") }
    val timeframes = listOf("Weekly", "Monthly", "90-Day")

    val scrollState = rememberScrollState()

    val currentXp = user?.xp ?: 150
    val currentRank = user?.currentRank ?: "Rookie"
    val (minXp, maxXp) = BuildXRepository.getXpThresholdsForRank(currentRank)
    val rankProgress = ((currentXp - minXp).toFloat() / (maxXp - minXp).toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BuildXBlack)
            .padding(horizontal = 18.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ANALYTICS & PROGRESSION",
            color = ElectricCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Text(
            text = "Performance Dashboard",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Timeframe Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(BuildXCardBackground)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            timeframes.forEach { frame ->
                val isSelected = selectedViewTimeframe == frame
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) ElectricCyan else Color.Transparent)
                        .clickable { selectedViewTimeframe = frame }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = frame,
                        color = if (isSelected) BuildXBlack else TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // High Level Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                title = "Push-Up Reps",
                value = "${(totalReps ?: 0) + (user?.totalPushUpReps ?: 0)}",
                icon = Icons.Default.FitnessCenter,
                iconTint = ElectricCyan,
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                title = "Workouts Done",
                value = "${(workoutCount) + (user?.totalWorkoutsCompleted ?: 0)}",
                icon = Icons.Default.EmojiEvents,
                iconTint = NeonMint,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                title = "Total Time",
                value = "${((totalDuration ?: 0) / 60) + 45}m",
                icon = Icons.Default.Timer,
                iconTint = ElectricBlue,
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                title = "Best Streak",
                value = "${user?.streakDays ?: 1} Days",
                icon = Icons.Default.LocalFireDepartment,
                iconTint = EnergyOrange,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Chart 1: Push-Up Reps Visual Bar Chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Push-Up Repetitions ($selectedViewTimeframe)",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Avg: 32 reps/day",
                        color = ElectricCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Custom Bar Chart using Canvas
                val repSamples = when (selectedViewTimeframe) {
                    "Weekly" -> listOf(20, 35, 25, 45, 30, 50, 40)
                    "Monthly" -> listOf(25, 30, 45, 50, 35, 60, 55, 40, 65, 70)
                    else -> listOf(20, 30, 40, 55, 65, 75, 80, 90, 100)
                }
                val labels = when (selectedViewTimeframe) {
                    "Weekly" -> listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    "Monthly" -> listOf("W1", "W1", "W2", "W2", "W3", "W3", "W4", "W4", "W5", "W5")
                    else -> listOf("D10", "D20", "D30", "D40", "D50", "D60", "D70", "D80", "D90")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val maxVal = repSamples.maxOrNull()?.toFloat() ?: 100f
                        val barSpacing = size.width / repSamples.size
                        val barWidth = barSpacing * 0.45f

                        repSamples.forEachIndexed { i, value ->
                            val barHeight = (value / maxVal) * (size.height - 25f)
                            val left = i * barSpacing + (barSpacing - barWidth) / 2f
                            val top = size.height - barHeight - 20f

                            // Draw Bar with rounded corners
                            drawRoundRect(
                                brush = Brush.verticalGradient(
                                    listOf(ElectricCyan, ElectricBlue)
                                ),
                                topLeft = Offset(left, top),
                                size = Size(barWidth, barHeight),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    labels.forEach { lbl ->
                        Text(lbl, color = TextMuted, fontSize = 10.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chart 2: Workout Duration Trend (Smooth Line Curve)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Workout Duration Trend (Minutes)",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val points = listOf(15f, 25f, 20f, 35f, 30f, 45f, 40f)
                        val maxPoints = 50f
                        val stepX = size.width / (points.size - 1)

                        val path = Path()
                        val fillPath = Path()

                        points.forEachIndexed { index, value ->
                            val x = index * stepX
                            val y = size.height - (value / maxPoints) * size.height

                            if (index == 0) {
                                path.moveTo(x, y)
                                fillPath.moveTo(x, size.height)
                                fillPath.lineTo(x, y)
                            } else {
                                path.lineTo(x, y)
                                fillPath.lineTo(x, y)
                            }
                        }

                        fillPath.lineTo(size.width, size.height)
                        fillPath.close()

                        // Draw area gradient fill
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                listOf(NeonMint.copy(alpha = 0.35f), Color.Transparent)
                            )
                        )

                        // Draw trend line
                        drawPath(
                            path = path,
                            color = NeonMint,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Draw point circles
                        points.forEachIndexed { index, value ->
                            val x = index * stepX
                            val y = size.height - (value / maxPoints) * size.height
                            drawCircle(color = BuildXBlack, radius = 5.dp.toPx(), center = Offset(x, y))
                            drawCircle(color = NeonMint, radius = 3.dp.toPx(), center = Offset(x, y))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Rank Progression Ladder
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
                        Text("ATHLETIC RANK TIER", color = ElectricCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(currentRank, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    }

                    Icon(
                        imageVector = Icons.Default.MilitaryTech,
                        contentDescription = "Rank emblem",
                        tint = when (currentRank) {
                            "Apex" -> ApexGold
                            "Master" -> ElectricCyan
                            "Elite" -> NeonMint
                            else -> TextSecondary
                        },
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { rankProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = ElectricCyan,
                    trackColor = BuildXBlack
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "$currentXp XP Earned • ${maxXp - currentXp} XP until next rank tier",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Rank tiers breakdown
                val ranksList = listOf(
                    "Rookie" to "0 - 499 XP",
                    "Beginner" to "500 - 1,499 XP",
                    "Warrior" to "1,500 - 3,499 XP",
                    "Elite" to "3,500 - 6,999 XP",
                    "Master" to "7,000 - 11,999 XP",
                    "Apex" to "12,000+ XP (Apex Master)"
                )

                ranksList.forEach { (rankName, xpReq) ->
                    val isCurrent = currentRank.equals(rankName, ignoreCase = true)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isCurrent) "▶ $rankName" else rankName,
                            color = if (isCurrent) ElectricCyan else TextSecondary,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                        Text(
                            text = xpReq,
                            color = if (isCurrent) TextPrimary else TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(title, color = TextSecondary, fontSize = 11.sp)
        }
    }
}
