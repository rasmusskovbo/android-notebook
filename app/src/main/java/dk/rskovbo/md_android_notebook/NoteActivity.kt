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
import dk.rskovbo.md_android_notebook.MainActivity.Companion.db
import dk.rskovbo.md_android_notebook.MainActivity.Companion.noteItems

class NoteActivity : AppCompatActivity() {
    lateinit var editTitle: EditText
    lateinit var editBody: EditText
    lateinit var builder: AlertDialog.Builder
    var title: String? = ""
    var body: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        title = intent.extras?.getString(TITLE)
        body = intent.extras?.getString(BODY)

        editBody = findViewById(R.id.editBody)
        editTitle = findViewById(R.id.editTitle)

        setTitle("Notebook")
        editTitle.setText(title)
        editBody.setText(body)

        builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm deletion")
        builder.setMessage("Please confirm deletion.")
        builder.setPositiveButton("OK") { dialog, which ->
            deleteItemInNote(this)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(applicationContext, "Deletion cancelled", Toast.LENGTH_SHORT).show()
        }

        setSupportActionBar(findViewById(R.id.top_toolbar))

    }

    // Connect menu layout to toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.show_note_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.save_note -> {
            saveItem(editTitle.text.toString(), editBody.text.toString())
            true
        }
        R.id.delete_note -> {
            builder.show()
            false
        }
        else -> super.onOptionsItemSelected(item)
    }

    // TODO Move to main activity and refactor
    fun saveItem(editedTitle: String, editedBody: String) {
        val currentIndex = MainActivity.currentIndex
        val currentNote = noteItems.get(currentIndex)

        val item = MovieItem(editedTitle, editedBody, currentNote.noteId)
        noteItems.set(currentIndex, item)

        db.collection("notes").document(item.noteId).set(item)

        MainActivity.adapter.notifyDataSetChanged()
    }

    fun deleteItemInNote(context: Context) {
        val position = MainActivity.currentIndex
        ListAdapter.deleteItem(position)
        finish()
    }

    companion object {
        const val TITLE = "title"
        const val BODY = "body"

        fun newIntent(context: Context, movieItem: MovieItem): Intent {
            val noteIntent = Intent(context, NoteActivity::class.java)

            noteIntent.putExtra(TITLE, movieItem.title)
            noteIntent.putExtra(BODY, movieItem.body)

            return noteIntent
        }
    }
}