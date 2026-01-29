package com.classmateplus.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import coil.compose.rememberAsyncImagePainter
import com.classmateplus.app.ui.theme.ClassMatePlusTheme
import org.json.JSONArray
import org.json.JSONObject

/* ===================== DATA MODEL ===================== */

data class Note(
    val title: String,
    val content: String,
    val imageUri: String? = null
)

/* ===================== MAIN ACTIVITY ===================== */

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()

        if (android.os.Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }

        setContent {
            ClassMatePlusTheme {

                var isLoggedIn by remember { mutableStateOf(false) }
                var showNotes by remember { mutableStateOf(false) }

                when {
                    !isLoggedIn -> LoginScreen { isLoggedIn = true }
                    showNotes -> NotesScreen { showNotes = false }
                    else -> DashboardScreen { showNotes = true }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                "attendance_channel",
                "Attendance Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}

/* ===================== LOGIN ===================== */

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var roll by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("ClassMatePlus", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(roll, { roll = it }, label = { Text("Roll Number") })
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(code, { code = it }, label = { Text("Class Code") })

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { if (roll.isNotBlank() && code.isNotBlank()) onLoginSuccess() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Login") }
    }
}

/* ===================== DASHBOARD ===================== */

@Composable
fun DashboardScreen(onNotesClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button(onNotesClick, Modifier.fillMaxWidth()) { Text("Notes") }
    }
}

/* ===================== NOTES ===================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    val subjects = remember { getSubjects(context) }
    var selectedSubject by remember { mutableStateOf(subjects.first()) }
    var expanded by remember { mutableStateOf(false) }

    val notes = remember { mutableStateListOf<Note>() }

    LaunchedEffect(selectedSubject) {
        notes.clear()
        notes.addAll(loadNotes(context, selectedSubject))
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            imageUri = it?.toString()
        }

    Column(Modifier.fillMaxSize().padding(24.dp)) {

        Text("Notes", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
            OutlinedTextField(
                selectedSubject, {},
                readOnly = true,
                label = { Text("Subject") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded, { expanded = false }) {
                subjects.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            selectedSubject = it
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(onClick = { showAddDialog = true }, Modifier.fillMaxWidth()) {
            Text("Add Note")
        }

        Spacer(Modifier.height(16.dp))

        notes.forEachIndexed { index, note ->
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Column(Modifier.padding(16.dp)) {

                    Text(note.title, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(note.content)

                    note.imageUri?.let {
                        Spacer(Modifier.height(8.dp))
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(180.dp)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    TextButton(onClick = {
                        notes.removeAt(index)
                        saveNotes(context, selectedSubject, notes)
                    }) { Text("Delete") }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Back") }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Note") },
            text = {
                Column {
                    OutlinedTextField(noteTitle, { noteTitle = it }, label = { Text("Title") })
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(noteContent, { noteContent = it }, label = { Text("Content") })
                }
            },
            confirmButton = {
                TextButton(onClick = { imagePicker.launch("image/*") }) {
                    Text("Attach Image")
                }
                TextButton(onClick = {
                    notes.add(Note(noteTitle, noteContent, imageUri))
                    saveNotes(context, selectedSubject, notes)
                    noteTitle = ""; noteContent = ""; imageUri = null
                    showAddDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

/* ===================== STORAGE ===================== */

fun saveNotes(context: Context, subject: String, notes: List<Note>) {
    val prefs = context.getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
    val array = JSONArray()
    notes.forEach {
        val obj = JSONObject()
        obj.put("title", it.title)
        obj.put("content", it.content)
        obj.put("imageUri", it.imageUri)
        array.put(obj)
    }
    prefs.edit().putString(subject, array.toString()).apply()
}

fun loadNotes(context: Context, subject: String): MutableList<Note> {
    val prefs = context.getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
    val array = JSONArray(prefs.getString(subject, "[]") ?: "[]")
    val list = mutableListOf<Note>()
    for (i in 0 until array.length()) {
        val o = array.getJSONObject(i)
        list.add(Note(o.getString("title"), o.getString("content"), o.optString("imageUri", null)))
    }
    return list
}

fun getSubjects(context: Context): MutableList<String> =
    context.getSharedPreferences("attendance_prefs", 0)
        .getStringSet("subjects", null)
        ?.toMutableList()
        ?: mutableListOf("Maths", "Physics", "Chemistry", "CS")
