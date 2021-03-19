package com.visionary.noteswithcouchbase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import butterknife.BindView

class New_Note_Activity : AppCompatActivity() {

    @BindView(R.id.new_note_input_title)
    var edtTitle: EditText? = null

    @BindView(R.id.new_note_input_desc)
    var edtDesc: EditText? = null

    @BindView(R.id.new_note_save_btn)
    var btnSave: Button? = null

    @BindView(R.id.new_note_cancel_btn)
    var btnBack: ImageButton? = null

    private var dbOperator: DBOperator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_note_activity)
        dbOperator = DBOperator.getInstance(this)
        setUpUI()
    }

    private fun setUpUI() {
        btnSave?.setOnClickListener { _ ->
            View.OnClickListener() {
                val note = Note()
                note.title = edtTitle?.text.toString()
                note.desc = edtDesc?.text.toString()
                dbOperator?.saveNote(note)
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