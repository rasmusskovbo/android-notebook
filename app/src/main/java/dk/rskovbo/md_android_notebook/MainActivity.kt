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

    // Static objects. Look into removing dp and adapter references as suggested
    companion object {
        var currentIndex = -1
        lateinit var noteItems: ArrayList<MovieItem>
        lateinit var adapter: ListAdapter
        val db = Firebase.firestore
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setMovieItems(1)

        setSupportActionBar(findViewById(R.id.top_toolbar))
        val toolbar = supportActionBar

        setTitle("Notebook")
        noteItems = getMovieItems()
        adapter = ListAdapter(this, noteItems)

        listView = findViewById(R.id.listView)
        listView.adapter = adapter
        listView.setOnItemClickListener{ _, _, position, _ ->
            currentIndex = position
            val element = adapter.getItem(position)
            val intent = NoteActivity.newIntent(this, element as MovieItem)
            startActivity(intent)
        }
    }

    // Connect menu layout to toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.add_note -> {
            addNote()
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
    fun addNote() {
        val newNote = MovieItem("New Note", "...")
        noteItems.add(newNote)
        db.collection("notes").add(newNote)
        adapter.notifyDataSetChanged()
    }

    // Get data from firestore
    fun getMovieItems(): ArrayList<MovieItem> {
        var listItems = arrayListOf<MovieItem>()
        val docRef = db.collection("notes")
        docRef.get().addOnSuccessListener { document ->
            document?.forEach {
                val movieItem = it.toObject<MovieItem>()
                println(movieItem.title + ", " + movieItem.body)
                listItems.add(movieItem)
            }
        }
        // Async workaround
        Thread.sleep(1_000)
        return listItems
    }

    // Create and upload mock data
    fun setMovieItems(loops: Int) {
        for(i in 1..loops) {
            val listItems = arrayListOf<MovieItem>()
            listItems.add(
                MovieItem(
                    "Star Wars V",
                    "The Empire Strikes Back (also known as Star Wars: Episode V – The Empire Strikes Back) is a 1980 American epic space opera film directed by Irvin Kershner"
                )
            )
            listItems.add(
                MovieItem(
                    "Dune",
                    "A mythic and emotionally charged hero's journey, \"Dune\" tells the story of Paul Atreides, a brilliant and gifted young man born into a great destiny beyond his understanding"
                )
            )
            listItems.add(
                MovieItem(
                    "Interstellar",
                    "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival."
                )
            )
            listItems.forEach {
                db.collection("notes").add(it)
            }
        }

    }

}