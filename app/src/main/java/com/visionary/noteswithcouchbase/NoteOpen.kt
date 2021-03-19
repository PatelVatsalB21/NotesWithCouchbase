package com.visionary.noteswithcouchbase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import butterknife.BindView
import com.couchbase.lite.Document

class NoteOpen : AppCompatActivity() {

    @BindView(R.id.edit_note_input_title)
    var edtTitle: EditText? = null

    @BindView(R.id.edit_note_input_desc)
    var edtDesc: EditText? = null

    @BindView(R.id.edit_note_save_btn)
    var btnSave: Button? = null

    @BindView(R.id.edit_note_cancel_btn)
    var btnBack: ImageButton? = null

    private var dbOperator: DBOperator? = null
    private var id: String = ""
    private var document: Document? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_open_activity)

        if (intent.extras != null) {
            id = intent.getStringExtra("id")!!
            dbOperator = DBOperator.getInstance(this)
            setUpUI()
        }
    }

    private fun setUpUI() {
        document = dbOperator?.getNote(id)
        edtTitle?.setText(document?.getValue("title").toString())
        edtDesc?.setText(document?.getValue("desc").toString())

        btnSave?.setOnClickListener { _ ->
            View.OnClickListener() {
                val note = Note()
                note.title = edtTitle?.text.toString()
                note.desc = edtDesc?.text.toString()
                dbOperator?.updateNote(document?.id!!, note)
                Toast.makeText(this, "Note Saved Successfully", Toast.LENGTH_SHORT).show()
                dbOperator?.dbManager?.closeDatabaseForUser()
                finish()
            }
        }

        btnBack?.setOnClickListener { _ ->
            View.OnClickListener() {
                dbOperator?.dbManager?.closeDatabaseForUser()
                finish()
            }
        }
    }
}