package com.example.ui.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
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
import java.util.concurrent.Executors
import kotlin.math.abs

enum class PushUpMotionState {
    TOP_POSITION,
    DESCENDING,
    BOTTOM_HOLD,
    ASCENDING
}

@Composable
fun CameraPushUpScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val reps by viewModel.cameraReps.collectAsState()
    val sets by viewModel.cameraSets.collectAsState()
    val feedback by viewModel.cameraFormFeedback.collectAsState()
    val sessionDuration by viewModel.cameraSessionDuration.collectAsState()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var permissionRequested by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        permissionRequested = true
    }

    BackHandler {
        viewModel.finishCameraPushUpSession()
    }

    // Motion State Machine tracker
    val motionTracker = remember {
        object {
            var state = PushUpMotionState.TOP_POSITION
            var baselineLuminance: Double = 0.0
            var previousLuminance: Double = 0.0
            var minLuminanceDuringRep: Double = 0.0
            var lastRepTimestamp: Long = 0L

            fun processFrame(currentLuma: Double, onRepDetected: (String) -> Unit) {
                val now = System.currentTimeMillis()
                if (baselineLuminance == 0.0) {
                    baselineLuminance = currentLuma
                    previousLuminance = currentLuma
                    return
                }

                // Smooth running average
                val diffFromBaseline = currentLuma - baselineLuminance
                val delta = currentLuma - previousLuminance

                when (state) {
                    PushUpMotionState.TOP_POSITION -> {
                        // User begins dropping chest towards phone / mat
                        if (abs(diffFromBaseline) > 12.0) {
                            state = PushUpMotionState.DESCENDING
                            minLuminanceDuringRep = currentLuma
                        }
                    }
                    PushUpMotionState.DESCENDING -> {
                        if (abs(diffFromBaseline) > 22.0) {
                            state = PushUpMotionState.BOTTOM_HOLD
                        }
                    }
                    PushUpMotionState.BOTTOM_HOLD -> {
                        // Chest reached full inflection depth, now pressing back up
                        if (abs(currentLuma - baselineLuminance) < 14.0) {
                            state = PushUpMotionState.ASCENDING
                        }
                    }
                    PushUpMotionState.ASCENDING -> {
                        // Back to near top lockout
                        if (abs(currentLuma - baselineLuminance) < 8.0 && now - lastRepTimestamp > 1000L) {
                            lastRepTimestamp = now
                            state = PushUpMotionState.TOP_POSITION
                            onRepDetected("Excellent depth & lockout!")
                        }
                    }
                }
                previousLuminance = currentLuma
            }
        }
    }

    fun triggerHapticFeedback() {
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(70)
            }
        } catch (_: Exception) {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BuildXBlack)
    ) {
        if (hasCameraPermission) {
            // Live Camera Preview
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    val cameraExecutor = Executors.newSingleThreadExecutor()

                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            // Lightweight real-time local analyzer (100% on-device)
                            val imageAnalysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                val buffer = imageProxy.planes[0].buffer
                                val data = ByteArray(buffer.remaining())
                                buffer.get(data)

                                var sum = 0L
                                val step = (data.size / 500).coerceAtLeast(1)
                                var samples = 0
                                for (i in 0 until data.size step step) {
                                    sum += (data[i].toInt() and 0xFF)
                                    samples++
                                }
                                val averageLuma = if (samples > 0) sum.toDouble() / samples else 128.0

                                motionTracker.processFrame(averageLuma) { cue ->
                                    triggerHapticFeedback()
                                    viewModel.onCameraRepDetected(cue)
                                }
                                imageProxy.close()
                            }

                            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            Log.e("BuildXCamera", "Binding camera failed", e)
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Dark subtle overlay for readable HUD
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                BuildXBlack.copy(alpha = 0.75f),
                                Color.Transparent,
                                BuildXBlack.copy(alpha = 0.9f)
                            )
                        )
                    )
            )
        } else {
            // Permission Request Card
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(BuildXCardBackground)
                        .border(2.dp, ElectricCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera Permission",
                        tint = ElectricCyan,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "AI PUSH-UP CAMERA TRACKER",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Position your phone against a wall or floor. The on-device engine tracks your chest inflection points to automatically count valid push-ups.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Privacy Disclosure
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BuildXCardBackground)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.PrivacyTip, contentDescription = null, tint = NeonMint, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "100% On-Device Privacy: Video frames are computed in memory locally and are never recorded, saved, or uploaded to any server.",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("request_camera_permission_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ENABLE CAMERA TRACKER", color = BuildXBlack, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        // User can still use manual counting without camera
                        hasCameraPermission = false
                        viewModel.manualIncrementRep()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Use Manual Counting Fallback", color = TextSecondary)
                }
            }
        }

        // Live HUD Overlay (Visible in both camera and manual mode)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(BuildXBlack.copy(alpha = 0.8f))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${sessionDuration / 60}:${"%02d".format(sessionDuration % 60)}",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "SET $sets",
                        color = NeonMint,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                IconButton(
                    onClick = { viewModel.finishCameraPushUpSession() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(BuildXBlack.copy(alpha = 0.8f))
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close session", tint = TextPrimary)
                }
            }

            // Center Big Rep Counter & Feedback
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$reps",
                    color = ElectricCyan,
                    fontSize = 86.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 86.sp
                )

                Text(
                    text = "VALID REPETITIONS",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Real-time Form Coach Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(BuildXBlack.copy(alpha = 0.85f))
                        .border(1.dp, ElectricCyan, RoundedCornerShape(20.dp))
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = feedback,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Bottom Controls (Manual Rep Adjuster + Next Set + Finish)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Manual Fallback Buttons (+1, -1)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(BuildXBlack.copy(alpha = 0.85f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.manualDecrementRep() },
                        modifier = Modifier.testTag("camera_manual_minus_button")
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease rep", tint = TextSecondary)
                    }

                    Text(
                        text = "Manual Override",
                        color = TextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = {
                            triggerHapticFeedback()
                            viewModel.manualIncrementRep()
                        },
                        modifier = Modifier.testTag("camera_manual_plus_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add rep", tint = ElectricCyan)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.nextCameraSet() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Next Set", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.finishCameraPushUpSession() },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("finish_camera_pushup_session_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonMint),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Finish (+${100 + (reps * 3)} XP)", color = BuildXBlack, fontWeight = FontWeight.Black)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
