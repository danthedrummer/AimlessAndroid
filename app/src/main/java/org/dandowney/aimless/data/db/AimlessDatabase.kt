package org.dandowney.aimless.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.dandowney.aimless.data.db.dao.SessionDao
import org.dandowney.aimless.data.db.entity.SessionEntity

@Database(
  entities = [
    SessionEntity::class,
  ],
  version = 1,
)
internal abstract class AimlessDatabase : RoomDatabase() {

  abstract fun sessionDao(): SessionDao
}
