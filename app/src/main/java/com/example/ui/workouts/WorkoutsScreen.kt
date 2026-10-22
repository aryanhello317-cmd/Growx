package com.example.ui.workouts

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.DefaultExercises
import com.example.data.model.Exercise
import com.example.ui.MainViewModel
import com.example.ui.theme.ApexGold
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
fun WorkoutsScreen(viewModel: MainViewModel) {
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedLevel by remember { mutableStateOf("All") }
    var selectedExerciseForDetail by remember { mutableStateOf<Exercise?>(null) }

    val categories = listOf("All", "Push", "Pull", "Legs", "Core", "Skills")
    val levels = listOf("All", "Beginner", "Intermediate", "Advanced")

    val filteredList = DefaultExercises.list.filter { ex ->
        val matchesCategory = if (selectedCategory == "All") true else ex.category.equals(selectedCategory, ignoreCase = true)
        val matchesLevel = if (selectedLevel == "All") true else ex.level.equals(selectedLevel, ignoreCase = true)
        matchesCategory && matchesLevel
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BuildXBlack)
            .padding(horizontal = 18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CALISTHENICS MASTERY",
                color = ElectricCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Text(
                text = "Workout Library",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Calisthenics Hero Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.calisthenics_banner_1791906289689),
                        contentDescription = "Calisthenics workout illustration",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, BuildXBlack.copy(alpha = 0.85f))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Zero Gym Needed",
                            color = ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Pure Human Leverage & Gravity",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) ElectricCyan else BuildXCardBackground)
                            .border(1.dp, if (isSelected) ElectricCyan else BuildXCardBorder, RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) BuildXBlack else TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Level Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(levels) { lvl ->
                    val isSelected = selectedLevel == lvl
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) ElectricBlue else BuildXCardBackground)
                            .clickable { selectedLevel = lvl }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = lvl,
                            color = if (isSelected) BuildXBlack else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Exercise Cards
        items(filteredList) { exercise ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { selectedExerciseForDetail = exercise }
                    .testTag("exercise_card_${exercise.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ElectricCyan.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                    Text(
                                        text = exercise.category.uppercase(),
                                        color = ElectricCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            when (exercise.level) {
                                                "Beginner" -> NeonMint.copy(alpha = 0.2f)
                                                "Intermediate" -> ApexGold.copy(alpha = 0.2f)
                                                else -> ElectricBlue.copy(alpha = 0.2f)
                                            }
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = exercise.level,
                                        color = when (exercise.level) {
                                            "Beginner" -> NeonMint
                                            "Intermediate" -> ApexGold
                                            else -> ElectricBlue
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = exercise.name,
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${exercise.sets} sets × ${exercise.repsOrHold} • ${exercise.restSeconds}s rest",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (exercise.isCameraSupported) {
                                IconButton(
                                    onClick = { viewModel.startCameraPushUpSession() }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "Camera Rep Counter",
                                        tint = ElectricCyan
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.startWorkoutSession(exercise) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Start Workout",
                                    tint = NeonMint
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Exercise Detail Dialog
    selectedExerciseForDetail?.let { ex ->
        AlertDialog(
            onDismissRequest = { selectedExerciseForDetail = null },
            title = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(ex.name, color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
                        IconButton(onClick = { selectedExerciseForDetail = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }
                    Text(
                        "${ex.category} • ${ex.level} • ${ex.sets} sets × ${ex.repsOrHold}",
                        color = ElectricCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text("Target Muscles:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(ex.targetMuscles, color = TextPrimary, fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Instructions for Correct Form:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    ex.instructions.forEachIndexed { i, step ->
                        Text("${i + 1}. $step", color = TextPrimary, fontSize = 12.sp, lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(3.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Form Checkpoints:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    ex.formCheckpoints.forEach { check ->
                        Text("• $check", color = NeonMint, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Safety Tips:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(ex.safetyTips, color = ApexGold, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Easier Alternative:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(ex.easierAlternative, color = ElectricBlue, fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val toStart = ex
                        selectedExerciseForDetail = null
                        viewModel.startWorkoutSession(toStart)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
                ) {
                    Text("Start Exercise", color = BuildXBlack, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                if (ex.isCameraSupported) {
                    OutlinedButton(
                        onClick = {
                            selectedExerciseForDetail = null
                            viewModel.startCameraPushUpSession()
                        }
                    ) {
                        Text("Track with Camera", color = ElectricCyan)
                    }
                }
            },
            containerColor = BuildXCardBackground
        )
    }
}
