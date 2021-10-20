package dk.rskovbo.md_android_notebook

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class ListAdapter(private val context: Context, private val dataSource: ArrayList<NoteItem>): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var builder: AlertDialog.Builder

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_item_layout, parent, false)

        val titleTextView = rowView.findViewById<TextView>(R.id.title)
        val bodyTextView = rowView.findViewById<TextView>(R.id.body)

        val movieItem = getItem(position) as NoteItem
        titleTextView.text = movieItem.title
        bodyTextView.text = movieItem.body

        val deleteButton: Button = rowView.findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener {
            MainActivity.deleteNote(position)
        }

        return rowView
    }

}