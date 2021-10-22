package dk.rskovbo.md_android_notebook

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// Todo refactor Firebase to here
class Repo {

    val db = Firebase.firestore
    val storage = Firebase.storage

}