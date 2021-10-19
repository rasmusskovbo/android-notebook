package dk.rskovbo.md_android_notebook

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Notebook")

        listView = findViewById(R.id.listView)
        val adapter = ListAdapter(this, getMovieItems())
        listView.adapter = adapter
        listView.setOnItemClickListener{ parent, view, position, id ->
            val element = adapter.getItem(position)
            val intent = NoteActivity.newIntent(this, element as MovieItem)
            startActivity(intent)
        }

    }

    fun getMovieItems(): ArrayList<MovieItem> {
        val listItems = arrayListOf<MovieItem>()
        listItems.add(MovieItem("Star Wars V", "The Empire Strikes Back (also known as Star Wars: Episode V – The Empire Strikes Back) is a 1980 American epic space opera film directed by Irvin Kershner"))
        listItems.add(MovieItem("Dune", "A mythic and emotionally charged hero's journey, \"Dune\" tells the story of Paul Atreides, a brilliant and gifted young man born into a great destiny beyond his understanding"))
        listItems.add(MovieItem("Interstellar", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival."))
        listItems.add(MovieItem("Star Wars V", "The Empire Strikes Back (also known as Star Wars: Episode V – The Empire Strikes Back) is a 1980 American epic space opera film directed by Irvin Kershner"))
        listItems.add(MovieItem("Dune", "A mythic and emotionally charged hero's journey, \"Dune\" tells the story of Paul Atreides, a brilliant and gifted young man born into a great destiny beyond his understanding"))
        listItems.add(MovieItem("Interstellar", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival."))
        listItems.add(MovieItem("Star Wars V", "The Empire Strikes Back (also known as Star Wars: Episode V – The Empire Strikes Back) is a 1980 American epic space opera film directed by Irvin Kershner"))
        listItems.add(MovieItem("Dune", "A mythic and emotionally charged hero's journey, \"Dune\" tells the story of Paul Atreides, a brilliant and gifted young man born into a great destiny beyond his understanding"))
        listItems.add(MovieItem("Interstellar", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival."))
        listItems.add(MovieItem("Star Wars V", "The Empire Strikes Back (also known as Star Wars: Episode V – The Empire Strikes Back) is a 1980 American epic space opera film directed by Irvin Kershner"))
        listItems.add(MovieItem("Dune", "A mythic and emotionally charged hero's journey, \"Dune\" tells the story of Paul Atreides, a brilliant and gifted young man born into a great destiny beyond his understanding"))
        listItems.add(MovieItem("Interstellar", "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival."))
        return listItems
    }

}