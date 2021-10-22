package dk.rskovbo.md_android_notebook

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayOutputStream

// TODO possibly refactor note handling functionality to here
class NoteService {
    private val repo: Repo = Repo()

    fun saveNote(editedTitle: String, editedBody: String) {
        val currentNote = MainActivity.noteItems[MainActivity.currentIndex]

        val item = NoteItem(editedTitle, editedBody, currentNote.noteId)
        MainActivity.noteItems[MainActivity.currentIndex] = item

        repo.db.collection("notes").document(item.noteId).set(item)
    }

    fun saveImage(image: ImageView) {
        val imageId = MainActivity.noteItems[MainActivity.currentIndex].noteId

        // Get reference to image container and set name as noteID
        val storageRef = repo.storage.reference.child("images")
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
        val storageRef = repo.storage.reference.child("images")
        val imageRef = storageRef.child(imageId)

        // Max size to avoid crashing app due to memory usage
        val ONE_MEGABYTE: Long = 1024 * 1024
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            // Sets the passed imageview reference to the downloaded bitmap version of the image
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }.addOnFailureListener {
            print("Failed to download image")
        }
    }

    fun deleteImage() {
        // TODO
    }

    fun deleteNote(position: Int) {
        // Local
        val itemToRemove = MainActivity.noteItems[position]
        MainActivity.noteItems.remove(itemToRemove)

        // Fstore
        repo.db.collection("notes").document(itemToRemove.noteId).delete()
    }


}