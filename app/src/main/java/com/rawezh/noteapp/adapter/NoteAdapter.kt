package com.rawezh.noteapp.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rawezh.noteapp.R
import com.rawezh.noteapp.entities.Notes
import kotlinx.android.synthetic.main.fragment_create_note.view.*
import kotlinx.android.synthetic.main.item_rv_notes.view.*

class NoteAdapter(private val arrayList: List<Notes>) :
    RecyclerView.Adapter<NoteAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_notes,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.itemView.tvTitle.text = arrayList[position].title
        holder.itemView.tvDesc.text = arrayList[position].noteText
        holder.itemView.tvDateTime.text = arrayList[position].dateTime

        if(arrayList[position].color != null){
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(arrayList[position].color))
        } else {
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor("#171c26"))
        }
        if(arrayList[position].imgPath != null) {
            holder.itemView.imgNote.setImageBitmap(BitmapFactory.decodeFile(arrayList[position].imgPath))
            holder.itemView.imgNote.visibility = View.VISIBLE
        }else {
            holder.itemView.imgNote.visibility = View.GONE
        }
        if(arrayList[position].webLink != null) {
            holder.itemView.Link_Visible_Note1.text = arrayList[position].webLink
            holder.itemView.Link_Visible_Note1.visibility = View.VISIBLE
        }else {
            holder.itemView.Link_Visible_Note1.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }


    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}