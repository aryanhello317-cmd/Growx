package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.DailyWinterArcEntity
import com.example.data.model.PersonalMilestoneEntity
import com.example.data.model.UserEntity
import com.example.data.model.WorkoutEntity

@Database(
    entities = [
        UserEntity::class,
        WorkoutEntity::class,
        DailyWinterArcEntity::class,
        PersonalMilestoneEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BuildXDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun winterArcDao(): WinterArcDao
    abstract fun milestoneDao(): MilestoneDao

    companion object {
        @Volatile
        private var INSTANCE: BuildXDatabase? = null

        fun getInstance(context: Context): BuildXDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BuildXDatabase::class.java,
                    "buildx_offline.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
