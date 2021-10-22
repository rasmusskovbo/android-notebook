package dk.rskovbo.md_android_notebook.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.google.firebase.firestore.ktx.toObject
import dk.rskovbo.md_android_notebook.model.NoteItem
import dk.rskovbo.md_android_notebook.activity.MainActivity
import dk.rskovbo.md_android_notebook.repository.Repo
import java.io.ByteArrayOutputStream

class NoteService {
    private val repo: Repo = Repo()
    private val storageRef = repo.storage.reference.child("images")

    // Retrieve all notes from Firebase and bind to list
    fun getAllNotes(): ArrayList<NoteItem> {
        var listItems = arrayListOf<NoteItem>()
        val docRef = repo.db.collection("notes")
        docRef.get().addOnSuccessListener { document ->
            document?.forEach {
                val noteItem = it.toObject<NoteItem>()
                listItems.add(noteItem)
            }
        }
        Thread.sleep(1_000)
        return listItems
    }

    fun addNote(newNote: NoteItem) {
        val newDocRef = repo.db.collection("notes").document()
        val generatedID = newDocRef.id

        newNote.noteId = generatedID
        repo.db.collection("notes").document(newNote.noteId).set(newNote)
    }

    fun updateNote(editedTitle: String, editedBody: String) {
        val currentNote = MainActivity.noteItems[MainActivity.currentIndex]

        val item = NoteItem(editedTitle, editedBody, currentNote.noteId)
        MainActivity.noteItems[MainActivity.currentIndex] = item

        repo.db.collection("notes").document(item.noteId).set(item)
    }

    fun deleteNote(position: Int) {
        // Local
        val itemToRemove = MainActivity.noteItems[position]
        MainActivity.noteItems.remove(itemToRemove)

        // Firebase
        repo.db.collection("notes").document(itemToRemove.noteId).delete()
    }

    fun saveImage(image: ImageView) {
        val imageId = MainActivity.noteItems[MainActivity.currentIndex].noteId

        // Get reference to image container and set name as noteID
        val imageRef = storageRef.child(imageId)

        // Convert image to byte
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // Upload byte
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            print("Successfully uploaded image")
        }
    }

    fun downloadImage(imageId: String, imageView: ImageView) {
        val imageRef = storageRef.child(imageId)

        // Max size to avoid crashing app due to memory usage
        val ONE_MEGABYTE: Long = 2048 * 2048
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Sets the passed imageview reference to the downloaded bitmap version of the image
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }.addOnFailureListener {
            print("Failed to download image")
        }
    }

    fun deleteImage(imageId: String) {
        val imageRef = storageRef.child(imageId)

        imageRef.delete()

        // Todo possibly add onSuccess or onFailure listeners
    }

    //////////
    // Function for adding mock data
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
                addNote(NoteItem(it.title, it.body))
            }
        }
    }


}