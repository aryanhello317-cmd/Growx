package com.example.ui.workouts

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppDestination
import com.example.ui.MainViewModel
import com.example.ui.theme.BuildXBlack
import com.example.ui.theme.BuildXCardBackground
import com.example.ui.theme.BuildXCardBorder
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonMint
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WorkoutPlayerScreen(viewModel: MainViewModel) {
    val exercise by viewModel.activeExercise.collectAsState()
    val completedSets by viewModel.completedSets.collectAsState()
    val activeRepsLogged by viewModel.activeRepsLogged.collectAsState()
    val restTimeRemaining by viewModel.restTimeRemaining.collectAsState()
    val isRestTimerRunning by viewModel.isRestTimerRunning.collectAsState()

    var repsForCurrentSet by remember { mutableIntStateOf(12) }
    val scrollState = rememberScrollState()

    BackHandler {
        viewModel.navigateTo(AppDestination.WORKOUTS)
    }

    if (exercise == null) {
        viewModel.navigateTo(AppDestination.WORKOUTS)
        return
    }

    val currentEx = exercise!!
    val totalSets = currentEx.sets

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BuildXBlack)
            .padding(18.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Navigation & Close
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "ACTIVE CALISTHENICS SESSION",
                    color = ElectricCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = currentEx.name,
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
            }

            IconButton(
                onClick = { viewModel.navigateTo(AppDestination.WORKOUTS) }
            ) {
                Icon(Icons.Default.Close, contentDescription = "Exit session", tint = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Set Progress Indicator Rings
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..totalSets).forEach { setIndex ->
                val isCompleted = setIndex <= completedSets
                val isCurrent = setIndex == completedSets + 1

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> NeonMint
                                isCurrent -> ElectricCyan.copy(alpha = 0.2f)
                                else -> BuildXCardBackground
                            }
                        )
                        .border(
                            1.5.dp,
                            when {
                                isCompleted -> NeonMint
                                isCurrent -> ElectricCyan
                                else -> BuildXCardBorder
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = BuildXBlack, modifier = Modifier.size(20.dp))
                    } else {
                        Text(
                            text = "$setIndex",
                            color = if (isCurrent) ElectricCyan else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Rest Timer Card vs Active Set Card
        if (isRestTimerRunning) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "REST & RECOVER",
                        color = ElectricCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier.size(130.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = {
                                if (currentEx.restSeconds > 0) {
                                    restTimeRemaining.toFloat() / currentEx.restSeconds.toFloat()
                                } else 0f
                            },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 8.dp,
                            color = ElectricCyan,
                            trackColor = BuildXBlack
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${restTimeRemaining}s",
                                color = TextPrimary,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text("Resting", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Deep nasal breathing • Shake out muscle tension",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { viewModel.skipRestTimer() },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.FastForward, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Skip Rest Timer", color = ElectricCyan, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Active Set Control
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SET ${completedSets + 1} OF $totalSets",
                        color = ElectricCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Target: ${currentEx.repsOrHold}",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Rep Adjuster
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(BuildXBlack)
                                .clickable {
                                    if (repsForCurrentSet > 1) repsForCurrentSet -= 1
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("-", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$repsForCurrentSet",
                                color = TextPrimary,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text("Reps Completed", color = TextSecondary, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(BuildXBlack)
                                .clickable { repsForCurrentSet += 1 },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.logSetCompleted(repsForCurrentSet)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("log_set_completed_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonMint),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "COMPLETE SET ${completedSets + 1}",
                            color = BuildXBlack,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Form Guidance Reminder Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Key Form Checkpoints",
                    color = ElectricCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                currentEx.formCheckpoints.forEach { pt ->
                    Text("• $pt", color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Finish Workout Action
        Button(
            onClick = { viewModel.finishWorkoutSession() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("finish_workout_session_button"),
            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "FINISH WORKOUT (+${150 + (activeRepsLogged * 2)} XP)",
                color = BuildXBlack,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
