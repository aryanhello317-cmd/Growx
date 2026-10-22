package com.example.ui.home

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.DefaultExercises
import com.example.data.local.DefaultMotivation
import com.example.data.repository.BuildXRepository
import com.example.ui.AppDestination
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
fun HomeScreen(viewModel: MainViewModel) {
    val user by viewModel.user.collectAsState()
    val winterArcDays by viewModel.winterArcDays.collectAsState()
    val completedDaysCount by viewModel.completedWinterDaysCount.collectAsState()
    val scrollState = rememberScrollState()

    val currentDayNumber = user?.winterArcDay ?: 1
    val todayWinterArc = winterArcDays.firstOrNull { it.dayNumber == currentDayNumber }
    val motivationText = DefaultMotivation.getQuoteForDay(currentDayNumber)

    val currentXp = user?.xp ?: 150
    val currentRank = user?.currentRank ?: "Rookie"
    val (minRankXp, maxRankXp) = BuildXRepository.getXpThresholdsForRank(currentRank)
    val rankProgress = ((currentXp - minRankXp).toFloat() / (maxRankXp - minRankXp).toFloat())
        .coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BuildXBlack)
            .padding(horizontal = 18.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "WELCOME ATHLETE",
                    color = ElectricCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "BuildX Command",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Streak Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(BuildXCardBackground)
                    .border(1.dp, BuildXCardBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = EnergyOrange,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${user?.streakDays ?: 1} Day Streak",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Winter Arc Cinematic Hero Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppDestination.WINTER_ARC) }
                .testTag("home_winter_arc_hero_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Banner background
                Image(
                    painter = painterResource(id = R.drawable.winter_arc_banner_1791906271431),
                    contentDescription = "Winter Arc Banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )

                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, BuildXBlack.copy(alpha = 0.95f))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(BuildXBlack.copy(alpha = 0.8f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AcUnit, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "DAY $currentDayNumber / 90",
                                    color = ElectricCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Text(
                            text = "${((completedDaysCount / 90f) * 100).toInt()}% COMPLETE",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = "THE 90-DAY WINTER ARC",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        text = "Quiet discipline, cold mornings & calisthenics mastery",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Motivation Quote Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = "Daily Quote",
                        tint = ApexGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DAILY MOTIVATION // DAY $currentDayNumber",
                        color = ApexGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "\"$motivationText\"",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rank & XP Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppDestination.PROGRESS) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(ElectricCyan.copy(alpha = 0.15f))
                                .border(1.5.dp, ElectricCyan, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentRank.first().toString(),
                                color = ElectricCyan,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "RANK: $currentRank",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "$currentXp XP Earned (Apex at 12,000 XP)",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View Rank",
                        tint = ElectricCyan
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

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("$minRankXp XP", color = TextMuted, fontSize = 11.sp)
                    Text("${maxRankXp - currentXp} XP to next rank", color = ElectricCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text("$maxRankXp XP", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Launch Buttons: Camera Counter & Today's Workout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Camera Rep Counter
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.startCameraPushUpSession() }
                    .testTag("home_camera_rep_counter_button"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(ElectricCyan),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = BuildXBlack, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("AI Camera Reps", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Live push-up tracking", color = TextSecondary, fontSize = 12.sp)
                }
            }

            // Start Workout
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        val defaultEx = DefaultExercises.list.first()
                        viewModel.startWorkoutSession(defaultEx)
                    }
                    .testTag("home_start_workout_button"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeonMint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = BuildXBlack, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Start Workout", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Standard Push-Ups", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Today's Winter Arc Routine Summary
        todayWinterArc?.let { day ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("TODAY'S WINTER ARC FOCUS", color = ElectricCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(day.routineTitle, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        if (day.isRestOrRecoveryDay) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonMint.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("RECOVERY", color = NeonMint, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Daily Goals: ${day.completedTasksCount} / 5 completed",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.navigateTo(AppDestination.WINTER_ARC) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BuildXBlack),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("View Winter Arc Checklist", color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
