package com.example.data.local

object DefaultMotivation {
    private val quotes = listOf(
        "Discipline is choosing what you want most over what you want now.",
        "Your only competition is the person looking back in the mirror.",
        "Mastering your own bodyweight is the purest form of human strength.",
        "Every rep done with clean form builds iron tendon strength for life.",
        "Small, non-negotiable habits performed daily compound into unstoppable results.",
        "Rest and recovery are not weakness; they are the catalyst for muscle repair and growth.",
        "Show up on the days you feel average; that is where champions are forged.",
        "The bar does not negotiate, but neither do you.",
        "Calisthenics is freedom — no equipment can hold back a determined mind.",
        "Focus on the depth of the push-up, not just the number.",
        "Consistency over intensity. Build the habit before chasing the limit.",
        "Your body can stand almost anything; it is your mind you have to convince.",
        "True strength is moving through life with control, balance, and quiet confidence.",
        "The Winter Arc isn't a race; it's 90 days of deliberate self-evolution.",
        "Listen to your joints. Longevity in training beats short-term ego every single time.",
        "Clean nutrition and sufficient hydration make every single set feel light.",
        "When in doubt, breathe deeply, tighten the core, and execute the rep.",
        "Progress is rarely linear. Celebrate showing up today.",
        "Gravity is your weight stack. Master it one lever at a time.",
        "You don't need a gym to build a fortress."
    )

    fun getQuoteForDay(day: Int): String {
        val index = (day - 1).coerceAtLeast(0) % quotes.size
        return quotes[index]
    }
}
