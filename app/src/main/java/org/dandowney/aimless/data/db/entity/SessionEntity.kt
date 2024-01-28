package org.dandowney.aimless.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
  tableName = "session",
  primaryKeys = ["activity_id", "start_time", "end_time"],
)
data class SessionEntity(

  @ColumnInfo(name = "activity_id")
  val aimlessId: String,

  @ColumnInfo(name = "start_time")
  val startTime: Long,

  @ColumnInfo(name = "end_time")
  val endTime: Long,
)
