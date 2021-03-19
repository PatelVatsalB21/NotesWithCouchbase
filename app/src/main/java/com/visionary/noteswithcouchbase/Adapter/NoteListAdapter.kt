package com.visionary.noteswithcouchbase.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.visionary.noteswithcouchbase.Activities.NoteOpen
import com.visionary.noteswithcouchbase.R
import com.visionary.noteswithcouchbase.Utils.Note

class NoteListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var notesList: ArrayList<Note> = ArrayList()
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.note_item, parent, false
        )
        vh = ViewHolder(v)
        context = parent.context
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.tvTitle?.text = notesList[position].title
            holder.tvDesc?.text = notesList[position].desc

            holder.itemLayout!!.setOnClickListener { _ ->
                View.OnClickListener {
                    context!!.startActivity(
                        Intent(context, NoteOpen::class.java).putExtra(
                            "position", notesList[position].id)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setNotes(list: ArrayList<Note>) {
        notesList = list
        notifyDataSetChanged()
    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        @JvmField
        @BindView(R.id.note_item_title)
        var tvTitle: TextView? = null

        @JvmField
        @BindView(R.id.note_item_desc)
        var tvDesc: TextView? = null

        @JvmField
        @BindView(R.id.ll_note_item)
        val itemLayout: LinearLayout? = null

        init {
            ButterKnife.bind(this, v!!)
        }
    }
}