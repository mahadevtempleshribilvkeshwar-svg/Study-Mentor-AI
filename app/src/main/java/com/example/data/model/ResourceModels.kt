package com.example.data.model

enum class VideoLectureType(val label: String) {
    FULL_CHAPTER("Full Chapter Lecture"),
    ONE_SHOT("One-Shot Marathon"),
    CONCEPT("Concept Breakdown"),
    NUMERICAL_PRACTICE("Numerical Practice"),
    PYQ_DISCUSSION("PYQ Discussion & Solutions"),
    REVISION("Fast Revision"),
    EXAM_STRATEGY("Exam Presentation Strategy")
}

data class TeacherVideoResource(
    val id: String,
    val teacherName: String,
    val title: String,
    val chapterName: String,
    val topic: String,
    val lectureType: VideoLectureType,
    val durationText: String,
    val publicUrl: String
)

data class TeacherNotesResource(
    val id: String,
    val teacherOrSource: String,
    val title: String,
    val chapterName: String,
    val description: String,
    val officialResourceUrl: String
)

data class AiRevisionCheatSheet(
    val id: String,
    val chapterName: String,
    val title: String,
    val keyFormulas: List<String>,
    val accountingRules: List<String>,
    val examMistakeAlerts: List<String>,
    val stepByStepFormat: String
)
