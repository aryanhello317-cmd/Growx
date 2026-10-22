package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PersonalMilestoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {
    @Query("SELECT * FROM personal_milestones ORDER BY targetDay ASC, id ASC")
    fun getAllMilestonesFlow(): Flow<List<PersonalMilestoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(milestones: List<PersonalMilestoneEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: PersonalMilestoneEntity): Long

    @Update
    suspend fun updateMilestone(milestone: PersonalMilestoneEntity)

    @Query("UPDATE personal_milestones SET isAchieved = :isAchieved, dateAchieved = :dateAchieved WHERE id = :id")
    suspend fun toggleAchieved(id: Long, isAchieved: Boolean, dateAchieved: Long)

    @Query("DELETE FROM personal_milestones WHERE id = :id")
    suspend fun deleteMilestone(id: Long)

    @Query("DELETE FROM personal_milestones")
    suspend fun clearMilestones()
}
