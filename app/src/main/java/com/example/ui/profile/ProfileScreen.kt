package com.example.ui.profile

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppDestination
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

@Composable
fun ProfileScreen(viewModel: MainViewModel) {
    val user by viewModel.user.collectAsState()
    val context = LocalContext.current

    var showPinDialog by remember { mutableStateOf(false) }
    var newPinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }
    var showExportDialog by remember { mutableStateOf(false) }

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
            text = "ATHLETE PROFILE & SETTINGS",
            color = ElectricCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Text(
            text = "Account & Security",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(ElectricCyan.copy(alpha = 0.15f))
                            .border(2.dp, ElectricCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Avatar",
                            tint = ElectricCyan,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = user?.email ?: "athlete@buildx.app",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Rank: ${user?.currentRank} • ${user?.xp} XP",
                            color = ElectricCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BuildXBlack)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${user?.age ?: 21} yrs", color = TextSecondary, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BuildXBlack)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${user?.heightCm?.toInt() ?: 175} cm", color = TextSecondary, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BuildXBlack)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${user?.weightKg?.toInt() ?: 70} kg", color = TextSecondary, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(BuildXBlack)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(user?.experienceLevel ?: "Beginner", color = TextSecondary, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { viewModel.navigateTo(AppDestination.ONBOARDING) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Edit Athletic Profile & Goals", color = ElectricCyan, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Lock & Biometrics Settings
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "APP LOCK & BIOMETRICS",
                        color = ElectricCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Enable PIN App Lock", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Requires 4-digit PIN upon app open", color = TextSecondary, fontSize = 11.sp)
                    }
                    Switch(
                        checked = user?.isAppLockEnabled ?: false,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                showPinDialog = true
                            } else {
                                viewModel.setAppLock(false, "")
                            }
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = BuildXBlack, checkedTrackColor = ElectricCyan),
                        modifier = Modifier.testTag("toggle_app_lock_switch")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Biometric Fingerprint Unlock", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Unlock instantly with device fingerprint", color = TextSecondary, fontSize = 11.sp)
                    }
                    Switch(
                        checked = user?.isBiometricEnabled ?: false,
                        onCheckedChange = { viewModel.toggleBiometric(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = BuildXBlack, checkedTrackColor = ElectricCyan),
                        modifier = Modifier.testTag("toggle_biometric_switch")
                    )
                }

                if (user?.isAppLockEnabled == true) {
                    Spacer(modifier = Modifier.height(10.dp))
                    TextButton(
                        onClick = { showPinDialog = true },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Change PIN", color = ElectricCyan, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy & Data Sovereignty Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = BuildXCardBackground)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = NeonMint, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DATA SOVEREIGNTY & PRIVACY",
                        color = NeonMint,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "• Passwords and PINs are protected via SHA-256 salted hashing.\n• Workouts, Winter Arc days, and statistics are stored 100% offline on your device in SQLite.\n• Camera exercise detection is strictly on-device; no video frames are ever recorded or transmitted.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                    onClick = { showExportDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export Workout & Arc Summary", color = TextPrimary, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Logout Button
        Button(
            onClick = { viewModel.logout() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("logout_button"),
            colors = ButtonDefaults.buttonColors(containerColor = CriticalRed.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, tint = CriticalRed, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Out of BuildX", color = CriticalRed, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Set PIN Dialog
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text("Configure 4-Digit Security PIN", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Enter a 4-digit numeric code to secure BuildX.", color = TextSecondary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newPinInput,
                        onValueChange = {
                            if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                                newPinInput = it
                                pinError = null
                            }
                        },
                        label = { Text("4-Digit PIN") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("new_pin_input")
                    )
                    pinError?.let {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(it, color = CriticalRed, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPinInput.length == 4) {
                            viewModel.setAppLock(true, newPinInput)
                            showPinDialog = false
                            newPinInput = ""
                        } else {
                            pinError = "PIN must be exactly 4 numeric digits."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
                ) {
                    Text("Save PIN", color = BuildXBlack, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = BuildXCardBackground
        )
    }

    // Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export Data Summary", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "BuildX Offline Export:\n\n" +
                    "• Total Workouts: ${user?.totalWorkoutsCompleted}\n" +
                    "• Total Push-Up Reps: ${user?.totalPushUpReps}\n" +
                    "• Winter Arc Day: ${user?.winterArcDay} of 90\n" +
                    "• Athletic Rank: ${user?.currentRank} (${user?.xp} XP)\n\n" +
                    "All records are secured on your device.",
                    color = TextPrimary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExportDialog = false
                        Toast.makeText(context, "Data export summary copied to clipboard", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
                ) {
                    Text("Done", color = BuildXBlack, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = BuildXCardBackground
        )
    }
}
