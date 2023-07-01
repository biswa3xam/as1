package com.biswa.krifynote

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.biswa.krifynote.adapter.NotesAdapter
import com.biswa.krifynote.database.NoteDatabase
import com.biswa.krifynote.databinding.ActivityMainBinding
import com.biswa.krifynote.model.Note
import com.biswa.krifynote.model.NotesViewModel

class MainActivity : AppCompatActivity() , NotesAdapter.NotesClickListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
     lateinit var viewModel: NotesViewModel
     lateinit var adapter: NotesAdapter
     lateinit var selectedNote : Note

    val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            val note = result.data?.getSerializableExtra("note") as? Note
            if (note != null){
                viewModel.updateNote(note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()

        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        viewModel.allNotes.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }

        database = NoteDatabase.getDatabase(this)

    }

    fun initUi(){

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
        adapter = NotesAdapter(this,this)
        binding.recyclerView.adapter = adapter
        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if (result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note") as? Note
                if (note != null){
                    viewModel.insertNote(note)
                }
            }
        }

        binding.fpAddNote.setOnClickListener {
            val intent = Intent(this,AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                 return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    adapter.filterList(newText)
                }

                return true
            }
        })
    }

    override fun onItemClick(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    override fun onLongItemClick(note: Note, cardView: CardView) {
        selectedNote  = note
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.menuInflater.inflate(R.menu.pop_up_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.Delete_note -> {
                    viewModel.deleteNote(selectedNote)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

}