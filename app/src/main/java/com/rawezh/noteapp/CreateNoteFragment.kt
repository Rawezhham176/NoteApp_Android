package com.rawezh.noteapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rawezh.noteapp.database.NoteDatabase
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.rawezh.noteapp.entities.Notes

class CreateNoteFragment : BaseFragment() {
    var currentDate:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = date.format(Date())

        noteDate.text = currentDate

        imgDone.setOnClickListener {
            saveNote()
        }

        imgBack.setOnClickListener {

        }
    }


    private fun saveNote() {
        if(noteTitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "Title Required", Toast.LENGTH_SHORT).show()
        }
        if(noteSubTitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "Sub Title Required", Toast.LENGTH_SHORT).show()
        }
        if(noteText.text.isNullOrEmpty()) {
            Toast.makeText(context, "Text Required", Toast.LENGTH_SHORT).show()
        }

        launch {
            var notes = Notes()
            notes.title = noteTitle.text.toString()
            notes.subTitle = noteSubTitle.text.toString()
            notes.noteText = noteText.text.toString()
            notes.dateTime = currentDate

            context?.let {
                NoteDatabase.getDatabase(it).noteDao().insertNotes(notes)
                noteTitle.setText("")
                noteSubTitle.setText("")
                noteText.setText("")
            }
        }

    }


}