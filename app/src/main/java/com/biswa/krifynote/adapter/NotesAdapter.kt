package com.biswa.krifynote.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.biswa.krifynote.R
import com.biswa.krifynote.model.Note
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener: NotesClickListener) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val noteList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun randomColor(): Int {
        val list = ArrayList<Int>()
        list.add(R.color.pink_1)
        list.add(R.color.pink_2)
        list.add(R.color.pink_3)
        list.add(R.color.pink_4)
        list.add(R.color.yellow)
        list.add(R.color.blue)
        list.add(R.color.green)
        return list[Random.nextInt(0, 7)]
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = noteList[position]
        holder.title.text = currentNote.title
        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.title.isSelected = true
        holder.date.isSelected = true
        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor()))

        holder.notes_layout.setOnClickListener {
            listener.onItemClick(noteList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClick(noteList[holder.adapterPosition], holder.notes_layout)
            true
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Note>) {
        fullList.clear()
        fullList.addAll(newList)
        noteList.clear()
        noteList.addAll(newList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(search: String) {
        noteList.clear()
        for (item in fullList) {
            if (item.title?.lowercase()
                    ?.contains(search.lowercase()) == true || item.note?.lowercase()
                    ?.contains(search.lowercase()) == true
            ) {
                noteList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val note = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)

    }

    interface NotesClickListener {
        fun onItemClick(note: Note)
        fun onLongItemClick(note: Note, cardView: CardView)
    }
}