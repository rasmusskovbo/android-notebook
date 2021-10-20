package dk.rskovbo.md_android_notebook

class NoteItem(
    var title: String = "",
    var body: String = "",
    var noteId: String = ""
) {
    override fun toString(): String {
        return "MovieItem(title='$title', body='$body', noteId='$noteId')"
    }
}

