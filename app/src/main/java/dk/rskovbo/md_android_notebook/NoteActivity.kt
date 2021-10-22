package dk.rskovbo.md_android_notebook

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException


class NoteActivity : AppCompatActivity() {
    lateinit var editTitle: EditText
    lateinit var editBody: EditText
    lateinit var builder: AlertDialog.Builder
    lateinit var noteImage: ImageView
    lateinit var filePath: Uri
    private val PICK_IMAGE_REQUEST = 234
    private var title: String? = ""
    private var body: String? = ""
    private val noteService: NoteService = NoteService()

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

        setupViewData()
        buildAlert()
        setSupportActionBar(findViewById(R.id.top_toolbar))

    }

    private fun setupViewData() {
        // Cache data passed to through intent
        title = intent.extras?.getString(TITLE)
        body = intent.extras?.getString(BODY)

        // Caches views
        editBody = findViewById(R.id.editBody)
        editTitle = findViewById(R.id.editTitle)
        noteImage = findViewById(R.id.noteImage)

        // Set data of views
        setTitle("Notebook")
        editTitle.setText(title)
        editBody.setText(body)
        noteService.downloadImage(MainActivity.noteItems[MainActivity.currentIndex].noteId, noteImage)
    }

    private fun buildAlert() {
        builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm deletion")
        builder.setMessage("Please confirm deletion.")
        builder.setPositiveButton("OK") { dialog, which ->
            deleteNote()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(applicationContext, "Deletion cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    //handling the image chooser activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                noteImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // Connect menu layout to toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.show_note_toolbar, menu)
        return true
    }

    // Assign actions to toolbar items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.choose_image -> {
            showFileChooser()
            true
        }
        R.id.save_note -> {
            noteService.saveNote(editTitle.text.toString(), editBody.text.toString())
            noteService.saveImage(noteImage)
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
        noteService.deleteNote(position)
        finish()
    }

}