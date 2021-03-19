package com.visionary.noteswithcouchbase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    var rvNotes: RecyclerView? = null

    var fabNewNote: FloatingActionButton? = null

    var swipeLayout: SwipeRefreshLayout? = null

    var tvNoNotes: TextView? = null

    private var adapter: NoteListAdapter? = null
    private var notesList: ArrayList<Note> = ArrayList()
    private var dbOperator: DBOperator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes_list)
        dbOperator = DBOperator.getInstance(this)
        setUpUI()
    }

    private fun setUpUI() {
        fabNewNote = findViewById(R.id.fab_new_note)
        rvNotes = findViewById(R.id.rv_notes)
        swipeLayout = findViewById(R.id.swipe_layout)
        tvNoNotes = findViewById(R.id.no_notes_view)
        onRefresh()
        adapter = NoteListAdapter()
        adapter!!.setNotes(notesList)
        rvNotes?.adapter = adapter
        rvNotes?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvNotes?.layoutDirection = View.LAYOUT_DIRECTION_LTR

        swipeLayout?.setOnRefreshListener { this }

        rvNotes?.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        fabNewNote?.show()
                    } else {
                        fabNewNote?.hide()
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })

        fabNewNote?.setOnClickListener { _ ->
            View.OnClickListener {
                startActivity(Intent(this, New_Note_Activity::class.java))
            }
        }
    }

    override fun onDestroy() {
        dbOperator?.dbManager?.closeDatabaseForUser()
        super.onDestroy()
    }

    override fun onRefresh() {
        notesList = dbOperator!!.getAllNotes()!!
        if (notesList.isEmpty()) {
            tvNoNotes?.visibility = View.VISIBLE
            rvNotes?.visibility = View.GONE
        } else {
            tvNoNotes?.visibility = View.GONE
            rvNotes?.visibility = View.VISIBLE
        }
    }
}