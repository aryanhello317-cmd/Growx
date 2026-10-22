package com.example.data.local

import com.example.data.model.Exercise

object DefaultExercises {
    val list: List<Exercise> = listOf(
        // Push Exercises
        Exercise(
            id = "push_standard",
            name = "Standard Push-Ups",
            category = "Push",
            level = "Beginner",
            sets = 3,
            repsOrHold = "12 - 15 Reps",
            restSeconds = 60,
            targetMuscles = "Chest, Triceps, Anterior Deltoids, Core",
            instructions = listOf(
                "Place hands slightly wider than shoulder-width, fingers pointing forward or slightly outward.",
                "Maintain a rigid plank line from head to heels; engage glutes and core.",
                "Lower with elbows tucked at a 45-degree angle until chest is 2-3 inches off the ground.",
                "Press the floor away aggressively back to full elbow lockout without flaring elbows."
            ),
            formCheckpoints = listOf(
                "No sagging lower back (posterior pelvic tilt).",
                "Elbows at 45 degrees, never 90-degree T-shape.",
                "Full range of motion: chest low, full extension at top."
            ),
            safetyTips = "If your wrists hurt, use push-up handles or clenched fists on a mat.",
            easierAlternative = "Knee Push-Ups or Incline Push-Ups against a sturdy table.",
            isCameraSupported = true
        ),
        Exercise(
            id = "push_knee",
            name = "Knee Push-Ups",
            category = "Push",
            level = "Beginner",
            sets = 3,
            repsOrHold = "10 - 15 Reps",
            restSeconds = 45,
            targetMuscles = "Chest, Anterior Deltoids, Triceps",
            instructions = listOf(
                "Kneel on a comfortable mat with hands shoulder-width apart.",
                "Keep body straight from knees to shoulders; do not leave hips in the air.",
                "Lower chest smoothly towards the ground.",
                "Press up evenly through the palms."
            ),
            formCheckpoints = listOf(
                "Straight line from knees through hips to shoulders.",
                "Controlled 2-second descent."
            ),
            safetyTips = "Place a folded towel or cushion under your knees for joint comfort.",
            easierAlternative = "Wall Push-Ups.",
            isCameraSupported = true
        ),
        Exercise(
            id = "push_diamond",
            name = "Diamond Push-Ups",
            category = "Push",
            level = "Intermediate",
            sets = 4,
            repsOrHold = "8 - 12 Reps",
            restSeconds = 75,
            targetMuscles = "Triceps (Inner Head), Inner Pectorals, Core",
            instructions = listOf(
                "Bring index fingers and thumbs together under your center chest forming a diamond shape.",
                "Brace your core and assume a solid high plank position.",
                "Lower your sternum directly toward the center of your diamond.",
                "Drive through the palms and lock out triceps at the top."
            ),
            formCheckpoints = listOf(
                "Keep core tight to prevent pelvic drop.",
                "Elbows track backward naturally."
            ),
            safetyTips = "Warm up your wrists thoroughly with wrist circles and stretches.",
            easierAlternative = "Close-Grip Push-Ups (hands 4-6 inches apart).",
            isCameraSupported = true
        ),
        Exercise(
            id = "push_archer",
            name = "Archer Push-Ups",
            category = "Push",
            level = "Advanced",
            sets = 4,
            repsOrHold = "6 - 8 Reps / Side",
            restSeconds = 90,
            targetMuscles = "Chest, Unilateral Triceps, Scapular Stabilizers",
            instructions = listOf(
                "Take a very wide hand placement with fingers rotated outward.",
                "Shift your body weight towards one side as you lower down, keeping the opposite arm completely straight.",
                "Press back up from the working arm to the center position.",
                "Alternate sides with control."
            ),
            formCheckpoints = listOf(
                "The assisted arm stays fully straight at the elbow.",
                "Keep hips level and do not twist violently."
            ),
            safetyTips = "Do not force range of motion if you feel sharp anterior shoulder strain.",
            easierAlternative = "Wide-Grip Push-Ups.",
            isCameraSupported = true
        ),
        Exercise(
            id = "push_pseudo_planche",
            name = "Pseudo-Planche Push-Ups",
            category = "Push",
            level = "Advanced",
            sets = 4,
            repsOrHold = "6 - 10 Reps",
            restSeconds = 90,
            targetMuscles = "Biceps Tendon, Anterior Deltoids, Upper Chest, Core",
            instructions = listOf(
                "Hands placed down near your hips with fingers turned out 45 degrees.",
                "Lean your shoulders forward so they are ahead of your wrists.",
                "Maintain protracted hollow-body shoulders as you lower.",
                "Push up and aggressively protract the shoulder blades at top."
            ),
            formCheckpoints = listOf(
                "Maintain forward lean throughout entire range of motion.",
                "Hollow body core engagement."
            ),
            safetyTips = "Begin with modest forward lean to condition your wrist and bicep tendons.",
            easierAlternative = "Diamond Push-Ups with forward weight shift.",
            isCameraSupported = false
        ),

        // Pull Progressions
        Exercise(
            id = "pull_australian",
            name = "Australian Bodyweight Rows",
            category = "Pull",
            level = "Beginner",
            sets = 3,
            repsOrHold = "10 - 12 Reps",
            restSeconds = 60,
            targetMuscles = "Rhomboids, Middle Trapezius, Latissimus Dorsi, Biceps",
            instructions = listOf(
                "Position yourself beneath a waist-height bar, sturdy low table, or suspension straps.",
                "Grip shoulder-width, heels on floor, body straight as an inverted plank.",
                "Pull your chest up until it touches the bar, squeezing shoulder blades together.",
                "Lower with control over 2 seconds."
            ),
            formCheckpoints = listOf(
                "Retract scapulae at the peak of the pull.",
                "Do not let hips sag or hike."
            ),
            safetyTips = "Check table or bar stability before applying full bodyweight.",
            easierAlternative = "Doorframe Isometric Rows with bent knees.",
            isCameraSupported = false
        ),
        Exercise(
            id = "pull_negative",
            name = "Negative Pull-Ups",
            category = "Pull",
            level = "Beginner",
            sets = 4,
            repsOrHold = "5 Reps (5s Eccentric)",
            restSeconds = 90,
            targetMuscles = "Lats, Biceps, Upper Back, Grip",
            instructions = listOf(
                "Stand on a box or jump up so your chin is comfortably over the pull-up bar.",
                "Stabilize your body at the top without swinging.",
                "Resist gravity as slowly as possible for 4 to 6 continuous seconds until elbows are straight.",
                "Step back down, reset, and repeat."
            ),
            formCheckpoints = listOf(
                "Consistent tempo; do not drop during the bottom half.",
                "Keep core tight to eliminate swinging."
            ),
            safetyTips = "Use a solid chair or box to step up rather than risking a reckless jump.",
            easierAlternative = "Australian Bodyweight Rows.",
            isCameraSupported = false
        ),
        Exercise(
            id = "pull_standard",
            name = "Standard Pull-Ups",
            category = "Pull",
            level = "Intermediate",
            sets = 4,
            repsOrHold = "6 - 10 Reps",
            restSeconds = 90,
            targetMuscles = "Latissimus Dorsi, Teres Major, Biceps, Core",
            instructions = listOf(
                "Overhand grip (palms away) slightly wider than shoulder-width.",
                "Start from a complete dead hang with engaged shoulders.",
                "Drive elbows down and back to hoist your chest towards the bar.",
                "Clear chin over bar, then lower with control to dead hang."
            ),
            formCheckpoints = listOf(
                "No kipping or leg kicking.",
                "Full range from dead hang to chin clearly above bar."
            ),
            safetyTips = "Engage your scapulae before initiating the elbow bend.",
            easierAlternative = "Resistance Band Assisted Pull-Ups or Negative Pull-Ups.",
            isCameraSupported = false
        ),

        // Dips & Triceps
        Exercise(
            id = "dips_bench",
            name = "Bench / Chair Dips",
            category = "Push",
            level = "Beginner",
            sets = 3,
            repsOrHold = "12 - 15 Reps",
            restSeconds = 60,
            targetMuscles = "Triceps, Anterior Deltoids, Lower Chest",
            instructions = listOf(
                "Sit on edge of a secure bench or sturdy chair, hands placed beside hips.",
                "Walk feet forward, keeping back close to the edge of the seat.",
                "Lower hips by bending elbows until upper arms are roughly parallel to floor (90 degrees).",
                "Press up forcefully through palms."
            ),
            formCheckpoints = listOf(
                "Keep torso close to the bench; do not drift forward.",
                "Do not dip lower than 90 degrees to protect shoulders."
            ),
            safetyTips = "Keep knees bent at 90 degrees if straight legs cause shoulder pinching.",
            easierAlternative = "Knee Push-Ups.",
            isCameraSupported = false
        ),
        Exercise(
            id = "dips_parallel",
            name = "Parallel Bar Dips",
            category = "Push",
            level = "Intermediate",
            sets = 4,
            repsOrHold = "8 - 12 Reps",
            restSeconds = 90,
            targetMuscles = "Triceps, Lower Pectorals, Anterior Deltoids",
            instructions = listOf(
                "Mount parallel bars or two sturdy counter corners with arms locked out.",
                "Lean slightly forward (15-20 degrees) to engage chest fibers.",
                "Lower until shoulders are level with or slightly below elbows.",
                "Drive down into the bars to lock out arms."
            ),
            formCheckpoints = listOf(
                "Controlled tempo without bouncing in the bottom pocket.",
                "Keep shoulders depressed away from your ears."
            ),
            safetyTips = "Avoid excessive shoulder depression if you have prior rotator cuff impingement.",
            easierAlternative = "Bench Dips with elevated feet.",
            isCameraSupported = false
        ),

        // Legs & Lower Body
        Exercise(
            id = "legs_squat",
            name = "Deep Bodyweight Squats",
            category = "Legs",
            level = "Beginner",
            sets = 4,
            repsOrHold = "15 - 20 Reps",
            restSeconds = 45,
            targetMuscles = "Quadriceps, Glutes, Hamstrings, Calves",
            instructions = listOf(
                "Stand feet shoulder-width apart, toes turned slightly outward 15-30 degrees.",
                "Hinge at hips and bend knees simultaneously, keeping chest proud.",
                "Descend until hip crease is below knee level (below parallel).",
                "Drive through whole foot to stand tall and squeeze glutes."
            ),
            formCheckpoints = listOf(
                "Heels glued to the floor at all times.",
                "Knees track over toes, no knee valgus (caving inward)."
            ),
            safetyTips = "If heels lift, elevate them slightly on a small book or mat edge.",
            easierAlternative = "Box Squats to a sturdy chair.",
            isCameraSupported = false
        ),
        Exercise(
            id = "legs_bulgarian",
            name = "Bulgarian Split Squats",
            category = "Legs",
            level = "Intermediate",
            sets = 3,
            repsOrHold = "10 - 12 Reps / Leg",
            restSeconds = 60,
            targetMuscles = "Glutes, Quadriceps, Hip Stabilizers",
            instructions = listOf(
                "Elevate rear foot on a chair, couch, or bench behind you.",
                "Step front foot forward into a lunge stance.",
                "Lower your rear knee straight down toward the ground.",
                "Drive up through the front heel to return to standing."
            ),
            formCheckpoints = listOf(
                "Front knee tracks directly over middle toes.",
                "Torso slightly pitched forward to load glutes."
            ),
            safetyTips = "Focus on balance before chasing high repetitions.",
            easierAlternative = "Standard Reverse Walking Lunges.",
            isCameraSupported = false
        ),
        Exercise(
            id = "legs_pistol",
            name = "Pistol Squat Progression",
            category = "Legs",
            level = "Advanced",
            sets = 3,
            repsOrHold = "5 - 8 Reps / Leg",
            restSeconds = 90,
            targetMuscles = "Quadriceps, Gluteus Medius, Core, Ankle Mobility",
            instructions = listOf(
                "Stand on one foot with the other leg extended straight in front.",
                "Descend smoothly on the working leg into a full deep single-leg squat.",
                "Keep working heel flat and torso balanced.",
                "Power back up to full standing position."
            ),
            formCheckpoints = listOf(
                "Extended leg never touches the floor.",
                "Working knee stays aligned over toes."
            ),
            safetyTips = "Use a doorframe or chair for fingertip assistance while learning.",
            easierAlternative = "Assisted Pistol Squats holding a door frame or chair.",
            isCameraSupported = false
        ),

        // Core & Planks
        Exercise(
            id = "core_plank",
            name = "Hollow-Body Plank",
            category = "Core",
            level = "Beginner",
            sets = 3,
            repsOrHold = "45 - 60 Seconds",
            restSeconds = 45,
            targetMuscles = "Rectus Abdominis, Transverse Abdominis, Serratus Anterior",
            instructions = listOf(
                "Assume forearm plank position with elbows directly under shoulders.",
                "Actively round upper back slightly (protraction) and tuck tailbone under (posterior tilt).",
                "Contract quadriceps, glutes, and abs with maximum tension.",
                "Breathe smoothly while maintaining ironclad rigidity."
            ),
            formCheckpoints = listOf(
                "Zero arch or sag in lumbar spine.",
                "Forearms pressing hard into the floor."
            ),
            safetyTips = "If lower back aches, immediately drop knees for a 5-second rest.",
            easierAlternative = "Knee Forearm Plank.",
            isCameraSupported = false
        ),
        Exercise(
            id = "core_hollow_hold",
            name = "Hollow Body Rock / Hold",
            category = "Core",
            level = "Intermediate",
            sets = 3,
            repsOrHold = "30 - 45 Seconds",
            restSeconds = 60,
            targetMuscles = "Deep Core, Hip Flexors, Calisthenics Tension",
            instructions = listOf(
                "Lie flat on your back, arms extended overhead, legs straight and together.",
                "Press lower back firmly into the floor so no space remains beneath.",
                "Lift shoulder blades and feet 4-6 inches off the ground.",
                "Hold this banana-shaped arc without letting the lower back lift."
            ),
            formCheckpoints = listOf(
                "Lumbar spine glued to the mat.",
                "Toes pointed, glutes squeezed."
            ),
            safetyTips = "Tuck knees to chest if lower back starts leaving the floor.",
            easierAlternative = "Tuck Hollow Body Hold (knees bent at 90 degrees).",
            isCameraSupported = false
        ),
        Exercise(
            id = "core_hanging_leg_raises",
            name = "Floor / Hanging Leg Raises",
            category = "Core",
            level = "Intermediate",
            sets = 3,
            repsOrHold = "10 - 15 Reps",
            restSeconds = 60,
            targetMuscles = "Lower Rectus Abdominis, Hip Flexors, Obliques",
            instructions = listOf(
                "Lie flat on back or hang from a bar with active shoulders.",
                "Keep legs straight and raise them until perpendicular to the floor (or touching bar).",
                "Tilt pelvis up at the top to fully contract the lower abs.",
                "Lower with continuous eccentric control without touching floor."
            ),
            formCheckpoints = listOf(
                "No swinging or momentum.",
                "Controlled 2-second negative."
            ),
            safetyTips = "Keep knees slightly bent if you experience hamstring tightness.",
            easierAlternative = "Lying Knee Tucks.",
            isCameraSupported = false
        ),

        // Skills & Calisthenics Mastery
        Exercise(
            id = "skill_crow_pose",
            name = "Crow Pose Balance",
            category = "Skills",
            level = "Intermediate",
            sets = 4,
            repsOrHold = "15 - 30s Hold",
            restSeconds = 60,
            targetMuscles = "Wrist Flexors, Anterior Deltoids, Core, Scapular Retractors",
            instructions = listOf(
                "Squat down and place hands shoulder-width on the floor, fingers spread wide.",
                "Rest knees on the backs of your triceps or armpits.",
                "Shift weight forward into hands, looking slightly ahead (not straight down).",
                "Float one foot off floor, then the other, balancing on palms."
            ),
            formCheckpoints = listOf(
                "Grip floor with fingertips (spider fingers) for balance adjustments.",
                "Gaze forward, keeping chest elevated."
            ),
            safetyTips = "Place a soft pillow in front of your head for confidence while practicing balance.",
            easierAlternative = "Frog Stand with toes touching ground lightly.",
            isCameraSupported = false
        ),
        Exercise(
            id = "skill_handstand_wall",
            name = "Chest-to-Wall Handstand Hold",
            category = "Skills",
            level = "Intermediate",
            sets = 4,
            repsOrHold = "20 - 45s Hold",
            restSeconds = 90,
            targetMuscles = "Trapezius, Anterior Deltoids, Wrist Stabilizers, Core",
            instructions = listOf(
                "Place hands 6-12 inches from wall and wall-walk feet up so your chest faces the wall.",
                "Push tall through shoulders, elevating scapulae up to ears.",
                "Tuck pelvis into hollow body; only toes and nose touch or hover near wall.",
                "Breathe calmly and step down safely when fatigued."
            ),
            formCheckpoints = listOf(
                "No banana back (keep ribs pulled down).",
                "Actively pushing floor away."
            ),
            safetyTips = "Never hold until failure; step down with 5-10 seconds of reserve energy.",
            easierAlternative = "Pike Handstand Hold with feet elevated on a bed or chair.",
            isCameraSupported = false
        ),
        Exercise(
            id = "skill_lsit",
            name = "L-Sit Hold on Floor / Parallettes",
            category = "Skills",
            level = "Advanced",
            sets = 4,
            repsOrHold = "10 - 20s Hold",
            restSeconds = 90,
            targetMuscles = "Triceps, Pectoralis Minor, Hip Flexors, Rectus Abdominis",
            instructions = listOf(
                "Sit with legs straight in front, hands beside hips on floor or parallettes.",
                "Depress shoulders forcefully, pushing the ground away.",
                "Engage hip flexors and quads to raise straight legs off the ground into an 'L' shape.",
                "Lock knees and point toes forward."
            ),
            formCheckpoints = listOf(
                "Depressed shoulders (neck long).",
                "Legs locked straight at 90-degree angle."
            ),
            safetyTips = "Warm up wrists and hip flexors thoroughly before attempts.",
            easierAlternative = "Tuck L-Sit with knees bent against chest.",
            isCameraSupported = false
        )
    )
}
