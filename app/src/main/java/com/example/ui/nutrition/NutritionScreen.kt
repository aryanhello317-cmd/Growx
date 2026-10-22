package com.example.ui.nutrition

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.data.local.NutritionCalculator
import com.example.ui.MainViewModel
import com.example.ui.theme.ApexGold
import com.example.ui.theme.BuildXBlack
import com.example.ui.theme.BuildXCardBackground
import com.example.ui.theme.BuildXCardBorder
import com.example.ui.theme.CriticalRed
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonMint
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun NutritionScreen(viewModel: MainViewModel) {
    val user by viewModel.user.collectAsState()

    var selectedBudgetTier by remember { mutableStateOf(user?.budgetTier ?: "Low Budget") }
    var isVegOnly by remember { mutableStateOf(user?.isVegetarian ?: false) }

    // Calculator inputs
    var calcAge by remember { mutableIntStateOf(user?.age ?: 21) }
    var calcHeight by remember { mutableFloatStateOf(user?.heightCm ?: 175f) }
    var calcWeight by remember { mutableFloatStateOf(user?.weightKg ?: 70f) }

    val calcResult = NutritionCalculator.calculateNutrition(
        age = calcAge,
        heightCm = calcHeight,
        weightKg = calcWeight,
        goal = user?.fitnessGoal ?: "Strength"
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BuildXBlack)
            .padding(horizontal = 18.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ATHLETIC FUEL ON A BUDGET",
            color = ElectricCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Text(
            text = "Budget-Friendly Nutrition",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preference Toggles (Budget & Veg/Non-Veg)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Your Budget Level", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Low Budget", "Moderate", "Flexible").forEach { tier ->
                        val isSelected = selectedBudgetTier == tier
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) ElectricCyan else BuildXBlack)
                                .clickable { selectedBudgetTier = tier }
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

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Strict Vegetarian Filter", color = TextSecondary, fontSize = 13.sp)
                    Switch(
                        checked = isVegOnly,
                        onCheckedChange = { isVegOnly = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = BuildXBlack, checkedTrackColor = ElectricCyan)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Calorie & Macro Calculator Card (With Adolescent Protection!)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Calculate, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("CALORIE & NUTRITION CALCULATOR", color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 1.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Adjust inputs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Age: $calcAge years", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Height: ${calcHeight.roundToInt()} cm", color = TextPrimary, fontSize = 13.sp)
                    Text("Weight: ${calcWeight.roundToInt()} kg", color = TextPrimary, fontSize = 13.sp)
                }

                Slider(
                    value = calcAge.toFloat(),
                    onValueChange = { calcAge = it.roundToInt() },
                    valueRange = 12f..65f,
                    colors = SliderDefaults.colors(thumbColor = ElectricCyan, activeTrackColor = ElectricCyan)
                )

                Slider(
                    value = calcWeight,
                    onValueChange = { calcWeight = it },
                    valueRange = 40f..120f,
                    colors = SliderDefaults.colors(thumbColor = ElectricCyan, activeTrackColor = ElectricCyan)
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (calcResult.isAdolescent) {
                    // Adolescent Guidance
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BuildXBlack)
                            .border(1.dp, ApexGold, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = ApexGold, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Youth Growth Protocol (Age < 18)", color = ApexGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = calcResult.adolescentAdvice,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Recommended Protein: ~${calcResult.proteinRecommendationGrams}g/day (from whole natural foods)",
                                color = NeonMint,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                } else {
                    // Adult TDEE Results
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BuildXBlack)
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Maintenance Energy", color = TextSecondary, fontSize = 11.sp)
                                    Text("~${calcResult.maintenanceCalories} kcal", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                }
                                Column {
                                    Text("Lean Muscle Gain", color = TextSecondary, fontSize = 11.sp)
                                    Text("~${calcResult.muscleGainCalories} kcal", color = NeonMint, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                }
                                Column {
                                    Text("Daily Protein", color = TextSecondary, fontSize = 11.sp)
                                    Text("~${calcResult.proteinRecommendationGrams}g", color = ElectricCyan, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "*Estimates are approximate baseline guides. Adjust based on weekly energy & recovery.",
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // High Protein Affordable Staples List
        Text(
            text = "AFFORDABLE PROTEIN STAPLES (NO COSTLY POWDERS)",
            color = ElectricCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val staples = NutritionCalculator.affordableStaples.filter {
            if (isVegOnly) it.isVegetarian else true
        }

        staples.forEach { food ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(food.name, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(food.category, color = TextSecondary, fontSize = 11.sp)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("${food.proteinGrams.toInt()}g Protein", color = NeonMint, fontSize = 14.sp, fontWeight = FontWeight.Black)
                            Text(food.estimatedCostPerServing, color = ElectricCyan, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Prep: ${food.prepTip}",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Meal Suggestions
        Text(
            text = "PRACTICAL PRE & POST-WORKOUT MEALS",
            color = ElectricCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val meals = NutritionCalculator.sampleMeals.filter {
            if (isVegOnly) it.isVegetarian else true
        }

        meals.forEach { meal ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricCyan.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(meal.mealType.uppercase(), color = ElectricCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Text("${meal.approxProteinGrams}g Protein • ~${meal.approxCalories} kcal", color = NeonMint, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(meal.title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(meal.description, color = TextSecondary, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Ingredients: ${meal.ingredients.joinToString(", ")}", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Food Substitution Matrix Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SMART FOOD SUBSTITUTIONS", color = ElectricCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("• Expensive Whey Protein ➔ Whole Eggs or Roasted Peanut Powder or Soya Chunks", color = TextPrimary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Expensive Greek Yogurt ➔ Strained Homemade Curd (hung curd)", color = TextPrimary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Expensive Salmon ➔ Canned Sardines, Chunk Tuna, or Ground Flaxseeds", color = TextPrimary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Expensive Energy Bars ➔ 1 Ripe Banana + 1 Spoon Peanut Butter", color = TextPrimary, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
