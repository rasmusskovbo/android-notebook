package dk.rskovbo.md_android_notebook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import dk.rskovbo.md_android_notebook.model.NoteItem
import dk.rskovbo.md_android_notebook.R
import dk.rskovbo.md_android_notebook.service.NoteService

class ListAdapter(context: Context, private val dataSource: ArrayList<NoteItem>): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val noteService = NoteService()

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Viewholder pattern
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        // Only inflate first time
        if (convertView == null) {

            view = inflater.inflate(R.layout.list_item_layout, parent, false)

            holder = ViewHolder()
            holder.titleTextView = view.findViewById(R.id.title) as TextView
            holder.bodyTextView = view.findViewById(R.id.body) as TextView

            view.tag = holder
        } else {
            // Get the view as created before
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        // Get subviews
        val titleTextView = holder.titleTextView
        val bodyTextView = holder.bodyTextView

        // Set subviews
        val noteItem = getItem(position) as NoteItem
        titleTextView.text = noteItem.title
        bodyTextView.text = noteItem.body

        val deleteButton: Button = view.findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            noteService.deleteNote(position)
            this.notifyDataSetChanged()
        }

        return view
    }

    private class ViewHolder {
        lateinit var titleTextView: TextView
        lateinit var bodyTextView: TextView
    }

}