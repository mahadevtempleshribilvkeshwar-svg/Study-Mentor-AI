package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    onComplete: (
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
        preferredLanguage: String,
        targetPercentage: Int
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var stepIndex by remember { mutableStateOf(1) }
    val totalSteps = 4

    // Form fields
    var selectedCountry by remember { mutableStateOf("India") }
    var selectedState by remember { mutableStateOf("Delhi NCR") }
    var selectedDistrict by remember { mutableStateOf("New Delhi") }
    var enteredArea by remember { mutableStateOf("Connaught Place") }

    var selectedBoard by remember { mutableStateOf(DefaultBoardsCatalog.first()) }
    var selectedGrade by remember { mutableStateOf(DefaultGradesCatalog.first { it.id == "class_12" }) }
    var selectedStream by remember { mutableStateOf(DefaultStreamsCatalog.first { it.id == "commerce" }) }

    var selectedSubjects by remember {
        mutableStateOf(
            DefaultSubjectsCatalog.filter { it.isCore || it.streamId == "commerce" }.map { it.id }.toSet()
        )
    }

    var schoolSearchQuery by remember { mutableStateOf("") }
    var selectedSchool by remember { mutableStateOf(DefaultSchoolsCatalog.first()) }
    var isAddingNewSchool by remember { mutableStateOf(false) }
    var newSchoolName by remember { mutableStateOf("") }
    var newSchoolArea by remember { mutableStateOf("") }

    var selectedLanguage by remember { mutableStateOf("English") }
    var targetPercent by remember { mutableFloatStateOf(92f) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SlateCanvas
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(NavyDark, IndigoDark, IndigoPrimary)))
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🎓", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "BOARDMENTOR AI",
                                    color = GoldAccent,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Academic Profile & Board Setup",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Step $stepIndex of $totalSteps",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    LinearProgressIndicator(
                        progress = { stepIndex.toFloat() / totalSteps },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = GoldAccent,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
            }

            // Step Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 20.dp)
            ) {
                when (stepIndex) {
                    1 -> {
                        item {
                            StepTitle(
                                title = "1. Geographic Location",
                                subtitle = "We tailor syllabus, state board patterns, and district study circles."
                            )
                        }

                        item {
                            Text("Select Country:", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val scrollState = rememberScrollState()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(scrollState),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DefaultCountriesCatalog.forEach { country ->
                                    val isSelected = country.name == selectedCountry
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            selectedCountry = country.name
                                            selectedState = country.defaultStates.firstOrNull() ?: "State"
                                        },
                                        label = { Text("${country.flagEmoji} ${country.name}") },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = IndigoPrimary,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        item {
                            val countryObj = DefaultCountriesCatalog.find { it.name == selectedCountry } ?: DefaultCountriesCatalog.first()
                            Text("State / Province:", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val scrollState = rememberScrollState()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(scrollState),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                countryObj.defaultStates.forEach { st ->
                                    val isSelected = st == selectedState
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedState = st },
                                        label = { Text(st) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = IndigoPrimary,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = selectedDistrict,
                                onValueChange = { selectedDistrict = it },
                                label = { Text("District") },
                                placeholder = { Text("e.g. Kamrup Metro / South Delhi") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = enteredArea,
                                onValueChange = { enteredArea = it },
                                label = { Text("Area / Locality") },
                                placeholder = { Text("e.g. Panbazar / Rohini") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    2 -> {
                        item {
                            StepTitle(
                                title = "2. Board, Grade & Stream",
                                subtitle = "Select your examination authority and current grade level."
                            )
                        }

                        item {
                            Text("Education Board:", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            DefaultBoardsCatalog.forEach { b ->
                                val isSelected = b.code == selectedBoard.code
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { selectedBoard = b },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) IndigoContainer else Color.White
                                    ),
                                    border = BorderStroke(1.dp, if (isSelected) IndigoPrimary else Slate200)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(b.displayName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            Text(b.description, color = TextSecondary, fontSize = 11.sp)
                                        }
                                        if (isSelected) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = IndigoPrimary)
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Text("Class / Grade:", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val scrollState = rememberScrollState()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(scrollState),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DefaultGradesCatalog.forEach { g ->
                                    val isSelected = g.id == selectedGrade.id
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedGrade = g },
                                        label = { Text(g.displayName) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = IndigoPrimary,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        if (selectedGrade.isStreamApplicable) {
                            item {
                                Text("Stream:", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    DefaultStreamsCatalog.forEach { s ->
                                        val isSelected = s.id == selectedStream.id
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = {
                                                selectedStream = s
                                                selectedSubjects = DefaultSubjectsCatalog
                                                    .filter { it.isCore || it.streamId == s.id }
                                                    .map { it.id }.toSet()
                                            },
                                            label = { Text("${s.emoji} ${s.displayName}") },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = IndigoPrimary,
                                                selectedLabelColor = Color.White
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    3 -> {
                        item {
                            StepTitle(
                                title = "3. Select Your Subjects",
                                subtitle = "Choose core and elective subjects for your study plan."
                            )
                        }

                        val relevantSubjects = DefaultSubjectsCatalog.filter {
                            it.isCore || it.streamId == selectedStream.id || it.streamId.isBlank()
                        }

                        items(relevantSubjects) { subj ->
                            val isChecked = selectedSubjects.contains(subj.id)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        selectedSubjects = if (isChecked) {
                                            if (selectedSubjects.size > 1) selectedSubjects - subj.id else selectedSubjects
                                        } else {
                                            selectedSubjects + subj.id
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isChecked) Slate50 else Color.White
                                ),
                                border = BorderStroke(1.dp, if (isChecked) IndigoPrimary else Slate200)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = {
                                            selectedSubjects = if (isChecked) {
                                                if (selectedSubjects.size > 1) selectedSubjects - subj.id else selectedSubjects
                                            } else {
                                                selectedSubjects + subj.id
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = IndigoPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = subj.displayName,
                                            color = TextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (subj.isCore) "Core Mandatory Subject" else "Elective Subject",
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    4 -> {
                        item {
                            StepTitle(
                                title = "4. School & Target Marks",
                                subtitle = "Connect to your school hub and set your board percentage goal."
                            )
                        }

                        item {
                            Text("Search Your School:", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = schoolSearchQuery,
                                onValueChange = { schoolSearchQuery = it },
                                placeholder = { Text("e.g. Delhi Public School / Cotton / KV...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        val filteredSchools = DefaultSchoolsCatalog.filter {
                            it.name.contains(schoolSearchQuery, ignoreCase = true) ||
                                    it.district.contains(schoolSearchQuery, ignoreCase = true)
                        }

                        items(filteredSchools.take(3)) { sch ->
                            val isSelected = sch.id == selectedSchool.id && !isAddingNewSchool
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        selectedSchool = sch
                                        isAddingNewSchool = false
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) IndigoContainer else Color.White
                                ),
                                border = BorderStroke(1.dp, if (isSelected) IndigoPrimary else Slate200)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(sch.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text("${sch.areaLocality}, ${sch.district} • ${sch.boardAffiliation}", color = TextSecondary, fontSize = 11.sp)
                                    }
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = IndigoPrimary)
                                    }
                                }
                            }
                        }

                        item {
                            OutlinedButton(
                                onClick = { isAddingNewSchool = !isAddingNewSchool },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isAddingNewSchool) "Choose from List" else "Can't find school? Add New School", fontSize = 12.sp)
                            }
                        }

                        if (isAddingNewSchool) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Slate100, RoundedCornerShape(12.dp))
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = newSchoolName,
                                        onValueChange = { newSchoolName = it },
                                        label = { Text("School Name") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    OutlinedTextField(
                                        value = newSchoolArea,
                                        onValueChange = { newSchoolArea = it },
                                        label = { Text("School Locality / Area") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Target Board Marks / Percentage:", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${targetPercent.toInt()}%", fontSize = 24.sp, fontWeight = FontWeight.Black, color = IndigoPrimary)
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (targetPercent >= 90) EmeraldContainer else GoldContainer
                                ) {
                                    Text(
                                        text = if (targetPercent >= 95) "🌟 Topper Target" else if (targetPercent >= 90) "🎯 Distinction Target" else "📈 Solid Pass Target",
                                        color = if (targetPercent >= 90) EmeraldPrimary else GoldPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Slider(
                                value = targetPercent,
                                onValueChange = { targetPercent = it },
                                valueRange = 60f..99f,
                                steps = 38,
                                colors = SliderDefaults.colors(thumbColor = IndigoPrimary, activeTrackColor = IndigoPrimary)
                            )
                        }

                        item {
                            Text("Preferred Language for Explanations:", color = Slate700, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            val langs = listOf("English", "Hindi", "Hinglish", "Bengali", "Tamil", "Marathi")
                            val scrollState = rememberScrollState()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(scrollState),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                langs.forEach { lang ->
                                    val isSelected = lang == selectedLanguage
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedLanguage = lang },
                                        label = { Text(lang) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = IndigoPrimary,
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Navigation Buttons
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (stepIndex > 1) {
                        OutlinedButton(
                            onClick = { stepIndex -= 1 },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Back", fontSize = 13.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Button(
                        onClick = {
                            if (stepIndex < totalSteps) {
                                stepIndex += 1
                            } else {
                                val finalSchoolName = if (isAddingNewSchool && newSchoolName.isNotBlank()) newSchoolName else selectedSchool.name
                                val finalSchoolArea = if (isAddingNewSchool && newSchoolArea.isNotBlank()) newSchoolArea else selectedSchool.areaLocality
                                val finalSchoolId = if (isAddingNewSchool) "custom_sch_${System.currentTimeMillis()}" else selectedSchool.id

                                onComplete(
                                    selectedCountry,
                                    selectedState,
                                    selectedDistrict,
                                    enteredArea,
                                    selectedBoard.code,
                                    selectedBoard.displayName,
                                    selectedGrade.id,
                                    selectedGrade.displayName,
                                    selectedStream.id,
                                    selectedStream.displayName,
                                    selectedSubjects.joinToString(","),
                                    finalSchoolId,
                                    finalSchoolName,
                                    finalSchoolArea,
                                    selectedLanguage,
                                    targetPercent.toInt()
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("onboarding_next_button")
                    ) {
                        Text(
                            text = if (stepIndex == totalSteps) "Launch BoardMentor AI 🚀" else "Next Step",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StepTitle(title: String, subtitle: String) {
    Column {
        Text(title, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(subtitle, color = TextSecondary, fontSize = 12.sp)
    }
}
