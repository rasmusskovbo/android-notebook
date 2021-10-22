package dk.rskovbo.md_android_notebook.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import dk.rskovbo.md_android_notebook.adapter.ListAdapter
import dk.rskovbo.md_android_notebook.model.NoteItem
import dk.rskovbo.md_android_notebook.service.NoteService
import dk.rskovbo.md_android_notebook.R

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val noteService = NoteService()
    lateinit var adapter: ListAdapter

    companion object {
        var currentIndex = -1
        lateinit var noteItems: ArrayList<NoteItem>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.top_toolbar))
        setupView()
        adapter.notifyDataSetChanged()
    }

    private fun setupView() {
        // Set title, get all notes, connect adapter
        setTitle("Notebook")
        noteItems = noteService.getAllNotes()
        adapter = ListAdapter(this, noteItems)

        // Cache views
        listView = findViewById(R.id.listView)
        listView.adapter = adapter

        // Apply click-listener to list
        listView.setOnItemClickListener { _, _, position, _ ->
            currentIndex = position
            val element = adapter.getItem(position)
            val intent = NoteActivity.newIntent(this, element as NoteItem)
            startActivity(intent)
        }
    }

    // Connect menu layout to toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_note_toolbar, menu)
        return true
    }

    // Define events on row toolbar item click
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.add_note -> {
            addNote("", "...")
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    // Update listview on activity resume
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    // Add note to list, firebase & update
    fun addNote(title: String, body: String) {
        val newNote = NoteItem(title, body)

        noteService.addNote(newNote)
        noteItems.add(newNote)

        adapter.notifyDataSetChanged()
    }

}