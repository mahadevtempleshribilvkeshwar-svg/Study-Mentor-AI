package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.TestAttemptRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM chapter_progress")
    fun getAllChapterProgress(): Flow<List<ChapterProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChapterProgress(progress: ChapterProgressEntity)

    @Query("SELECT * FROM weak_topics ORDER BY masteryLevel ASC")
    fun getWeakTopics(): Flow<List<WeakTopicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeakTopic(weakTopic: WeakTopicEntity)

    @Query("DELETE FROM weak_topics WHERE topicName = :topicName")
    suspend fun removeWeakTopic(topicName: String)
}

@Dao
interface TestAttemptDao {
    @Query("SELECT * FROM test_attempts ORDER BY dateTimestamp DESC")
    fun getAllTestAttempts(): Flow<List<TestAttemptRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestAttempt(attempt: TestAttemptRecord)

    @Query("SELECT AVG(percentage) FROM test_attempts")
    suspend fun getAverageScore(): Double?

    @Query("SELECT COUNT(*) FROM test_attempts")
    suspend fun getTotalTestsCount(): Int
}

@Dao
interface WorkspaceDao {
    @Query("SELECT * FROM workspace_drafts ORDER BY savedAtTimestamp DESC")
    fun getAllWorkspaceDrafts(): Flow<List<WorkspaceDraftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWorkspaceDraft(draft: WorkspaceDraftEntity)

    @Query("DELETE FROM workspace_drafts WHERE id = :id")
    suspend fun deleteDraft(id: String)
}
