package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.AccountingWorkspaceType

@Entity(tableName = "chapter_progress")
data class ChapterProgressEntity(
    @PrimaryKey val chapterId: String,
    val isCompleted: Boolean = false,
    val masteryPercentage: Int = 0,
    val lastRevisedTimestamp: Long = System.currentTimeMillis(),
    val notesCount: Int = 0
)

@Entity(tableName = "weak_topics")
data class WeakTopicEntity(
    @PrimaryKey val topicName: String,
    val chapterName: String,
    val errorCount: Int = 1,
    val masteryLevel: Int = 45, // 0 - 100
    val lastTestedDate: Long = System.currentTimeMillis()
)

@Entity(tableName = "workspace_drafts")
data class WorkspaceDraftEntity(
    @PrimaryKey val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val workspaceType: AccountingWorkspaceType,
    val contentJson: String,
    val savedAtTimestamp: Long = System.currentTimeMillis()
)
