package dk.rskovbo.md_android_notebook

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView

    // TODO How to refactor to maintain functionality but avoid memory leaks as suggested?
    companion object {
        var currentIndex = -1
        lateinit var noteItems: ArrayList<NoteItem>
        lateinit var adapter: ListAdapter
        val db = Firebase.firestore

        fun saveNote(editedTitle: String, editedBody: String) {
            val currentNote = noteItems[currentIndex]

            val item = NoteItem(editedTitle, editedBody, currentNote.noteId)
            noteItems[currentIndex] = item

            db.collection("notes").document(item.noteId).set(item)

            adapter.notifyDataSetChanged()
        }

        fun deleteNote(position: Int) {
            // Local
            val itemToRemove = noteItems[position]
            noteItems.remove(itemToRemove)

            // Fstore
            db.collection("notes").document(itemToRemove.noteId).delete()

            // Update listview
            adapter.notifyDataSetChanged()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.top_toolbar))

        setTitle("Notebook")
        noteItems = getMovieItems()
        adapter = ListAdapter(this, noteItems)

        listView = findViewById(R.id.listView)
        listView.adapter = adapter
        listView.setOnItemClickListener{ _, _, position, _ ->
            currentIndex = position
            val element = adapter.getItem(position)
            val intent = NoteActivity.newIntent(this, element as NoteItem)
            startActivity(intent)
        }

        //addMovieItems(1)
        //adapter.notifyDataSetChanged()
    }

    // Connect menu layout to toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_note_toolbar, menu)
        return true
    }

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

    // Add note to list & firestore
    fun addNote(title: String, body: String) {
        val newNote = NoteItem(title, body)

        val newDocRef = db.collection("notes").document()
        val generatedID = newDocRef.id

        newNote.noteId = generatedID
        db.collection("notes").document(newNote.noteId).set(newNote)

        noteItems.add(newNote)
        adapter.notifyDataSetChanged()
    }

    // Get data from firestore
    fun getMovieItems(): ArrayList<NoteItem> {
        var listItems = arrayListOf<NoteItem>()
        val docRef = db.collection("notes")
        docRef.get().addOnSuccessListener { document ->
            document?.forEach {
                val movieItem = it.toObject<NoteItem>()
                listItems.add(movieItem)
            }
        }
        // Async workaround
        Thread.sleep(1_000)
        return listItems
    }

    // Create and upload mock data
    fun addMovieItems(loops: Int) {
        for(i in 1..loops) {
            val listItems = arrayListOf<NoteItem>()
            listItems.add(
                NoteItem(
                    "Star Wars V",
                    "The Empire Strikes Back (also known as Star Wars: Episode V – The Empire Strikes Back) is a 1980 American epic space opera film directed by Irvin Kershner"
                )
            )
            listItems.add(
                NoteItem(
                    "Dune",
                    "A mythic and emotionally charged hero's journey, \"Dune\" tells the story of Paul Atreides, a brilliant and gifted young man born into a great destiny beyond his understanding"
                )
            )
            listItems.add(
                NoteItem(
                    "Interstellar",
                    "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival."
                )
            )
            listItems.add(
                NoteItem(
                    "Edge of Tomorrow",
                    "Edge of Tomorrow takes place in a future where most of Europe is invaded by an alien race. Major William Cage (Cruise), a public relations officer with limited combat experience, is forced by his superiors to join a landing operation against the aliens, only to find himself experiencing a time loop as he tries to find a way to defeat the invaders. "
                )
            )
            listItems.add(
                NoteItem(
                    "Sunshine",
                    "A team of international astronauts are sent on a dangerous mission to reignite the dying Sun with a nuclear fission bomb in 2057."
                )
            )
            listItems.add(
                NoteItem(
                    "Armageddon",
                    "After discovering that an asteroid the size of Texas is going to impact Earth in less than a month, NASA recruits a misfit team of deep-core drillers to save the planet."
                )
            )
            listItems.add(
                NoteItem(
                    "Men In Black",
                    "A police officer joins a secret organization that polices and monitors extraterrestrial interactions on Earth."
                )
            )
            listItems.add(
                NoteItem(
                    "Blade Runner",
                    "A blade runner must pursue and terminate four replicants who stole a ship in space, and have returned to Earth to find their creator."
                )
            )
            listItems.forEach {
                addNote(it.title, it.body)
            }
        }

    }

}