package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.StudentAcademicProfile
import com.example.data.model.TestAttemptRecord
import com.example.data.model.UserProfile

@Database(
    entities = [
        UserProfile::class,
        StudentAcademicProfile::class,
        ChapterProgressEntity::class,
        WeakTopicEntity::class,
        TestAttemptRecord::class,
        WorkspaceDraftEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun progressDao(): ProgressDao
    abstract fun testAttemptDao(): TestAttemptDao
    abstract fun workspaceDao(): WorkspaceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "commerce_master_db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
