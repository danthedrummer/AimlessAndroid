package org.dandowney.aimless.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.dandowney.aimless.data.db.entity.SessionEntity

@Dao
interface SessionDao {

  @Insert
  suspend fun createSession(sessionEntity: SessionEntity)

  @Query("SELECT * FROM session WHERE activity_id=:activityId")
  suspend fun sessionsFor(activityId: String): List<SessionEntity>

  @Query("SELECT SUM(end_time - start_time) FROM session")
  suspend fun totalPlaytime(): Long

  @Query("SELECT SUM(end_time - start_time) FROM session WHERE activity_id=:activityId")
  suspend fun totalPlaytimeFor(activityId: String): Long
}
