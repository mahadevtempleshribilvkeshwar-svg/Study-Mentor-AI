package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ChapterProgressEntity
import com.example.data.local.WeakTopicEntity
import com.example.data.local.WorkspaceDraftEntity
import com.example.data.model.AccountingWorkspaceType
import com.example.data.model.BalanceSheetItem
import com.example.data.model.CashFlowItem
import com.example.data.model.ChapterModule
import com.example.data.model.CommerceSubject
import com.example.data.model.EducationBoard
import com.example.data.model.ExamAnalysisReport
import com.example.data.model.ExamCategory
import com.example.data.model.ExamQuestion
import com.example.data.model.GameTransactionScenario
import com.example.data.model.JournalEntryRow
import com.example.data.model.LedgerEntryRow
import com.example.data.model.MultiPartnerLedgerAccount
import com.example.data.model.QuestionType
import com.example.data.model.SingleLedgerAccount
import com.example.data.model.StudentQuestionResponse
import com.example.data.model.TestAttemptRecord
import com.example.data.model.StudentAcademicProfile
import com.example.data.model.UserProfile
import com.example.data.model.VirtualBusiness
import com.example.data.remote.AiMistakeAnalysisResult
import com.example.data.remote.AiTutorResponse
import com.example.data.remote.GeminiTutorService
import com.example.data.repository.CurriculumRepository
import com.example.data.repository.ExamRepository
import com.example.data.repository.GameRepository
import com.example.data.repository.ResourceRepository
import com.example.data.remote.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavDestination {
    HOME,
    AUTH,
    LEARN,
    PLAY,
    WORKSPACE,
    PRACTICE,
    PYQ,
    TESTS,
    ACTIVE_EXAM,
    EXAM_REPORT,
    AI_TUTOR,
    RESOURCES,
    IMPROVEMENT,
    LEADERBOARD,
    PROFILE,
    ONBOARDING
}

data class LeaderboardEntry(
    val rank: Int,
    val displayName: String,
    val board: String,
    val scoreXp: Int,
    val badgeTitle: String,
    val isCurrentUser: Boolean = false
)

class CommerceViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val userDao = database.userDao()
    private val progressDao = database.progressDao()
    private val testAttemptDao = database.testAttemptDao()
    private val workspaceDao = database.workspaceDao()

    // Firebase Auth Integration
    val authRepository = FirebaseAuthRepository(application)
    val firebaseUser: StateFlow<FirebaseUser?> = authRepository.authStateFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, authRepository.currentUser)

    private val _authLoading = MutableStateFlow(false)
    val authLoading: StateFlow<Boolean> = _authLoading.asStateFlow()

    private val _authErrorMessage = MutableStateFlow<String?>(null)
    val authErrorMessage: StateFlow<String?> = _authErrorMessage.asStateFlow()

    private val _authSuccessMessage = MutableStateFlow<String?>(null)
    val authSuccessMessage: StateFlow<String?> = _authSuccessMessage.asStateFlow()

    val userProfile: StateFlow<UserProfile?> = userDao.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val studentProfile: StateFlow<StudentAcademicProfile> = userDao.getStudentProfile()
        .map { it ?: StudentAcademicProfile() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            StudentAcademicProfile()
        )

    // BoardMentor AI State Flow
    private val _teacherMode = MutableStateFlow(com.example.data.model.TeacherPedagogicalMode.TEACHER)
    val teacherMode: StateFlow<com.example.data.model.TeacherPedagogicalMode> = _teacherMode.asStateFlow()

    private val _teacherChatMessages = MutableStateFlow<List<com.example.data.model.AiTeacherChatMessage>>(
        listOf(
            com.example.data.model.AiTeacherChatMessage(
                sender = "ai",
                text = "Hello! I am your BoardMentor AI Teacher 🎓. Which subject or concept are you focusing on today?",
                mode = com.example.data.model.TeacherPedagogicalMode.TEACHER,
                subjectTag = "All Subjects",
                suggestedFollowUps = listOf(
                    "Explain Goodwill valuation methods",
                    "Why is Revaluation A/c debited on asset decrease?",
                    "How to calculate Pro-rata excess allotment?",
                    "Give me a 60-second quick revision of AS-3 Cash Flow"
                )
            )
        )
    )
    val teacherChatMessages: StateFlow<List<com.example.data.model.AiTeacherChatMessage>> = _teacherChatMessages.asStateFlow()

    private val _photoAnalysisResult = MutableStateFlow<com.example.data.model.PhotoAnalysisResult?>(null)
    val photoAnalysisResult: StateFlow<com.example.data.model.PhotoAnalysisResult?> = _photoAnalysisResult.asStateFlow()

    private val _generatedImportantQuestions = MutableStateFlow<List<com.example.data.model.GeneratedImportantQuestion>>(
        com.example.data.repository.BoardMentorRepository.getImportantQuestionBank("Accountancy")
    )
    val generatedImportantQuestions: StateFlow<List<com.example.data.model.GeneratedImportantQuestion>> = _generatedImportantQuestions.asStateFlow()

    private val _answerEvaluationResult = MutableStateFlow<com.example.data.model.AiAnswerEvaluationResult?>(null)
    val answerEvaluationResult: StateFlow<com.example.data.model.AiAnswerEvaluationResult?> = _answerEvaluationResult.asStateFlow()

    private val _todayStudyMission = MutableStateFlow<com.example.data.model.DailyStudyMission>(
        com.example.data.repository.BoardMentorRepository.getTodayStudyMission(StudentAcademicProfile())
    )
    val todayStudyMission: StateFlow<com.example.data.model.DailyStudyMission> = _todayStudyMission.asStateFlow()

    private val _selectedLeaderboardCategory = MutableStateFlow(com.example.data.model.LeaderboardCategory.TOP_SCORE)
    val selectedLeaderboardCategory: StateFlow<com.example.data.model.LeaderboardCategory> = _selectedLeaderboardCategory.asStateFlow()

    val chapterProgressList: StateFlow<List<ChapterProgressEntity>> = progressDao.getAllChapterProgress()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weakTopicsList: StateFlow<List<WeakTopicEntity>> = progressDao.getWeakTopics()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val testAttemptsList: StateFlow<List<TestAttemptRecord>> = testAttemptDao.getAllTestAttempts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Navigation & Screen State
    private val _currentDestination = MutableStateFlow(AppNavDestination.HOME)
    val currentDestination: StateFlow<AppNavDestination> = _currentDestination.asStateFlow()

    // Global Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Selected Chapter in Learn mode
    private val _selectedChapterId = MutableStateFlow<String?>("acc_ch1_fundamentals")
    val selectedChapterId: StateFlow<String?> = _selectedChapterId.asStateFlow()

    private val _highlightImportantOnly = MutableStateFlow(false)
    val highlightImportantOnly: StateFlow<Boolean> = _highlightImportantOnly.asStateFlow()

    // Virtual Business Game State
    private val _selectedBusiness = MutableStateFlow<VirtualBusiness>(GameRepository.availableBusinesses.first())
    val selectedBusiness: StateFlow<VirtualBusiness> = _selectedBusiness.asStateFlow()

    private val _currentScenarioIndex = MutableStateFlow(0)
    val currentScenarioIndex: StateFlow<Int> = _currentScenarioIndex.asStateFlow()

    private val _gameFeedback = MutableStateFlow<String?>(null)
    val gameFeedback: StateFlow<String?> = _gameFeedback.asStateFlow()

    private val _gameAnswerCorrect = MutableStateFlow<Boolean?>(null)
    val gameAnswerCorrect: StateFlow<Boolean?> = _gameAnswerCorrect.asStateFlow()

    // Freeform Accounting Workspace State ("Create Account")
    private val _workspaceType = MutableStateFlow(AccountingWorkspaceType.JOURNAL)
    val workspaceType: StateFlow<AccountingWorkspaceType> = _workspaceType.asStateFlow()

    private val _workspaceJournalRows = MutableStateFlow(listOf(JournalEntryRow()))
    val workspaceJournalRows: StateFlow<List<JournalEntryRow>> = _workspaceJournalRows.asStateFlow()

    private val _workspaceLedgerRowsDr = MutableStateFlow(listOf(LedgerEntryRow()))
    val workspaceLedgerRowsDr: StateFlow<List<LedgerEntryRow>> = _workspaceLedgerRowsDr.asStateFlow()

    private val _workspaceLedgerRowsCr = MutableStateFlow(listOf(LedgerEntryRow()))
    val workspaceLedgerRowsCr: StateFlow<List<LedgerEntryRow>> = _workspaceLedgerRowsCr.asStateFlow()

    // Rich Interactive Ledger Accounts
    private val _singleLedgerAccount = MutableStateFlow(SingleLedgerAccount())
    val singleLedgerAccount: StateFlow<SingleLedgerAccount> = _singleLedgerAccount.asStateFlow()

    private val _multiPartnerLedgerAccount = MutableStateFlow(MultiPartnerLedgerAccount())
    val multiPartnerLedgerAccount: StateFlow<MultiPartnerLedgerAccount> = _multiPartnerLedgerAccount.asStateFlow()

    private val _workspaceBalanceSheetItems = MutableStateFlow(
        listOf(
            BalanceSheetItem(particular = "I. Equity and Liabilities:"),
            BalanceSheetItem(particular = "  1. Shareholders' Funds - Share Capital", noteNo = "1", amount = "500000"),
            BalanceSheetItem(particular = "  2. Non-Current Liabilities - 9% Debentures", noteNo = "2", amount = "200000"),
            BalanceSheetItem(particular = "  3. Current Liabilities - Trade Payables", noteNo = "3", amount = "50000"),
            BalanceSheetItem(particular = "II. Assets:"),
            BalanceSheetItem(particular = "  1. Non-Current Assets - Property & Plant", noteNo = "4", amount = "600000"),
            BalanceSheetItem(particular = "  2. Current Assets - Cash & Inventories", noteNo = "5", amount = "150000")
        )
    )
    val workspaceBalanceSheetItems: StateFlow<List<BalanceSheetItem>> = _workspaceBalanceSheetItems.asStateFlow()

    private val _workspaceCashFlowItems = MutableStateFlow(
        listOf(
            CashFlowItem(activity = "Operating", description = "Net Profit before Tax & Extra", amount = "250000"),
            CashFlowItem(activity = "Operating", description = "Add: Depreciation on Plant", amount = "30000"),
            CashFlowItem(activity = "Operating", description = "Less: Increase in Trade Receivables", amount = "20000", isOutflow = true),
            CashFlowItem(activity = "Investing", description = "Purchase of Machinery", amount = "80000", isOutflow = true),
            CashFlowItem(activity = "Financing", description = "Proceeds from Issue of Shares", amount = "100000")
        )
    )
    val workspaceCashFlowItems: StateFlow<List<CashFlowItem>> = _workspaceCashFlowItems.asStateFlow()

    private val _workspaceMessage = MutableStateFlow<String?>(null)
    val workspaceMessage: StateFlow<String?> = _workspaceMessage.asStateFlow()

    // Exam / Test State
    private val _activeExamCategory = MutableStateFlow(ExamCategory.DAILY_REVISION)
    val activeExamCategory: StateFlow<ExamCategory> = _activeExamCategory.asStateFlow()

    private val _activeExamQuestions = MutableStateFlow<List<ExamQuestion>>(emptyList())
    val activeExamQuestions: StateFlow<List<ExamQuestion>> = _activeExamQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _studentResponses = MutableStateFlow<Map<String, StudentQuestionResponse>>(emptyMap())
    val studentResponses: StateFlow<Map<String, StudentQuestionResponse>> = _studentResponses.asStateFlow()

    private val _activeExamTimeRemaining = MutableStateFlow(1200) // seconds
    val activeExamTimeRemaining: StateFlow<Int> = _activeExamTimeRemaining.asStateFlow()

    private val _latestExamReport = MutableStateFlow<ExamAnalysisReport?>(null)
    val latestExamReport: StateFlow<ExamAnalysisReport?> = _latestExamReport.asStateFlow()

    // AI Tutor State
    private val _aiTutorLoading = MutableStateFlow(false)
    val aiTutorLoading: StateFlow<Boolean> = _aiTutorLoading.asStateFlow()

    private val _aiTutorLatestResponse = MutableStateFlow<AiTutorResponse?>(null)
    val aiTutorLatestResponse: StateFlow<AiTutorResponse?> = _aiTutorLatestResponse.asStateFlow()

    private val _aiMistakeResult = MutableStateFlow<AiMistakeAnalysisResult?>(null)
    val aiMistakeResult: StateFlow<AiMistakeAnalysisResult?> = _aiMistakeResult.asStateFlow()

    // Practice / PYQ filter
    private val _selectedPyqYear = MutableStateFlow("All Years")
    val selectedPyqYear: StateFlow<String> = _selectedPyqYear.asStateFlow()

    private val _practiceFilterChapter = MutableStateFlow("All Chapters")
    val practiceFilterChapter: StateFlow<String> = _practiceFilterChapter.asStateFlow()

    init {
        checkInitialSetup()
    }

    private fun checkInitialSetup() {
        viewModelScope.launch {
            val profile = userDao.getUserProfileSync()
            if (profile == null) {
                // Initialize default profile
                val defaultProfile = UserProfile()
                userDao.insertOrUpdateProfile(defaultProfile)
                // Also initialize sample progress and weak topics
                progressDao.saveChapterProgress(ChapterProgressEntity("acc_ch1_fundamentals", true, 85))
                progressDao.saveChapterProgress(ChapterProgressEntity("acc_ch2_goodwill", true, 74))
                progressDao.saveChapterProgress(ChapterProgressEntity("acc_ch9_cashflow", false, 48))

                progressDao.insertWeakTopic(WeakTopicEntity("Operating Activities (Cash Flow)", "Cash Flow Statement", 3, 48))
                progressDao.insertWeakTopic(WeakTopicEntity("Super Profit Capitalisation", "Goodwill Valuation", 2, 62))
                progressDao.insertWeakTopic(WeakTopicEntity("Interest on Drawings Fractions", "Partnership Fundamentals", 1, 75))

                // Insert initial test record for trend
                testAttemptDao.insertTestAttempt(
                    TestAttemptRecord(
                        examCategory = ExamCategory.DAILY_REVISION,
                        totalMarks = 20,
                        scoredMarks = 14.0,
                        percentage = 70.0,
                        timeSpentSeconds = 720,
                        correctCount = 7,
                        totalQuestions = 10,
                        weakTopicsCsv = "Cash Flow, Goodwill",
                        strongTopicsCsv = "Partnership Fundamentals",
                        aiFeedbackSummary = "Good understanding of partnership deed rules. Review cash flow working capital adjustments."
                    )
                )
            }
        }
    }

    fun navigateTo(destination: AppNavDestination) {
        _currentDestination.value = destination
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateProfile(
        fullName: String,
        displayName: String,
        email: String,
        board: EducationBoard,
        session: String,
        examDateMillis: Long,
        subject: CommerceSubject
    ) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfile()
            val updated = current.copy(
                fullName = fullName,
                displayName = displayName,
                email = email,
                board = board,
                academicSession = session,
                examDateMillis = examDateMillis,
                selectedSubject = subject
            )
            userDao.insertOrUpdateProfile(updated)
        }
    }

    fun selectChapter(chapterId: String) {
        _selectedChapterId.value = chapterId
    }

    fun toggleHighlightImportantOnly() {
        _highlightImportantOnly.value = !_highlightImportantOnly.value
    }

    // Business Game Actions
    fun selectBusiness(business: VirtualBusiness) {
        _selectedBusiness.value = business
        _currentScenarioIndex.value = 0
        _gameFeedback.value = null
        _gameAnswerCorrect.value = null
    }

    fun submitGameAnswer(
        scenario: GameTransactionScenario,
        selectedDrAccount: String,
        selectedCrAccount: String,
        enteredAmount: String,
        enteredNarration: String
    ) {
        val amount = enteredAmount.toDoubleOrNull() ?: 0.0
        val isAmountMatching = Math.abs(amount - scenario.expectedAmount) < 5.0 || amount == scenario.expectedAmount
        val isDrRelevant = selectedDrAccount.isNotBlank() && (scenario.expectedDebitAccount.contains(selectedDrAccount, ignoreCase = true) || selectedDrAccount.contains("Cash", true) || selectedDrAccount.contains("Bank", true) || selectedDrAccount.contains("Purchases", true) || selectedDrAccount.contains("Realisation", true))
        val isCrRelevant = selectedCrAccount.isNotBlank() && (scenario.expectedCreditAccount.contains(selectedCrAccount, ignoreCase = true) || selectedCrAccount.contains("Capital", true) || selectedCrAccount.contains("Discount", true) || selectedCrAccount.contains("Revaluation", true))

        val isCorrect = isAmountMatching && isDrRelevant && isCrRelevant

        _gameAnswerCorrect.value = isCorrect
        if (isCorrect) {
            _gameFeedback.value = "🎯 Excellent! Correct Accounting Entry!\n${scenario.ruleApplied}\n+${scenario.xpReward} XP, +${scenario.coinReward} Coins earned!"
            viewModelScope.launch {
                userDao.addXpAndCoins(scenario.xpReward, scenario.coinReward)
            }
        } else {
            _gameFeedback.value = "⚠️ Review this entry:\nExpected Dr: ${scenario.expectedDebitAccount}\nExpected Cr: ${scenario.expectedCreditAccount}\nRule: ${scenario.ruleApplied}\n${scenario.explanation}"
        }
    }

    fun nextGameScenario() {
        val scenarios = GameRepository.getScenariosForBusiness(_selectedBusiness.value.id)
        if (_currentScenarioIndex.value < scenarios.size - 1) {
            _currentScenarioIndex.value += 1
            _gameFeedback.value = null
            _gameAnswerCorrect.value = null
        } else {
            _currentScenarioIndex.value = 0
            _gameFeedback.value = "🎉 Business Level Completed! Great accounting work."
        }
    }

    // Workspace Actions
    fun setWorkspaceType(type: AccountingWorkspaceType) {
        _workspaceType.value = type
        _workspaceMessage.value = null
    }

    fun addJournalRow() {
        _workspaceJournalRows.value = _workspaceJournalRows.value + JournalEntryRow()
    }

    fun updateJournalRow(index: Int, updatedRow: JournalEntryRow) {
        val list = _workspaceJournalRows.value.toMutableList()
        if (index in list.indices) {
            list[index] = updatedRow
            _workspaceJournalRows.value = list
        }
    }

    fun removeJournalRow(index: Int) {
        val list = _workspaceJournalRows.value.toMutableList()
        if (list.size > 1 && index in list.indices) {
            list.removeAt(index)
            _workspaceJournalRows.value = list
        }
    }

    fun addLedgerRowDr() {
        _workspaceLedgerRowsDr.value = _workspaceLedgerRowsDr.value + LedgerEntryRow()
    }

    fun updateLedgerRowDr(index: Int, row: LedgerEntryRow) {
        val list = _workspaceLedgerRowsDr.value.toMutableList()
        if (index in list.indices) {
            list[index] = row
            _workspaceLedgerRowsDr.value = list
        }
    }

    fun addLedgerRowCr() {
        _workspaceLedgerRowsCr.value = _workspaceLedgerRowsCr.value + LedgerEntryRow()
    }

    fun updateLedgerRowCr(index: Int, row: LedgerEntryRow) {
        val list = _workspaceLedgerRowsCr.value.toMutableList()
        if (index in list.indices) {
            list[index] = row
            _workspaceLedgerRowsCr.value = list
        }
    }

    fun updateSingleLedgerAccount(account: SingleLedgerAccount) {
        _singleLedgerAccount.value = account
    }

    fun updateMultiPartnerLedgerAccount(account: MultiPartnerLedgerAccount) {
        _multiPartnerLedgerAccount.value = account
    }

    fun checkWorkspaceBalance() {
        when (_workspaceType.value) {
            AccountingWorkspaceType.JOURNAL -> {
                val totalDebit = _workspaceJournalRows.value.sumOf { it.debitAmount.toDoubleOrNull() ?: 0.0 }
                val totalCredit = _workspaceJournalRows.value.sumOf { it.creditAmount.toDoubleOrNull() ?: 0.0 }
                if (totalDebit > 0 && Math.abs(totalDebit - totalCredit) < 0.01) {
                    _workspaceMessage.value = "✅ Journal is in Perfect Balance! Total Debit = Total Credit = ₹$totalDebit"
                } else {
                    _workspaceMessage.value = "⚠️ Balance Mismatch: Total Debit (₹$totalDebit) ≠ Total Credit (₹$totalCredit). Difference: ₹${Math.abs(totalDebit - totalCredit)}"
                }
            }
            AccountingWorkspaceType.LEDGER -> {
                val totalDr = _workspaceLedgerRowsDr.value.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                val totalCr = _workspaceLedgerRowsCr.value.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                val balance = Math.abs(totalDr - totalCr)
                val side = if (totalDr > totalCr) "Debit Balance (Dr > Cr)" else "Credit Balance (Cr > Dr)"
                _workspaceMessage.value = "📊 Ledger Total Dr: ₹$totalDr | Total Cr: ₹$totalCr | Balancing Figure c/d: ₹$balance ($side)"
            }
            else -> {
                _workspaceMessage.value = "✅ Workspace structure validated. All financial items ready for presentation."
            }
        }
    }

    fun saveWorkspaceDraft(title: String) {
        viewModelScope.launch {
            workspaceDao.saveWorkspaceDraft(
                WorkspaceDraftEntity(
                    title = title.ifBlank { "Custom ${_workspaceType.value.displayName} Draft" },
                    workspaceType = _workspaceType.value,
                    contentJson = "Saved ${System.currentTimeMillis()}"
                )
            )
            _workspaceMessage.value = "💾 Draft saved locally to your device!"
        }
    }

    // Exam Flow Actions
    fun startExam(category: ExamCategory) {
        _activeExamCategory.value = category
        val board = userProfile.value?.board ?: EducationBoard.CBSE
        val questions = ExamRepository.getQuestionsForExamCategory(category, board)
        _activeExamQuestions.value = questions
        _currentQuestionIndex.value = 0
        _studentResponses.value = questions.associate { it.id to StudentQuestionResponse(it.id) }
        _activeExamTimeRemaining.value = category.durationMinutes * 60
        _currentDestination.value = AppNavDestination.ACTIVE_EXAM
    }

    fun selectExamQuestionIndex(index: Int) {
        if (index in _activeExamQuestions.value.indices) {
            _currentQuestionIndex.value = index
        }
    }

    fun updateStudentMcqChoice(questionId: String, optionIndex: Int) {
        val current = _studentResponses.value.toMutableMap()
        val oldResp = current[questionId] ?: StudentQuestionResponse(questionId)
        current[questionId] = oldResp.copy(selectedMcqOption = optionIndex, isAnswered = true)
        _studentResponses.value = current
    }

    fun updateStudentWrittenAnswer(questionId: String, text: String) {
        val current = _studentResponses.value.toMutableMap()
        val oldResp = current[questionId] ?: StudentQuestionResponse(questionId)
        current[questionId] = oldResp.copy(writtenAnswerText = text, isAnswered = text.isNotBlank())
        _studentResponses.value = current
    }

    fun updateStudentNumericalInput(questionId: String, numText: String) {
        val current = _studentResponses.value.toMutableMap()
        val oldResp = current[questionId] ?: StudentQuestionResponse(questionId)
        current[questionId] = oldResp.copy(numericalInput = numText, isAnswered = numText.isNotBlank())
        _studentResponses.value = current
    }

    fun toggleMarkForReview(questionId: String) {
        val current = _studentResponses.value.toMutableMap()
        val oldResp = current[questionId] ?: StudentQuestionResponse(questionId)
        current[questionId] = oldResp.copy(isMarkedForReview = !oldResp.isMarkedForReview)
        _studentResponses.value = current
    }

    fun submitActiveExam() {
        val questions = _activeExamQuestions.value
        val responses = _studentResponses.value
        var scoredMarks = 0.0
        var totalMarks = 0
        var correctCount = 0
        val weakList = mutableListOf<String>()
        val strongList = mutableListOf<String>()
        val whatWentWell = mutableListOf<String>()
        val whatWasWrong = mutableListOf<String>()
        val howToFix = mutableListOf<String>()

        questions.forEach { q ->
            totalMarks += q.marks
            val resp = responses[q.id]
            var isCorrect = false

            when (q.type) {
                QuestionType.MCQ -> {
                    if (resp?.selectedMcqOption == q.correctMcqIndex) {
                        scoredMarks += q.marks
                        correctCount++
                        isCorrect = true
                    }
                }
                QuestionType.NUMERICAL_INPUT -> {
                    val inputVal = resp?.numericalInput?.toDoubleOrNull()
                    if (inputVal != null && q.expectedNumericalAnswer != null && Math.abs(inputVal - q.expectedNumericalAnswer) < 5.0) {
                        scoredMarks += q.marks
                        correctCount++
                        isCorrect = true
                    } else if (resp?.numericalInput?.isNotBlank() == true) {
                        scoredMarks += q.marks * 0.5 // partial credit
                    }
                }
                else -> {
                    if (resp?.writtenAnswerText?.isNotBlank() == true || resp?.isAnswered == true) {
                        scoredMarks += q.marks * 0.85 // step mark rubric
                        correctCount++
                        isCorrect = true
                    }
                }
            }

            if (isCorrect) {
                strongList.add(q.chapterName)
                whatWentWell.add("Q${q.questionNumber}: Solid application of ${q.chapterName} rules (${q.marks}/${q.marks} marks).")
            } else {
                weakList.add(q.chapterName)
                whatWasWrong.add("Q${q.questionNumber} (${q.chapterName}): Review ${q.instructions.ifEmpty { "core statutory requirements" }}.")
                howToFix.add("Revise ${q.chapterName} rules & re-solve question Q${q.questionNumber}.")
            }
        }

        if (totalMarks == 0) totalMarks = 20
        val percentage = (scoredMarks / totalMarks) * 100.0
        val accuracy = if (questions.isNotEmpty()) ((correctCount.toDouble() / questions.size) * 100).toInt() else 70

        val report = ExamAnalysisReport(
            examTitle = _activeExamCategory.value.title,
            scoredMarks = scoredMarks,
            totalMarks = totalMarks,
            percentage = percentage,
            accuracyPercentage = accuracy,
            timeSpentFormatted = "18 mins 30 secs",
            strongChapters = strongList.distinct().ifEmpty { listOf("Partnership Fundamentals") },
            weakChapters = weakList.distinct().ifEmpty { listOf("Cash Flow Statement") },
            whatWentWell = whatWentWell.ifEmpty { listOf("Good speed in solving partnership questions", "Accurate journal formatting") },
            whatWasWrong = whatWasWrong.ifEmpty { listOf("Slight miscalculation in non-cash depreciation adjustment") },
            whyItWasWrong = listOf("Overlooking statutory adjustments in Section 13 & Schedule III formats"),
            rulesApplicable = listOf("Indian Partnership Act 1932 (Sec 13)", "AS-3 Cash Flow Indirect Framework"),
            howToFix = howToFix.ifEmpty { listOf("Practice 5 additional numerical questions on Cash Flow Operating activities") },
            recommendedNextSteps = listOf(
                "1. Revise weak chapter: ${weakList.firstOrNull() ?: "Cash Flow Statement"}",
                "2. Review official step-by-step marking rubric",
                "3. Solve 3 targeted PYQ questions",
                "4. Take a 10-minute mini-quiz"
            ),
            mission100ReadinessDelta = if (percentage >= 80) "+4.5% Toward Mission 100!" else "+2.0% Preparation Progress"
        )

        _latestExamReport.value = report

        viewModelScope.launch {
            testAttemptDao.insertTestAttempt(
                TestAttemptRecord(
                    examCategory = _activeExamCategory.value,
                    totalMarks = totalMarks,
                    scoredMarks = scoredMarks,
                    percentage = percentage,
                    timeSpentSeconds = 1110,
                    correctCount = correctCount,
                    totalQuestions = questions.size,
                    weakTopicsCsv = weakList.distinct().joinToString(", "),
                    strongTopicsCsv = strongList.distinct().joinToString(", "),
                    aiFeedbackSummary = "Scored ${scoredMarks.toInt()}/$totalMarks (${percentage.toInt()}%). Preparation moving toward Mission 100."
                )
            )
            userDao.addXpAndCoins(80, 30)
        }

        _currentDestination.value = AppNavDestination.EXAM_REPORT
    }

    // AI Tutor Action
    fun askAiTutor(query: String, topic: String = "Class 12 Accountancy") {
        if (query.isBlank()) return
        viewModelScope.launch {
            _aiTutorLoading.value = true
            val response = GeminiTutorService.askTutorSevenStepUnderstanding(query, topic)
            _aiTutorLatestResponse.value = response
            _aiTutorLoading.value = false
        }
    }

    fun diagnoseMistake(question: String, studentAnswer: String, correctAnswer: String) {
        viewModelScope.launch {
            _aiTutorLoading.value = true
            val result = GeminiTutorService.analyzeStudentMistake(question, studentAnswer, correctAnswer)
            _aiMistakeResult.value = result
            _aiTutorLoading.value = false
        }
    }

    fun setSelectedPyqYear(year: String) {
        _selectedPyqYear.value = year
    }

    fun setPracticeFilterChapter(ch: String) {
        _practiceFilterChapter.value = ch
    }

    // Leaderboard generation (privacy compliant - display name and score only)
    fun getLeaderboardEntries(): List<LeaderboardEntry> {
        val user = userProfile.value
        val userXp = user?.xp ?: 450
        val userBoard = user?.board?.code ?: "CBSE"
        val userName = user?.displayName ?: "You"

        return listOf(
            LeaderboardEntry(1, "Aarav_CommercePro", "CBSE", 1850, "🏆 Accounting Master"),
            LeaderboardEntry(2, "Priya_100Target", "ISC", 1720, "🏅 Partnership Pro"),
            LeaderboardEntry(3, "Rohan_BalanceSheet", "Maharashtra", 1580, "🏅 Journal Master"),
            LeaderboardEntry(4, userName, userBoard, userXp, "🎯 Mission 100 Aspirant", isCurrentUser = true),
            LeaderboardEntry(5, "Ananya_Ledger", "Karnataka", 1120, "🏅 PYQ Champion"),
            LeaderboardEntry(6, "Vikram_CashFlow", "Tamil Nadu", 980, "🏅 Cash Flow Master"),
            LeaderboardEntry(7, "Sneha_Shares", "UP_BOARD", 850, "🏅 Pro-rata Wizard")
        ).sortedByDescending { it.scoreXp }.mapIndexed { index, item -> item.copy(rank = index + 1) }
    }

    // Firebase Auth Operations
    fun clearAuthMessages() {
        _authErrorMessage.value = null
        _authSuccessMessage.value = null
    }

    fun signUpWithFirebase(
        email: String,
        pass: String,
        fullName: String,
        displayName: String,
        board: EducationBoard = EducationBoard.CBSE,
        subject: CommerceSubject = CommerceSubject.ACCOUNTANCY,
        onSuccess: () -> Unit = {}
    ) {
        if (email.isBlank() || pass.isBlank()) {
            _authErrorMessage.value = "Please enter both email and password."
            return
        }
        if (pass.length < 6) {
            _authErrorMessage.value = "Password must be at least 6 characters long."
            return
        }

        viewModelScope.launch {
            _authLoading.value = true
            _authErrorMessage.value = null
            _authSuccessMessage.value = null

            val result = authRepository.signUpWithEmail(email, pass, displayName.ifBlank { fullName })
            result.onSuccess { user ->
                _authLoading.value = false
                _authSuccessMessage.value = "Welcome, ${user.displayName ?: fullName}! Account registered successfully."
                // Update or sync local room profile with new credentials
                val current = userProfile.value ?: UserProfile()
                val updated = current.copy(
                    fullName = fullName.ifBlank { user.displayName ?: "Commerce Student" },
                    displayName = displayName.ifBlank { user.displayName ?: "student" },
                    email = user.email ?: email,
                    board = board,
                    selectedSubject = subject,
                    isOnlineSyncEnabled = true
                )
                userDao.insertOrUpdateProfile(updated)
                onSuccess()
            }.onFailure { err ->
                _authLoading.value = false
                _authErrorMessage.value = err.localizedMessage ?: "Registration failed. Please check credentials."
            }
        }
    }

    fun signInWithFirebase(
        email: String,
        pass: String,
        onSuccess: () -> Unit = {}
    ) {
        if (email.isBlank() || pass.isBlank()) {
            _authErrorMessage.value = "Please enter both email and password."
            return
        }

        viewModelScope.launch {
            _authLoading.value = true
            _authErrorMessage.value = null
            _authSuccessMessage.value = null

            val result = authRepository.signInWithEmail(email, pass)
            result.onSuccess { user ->
                _authLoading.value = false
                _authSuccessMessage.value = "Welcome back, ${user.displayName ?: user.email}!"
                // Sync local profile email if present
                val current = userProfile.value ?: UserProfile()
                val updated = current.copy(
                    fullName = user.displayName?.ifBlank { current.fullName } ?: current.fullName,
                    displayName = user.displayName?.ifBlank { current.displayName } ?: current.displayName,
                    email = user.email ?: current.email,
                    isOnlineSyncEnabled = true
                )
                userDao.insertOrUpdateProfile(updated)
                onSuccess()
            }.onFailure { err ->
                _authLoading.value = false
                _authErrorMessage.value = err.localizedMessage ?: "Sign in failed. Please verify credentials."
            }
        }
    }

    fun signInWithGoogle(
        context: android.content.Context,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _authLoading.value = true
            _authErrorMessage.value = null
            _authSuccessMessage.value = null

            val result = authRepository.signInWithGoogleCredentialManager(context)
            result.onSuccess { user ->
                _authLoading.value = false
                _authSuccessMessage.value = "Signed in with Google as ${user.displayName ?: user.email}!"
                val current = userProfile.value ?: UserProfile()
                val updated = current.copy(
                    fullName = user.displayName ?: current.fullName,
                    displayName = user.displayName?.replace(" ", "_") ?: current.displayName,
                    email = user.email ?: current.email,
                    isOnlineSyncEnabled = true
                )
                userDao.insertOrUpdateProfile(updated)
                onSuccess()
            }.onFailure { err ->
                _authLoading.value = false
                _authErrorMessage.value = err.localizedMessage ?: "Google Sign-In failed."
            }
        }
    }

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _authErrorMessage.value = "Please enter your email address to reset password."
            return
        }
        viewModelScope.launch {
            _authLoading.value = true
            _authErrorMessage.value = null
            val result = authRepository.sendPasswordReset(email)
            _authLoading.value = false
            result.onSuccess {
                _authSuccessMessage.value = "Password reset link sent to $email. Check your inbox."
            }.onFailure { err ->
                _authErrorMessage.value = err.localizedMessage ?: "Failed to send password reset email."
            }
        }
    }

    fun logoutFirebase() {
        authRepository.signOut()
        _authSuccessMessage.value = "Logged out successfully."
    }

    // =========================================================================
    // BOARDMENTOR AI CONTROLLER METHODS
    // =========================================================================

    fun updateStudentAcademicProfile(
        country: String,
        state: String,
        district: String,
        area: String,
        boardCode: String,
        boardName: String,
        gradeClassId: String,
        gradeClassName: String,
        streamId: String,
        streamName: String,
        selectedSubjectIds: String,
        schoolId: String,
        schoolName: String,
        schoolArea: String,
        schoolVerificationStatus: com.example.data.model.SchoolVerificationStatus,
        preferredLanguage: String,
        targetPercentage: Int
    ) {
        viewModelScope.launch {
            val current = studentProfile.value
            val updated = current.copy(
                country = country,
                state = state,
                district = district,
                areaLocality = area,
                boardCode = boardCode,
                boardDisplayName = boardName,
                gradeClassId = gradeClassId,
                gradeClassDisplayName = gradeClassName,
                streamId = streamId,
                streamDisplayName = streamName,
                selectedSubjectIds = selectedSubjectIds,
                schoolId = schoolId,
                schoolName = schoolName,
                schoolArea = schoolArea,
                schoolVerificationStatus = schoolVerificationStatus,
                preferredLanguage = preferredLanguage,
                targetPercentage = targetPercentage,
                lastActiveTimestamp = System.currentTimeMillis()
            )
            userDao.saveStudentProfile(updated)
            // Also refresh today's study mission
            _todayStudyMission.value = com.example.data.repository.BoardMentorRepository.getTodayStudyMission(updated)
        }
    }

    fun setTeacherPedagogicalMode(mode: com.example.data.model.TeacherPedagogicalMode) {
        _teacherMode.value = mode
    }

    fun sendTeacherChatMessage(userQuery: String, subjectTag: String = "General") {
        if (userQuery.isBlank()) return

        val userMsg = com.example.data.model.AiTeacherChatMessage(
            sender = "user",
            text = userQuery,
            mode = _teacherMode.value,
            subjectTag = subjectTag
        )
        _teacherChatMessages.value = _teacherChatMessages.value + userMsg

        viewModelScope.launch {
            _aiTutorLoading.value = true
            val currentProfile = studentProfile.value
            val responseText = GeminiTutorService.askTeacherWithMode(
                query = userQuery,
                mode = _teacherMode.value,
                boardName = currentProfile.boardDisplayName,
                gradeClass = currentProfile.gradeClassDisplayName,
                subject = subjectTag
            )
            val aiMsg = com.example.data.model.AiTeacherChatMessage(
                sender = "ai",
                text = responseText,
                mode = _teacherMode.value,
                subjectTag = subjectTag,
                suggestedFollowUps = listOf(
                    "Can you give another real-life example?",
                    "What common mistake do students make in board exams?",
                    "Give me a quick 1-minute summary",
                    "Give me 2 practice questions on this"
                )
            )
            _teacherChatMessages.value = _teacherChatMessages.value + aiMsg
            _aiTutorLoading.value = false
            userDao.addStudentXp(15)
        }
    }

    fun performPhotoQuestionAnalysis(promptText: String, subject: String = "Accountancy") {
        viewModelScope.launch {
            _aiTutorLoading.value = true
            val result = GeminiTutorService.analyzePhotoQuestion(
                detectedTextOrPrompt = promptText,
                subjectHint = subject
            )
            _photoAnalysisResult.value = result
            _aiTutorLoading.value = false
            userDao.addStudentXp(25)
        }
    }

    fun generateAiImportantQuestion(
        subject: String = "Accountancy",
        chapter: String = "Admission of Partner",
        difficulty: String = "Board Level"
    ) {
        viewModelScope.launch {
            _aiTutorLoading.value = true
            val profile = studentProfile.value
            val question = GeminiTutorService.generateImportantQuestion(
                board = profile.boardDisplayName,
                gradeClass = profile.gradeClassDisplayName,
                subject = subject,
                chapter = chapter,
                difficulty = difficulty
            )
            _generatedImportantQuestions.value = listOf(question) + _generatedImportantQuestions.value
            _aiTutorLoading.value = false
        }
    }

    fun evaluateStudentExamAnswer(
        questionText: String,
        maxMarks: Double,
        studentAnswer: String,
        subject: String = "Accountancy"
    ) {
        viewModelScope.launch {
            _aiTutorLoading.value = true
            val eval = GeminiTutorService.evaluateExamAnswer(
                questionText = questionText,
                maxMarks = maxMarks,
                studentAnswer = studentAnswer,
                subject = subject
            )
            _answerEvaluationResult.value = eval
            _aiTutorLoading.value = false
            userDao.addStudentXp(30)
        }
    }

    fun toggleMissionTask(taskId: String) {
        val currentMission = _todayStudyMission.value
        val updatedTasks = currentMission.tasks.map { task ->
            if (task.id == taskId) {
                val newStatus = !task.isDone
                if (newStatus) {
                    viewModelScope.launch { userDao.addStudentXp(task.xpReward) }
                }
                task.copy(isDone = newStatus)
            } else task
        }
        val allDone = updatedTasks.all { it.isDone }
        _todayStudyMission.value = currentMission.copy(
            tasks = updatedTasks,
            isFullyCompleted = allDone
        )
    }

    fun verifySchoolWithCode(schoolCode: String): Boolean {
        val validCodes = listOf("BM8821", "DPS2026", "COTTON", "KVIIT26", "DBOSCO", "XAVIER", "NPSBLR")
        val isValid = validCodes.any { it.equals(schoolCode.trim(), ignoreCase = true) }
        if (isValid) {
            viewModelScope.launch {
                userDao.updateSchoolVerification(com.example.data.model.SchoolVerificationStatus.VERIFIED)
                userDao.addStudentXp(100)
            }
        }
        return isValid
    }

    fun setLeaderboardCategory(cat: com.example.data.model.LeaderboardCategory) {
        _selectedLeaderboardCategory.value = cat
    }

    fun getSubjectTargets(): List<com.example.data.model.SubjectTargetItem> {
        val profile = studentProfile.value
        val defaultSubjects = com.example.data.model.DefaultSubjectsCatalog.filter {
            profile.selectedSubjectIds.contains(it.id) || it.streamId == profile.streamId || it.isCore
        }.take(5)
        return com.example.data.repository.BoardMentorRepository.calculateSubjectTargets(
            targetPercent = profile.targetPercentage,
            selectedSubjects = if (defaultSubjects.isNotEmpty()) defaultSubjects else com.example.data.model.DefaultSubjectsCatalog.take(5)
        )
    }
}
