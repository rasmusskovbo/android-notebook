package dk.rskovbo.md_android_notebook

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

class NoteActivity : AppCompatActivity() {
    lateinit var editTitle: EditText
    lateinit var editBody: EditText
    lateinit var builder: AlertDialog.Builder
    var title: String? = ""
    var body: String? = ""

    companion object {
        const val TITLE = "title"
        const val BODY = "body"

        fun newIntent(context: Context, noteItem: NoteItem): Intent {
            val noteIntent = Intent(context, NoteActivity::class.java)

            noteIntent.putExtra(TITLE, noteItem.title)
            noteIntent.putExtra(BODY, noteItem.body)

            return noteIntent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        // Cache data passed to through intent
        title = intent.extras?.getString(TITLE)
        body = intent.extras?.getString(BODY)

        // Caches text views
        editBody = findViewById(R.id.editBody)
        editTitle = findViewById(R.id.editTitle)

        // Set data of views
        setTitle("Notebook")
        editTitle.setText(title)
        editBody.setText(body)

        // Build alert for deletion
        builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm deletion")
        builder.setMessage("Please confirm deletion.")
        builder.setPositiveButton("OK") { dialog, which ->
            deleteNote()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(applicationContext, "Deletion cancelled", Toast.LENGTH_SHORT).show()
        }

        // Assign toolbar
        setSupportActionBar(findViewById(R.id.top_toolbar))

    }

    // Connect menu layout to toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.show_note_toolbar, menu)
        return true
    }

    // Assign actions to toolbar items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.save_note -> {
            MainActivity.saveNote(editTitle.text.toString(), editBody.text.toString())
            Toast.makeText(applicationContext, "Note saved", Toast.LENGTH_SHORT).show()
            true
        }
        R.id.delete_note -> {
            builder.show()
            false
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun deleteNote() {
        val position = MainActivity.currentIndex
        MainActivity.deleteNote(position)
        finish()
    }

}