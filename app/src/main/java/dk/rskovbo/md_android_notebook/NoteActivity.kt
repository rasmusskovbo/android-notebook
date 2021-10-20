package dk.rskovbo.md_android_notebook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        val title = intent.extras?.getString(TITLE)
        val body = intent.extras?.getString(BODY)
        val editText: EditText = findViewById(R.id.editText)
        setTitle(title)
        editText.setText(body)

        findViewById<Button>(R.id.saveButton).setOnClickListener{
            saveBody(editText.text.toString())
        }

        findViewById<Button>(R.id.deleteButton).setOnClickListener{
            deleteItemInNote(this)
        }
    }

    // TODO Move to main activity and refactor
    fun saveBody(editedBody: String) {
        MainActivity.noteItems.get(MainActivity.currentIndex).body = editedBody
    }

    fun deleteItemInNote(context: Context) {
        val position = MainActivity.currentIndex
        ListAdapter.deleteItem(position)
        finish()
        //startActivity(Intent(context, MainActivity::class.java))
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