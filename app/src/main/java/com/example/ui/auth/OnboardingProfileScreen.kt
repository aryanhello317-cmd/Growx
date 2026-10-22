package com.example.ui.auth

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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.ui.MainViewModel
import com.example.ui.theme.ApexGold
import com.example.ui.theme.BuildXBlack
import com.example.ui.theme.BuildXCardBackground
import com.example.ui.theme.BuildXCardBorder
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonMint
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun OnboardingProfileScreen(viewModel: MainViewModel) {
    var age by remember { mutableIntStateOf(21) }
    var experience by remember { mutableStateOf("Beginner") }
    var heightCm by remember { mutableFloatStateOf(175f) }
    var weightKg by remember { mutableFloatStateOf(68f) }
    var goal by remember { mutableStateOf("Strength") }
    var workoutTime by remember { mutableIntStateOf(30) }
    var equipment by remember { mutableStateOf("Bodyweight Only") }
    var budgetTier by remember { mutableStateOf("Low Budget") }
    var isVegetarian by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BuildXBlack)
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "CUSTOMIZE YOUR ATHLETIC PROFILE",
            color = ElectricCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tailor Your Calisthenics Routine",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Age Selection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Your Age: $age years", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Min: 12", color = TextMuted, fontSize = 12.sp)
                }

                Slider(
                    value = age.toFloat(),
                    onValueChange = { age = it.roundToInt() },
                    valueRange = 12f..65f,
                    steps = 52,
                    colors = SliderDefaults.colors(
                        thumbColor = ElectricCyan,
                        activeTrackColor = ElectricCyan,
                        inactiveTrackColor = TextMuted
                    ),
                    modifier = Modifier.testTag("onboarding_age_slider")
                )

                if (age < 18) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(BuildXBlack)
                            .border(1.dp, ApexGold, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = ApexGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Youth Safety Guard: At age $age, BuildX prioritizes safe form, bodyweight mastery, and balanced nutrition. Aggressive calorie cuts are automatically disabled.",
                                color = TextPrimary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fitness Goal
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Primary Goal", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))

                val goals = listOf("Strength", "General Fitness", "Muscle Development", "Skill Training")
                goals.chunked(2).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { item ->
                            val isSelected = goal == item
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) ElectricCyan else BuildXBlack)
                                    .clickable { goal = item }
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item,
                                    color = if (isSelected) BuildXBlack else TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Experience Level
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Experience Level", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Beginner", "Intermediate", "Advanced").forEach { level ->
                        val isSelected = experience == level
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ElectricCyan else BuildXBlack)
                                .clickable { experience = level }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = level,
                                color = if (isSelected) BuildXBlack else TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Height & Weight Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Height: ${heightCm.roundToInt()} cm", color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("Weight: ${weightKg.roundToInt()} kg", color = TextPrimary, fontWeight = FontWeight.Bold)
                }

                Slider(
                    value = heightCm,
                    onValueChange = { heightCm = it },
                    valueRange = 130f..210f,
                    colors = SliderDefaults.colors(thumbColor = ElectricCyan, activeTrackColor = ElectricCyan)
                )

                Slider(
                    value = weightKg,
                    onValueChange = { weightKg = it },
                    valueRange = 35f..130f,
                    colors = SliderDefaults.colors(thumbColor = ElectricCyan, activeTrackColor = ElectricCyan)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Available Time & Equipment
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Daily Workout Time", color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(15, 30, 45, 60).forEach { mins ->
                        val isSelected = workoutTime == mins
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ElectricCyan else BuildXBlack)
                                .clickable { workoutTime = mins }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$mins min",
                                color = if (isSelected) BuildXBlack else TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Available Equipment", color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                val eqList = listOf("Bodyweight Only", "Pull-up Bar", "Parallel Bars", "Resistance Bands")
                eqList.chunked(2).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { eq ->
                            val isSelected = equipment == eq
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) ElectricCyan else BuildXBlack)
                                    .clickable { equipment = eq }
                                    .padding(vertical = 10.dp, horizontal = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = eq,
                                    color = if (isSelected) BuildXBlack else TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Food Budget & Diet
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Food Budget & Diet Preference", color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Low Budget", "Moderate", "Flexible").forEach { tier ->
                        val isSelected = budgetTier == tier
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ElectricCyan else BuildXBlack)
                                .clickable { budgetTier = tier }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tier,
                                color = if (isSelected) BuildXBlack else TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Strictly Vegetarian Meal Plans", color = TextSecondary, fontSize = 14.sp)
                    Switch(
                        checked = isVegetarian,
                        onCheckedChange = { isVegetarian = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = BuildXBlack,
                            checkedTrackColor = ElectricCyan
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                viewModel.saveOnboardingProfile(
                    age = age,
                    experience = experience,
                    height = heightCm,
                    weight = weightKg,
                    goal = goal,
                    time = workoutTime,
                    equip = equipment,
                    budget = budgetTier,
                    isVeg = isVegetarian
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("save_onboarding_profile_button"),
            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "INITIALIZE YOUR CALISTHENICS PLAN",
                color = BuildXBlack,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
