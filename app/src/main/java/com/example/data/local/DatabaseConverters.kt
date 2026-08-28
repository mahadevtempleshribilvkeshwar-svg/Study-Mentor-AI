package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.AccountingWorkspaceType
import com.example.data.model.CommerceSubject
import com.example.data.model.EducationBoard
import com.example.data.model.ExamCategory

class DatabaseConverters {
    @TypeConverter
    fun fromEducationBoard(board: EducationBoard): String = board.name

    @TypeConverter
    fun toEducationBoard(value: String): EducationBoard = try {
        EducationBoard.valueOf(value)
    } catch (e: Exception) {
        EducationBoard.CBSE
    }

    @TypeConverter
    fun fromCommerceSubject(subject: CommerceSubject): String = subject.name

    @TypeConverter
    fun toCommerceSubject(value: String): CommerceSubject = try {
        CommerceSubject.valueOf(value)
    } catch (e: Exception) {
        CommerceSubject.ACCOUNTANCY
    }

    @TypeConverter
    fun fromExamCategory(category: ExamCategory): String = category.name

    @TypeConverter
    fun toExamCategory(value: String): ExamCategory = try {
        ExamCategory.valueOf(value)
    } catch (e: Exception) {
        ExamCategory.DAILY_REVISION
    }

    @TypeConverter
    fun fromWorkspaceType(type: AccountingWorkspaceType): String = type.name

    @TypeConverter
    fun toWorkspaceType(value: String): AccountingWorkspaceType = try {
        AccountingWorkspaceType.valueOf(value)
    } catch (e: Exception) {
        AccountingWorkspaceType.JOURNAL
    }

    @TypeConverter
    fun fromSchoolVerificationStatus(status: com.example.data.model.SchoolVerificationStatus): String = status.name

    @TypeConverter
    fun toSchoolVerificationStatus(value: String): com.example.data.model.SchoolVerificationStatus = try {
        com.example.data.model.SchoolVerificationStatus.valueOf(value)
    } catch (e: Exception) {
        com.example.data.model.SchoolVerificationStatus.VERIFIED
    }

    @TypeConverter
    fun fromUserRole(role: com.example.data.model.UserRole): String = role.name

    @TypeConverter
    fun toUserRole(value: String): com.example.data.model.UserRole = try {
        com.example.data.model.UserRole.valueOf(value)
    } catch (e: Exception) {
        com.example.data.model.UserRole.STUDENT
    }
}
